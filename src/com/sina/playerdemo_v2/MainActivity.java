package com.sina.playerdemo_v2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sina.playerdemo_v2.activity.OnlyHorizonActivity;
import com.sina.playerdemo_v2.activity.SimpleTestActivity;
import com.sina.playerdemo_v2.activity.SinaVideoActivity;
import com.sina.playerdemo_v2.activity.TestAutoSaveActivity;
import com.sina.playerdemo_v2.activity.TestListActivity;
import com.sina.playerdemo_v2.activity.TestM3u8Activity;
import com.sina.playerdemo_v2.activity.TestMultiADActivity;
import com.sina.playerdemo_v2.activity.TestSimpleADActivity;
import com.sina.playerdemo_v2.activity.TestWebviewActivity;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						SimpleTestActivity.class);
				startActivity(intent);
			}
		});

		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						SinaVideoActivity.class);
				startActivity(intent);
			}
		});

		Button button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						TestSimpleADActivity.class);
				startActivity(intent);
			}
		});

		Button button4 = (Button) findViewById(R.id.button4);
		button4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						TestMultiADActivity.class);
				startActivity(intent);
			}
		});

		Button button5 = (Button) findViewById(R.id.button5);
		button5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						OnlyHorizonActivity.class);
				startActivity(intent);
			}
		});

		Button button6 = (Button) findViewById(R.id.button6);
		button6.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						TestM3u8Activity.class);
				startActivity(intent);
			}
		});

		Button button7 = (Button) findViewById(R.id.button7);
		button7.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						TestAutoSaveActivity.class);
				startActivity(intent);
			}
		});

		Button button8 = (Button) findViewById(R.id.button8);
		button8.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						TestWebviewActivity.class);
				startActivity(intent);
			}
		});
		
		Button button9 = (Button) findViewById(R.id.button9);
		button9.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						TestListActivity.class);
				startActivity(intent);
			}
		});

	}
}
