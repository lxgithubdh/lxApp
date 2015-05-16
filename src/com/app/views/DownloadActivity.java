package com.app.views;

import java.io.File;

import com.app.naviapp.R;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

public class DownloadActivity extends Activity implements MKOfflineMapListener{

	private boolean isFirst;
	private Button startBtn;
	private Button stopBtn;
	private Button restartBtn;
	private Button deleteBtn;
	private Button updateBtn;
	private ProgressBar progressBar;
	private MKOfflineMap map;
	private int cityId = 218;
	
	
	public void onCreate(Bundle bundle){
		super.onCreate(bundle);
		setContentView(R.layout.download_activity);
		map = new MKOfflineMap();
		map.init(this);
		startBtn = (Button)findViewById(R.id.downloadBtn);
		stopBtn = (Button)findViewById(R.id.stop);
		restartBtn = (Button)findViewById(R.id.restart);
		deleteBtn = (Button)findViewById(R.id.delete);
		updateBtn = (Button)findViewById(R.id.update);
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		isFirst = !(new File(Environment.getExternalStorageDirectory().getPath()
				+"/BaiduMapSDK/vmp/h/wuhan_218.dat").exists());
		
		init();
		
		startBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				progressBar.setVisibility(View.VISIBLE);
				stopBtn.setVisibility(View.VISIBLE);
				restartBtn.setVisibility(View.VISIBLE);
				deleteBtn.setVisibility(View.VISIBLE);
				map.start(cityId);
				Toast.makeText(DownloadActivity.this, "开始下载离线地图.", Toast.LENGTH_SHORT)
						.show();
			}
		});
		
		stopBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				map.pause(cityId);
				Toast.makeText(DownloadActivity.this, "暂停下载离线地图.", Toast.LENGTH_SHORT)
						.show();
			}
		});
		
		restartBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				map.remove(cityId);
				map.start(cityId);
				Toast.makeText(DownloadActivity.this, "开始下载离线地图.", Toast.LENGTH_SHORT)
						.show();
			}
		});
		
		deleteBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				map.remove(cityId);
				isFirst = true;
				Toast.makeText(DownloadActivity.this, "删除离线地图.", Toast.LENGTH_SHORT)
						.show();
			}
		});
		
		updateBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MKOLUpdateElement update = map.getUpdateInfo(cityId);
				if(update.update){
					progressBar.setVisibility(View.VISIBLE);
					stopBtn.setVisibility(View.VISIBLE);
					restartBtn.setVisibility(View.VISIBLE);
					map.remove(cityId);
					map.start(cityId);
				}else{
					Toast.makeText(DownloadActivity.this, "已经是最新版本！", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}


	private void init() {
		// TODO Auto-generated method stub
		if(isFirst){
			startBtn.setVisibility(View.VISIBLE);
			stopBtn.setVisibility(View.INVISIBLE);
			restartBtn.setVisibility(View.INVISIBLE);
			deleteBtn.setVisibility(View.INVISIBLE);
			updateBtn.setVisibility(View.INVISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
		}else{
			startBtn.setVisibility(View.INVISIBLE);
			stopBtn.setVisibility(View.INVISIBLE);
			restartBtn.setVisibility(View.INVISIBLE);
			deleteBtn.setVisibility(View.VISIBLE);
			updateBtn.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.INVISIBLE);
		}
	}


	@Override
	public void onGetOfflineMapState(int type, int state) {
		// TODO Auto-generated method stub
		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: {
			MKOLUpdateElement download = map.getUpdateInfo(state);
			if (download != null) {
				progressBar.setProgress(download.ratio);
				}
			
			if(download.ratio==100){
				isFirst = false;
				Toast.makeText(DownloadActivity.this, "下载完成！", Toast.LENGTH_SHORT).show();
				map.importOfflineData();
			}
		}
			break;
		case MKOfflineMap.TYPE_NEW_OFFLINE:
			Toast.makeText(DownloadActivity.this, "离线地图安装成功！", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(DownloadActivity.this,MainActivity.class);
			DownloadActivity.this.startActivity(intent);
			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
			MKOLUpdateElement element = map.getUpdateInfo(state);
			if(element!=null){
				progressBar.setProgress(element.ratio);
			}
			break;
		}

	}

	
	@Override
	protected void onPause() {
		MKOLUpdateElement temp = map.getUpdateInfo(cityId);
		if (temp != null && temp.status == MKOLUpdateElement.DOWNLOADING) {
			map.pause(cityId);
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		map.destroy();
		super.onDestroy();
	}
	
}
