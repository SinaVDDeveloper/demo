
package com.sina.playerdemo_v2.activity;

import com.sina.playerdemo_v2.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * WEBView嵌入视频功能，暂时不能用
 * 
 * @author sunxiao
 */
public class TestWebviewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.test_webview);

        WebView wv1 = (WebView) findViewById(R.id.webview1);

        wv1.getSettings().setJavaScriptEnabled(true);
        wv1.loadUrl("file:///android_res/raw/test_webview.html");
    }
}
