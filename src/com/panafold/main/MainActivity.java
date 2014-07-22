package com.panafold.main;

import java.util.Date;
import java.util.Locale;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;

import android.location.Location;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import at.theengine.android.bestlocation.BestLocationListener;
import at.theengine.android.bestlocation.BestLocationProvider;
import at.theengine.android.bestlocation.BestLocationProvider.LocationType;

import com.panafold.R;
import com.panafold.adapter.TabsPagerAdapter;

public class MainActivity extends FragmentActivity implements TextToSpeech.OnInitListener, YahooWeatherInfoListener{
	private TextToSpeech tts;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, 5000, true);
	public static Boolean dateShown;
	private ImageView mIvWeather0;
	private BestLocationProvider mBestLocationProvider;
	private BestLocationListener mBestLocationListener;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);

		//setup text to speech engine
		tts = new TextToSpeech(this, this);
		
		
		//first word is strawberry. otherwise it is already chose by the user
		if(CurrentWord.currentEnglishWord==null){
			CurrentWord.currentEnglishWord="strawberry";
			
		}
	}
	
	@Override
	protected void onResume() {
		initLocation();
		//get location for waether info
		mBestLocationProvider.startLocationUpdatesWithListener(mBestLocationListener);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		initLocation();
		mBestLocationProvider.stopLocationUpdates();
		super.onPause();
	}
		@Override
	    public void onDestroy() {
	        // Don't forget to shutdown tts!
	        if (tts != null) {
	            tts.stop();
	            tts.shutdown();
	        }
	        super.onDestroy();
	    }
	    @Override
	    public void onInit(int status) {
	 
	        if (status == TextToSpeech.SUCCESS) {
	 
	            int result = tts.setLanguage(Locale.JAPANESE);
	 
	            if (result == TextToSpeech.LANG_MISSING_DATA
	                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
	                Log.e("TTS", "This Language is not supported");
	            } else {
	                //speakOut();
	            }
	 
	        } else {
	            Log.e("TTS", "Initilization Failed!");
	        }
	 
	    }
	    public void speakOut(View v){
	    	speakOut();
	    }
	 
	    private void speakOut() {
	    	//speak the japanese text
	        TextView textview = (TextView)findViewById(R.id.japaneseTextView);
	        tts.speak(textview.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);
	        
	    }
	    
	    private void initLocation(){
			if(mBestLocationListener == null){
				mBestLocationListener = new BestLocationListener() {
					
					@Override
					public void onStatusChanged(String provider, int status, Bundle extras) {					}
					
					@Override
					public void onProviderEnabled(String provider) {					}
					
					@Override
					public void onProviderDisabled(String provider) {					}
					
					@Override
					public void onLocationUpdateTimeoutExceeded(LocationType type) {					}
					
					@Override
					public void onLocationUpdate(Location location, LocationType type,
							boolean isFresh) {
						String lat =String.valueOf(location.getLatitude());
						String lng =String.valueOf(location.getLongitude());
						mYahooWeather.queryYahooWeatherByLatLon(getApplicationContext(),lat,lng,MainActivity.this);
					}
				};
				
				if(mBestLocationProvider == null){
					mBestLocationProvider = new BestLocationProvider(this, true, true, 10000, 1000, 2, 0);
				}
			}
		}
	    @Override
	    public void gotWeatherInfo(WeatherInfo weatherInfo) {
	        if(weatherInfo != null) {
	        	//turn off location updates because they are no longer needed.
	        	mBestLocationProvider.stopLocationUpdates();
	        	
	        	
	        	//set weather information on screen
	        	mIvWeather0 = (ImageView) findViewById(R.id.weatherImageView);
	        	if (weatherInfo.getCurrentConditionIcon() != null) {
	        		mIvWeather0.setImageBitmap(weatherInfo.getCurrentConditionIcon());
				}
	        	TextView weatherTextView =(TextView)findViewById(R.id.weatherTextView);
	        	weatherTextView.setText(weatherInfo.getCurrentText() + "\n" +
				           weatherInfo.getCurrentTempF() + " degrees");
	        }
	    }
	}