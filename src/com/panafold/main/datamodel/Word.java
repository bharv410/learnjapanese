package com.panafold.main.datamodel;

public class Word {
	String english, romaji, hirigana, kanji, japPhrase, englPhrase, romPhrase,imageUrl;

	public Word(String eng, String rom, String hir, String kan,
			int unlock, String engPhras, String japPhras, String image
			,String romajiPhrase,int timestamp, String attr) {
		this.english = eng;
		this.romaji = rom;
		this.hirigana = hir;
		this.kanji = kan;
//		this.unlocked=unlock;
//		this.siteurl=site;
		this.englPhrase = engPhras;
		this.japPhrase = japPhras;
		this.imageUrl = image;
		this.romPhrase=romajiPhrase;
//		this.timestamp=timeStamp;
		
	}

	public String getEnglish() {
		return this.english;
	}

	public String getRomaji() {
		return this.romaji;
	}

	public String getHirigana() {
		return this.hirigana;
	}

	public String getKanji() {
		return this.kanji;
	}

	public String getJapPhrase() {
		return this.japPhrase;
	}

	public String getEnglPhrase() {
		return this.englPhrase;
	}

	public String getUrl() {
		return this.imageUrl;
	}

	public String getRomajiPhrase() {
		return this.romPhrase;
	}
}
