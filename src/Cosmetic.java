import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Cosmetic {
	
	private int x;
	private int y;
	
	private static int width;
	private static int height;
	
	private static Map map;
	
	private CosmeticType type;
	private String name;
	
	private Animation animation;
	private BufferedImage spritesheet;
	private BufferedImage[] idleSprites;
	private BufferedImage[] upSprites;
	private BufferedImage[] downSprites;
	private BufferedImage[] leftSprites;
	private BufferedImage[] rightSprites;
	
	public Cosmetic(Map map, CosmeticType type, String name) {
		this.map = map;
		this.type = type;
		this.name = name;
		
		width = 64;
		height = 64;
		
		try {
			idleSprites = new BufferedImage[1];
			upSprites = new BufferedImage[4];
			downSprites = new BufferedImage[4];
			leftSprites = new BufferedImage[4];
			rightSprites = new BufferedImage[4];
			
			spritesheet = ImageIO.read(new File("ressources/cosmetics/"+name+".png"));
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
			System.out.println("Cosmetic : " + name + "failed.");
			e.printStackTrace();
		}
		
		animation = new Animation();
	}
	
}
