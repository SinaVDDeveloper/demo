package com.sina.playerdemo_v2.activity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.sina.playerdemo_v2.R;
import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoFrameADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoInsertADEndListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoInsertADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoPlaylistListener;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.sina.sinavideo.sdk.widgets.playlist.VDVideoPlayListView;

public class TestMultiADActivity extends Activity implements
		OnVDVideoInsertADListener, OnVDVideoFrameADListener,
		OnVDVideoPlaylistListener, OnVDVideoInsertADEndListener {

	private VDVideoView mVDVideoView = null;
	private final static String TAG = "Test1Activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.simple_test);
		// adIV是广告部分的控件，这个需要产品侧开发人员自己处理了
		ImageView adIV = (ImageView) findViewById(R.id.adFrameImageView);
		adIV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(TestMultiADActivity.this, "点击了静帧广告",
						Toast.LENGTH_LONG).show();
			}
		});
		// 从layout里面得到播放器ID
		mVDVideoView = (VDVideoView) findViewById(R.id.vv1);
		// 手动这是播放窗口父类，横屏的时候，会用这个做为容器使用，如果不设置，那么默认直接跳转到DecorView
		mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView
				.getParent());

		VDVideoListInfo infoList = new VDVideoListInfo();
		VDVideoInfo info = new VDVideoInfo();
		// 多流广告方式，就是在播放列表中配置广告流的方式
		// 单流方式：INSERTAD_TYPE_SINGLE，暂时不支持，如果设置了，会报exception
		infoList.mInsertADType = VDVideoListInfo.INSERTAD_TYPE_MULTI;
		// 如果是两个或者以上的广告流，因为没办法直接取到每条流的时间，所以需要手动设置，否则会报exception
		// BTW：ticker组件最大显示长度为两位，也就是99，如果超过99秒的广告时长设置，会一直显示99，直到当前播放时间小于99为止
		// infoList.mInsertADSecNum = 6;
		// 如果没有办法获取广告播放时间，那么设置这个选项后，由系统在播放时候分别获取时间来进行播放ß
		infoList.mIsSetInsertADTime = false;
		// 填充播放列表，第一个是广告，理论上，可以在任何位置
		// info.mTitle = "这个是一个测试广告";
		// info.mPlayUrl =
		// "http://v.iask.com/v_play_ipad.php?vid=137535755&tags=videoapp_android";
		// info.mPlayUrl = "http://123.126.53.108:10009/2.mp4";
		// info.mIsInsertAD = true;
		// infoList.addVideoInfo(info);

		// 实验第二个广告视频
		info = new VDVideoInfo();
		info.mTitle = "这个是第二个测试广告";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=137643076&tags=videoapp_android";
		// info.mPlayUrl =
		// "http://v.iask.com/v_play_ipad.php?vid=137858284&tags=videoapp_android";
		info.mIsInsertAD = true;
		infoList.addVideoInfo(info);

		info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频0";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=137535755&tags=videoapp_android";
		infoList.addVideoInfo(info);

		info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频1";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386883&tags=videoapp_android";
		infoList.addVideoInfo(info);

		// 实验第三个广告视频
		info = new VDVideoInfo();
		info.mTitle = "这个是第三个测试广告";
		info.mPlayUrl = "http://123.126.53.108:10009/1.mp4";
		info.mIsInsertAD = true;
		infoList.addVideoInfo(info);

		info = new VDVideoInfo();
		info.mTitle = "这就是一个测试视频2";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=131386885&tags=videoapp_android";
		infoList.addVideoInfo(info);
		//
		// info = new VDVideoInfo();
		// info.mTitle = "这就是一个测试视频3";
		// info.mPlayUrl =
		// "http://v.iask.com/v_play_ipad.php?vid=131386882&tags=videoapp_android";
		// infoList.addVideoInfo(info);

		// 广告回调接口
		// FrameAD表明是一个帧间广告（点击暂停等出现的那个图）
		mVDVideoView.setFrameADListener(this);
		// InsertAD表明是一个前贴片广告，（插入广告）
		mVDVideoView.setInsertADListener(this);
		// 播放列表回调接口
		mVDVideoView.setPlaylistListener(this);
		mVDVideoView.setInsertADEndListener(this);

		// 设置播放列表类型
		VDVideoPlayListView listView = (VDVideoPlayListView) findViewById(R.id.play_list_view);
		if (listView != null) {
			listView.onVideoList(infoList);
		}
		// 初始化播放器以及播放列表
		mVDVideoView.open(TestMultiADActivity.this, infoList);
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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mVDVideoView.onResume();
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

	/**
	 * 播放列表里面点击了某个视频，触发外部事件
	 */
	@Override
	public void onPlaylistClick(VDVideoInfo info, int p) {
		// TODO Auto-generated method stub
		if (info == null) {
			LogS.e(TAG, "info is null");
		}
		mVDVideoView.play(p);
		// openVideo(info, p);
	}

	/**
	 * 帧间动画传回的接口，在有帧间动画时候，然后按暂停按钮，会调用此方法，在里面写响应的逻辑，看匹配那个广告
	 */

	/**
	 * 视频插入广告传回的接口，表明当前的广告被点击了『了解更多』
	 */
	@Override
	public void onInsertADClick(VDVideoInfo info) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "插入广告被点击了", Toast.LENGTH_LONG).show();
	}

	/**
	 * 视频插入广告传回的接口，表明当前的广告被点击了『去掉广告』，按照其他视频逻辑，直接跳转会员页
	 */
	@Override
	public void onInsertADStepOutClick(VDVideoInfo info) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "去掉广告被点击了", Toast.LENGTH_LONG).show();
	}

	/**
	 * 静帧广告换图，从这儿来
	 */
	@Override
	public void onFrameADPrepared(VDVideoInfo info) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "开始换图", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onInsertADEnd(VDVideoInfo info, int status) {
		// TODO Auto-generated method stub
		Toast.makeText(this, "贴片广告播放结束,status:" + status, Toast.LENGTH_LONG)
				.show();
	}
}
