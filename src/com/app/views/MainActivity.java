package com.app.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.naviapp.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity {

	private GridView gridView;
	private int[] titles = new int[]{R.string.location,
			R.string.bussearch,R.string.routeguide,
			R.string.poisearch,R.string.setting,
			R.string.download
	};
	private int[] images = new int[]{R.drawable.location,
			R.drawable.bussearch,R.drawable.routeguide,
			R.drawable.poisearch,R.drawable.setting,
			R.drawable.download
	};
	
	
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.main_activity);
		gridView = (GridView)findViewById(R.id.gvInfo);
		SimpleAdapter adapter = new SimpleAdapter(this, generateList(), 
				R.layout.item_layout, new String[]{"title","image"},
				new int[]{R.id.itemTitle,R.id.itemImage});
		if(!checkNetwork()){
			Toast.makeText(this, "无法连接网络，请检查网络是否打开", Toast.LENGTH_SHORT).show();
		}
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				// TODO Auto-generated method stub
				Intent intent = null;
				switch (position) {
				case 0:
					intent = new Intent(MainActivity.this, LocationActivity.class);
					break;
				case 1:
					intent = new Intent(MainActivity.this, BusLineSearchActivity.class);
					break;
				case 2:
					intent = new Intent(MainActivity.this, RoutePlanActivity.class);
					break;
				case 3:
					intent = new Intent(MainActivity.this, PoiSearchActivity.class);
					break;
				case 4:
					intent = new Intent(MainActivity.this, SettingActivity.class);
					break;
				case 5:
					intent = new Intent(MainActivity.this,DownloadActivity.class);
				default:
					break;
				}
				startActivity(intent);
			}
		});
	}
	
	
	private List<? extends Map<String, ?>> generateList(){
		ArrayList<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		int count = images.length;
		for(int i=0;i<count;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("title", getString(titles[i]));
			map.put("image", images[i]);
			list.add(map);
		}
		return list;
	}
	
	private boolean checkNetwork(){
		ConnectivityManager manager = (ConnectivityManager)MainActivity
				.this.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		return (info!=null&&info.isConnected());
	}
	
	protected void onDestory(){
		super.onDestroy();
	}
}
