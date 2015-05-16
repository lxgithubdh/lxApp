package com.app.views;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Toast;

import com.app.globals.Globals;
import com.app.naviapp.R;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;

public class RouteResult extends Activity implements OnMapClickListener,
		OnGetRoutePlanResultListener {

	    
	    private MapView mapView = null;
	    private RoutePlanSearch routeSearch = null;
	    private BaiduMap baiduMap;
        private TransitRouteResult routeResult;
        private TransitRouteOverlay tOverlay;

	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.route_result);
	        mapView = (MapView) findViewById(R.id.map);
	        mapView.showZoomControls(false);
	        baiduMap = mapView.getMap();
	        Globals.setMapSetting(baiduMap);
	        baiduMap.setOnMapClickListener(this);
	        routeSearch = RoutePlanSearch.newInstance();
	        routeSearch.setOnGetRoutePlanResultListener(this);
	        searchRoute();
	    }

	    
	    private void searchRoute() {
	        baiduMap.clear();
	        Bundle msg = RouteResult.this.getIntent().getExtras();
	        PlanNode stNode = PlanNode.withCityNameAndPlaceName(Globals.CITY, msg.getString("start"));
	        PlanNode enNode = PlanNode.withCityNameAndPlaceName(Globals.CITY, msg.getString("end"));

	        int method = msg.getInt("method");
	        switch (method) {
			case 0:
				routeSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
				break;
			case 1:
				routeSearch.transitSearch((new TransitRoutePlanOption()).from(stNode).city(Globals.CITY)
	                    .to(enNode));
				break;
			case 2:
				routeSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
				break;
			default:
				break;
			}
	    }

	    @Override
	    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	        super.onRestoreInstanceState(savedInstanceState);
	    }

	    @Override
	    public void onGetWalkingRouteResult(WalkingRouteResult result) {
	        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	            Toast.makeText(RouteResult.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
	        }
	        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	            return;
	        }
	        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	            WalkingRouteOverlay overlay = new MyWalkingRouteOverlay(baiduMap);
	            baiduMap.setOnMarkerClickListener(overlay);
	            overlay.setData(result.getRouteLines().get(0));
	            overlay.addToMap();
	            overlay.zoomToSpan();
	        }

	    }

	    @Override
	    public void onGetTransitRouteResult(TransitRouteResult result) {

	        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	            Toast.makeText(RouteResult.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
	        }
	        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	            return;
	        }
	        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	        	routeResult = result;
	            tOverlay = new MyTransitRouteOverlay(baiduMap);
	            baiduMap.setOnMarkerClickListener(tOverlay);
	            List<TransitRouteLine> list = result.getRouteLines();
	            int length = list.size();
	            String[] lines = new String[length];
	            for(int i=0 ; i<length ; i++){
	            	lines[i] = "路线" + (i+1);
	            }
	            Builder builder = new AlertDialog.Builder(RouteResult.this);
	            builder.setTitle("选择公交路线");
	            builder.setSingleChoiceItems(lines, 0, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						tOverlay.setData(routeResult.getRouteLines().get(arg1));
			            tOverlay.addToMap();
			            tOverlay.zoomToSpan();
						arg0.dismiss();
					}
				});
	            builder.setNegativeButton("取消", null);
	            builder.create().show();
	            
	        }
	    }

	    @Override
	    public void onGetDrivingRouteResult(DrivingRouteResult result) {
	        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
	            Toast.makeText(RouteResult.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
	        }
	        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
	            return;
	        }
	        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
	            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(baiduMap);
	            baiduMap.setOnMarkerClickListener(overlay);
	            overlay.setData(result.getRouteLines().get(0));
	            overlay.addToMap();
	            overlay.zoomToSpan();
	        }
	    }

	    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

	        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
	            super(baiduMap);
	        }
	    }

	    private class MyWalkingRouteOverlay extends WalkingRouteOverlay {

	        public MyWalkingRouteOverlay(BaiduMap baiduMap) {
	            super(baiduMap);
	        }
	    }

	    private class MyTransitRouteOverlay extends TransitRouteOverlay {

	        public MyTransitRouteOverlay(BaiduMap baiduMap) {
	            super(baiduMap);
	        }
	    }

	    @Override
	    public void onMapClick(LatLng point) {
	        baiduMap.hideInfoWindow();
	    }

	    @Override
	    public boolean onMapPoiClick(MapPoi poi) {
	    	return false;
	    }

	    @Override
	    protected void onPause() {
	        mapView.onPause();
	        super.onPause();
	    }

	    @Override
	    protected void onResume() {
	        mapView.onResume();
	        super.onResume();
	    }

	    @Override
	    protected void onDestroy() {
	        routeSearch.destroy();
	        mapView.onDestroy();
	        super.onDestroy();
	    }

}
