import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

public class Bomb {
	
	private double x;
	private double y;
	
	private static int width;
	private static int height;
	
	private boolean dropped;
	private int bombPower;
	private static long delayStart;
	private static long delayEnd;
	private static long period;
	
	private static Map map;
		
	public static Animation bombAnimation;
	public static BufferedImage spritesheet;
	public static BufferedImage[] bombSprites;
	
	public static Animation flameAnimation;
	public static BufferedImage flameSpritesheet;
	public static BufferedImage[] flameSprites;
	
	public Bomb(Map map) {
		this.map = map;
		
		width = 64;
		height = 64;
		
		bombPower = 2;
		delayStart = 3000;
	    delayEnd = 4000;
	    period = 500;
		
		try {
			bombSprites = new BufferedImage[3];
			
			spritesheet = ImageIO.read(new File("ressources/bomb-spritesheet.png"));
			bombSprites[0] = spritesheet.getSubimage(0, 0, width, height);
			bombSprites[1] = spritesheet.getSubimage(64, 0, width, height);
			bombSprites[2] = spritesheet.getSubimage(128, 0, width, height);
			
			flameSprites = new BufferedImage[2];
			
			flameSpritesheet = ImageIO.read(new File("ressources/flames.png"));
			flameSprites[0] = flameSpritesheet.getSubimage(0, 0, width, height);
			flameSprites[1] = spritesheet.getSubimage(64, 0, width, height);
		}
		catch(Exception e) {
			System.out.println("Bomb creation failed.");
			e.printStackTrace();
		}
		
		bombAnimation = new Animation();
		flameAnimation = new Animation();
	}
	
	public void setX(double x) { this.x = x; }
	public void setY(double y) { this.y = y; }
	public void setBombPower(int i) { this.bombPower = i; }
	public void setDropped(boolean b) { this.dropped = b; }
		
	public void update() {
		bombAnimation.setSprite(bombSprites);
		bombAnimation.setDelay(150);
		flameAnimation.setSprite(flameSprites);
		flameAnimation.setDelay(150);
		bombAnimation.update();
		flameAnimation.update();
	}
	
	public void draw(Graphics g) {
		if(dropped) {
			int roundXCord = highest64ValueUnder((x - width / 2));
			int roundYCord = highest64ValueUnder((y - height / 2));
			g.drawImage(bombAnimation.sprites[0], roundXCord, roundYCord, null);
			
			explode(g);
		}
	}
	
	public int highest64ValueUnder(double d) {
		int ret = 0;
		
		while(ret < d) {
			ret += 64;
		}
		int a = (int) ((double) ret - d);
		int b = (int) d - (ret - 64);
		if(a < b)
			return (int) (d + a) + 1;
		return (int) ((double) d - b) + 1;
	}
	
	public void explode(Graphics g) {
	    Timer timer = new Timer("Timer");
	    Bomb b = this;
	    
	    timer.scheduleAtFixedRate(new TimerTask() {
            long t = System.currentTimeMillis();
            Bomb tmp = b;
            
            public void run() {
            	if(System.currentTimeMillis() - t > delayEnd) {
            		tmp.dropped = false;
            		cancel();
            	}
            	else {
            		drawFlames(g, tmp.dropped);
            	}
            }
	    }, delayStart, period);
	}
	
