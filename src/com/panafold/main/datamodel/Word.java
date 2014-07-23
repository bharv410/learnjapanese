package com.panafold.main.datamodel;

public class Word {
String english,romaji,hirigana,kanji,phrase, imageUrl;

public Word(String eng,String rom,String hir,String kan,String phras, String url){
	this.english=eng;
	this.romaji=rom;
	this.hirigana=hir;
	this.kanji=kan;
	this.phrase=phras;
	this.imageUrl=url;
}
public String getEnglish(){
	return this.english;
}
public String getRomaji(){
	return this.romaji;
}
public String getHirigana(){
	return this.hirigana;
}
public String getKanji(){
	return this.kanji;
}
public String getPhrase(){
	return this.phrase;
}
public String getUrl(){
	return this.imageUrl;
}
}
