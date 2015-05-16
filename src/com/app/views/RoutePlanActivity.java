package com.app.views;

import com.app.naviapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class RoutePlanActivity extends Activity {

    private EditText start;
    private EditText end;
    private int method;
	
	
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.route_plan_activity);
		start = (EditText)findViewById(R.id.start);
		end = (EditText)findViewById(R.id.end);
		RadioGroup group = (RadioGroup)findViewById(R.id.selectMethod);
		Button btnSearch = (Button)findViewById(R.id.btnSearch);
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// TODO Auto-generated method stub
				switch(arg1){
				case R.id.drive:
					method = 0;
					break;
				case R.id.transit:
					method = 1;
					break;
				case R.id.walk:
					method = 2;
					break;
					default:
						break;
				}
			}
		});
		
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(RoutePlanActivity.this,RouteResult.class);
				Bundle msg = new Bundle();
				String startStr = start.getText().toString();
				String endStr = end.getText().toString();
				if(startStr.equals("")||startStr.equals(" ")||endStr.equals("")||endStr.equals(" ")){
					Toast.makeText(RoutePlanActivity.this, "起点或终点不能为空！", Toast.LENGTH_SHORT).show();
					return;
				}
				msg.putString("start", startStr);
				msg.putString("end", endStr);
				msg.putInt("method", method);
				intent.putExtras(msg);
				startActivity(intent);
			}
		});
		
	}
}
