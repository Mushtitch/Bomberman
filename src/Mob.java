import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Mob {
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private int width;
	private int height;
	 
	private boolean left;
	private boolean right;
	private boolean up;
	private boolean down;
	
	private double moveSpeed;
	private double MAX_SPEED;
	private double stopSpeed;
	
	private Map map;
	
	private boolean topLeft;
	private boolean topRight;
	private boolean bottomLeft;
	private boolean bottomRight;
	
	private boolean blockedUp;
	private boolean blockedDown;
	private boolean blockedRight;
	private boolean blockedLeft;
	
	private boolean firstMove;
	
	private Animation animation;
	private BufferedImage spritesheet;
	private BufferedImage[] idleSprites;
	private BufferedImage[] upSprites;
	private BufferedImage[] downSprites;
	private BufferedImage[] leftSprites;
	private BufferedImage[] rightSprites;
	
	public Mob(Map map) {
		this.map = map;
		
		width = 64;
		height = 64;
		
		moveSpeed = 0.2;
		MAX_SPEED = 0.6;
		stopSpeed = 0.3;
		
		firstMove = true;
		
		try {
			idleSprites = new BufferedImage[1];
			upSprites = new BufferedImage[4];
			downSprites = new BufferedImage[4];
			leftSprites = new BufferedImage[4];
			rightSprites = new BufferedImage[4];
			
			spritesheet = ImageIO.read(new File("ressources/mob.png"));
			idleSprites[0] = spritesheet.getSubimage(0, 0, width, height);
			for(int i = 0; i < upSprites.length; i++) {
				upSprites[i] = spritesheet.getSubimage(i * width, 3 * height, width, height);
			}
			for(int i = 0; i < downSprites.length; i++) {
				downSprites[i] = spritesheet.getSubimage(i * width, 2 * height, width, height);
			}
			for(int i = 0; i < leftSprites.length; i++) {
				leftSprites[i] = spritesheet.getSubimage(i * width, 0 * height, width, height);
			}
			for(int i = 0; i < rightSprites.length; i++) {
				rightSprites[i] = spritesheet.getSubimage(i * width, 1 * height, width, height);
			}
						
		}
		catch(Exception e) {
			System.out.println("Mob creation failed.");
			e.printStackTrace();
		}
		
		animation = new Animation();
	}
	
	public void setX(int x) { this.x = x; }
	public void setY(int y) { this.y = y; }
	public void setLeft(boolean b) { left = b; }
	public void setRight(boolean b) { right = b; }
	public void setUp(boolean b) { up = b; }
	public void setDown(boolean b) { down = b; }
	
	public double getX() { return this.x; }
	public double getY() { return this.y; }
	public int getSize() { return width; }
	
	private void calculateCorners(double x, double y) {
		int leftTile = map.getColTile((int) (x - width / 2) + 1);
		int rightTile = map.getColTile((int) (x + width / 2) - 1);
		int topTile = map.getLigneTile((int) (y - height / 2) + 1);
		int bottomTile = map.getLigneTile((int) (y + height / 2) - 1);
		topLeft = map.getTile(topTile, leftTile) == 1 || map.getTile(topTile, leftTile) == 2 || map.getTile(topTile, leftTile) == 4; // 1 = mur | 2 = brique | 3 = brique + bonus bombe supp.
		topRight = map.getTile(topTile, rightTile) == 1 || map.getTile(topTile, rightTile) == 2 || map.getTile(topTile, rightTile) == 4;
		bottomLeft = map.getTile(bottomTile, leftTile) == 1 || map.getTile(bottomTile, leftTile) == 2 || map.getTile(bottomTile, leftTile) == 4;
		bottomRight = map.getTile(bottomTile, rightTile) == 1 || map.getTile(bottomTile, rightTile) == 2 || map.getTile(bottomTile, rightTile) == 4;
	}
	
	public void update() {
		
		// Détermine la prochaine position
		if(left) {
			dx -= moveSpeed;
			if(dx < -MAX_SPEED) {
				dx = -MAX_SPEED;
			}
		}
		else if(right) {
			dx += moveSpeed;
			if(dx > MAX_SPEED) {
				dx = MAX_SPEED;
			}
		}
		else {
			if(dx > 0) { // Si à gauche
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) { // Si à droite
				dx += stopSpeed;
				if(dx > 0) {
					dx = 0;
				}
			}
		}
		if(up) {
			dy -= moveSpeed;
			if(dy < -MAX_SPEED) {
				dy = -MAX_SPEED;
			}
		}
		else if(down) {
			dy += moveSpeed;
			if(dy > MAX_SPEED) {
				dy = MAX_SPEED;
			}
		}
		else {
			dy = 0;
		}
		
		// Vérifie les collisions
		int playerCol = map.getColTile((int) x);
		int playerLigne = map.getLigneTile((int) y);
		
		// Prochaine position
		double versX = x + dx;
		double versY = y + dy;
		
		double tmpX = x;
		double tmpY = y;
		
		calculateCorners(x, versY);
		if(dy < 0) {
			if(topLeft || topRight) {
				dy = 0;
				tmpY = playerLigne * map.getTileSize() + height / 2;
				blockedUp = true;
			}
			else {
				tmpY += dy;
				blockedUp = false;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				tmpY = (playerLigne + 1) * map.getTileSize() - height / 2;
				blockedDown = true;
			}
			else {
				tmpY += dy;
				blockedDown = false;
			}
		}
		
		calculateCorners(versX, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				tmpX = playerCol * map.getTileSize() + width / 2;
				blockedLeft = true;
			}
			else {
				tmpX += dx;
				blockedLeft = false;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				tmpX = (playerCol + 1) * map.getTileSize() - width / 2;
				blockedRight = true;
			}
			else {
				tmpX += dx;
				blockedRight = false;
			}
		}
		
		x = tmpX;
		y = tmpY;
		
		// Animation des sprites
		if(up) {
			animation.setSprite(upSprites);
			idleSprites[0] = spritesheet.getSubimage(0, 192, width, height);
			animation.setDelay(150);
		}
		else if(down) {
			animation.setSprite(downSprites);
			idleSprites[0] = spritesheet.getSubimage(0, 128, width, height);
			animation.setDelay(150);
		}
		else if(left) {
			animation.setSprite(leftSprites);
			idleSprites[0] = spritesheet.getSubimage(0, 0, width, height);
			animation.setDelay(150);
		}
		else if(right) {
			animation.setSprite(rightSprites);
			idleSprites[0] = spritesheet.getSubimage(0, 64, width, height);
			animation.setDelay(150);
		}
		else {
			animation.setSprite(idleSprites);
			animation.setDelay(-1);
		}
		animation.update();
		move();
		
	}
	
	public void move() {
		if(firstMove) {
			this.setLeft(true);
			firstMove = false;
		}
				
		if(blockedLeft) {
			this.setLeft(false);
			blockedLeft = false;
			this.setUp(true);
		}
		if(blockedUp) {
			this.setUp(false);
			blockedUp = false;
			this.setRight(true);
		}
		if(blockedRight) {
			this.setRight(false);
			blockedRight = false;
			this.setDown(true);
		}
		if(blockedDown) {
			this.setDown(false);
			blockedDown = false;
			this.setLeft(true);
		}
	}
	
	public void changeDirection() {
	}
	
	public void draw(Graphics g) {
		g.drawImage(animation.getSprite(), (int) (x - width / 2), (int) (y - height / 2), null);
	}
	
}