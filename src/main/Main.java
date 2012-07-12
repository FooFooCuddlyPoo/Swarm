package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.Timer;

import canvas.Canvas;

import map.Map;


public class Main implements ActionListener{
	public static final int FPS = 60;
	private Canvas canvas;
	private Map map;

	private int tick;
	private Timer t;
	
	private ArrayList<String> mapsArray = new ArrayList<String>();
	private int mapCount = 0;
	
	public Main()
	{
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
		if(map.isComplete()){
			mapCount++;
			changeMap();
		}
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
		canvas = new Canvas(map);
	}
	
	

}
