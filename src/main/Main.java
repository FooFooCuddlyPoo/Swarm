package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Timer;

import swarm.ImageWrapper;
import sprites.Character;

import canvas.Canvas;

import map.Map;


public class Main implements ActionListener{
	public static final int FPS = 70;
	private Canvas canvas;
	private Map map;

	private int tick;
	private Timer t;
	
	private ArrayList<String> mapsArray = new ArrayList<String>();
	ImageWrapper winImage;
	private int mapCount = 0;
	
	private boolean hasWon = false;
	
	public Main()
	{
		winImage = new ImageWrapper("images/splashVictory.png");
		readMaps();
		changeMap();
		this.tick = 1000/FPS;
		t = new Timer(this.tick, this); 

	}

	public static void main(String[] args){
		new Main().start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(map.getCharacter().getDead()){
			changeMap();
		}
		if(map.isComplete()){
			this.mapCount++;
			Character p = map.getCharacter();
			if(mapCount<this.mapsArray.size())
				changeMap();
			else
				win();
			map.getCharacter().setItemList(p.getItemList());
			map.getCharacter().setGold(p.getGold());
		}
		if(!hasWon)
			map.update();
		canvas.repaint();
	}
	
	public void start(){
		t.start();
	}
	
	private void readMaps(){
		File file = new File("allMaps.txt");
		Scanner scan;
		try{
			scan = new Scanner(file);
			
			while(scan.hasNext()){
				mapsArray.add(scan.next());
			}
		}catch (IOException e){System.out.println("your error: " + e);}
	}
	
	private void changeMap(){
		map = new Map("levels/" + mapsArray.get(mapCount));
		if(canvas == null)
			canvas = new Canvas(map);
		else
			canvas.setMap(map);
	}
	
	private void win(){
		canvas.setImage(winImage);
		this.hasWon = true;
	}

}
