package com.app.views;

import com.app.globals.Globals;
import com.app.naviapp.R;
import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingActivity extends Activity {

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		CheckBox zoom = (CheckBox)findViewById(R.id.zoom);
		CheckBox scroll = (CheckBox)findViewById(R.id.scroll);
		CheckBox rotate = (CheckBox)findViewById(R.id.rotate);
		CheckBox overlook = (CheckBox)findViewById(R.id.overlook);
		CheckBox compass = (CheckBox)findViewById(R.id.compass);
		CheckBox satellite = (CheckBox)findViewById(R.id.satellite);
		
		zoom.setChecked(Globals.MapStatuArr[0]);
		scroll.setChecked(Globals.MapStatuArr[1]);
		rotate.setChecked(Globals.MapStatuArr[2]);
		overlook.setChecked(Globals.MapStatuArr[3]);
		compass.setChecked(Globals.MapStatuArr[4]);
		satellite.setChecked(Globals.MapStatuArr[5]);
		
		zoom.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Globals.MapStatuArr[0] = arg1;
			}
		});
		
		scroll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Globals.MapStatuArr[1] = arg1;
			}
		});
		
		rotate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Globals.MapStatuArr[2] = arg1;
			}
		});
		
		overlook.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Globals.MapStatuArr[3] = arg1;
			}
		});
		
		compass.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Globals.MapStatuArr[4] = arg1;
			}
		});
		
		satellite.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				Globals.MapStatuArr[5] = arg1;
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
		super.onDestroy();
	}
}
