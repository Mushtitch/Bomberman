import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable, KeyListener {
	
	public static final int WIDTH = 1216;
	public static final int HEIGHT = 704;
	
	private Thread thread;
	private boolean running;
	
	private BufferedImage image;
	private Graphics g;
		
	private Map map;
	private Player player;
	private Mob mob1;
	
	public GamePanel() {
		super();
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		setFocusable(true);
		requestFocus();
	}
	
	public void addNotify() {
		super.addNotify();
		if(thread == null) {
			thread = new Thread(this);
			thread.start();
		}
		addKeyListener(this);
	}
	
	public void run() {
		init();
		
		while(running) {
			update();
			render();
			draw();
		}
	}
	
	private void init() {
		running = true;
		image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		g = (Graphics) image.getGraphics();
		
		map = new Map("ressources/levels/level1.txt", 64);
		map.loadTiles("ressources/tileset.png");
		
		player = new Player(map, "chapeau", "lunettes", "costard", "none");
		player.setX(1 * player.getSize()+(player.getSize() / 2));
		player.setY(1 * player.getSize()+(player.getSize() / 2));
		
		mob1 = new Mob(map);
		mob1.setX(17 * mob1.getSize()+(mob1.getSize() / 2));
		mob1.setY(9 * mob1.getSize()+(mob1.getSize() / 2));
	}
	
	private void update() {
		map.update();
		player.update();
		mob1.update();
	}
	
	private void render() {
		map.draw(g);
		player.draw(g);
		for(int i = 0; i < player.getNbBombes(); i++) {
			player.getBomb()[i].draw(g);
		}
		mob1.draw(g);
	}
	
	private void draw() {
		Graphics g2 = getGraphics();
		g2.drawImage(image,  0,  0,  null);
		g2.dispose();
	}
	
	public void keyTyped(KeyEvent key) {} // Inutile, juste pour ne pas avoir d'erreurs avec l'interface de KeyListener
	public void keyPressed(KeyEvent key) {
		int code = key.getKeyCode();
		
		if(code == KeyEvent.VK_LEFT) {
			player.setLeft(true);
		}
		if(code == KeyEvent.VK_RIGHT) {
			player.setRight(true);
		}
		if(code == KeyEvent.VK_UP) {
			player.setUp(true);
		}
		if(code == KeyEvent.VK_DOWN) {
			player.setDown(true);
		}
		if(code == KeyEvent.VK_SPACE) {
			player.setDropBomb(player.getBombDropped(), true);
			player.getBomb()[player.getBombDropped()].setX(player.getX());
			player.getBomb()[player.getBombDropped()].setY(player.getY());
		}
	}
	public void keyReleased(KeyEvent key) {
		int code = key.getKeyCode();
		
		if(code == KeyEvent.VK_LEFT) {
			player.setLeft(false);
		}
		if(code == KeyEvent.VK_RIGHT) {
			player.setRight(false);;
		}
		if(code == KeyEvent.VK_UP) {
			player.setUp(false);
		}
		if(code == KeyEvent.VK_DOWN) {
			player.setDown(false);
		}
	}
	
}