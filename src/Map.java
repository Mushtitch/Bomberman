import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

public class Map {

	private String mapLevel;
	
	private int tileSize;
	private int mapWidth;
	private int mapHeight;
	
	private BufferedImage tileset;
	private Tile[][] tiles;
	
	private Animation speedBonusAnimation;
	private BufferedImage speedBonusSpritesheet;
	private BufferedImage[] speedBonus;
	
	private int[][] map;
	
	private Random random;
	private int[][] newMap;
		
	public Map(String mapLevel, int tileSize) {
		this.tileSize = tileSize;
		this.mapLevel = mapLevel;
		
		speedBonus = new BufferedImage[1];
		try {
			speedBonusSpritesheet = ImageIO.read(new File("ressources/speed.png"));
			speedBonus[0] = speedBonusSpritesheet.getSubimage(0, 0, 64, 64);
		} catch (IOException e) {
			e.printStackTrace();
		}
		speedBonusAnimation = new Animation();
		speedBonusAnimation.setSprite(speedBonus);
		loadLevel();
		newMap = new int[mapHeight][mapWidth];
		initNewMap();
	}
	
	public void loadLevel() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(mapLevel));
			mapWidth = Integer.parseInt(br.readLine()); // On lit les entiers dans le fichier
			mapHeight = Integer.parseInt(br.readLine()); // On lit les entiers dans le fichier
			map = new int[mapHeight][mapWidth];
			String delimiteur = "\\s+";
			for(int ligne = 0; ligne < mapHeight; ligne++) {
				String line = br.readLine(); // On prend toute la ligne du fichier
				String[] tokens = line.split(delimiteur); // On prends les valeurs entre les d�limiteurs
				for(int col = 0; col < mapWidth; col++) {
					if(Integer.parseInt(tokens[col]) == 0) {
						random = new Random();
						Integer brick = random.nextInt(2) + 1;
						if(brick == 2) {
							random = new Random();
							Integer bonus = random.nextInt(3) + 1;
							/*
							if(bonus == 2) { // Bombe suppl�mentaire
								map[ligne][col] = 4;
							}
							else */
							if(bonus == 3) // Vitesse du personnage
								map[ligne][col] = 7;
							else
								map[ligne][col] = brick;
						}
						else
							map[ligne][col] = Integer.parseInt(tokens[col]);
					}
					else
						map[ligne][col] = Integer.parseInt(tokens[col]); // On met la valeur dans le tableau 
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadTiles(String file) {
		try {
			tileset = ImageIO.read(new File(file));
			int tilesetWidth = (tileset.getWidth() + 1) / (tileSize + 1);
			int tilesetHeight = (tileset.getHeight() + 1) / (tileSize + 1);
			tiles = new Tile[tilesetHeight][tilesetWidth];
			
			BufferedImage tile;
			for(int ligne = 0; ligne < tilesetHeight; ligne++) {
				for(int col = 0; col < tilesetWidth; col++) {
					tile = tileset.getSubimage(col * tileSize, ligne * tileSize, tileSize, tileSize);
					tiles[ligne][col] = new Tile(tile);
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update() {
		
	}
	
	public void draw(Graphics g) {
		for(int ligne = 0; ligne < mapHeight; ligne++) {
			for(int col = 0; col < mapWidth; col++) {
				
				int valeur = map[ligne][col];
			
				if(valeur == 0 || valeur == 3) { // Herbe
					g.drawImage(tiles[0][0].getImage(), col * tileSize, ligne * tileSize, null);
				}
				if(valeur == 1) { // Mur
					g.drawImage(tiles[0][1].getImage(), col * tileSize, ligne * tileSize, null);
				}
				if(valeur == 2) { // Brique
					g.drawImage(tiles[0][2].getImage(), col * tileSize, ligne * tileSize, null);
				}
				/*
				if(valeur == 4) { // Brique + bonus bombe suppl�mentaire
					g.drawImage(Bomb.bombAnimation.sprites[1], col * tileSize, ligne * tileSize, null);
					g.drawImage(tiles[0][2].getImage(), col * tileSize, ligne * tileSize, null);
				}
				if(valeur == 5) { // Herbe + bonus bombe suppl�mentaire
					g.drawImage(tiles[0][0].getImage(), col * tileSize, ligne * tileSize, null);
					g.drawImage(Bomb.bombAnimation.sprites[1], col * tileSize, ligne * tileSize, null);
				}
				*/
				if(valeur == 6) { // Brique cass�e sans bonus (redevient herbe)
					g.drawImage(tiles[0][0].getImage(), col * tileSize, ligne * tileSize, null);
				}
				if(valeur == 7) { // Brique + bonus vitesse
					g.drawImage(speedBonusAnimation.sprites[0], col * tileSize, ligne * tileSize, null);
					g.drawImage(tiles[0][2].getImage(), col * tileSize, ligne * tileSize, null);
				}
				if(valeur == 8) { // Herbe + bonus vitesse
					g.drawImage(tiles[0][0].getImage(), col * tileSize, ligne * tileSize, null);
					g.drawImage(speedBonusAnimation.sprites[0], col * tileSize, ligne * tileSize, null);
				}
			}
		}
	}
	
	public void initNewMap() {
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				newMap[i][j] = -1;
			}
		}
	}

	public int[][] getMap() { return map; }
	public int[][] getNewMap() { return newMap; }

	
	public int getColTile(int x) {
		return x / tileSize;
	}
	public int getLigneTile(int y) {
		return y / tileSize;
	}
	public int getTile(int ligne, int col) {
		if(ligne < mapHeight && col < mapWidth) {
			return map[ligne][col];
		}
		else {
			System.out.println(map[(mapHeight)-(ligne-mapHeight)][(mapWidth)-(col-mapWidth)]);
			return map[(mapHeight-1)-(ligne-mapHeight)][(mapWidth-1)-(col-mapWidth)];
		}
	}
	public int getTileSize() {
		return tileSize;
	}
	
	public void afficheMap() {
		for(int ligne = 0; ligne < mapHeight; ligne++) {
			for(int col = 0; col < mapWidth; col++)  {
				System.out.println(map[ligne][col]);
			}
		}
	}
	
}