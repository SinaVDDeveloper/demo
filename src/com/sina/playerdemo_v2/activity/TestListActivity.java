package com.sina.playerdemo_v2.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sina.playerdemo_v2.R;
import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoFrameADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoInsertADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoPlaylistListener;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.VDVideoViewController;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.sina.sinavideo.sdk.widgets.playlist.VDVideoPlayListContainer;

/**
 * 列表样式
 * 
 * @author liu_chonghui
 * 
 */
public class TestListActivity extends Activity implements
		OnVDVideoFrameADListener, OnVDVideoInsertADListener,
		OnVDVideoPlaylistListener {
	ListView mVideoList;
	VideoListAdapter mAdapter;
	ArrayList<Item> contentList = new ArrayList<Item>();

	View videoContainer;
	VDVideoView videoView;
	VDVideoPlayListContainer mVDVideoPlayListContainer;

	@SuppressLint("InflateParams")
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.list_vision);

		prepareTestData();

		mVideoList = (ListView) findViewById(R.id.list);
		mAdapter = new VideoListAdapter();
		mAdapter.setItems(contentList);
		mVideoList.setAdapter(mAdapter);
		mAdapter.notifyDataSetChanged();

		videoContainer = LayoutInflater.from(this).inflate(
				R.layout.video_layout, null);
		videoView = (VDVideoView) videoContainer.findViewById(R.id.vv1);
		videoView.setVDVideoViewContainer((ViewGroup) videoView.getParent());
		mVDVideoPlayListContainer = (VDVideoPlayListContainer) videoContainer
				.findViewById(R.id.playlist1);

		registerListener();
	}

	private void registerListener() {
		if (VDVideoViewController.getInstance(this) != null) {
			VDVideoViewController.getInstance(this).getExtListener()
					.setFrameADListener(this);
			VDVideoViewController.getInstance(this).getExtListener()
					.setInsertADListener(this);
			VDVideoViewController.getInstance(this).getExtListener()
					.setPlaylistListener(this);
		}
	}

	protected Activity getActivity() {
		return this;
	}

	protected View getContainer() {
		return videoContainer;
	}

	private class VideoListAdapter extends BaseAdapter {

		private ArrayList<Item> mItems;

		public void setItems(ArrayList<Item> list) {
			mItems = list;
		}

		@Override
		public int getCount() {
			if (mItems != null) {
				return mItems.size();
			}
			return 0;
		}

		@Override
		public Object getItem(int pos) {
			return mItems.get(pos);
		}

		@Override
		public long getItemId(int pos) {
			return pos;
		}

		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertView, ViewGroup rootView) {
			final Item item = mItems.get(position);
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.list_vision_item, null);
				holder.videoLayout = (ViewGroup) convertView
						.findViewById(R.id.video_layout);
				holder.logo = (ImageView) convertView
						.findViewById(R.id.iv_video_thumb);
				holder.play = (ImageView) convertView
						.findViewById(R.id.iv_video_playicon);
				holder.playTime = (TextView) convertView
						.findViewById(R.id.iv_video_playtime);
				holder.listener = new ClickListener();
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (holder.play != null) {
				holder.listener.setData(holder.videoLayout, item.getTitle(),
						item.getUrl(), position);
				holder.play.setOnClickListener(holder.listener);
			}
			if (holder.logo != null) {
				holder.logo.setImageResource(R.drawable.focusads_default);
				// ImageLoader.getInstance().displayImage(item.getImg(),
				// holder.logo, videoThumbOptions,
				// new AnimateFirstDisplayListener());
			}
			if (holder.playTime != null) {
				holder.playTime.setText(String.valueOf(item.getNo()));
			}

			if (item.isVideoShow) {
				holder.logo.setVisibility(View.GONE);
				holder.play.setVisibility(View.GONE);
				holder.playTime.setVisibility(View.GONE);
			} else {
				holder.logo.setVisibility(View.VISIBLE);
				holder.play.setVisibility(View.VISIBLE);
				holder.playTime.setVisibility(View.VISIBLE);
			}

			return convertView;
		}

		class ClickListener implements OnClickListener {
			String title;
			String url;
			ViewGroup parent;
			int position;

			public void setData(ViewGroup parent, String title, String url,
					int position) {
				this.parent = parent;
				this.title = title;
				this.url = url;
				this.position = position;
			}

			@Override
			public void onClick(View view) {
				View container = getContainer();
				if (container == null || this.parent == null) {
					return;
				}
				if (container.getParent() != null) {
					((ViewGroup) container.getParent()).removeAllViews();
				}
				this.parent.addView(container);

				VDVideoListInfo infoList = new VDVideoListInfo();
				VDVideoInfo info = new VDVideoInfo();
				info = new VDVideoInfo();
				info.mTitle = title;
				info.mPlayUrl = url;
				infoList.addVideoInfo(info);
				videoView.open(getActivity(), infoList);
				videoView.play(0);

				for (Item item : mItems) {
					item.isVideoShow = false;
				}
				if (position < mItems.size()) {
					mItems.get(position).isVideoShow = true;
				}
				mAdapter.setItems(mItems);
				mAdapter.notifyDataSetChanged();
			}
		}

		class ViewHolder {
			ViewGroup videoLayout;
			ImageView logo;
			ImageView play;
			TextView playTime;
			ClickListener listener;
		}
	}

	class Item {
		int no;
		String title;
		String img;
		String url;
		boolean isVideoShow;

		public Item(int no, String title, String img, String url) {
			this.no = no;
			this.title = title;
			this.img = img;
			this.url = url;
		}

		public int getNo() {
			return this.no;
		}

		public String getTitle() {
			return this.title;
		}

		public String getImg() {
			return this.img;
		}

		public String getUrl() {
			return this.url;
		}

	}

	protected void prepareTestData() {
		String TEST_THUMB_URL = "http://n.sinaimg.cn/transform/20150526/splR-avxeafs8127570.jpg";
		String TEST_THUMB_URL2 = "http://n.sinaimg.cn/default/20150525/xqHu-avxeafs8051553.jpg";
		String TEST_PLAY_URL = "http://v.iask.com/v_play_ipad.php?vid=138152839";
		String TEST_PLAY_URL2 = "http://v.iask.com/v_play_ipad.php?vid=138116139";
		String TEST_PLAY_TITLE = "<GTA5>闪电侠席卷圣洛城";
		String TEST_PLAY_TITLE2 = "DOTA2-TI5国际邀请赛";
		contentList.clear();
		contentList.add(new Item(1, TEST_PLAY_TITLE, TEST_THUMB_URL,
				TEST_PLAY_URL));
		contentList.add(new Item(2, TEST_PLAY_TITLE2, TEST_THUMB_URL2,
				TEST_PLAY_URL2));
		contentList.add(new Item(3, TEST_PLAY_TITLE, TEST_THUMB_URL,
				TEST_PLAY_URL));
		contentList.add(new Item(4, TEST_PLAY_TITLE2, TEST_THUMB_URL2,
				TEST_PLAY_URL2));
		contentList.add(new Item(5, TEST_PLAY_TITLE, TEST_THUMB_URL,
				TEST_PLAY_URL));
		contentList.add(new Item(6, TEST_PLAY_TITLE2, TEST_THUMB_URL2,
				TEST_PLAY_URL2));
		contentList.add(new Item(7, TEST_PLAY_TITLE, TEST_THUMB_URL,
				TEST_PLAY_URL));
		contentList.add(new Item(8, TEST_PLAY_TITLE2, TEST_THUMB_URL2,
				TEST_PLAY_URL2));
		contentList.add(new Item(9, TEST_PLAY_TITLE, TEST_THUMB_URL,
				TEST_PLAY_URL));
		contentList.add(new Item(10, TEST_PLAY_TITLE2, TEST_THUMB_URL2,
				TEST_PLAY_URL2));
	}

	@Override
	public void onPlaylistClick(VDVideoInfo info, int p) {
		videoView.play(p);
	}

	@Override
	public void onInsertADClick(VDVideoInfo info) {
	}

	@Override
	public void onInsertADStepOutClick(VDVideoInfo info) {
	}

	@Override
	public void onFrameADPrepared(VDVideoInfo info) {
	}

	public void onResume() {
		super.onResume();
		if (VDVideoViewController.getInstance(this) != null
				&& VDVideoViewController.getInstance(this).getVideoInfoNum() > 0) {
			VDVideoViewController.getInstance(this).onResume();
		}
	}

	public void onPause() {
		super.onPause();
		if (VDVideoViewController.getInstance(this) != null) {
			VDVideoViewController.getInstance(this).onPause();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		if (VDVideoViewController.getInstance(this) != null) {
			VDVideoViewController.getInstance(this).onStop();
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (videoView != null) {
				videoView.setIsFullScreen(true);
				LogS.e(VDVideoFullModeController.TAG,
						"onConfigurationChanged---横屏");
			}
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (videoView != null) {
				videoView.setIsFullScreen(false);
				LogS.e(VDVideoFullModeController.TAG,
						"onConfigurationChanged---竖屏");
			}
		}
	}

	public boolean holdGoBack() {
		boolean isLandscape = !VDVideoFullModeController.getInstance()
				.getIsPortrait();
		if (isLandscape) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (holdGoBack()) {
				if (VDVideoViewController.getInstance(this) != null
						&& !VDVideoViewController.getInstance(this).onKeyEvent(
								event)) {
					return false;
				}
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onDestroy() {
		videoView.release(false);
		super.onDestroy();
	}
}
