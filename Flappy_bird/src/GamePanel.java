import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.util.*;
public class GamePanel extends JPanel implements ActionListener,KeyListener{
	private static final long serialVersionUID = 1L;
	
	public final int borderheight = 640;// 1/4 ==  160;
	public final int borderwidth = 360;//1/3 == 120;
	public BufferedImage BG,Bird,Bottom_pipe,Top_pipe;
	
	//bird
	public final int BirdX = borderwidth/8;
	public final int BirdY = borderheight/2;
	public final int BirdWidth = 34;
	public final int BirdHeight = 24;
	public final int FPS = 60;

	Timer gameLoop;
	Timer placePipesTimer;
	public boolean gameOver = false;
	double score = 0;
	
	//pipes
	public int pipeX = borderwidth;
	public int pipeY = 0;
	public int pipeWidth = 64;
	public int pipeHeight = 512;
	
	
	class Pipe{
		int x = pipeX;
		int y = pipeY;
		int width = pipeWidth;
		int height = pipeHeight;
		BufferedImage img;
		boolean passed = false;
		Pipe(BufferedImage img){
			this.img = img;
		}
	}
	
	class Bird{
		int x = BirdX;
		int y = BirdY;
		int width = BirdWidth;
		int height = BirdHeight;
		
		BufferedImage img;
		Bird(BufferedImage img){
			this.img = img;
		}
	}

	//Game Logics
		Bird bird;
		Pipe pipe;
		public int velocityX = -4;
		public int velocityY = -10;
		public int gravity = 1;
		
		ArrayList<Pipe>pipes;
		Random rand = new Random();
		
	GamePanel() throws IOException {
		Dimension size = new Dimension(borderwidth,borderheight);
		setPreferredSize(size);
		setFocusable(true);
		addKeyListener(this);
		
		//Adding Images
		BG = ImageIO.read(getClass().getResourceAsStream("Images/BG.png"));
		Bird = ImageIO.read(getClass().getResourceAsStream("Images/Bird.png"));
		Bottom_pipe = ImageIO.read(getClass().getResourceAsStream("Images/Bottom_pipe.png"));
		Top_pipe = ImageIO.read(getClass().getResourceAsStream("Images/Top_pipe.png"));
		
		bird = new Bird(Bird);
		pipes = new ArrayList<Pipe>();
		
		placePipesTimer = new Timer(1500, new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	              // Code to be executed
	              placePipes();
	            }

				
	        });
	        placePipesTimer.start();
	        
	        gameLoop = new Timer(1000/FPS,this);
	        gameLoop.start();
		       
	}
	private void placePipes() {
		
		int randomPipeY = (int)(pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
		int openingSpace = borderheight/4; 
		Pipe topPipe = new Pipe(Top_pipe);	
		topPipe.y = randomPipeY;
		pipes.add(topPipe);
		
		Pipe bottomPipe = new Pipe(Bottom_pipe);
		bottomPipe.y = topPipe.y + pipeHeight + openingSpace;
		pipes.add(bottomPipe);
		
		}

	public boolean collision(Bird a,Pipe b) {
		return a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
	               a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
	               a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
	               a.y + a.height > b.y;  //a's bottom left corner passes b's top left corner
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		update();
		repaint();
		
		if(gameOver) {
			placePipesTimer.stop();
			gameLoop.stop();
		}
		
	}    
	public void update() {
		velocityY += gravity;
		bird.y += velocityY;
		bird.y = Math.max(bird.y, 0);
		
		for(int i=0;i<pipes.size();i++) {
			pipe = pipes.get(i);
			pipe.x += velocityX;
			
			if(!pipe.passed && bird.x > pipe.x + pipe.width) {
				pipe.passed = true;
				score += 0.5;
			}
			if(collision(bird,pipe)) {
				gameOver = true;
			}
		}
		
		if(bird.y >borderheight) {
			gameOver = true;
		}
		
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		draw(g2);
	}

	private void draw(Graphics2D g2) {
		// TODO Auto-generated method stub
		g2.drawImage(BG, 0, 0, borderwidth, borderheight,null);
		g2.drawImage(bird.img,bird.x,bird.y,bird.width,bird.height,null);
		//pipes
		for(int i=0;i<pipes.size();i++) {
			pipe = pipes.get(i);
			g2.drawImage(pipe.img,pipe.x,pipe.y,pipe.width,pipe.height,null);
		}
		
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("Arial",Font.PLAIN,32));
		if(gameOver) {
			g2.drawString("Total Score:"+String.valueOf((int) score), 10,35);
			g2.drawString("GAME OVER", BirdX, BirdY);
		}
		else {
			g2.drawString("Score :"+String.valueOf((int) score), 10, 35);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			velocityY = -10;
			
			if(gameOver) {
				bird.y = BirdY;
				velocityY = -10;
				pipes.clear();
				score = 0;
				gameOver = false;
				gameLoop.start();
				placePipesTimer.start();
			}
		}   
	}

	@Override
	public void keyTyped(KeyEvent e) {}
	@Override
	public void keyReleased(KeyEvent e) {}
	
}

