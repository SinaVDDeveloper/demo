package com.sina.playerdemo_v2.activity;

import com.sina.playerdemo_v2.R;
import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoFrameADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoInsertADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoPlaylistListener;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.sina.sinavideo.sdk.widgets.playlist.VDVideoPlayListView;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

public class OnlyHorizonActivity extends Activity implements
		OnVDVideoFrameADListener, OnVDVideoInsertADListener,
		OnVDVideoPlaylistListener {

	private VDVideoView mVDVideoView = null;
	private final static String TAG = "Test1Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.only_horizon);
		mVDVideoView = (VDVideoView) findViewById(R.id.vv1);
		mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView
				.getParent());

		VDVideoListInfo infoList = new VDVideoListInfo();
		VDVideoInfo info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频0";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131396797&tags=videoapp_android";
		infoList.addVideoInfo(info);

		info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频1";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
		infoList.addVideoInfo(info);

		info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频2";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
		infoList.addVideoInfo(info);

		info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频3";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
		infoList.addVideoInfo(info);

		registerListener();

		// 简单方式处理的视频列表
		VDVideoPlayListView listView = (VDVideoPlayListView) findViewById(R.id.play_list_view);
		if (listView != null) {
			listView.onVideoList(infoList);
		}
		mVDVideoView.open(OnlyHorizonActivity.this, infoList);
		mVDVideoView.play(0);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mVDVideoView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mVDVideoView.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (!mVDVideoView.onVDKeyDown(keyCode, event)) {
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	private void registerListener() {
		mVDVideoView.setFrameADListener(this);
		mVDVideoView.setInsertADListener(this);
		mVDVideoView.setPlaylistListener(this);
	}

	// private void openVideo(VDVideoInfo info, int p) {
	// mVDVideoView.stop();
	// mVDVideoView.release(true);
	// mVDVideoView.open(this, info);
	// mVDVideoView.play(p);
	// }

	@Override
	protected void onStop() {
		super.onStop();
		mVDVideoView.onStop();
	}

	@Override
	protected void onDestroy() {
		mVDVideoView.release(false);
		super.onDestroy();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			mVDVideoView.setIsFullScreen(true);
			LogS.e(VDVideoFullModeController.TAG, "onConfigurationChanged---横屏");
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			mVDVideoView.setIsFullScreen(false);
			LogS.e(VDVideoFullModeController.TAG, "onConfigurationChanged---竖屏");
		}

	}

	@Override
	public void onPlaylistClick(VDVideoInfo info, int p) {
		// TODO Auto-generated method stub
		if (info == null) {
			LogS.e(TAG, "info is null");
		}
		mVDVideoView.play(p);
	}

	@Override
	public void onInsertADClick(VDVideoInfo info) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "广告被点击了", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onInsertADStepOutClick(VDVideoInfo info) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "去掉广告被点击了", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onFrameADPrepared(VDVideoInfo info) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "从这儿换图", Toast.LENGTH_LONG).show();
	}
}
