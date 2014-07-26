package com.panafold.main;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import at.theengine.android.bestlocation.BestLocationListener;
import at.theengine.android.bestlocation.BestLocationProvider;
import at.theengine.android.bestlocation.BestLocationProvider.LocationType;

import com.panafold.R;
import com.panafold.adapter.TabsPagerAdapter;
import com.panafold.main.datamodel.DatabaseHandler;
import com.panafold.main.datamodel.ReviewWord;
import com.panafold.main.datamodel.SqlLiteDbHelper;
import com.panafold.main.datamodel.Word;

public class MainActivity extends FragmentActivity implements TextToSpeech.OnInitListener, YahooWeatherInfoListener{
	private TextToSpeech tts;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, 5000, true);
	public static int shown;
	private ImageView mIvWeather0;
	private BestLocationProvider mBestLocationProvider;
	private BestLocationListener mBestLocationListener;
	DatabaseHandler dh;
	public static Typeface gothamFont,neutrafaceFont;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);		
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		viewPager.setCurrentItem(1);
		
		//setup text to speech engine
		tts = new TextToSpeech(this, this);
		
		
		//add custom fonts
		gothamFont = Typeface.createFromAsset(getAssets(), "fonts/Gotham-Book.ttf");
		neutrafaceFont= Typeface.createFromAsset(getAssets(), "fonts/NeutraText-Bold.otf");
		
		
		CurrentWord.allWords= new ArrayList<Word>();
		
		 dh = new DatabaseHandler(MainActivity.this);
		SqlLiteDbHelper dbhelper = new SqlLiteDbHelper(this);
		try {
			dbhelper.CopyDataBaseFromAsset();
			dbhelper.openDataBase();
			List<Word> allWords = dbhelper.getAllWords();
			for(Word w: allWords){
				
				CurrentWord.allWords.add(w);
				//send some to reviewmenu review section. 9 days after timestamp of seen
				
				
				//send some to reviewmenu SEEN section
				
				//set todays word and update its timestamp of seen
				
				
				
				//1st day they open the app
				// english word should be saved to "seen" db with a flag for review and a timestamp that they saw it
				
				//everytime they open the app. the db will load all "seen"words and update reviewflag if 9 days has passed
				
				
				
				
				
				//db should record # of words seen
				//for i upto wordsseen.length
				//if reviewTheseWords.contains(w.getEngligh)
				//
				if(CurrentWord.theCurrentWord==null)
				CurrentWord.theCurrentWord=allWords.get(1);
				 
				//dh.addWord(w);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//get seen words. dont have to be reviewed tho
		for(ReviewWord r: dh.getReviewWords(false)){
//			System.out.println("BEFOREUPDATE: "+r.getEnglish()+" "+r.getReview());
//			dh.setAsSeen(r.getEnglish());
//			
			System.out.println("nothighlighted: "+r.getEnglish()+" "+r.getReview());
			
			String string = r.getTimeStamp();
			Date date;
			try {
				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).parse(string);
				System.out.println(date);
				
				//if the date is 9 days in the past then simply
				//increment the number
				//if they indeed review it then we must set it back to 0
				
				
				} catch (ParseException e) {			}
			
			
		}
		for(ReviewWord ro: dh.getReviewWords(true)){		
			System.out.println("highlighted: "+ro.getEnglish()+" "+ro.getReview()+ro.getTimeStamp());
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
	            tts.setSpeechRate((float) 0.2);
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
	        	
	        	ProgressBar weatherPB=(ProgressBar)findViewById(R.id.weatherProgressBar);
	            weatherPB.setVisibility(ProgressBar.GONE);
	        	//set weather information on screen
//	        	mIvWeather0 = (ImageView) findViewById(R.id.weatherImageView);
//	        	if (weatherInfo.getCurrentConditionIcon() != null) {
//	        		mIvWeather0.setImageBitmap(weatherInfo.getCurrentConditionIcon());
//				}
	        	
	        	//exception thrown when user exits app but weather info is updated
	        	try{
	        	TextView weatherTextView =(TextView)findViewById(R.id.weatherTextView);
	        	weatherTextView.setTypeface(gothamFont);
	        	weatherTextView.setText(weatherInfo.getCurrentText() + "\n" +
				           weatherInfo.getCurrentTempF() + " degrees");
	        	}catch(Exception e){
	        		System.out.println("Exited app");
	        	}
	        }
	    }
	}