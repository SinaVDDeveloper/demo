package com.sina.playerdemo_v2.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.sina.playerdemo_v2.R;
import com.sina.sinavideo.coreplayer.util.LogS;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDPlayerTypeSwitchListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoPreparedListener;
import com.sina.sinavideo.sdk.VDVideoView;
import com.sina.sinavideo.sdk.VDVideoViewController;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoCompletionListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoFrameADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoInsertADListener;
import com.sina.sinavideo.sdk.VDVideoExtListeners.OnVDVideoPlaylistListener;
import com.sina.sinavideo.sdk.data.VDVideoInfo;
import com.sina.sinavideo.sdk.data.VDVideoListInfo;
import com.sina.sinavideo.sdk.utils.VDVideoFullModeController;
import com.sina.sinavideo.sdk.widgets.playlist.VDVideoPlayListView;

/**
 * 最简单的例子，用在工程中，需要自己更改存储等信息
 * 
 * @author alexsun
 * 
 */
public class TestAutoSaveActivity extends Activity implements OnVDVideoInsertADListener, OnVDVideoFrameADListener,
        OnVDVideoPlaylistListener, OnVDVideoPreparedListener, OnVDVideoCompletionListener, OnVDPlayerTypeSwitchListener {

    private VDVideoView mVDVideoView = null;
    private final static String TAG = "Test1Activity";

    private final static int DELAY_SEC = 1000;
    private final static String SP_NAME = "auto_saved_position";
    private SharedPreferences mSP = null;
    private SharedPreferences.Editor mSPEditor = null;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {

        public void run() {
            // 当前进度存盘
            VDVideoViewController controller = VDVideoViewController.getInstance(TestAutoSaveActivity.this);
            if (controller == null) {
                // 注意: VDVideoView 和 VDVideoViewController 可能已经销毁
                return;
            }
            if (!controller.mVDPlayerInfo.isPlaying()) {
                return;
            }
            long position = controller.getCurrentPosition();
            mSPEditor.putLong(SP_NAME, position);
            mSPEditor.commit();
            mHandler.postDelayed(mRunnable, DELAY_SEC);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        mSP = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        mSPEditor = mSP.edit();
        long position = mSP.getLong(SP_NAME, 0);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.simple_test);
        // 从layout里面得到播放器ID
        mVDVideoView = (VDVideoView) findViewById(R.id.vv1);
        // 手动这是播放窗口父类，横屏的时候，会用这个做为容器使用，如果不设置，那么默认直接跳转到DecorView
        mVDVideoView.setVDVideoViewContainer((ViewGroup) mVDVideoView.getParent());

        VDVideoListInfo infoList = new VDVideoListInfo();
        VDVideoInfo info = new VDVideoInfo();

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
        mVDVideoView.setCompletionListener(this);
        mVDVideoView.setPreparedListener(this);
        mVDVideoView.setOnVDPlayerTypeSwitchListener(this);

        // 设置播放列表类型
        VDVideoPlayListView listView = (VDVideoPlayListView) findViewById(R.id.play_list_view);
        if (listView != null) {
            listView.onVideoList(infoList);
        }
        // 初始化播放器以及播放列表
        mVDVideoView.open(TestAutoSaveActivity.this, infoList);
        // 开始播放，直接选择序号即可
        mVDVideoView.play(0, position);
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

    @Override
    public void onVDVideoCompletion(VDVideoInfo info, int status) {
        // TODO Auto-generated method stub
        mHandler.post(mRunnable);
    }

    @Override
    public void onVDVideoPrepared(VDVideoInfo info) {
        // TODO Auto-generated method stub
        mHandler.postDelayed(mRunnable, 0);
    }

    @Override
    public void OnVDVideoPlayerTypeSwitch(VDVideoInfo info, int p) {
        // TODO Auto-generated method stub
        mVDVideoView.stop();
        mVDVideoView.release(true);
        mVDVideoView.open(this, info);
        mVDVideoView.play(0);
    }

}
