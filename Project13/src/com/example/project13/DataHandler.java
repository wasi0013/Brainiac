package com.example.project13;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHandler extends SQLiteOpenHelper{
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "collection";
	public static final String TABLE_NAME = "game";
	public static final String PLAYER_ID = "id";
	public static final String PLAYER_NAME = "name";
	public static final String PLAYER_SCORE = "score";

	public DataHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("+ PLAYER_ID + 
				" INTEGER PRIMARY KEY AUTOINCREMENT," + PLAYER_NAME + " TEXT,"+ PLAYER_SCORE + " INTEGER" + ");";
        db.execSQL(CREATE_CONTACTS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);	 
        onCreate(db);
	}
	
	void addPlayer(String name, int score)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		 
        ContentValues values = new ContentValues();
        values.put(PLAYER_NAME, name);
        values.put(PLAYER_SCORE, score);
        db.insert(TABLE_NAME, null, values);
        db.close();
	}
	
	void deletePlayer(int id)
	{
		String[] selections={String.valueOf(id)};
		SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, PLAYER_ID + " = ?",selections);
        db.close();
	}
	
	int getPlayerCount()
	{
		String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count=cursor.getCount();
        db.close();
		return count;
	}
	
	
    Cursor getPlayer(String name)
    {
    	SQLiteDatabase db = this.getReadableDatabase();
    	String[] selections={String.valueOf(name)};
    	String columns[]={PLAYER_NAME,PLAYER_SCORE};
    	Cursor cursor=db.query(TABLE_NAME, columns, PLAYER_NAME + "=?", selections, null, null, null);
    	//db.close();
    	return cursor;
    }
    
    Cursor getAllPlayer()
    {
    	String selectQuery = "SELECT  * FROM " + TABLE_NAME+" ORDER BY "+PLAYER_SCORE+" DESC; ";	 
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //db.close();
    	return cursor;
    }
    
    public int updatePlayer(String name, int score)
    {
    	SQLiteDatabase db=this.getWritableDatabase();
    	ContentValues values = new ContentValues();
        values.put(PLAYER_NAME, name);
        values.put(PLAYER_SCORE, score);
        
        String[] selections={String.valueOf(name)};
        return db.update(TABLE_NAME, values, PLAYER_NAME + "=?", selections);
    }
	

}
