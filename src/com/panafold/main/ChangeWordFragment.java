package com.panafold.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.panafold.R;

public class ChangeWordFragment extends ListFragment {
	String[] values;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_games, container, false);
		values = new String[] { "Banana", "Watermelon", "Butterfly",
		        "Normal", "Red", "Summer", "Apple", "Tangerine",
		        "Hello", "Goodbye" };
		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_list_item_1, values);
		    setListAdapter(adapter);
		    return rootView;
		  }

		  @Override
		  public void onListItemClick(ListView l, View v, int position, long id) {
			  CurrentWord.currentEnglishWord=values[position];
			  Intent i = new Intent(getActivity(),MainActivity.class);
			  startActivity(i);
			  
		  }
		} 