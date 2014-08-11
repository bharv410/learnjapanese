package com.panafold.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import zh.wang.android.apis.yweathergetter4a.WeatherInfo;
import zh.wang.android.apis.yweathergetter4a.YahooWeather;
import zh.wang.android.apis.yweathergetter4a.YahooWeatherInfoListener;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;
import at.theengine.android.bestlocation.BestLocationListener;
import at.theengine.android.bestlocation.BestLocationProvider;
import at.theengine.android.bestlocation.BestLocationProvider.LocationType;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.panafold.AboutPageActivity;
import com.panafold.InAppPurchaseActivity;
import com.panafold.R;
import com.panafold.TutorialActivity;
import com.panafold.adapter.TabsPagerAdapter;
import com.panafold.main.datamodel.LocalDBHelper;
import com.panafold.main.datamodel.ReviewWord;
import com.panafold.main.datamodel.SqlLiteDbHelper;
import com.panafold.main.datamodel.Word;
import com.viewpagerindicator.LinePageIndicator;

public class MainActivity extends FragmentActivity implements
		TextToSpeech.OnInitListener, YahooWeatherInfoListener, BillingProcessor.IBillingHandler{
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
	private Boolean currentWordIsSet,supportsTextToSpeech;
	BillingProcessor bp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		supportsTextToSpeech=false;
		
		String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqiLU0GwvBQu7VQTN821qMfmjaec2DKksSfXU8klufTp8H0nPoVnufdb87W5PVIttNWfOQK+3SO+ZTfPNCPYZWf5RBDR9U6Km/jMPxhQ526NdYf9Q4PyBBJDlo96ycDxBdjgi7yoCSfdVsCKgBuThAjsdcUmHrdRMAQIBN9b8IGFH2lhtgQHHbvHXz9k4Vyx/xjMw3YJHaOmh9RtZTKB944u9i1AFVa+YCisvVabeIafV+vcG2D2LdyucWcuG+3LROn8EZhyC3ByJNuexebTKg/7KqWD826bh6o5Wg0AnOa2AdnsyXl18S19oZ44QkKfM7IOpSlB+W4JqXbc7gDaxkwIDAQAB";
		bp = new BillingProcessor(this, null, this);
		
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
		
		setupFonts();
		setupDatabases();
		
		setWordsThatShouldBeReviewed();
		addSomeWords();
		showCorrectWord();
		initLocation();
		// get location for waether info
		mBestLocationProvider
				.startLocationUpdatesWithListener(mBestLocationListener);

	}

	private void setupFonts() {
		// add custom fonts
		gothamFont = Typeface.createFromAsset(getAssets(),
				"fonts/Gotham-Book.ttf");
		neutrafaceFont = Typeface.createFromAsset(getAssets(),
				"fonts/NeutraText-Demi.otf");
		japaneseFont = Typeface.createFromAsset(getAssets(),
				"fonts/AozoraMinchoMedium.ttf");
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
		 if (bp != null) 
	            bp.release();
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
				supportsTextToSpeech=false;
				
			} else {
				supportsTextToSpeech=true;
				// speakOut();
			}

		} else {
			Log.e("TTS", "Initilization Failed!");
		}

	}

	private void setupDatabases() {
		try {
			// init databases
			dynamicdb = new LocalDBHelper(MainActivity.this);
			SqlLiteDbHelper dbhelper = new SqlLiteDbHelper(MainActivity.this);

			// open db
						dbhelper.CopyDataBaseFromAsset();
						dbhelper.openDataBase();
						
			// init static objects
			CurrentWord.allWords = dbhelper.getAllWords();
			CurrentWord.previouslySavedWords = new ArrayList<ReviewWord>();
			CurrentWord.previouslySavedStrings = new ArrayList<String>();

			
			for (ReviewWord r : dynamicdb.getReviewWords()) {
				CurrentWord.previouslySavedStrings.add(r.getEnglish());
				CurrentWord.previouslySavedWords.add(r);
			}
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private void setWordsThatShouldBeReviewed() {
		CurrentWord.shouldBeBold = new ArrayList<String>();

		//check all words and if haven't been viewed in 9 days. add to shouldBeBold list for review page
		for (ReviewWord r : CurrentWord.previouslySavedWords) {
			try {
				String timestampOfWord = r.getTimeStamp();
				Date wordTimeStamp = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss").parse(timestampOfWord);
				

				Calendar calendar = Calendar.getInstance();
				calendar.add(Calendar.DATE, -3);
				Date nineDaysPrior = calendar.getTime();

				
				if (wordTimeStamp.before(nineDaysPrior)) {
					System.out.println(r.getEnglish()
							+ "hasn't been clicked in 9days");
					CurrentWord.shouldBeBold.add(r.getEnglish());
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
	}

	private void showCorrectWord() {
		for (Word w : CurrentWord.allWords) {

			// if no word has been chosen. and w isnt in review list.
			// then we found the word that should be shown next
			if (!currentWordIsSet
					&& (!CurrentWord.previouslySavedStrings
							.contains(w.getEnglish()))) {

				// if no word has been chosen and its a new day. then show new
				// word and save to db
				if (CurrentWord.theCurrentWord == null && itsANewDay()) {
					CurrentWord.theCurrentWord = w;
					dynamicdb.addWord(CurrentWord.theCurrentWord);

				} else if (CurrentWord.theCurrentWord == null
						&& (!itsANewDay())) {
					// no word was chosen. but its the same day
					for (Word wrd : CurrentWord.allWords) {
						// show the same word as earlier that day
						if (wrd.getEnglish().contains(
								CurrentWord.previouslySavedWords
										.get(CurrentWord.previouslySavedWords
												.size() - 1).getEnglish())) {
							CurrentWord.theCurrentWord = wrd;
						}
					}
				}
				currentWordIsSet = true;
			}
		}
	}

	public void speakOut(View v) {
		// speak the japanese text
		
		
		if(!supportsTextToSpeech){
			Toast.makeText(getApplicationContext(), "Your device does not support Japanese speech-to-text", Toast.LENGTH_LONG).show();
			supportsTextToSpeech=true;
		}
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
						+ " °";
				weatherTextView.setText(CurrentWord.weatherString);
			} catch (Exception e) {
				System.out.println("Exited app");
			}
		}
	}
	

	private void setWeatherIcon(int code) {
		mIvWeather0 = (ImageView) findViewById(R.id.imageView2);
if(mIvWeather0!=null){
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
	}
private void addSomeWords(){
	CurrentWord.beginningReviewWords = new ArrayList<Word>();
	CurrentWord.beginningReviewWords.add(new Word("give critical assistance",
			"tasukete", "たすけて", "助けて", 0, "Thank you for helping me.",
			"助 (たす)けてくれてありがとうございます。", "providecriticalassistance",
			"Tasukete kurete arigatō gozaimasu.", 0,
			"Help by Pieter J. Smits from the thenounproject.com"));
	CurrentWord.allWords.add(new Word("give critical assistance",
			"tasukete", "たすけて", "助けて", 0, "Thank you for helping me.",
			"助 (たす)けてくれてありがとうございます。", "providecriticalassistance",
			"Tasukete kurete arigatō gozaimasu.", 0,
			"Help by Pieter J. Smits from the thenounproject.com"));
	
	CurrentWord.beginningReviewWords.add(new Word("business card", "meishi",
			"めいし", "名刺", 0, "Here is my business card.", "私の名刺です。",
			"businesscard", "Watashi no meishidesu.", 0,
			"one, two, three"));
	CurrentWord.allWords.add(new Word("business card", "meishi",
			"めいし", "名刺", 0, "Here is my business card.", "私の名刺です。",
			"businesscard", "Watashi no meishidesu.", 0,
			"one, two, three"));

	CurrentWord.beginningReviewWords.add(new Word("one, two, three",
			"Ichi, ni, san", "いち、に、さん", "一 二 三 ", 0, "From scratch.",
			"いちから 。", "onetwothree", " Ichi kara. ", 0, ""));
	CurrentWord.allWords.add(new Word("one, two, three",
			"Ichi, ni, san", "いち、に、さん", "一 二 三 ", 0, "From scratch.",
			"いちから 。", "onetwothree", " Ichi kara. ", 0, ""));
	
	CurrentWord.beginningReviewWords
			.add(new Word("coffee", "kōhī", "コーヒー", "珈琲", 0,
					"Coffee grinder.", "コーヒーミル 。", "coffee",
					"Kōhīmiru.", 0,
					"Coffee Maker by Antonieta Gomez from the thenounproject.com"));
	CurrentWord.allWords.add(new Word("coffee", "kōhī", "コーヒー", "珈琲", 0,
			"Coffee grinder.", "コーヒーミル 。", "coffee",
			"Kōhīmiru.", 0,
			"Coffee Maker by Antonieta Gomez from the thenounproject.com"));
	

	CurrentWord.beginningReviewWords.add(new Word("cat", "neko", "ねこ", "猫",
			0, " Kitten.", "こねこ。", "cat", "Koneko.", 0,
			"Cat by Ramburu from the thenounproject.com"));
	CurrentWord.allWords.add(new Word("cat", "neko", "ねこ", "猫",
			0, " Kitten.", "こねこ。", "cat", "Koneko.", 0,
			"Cat by Ramburu from the thenounproject.com"));
	
	CurrentWord.beginningReviewWords.add(new Word("spider", "kumo", "クモ",
			"蜘蛛", 0, " Spider web.", "くものす。", "spider", "Kumo no su.",
			0, "Spider by Johnbosco Ng from the thenounproject.com"));
	CurrentWord.allWords.add(new Word("spider", "kumo", "クモ",
			"蜘蛛", 0, " Spider web.", "くものす。", "spider", "Kumo no su.",
			0, "Spider by Johnbosco Ng from the thenounproject.com"));
	
	CurrentWord.beginningReviewWords.add(new Word("white"	, "shiro"	, "しろ", "白 ", 0,
			" Interesting.", "面白い 。", "white", "Omoshiroi.", 0, null));
	CurrentWord.allWords.add(new Word("white"	, "shiro"	, "しろ", "白 ", 0,
			" Interesting.", "面白い 。", "white", "Omoshiroi.", 0, null));
	
}
	// private void saveDateAndNumberOfWords() {
	//
	// //if date is the same as the saved date then keep the same word
	// //String outputString = "Hello world!";
	// Calendar now = Calendar.getInstance();
	// SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd",
	// Locale.ENGLISH);
	//
	// if(lastDate()==null){
	// //first time using app
	// saveDate(dff.format(now.getTime()));
	// }else{
	//
	// //check if the last date saved is the same as todays
	// if(dff.format(now.getTime()).contains(lastDate())){
	// //load the same word
	// }else{
	// //load different word.
	// //save the new date
	// saveDate(dff.format(now.getTime()));
	// }
	// }
	//
	// }

	private Boolean itsANewDay() {
		Calendar now = Calendar.getInstance();
		SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd",
				Locale.ENGLISH);
		String theTime = dff.format(now.getTime());
		if (lastDate() == null) {
			// first time opening app
			saveDate(theTime);
			return true;
		} else {

			// check if the last date saved is the same as todays
			if (theTime.contains(lastDate())) {
				return false;
			} else {
				// load different word.
				// save the new date
				saveDate(dff.format(now.getTime()));
				return true;
			}
		}
	}

	private void saveDate(String date) {
		try {
			FileOutputStream outputStream = openFileOutput("date.txt",
					Context.MODE_PRIVATE);
			outputStream.write(date.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String lastDate() {
		try {
			FileInputStream inputStream = openFileInput("date.txt");
			BufferedReader r = new BufferedReader(new InputStreamReader(
					inputStream));
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
			Calendar now = Calendar.getInstance();
			SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd",
					Locale.ENGLISH);
			String theTime = dff.format(now.getTime());
			saveDate(theTime);
			e.printStackTrace();
			return null;
		}
	}
	private void showDialog(){
    	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				MainActivity.this);
 
			// set title
			alertDialogBuilder.setTitle("Purchase More Words?");
 
			// set dialog message
			alertDialogBuilder
				.setMessage("Do you want to purchase all words and phrases rather than recieving 1 per day?")
				.setCancelable(false)
				.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						
						bp.purchase("com.panafold.allwords");
						System.out.println("clicked");
					}
				  })
				.setNegativeButton("No",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
						}
				});
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
    }
	public void goToTutorialPage(View v) {
		startActivity(new Intent(this, TutorialActivity.class));
	}

	public void aboutPage(View v) {
		startActivity(new Intent(MainActivity.this, AboutPageActivity.class));
	}

	public void getMoreWords(View v) {
		showDialog();
	}
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
    }
	@Override
    public void onBillingInitialized() {
        System.out.println("onBillingInitialized");
    }

    @Override
    public void onProductPurchased(String productId) {
        /*
         * Called then requested PRODUCT ID was successfully purchased
         */
    	System.out.println("onProductPurchased");
    	
    	LocalDBHelper dynamicdb = new LocalDBHelper(MainActivity.this);
		dynamicdb.getWritableDatabase();
		
    	for(Word w : CurrentWord.allWords){
    		if(CurrentWord.previouslySavedStrings.contains(w.getEnglish())){
    			//if word is already saved do nothing
    		}else{
				dynamicdb.addWord(w);
    		}
    		
    	}
    	
    	
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        /*
         * Called then some error occured. See Constants class for more details
         */
    	System.out.println("onBillingError");
    }

    @Override
    public void onPurchaseHistoryRestored() {
        /*
         * Called then purchase history was restored and the list of all owned PRODUCT ID's 
         * was loaded from Google Play
         */
    	System.out.println("onPurchaseHistoryRestored");
    }
}