package armaan.pong;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;

@SuppressWarnings("serial")
public class Pong extends Applet implements Runnable, KeyListener{
	private Random r = new Random();
	private AI ai = new AI();
	private AI ai2 = new AI();
	
	final static int WIDTH = 800; //Frame dimensions


	final static int HEIGHT = 500;
	final int MOVESPEED = 5; //movement speed of paddles
	Thread thread;
	
	int y = HEIGHT/2 - 100; //paddle1 y cords
	int velY; //paddle1 y velocity
	
	int y2 = HEIGHT/2 - 100; //paddle2 y cords
	int velY2; //paddle2 y velocity
	
	//ball cords
	int ballX = WIDTH/2 - 10;
	int ballY = HEIGHT/2 - 10;
	
	//ball velocity (set to a random integer between -5 and +5 at start of round)
	int ballVelX = r.nextInt(10) - 5;
	int ballVelY = r.nextInt(10) - 5;
	
	int ballSpeed = 2;
	
	int Score1 = 0;
	int Score2 = 0;
	
	int R = 255;
	int G = 255;
	int B = 255;
	
	int menuColorR = 255;
	int menuColorG = 255;
	int menuColorB = 255;
	int c = 255;
	int d = 255;

	
	boolean menuExit = false;
	boolean pause = false;
	int directions = 0;
	int playerNum = 0;
	
	public void init() {
		this.resize(WIDTH, HEIGHT);
		
		thread = new Thread(this);
		thread.start();
		
		this.addKeyListener(this);
		
	}
	
//	public static void main(String[] args) {
//		JFrame j = new JFrame();
//		Pong game = new Pong();
//		j.pack();
//		j.setSize(WIDTH, HEIGHT);
//		j.setResizable(false);
//		j.setLocationRelativeTo(null);
//		j.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		j.add(game);
//		j.setVisible(true);
//		game.start();
//		
//	}
	
