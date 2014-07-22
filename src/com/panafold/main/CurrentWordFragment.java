package com.panafold.main;

import java.io.IOException;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.panafold.R;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CurrentWordFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_imageandword, container, false);
		return rootView;
	}
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {       
        super.onActivityCreated(savedInstanceState);
        
        //translate current word into japanese
        new TranslateNow().execute(CurrentWord.currentEnglishWord);
        
        
        //set current date and time 
        Calendar cal=Calendar.getInstance();
        SimpleDateFormat month_date = new SimpleDateFormat("F MMMM\nEEE",Locale.US);
        String month_name = month_date.format(cal.getTime());
        TextView dateview = (TextView) getActivity().findViewById(R.id.dateTextView);
		dateview.setText(month_name);
		MainActivity.dateShown=true; 
		
		//if you click the bottom part of screen it will toggle between date or japanese translation
		RelativeLayout relativeclic1 =(RelativeLayout)getActivity().findViewById(R.id.relativeLayout1);
        relativeclic1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
            	if(MainActivity.dateShown){
    	    		TextView japanTextView = (TextView)getActivity().findViewById(R.id.japaneseTextView);
    		    	japanTextView.setVisibility(View.VISIBLE);
    		    	//show japanese and hide weather and date
    		    	TextView dateView = (TextView)getActivity().findViewById(R.id.dateTextView);
    		    	dateView.setVisibility(View.INVISIBLE);
    		    	TextView weatherTextView =(TextView)getActivity().findViewById(R.id.weatherTextView);
    		    	weatherTextView.setVisibility(View.INVISIBLE);
    	    		MainActivity.dateShown=false;
    	    	}else{
    	    		TextView japanTextView = (TextView)getActivity().findViewById(R.id.japaneseTextView);
    		    	japanTextView.setVisibility(View.INVISIBLE);
    		    	//hide japanese and show weather and date
    		    	TextView dateView = (TextView)getActivity().findViewById(R.id.dateTextView);
    		    	dateView.setVisibility(View.VISIBLE);
    		    	TextView weatherTextView =(TextView)getActivity().findViewById(R.id.weatherTextView);
    		    	weatherTextView.setVisibility(View.VISIBLE);
    		    	MainActivity.dateShown=true;
    	    	}
            }
        });
        
    }
	private class TranslateNow extends AsyncTask<String, Integer, String> {
		protected String doInBackground(String... word) {
			String translateThisWord = word[0];
			try {
				//translate current english to japanese
				CurrentWord.currentJapeneseWord=CurrentWord.translate(translateThisWord);
				return CurrentWord.currentJapeneseWord;
			} catch (IOException e) {
				return null;
			}
		}
		protected void onPostExecute(String result) {
			//set both the english and japenese text
			TextView text = (TextView) getActivity().findViewById(R.id.englishTextView);
			text.setText(CurrentWord.currentEnglishWord);
			
			TextView text2 = (TextView) getActivity().findViewById(R.id.japaneseTextView);
			text2.setText(CurrentWord.currentJapeneseWord);
			
		}
	}
}
