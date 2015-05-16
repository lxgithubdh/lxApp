package com.app.views;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.globals.Globals;
import com.app.naviapp.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.BusLineOverlay;
import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

public class BusLineSearchActivity extends FragmentActivity implements
		OnGetPoiSearchResultListener,OnGetBusLineSearchResultListener,
		OnMapClickListener {

	private List<String> busLineList = null;
	private int busLineIndex = 0;
	private PoiSearch poiSearch = null;
	private BusLineSearch busLineSearch = null;
	private BaiduMap baiduMap = null;
	
	
	protected void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.bus_search_activity);
		SupportMapFragment fragment = ((SupportMapFragment)getSupportFragmentManager()
				.findFragmentById(R.id.bmapView));
		baiduMap = fragment.getBaiduMap();
		fragment.getMapView().showZoomControls(false);
		Globals.setMapSetting(baiduMap);
		baiduMap.setOnMapClickListener(this);
		poiSearch = PoiSearch.newInstance();
		poiSearch.setOnGetPoiSearchResultListener(this);
		busLineSearch = BusLineSearch.newInstance();
		busLineSearch.setOnGetBusLineSearchResultListener(this);
		busLineList = new ArrayList<String>();
	}
	
	public void searchButtonProcess(View v){
		busLineList.clear();
		busLineIndex = 0;
		String temp = ((EditText)findViewById(R.id.routeNum)).getText().toString();
		if(temp.equals("")||temp.equals(" ")){
			Toast.makeText(BusLineSearchActivity.this, "路线不能为空！", Toast.LENGTH_SHORT).show();
			return;
		}
		poiSearch.searchInCity((new PoiCitySearchOption()).city
				(Globals.CITY).keyword(temp));
	}
	
	public void reverseBusline(View v){
		if(busLineIndex>=busLineList.size()){
			busLineIndex = 0;
		}
		if(busLineIndex>=0&&
				busLineIndex<busLineList.size()&&
				busLineList.size()>0){
			busLineSearch.searchBusLine((new BusLineSearchOption().
					city(Globals.CITY).uid(busLineList.get(busLineIndex))));
			busLineIndex++;
		}
	}
	
	
	protected void onPause(){
		super.onPause();
	}
	
	protected void onResume(){
		super.onResume();
	}
	
	protected void onDestroy(){
		poiSearch.destroy();
		busLineSearch.destroy();
		super.onDestroy();
	}
	
	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		// TODO Auto-generated method stub
        if(result==null||result.error!=SearchResult.ERRORNO.NO_ERROR){
        	Toast.makeText(BusLineSearchActivity.this, "抱歉，未找到结果", 
        			Toast.LENGTH_LONG).show();
        	return;
        }
        busLineList.clear();
        for(PoiInfo poi:result.getAllPoi()){
        	if(poi.type==PoiInfo.POITYPE.BUS_LINE||
        			poi.type==PoiInfo.POITYPE.SUBWAY_LINE){
        		busLineList.add(poi.uid);
        	}
        }
        reverseBusline(null);
	}

	@Override
	public void onGetBusLineResult(BusLineResult result) {
		// TODO Auto-generated method stub
		if(result==null||result.error!=SearchResult.ERRORNO.NO_ERROR){
			Toast.makeText(BusLineSearchActivity.this, "抱歉，未找到结果", 
					Toast.LENGTH_LONG).show();
			return;
		}
		baiduMap.clear();
		BusLineOverlay overlay = new BusLineOverlay(baiduMap);
		baiduMap.setOnMarkerClickListener(overlay);
		overlay.setData(result);
		overlay.addToMap();
		overlay.zoomToSpan();
		Toast.makeText(BusLineSearchActivity.this, 
				result.getBusLineName(), Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onMapClick(LatLng arg0) {
		// TODO Auto-generated method stub
		baiduMap.hideInfoWindow();
	}

	@Override
	public boolean onMapPoiClick(MapPoi arg0) {
		return false;
	}

}
