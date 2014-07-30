package com.panafold.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
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
import android.widget.Toast;

import com.panafold.R;
import com.panafold.main.datamodel.Word;

public class ChangeWordFragment extends ListFragment {
	// Search EditText
	EditText inputSearch;
	WordAdapter adapter;
	ArrayList<String> values;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// add allwords to array for list
		values = new ArrayList<String>();
		for (Word w : CurrentWord.allWords) {
			values.add(w.getEnglish());
		}

		View rootView = inflater.inflate(R.layout.fragment_reviewwords,
				container, false);
		adapter = new WordAdapter(getActivity(),R.layout.list_item, CurrentWord.allWords);
		setListAdapter(adapter);
		
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		inputSearch = (EditText) getActivity().findViewById(R.id.inputSearch);
		inputSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// When user changed the Text
				ChangeWordFragment.this.adapter.getFilter().filter(cs);
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		for (Word w : CurrentWord.allWords) {
			if (w.getEnglish().contains(((TextView) v).getText().toString())) {
				CurrentWord.theCurrentWord = w;
				Toast.makeText(getActivity(), CurrentWord.theCurrentWord.getEnglish(),
						Toast.LENGTH_SHORT).show();
			}
		}
		

		final String text = ((TextView) v).getText().toString();
		// CurrentWord.currentEnglishWord=text;
		Intent i = new Intent(getActivity(), MainActivity.class);
		startActivity(i);

	}
	
	
	public class WordAdapter extends ArrayAdapter<Word> {

	    private Context context;

	    public WordAdapter(Context context, int textViewResourceId, List<Word> items) {
	        super(context, textViewResourceId, items);
	        this.context = context;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        View view = convertView;
	        if (view == null) {
	            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = inflater.inflate(R.layout.list_item, null);
	            
	        }

	        Word item = getItem(position);
	        if (item!= null) {
	            // My layout has only one TextView
	            TextView itemView = (TextView) view.findViewById(R.id.wordtext);
	            if (itemView != null) {
	                // do whatever you want with your string and long
	                itemView.setText(item.getEnglish()+"yay");
	            }
	         }

	        return view;
	    }
	}
}