	@SuppressWarnings("deprecation")
	public void paint(Graphics g) {
		//Main Menu
		
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		g.setColor(new Color(menuColorR, menuColorG, menuColorB));
		g.setFont(new Font("Arial", Font.BOLD, 100));
		g.drawString("COLOR PONG", WIDTH/2-350, 250);
		
		g.setColor(new Color(c, c, c));
		g.setFont(new Font("Arial", Font.PLAIN, 30));
		g.drawString("Press \"enter\" to play, \"d\" for directions", WIDTH/2 - 280, 300);
		
		menuColorR = r.nextInt(255);
		menuColorG = r.nextInt(255);
		menuColorB = r.nextInt(255);
		
		//Directions set 1
		if (directions == 1 && menuExit == false) {
			menuColorR = 0;
			menuColorG = 0;
			menuColorB = 0;
			c = 0;
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 40));
			g.drawString("Player 1 is right, computer/player 2 is left", WIDTH/2 - 390, 200);
			g.setFont(new Font("Arial", Font.PLAIN, 30));
			g.drawString("Press \"enter\" to play, \"d\" to continue", WIDTH/2 - 265, 300);
		}
		
		//Directions set 2
		if (directions == 2 && menuExit == false) {
			menuColorR = 0;
			menuColorG = 0;
			menuColorB = 0;
			c = 0;
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 35));
			g.drawString("Player 1: Up to move up, down to move down", WIDTH/2 - 380, 200);
			g.drawString("Player 2: \"W\" to move up, \"S\" to move down", WIDTH/2 - 380, 250);
			g.setFont(new Font("Arial", Font.PLAIN, 30));
			g.drawString("Press \"enter\" to play, \"d\" to continue", WIDTH/2 - 265, 400);		
		}
		
		//Directions set 3
		if (directions == 3 && menuExit == false) {
			
			menuColorR = 0;
			menuColorG = 0;
			menuColorB = 0;
			c = 0;
			
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 45));
			g.drawString("Press \"p\" to pause the game", WIDTH/2 - 300, 200);
			g.setFont(new Font("Arial", Font.PLAIN, 30));
			g.drawString("Press \"enter\" to play, \"d\" to continue", WIDTH/2 - 265, 400);		
		} 
		
		//Directions set 4
		if (directions >= 4 && menuExit == false) {
			
			menuColorR = 0;
			menuColorG = 0;
			menuColorB = 0;
			c = 0;
			
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.BOLD, 45));
			g.drawString("Get 10 points to win", WIDTH/2 - 230, 200);
			g.setFont(new Font("Arial", Font.PLAIN, 30));
			g.drawString("Press \"enter\" to play", WIDTH/2 - 150, 300);
		} 
		
		//Prompts player to enter number of players
		if (menuExit == true) {
			menuColorR = 0;
			menuColorG = 0;
			menuColorB = 0;
			c = 0;
			
			g.setColor(new Color(d, d, d));
			g.setFont(new Font("Arial", Font.BOLD, 45));
			g.drawString("One or two player game?", WIDTH/2 - 300, 200);
			g.setFont(new Font("Arial", Font.PLAIN, 30));
			g.drawString("Press \"1\" or \"2\"", WIDTH/2 - 150, 300);
		}
		
		//Main game
		if (pause == false && playerNum != 0){
			menuColorR = 0;
			menuColorG = 0;
			menuColorB = 0;
			c = 0;
			d = 0;

			
			Font myFont = new Font("Arial", Font.PLAIN, 30);
			
			g.setColor(Color.black);// Background
			g.fillRect(0, 0, WIDTH, HEIGHT); 
			
			g.setColor(new Color(R, G, B));
			// Paddle1
			g.fillRect(20, y, 20, 200);
			
			// Paddle2
			g.fillRect(WIDTH - 40, y2, 20, 200);
			
			//Score
			g.setFont(myFont);
			g.drawString(Integer.toString(Score1), WIDTH/2 - 100, 50);
			g.drawString(Integer.toString(Score2), WIDTH/2 + 65, 50);
			g.drawString("Error: " +  Double.toString(ai.error).substring(0, (Double.toString(ai.error).length() > 7 ? 7 : (Double.toString(ai.error).length()))), 50, HEIGHT-50);
			g.drawString("Error: " +  Double.toString(ai2.error).substring(0, (Double.toString(ai2.error).length() > 7 ? 7 : (Double.toString(ai2.error).length()))), WIDTH-225, HEIGHT-50);

			
			//dividing bar
			g.fillRect(WIDTH/2 - 5, 0, 5, HEIGHT);
			
			//Ai
			/*if (playerNum == 1) {
				if (ballVelY <= 0) {
					velY = -2;
					while (y <= 0) {y+=1;}
				}
				if (ballVelY >= 0) {	
					velY = 2;
					while (y + 200 >= HEIGHT) {y-=1;}
				}
				if(ballX > WIDTH/2-10 || ballVelX > 0) {
					velY = 0;
				}
			}*/
			
			// Ball
			g.fillRect(ballX, ballY, 20, 20);
			
			
			//Sets paddle movement
			y += velY;
			y2 += velY2;
			
			//Makes sure the balls x or y velocity is != 0
			if (ballVelX == 0) {
				if (r.nextInt(1) == 1) {
					ballVelX++;
				} else {ballVelX--;}
				
			}
			
			if (ballVelY == 0) {
				if (r.nextInt(1) == 1) {
					ballVelY++;
				} else {ballVelY--;}
			}
			
			//Sets ball movement
			ballX += ballVelX * ballSpeed;
			ballY += ballVelY * ballSpeed;
			
			
			if (ballX <= 40 && ballY >= y && ballY <= y + 200) { // Ball Paddle 1 collision
				ballVelX = -(ballVelX);
				
				//Randomize color
				rand();
				}
			
			if (ballX >= WIDTH - 60 && ballY >= y2 && ballY <= y2 + 200) { // Ball Paddle 2 collision
				ballVelX = -(ballVelX);
				
				//Randomize color
				rand();
				}
			
			
			if (ballY <= 1) { //Ball top collision
				ballVelY = -(ballVelY);
				
				//Randomize color
				rand();
			}
			
			if (ballY + 20 >= HEIGHT) { //Ball bottom collision
				ballVelY = -(ballVelY);
				
				//Randomize color
				rand();
			}
			
			if (ballX <= 0) { // Ball Left Wall collision
				ballVelY = r.nextInt(10) - 5;
				ballVelX = r.nextInt(5);
		
				
				if (ballVelX == 0) {
					ballVelX++;
				}
				
				if (ballVelY == 0
						) {
					if (r.nextInt(1) == 1) {ballVelY++;}
					else {ballVelY--;}
				}
				
				ballX = WIDTH/2-20;
				ballY = HEIGHT/2-20;
				
				Score2++;
				
				//Randomize color
				rand();
			}
			
			if (ballX >= WIDTH - 20) { // Ball Right Wall collision
				ballVelX = -(r.nextInt(5));
				ballVelY = r.nextInt(10) - 5;
				
				if (ballVelX == 0) {
					ballVelX--;
				}
				
				if (ballVelY == 0) {
					if (r.nextInt(1) == 1) {ballVelY++;}
					else {ballVelY--;}
				}
				
				ballX = WIDTH/2-20;
				ballY = HEIGHT/2-20;
				
				Score1++;
				
				//Randomize color
				 rand();
			}
			
			if (y < 0 || y + 200 > HEIGHT) {velY = 0;} //Paddle1 out of frame
			if (y2 < 0 || y2 + 200 > HEIGHT) {velY2 = 0;} //Paddle2 out of frame
			
			/*if (Score1 == 10) { //Computer win
				
				g.setFont(myFont);
				g.drawString("Computer Wins", WIDTH/2 - 225, 200);
				
				thread.stop(); //Ends thread
			}
			if (Score2 == 10) { //Player
				g.setFont(myFont);
				g.drawString("You Win!", WIDTH/2 + 10, 200);
				
				thread.stop(); //Ends thread
			}*/
		}
		
		//pauses game
		if (pause == true) {
			menuColorR = 0;
			menuColorG = 0;
			menuColorB = 0;
			
			g.setColor(Color.black);
			g.fillRect(0, 0, WIDTH, HEIGHT); 
			
			g.setColor(new Color(R, G, B));
			// Paddle1
			g.fillRect(20, y, 20, 200);
			
			// Paddle2
			g.fillRect(WIDTH - 40, y2, 20, 200);
			
			
			g.setFont(new Font("Arial", Font.PLAIN, 30));
			g.drawString(Integer.toString(Score1), WIDTH/2 - 100, 50);
			g.drawString(Integer.toString(Score2), WIDTH/2 + 65, 50);
			
			g.fillRect(WIDTH/2 - 5, 0, 5, HEIGHT);
			
			g.fillRect(ballX, ballY, 20, 20);

		}
			
		
	
	}
	
	private void rand() {
		R = r.nextInt(255);
		G = r.nextInt(255);
		B = r.nextInt(255);
	}
	
	public void update (Graphics g) {
		paint(g);
	}

	@Override
	public void run() {
		while (true) {
			
			
			try {
				if (menuExit == false ) {
					repaint();
					Thread.sleep(200);
				}
				else {
					long start = System.currentTimeMillis();
					
					repaint();
					int motion = 5;
					if (playerNum == 1) {
						ai.update(new double[]{y+100, ballY+10});
						if (ballX + 10 < WIDTH/2 && ballVelX  < 0) {
							velY = (ai.outputs[2][0] < .5 ? -motion : motion);
						} else velY = 0;
						
						ai2.update(new double[]{y2+100, ballY+10});
						if (ballX + 10 > WIDTH/2 && ballVelX  > 0) {
							velY2 = (ai2.outputs[2][0] < .5 ? -motion : motion);
						} else velY2 = 0;
					}
					
					if (y <= 0 && velY < 0) velY = 2;
					else if (y >= HEIGHT-200 && velY > 0) velY = -2;
					
					if (y2 <= 0 && velY2 < 0) velY2 = 2;
					else if (y2 >= HEIGHT-200 && velY2 > 0) velY2 = -2;
					
					long timePassed = System.currentTimeMillis() - start;
					Thread.sleep((10 - timePassed > 0 ? 10 - timePassed : 0));
				}
			} catch (InterruptedException e){
				
				e.printStackTrace();
			}		
		}
	}

	
	//Unused keyTyped method (auto generated)
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		//Menu exit
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			menuExit = true;
		}
		//Directions
		if(e.getKeyCode() == KeyEvent.VK_D) {
			directions++;
		}
		//Pause
		if(e.getKeyCode() == KeyEvent.VK_P) {
			pause = !pause;
			System.out.println("p");
		}
		//1 Player
		if(e.getKeyCode() == KeyEvent.VK_1) {
			playerNum = 1;
		}
		//2 Player
		if(e.getKeyCode() == KeyEvent.VK_2) {
			playerNum = 2;
		}
		// Sets Paddle1 up is up, down is down
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			velY2 -= MOVESPEED;
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			velY2 += MOVESPEED;
		}
		
		//Sets Paddle2 up is w, down is s ONLY if there are two players
		if (playerNum == 2) {
			if(e.getKeyCode() == KeyEvent.VK_W) {
				velY -= MOVESPEED;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				velY += MOVESPEED;
			}
		}
	}
	
	@Override
	public void keyReleased(KeyEvent e) {		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			
		}
		if(e.getKeyCode() == KeyEvent.VK_D) {

		}
		if(e.getKeyCode() == KeyEvent.VK_P) {

		}
		if(e.getKeyCode() == KeyEvent.VK_1) {

		}
		if(e.getKeyCode() == KeyEvent.VK_2) {

		}
		
		// Makes sure velocity of paddle 1 and 2 is zero after key is released
		if(e.getKeyCode() == KeyEvent.VK_UP) {
			velY2  = 0;
			if (y2 < 0) {y2 = 0;}
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			velY2 = 0;
			if (y2 + 200 > HEIGHT) {y2 = HEIGHT - 200;}
		}
		
		if (playerNum == 2) {
			
			if(e.getKeyCode() == KeyEvent.VK_W) {
				velY = 0;
			} else if (e.getKeyCode() == KeyEvent.VK_S) {
				velY = 0;
			}
		}
	}
	
}




