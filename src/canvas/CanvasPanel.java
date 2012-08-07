package canvas;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;

import sprites.Enemy;
import swarm.ImageWrapper;
import items.Bullet;
import items.Item;
import map.Map;
import map.MoveTile;
import map.Tile;


public class CanvasPanel extends JPanel implements KeyListener,
		MouseMotionListener, MouseListener{

	private static final long serialVersionUID = 8187920910989915064L;
	private Cursor cursor;
	private Map map;
	private Camera cam;
	private Robot mouseController;

	private boolean leftKeyPressed;
	private boolean rightKeyPressed;
	private boolean upKeyPressed;
	
	private ImageWrapper bg;
	private boolean weaponSwitchKeyPressed;
	private boolean weaponFired;

	private static final int blockSize = 32;
	
	private int frames = 0;
	

	public CanvasPanel(Map m) {
		setPreferredSize(new Dimension(1024, 768));
		setDoubleBuffered(true);
		
		try {
			mouseController = new Robot();
			
		} catch (AWTException e) {
			e.printStackTrace();
		}
		this.map = m;
		this.cam = new Camera(map.getCharacter());
		cursor = new Cursor(0, 0, blockSize, blockSize, cam);
		//mouseController.mouseMove(map.getCharacter().getX(), map.getCharacter().getY());
		
		bg = new ImageWrapper("images/backgroundCave.jpg");
		
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		
	}

	public void changeMap(Map m){
		this.map = m;
		this.cam = new Camera(map.getCharacter());
		cursor = new Cursor(0, 0, blockSize, blockSize, cam);
		mouseController.mouseMove(map.getCharacter().getX(), map.getCharacter().getY());
	}
	
	public void paint(Graphics g) {
		frames++;
		g.drawImage(bg.getImage(), 0, 0, null);
		if(map!=null){
				g.translate(-cam.getX(), -cam.getY());
			
		
			for (int i = 0; i < map.getMap().length; i++) {
				for (int j = 0; j < map.getMap()[0].length; j++) {
					Tile tempTile = map.getMap()[i][j];
				
					tempTile.setX(tempTile.getX());
					tempTile.setY(tempTile.getY());
				
					tempTile.draw(g);
				
				}
			}
		
			for(MoveTile m: map.movingTiles() )
				m.draw(g);

			for(Enemy e : map.enemies())
				e.draw(g);
		
			for(Item i: map.items())
				i.draw(g);
		
			for(Bullet b: map.getBullets())
				b.draw(g);
		
			map.getEntrance().draw(g);
			map.getExit().draw(g);
	
			map.getCharacter().draw(g);
			
			cursor.draw(g);
			g.translate(cam.getX(), cam.getY());
		
			map.getHud().draw(g);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT
				|| e.getKeyCode() == KeyEvent.VK_A) {
			leftKeyPressed = true;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT
				|| e.getKeyCode() == KeyEvent.VK_D) {
			rightKeyPressed = true;
		} else if (e.getKeyCode() == KeyEvent.VK_UP
				|| e.getKeyCode() == KeyEvent.VK_W) {
			upKeyPressed = true;
		}else if (e.getKeyCode() == KeyEvent.VK_Q) {
			
			 weaponSwitchKeyPressed = true;
			
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if    (e.getKeyCode() == KeyEvent.VK_LEFT
			|| e.getKeyCode() == KeyEvent.VK_A) {
			leftKeyPressed = false;
		} else if (e.getKeyCode() == KeyEvent.VK_RIGHT
				|| e.getKeyCode() == KeyEvent.VK_D) {
			rightKeyPressed = false;

		} else if (e.getKeyCode() == KeyEvent.VK_UP
				|| e.getKeyCode() == KeyEvent.VK_W) {
			upKeyPressed = false;
		}else if(e.getKeyCode()== KeyEvent.VK_ESCAPE){
			System.exit(0);
			
		}else if (e.getKeyCode() == KeyEvent.VK_Q) {
			
			 weaponSwitchKeyPressed = false;
			
		}
	}

	public void setImage(ImageWrapper i){
		map = null;
		bg=i;
	}
	
	public Camera getCamera(){
		return this.cam;
	}
	
	public void movecharacter() {
		if(map==null)
			return;
		
		if (leftKeyPressed) {
			map.getCharacter().walk(false);
		
		}
		if (rightKeyPressed) {
			map.getCharacter().walk(true);
		
		}
		if (upKeyPressed) {
			map.getCharacter().jump();
		}
		if(weaponSwitchKeyPressed){
			
			map.getCharacter().cycleItem();
			weaponSwitchKeyPressed = false;
		}
		if(weaponFired){

			if(map.getCharacter().getCurrentWeapon() != null){
				
				Bullet b;
				if(cursor.getGameworldX() > map.getCharacter().getX())
					b = map.getCharacter().getCurrentWeapon().getBullet(map.getCharacter().getX() + 38,
							map.getCharacter().getY() + 40, cursor.getGameworldX(), cursor.getGameworldY(), frames);
				else
					b = map.getCharacter().getCurrentWeapon().getBullet(map.getCharacter().getX(), 
							map.getCharacter().getY() + 40, cursor.getGameworldX(), cursor.getGameworldY(), frames);
				
				if(b != null)
					map.addBullet(b);
			}
		}

		cam.setX(cursor);
		cam.setY(cursor);
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseMoved(e);
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		cursor.moveCursor(e.getX(), e.getY());
		
		requestFocusInWindow();
		
		}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		weaponFired = true;
		/*Bullet b = new Bullet(map.getCharacter().getX(), map.getCharacter().getY(), 32, 32,
				 "images/projectileBullet.png", 0, cursor.getGameworldX(), cursor.getGameworldY());
		map.addBullet(b);*/
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		weaponFired = false;
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		System.out.println("Mouse exited");
	}
}
