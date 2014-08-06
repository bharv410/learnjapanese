package com.panafold.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;
import android.content.Context;
import android.graphics.Color;
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
import com.panafold.main.datamodel.LocalDBHelper;
import com.panafold.main.datamodel.ReviewWord;
import com.panafold.main.datamodel.SqlLiteDbHelper;
import com.panafold.main.datamodel.Word;
import com.viewpagerindicator.LinePageIndicator;

public class MainActivity extends FragmentActivity implements
		TextToSpeech.OnInitListener, YahooWeatherInfoListener {
	private TextToSpeech tts;
	private ViewPager viewPager;
	private TabsPagerAdapter mAdapter;
	private YahooWeather mYahooWeather = YahooWeather.getInstance(5000, 5000,
			true);
	public static int shown;
	private ImageView mIvWeather0;
	private BestLocationProvider mBestLocationProvider;
	private BestLocationListener mBestLocationListener;
	LocalDBHelper dynamicdb;
	public static Typeface gothamFont, neutrafaceFont, japaneseFont;
	private ProgressBar weatherPB;
	private Boolean currentWordIsSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new TabsPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(mAdapter);
		viewPager.setCurrentItem(1);
		CurrentWord.initHashMap();
		CurrentWord.initStrings();
		currentWordIsSet = false;
		// Bind the title indicator to the adapter
		final LinePageIndicator titleIndicator = (LinePageIndicator) findViewById(R.id.indicator);
		titleIndicator.setViewPager(viewPager);
		titleIndicator.setCurrentItem(1);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int i, float v, int i2) {
			}
			@Override
			public void onPageSelected(int i) {
				if (i == 1) {
					titleIndicator.setBackgroundColor(Color
							.parseColor("#F8EFCB"));
				} else {
					titleIndicator.setBackgroundColor(Color
							.parseColor("#555F5F"));
				}
				titleIndicator.setCurrentItem(i);
			}
			@Override
			public void onPageScrollStateChanged(int i) {

			}
		});

		// setup text to speech engine
		tts = new TextToSpeech(this, this);

		// add custom fonts
		gothamFont = Typeface.createFromAsset(getAssets(),
				"fonts/Gotham-Book.ttf");
		neutrafaceFont = Typeface.createFromAsset(getAssets(),
				"fonts/NeutraText-Demi.otf");
		japaneseFont = Typeface.createFromAsset(getAssets(),
				"fonts/AozoraMinchoMedium.ttf");

		
		setupDatabases();
			
			showCorrectWord();
			
			//saves the word that was just set as current
			
		
//		// get seen words. dont have to be reviewed tho
//		for (ReviewWord r : dh.getReviewWords(false)) {
//			// System.out.println("BEFOREUPDATE: "+r.getEnglish()+" "+r.getReview());
//			// dh.setAsSeen(r.getEnglish());
//			//
//			String string = r.getTimeStamp();
//			Date date;
//			try {
//				date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
//						Locale.ENGLISH).parse(string);
//				Calendar now = Calendar.getInstance();
//				SimpleDateFormat dff = new SimpleDateFormat(
//						"yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//				// if the date is 9 days in the past then simply
//				// increment the number
//				// if they indeed review it then we must set it back to 0
//
//			} catch (ParseException e) {
//			}
//
//		}
//		for (ReviewWord ro : dh.getReviewWords(true)) {
//			System.out.println("highlighted: " + ro.getEnglish() + " "
//					+ ro.getReview() + ro.getTimeStamp());
//		}

		initLocation();
		// get location for waether info
		mBestLocationProvider
				.startLocationUpdatesWithListener(mBestLocationListener);

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
				// speakOut();
			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}
	private void setupDatabases(){
		try {
		CurrentWord.allWords = new ArrayList<Word>();
		CurrentWord.alreadySeen = new ArrayList<ReviewWord>();
		CurrentWord.alreadySeenStrings = new ArrayList<String>();
		dynamicdb = new LocalDBHelper(MainActivity.this);
		SqlLiteDbHelper dbhelper = new SqlLiteDbHelper(this);
			dbhelper.CopyDataBaseFromAsset();
			dbhelper.openDataBase();
			for (ReviewWord r : dynamicdb.getReviewWords(false)) {
				CurrentWord.alreadySeenStrings.add(r.getEnglish());
			}
			//grabs all words that were ever seen and adds to CurrnetWord.alreadySeen
			CurrentWord.alreadySeen=dynamicdb.getReviewWords(false);
			//grabs all words from the given DB and adds to CurrentWord.allWords
			CurrentWord.allWords = dbhelper.getAllWords();
			
		} catch (IOException e1) {
			e1.printStackTrace();
			}
		}
	
