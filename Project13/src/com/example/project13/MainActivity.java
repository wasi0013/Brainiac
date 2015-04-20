package com.example.project13;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity {
	Button startButton, changeProfileButton,highScoreButton,helpButton;
	SharedPreferences pref;
	SharedPreferences.Editor prefEditor;
	DataHandler gameDb;
	TextView userNameTextView;
	MediaPlayer mediaPlayer;
	
	String user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
 
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);
		setTitle("Brainiac");
		mediaPlayer = MediaPlayer.create(this, R.raw.relax);
		mediaPlayer.setLooping(true);
		mediaPlayer.start();
		gameDb = new DataHandler(this);
		userNameTextView = (TextView) findViewById(R.id.textView1);
		pref = getSharedPreferences("game", MODE_PRIVATE);
		prefEditor=pref.edit();
		user = pref.getString("user", "-1");
		if(user.equals("-1")){
		 AlertDialog.Builder alert = new AlertDialog.Builder(this);

		    final EditText userName= new EditText(MainActivity.this);
		    alert.setTitle("Enter a User Name:");
		    alert.setView(userName);
		    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	if(userName.getText().toString().length()==0){
			    	 Toast.makeText(getApplicationContext(), "Invalid Username!", Toast.LENGTH_SHORT).show();
			    	 
		    	}
		    	else{
		    		String user = userName.getText().toString();
		    		user.toLowerCase();
		    		//Toast.makeText(getApplicationContext(), "Successfully created: "+user, Toast.LENGTH_SHORT).show();
		    		gameDb.addPlayer(user, 0);
		    		prefEditor.putString("user",user);
					prefEditor.commit();
					userNameTextView.setText(user+" ("+0+")");
		    	}
		      }
		    });

		    
		    alert.setCancelable(false);
		    alert.show();
		}
		else{
			Cursor cursor = gameDb.getPlayer(user);
			cursor.moveToFirst();
			String score = cursor.getString(cursor.getColumnIndex(gameDb.PLAYER_SCORE));
			userNameTextView.setText(user+" ("+score+")");
		}
		
		startButton = (Button) findViewById(R.id.button1);
		changeProfileButton = (Button) findViewById(R.id.button2);
		highScoreButton = (Button) findViewById(R.id.button3);
		helpButton = (Button) findViewById(R.id.button4);
		highScoreButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,HighScoreActivity.class);
				startActivity(intent);
			}
		});
		
		
		
		startButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mediaPlayer.pause();
				Intent intent = new Intent(MainActivity.this,GameActivity.class);
				intent.putExtra("user",user);
				intent.putExtra("level",1);
				intent.putExtra("score",0);
				startActivity(intent);
				
			}
		});
		helpButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,HelpActivity.class);
				startActivity(intent);
				
			}
		});
		
		changeProfileButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				

				 AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
		
				    final EditText userName= new EditText(MainActivity.this);
				    alert.setTitle("Select a Different User:");
		
				    alert.setView(userName);
		
				    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int whichButton) {
				    	if(userName.getText().toString().length()==0){
					    	 Toast.makeText(getApplicationContext(), "Invalid Username!", Toast.LENGTH_SHORT).show();
					    	 
				    	}
				    	else{
				    		String user = userName.getText().toString();
				    		user.toLowerCase();
				    		boolean isExist=false;
				    		Cursor cursor =gameDb.getAllPlayer();
			    			cursor.moveToFirst();
			    			do
			    			{
			    				String name=cursor.getString(cursor.getColumnIndex(DataHandler.PLAYER_NAME));
			    				if(user.equals(name)){
			    					isExist=true;
			    					break;
			    				}
			    			
			    			}
			    			while(cursor.moveToNext());
			    		
			    			
				    		//Toast.makeText(getApplicationContext(), ""+user, Toast.LENGTH_SHORT).show();
				    		try {
				    			
				    			

					    	    if(!isExist)gameDb.addPlayer(user, 0);
					    		
							} catch (Exception e) {
								//user already exists
								Toast.makeText(getApplicationContext(), user+" errors"+e, Toast.LENGTH_SHORT).show();
					    			
								
							}
				    		
				    		prefEditor.putString("user", user);
				    		prefEditor.commit();
				    		mediaPlayer.pause();
				    		finish();
				    		Intent intent = new Intent(MainActivity.this,MainActivity.class);
				    		startActivity(intent);
				    	}
				      }
				    });
		
				    
				    alert.setCancelable(false);
				    alert.show();
			
			
				
				
			}
		});
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mediaPlayer.pause();
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mediaPlayer.pause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mediaPlayer.start();
	}

}
