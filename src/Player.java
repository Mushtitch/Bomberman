import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Player {
	
	private double x;
	private double y;
	private double dx;
	private double dy;
	
	private int width;
	private int height;
	
	private Bomb[] bomb;
	private int nbBombes;
	private int MAX_BOMBS;
	private int bombDropped;
	private boolean dropBomb;
	
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
	
	private Animation animation;
	private BufferedImage spritesheet;
	private BufferedImage[] idleSprites;
	private BufferedImage[] upSprites;
	private BufferedImage[] downSprites;
	private BufferedImage[] leftSprites;
	private BufferedImage[] rightSprites;
	
	private String nameCosmeticHair;
	private String nameCosmeticFace;
	private String nameCosmeticChest;
	private String nameCosmeticBoots;
	private Cosmetic[] favoriteCosmetics;
	
	public Player(Map map, String nameCosmeticHair, String nameCosmeticFace, String nameCosmeticChest, String nameCosmeticBoots) {
		this.map = map;
		this.nameCosmeticHair = nameCosmeticHair;
		this.nameCosmeticFace = nameCosmeticFace;
		this.nameCosmeticChest = nameCosmeticChest;
		this.nameCosmeticBoots = nameCosmeticBoots;
		
		width = 64;
		height = 64;
		
		nbBombes = 1;
		MAX_BOMBS = 4;
		bombDropped = 0;
		bomb = new Bomb[MAX_BOMBS];
		loadBombs();
		
		moveSpeed = 0.2;
		MAX_SPEED = 0.6;
		stopSpeed = 0.3;
		
		try {
			idleSprites = new BufferedImage[1];
			upSprites = new BufferedImage[4];
			downSprites = new BufferedImage[4];
			leftSprites = new BufferedImage[4];
			rightSprites = new BufferedImage[4];
			
			spritesheet = ImageIO.read(new File("ressources/bob.png"));
			idleSprites[0] = spritesheet.getSubimage(0, 0, width, height);
			for(int i = 0; i < upSprites.length; i++) {
				upSprites[i] = spritesheet.getSubimage(i * width, 3 * height, width, height);
			}
			for(int i = 0; i < downSprites.length; i++) {
				downSprites[i] = spritesheet.getSubimage(i * width, 0 * height, width, height);
			}
			for(int i = 0; i < leftSprites.length; i++) {
				leftSprites[i] = spritesheet.getSubimage(i * width, 1 * height, width, height);
			}
			for(int i = 0; i < rightSprites.length; i++) {
				rightSprites[i] = spritesheet.getSubimage(i * width, 2 * height, width, height);
			}
		}
		catch(Exception e) {
			System.out.println("Player creation failed.");
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
	public void setDropBomb(int i, boolean b) { dropBomb = b; bomb[i].setDropped(b);}
	public void setBombDropped(int i) { this.bombDropped = i; }
	
	public double getX() { return this.x; }
	public double getY() { return this.y; }
	public int getSize() { return width; }
	public Bomb[] getBomb() { return this.bomb; }
	public int getNbBombes() { return this.nbBombes; }
	public int getBombDropped() { return this.bombDropped; }
	
	private void calculateCorners(double x, double y) {
		int leftTile = map.getColTile((int) (x - width / 2) + 1);
		int rightTile = map.getColTile((int) (x + width / 2) - 1);
		int topTile = map.getLigneTile((int) (y - height / 2) + 1);
		int bottomTile = map.getLigneTile((int) (y + height / 2) - 1);
		//System.out.println(map.getMap()[map.getLigneTile((int) (y - 3 * (height / 2)))][map.getColTile((int) (x - width / 2))]);
		//System.out.println(x + " " + y);
		topLeft = map.getTile(topTile, leftTile) == 1 || map.getTile(topTile, leftTile) == 2 || map.getTile(topTile, leftTile) == 4 || map.getTile(topTile, leftTile) == 7; // 1 = mur | 2 = brique | 4 = brique + bonus bombe supp.
		topRight = map.getTile(topTile, rightTile) == 1 || map.getTile(topTile, rightTile) == 2 || map.getTile(topTile, rightTile) == 4 || map.getTile(topTile, rightTile) == 7;
		bottomLeft = map.getTile(bottomTile, leftTile) == 1 || map.getTile(bottomTile, leftTile) == 2 || map.getTile(bottomTile, leftTile) == 4 || map.getTile(bottomTile, leftTile) == 7;
		bottomRight = map.getTile(bottomTile, rightTile) == 1 || map.getTile(bottomTile, rightTile) == 2 || map.getTile(bottomTile, rightTile) == 4 || map.getTile(bottomTile, rightTile) == 7;
	}
	
	public void update() {
		
		// D�termine la prochaine position
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
			if(dx > 0) { // Si � gauche
				dx -= stopSpeed;
				if(dx < 0) {
					dx = 0;
				}
			}
			else if(dx < 0) { // Si � droite
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
		
		// V�rifie les collisions
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
			}
			else {
				tmpY += dy;
			}
		}
		if(dy > 0) {
			if(bottomLeft || bottomRight) {
				dy = 0;
				tmpY = (playerLigne + 1) * map.getTileSize() - height / 2;
			}
			else {
				tmpY += dy;
			}
		}
		
		calculateCorners(versX, y);
		if(dx < 0) {
			if(topLeft || bottomLeft) {
				dx = 0;
				tmpX = playerCol * map.getTileSize() + width / 2;
			}
			else {
				tmpX += dx;
			}
		}
		if(dx > 0) {
			if(topRight || bottomRight) {
				dx = 0;
				tmpX = (playerCol + 1) * map.getTileSize() - width / 2;
			}
			else {
				tmpX += dx;
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
			idleSprites[0] = spritesheet.getSubimage(0, 0, width, height);
			animation.setDelay(150);
		}
		else if(left) {
			animation.setSprite(leftSprites);
			idleSprites[0] = spritesheet.getSubimage(0, 64, width, height);
			animation.setDelay(150);
		}
		else if(right) {
			animation.setSprite(rightSprites);
			idleSprites[0] = spritesheet.getSubimage(0, 128, width, height);
			animation.setDelay(150);
		}
		else {
			animation.setSprite(idleSprites);
			animation.setDelay(-1);
		}
		animation.update();
		for(int i = 0; i < nbBombes; i++) {
			bomb[i].update();
		}
		checkForBonus();
	}
	
	public void draw(Graphics g) {
		// g.drawImage(sprites[0][0].getImage(), (int) (mapX + x - width / 2), (int) (mapY + y - height / 2), null);
		// g.fillRect((int) (mapX + x - width / 2), (int) (mapY + y - height / 2), 10, 10); // id�e : cosmetics ? 
		for(int i = 0; i < nbBombes; i++) {
			bomb[i].draw(g);
		}
		g.drawImage(animation.getSprite(), (int) (x - width / 2), (int) (y - height / 2), null);
	}
	
	public void loadBombs() {
		for(int i = 0; i < MAX_BOMBS; i++) {
			bomb[i] = new Bomb(this.map);
		}
	}
	
	public void checkForBonus() {
		int casePlayer = map.getMap()[map.getLigneTile((int) y)][map.getColTile((int) x)];
		
		/*
		if(casePlayer == 5) {
			if(nbBombes < MAX_BOMBS) {
				nbBombes++;
				map.getMap()[map.getLigneTile((int) y)][map.getColTile((int) x)] = 0;
			}
			else
				map.getMap()[map.getLigneTile((int) y)][map.getColTile((int) x)] = 0;
		}
		*/
		if(casePlayer == 8) {
			if(nbBombes < MAX_BOMBS) {
				if(this.MAX_SPEED <= 1.0)
					this.MAX_SPEED += 0.2;
				map.getMap()[map.getLigneTile((int) y)][map.getColTile((int) x)] = 0;
			}
			else
				map.getMap()[map.getLigneTile((int) y)][map.getColTile((int) x)] = 0;
		}
	}
	
}