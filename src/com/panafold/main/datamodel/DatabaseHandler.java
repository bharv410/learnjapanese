package com.panafold.main.datamodel;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;
 
    // Database Name
    private static final String DATABASE_NAME = "wordsManager";
 
    // Contacts table name
    private static final String TABLE_WORDS = "words";
 
    // Contacts Table Columns names for english,romaji,hirigana,kanji,phrase
    private static final String KEY_ID = "id";
    private static final String KEY_ENGLISH = "english";
    private static final String KEY_ROMAJI = "romaji";
    private static final String KEY_HIRIGANA = "hirigana";
    private static final String KEY_KANJI = "kanji";
    private static final String KEY_ENGPHRASE = "engphrase";
    private static final String KEY_JAPPHRASE = "japphrase";
    private static final String KEY_URL = "imageurl";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_WORDS_TABLE = "CREATE TABLE " + TABLE_WORDS + "("
                + KEY_ENGLISH + " TEXT PRIMARY KEY," + KEY_ROMAJI + " TEXT,"
                + KEY_HIRIGANA + " TEXT,"+ KEY_KANJI + " TEXT,"
                + KEY_ENGPHRASE + " TEXT,"+ KEY_JAPPHRASE + " TEXT,"
                + KEY_URL + " TEXT" + ")";
        db.execSQL(CREATE_WORDS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORDS);
 
        // Create tables again
        onCreate(db);
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    // Adding new contact
    public void addWord(Word curWord) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_ENGLISH, curWord.getEnglish()); 
        values.put(KEY_ROMAJI, curWord.getRomaji());
        values.put(KEY_HIRIGANA, curWord.getHirigana());
        values.put(KEY_KANJI, curWord.getKanji());
        values.put(KEY_JAPPHRASE, curWord.getJapPhrase());
        values.put(KEY_ENGPHRASE, curWord.getEnglPhrase());
        values.put(KEY_URL, curWord.getUrl());
        
        // Inserting Row
        db.insert(TABLE_WORDS, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single word
    Word getWord(String eng) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_WORDS, new String[] { KEY_ENGLISH,
        		KEY_ROMAJI, KEY_HIRIGANA,KEY_KANJI, KEY_JAPPHRASE,KEY_ENGPHRASE,KEY_URL}, KEY_ENGLISH + "=?",
                new String[] { String.valueOf(eng) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Word curWord = new Word(cursor.getInt(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getString(6),cursor.getString(7));
        // return contact
        return curWord;
    }
     
    // Getting All Contacts
    public List<Word> getAllWords() {
        List<Word> wordList = new ArrayList<Word>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WORDS;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
            	Word curWord = new Word(cursor.getInt(0),
                        cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5),cursor.getString(6),cursor.getString(7));
                // Adding contact to list
            	wordList.add(curWord);
            } while (cursor.moveToNext());
        }
 
        // return contact list
        return wordList;
    }

 
    // Getting contacts Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_WORDS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }
 
}