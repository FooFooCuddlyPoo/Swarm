import java.awt.Graphics;

public class Character implements Drawable{
	private ImageWrapper image;
	private int height;
	private int width;
	private int x;
	private int y;
	
	private int moveDistance = 8;
	
	private boolean isJumping = false;
	private boolean isWalking = false;
	private boolean direction = true;
	private int maxJump;
	private int startHeight;
	
	private Hitbox movementBox;
	private Hitbox shootBox;
	
	private SpriteSheet sprite;
	
	public Character(int x, int y, SpriteSheet sprites)
	{
		this.setX(x);
		this.setY(y);
		
		this.sprite = sprites;
		this.height = 96;
		this.width = 64;
		image = new ImageWrapper(-1, width, height, sprites);	//-1 cause of offset initialization
	}
	
	public void draw(Graphics g)
	{
		image.draw(g, x, y, width, height);
	}
	
	public void move(Tile[][] map)
	{
		if(isJumping){
			y -= moveDistance;
			
			if(y == maxJump)
			{
				isJumping = false;  
			}
		}else{
			boolean falling = true;
			for(int x=this.x; x<this.x+this.width; x++){}
		}
		
		
		if(isWalking){
			int movement = moveDistance;
			if(!direction){movement = -moveDistance;}
		
			x += movement;
		}
	}
	
	public void jump()
	{
		if(isJumping){return;}
		
		isJumping = true;
		maxJump = y - 4*32;  //4 blocks of 32
	}
	

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getX() {
		return x;
	}
	
	public void walk(boolean direction){
		this.direction = direction;
		this.isWalking = true;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public ImageWrapper getImage() {
		return image;
	}

	public void setImage(ImageWrapper image) {
		this.image = image;
	}

	public Hitbox getMovementBox() {
		return movementBox;
	}

	public void setMovementBox(Hitbox movementBox) {
		this.movementBox = movementBox;
	}

	public Hitbox getShootBox() {
		return shootBox;
	}

	public void setShootBox(Hitbox shootBox) {
		this.shootBox = shootBox;
	}
}
