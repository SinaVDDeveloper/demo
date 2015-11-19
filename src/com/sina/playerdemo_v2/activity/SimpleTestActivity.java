package com.sina.playerdemo_v2.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.sina.playerdemo_v2.R;
import com.sina.sinavideo.coreplayer.ISinaVideoView;
import com.sina.sinavideo.coreplayer.splayer.GLSurfaceView;
import com.sina.sinavideo.coreplayer.splayer.TextureVideoView;
import com.sina.sinavideo.coreplayer.splayer.VideoView;
import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoFrameADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoInsertADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoPlaylistListener;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.VDVideoViewController;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.sina.sinavideo.sdk.widgets.playlist.VDVideoPlayListView;

@SuppressLint("NewApi")
public class SimpleTestActivity extends Activity implements
		OnVDVideoInsertADListener, OnVDVideoFrameADListener,
		OnVDVideoPlaylistListener, OnClickListener {

	private VDVideoView mVDVideoView = null;
	private final static String TAG = "Test1Activity";
	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.simple_test);
		// adIV是广告部分的控件，这个需要产品侧开发人员自己处理了
		ImageView adIV = (ImageView) findViewById(R.id.adFrameImageView);

		findViewById(R.id.rotation_btn).setOnClickListener(this);
		findViewById(R.id.capture_btn).setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.image);
		adIV.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(SimpleTestActivity.this, "点击了静帧广告",
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
		// infoList.mInsertADSecNum = 133;
		// 填充播放列表，第一个是广告，理论上，可以在任何位置
		info.mTitle = "这个是一个测试广告";
		info.mPlayUrl = "http://v.iask.com/v_play_ipad.php?vid=137535755&tags=videoapp_android";
		info.mIsInsertAD = true;
		infoList.addVideoInfo(info);

		info = new VDVideoInfo();
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

		// 广告回调接口
		// FrameAD表明是一个帧间广告（点击暂停等出现的那个图）
		mVDVideoView.setFrameADListener(this);
		// InsertAD表明是一个前贴片广告，（插入广告）
		mVDVideoView.setInsertADListener(this);
		// 播放列表回调接口
		mVDVideoView.setPlaylistListener(this);

		// 简单方式处理的视频列表
		VDVideoPlayListView listView = (VDVideoPlayListView) findViewById(R.id.play_list_view);
		if (listView != null) {
			listView.onVideoList(infoList);
		}
		// 初始化播放器以及播放列表
		mVDVideoView.open(SimpleTestActivity.this, infoList);
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
	}

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
		Toast.makeText(this, "开始换图", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rotation_btn:
			ISinaVideoView videoView = VDVideoViewController.getInstance(this)
					.getVideoView();
			if (videoView instanceof TextureVideoView) {
				((TextureVideoView) videoView).animate().rotationBy(90.0f)
						.setDuration(500).start();
			} else {
				Toast.makeText(getApplicationContext(), "不支持旋转",
						Toast.LENGTH_SHORT).show();
				// ((VideoView)videoView).animate().rotationBy(90.0f).setDuration(500).start();
			}
			break;
		case R.id.capture_btn:
			saveCurrentFrame();
			break;

		default:
			break;
		}

	}

	private void saveCurrentFrame() {
		ISinaVideoView videoView = VDVideoViewController.getInstance(this)
				.getVideoView();
		if (videoView instanceof TextureVideoView) {
			final Bitmap currentFrameBitmap = ((TextureVideoView) videoView)
					.getBitmap();
			if (currentFrameBitmap == null){
				Toast.makeText(getApplicationContext(), "截屏失败",
						Toast.LENGTH_SHORT).show();
				return;
			}
			final File currentFrameFile = new File(
					getExternalFilesDir("frames"), "frame"
							+ System.currentTimeMillis() + ".jpg");
			writeBitmapToFile(currentFrameBitmap, currentFrameFile);

			imageView.setImageBitmap(currentFrameBitmap);
			Toast.makeText(
					this,
					"Frame saved as " + currentFrameFile.getAbsolutePath()
							+ ".", Toast.LENGTH_SHORT).show();
		} else {
			final Bitmap currentFrameBitmap = ((VideoView) videoView)
					.getDrawingCache();
			if (currentFrameBitmap == null) {
				Toast.makeText(getApplicationContext(), "不支持截屏",
						Toast.LENGTH_SHORT).show();
				return;
			}
			final File currentFrameFile = new File(
					getExternalFilesDir("frames"), "frame"
							+ System.currentTimeMillis() + ".jpg");
			writeBitmapToFile(currentFrameBitmap, currentFrameFile);

			imageView.setImageBitmap(currentFrameBitmap);
		}

	}

	private void writeBitmapToFile(final Bitmap bitmap, final File file) {
		try {
			FileOutputStream out = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
			out.close();
		} catch (final IOException e) {
			Log.e(TAG, "Error writing bitmap to file.", e);
		}
	}
}
