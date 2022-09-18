import javax.swing.JFrame;

public class Game {
	
	public static void main(String[] args) {
		JFrame window = new JFrame("Bomberman");
		window.setResizable(false);		
		window.setContentPane(new GamePanel());
		window.pack();
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.setTitle("Bomberman");
	}

}
