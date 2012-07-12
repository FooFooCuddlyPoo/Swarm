package map;

import java.io.File;

import items.Coin;
import items.WeaponItem;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import sprites.*;
import swarm.ImageWrapper;
import sprites.Character;

import main.Main;

import items.Item;

public class Map {
	private int enemyTicks;
	private int eCountTick = 0;
	
	private Tile[][] map;
	private File inputfile;
	private Scanner scan;
	private int xDimension;
	private int yDimension;
	private Character character;
	private SpriteSheet worldSprites;
	private Hud hud ;
	private SpriteSheet weaponSprites;
	
	private Sprite entrance;
	private Exit exit;
	
	private ArrayList<MoveTile> movingTiles = new ArrayList<MoveTile>();
	private ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	private ArrayList<Item> items = new ArrayList<Item>();
	
	private boolean complete = false;
	private boolean dead = false;
	
	private int[][] enemyFrames = {{BLOCK_SIZE*2, BLOCK_SIZE*2, 8}, {BLOCK_SIZE, BLOCK_SIZE, 5}};
	
	public static final int TILE_DEPTH = 5;	
	public static final int BLOCK_SIZE = 32;
	
	public Map(String filename) {
		worldSprites = new SpriteSheet("images/tilesCave.gif");
		weaponSprites = new SpriteSheet("images/hudWeapons.png");

		try {
			File file = new File(filename);
			scan = new Scanner(file);
			xDimension = scan.nextInt();
			yDimension = scan.nextInt();
			map = new Tile[xDimension][yDimension];
			enemies =  new ArrayList<Enemy>();
			movingTiles = new ArrayList<MoveTile>();
			items = new ArrayList<Item>();
			readmap();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		//readWeapons();
		enemyTicks = Main.FPS/6;

		hud = new Hud(character,0,0);
	}
		
	public Tile[][] getMap(){return map;}

	private void readmap() {
		while(scan.hasNext()){
			
			for(int j = 0; j< yDimension; j++){
				for(int i = 0; i< xDimension; i++){
					int item = scan.nextInt();
					if(item==17){	
						character = new Character(i*BLOCK_SIZE,j*BLOCK_SIZE, 64, 96, "images/playerWalk.png",60);

						map[i][j] = new BackgroundTile(BLOCK_SIZE,BLOCK_SIZE,i*BLOCK_SIZE,j*BLOCK_SIZE,TILE_DEPTH,new ImageWrapper(0, BLOCK_SIZE, BLOCK_SIZE,worldSprites));

						entrance = new SpriteImage(i*BLOCK_SIZE, j*BLOCK_SIZE, 64, 96, "images/doorIn.png", 0);

					}else if(item<0){
						map[i][j] = new DestTile(BLOCK_SIZE, BLOCK_SIZE, i*BLOCK_SIZE, j*BLOCK_SIZE, TILE_DEPTH, 
								new ImageWrapper(item*-1, BLOCK_SIZE, BLOCK_SIZE, worldSprites), 
								new ImageWrapper(0, BLOCK_SIZE, BLOCK_SIZE, worldSprites));
					}else if(item>299){
						map[i][j] = new BackgroundTile(BLOCK_SIZE, BLOCK_SIZE, i*BLOCK_SIZE, j*BLOCK_SIZE,
								TILE_DEPTH, new ImageWrapper(0, BLOCK_SIZE, BLOCK_SIZE, worldSprites));
						exit = new Exit(i*BLOCK_SIZE,j*BLOCK_SIZE, 64, 96, "images/doorOut.png",0);
					}else if(item>199){
						map[i][j] = new BackgroundTile(BLOCK_SIZE, BLOCK_SIZE, i*BLOCK_SIZE, j*BLOCK_SIZE,
								TILE_DEPTH, new ImageWrapper(0, BLOCK_SIZE, BLOCK_SIZE, worldSprites));
						WeaponItem.Type type = null;
						switch(item - 200){
						case 0:
							items.add(new Coin(i*BLOCK_SIZE, j*BLOCK_SIZE, 32, 32, "images/pickupCoin.png", 4));
							break;
						case 1:
							type = WeaponItem.Type.SWORD;
							break;
						case 2:
							type = WeaponItem.Type.MACE;
							break;
						case 3:
							type = WeaponItem.Type.PISTOL;
							break;
						case 4:
							type = WeaponItem.Type.CROSSBOW;
							break;
						default:
							throw new RuntimeException("wat?");
						}
						if (type != null) {
							items.add(new WeaponItem(i*BLOCK_SIZE, j*BLOCK_SIZE, 32, 32,"images/hudWeapons.png", type));
						}
					}else if(item>99){
						map[i][j] = new BackgroundTile(BLOCK_SIZE, BLOCK_SIZE, i*BLOCK_SIZE, j*BLOCK_SIZE,
								TILE_DEPTH, new ImageWrapper(0, BLOCK_SIZE, BLOCK_SIZE, worldSprites));
						
						Enemy.Type type = null;
						switch(item-100){
						case 0:
							type = Enemy.Type.WALKER;
							break;
						case 1:
							type = Enemy.Type.FLYER;
							break;
						default:
							throw new RuntimeException("wat?");
						}
						if(type!=null)
							if(type == Enemy.Type.FLYER)
								enemies.add(new FlyingEnemy(i*BLOCK_SIZE, j*BLOCK_SIZE, enemyFrames[item - 100][0],enemyFrames[item - 100][1], "images/enemyMove" + (item-100) + ".png", enemyFrames[item - 100][2],1));
							else
								enemies.add(new Enemy(i*BLOCK_SIZE, j*BLOCK_SIZE, enemyFrames[item - 100][0],enemyFrames[item - 100][1], "images/enemyMove" + (item-100) + ".png", enemyFrames[item - 100][2],1));
					}else if(item>17){
						map[i][j] = new BackgroundTile(BLOCK_SIZE, BLOCK_SIZE, i*BLOCK_SIZE, j*BLOCK_SIZE,
								TILE_DEPTH, new ImageWrapper(0, BLOCK_SIZE, BLOCK_SIZE, worldSprites));
						movingTiles.add(new MoveTile(BLOCK_SIZE, BLOCK_SIZE, i*BLOCK_SIZE, j*BLOCK_SIZE,
								TILE_DEPTH, new ImageWrapper(item-17, BLOCK_SIZE, BLOCK_SIZE, worldSprites),
								j*BLOCK_SIZE, (j-10)*BLOCK_SIZE, 4, i*BLOCK_SIZE, (i+10)*BLOCK_SIZE, 4));																//minus 17 as dont want access to moving background tiles
					}else{
						ImageWrapper imgwrap = new ImageWrapper(item, BLOCK_SIZE, BLOCK_SIZE, worldSprites);
						if(item==0)
							map[i][j] = new BackgroundTile(BLOCK_SIZE, BLOCK_SIZE, i*BLOCK_SIZE, j*BLOCK_SIZE, TILE_DEPTH, imgwrap);
						else
							map[i][j] = new FloorTile(BLOCK_SIZE,BLOCK_SIZE,i*BLOCK_SIZE,j*BLOCK_SIZE,TILE_DEPTH,imgwrap);
					}
				}
			}
		}
	}

	public Character getCharacter() {
		return character;
	}
	
	public ArrayList<MoveTile> movingTiles(){
		return this.movingTiles;
	}
	
	public ArrayList<Enemy> enemies(){
		return this.enemies;
	}
	

	public Hud getHud(){
		return hud;
	}
	public ArrayList<Item> items(){
		return this.items;

	}
	
	public void update(){
		
		
		//update moving tiles
		for(MoveTile t : this.movingTiles)
			t.move();
		
		if(eCountTick >= enemyTicks){
			for(Item e : items)
				e.move(this);
		}
		
		//update enemies
		if(eCountTick >= enemyTicks){
			eCountTick = 0;
			
			ArrayList<Enemy> temp = new ArrayList<Enemy>();
			
			for(Enemy e : enemies){
				if(e.getHealth() < 0)
					temp.add(e);
				else
					e.move(this);
			}
			
			enemies.removeAll(temp);
		}else
			eCountTick++;
		
		//update player
		this.character.move(this);
		//update hud
		this.hud.update();
		
	}

	public Sprite getEntrance() {
		return entrance;
	}

	public Exit getExit() {
		return exit;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
}