private void showCorrectWord(){
	for (Word w : CurrentWord.allWords) {
		
		// if no word has been chosen. and current word isnt in review list. then we found the word that should be shown next
		if (!currentWordIsSet
				&& (!CurrentWord.alreadySeenStrings.contains(w
						.getEnglish()))) {


			//if no word has been chosen and its a new day. then show new word and save to db
			if (CurrentWord.theCurrentWord == null&&itsANewDay()){
				CurrentWord.theCurrentWord = w;
				dynamicdb.addWord(CurrentWord.theCurrentWord);
				
			}else if(CurrentWord.theCurrentWord == null&&(!itsANewDay())){
				//no word was chosen. but its the same day

					for(Word wrd:CurrentWord.allWords){
						//show the same word as earlier that day
						if(wrd.getEnglish().contains(CurrentWord.alreadySeen.get(CurrentWord.alreadySeen.size()-1).getEnglish())){
							CurrentWord.theCurrentWord=wrd;
						
					}
				}
			}
			currentWordIsSet = true;
		}
	}
}
	public void speakOut(View v) {
		// speak the japanese text
		TextView textview = (TextView) findViewById(R.id.japaneseTextView);
		speakOut(textview.getText().toString());
	}

	private void speakOut(String text) {
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);

	}

	public void speakOutPhrase(View v) {
		TextView textview = (TextView) findViewById(R.id.japanesePhrase);
		speakOut(textview.getText().toString());
	}

	private void initLocation() {
		if (mBestLocationListener == null) {
			mBestLocationListener = new BestLocationListener() {

				@Override
				public void onStatusChanged(String provider, int status,
						Bundle extras) {
				}

				@Override
				public void onProviderEnabled(String provider) {
				}

				@Override
				public void onProviderDisabled(String provider) {
				}

				@Override
				public void onLocationUpdateTimeoutExceeded(LocationType type) {
				}

				@Override
				public void onLocationUpdate(Location location,
						LocationType type, boolean isFresh) {
					String lat = String.valueOf(location.getLatitude());
					String lng = String.valueOf(location.getLongitude());
					mYahooWeather.queryYahooWeatherByLatLon(
							getApplicationContext(), lat, lng,
							MainActivity.this);

				}
			};

			if (mBestLocationProvider == null) {
				mBestLocationProvider = new BestLocationProvider(this, true,
						true, 10000, 1000, 2, 0);
			}
		}
	}

	@Override
	public void gotWeatherInfo(WeatherInfo weatherInfo) {
		if (weatherInfo != null) {
			// turn off location updates because they are no longer needed.
			mBestLocationProvider.stopLocationUpdates();

			setWeatherIcon(weatherInfo.getCurrentCode());

			// exception thrown when user exits app but weather info is updated
			try {
				// start weather progressbar to indicate loading
				weatherPB = (ProgressBar) findViewById(R.id.weatherProgressBar);
				weatherPB.setVisibility(ProgressBar.GONE);

				TextView weatherTextView = (TextView) findViewById(R.id.weatherTextView);
				weatherTextView.setTypeface(gothamFont);
				CurrentWord.weatherString = weatherInfo.getCurrentTempF()
						+ " degrees";
				weatherTextView.setText(CurrentWord.weatherString);
			} catch (Exception e) {
				System.out.println("Exited app");
			}
		}
	}

	private void setWeatherIcon(int code) {
		mIvWeather0 = (ImageView) findViewById(R.id.imageView2);

		switch (code) {
		case 0:
			mIvWeather0.setImageResource(R.drawable.tornado);
			break;
		case 1:
			mIvWeather0.setImageResource(R.drawable.storm);
			break;
		case 2:
			mIvWeather0.setImageResource(R.drawable.tornado);
			break;
		case 3:
			mIvWeather0.setImageResource(R.drawable.storm);
			break;
		case 4:
			mIvWeather0.setImageResource(R.drawable.storm);
			break;
		case 5:
			mIvWeather0.setImageResource(R.drawable.rainsnow);
			break;
		case 6:
			mIvWeather0.setImageResource(R.drawable.rainhail);
			break;
		case 7:
			mIvWeather0.setImageResource(R.drawable.rainsnow);
			break;
		case 8:
			mIvWeather0.setImageResource(R.drawable.rain);
			break;
		case 9:
			mIvWeather0.setImageResource(R.drawable.rain);
			break;
		case 10:
			mIvWeather0.setImageResource(R.drawable.rain);
			break;
		case 11:
			mIvWeather0.setImageResource(R.drawable.rain);
			break;
		case 12:
			mIvWeather0.setImageResource(R.drawable.rain);
			break;
		case 13:
			mIvWeather0.setImageResource(R.drawable.snow);
			break;
		case 14:
			mIvWeather0.setImageResource(R.drawable.snow);
			break;
		case 15:
			mIvWeather0.setImageResource(R.drawable.snow);
			break;
		case 16:
			mIvWeather0.setImageResource(R.drawable.snow);
			break;
		case 17:
			mIvWeather0.setImageResource(R.drawable.rainhail);
			break;
		case 18:
			mIvWeather0.setImageResource(R.drawable.rainhail);
			break;
		case 19:
			mIvWeather0.setImageResource(R.drawable.cloudy);
			break;
		case 20:
			mIvWeather0.setImageResource(R.drawable.foggy);
			break;
		case 21:
			mIvWeather0.setImageResource(R.drawable.foggy);
			break;
		case 22:
			mIvWeather0.setImageResource(R.drawable.foggy);
			break;
		case 23:
			mIvWeather0.setImageResource(R.drawable.foggy);
			break;
		case 24:
			mIvWeather0.setImageResource(R.drawable.windy);
			break;
		case 25:
			mIvWeather0.setImageResource(R.drawable.cloudy);
			break;
		case 26:
			mIvWeather0.setImageResource(R.drawable.cloudy);
			break;
		case 27:
			mIvWeather0.setImageResource(R.drawable.cloudy);
			break;
		case 28:
			mIvWeather0.setImageResource(R.drawable.cloudy);
			break;
		case 29:
			mIvWeather0.setImageResource(R.drawable.cloudy);
			break;
		case 30:
			mIvWeather0.setImageResource(R.drawable.cloudy);
			break;
		case 31:
			mIvWeather0.setImageResource(R.drawable.cloudy);
			break;
		case 32:
			mIvWeather0.setImageResource(R.drawable.sunny);
			break;
		case 33:
			mIvWeather0.setImageResource(R.drawable.cloudy);
			break;
		case 34:
			mIvWeather0.setImageResource(R.drawable.cloudy);
			break;
		case 35:
			mIvWeather0.setImageResource(R.drawable.rainsnow);
			break;
		case 36:
			mIvWeather0.setImageResource(R.drawable.sunny);
			break;
		case 37:
			mIvWeather0.setImageResource(R.drawable.thunderstorm);
			break;
		case 38:
			mIvWeather0.setImageResource(R.drawable.thunderstorm);
			break;
		case 39:
			mIvWeather0.setImageResource(R.drawable.thunderstorm);
			break;
		case 40:
			mIvWeather0.setImageResource(R.drawable.rain);
			break;
		case 41:
			mIvWeather0.setImageResource(R.drawable.snow);
			break;
		case 42:
			mIvWeather0.setImageResource(R.drawable.snow);
			break;
		case 43:
			mIvWeather0.setImageResource(R.drawable.snow);
			break;
		case 44:
			mIvWeather0.setImageResource(R.drawable.storm);
			break;
		case 45:
			mIvWeather0.setImageResource(R.drawable.cloudy);
			break;
		case 46:
			mIvWeather0.setImageResource(R.drawable.rainsnow);
			break;
		case 47:
			mIvWeather0.setImageResource(R.drawable.thunderstorm);
			break;
		default:
			mIvWeather0.setImageResource(R.drawable.sunny);
			break;
		}
	}

