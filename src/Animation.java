import java.awt.image.*;

public class Animation {
	
	public BufferedImage[] sprites;
	private int currentSprite;
	
	private long startTime;
	private long delay;
	
	public Animation() {}
	
	public void setSprite(BufferedImage[] sprites) {
		this.sprites = sprites;
		
		if(currentSprite >= sprites.length) // Out of bounds
			currentSprite = 0;
	}
	
	public void setDelay(long delay) {
		this.delay = delay;
	}
	
	public void update() {
		if(delay == -1) // Si d�lai n�gatif, on fait aucune animation
			return;
		
		long tempsEcoule = (System.nanoTime() - startTime) / 1000000;
		if(tempsEcoule > delay) { // Si le temps �coul� est plus grand que le d�lai, on passe au sprite suivant
			currentSprite++;
			startTime = System.nanoTime();
		}
		if(currentSprite == sprites.length) { // Si on est � derni�re colonne de la ligne, on revient au d�but de la ligne
			currentSprite = 0;
		}
	}
	
	public BufferedImage getSprite() {
		return sprites[currentSprite];
	}
	
}
