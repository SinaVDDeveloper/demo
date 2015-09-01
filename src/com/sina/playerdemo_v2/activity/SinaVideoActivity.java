package com.sina.playerdemo_v2.activity;

import java.io.File;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.sina.playerdemo_v2.R;
import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoFrameADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoInsertADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoPlaylistListener;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDLog;
import com.sina.sinavideo.sdk.utils.VDUtility;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.sina.sinavideo.sdk.widgets.playlist.VDVideoPlayListView;

public class SinaVideoActivity extends Activity implements
		OnVDVideoInsertADListener, OnVDVideoFrameADListener,
		OnVDVideoPlaylistListener {

	private VDVideoView mVDVideoView = null;
	private final static String TAG = "Test1Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.simple_test);
		// 从layout里面得到播放器ID
		mVDVideoView = (VDVideoView) findViewById(R.id.vv1);
		// 手动这是播放窗口父类，横屏的时候，会用这个做为容器使用，如果不设置，那么默认直接跳转到DecorView
		mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView
				.getParent());

		VDVideoListInfo infoList = new VDVideoListInfo();
		VDVideoInfo info = new VDVideoInfo();

		info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频0";
		info.mVideoId = "2015052841";
		// String path = VDUtility.getSDCardDataPath(this);
		// path += "/137535755.mp4";
		// info.mPlayUrl = path;
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131396797&tags=videoapp_android";
		infoList.addVideoInfo(info);

		info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频1";
		info.mVideoId = "131386882";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
		infoList.addVideoInfo(info);

		info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频2";
		info.mVideoId = "131386882";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
		infoList.addVideoInfo(info);

		info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频3";
		info.mVideoId = "131386882";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
		infoList.addVideoInfo(info);

		// 广告回调接口
		// FrameAD表明是一个帧间广告（点击暂停等出现的那个图）
		mVDVideoView.setFrameADListener(this);
		// InsertAD表明是一个前贴片广告，（插入广告）
		mVDVideoView.setInsertADListener(this);
		// 播放列表回调接口
		mVDVideoView.setPlaylistListener(this);

		// 设置播放列表类型
		VDVideoPlayListView listView = (VDVideoPlayListView) findViewById(R.id.play_list_view);
		if (listView != null) {
			listView.onVideoList(infoList);
		}
		// 初始化播放器以及播放列表
		mVDVideoView.open(SinaVideoActivity.this, infoList);
		// 开始播放，直接选择序号即可
		mVDVideoView.play(0);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (!mVDVideoView.onVDKeyDown(keyCode, event)) {
			return super.onKeyDown(keyCode, event);
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mVDVideoView.release(false);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		int index = mVDVideoView.getListInfo().mIndex;
		long position = mVDVideoView.getListInfo().getCurrInfo().mVideoPosition;
		mVDVideoView.play(index, position);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mVDVideoView.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mVDVideoView.onStop();
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
	public void onFrameADPrepared(VDVideoInfo info) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "在这儿换图", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onInsertADClick(VDVideoInfo info) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "插入广告被点击了", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onInsertADStepOutClick(VDVideoInfo info) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "去掉广告被点击了", Toast.LENGTH_LONG).show();
	}

}
