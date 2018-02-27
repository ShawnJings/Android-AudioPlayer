package com.timeapp.shawn.voiceplay;


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.timeapp.shawn.voiceplay.audiotrack.NativeMp3PlayerController;
import com.timeapp.shawn.voiceplay.opensles.SoundTrackController;
import com.timeapp.shawn.voiceplay.utils.LogUtils;
import com.timeapp.shawn.voiceplay.utils.SDCardUtils;

import java.io.File;

public class MainActivity extends Activity {

	static {
		System.loadLibrary("libsongstudio");
	}
	private static String TAG = "MainActivity";
	
	private Button audioTrackPlayBtn;
	private Button audioTrackStopBtn;
	private Button openSLESPlayBtn;
	private Button openSLESStopBtn;
	/** 要播放的文件路径 **/
	private static String playFilePath;

	private static final String[] permissionManifest = {
			Manifest.permission.READ_EXTERNAL_STORAGE
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		playFilePath = SDCardUtils.getExternalSdCardPath(this) + File.separator + "aotu.mp3";
//		playFilePath = SDCardUtils.getExternalSdCardPath(this) + File.separator + "test.wav";
		LogUtils.e("Voice", "playFilePath = " + playFilePath);
		findView();
		bindListener();

		permissionCheck();
	}

	private void findView() {
		audioTrackPlayBtn = (Button) findViewById(R.id.play_audiotrack_btn);
		audioTrackStopBtn = (Button) findViewById(R.id.stop_audiotrack_btn);
		openSLESPlayBtn = (Button) findViewById(R.id.play_opensl_es_btn);
		openSLESStopBtn = (Button) findViewById(R.id.stop_opensl_es_btn);
	}
	
	private void bindListener() {
		audioTrackPlayBtn.setOnClickListener(audioTrackPlayBtnListener);
		audioTrackStopBtn.setOnClickListener(audioTrackStopBtnListener);
		openSLESPlayBtn.setOnClickListener(openSLESPlayBtnListener);
		openSLESStopBtn.setOnClickListener(openSLESStopBtnListener);
	}
	
	private NativeMp3PlayerController audioTrackPlayerController;
	OnClickListener audioTrackPlayBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.i(TAG, "Click AudioTrack Play Btn");
			audioTrackPlayerController = new NativeMp3PlayerController();
			audioTrackPlayerController.setHandler(handler);
			audioTrackPlayerController.setAudioDataSource(playFilePath);
			audioTrackPlayerController.start();
		}
	};
	
	OnClickListener audioTrackStopBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.i(TAG, "Click AudioTrack Stop Btn");
			// 普通AudioTrack的停止播放
			if (null != audioTrackPlayerController) {
				audioTrackPlayerController.stop();
				audioTrackPlayerController = null;
			}
		}
	};
	
	private SoundTrackController openSLPlayerController;
	OnClickListener openSLESPlayBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.i(TAG, "Click OpenSL ES Play Btn");
			// OpenSL EL初始化播放器
			openSLPlayerController = new SoundTrackController();
			boolean isError = openSLPlayerController.setAudioDataSource(playFilePath, 0.2f);
			if (isError) {
				LogUtils.e("Voice", "openSLPlayerController init error");
			} else {
				// OpenSL EL进行播放
				openSLPlayerController.play();
			}

		}
	};
	
	OnClickListener openSLESStopBtnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Log.i(TAG, "Click OpenSL ES Stop Btn");
			if (null != openSLPlayerController) {
				openSLPlayerController.stop();
				openSLPlayerController = null;
			}
		}
	};
	
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 计算当前时间
			int _time = Math.max(msg.arg1, 0) / 1000;
//			int total_time = Math.max(msg.arg2, 0) / 1000;
			int total_time = Math.max(msg.arg2, 0);

			float ratio = 0;
			if (total_time > 0) {
				ratio = (float) _time / (float) total_time;
			}
			Log.i(TAG, "Play Progress : " + ratio);
		}
	};

	private void playMusic() {
		File file = new File(playFilePath);
		if (file.exists()) {
			LogUtils.e("Voice", "exists");
		} else {
			LogUtils.e("Voice", "not exists");
		}
	}

	public void showToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	}

	private final int PERMISSION_REQUEST_CODE = 0x001;

	private void permissionCheck() {
		LogUtils.e("Permission", "permissionCheck begin");
		if (Build.VERSION.SDK_INT >= 23) {

			LogUtils.e("Permission", "SDK_INT >= 23");
			boolean permissionState = true;
			for (String permission : permissionManifest) {
				if (ContextCompat.checkSelfPermission(this, permission)
						!= PackageManager.PERMISSION_GRANTED) {
					permissionState = false;
				}
			}
			if (!permissionState) {
				LogUtils.e("Permission", "permissionState false");
				ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
			} else {
				LogUtils.e("Permission", "permissionState true");
				playMusic();
			}
		} else {
			LogUtils.e("Permission", "SDK_INT < 23");

			playMusic();
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (requestCode == PERMISSION_REQUEST_CODE) {
			boolean isGrant = true;

			LogUtils.e("Permission", "onRequestPermissionsResult");

			for (int i = 0; i < permissions.length; i++) {
				LogUtils.e("Video", "permission: " + permissions[i] + " = " + grantResults[i]);
				if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
					isGrant = false;
				}
			}
			if (isGrant) {
				playMusic();
			}
		}
	}
}