	public void drawFlames(Graphics g, boolean b) {
		if(!b)
			return;
		
		int caseBas = map.getMap()[map.getLigneTile(highest64ValueUnder(y + height / 2))][map.getColTile(highest64ValueUnder(x - width / 2))];
		boolean caseBasCassable = true;
		
		int caseDroite = map.getMap()[map.getLigneTile(highest64ValueUnder(y - height / 2))][map.getColTile(highest64ValueUnder(x + width / 2))];
		boolean caseDroiteCassable = true;
		
		int caseGauche = map.getMap()[map.getLigneTile(highest64ValueUnder(y - height / 2))][map.getColTile(highest64ValueUnder(x - 3 * (width / 2)))];
		boolean caseGaucheCassable = true;
		
		int caseHaut = map.getMap()[map.getLigneTile(highest64ValueUnder(y - 3 * (height / 2)))][map.getColTile(highest64ValueUnder(x - width / 2))];
		boolean caseHautCassable = true;
		
		for(int i = 0; i < bombPower; i++) {
			if(caseBasCassable) {
				caseBas = map.getMap()[map.getLigneTile(highest64ValueUnder(y + (i * 64) + height / 2))][map.getColTile(highest64ValueUnder(x - width / 2))];
				if(caseBas != 1)
					caseBasCassable = true;
				else
					caseBasCassable = false; 
			}
			
			if(caseDroiteCassable) {
				caseDroite = map.getMap()[map.getLigneTile(highest64ValueUnder(y - height / 2))][map.getColTile(highest64ValueUnder(x + (i * 64) + width / 2))];
				if(caseDroite != 1)
					caseDroiteCassable = true;
				else
					caseDroiteCassable = false;
			}
			
			if(caseGaucheCassable) {
				caseGauche = map.getMap()[map.getLigneTile(highest64ValueUnder(y - height / 2))][map.getColTile(highest64ValueUnder(x - (i * 64) - 3 * (width / 2)))];
				if(caseGauche != 1)
					caseGaucheCassable = true;
				else
					caseGaucheCassable = false;
			}
			
			if(caseHautCassable) {
				caseHaut = map.getMap()[map.getLigneTile(highest64ValueUnder(y - (i * 64) - 3 * (height / 2)))][map.getColTile(highest64ValueUnder(x - width / 2))];
				if(caseHaut != 1)
					caseHautCassable = true;
				else
					caseHautCassable = false;
			}
			
			if(caseBasCassable) {
				g.drawImage(flameAnimation.sprites[0], highest64ValueUnder(x - width / 2), highest64ValueUnder(y + (i * 64) + height / 2), null);
				if(caseBas == 4 || caseBas == 5) {
					map.getMap()[map.getLigneTile(highest64ValueUnder(y + (i * 64) + height / 2))][map.getColTile(highest64ValueUnder(x - width / 2))] = 5;
				}
				else {
					map.getMap()[map.getLigneTile(highest64ValueUnder(y + (i * 64) + height / 2))][map.getColTile(highest64ValueUnder(x - width / 2))] = 6;
				}
				int nextCaseBas = map.getMap()[map.getLigneTile(highest64ValueUnder(y + (i * 64 + 64) + height / 2))][map.getColTile(highest64ValueUnder(x - width / 2))];
				//System.out.println("BAS : " + caseBas + " " + nextCaseBas);
				if((caseBas == 2 || caseBas == 4 || caseBas == 5 || caseBas == 6) && nextCaseBas != 1 && nextCaseBas != 6)
					caseBasCassable = false;
			}
		
			if(caseDroiteCassable) {
				g.drawImage(flameAnimation.sprites[0], highest64ValueUnder(x + (i * 64) + width / 2), highest64ValueUnder(y - height / 2), null);
				if(caseDroite == 4 || caseDroite == 5) {
					map.getMap()[map.getLigneTile(highest64ValueUnder(y - height / 2))][map.getColTile(highest64ValueUnder(x + (i * 64) + width / 2))] = 5;
				}
				else if(caseDroite == 7 || caseDroite == 8) {
					map.getMap()[map.getLigneTile(highest64ValueUnder(y - height / 2))][map.getColTile(highest64ValueUnder(x + (i * 64) + width / 2))] = 8;
				}
				else {
					map.getMap()[map.getLigneTile(highest64ValueUnder(y - height / 2))][map.getColTile(highest64ValueUnder(x + (i * 64) + width / 2))] = 6;
				}
				int nextCaseDroite = map.getMap()[map.getLigneTile(highest64ValueUnder(y - height / 2))][map.getColTile(highest64ValueUnder(x + (i * 64 + 64) + width / 2))];
				//System.out.println("DROITE " + caseDroite + " " + nextCaseDroite);
				if((caseDroite == 2 || caseDroite == 4 || caseDroite == 5 || caseDroite == 6) && nextCaseDroite != 1 && nextCaseDroite != 6)
					caseDroiteCassable = false;
			}
		
		
			if(caseGaucheCassable) {
				g.drawImage(flameAnimation.sprites[0], highest64ValueUnder(x - (i * 64) - 3 * (width / 2)), highest64ValueUnder(y - height / 2), null);
				if(caseGauche == 4 | caseGauche == 5) {
					map.getMap()[map.getLigneTile(highest64ValueUnder(y - height / 2))][map.getColTile(highest64ValueUnder(x - (i * 64) - 3 * (width / 2)))] = 5;
				}
				else {
					map.getMap()[map.getLigneTile(highest64ValueUnder(y - height / 2))][map.getColTile(highest64ValueUnder(x - (i * 64) - 3 * (width / 2)))] = 6;
				}
				int nextCaseGauche = map.getMap()[map.getLigneTile(highest64ValueUnder(y - height / 2))][map.getColTile(highest64ValueUnder(x - (i * 64 + 64) - 3 * (width / 2)))];
				//System.out.println("GAUCHE : " + caseGauche + " " + nextCaseGauche);
				if((caseGauche == 2 || caseGauche == 4 || caseGauche == 5 || caseGauche == 6) && nextCaseGauche != 1 && nextCaseGauche != 6)
					caseGaucheCassable = false;
			}
		
		
			if(caseHautCassable) {
				g.drawImage(flameAnimation.sprites[0], highest64ValueUnder(x - width / 2), highest64ValueUnder(y - (i * 64) - 3 * (height / 2)), null);
				if(caseHaut == 4 || caseHaut == 5) {
					map.getMap()[map.getLigneTile(highest64ValueUnder(y - (i * 64) - 3 * (height / 2)))][map.getColTile(highest64ValueUnder(x - width / 2))] = 5;
				}
				else {
					map.getMap()[map.getLigneTile(highest64ValueUnder(y - (i * 64) - 3 * (height / 2)))][map.getColTile(highest64ValueUnder(x - width / 2))] = 6;
				}
				int nextCaseHaut = map.getMap()[map.getLigneTile(highest64ValueUnder(y - (i * 64 + 64) - 3 * (height / 2)))][map.getColTile(highest64ValueUnder(x - width / 2))];
				//System.out.println("HAUT : " + caseHaut + " " + nextCaseHaut);
				if((caseHaut == 2 || caseHaut == 4 || caseHaut == 5 || caseHaut == 6) && nextCaseHaut != 1 && nextCaseHaut != 6)
					caseHautCassable = false;
			}
		}
	}
	
}