package com.example.project13;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity implements OnClickListener{
	
	Button button1,button2,button3,button4;
	TextView textView1,textView2;
	MediaPlayer mediaPlayer;
	DataHandler gameDB;
	
	String keyPress = "";
	String key = "";
	String user;
	String buttonText = "1234";
	
	int highScore = 0;
	static int score = 0;
	static int level = 1;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game);
	
		gameDB = new DataHandler(this);
		mediaPlayer = MediaPlayer.create(this, R.raw.twing);
		mediaPlayer.setLooping(false);
		
		user=getIntent().getExtras().getString("user");
		level=getIntent().getExtras().getInt("level");
		score=getIntent().getExtras().getInt("score");
		
		button1 = (Button) findViewById(R.id.button1);
		button2 = (Button) findViewById(R.id.button2);
		button3 = (Button) findViewById(R.id.button3);
		button4 = (Button) findViewById(R.id.button4);
		
		textView1 = (TextView) findViewById(R.id.textView1);
		textView2 = (TextView) findViewById(R.id.textView2);
		
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		
		button1.setClickable(false);
		button2.setClickable(false);
		button3.setClickable(false);
		button4.setClickable(false);
		
		alert("Level "+level,score==0?"Memorize the Pattern\nPress those when the pattern disappears":"Memorize the next pattern!");
		
		setButtonText(buttonText);
		
		key=getPattern(level);
		keyPress="";
		textView2.setText("score: " + score);
	    
		Cursor cursor =gameDB.getAllPlayer();
 		cursor.moveToFirst();
 		do{
 			String name=cursor.getString(cursor.getColumnIndex(DataHandler.PLAYER_NAME));
 			if(user.equals(name)){
 				highScore =cursor.getInt(cursor.getColumnIndex(DataHandler.PLAYER_SCORE));
     			break;
 			}	
 		}while(cursor.moveToNext());
		
	}

	public void setButtonText(String text){
		button1.setText("" + text.charAt(0));
		button2.setText("" + text.charAt(1));
		button3.setText("" + text.charAt(2));
		button4.setText("" + text.charAt(3));
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.button1){
			keyPress += button1.getText();
			textView1.setText(keyPress);
			
		}
		else if (v.getId() == R.id.button2) {
			keyPress += button2.getText();
			textView1.setText(keyPress);
		}
		else if (v.getId() == R.id.button3) {
			keyPress += button3.getText();
			textView1.setText(keyPress);
		}
		else if(v.getId() == R.id.button4){
			keyPress += button4.getText();
			textView1.setText(keyPress);
		}
		
		if(keyPress.length() == key.length()){
			
		 if(key.equals(keyPress)){
			 //pattern matched
			 
			 level+=1;
			 score+=1+level*2;
			 
			 try {
			    highScore=score>highScore?score:highScore;
			    gameDB.updatePlayer(user, highScore);
			 	}catch (Exception e) {
					Toast.makeText(GameActivity.this, "Error: Score Update failed", Toast.LENGTH_SHORT).show();
			 		}
			 textView2.setText("score: " + score);
			 
			 //twing sounds
			 mediaPlayer.start();
			 
			 finish();
			 
			 Intent intent = new Intent(GameActivity.this,GameActivity.class);
			 intent.putExtra("user",user);
			 intent.putExtra("level", level);
			 intent.putExtra("score", score);
			 startActivity(intent);
			 
		 }
		 else{
			 AlertDialog.Builder alert = new AlertDialog.Builder(GameActivity.this);
			 	
			 	button1.setClickable(false);
			    button2.setClickable(false);
			    button3.setClickable(false);
			    button4.setClickable(false);
			    
			    textView1.setText("Game Over!");
			    alert.setTitle("Game Over!");
			    
			    mediaPlayer = MediaPlayer.create(this, R.raw.illegal);
				mediaPlayer.setLooping(false);
				mediaPlayer.start();
			    
			    alert.setMessage("You Scored: "+score);
			    try {
			    	highScore=score>highScore?score:highScore;
			    	gameDB.updatePlayer(user, highScore);
			    	
				} catch (Exception e) {
					Toast.makeText(GameActivity.this, "Error: Score Update failed", Toast.LENGTH_SHORT).show();
				}
			    alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
			    public void onClick(DialogInterface dialog, int whichButton) {
			    	finish();
			    	Intent intent = new Intent(GameActivity.this,MainActivity.class);
			    	startActivity(intent);
			    }});
			    alert.setCancelable(false);
			    alert.show();		 
		 }
		}
		
		
	};
	
	//hide text box with animation
	void hideView(View view,int durationMS){

	    Handler handler = new Handler();
	    final View v = view;
	    handler.postDelayed(new Runnable() {

	    @Override
	    public void run() {

	    TranslateAnimation animate = new TranslateAnimation(0,-v.getWidth(),0,0);
	    animate.setDuration(500);
	    animate.setFillAfter(true);
	    v.startAnimation(animate);
	    v.setVisibility(View.INVISIBLE);
	    button1.setClickable(true);
	    button2.setClickable(true);
	    button3.setClickable(true);
	    button4.setClickable(true);

	  }}, durationMS);
		
		
	}
	
	void alert(String alertTitle, String alertMsg){
		
		 AlertDialog.Builder alert = new AlertDialog.Builder(GameActivity.this);
			
		    alert.setTitle(alertTitle);
		    alert.setMessage(alertMsg);
		    
		    alert.setPositiveButton("Ready!", new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int whichButton) {
		    	textView1.setText(key);
		    	hideView(textView1, 3000);
				
		    	
		    }});

		    
		    alert.setCancelable(false);
		    alert.show();
	
	}
	
	
	
	public String getPattern(int length){
		//generates random patterns using the button text
		String pattern = "";
		for(int i=0; i<length; i++){
			int key = (int)(Math.random()*10)%4;
			switch (key) {
			case 0:
				pattern += button1.getText();
				break;
			case 1:
				pattern += button2.getText();
				break;
			case 2:
				pattern += button3.getText();
				break;
			case 3:
				pattern += button4.getText();
				break;
			}
		}
		return pattern;
	}

}
