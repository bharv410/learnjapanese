package com.panafold;

import com.panafold.main.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TutorialFragment0 extends Fragment {

	 @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
       ViewGroup rootView = (ViewGroup) inflater.inflate(
               R.layout.tutorial_slide1, container, false);
       startActivity(new Intent(getActivity(),MainActivity.class));
       return rootView;
   }
}