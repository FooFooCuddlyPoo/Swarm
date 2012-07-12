package items;

import sprites.Sprite;
import map.Map;

public class Item extends Sprite{
	
	public Item(int x,int y, int width, int height,String Spritesheet, int spritenumber){
		super(x, y, width, height, Spritesheet, spritenumber);
}

	public Item(int x, int y, int width, int height, String runCycle, int frames,int health) {
		super(x, y, width, height, runCycle, frames, health);
		this.setMoveDistance(0);
			}
	
	public void move(Map m){
		walk(true);
		super.move(m);
	}
	
	
}
