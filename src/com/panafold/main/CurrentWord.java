package com.panafold.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;

import com.panafold.R;
import com.panafold.main.datamodel.Word;

public class CurrentWord {
public static Word theCurrentWord;
public static List<Word> allWords;
public static String weatherString;
public static HashMap<String,Integer> getImage;
	public static String translate(String text) throws IOException {
		// fetch
		URL url = new URL(
				"http://translate.google.com.tw/translate_a/t?client=t&hl=en&sl=en&tl=ja&ie=UTF-8&oe=UTF-8&multires=1&oc=1&otf=2&ssel=0&tsel=0&sc=1&q="
						+ URLEncoder.encode(text, "UTF-8"));
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("User-Agent", "Something Else");
		BufferedReader br = new BufferedReader(new InputStreamReader(
				urlConnection.getInputStream()));
		String result = br.readLine();
		br.close();
		// parse
		// System.out.println(result);
		result = result.substring(2, result.indexOf("]]") + 1);
		StringBuilder sb = new StringBuilder();
		String[] splits = result.split("(?<!\\\\)\"");
		for (int i = 1; i < splits.length; i += 8)
			sb.append(splits[i]);
		return sb.toString().replace("\\n", "\n").replaceAll("\\\\(.)", "$1");
	}

public static void initHashMap(){
	getImage= new HashMap<String,Integer>();
	getImage.put("weather", R.drawable.weather);
	getImage.put("cellphone", R.drawable.cellphone);
	getImage.put("23", R.drawable.weather);
}
}
