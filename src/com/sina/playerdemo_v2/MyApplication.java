package com.sina.playerdemo_v2;

import com.sina.sinavideo.sdk.utils.VDApplication;
import com.sina.sinavideo.sdk.utils.VDResolutionManager;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

		// 播放器初始化，要在app启动前进行初始化，才能解压出相应的解码器
		VDApplication.getInstance().initPlayer(this);
		VDResolutionManager.getInstance(this).init(
				VDResolutionManager.RESOLUTION_SOLUTION_NONE);
	}
}
