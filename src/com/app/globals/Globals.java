package com.app.globals;

import com.app.views.LocationActivity;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;

/**
*全局数据
*/
public class Globals {

	/**
	 * 默认城市
	 */
	public static final String CITY = "武汉";
	public static boolean[] MapStatuArr={true,true,true,true,false,false};
	
	
	
	public static void setMapSetting(BaiduMap baiduMap){
		UiSettings uiSettings = baiduMap.getUiSettings();
		MapStatus.Builder builder = new MapStatus.Builder().overlook(-30);
		builder.zoom(15);
		if(LocationActivity.curLocation!=null){
			builder.target(LocationActivity.curLocation);
		}else{
			builder.target(new LatLng(30.525099,114.360492));
		}
		MapStatus ms = builder.build();
		MapStatusUpdate u = MapStatusUpdateFactory.newMapStatus(ms);
		baiduMap.animateMapStatus(u, 1000);
		uiSettings.setZoomGesturesEnabled(MapStatuArr[0]);
		uiSettings.setScrollGesturesEnabled(MapStatuArr[1]);
		uiSettings.setRotateGesturesEnabled(MapStatuArr[2]);
		uiSettings.setOverlookingGesturesEnabled(MapStatuArr[3]);
		uiSettings.setCompassEnabled(MapStatuArr[4]);
		if(MapStatuArr[5]){
			baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
		}else{
			baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		}
	}
	
}
