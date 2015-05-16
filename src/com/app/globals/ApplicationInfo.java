package com.app.globals;

import com.baidu.mapapi.SDKInitializer;

import android.app.Application;

public class ApplicationInfo extends Application{

	public void onCreate(){
		super.onCreate();
		SDKInitializer.initialize(this);
	}
	
}
