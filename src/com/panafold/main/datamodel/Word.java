package com.panafold.main.datamodel;

public class Word {
	String english, romaji, hirigana, kanji, japPhrase, englPhrase, imageUrl;
	int _id;

	public Word(int id, String eng, String rom, String hir, String kan,
			String phras, String englPh, String url) {
		this._id = id;
		this.english = eng;
		this.romaji = rom;
		this.hirigana = hir;
		this.kanji = kan;
//		this.unlocked=unlock;
//		this.siteurl=site;
		
		this.englPhrase = englPh;
		this.japPhrase = phras;
		this.imageUrl = url;
//		this.romPhrase=romanjiPhrase;
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

	public int getId() {
		return this._id;
	}
}
