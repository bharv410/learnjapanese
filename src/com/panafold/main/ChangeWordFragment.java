package com.panafold.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.panafold.R;
import com.panafold.main.datamodel.Word;

public class ChangeWordFragment extends ListFragment {
	// Search EditText
	EditText inputSearch;
	WordAdapter adapter;
	List<String> wordsForAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// add allwords to array for list
//		values = new ArrayList<String>();
//		for (Word w : CurrentWord.allWords) {
//			values.add(w.getEnglish());
//		}
		wordsForAdapter= new ArrayList<String>();
		for(Word w:CurrentWord.allWords){
			wordsForAdapter.add(w.getEnglish());
		}
		
		
		View rootView = inflater.inflate(R.layout.fragment_reviewwords,
				container, false);
		adapter = new WordAdapter(getActivity(),R.layout.list_item, wordsForAdapter);
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
		TextView tv=(TextView)v.findViewById(R.id.wordtext);
		final String text = tv.getText().toString();


		for (Word w : CurrentWord.allWords) {
			if (w.getEnglish().contains(text)) {
				CurrentWord.theCurrentWord = w;
			}
		}
		
		

		
		// CurrentWord.currentEnglishWord=text;
		Intent i = new Intent(getActivity(), MainActivity.class);
		startActivity(i);

	}
	
	
	public class WordAdapter extends ArrayAdapter<String> implements Filterable{

	    private Context context;

	    public WordAdapter(Context context, int textViewResourceId, List<String> items) {
	        super(context, textViewResourceId, items);
	        this.context = context;
	    }

	    public View getView(int position, View convertView, ViewGroup parent) {
	        View view = convertView;
	        if (view == null) {
	            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	            view = inflater.inflate(R.layout.list_item, null);
	        }

	        String item = getItem(position);
	        if (item!= null) {
	            // My layout has only one TextView
	            TextView itemView = (TextView) view.findViewById(R.id.wordtext);
	            if (itemView != null) {
	                // do whatever you want with your string and long
	                itemView.setText(item);
	            }
	            ImageView iv =(ImageView)view.findViewById(R.id.list_image);
	            if(iv!=null){
	            	System.out.println(item);
	            	
	            	BitmapFactory.Options options = new BitmapFactory.Options();
					options.inPurgeable = true; // inPurgeable is used to free up
												// memory while required
					Bitmap bm = drawableToBitmap(getResources().getDrawable(CurrentWord.getImage.get(item)));
					Bitmap bm2 = Bitmap.createScaledBitmap(bm, bm.getWidth()/3,
							bm.getHeight()/3, true);
	            	
	            	iv.setImageBitmap(bm2);
	            }
	         }

	        return view;
	    }
	}
	public  Bitmap drawableToBitmap (Drawable drawable) {
	    if (drawable instanceof BitmapDrawable) {
	        return ((BitmapDrawable)drawable).getBitmap();
	    }

	    Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(bitmap); 
	    drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
	    drawable.draw(canvas);

	    return bitmap;
	}
}