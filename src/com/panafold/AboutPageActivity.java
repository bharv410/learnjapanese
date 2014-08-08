package com.panafold;

import com.panafold.main.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AboutPageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_page);
	}
	public void back(View v){
		startActivity(new Intent(AboutPageActivity.this,MainActivity.class));
	}
}
