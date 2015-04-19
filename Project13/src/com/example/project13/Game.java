package com.example.project13;

public class Game {
	String pattern="";
	String chars="0123456789";
	int size=0;
	int level=0;
	public void createGame(int level){
		int index= (int)(Math.random()*10)%10;
		System.out.println(chars.charAt(index));
		
	}
	public void check(){
		
		
	}
	public void gameOver(){
		
	}
	

}
