package com.panafold;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.panafold.MyApplication.TrackerName;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class AboutPageActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_page);
	}
	public void back(View v){
		finish();
	}
	@Override
	public void onStart(){
	    super.onStart();
	    

	 // Get tracker.
        Tracker t = ((MyApplication) getApplication()).getTracker(
            TrackerName.GLOBAL_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("com.panafold.AboutPageActivity");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
	}
}
