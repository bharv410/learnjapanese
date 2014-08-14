package com.panafold;

import com.panafold.main.CurrentWord;
import com.panafold.main.MainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

public class TutorialActivity extends FragmentActivity {

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;
    public static int currentImage,currentPageNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.viewpagerfortutorial);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

public void exitTutorial(View v){
	
		startActivity(new Intent(TutorialActivity.this,MainActivity.class));
		finish();
	
	}
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	
        	
        	switch(position){
        	case 0: return new TutorialFragment1();

        	case 1: return new TutorialFragment2();
        	
        	case 2: return new TutorialFragment3();
        	
        	case 3: return new TutorialFragment4();
        	        	
        	}
			return null;
        	 

        	}
            
        @Override
        public int getCount() {
            return 4;
        }
    }
    
    public void showImage(){
    	
    }
    
    public void nextPage(){
    	try{
    	mPager.setCurrentItem(mPager.getCurrentItem() + 1);
    	}catch(Exception e){
    		System.out.print("Out of memory error");
    	}
    }
}