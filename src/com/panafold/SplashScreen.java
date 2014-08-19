package com.panafold;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.panafold.MyApplication.TrackerName;
import com.panafold.main.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;



public class SplashScreen extends Activity {
	String PREFS_NAME = "MyPrefsFile";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash_screen);
		new Handler().postDelayed(new Runnable() {
 
            @Override
            public void run() {
            	SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

            	if (settings.getBoolean("my_first_time", true)) {
            		Intent i = new Intent(SplashScreen.this, TutorialActivity.class);
                    startActivity(i);
            	    settings.edit().putBoolean("my_first_time", false).commit(); 
            	}else{
            		Intent i = new Intent(SplashScreen.this, MainActivity.class);
                    startActivity(i);
            	}
                finish();
            }
        }, 300);
	}
	
	
	@Override
	public void onStart(){
	    super.onStart();
	    

	 // Get tracker.
        Tracker t = ((MyApplication) getApplication()).getTracker(
            TrackerName.GLOBAL_TRACKER);

        // Set screen name.
        // Where path is a String representing the screen name.
        t.setScreenName("com.panafold.SplashScreen");

        // Send a screen view.
        t.send(new HitBuilders.AppViewBuilder().build());
	}
	
	
}
