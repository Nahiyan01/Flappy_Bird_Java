import java.io.IOException;
import javax.swing.JFrame;

public class Main extends JFrame{
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) throws IOException {
		JFrame window = new JFrame("Flappy Brid");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//window.setLocationRelativeTo(null);
		window.setResizable(false);
		GamePanel gamePanel = new GamePanel();
		window.setSize(gamePanel.borderwidth,gamePanel.borderheight);
		window.add(gamePanel);
		window.pack();
		gamePanel.requestFocus();
		window.setVisible(true);
	}
}
