package com.panafold.main;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.panafold.R;

public class CurrentWordFragment extends Fragment {
	ProgressBar weatherPB;
	ShowcaseView sv;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_imageandword, container, false);
		
		
//		
//		new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            	//setup overlay
//        		sv = new ShowcaseView.Builder(getActivity())
//                .setTarget(new ViewTarget(getActivity().findViewById(R.id.japaneseTextView)))
//                .setContentTitle("Japanese Translations")
//    .setContentText("Click to toggle between Japanese translations. The top switches between the english pronunciation(Romaji) " +
//    		"and the Japanese spelling(Hirigana). The bottom is always the official Japanese symbol(Kanji)")
//    .hideOnTouchOutside()
//                .build();
//        		sv.setOnShowcaseEventListener(new OnShowcaseEventListener() {
//       			 @Override
//       			    public void onShowcaseViewHide(ShowcaseView showcaseView) {
//       				 sv = new ShowcaseView.Builder(getActivity())
//       	                .setTarget(new ViewTarget(getActivity().findViewById(R.id.button1)))
//       	                .setContentTitle("Click to play words")
//       	    .setContentText("Click to hear the japenese sound of the word")
//       	                .build();
//       				sv.setOnShowcaseEventListener(new OnShowcaseEventListener() {
//           			 @Override
//           			    public void onShowcaseViewHide(ShowcaseView showcaseView) {
//           				 sv = new ShowcaseView.Builder(getActivity())
//           	                .setTarget(new ViewTarget(getActivity().findViewById(R.id.englishTextView)))
//           	                .setContentTitle("How to use:")
//           	    .setContentText("Each day you'll get a new word.After a while you'll be reminded to review learned words" +
//           	    		"by being added to the review section to the left. Check for phrases to the right and related videos")
//           	    		.hideOnTouchOutside()
//           	                .build();
//           			    }
//           			 @Override
//           			    public void onShowcaseViewShow(ShowcaseView showcaseView) {
//           			        //The view is shown
//           			    }
//   					@Override
//   					public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
//   						// TODO Auto-generated method stub
//   						
//   					}
//           			});
//       			    }
//
//       			 @Override
//       			    public void onShowcaseViewShow(ShowcaseView showcaseView) {
//       			        //The view is shown
//       			    }
//
//					@Override
//					public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
//						// TODO Auto-generated method stub
//						
//					}
//       			});
//        		
//            }
//        }, 100);
		return rootView;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {       
        super.onActivityCreated(savedInstanceState);
        
      //set big pic image to the corresponding resource in the static hashmap
      		ImageView bigPic=(ImageView)getActivity().findViewById(R.id.bigPicImageView);
      		bigPic.setImageDrawable(getResources().getDrawable(CurrentWord.getImage.get(CurrentWord.theCurrentWord.getEnglish())));
      		
        
        
        //set english text
        TextView text = (TextView) getActivity().findViewById(R.id.englishTextView);
        text.setTypeface(MainActivity.gothamFont);
		text.setText(CurrentWord.theCurrentWord.getEnglish());
		//set thejapenese text
		TextView text2 = (TextView) getActivity().findViewById(R.id.japaneseTextView);
		text2.setText(CurrentWord.theCurrentWord.getKanji());
        
        //initialize progress bar
//        weatherPB=(ProgressBar)getActivity().findViewById(R.id.weatherProgressBar);
//        weatherPB.setVisibility(ProgressBar.VISIBLE);
        
        //set current date and time 
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("F MMMM",Locale.US);
        SimpleDateFormat day_date = new SimpleDateFormat("EEE",Locale.US);
        String month_name = month_date.format(cal.getTime());
        String day_name = day_date.format(cal.getTime());
        String daytext = day_name.toUpperCase(Locale.US);
        TextView dateview = (TextView) getActivity().findViewById(R.id.dateTextView);
        dateview.setTypeface(MainActivity.gothamFont);
		dateview.setText(month_name + "\n " +daytext);
		MainActivity.shown=0; 
		
		//if you click the bottom part of screen it will toggle between date or japanese translation
		RelativeLayout relativeclic1 =(RelativeLayout)getActivity().findViewById(R.id.relativeLayout1);
		
		//set background to the current random color
		relativeclic1.setBackgroundColor(Color.parseColor(CurrentWord.currentColor));
		
		//set on click listener to change textviews
        relativeclic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            	ProgressBar weatherPB = (ProgressBar) getActivity().findViewById(R.id.weatherProgressBar);
    			weatherPB.setVisibility(ProgressBar.GONE);
    			TextView romTextView = (TextView)getActivity().findViewById(R.id.romajiOrHiriganaTextView);
		    	
            	if(MainActivity.shown%3==0){
    	    		TextView japanTextView = (TextView)getActivity().findViewById(R.id.japaneseTextView);
    	    		japanTextView.setTypeface(MainActivity.japaneseFont);
    		    	japanTextView.setVisibility(View.VISIBLE);
    		    	romTextView.setVisibility(View.VISIBLE);
    		    	romTextView.setTypeface(MainActivity.neutrafaceFont);
    		    	String romText = CurrentWord.theCurrentWord.getRomaji().replace("\r\n", " ").replace("\n", " ");
    		    	romTextView.setText(romText);
    		    	
    		    	//show japanese and hide weather and date and line
    		    	TextView dateView = (TextView)getActivity().findViewById(R.id.dateTextView);
    		    	dateView.setVisibility(View.INVISIBLE);
    		    	TextView weatherTextView =(TextView)getActivity().findViewById(R.id.weatherTextView);
    		    	weatherTextView.setVisibility(View.INVISIBLE);
    		    	ImageView lineImageView =(ImageView)getActivity().findViewById(R.id.lineImageView);
    		    	lineImageView.setVisibility(View.INVISIBLE);
    		    	ImageView cloudIconImageView =(ImageView)getActivity().findViewById(R.id.imageView2);
    		    	cloudIconImageView.setVisibility(View.INVISIBLE);
    	    		MainActivity.shown++;
    	    	}else if(MainActivity.shown%3==1){
    	    		String romText2 = CurrentWord.theCurrentWord.getHirigana().replace("\r\n", " ").replace("\n", " ");
    		    	romTextView.setText(romText2);
    		    	romTextView.setTypeface(MainActivity.japaneseFont);
    		    	MainActivity.shown++;MainActivity.shown++;
    	    	}else{
    	    		TextView japanTextView = (TextView)getActivity().findViewById(R.id.japaneseTextView);
    		    	japanTextView.setVisibility(View.INVISIBLE);
    		    	romTextView.setVisibility(View.INVISIBLE);
    		    	//hide japanese and show weather and date and line
    		    	TextView dateView = (TextView)getActivity().findViewById(R.id.dateTextView);
    		    	dateView.setVisibility(View.VISIBLE);
    		    	TextView weatherTextView =(TextView)getActivity().findViewById(R.id.weatherTextView);
    		    	weatherTextView.setVisibility(View.VISIBLE);
    		    	ImageView lineImageView =(ImageView)getActivity().findViewById(R.id.lineImageView);
    		    	lineImageView.setVisibility(View.VISIBLE);
    		    	ImageView cloudIconImageView =(ImageView)getActivity().findViewById(R.id.imageView2);
    		    	cloudIconImageView.setVisibility(View.VISIBLE);
    	    		MainActivity.shown++;
    	    	}
            }
        });
      //start weather progressbar to indicate loading
        if(CurrentWord.weatherString==null){
      		weatherPB = (ProgressBar) getActivity().findViewById(R.id.weatherProgressBar);
      		weatherPB.setVisibility(ProgressBar.VISIBLE);
        }else{
        	TextView weatherTextView = (TextView)getActivity().findViewById(R.id.weatherTextView);
        	weatherTextView.setTypeface(MainActivity.gothamFont);
			weatherTextView.setText(CurrentWord.weatherString);
        }
//        new ShowcaseView.Builder(getActivity())
//	    .setTarget(new ViewTarget(R.id.englishTextView,getActivity()))
//	    .setContentTitle("ShowcaseView")
//	    .setContentText("This is highlighting the Home button")
//	    .hideOnTouchOutside()
//	    .build();
       
    }
}
