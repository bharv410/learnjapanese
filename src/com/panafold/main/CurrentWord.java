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
	public static HashMap<String, Integer> getImage;

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

	public static void initHashMap() {
		getImage = new HashMap<String, Integer>();
		getImage.put("weather", R.drawable.weather);
		getImage.put("cellphone", R.drawable.cellphone);
		getImage.put("Child", R.drawable.child);
		getImage.put("home", R.drawable.home);
		getImage.put("Restaurant", R.drawable.resturaunt);
		getImage.put("hotel", R.drawable.hotel);
		getImage.put("hip hop", R.drawable.hiphop);

		getImage.put("Virtual reality", R.drawable.virtualreality);

		getImage.put("toilet", R.drawable.toilet);
		getImage.put("Television", R.drawable.television);

		getImage.put("taxi", R.drawable.taxi);
		getImage.put("Coffee", R.drawable.coffee);
		getImage.put("Spider", R.drawable.spider);
		getImage.put("Kilometer", R.drawable.kilometer);
		getImage.put("Internet", R.drawable.internet);
		getImage.put("Urban", R.drawable.urban);
		getImage.put("Apartment", R.drawable.apartment);
		getImage.put("Bad thing", R.drawable.badthing);
		getImage.put("understand", R.drawable.understand);
		getImage.put("Practice", R.drawable.practice);
		getImage.put("apple", R.drawable.apple);
		getImage.put("Camel", R.drawable.camel);
		getImage.put("\"Four, five, six\"", R.drawable.fourfivesix);
		getImage.put("Slowly", R.drawable.slowly);
		getImage.put("quit", R.drawable.quit);
		getImage.put("Problem", R.drawable.problem);
		getImage.put("Hello hello (phone)", R.drawable.hellohello);
		getImage.put("Once again", R.drawable.onceagain);
		getImage.put("Eyeglasses", R.drawable.eyeglasses);
		getImage.put("Business card", R.drawable.businesscard);
		getImage.put("Green", R.drawable.green);
		getImage.put("Short", R.drawable.short_);
		getImage.put("again", R.drawable.again);
		getImage.put("Chubby", R.drawable.chubby);
		getImage.put("Incompetent", R.drawable.incompetent);
		getImage.put("Winter", R.drawable.winter);
		getImage.put("Hospital", R.drawable.hospital);
		getImage.put("Beginning", R.drawable.beginning);
		getImage.put("Cat", R.drawable.cat);
		getImage.put("Japanese", R.drawable.japanese);
		getImage.put("\"first, given name\"", R.drawable.internet);
		getImage.put("what", R.drawable.what);
		getImage.put("Summer", R.drawable.summer);
		getImage.put("Long", R.drawable.long_);
		getImage.put("where", R.drawable.where);
		getImage.put("Please (offering)", R.drawable.pleaseoffering);
		getImage.put("Bird", R.drawable.bird);
		getImage.put("Friend", R.drawable.friend);
		getImage.put("Next to", R.drawable.nextto);
		getImage.put("Year", R.drawable.year);
		getImage.put("Tokyo", R.drawable.tokyo);
		getImage.put("assist", R.drawable.assist);
		getImage.put("Hand", R.drawable.hand);
		getImage.put("tired", R.drawable.tired);
		getImage.put("a little", R.drawable.alittle);
		getImage.put("Like very much", R.drawable.likeverymuch);
		getImage.put("food", R.drawable.food);
		getImage.put("Fun", R.drawable.fun);
		getImage.put("IÕm back", R.drawable.imback);
		getImage.put("provide critical assistance",
				R.drawable.providecriticalassistance);
		getImage.put("teacher", R.drawable.teacher);
		getImage.put("Life", R.drawable.life);
		getImage.put("adult", R.drawable.adult);
		getImage.put("sorry", R.drawable.sorry);
		getImage.put("manufactured", R.drawable.manufactured);
		getImage.put("Skillful", R.drawable.skillful);
		getImage.put("time", R.drawable.time);
		getImage.put("White", R.drawable.white);
		getImage.put("Book.", R.drawable.book);
		getImage.put("Hometown", R.drawable.hometown);
		getImage.put("question", R.drawable.question);
		getImage.put("Cold", R.drawable.cold);
		getImage.put("good evening", R.drawable.goodevening);
		getImage.put("Good Afternoon", R.drawable.goodafternoon);
		getImage.put("Spring", R.drawable.spring);
		getImage.put("this", R.drawable.this_);
		getImage.put("Thing", R.drawable.thing);
		getImage.put("\"This one, this way\"", R.drawable.thisonethisway);
		getImage.put("Highway", R.drawable.highway);
		getImage.put("English", R.drawable.english);
		getImage.put("\"fine, healthy\"", R.drawable.healthyfine);
		getImage.put("police", R.drawable.police);
		getImage.put("Black", R.drawable.black);
		getImage.put("Cloud", R.drawable.cloud);
		getImage.put("Socks", R.drawable.socks);
		getImage.put("\"Shoes, boots\"", R.drawable.shoesboots);
		getImage.put("Please (a favor)", R.drawable.pleasefavor);
		getImage.put("airport", R.drawable.airport);
		getImage.put("Today", R.drawable.today);
		getImage.put("dangerous", R.drawable.dangerous);
		getImage.put("Yellow", R.drawable.yellow);
		getImage.put("Feeling", R.drawable.feeling);
		getImage.put("Tree", R.drawable.tree);
		getImage.put("Student", R.drawable.student);
		getImage.put("\"cool, stylish\"", R.drawable.cool);
		getImage.put("Write", R.drawable.write);
		getImage.put("office worker", R.drawable.officeworker);
		getImage.put("idea", R.drawable.idea);
		getImage.put("hungry", R.drawable.hunfry);
		getImage.put("Tea", R.drawable.tea);
		getImage.put("welcome(home)", R.drawable.welcomehome);
		getImage.put("Yen", R.drawable.yen);
		getImage.put("railway station", R.drawable.railwaystation);
		getImage.put("Movies", R.drawable.movies);
		getImage.put("Horse", R.drawable.horse);
		getImage.put("clothes", R.drawable.clothes);
		getImage.put("Dog", R.drawable.dog);
		getImage.put("Someday", R.drawable.someday);
		getImage.put("when", R.drawable.when);
		getImage.put("\"One, two, three\"", R.drawable.onetwothree);
		getImage.put("Bon appetit.", R.drawable.bonappetit);
		getImage.put("do", R.drawable.do_);
		getImage.put("thanks", R.drawable.thanks);
		getImage.put("Hot", R.drawable.hot);
		getImage.put("tomorrow", R.drawable.tomorrow);
		getImage.put("Autumn", R.drawable.autumn);
		getImage.put("Red", R.drawable.red);
		getImage.put("Blue", R.drawable.blue);
		getImage.put("\"Seven, eight, nine, ten.\"", R.drawable.seveneightnineten);
		getImage.put("Hat", R.drawable.hat);
		getImage.put("Congratulations", R.drawable.congratulations);

	}
}
