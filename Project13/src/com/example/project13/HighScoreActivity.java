package com.example.project13;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class HighScoreActivity extends Activity {
	
	List <String> scores = new ArrayList<String>();
	DataHandler gameDB;
	ListView list;
	int counter=1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_high_score);
		list = (ListView) findViewById(R.id.listView1);
		gameDB = new DataHandler(this);
		Cursor cursor =gameDB.getAllPlayer();
		if(cursor.getCount()<1){
			Toast.makeText(getApplicationContext(), "No data",Toast.LENGTH_SHORT).show();
		}
		else{
		cursor.moveToFirst();
		counter=1;
		do
		{
			String name=cursor.getString(cursor.getColumnIndex(DataHandler.PLAYER_NAME));
			String score=cursor.getString(cursor.getColumnIndex(DataHandler.PLAYER_SCORE));
			scores.add(counter+". "+name+": "+score);
			counter+=1;
		}
		while(cursor.moveToNext());
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,scores);
		list.setAdapter(adapter);
		}
	}

}
