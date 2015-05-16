package com.app.views;

import java.util.HashMap;
import java.util.Map;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.globals.Globals;
import com.app.naviapp.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.PoiDetailShareURLOption;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;

public class PoiSearchActivity extends FragmentActivity implements
		OnGetPoiSearchResultListener, OnGetSuggestionResultListener,
		OnGetShareUrlResultListener{

	private PoiSearch poiSearch = null;
	private SuggestionSearch suggestionSearch = null;
	private ShareUrlSearch shareUrlSearch = null;
	private BaiduMap baiduMap = null;
	private Map<String, LatLng> locationMap = new HashMap<String, LatLng>();
	private AutoCompleteTextView keyWorldsView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index = 0;
	private LatLng curLocation = null;
	private boolean isFirst = true;
	private Spinner spinner = null;
	private Button btnSearch = null;
	private String curAddress;
	private String curName;
	private PoiInfo poiInfo;
	private MyPoiOverlay overlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.poi_search_activity);
		
		poiSearch = PoiSearch.newInstance();
		poiSearch.setOnGetPoiSearchResultListener(this);
		suggestionSearch = SuggestionSearch.newInstance();
		suggestionSearch.setOnGetSuggestionResultListener(this);
		shareUrlSearch = ShareUrlSearch.newInstance();
		shareUrlSearch.setOnGetShareUrlResultListener(this);
		locationMap.put("升升公寓", new LatLng(30.511072,114.349548));
		locationMap.put("南湖校区", new LatLng(30.514296,114.339654));
		locationMap.put("鉴湖校区", new LatLng(30.519324,114.349372));
		locationMap.put("西院", new LatLng(30.528735,114.354935));
		locationMap.put("东院", new LatLng(30.525099,114.360492));
		locationMap.put("余家头校区", new LatLng(30.613097,114.362342));
		locationMap.put("当前位置", LocationActivity.curLocation);
		keyWorldsView = (AutoCompleteTextView) findViewById(R.id.searchkey);
		spinner = (Spinner)findViewById(R.id.location);
		btnSearch = (Button)findViewById(R.id.search);
		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		keyWorldsView.setAdapter(sugAdapter);
		SupportMapFragment fragment = ((SupportMapFragment)getSupportFragmentManager()
				.findFragmentById(R.id.map));
		fragment.getMapView().showZoomControls(false);
		baiduMap = fragment.getBaiduMap();
		Globals.setMapSetting(baiduMap);
		overlay = new MyPoiOverlay(baiduMap);
		baiduMap.setOnMarkerClickListener(overlay);

		
		keyWorldsView.addTextChangedListener(new TextWatcher() {

			@Override
			public void afterTextChanged(Editable arg0) {
                 isFirst = true;
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {

			}

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (cs.length() <= 0) {
					return;
				}
				
				if(curLocation!=null){
					suggestionSearch.requestSuggestion(
							(new SuggestionSearchOption()).keyword(
									cs.toString()).location(curLocation).city(Globals.CITY));
				}
				
			}
		});
		
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String value = arg0.getItemAtPosition(arg2).toString();
				curLocation = locationMap.get(value);
				isFirst = true;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(curLocation==null){
					Toast.makeText(PoiSearchActivity.this, "请先定位！", Toast.LENGTH_LONG).show();
				}
				String temp = keyWorldsView.getText().toString();
				if(temp.equals("")||temp.equals(" ")){
					Toast.makeText(PoiSearchActivity.this, "请输入关键字！", Toast.LENGTH_SHORT).show();
					return;
				}
				if(isFirst){
					load_Index = 0;
					searchButtonProcess();
					isFirst = !isFirst;
				}else{
					goToNext();
				}
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		poiSearch.destroy();
		suggestionSearch.destroy();
		shareUrlSearch.destroy();
		super.onDestroy();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}

	
	public void searchButtonProcess() {
		poiSearch.searchNearby((new PoiNearbySearchOption())
				.location(curLocation)
				.radius(5000)
				.keyword(keyWorldsView.getText().toString())
				.pageNum(load_Index));
	}

	public void goToNext() {
		load_Index++;
		searchButtonProcess();
	}

	public void onGetPoiResult(PoiResult result) {
		if (result == null
				|| result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			Toast.makeText(PoiSearchActivity.this, "未找到结果", Toast.LENGTH_LONG)
			.show();
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			baiduMap.clear();
			overlay.setData(result);
			overlay.addToMap();
			overlay.zoomToSpan();
			return;
		}
	}

	public void onGetPoiDetailResult(PoiDetailResult result) {
		if (result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(PoiSearchActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(PoiSearchActivity.this, result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
			.show();
		}
	}

	@Override
	public void onGetSuggestionResult(SuggestionResult res) {
		if (res == null || res.getAllSuggestions() == null) {
			return;
		}
		sugAdapter.clear();
		for (SuggestionResult.SuggestionInfo info : res.getAllSuggestions()) {
			if (info.key != null)
				sugAdapter.add(info.key);
		}
		sugAdapter.notifyDataSetChanged();
	}

	private class MyPoiOverlay extends PoiOverlay {

		public MyPoiOverlay(BaiduMap baiduMap) {
			super(baiduMap);
		}

		@Override
		public boolean onPoiClick(int index) {
			super.onPoiClick(index);
			poiInfo = getPoiResult().getAllPoi().get(index);
			curName = poiInfo.name;
			curAddress = poiInfo.address;
			Builder builder = new AlertDialog.Builder(PoiSearchActivity.this);
			builder.setTitle("详情");
			LayoutInflater inflater = (LayoutInflater)PoiSearchActivity.this.getLayoutInflater();
			View layout = inflater.inflate(R.layout.dialog_content, null);
			TextView name = (TextView)layout.findViewById(R.id.name);
			TextView address = (TextView)layout.findViewById(R.id.address);
			name.setText("名称:"+curName);
			address.setText("地址:"+curAddress);
			builder.setView(layout);
			builder.setPositiveButton("分享", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					shareUrlSearch.requestPoiDetailShareUrl(new PoiDetailShareURLOption()
							.poiUid(poiInfo.uid));
				}
			});
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					arg0.dismiss();
				}
				
			});
			builder.create().show();
			return true;
		}
	}

	@Override
	public void onGetLocationShareUrlResult(ShareUrlResult result) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetPoiDetailShareUrlResult(ShareUrlResult result) {
		// TODO Auto-generated method stub
		Intent it = new Intent(Intent.ACTION_SEND);
		it.putExtra(Intent.EXTRA_TEXT, "您的朋友向您分享一个位置: " + curAddress
				+ " -- " + result.getUrl());
		it.setType("text/plain");
		startActivity(Intent.createChooser(it, "将短串分享到"));
	}
	
	
}
