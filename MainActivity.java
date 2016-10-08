package com.changel.test_calculator.ui;

import com.changel.test_calculator.StartView;
import com.changel.test_calculator.R;
import com.changel.test_calculator.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class MainActivity extends Activity {

	//ImageView im;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//im = (ImageView) findViewById(R.id.imageView1);


		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(MainActivity.this, StartView.class);
				startActivity(intent);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);
				MainActivity.this.finish();
			}

		}, SPLASH_DISPLAY_LENGHT);

	}

	private final int SPLASH_DISPLAY_LENGHT = 3000;

}