//	private void saveDateAndNumberOfWords() {
//		
//		//if date is the same as the saved date then keep the same word
//	    //String outputString = "Hello world!";
//		Calendar now = Calendar.getInstance();
//		SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd",
//				Locale.ENGLISH);
//		
//	    if(lastDate()==null){
//	    	//first time using app
//	    	saveDate(dff.format(now.getTime()));
//	    }else{
//	    	
//			//check if the last date saved is the same as todays
//			if(dff.format(now.getTime()).contains(lastDate())){
//				//load the same word
//			}else{
//				//load different word.
//				//save the new date
//				saveDate(dff.format(now.getTime()));
//			}
//	    }
//	    
//	}
	
private Boolean itsANewDay(){
	Calendar now = Calendar.getInstance();
	SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd",
			Locale.ENGLISH);
	String theTime=dff.format(now.getTime());
	if(lastDate()==null){
		//first time opening app
		saveDate(theTime);
    	return true;
    }else{


		//check if the last date saved is the same as todays
		if(theTime.contains(lastDate())){
			return false;
		}else{
			//load different word.
			//save the new date
			saveDate(dff.format(now.getTime()));
			return true;
		}
    }
}
	private void saveDate(String date){
		
		try {
	        FileOutputStream outputStream = openFileOutput("date.txt", Context.MODE_PRIVATE);
	        outputStream.write(date.getBytes());
	        outputStream.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	}
	private String lastDate(){
		try {
	        FileInputStream inputStream = openFileInput("date.txt");
	        BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
	        StringBuilder total = new StringBuilder();
	        String line;
	        while ((line = r.readLine()) != null) {
	            total.append(line);
	        }
	        r.close();
	        inputStream.close();
	        Log.d("File", "File contents: " + total);
	        return total.toString();
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
}