package com.panafold;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class TutorialFragment1 extends Fragment {

	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.tutorial_slide1, container, false);
        return rootView;
    }
	 
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        
	        RelativeLayout rl = (RelativeLayout)getActivity().findViewById(R.id.slide1);
	        rl.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					TutorialActivity act = (TutorialActivity)getActivity();
					act.nextPage();					
				}
	        	
	        });
	    }
}