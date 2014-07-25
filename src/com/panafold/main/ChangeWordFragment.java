package com.panafold.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.panafold.R;

public class ChangeWordFragment extends ListFragment {
	String[] values;
	// Search EditText
    EditText inputSearch;
    ArrayAdapter<String> adapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_reviewwords, container, false);
		values = new String[] { "Banana", "Watermelon", "Butterfly",
		        "Normal", "Red", "Summer", "Apple", "Tangerine",
		        "Hello", "Goodbye" };
		    adapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_list_item_1, values);
		    setListAdapter(adapter);
		    return rootView;
		  }
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {       
        super.onActivityCreated(savedInstanceState);
        inputSearch = (EditText)getActivity().findViewById(R.id.inputSearch);
	    inputSearch.addTextChangedListener(new TextWatcher() {
	        
	        @Override
	        public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
	            // When user changed the Text
	            ChangeWordFragment.this.adapter.getFilter().filter(cs);   
	        }
	         
	        @Override
	        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
	                int arg3) {		             
	        }
	         
	        @Override
	        public void afterTextChanged(Editable arg0) {
	        }
	    });
    }

		  @Override
		  public void onListItemClick(ListView l, View v, int position, long id) {
			  
			  final String text = ((TextView)v).getText().toString();
			  //CurrentWord.currentEnglishWord=text;
			  Intent i = new Intent(getActivity(),MainActivity.class);
			  startActivity(i);
			  
		  }
		} 