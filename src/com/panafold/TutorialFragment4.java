package com.panafold;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TutorialFragment4  extends Fragment {

	 @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
       ViewGroup rootView = (ViewGroup) inflater.inflate(
               R.layout.tutorial_slide4, container, false);
       return rootView;
   }
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        
//	        Button rl = (Button)getActivity().findViewById(R.id.slide4);
//	        rl.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					if(CurrentWord.theCurrentWord!=null){
//						getActivity().finish();
//					}else{
//						startActivity(new Intent(getActivity(),MainActivity.class));
//					}
//									
//				}
//	        	
//	        });
	    }
}