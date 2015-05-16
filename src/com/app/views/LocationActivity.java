package com.app.views;

import com.app.globals.Globals;
import com.app.naviapp.R;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LocationActivity extends Activity {

	
	private LocationClient locationClient;
	private MyLocationListener myListener = new MyLocationListener();
	private LocationMode currentMode;
	private BitmapDescriptor currentMarker;
	private MapView mapView;
	private BaiduMap baiduMap;
	private Button modeButton;
	private boolean isFirst = true;
	private SensorManager manager;
	private Sensor sensor;
	private float orientation;
	private SensorEventListener listener;
	
	public static LatLng curLocation = null;
	
	
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.location_activity);
		modeButton = (Button)findViewById(R.id.btnMode);
		currentMode = LocationMode.NORMAL;
		modeButton.setText("ÆÕÍ¨");
		mapView = (MapView)findViewById(R.id.bmapView);
		mapView.showZoomControls(false);
		baiduMap = mapView.getMap();
		Globals.setMapSetting(baiduMap);
		baiduMap.setMyLocationEnabled(true);
		locationClient = new LocationClient(this);
		locationClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);
		option.setCoorType("bd09ll");
		option.setScanSpan(1000);
		locationClient.setLocOption(option);
		locationClient.start();
		manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
		sensor = manager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		listener=new SensorEventListener() {
			
			@Override
			public void onSensorChanged(SensorEvent arg0) {
				// TODO Auto-generated method stub
				orientation = arg0.values[0];
			}
			
			@Override
			public void onAccuracyChanged(Sensor arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		};
		manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		
		
		modeButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				switch(currentMode){
				case NORMAL:
					modeButton.setText("¸úËæ");
					currentMode = LocationMode.FOLLOWING;
					baiduMap.setMyLocationConfigeration(
							new MyLocationConfiguration(currentMode,
									true, currentMarker));
					break;
				case COMPASS:
					modeButton.setText("ÆÕÍ¨");
					currentMode = LocationMode.NORMAL;
					baiduMap.setMyLocationConfigeration(
							new MyLocationConfiguration(currentMode, 
									true, currentMarker));
					break;
				case FOLLOWING:
					modeButton.setText("ÂÞÅÌ");
					currentMode = LocationMode.COMPASS;
					baiduMap.setMyLocationConfigeration(
							new MyLocationConfiguration(currentMode, 
									true, currentMarker));
					break;
				}
			}
		});
		
	}
	
	public class MyLocationListener implements BDLocationListener{

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			if(location==null||mapView==null){
				return;
			}
			MyLocationData data = new MyLocationData.Builder()
			.accuracy(location.getRadius()).direction(orientation).latitude(
					location.getLatitude()).longitude(location
							.getLongitude()).build();
			baiduMap.setMyLocationData(data);
			if(isFirst){
				isFirst = false;
				curLocation = new LatLng(location.getLatitude(), 
						location.getLongitude());
				MapStatusUpdate update = MapStatusUpdateFactory
						.newLatLng(curLocation);
				baiduMap.animateMapStatus(update);
			}
		}
		
		public void onReceivePoi(BDLocation location){
			
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		locationClient.stop();
		baiduMap.setMyLocationEnabled(false);
		mapView.onDestroy();
		mapView = null;
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		manager.unregisterListener(listener);
		mapView.onPause();
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		manager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		mapView.onResume();
		super.onResume();
	}
	
}
