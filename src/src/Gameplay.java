package src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;

public class Gameplay extends PApplet implements KeyListener, MouseListener{
	private static final long serialVersionUID = 1L;

	//pictures
	
	//bee
	PImage bee = loadImage("bee.png");
	PImage beeOpenMouth = loadImage("bee open mouth.png");
	PImage beeHurt = loadImage("bee hurt closed mouth.png");
	PImage beeHurtOpenMouth = loadImage("bee hurt.png");
	PImage beeDrinking = loadImage("bee drinking.png");
	PImage beeHurtDrinking = loadImage("bee hurt drinking.png");
	
	//pages
	PImage background = loadImage("bee background.png");
	PImage title = loadImage("bee title.png");
	PImage startButton = loadImage("start button.png");
	PImage instructions = loadImage("instructions.png");
	
	//sprites
	PImage flower = loadImage("daisy.png");
	PImage honey = loadImage("honey.png");
	PImage enemy = loadImage("wasp.png");
	
	//shop stuff
	PImage shopHeart = loadImage("shop_heart.png");
	PImage shopSpeedUp = loadImage("shop_wings.png");
	PImage shopShrinkSerum = loadImage("shop_growBee.png");
	PImage shopShield = loadImage("shop_shield.png");
	PImage shopSlowEnemy = loadImage("shop_slowEnemy.png");
	
	//variables
	int amountOfHoney;
	int score;
	int highScore;
	boolean checkScore;
	int lives;
	
	//pages
	boolean startPage;
	boolean instructionsPage;
	boolean gamePage;
	boolean storePage;
	boolean endPage;
	
	//sprite vars
	int beeX;
	int beeY;
	
	int flowerMax;
	int[] flowerX;
	int[] flowerY;
	int[] flowerAmount;
	
	int honeyMax;
	int[] honeyX;
	int[] honeyY;
	int honeyIterator;
	boolean noHoney;
	
	int enemyMax;
	int[] enemyX;
	int[] enemyY;
	
	//shop vars
	int speedIncrease;
	int enemySpeedDecrease;
	int sizeDecrease;
	boolean shieldActive;
	int shieldX;
	int shieldY;
	
	//formatting
	PFont myFont = createFont("Arial",50);
	PFont myFont1 = createFont("Arial", 40);
	
	Random randomNumberGenerator = new Random();
	
	public void setup() {
		size(1365,660);
		drawBackground(0,0);
		
		//initializing page vars
		startPage = true;
		instructionsPage = false;
		gamePage = false;
		storePage = false;
		endPage = false;
		
		//initializing other variables
		noHoney = false;
		amountOfHoney = 4;
		score = 0;
		highScore = 0;
		checkScore = false;
		lives = 5;
		
		honeyMax = 100;
		honeyX = new int[honeyMax];
		honeyY = new int[honeyMax];
		honeyIterator = 0;
		
		beeX = 30;
		beeY = 250;
		
		flowerMax = 4;
		flowerX = new int[flowerMax];
		flowerY = new int[flowerMax];
		flowerAmount = new int[flowerMax];
		
		enemyMax = 8;
		enemyX = new int[enemyMax];
		enemyY = new int[enemyMax];
		
		//initializing shop vars
		speedIncrease = 0;
		enemySpeedDecrease = 0;
		sizeDecrease = 0;
		shieldActive = false;
		
		//setting all values in flowerAmt or how much honey each flower contains
		for(int a = 0; a < flowerAmount.length; a ++) {
			flowerAmount[a] = 0;
		}
	}
	
	public void draw() {
		drawBackground(0,0);
		if(startPage == true) {
			drawTitle(500, 200);
			drawStartButton(550, 300);
		}
		else if(instructionsPage == true) {
			drawInstructions(0, 0);
		}
		else if(gamePage == true) {
			textFont(myFont);
			text("Honey: " + amountOfHoney, 0, 600, 500, 500);
			text("Score: " + score, 0, 0, 500, 500);
			if(amountOfHoney == 0) {
				textFont(myFont1);
				text("Go collect some more honey", 300, 0, 500, 500);
			}
			
			//drawing the flowers
			for(int a = 0; a < flowerMax; a ++) {
				if(flowerAmount[a] < 0) {
					flowerX[a] = randomNumberGenerator.nextInt(1000);
					flowerY[a] = 470;
					drawFlower(flowerX[a], flowerY[a]);
					flowerAmount[a] = 5;
				}
				else {
					drawFlower(flowerX[a],flowerY[a]);
				}
			}
			
			//shooting honey and collisions
			for(int b = 0; b < honeyMax; b ++) {
				for(int c = 0; c < enemyMax; c ++) {
					if((honeyX[b] > (enemyX[c] - 80)) && (honeyX[b] < (enemyX[c] + 80)) && (honeyY[b] > (enemyY[c] - 80)) && (honeyY[b] < (enemyY[c] + 80))) {
						score += 5;
						enemyX[c] = randomNumberGenerator.nextInt(300) + 1370;
						enemyY[c] = randomNumberGenerator.nextInt(500);
						honeyX[b] = -50;
						honeyY[b] = -50;
					}
				}
				if(honeyY[b] > 1370 || honeyX[b] <= 0) {
					//deletes the honey if offscreen
					honeyX[b] = -50;
					honeyY[b] = -50;
				}
				else {
					drawHoney(honeyX[b], honeyY[b]);
					honeyX[b] += 5;
				}
			}
			
			//creates the enemies
			for(int d = 0; d < enemyMax; d ++) {
				if((beeX > (enemyX[d] - 80)) && (beeX < (enemyX[d] + 80)) && (beeY > (enemyY[d] - 80)) && (beeY < (enemyY[d] + 80)) && shieldActive == false) {
					enemyX[d] = randomNumberGenerator.nextInt(300) + 1370;
					enemyY[d] = randomNumberGenerator.nextInt(500);
					lives --;
				}
				else if((beeX > (enemyX[d] - 80)) && (beeX < (enemyX[d] + 80)) && (beeY > (enemyY[d] - 80)) && (beeY < (enemyY[d] + 80)) && shieldActive == true) {
					enemyX[d] = randomNumberGenerator.nextInt(300) + 1370;
					enemyY[d] = randomNumberGenerator.nextInt(500);
					shieldActive = false;
				}
				drawEnemy(enemyX[d], enemyY[d]);
				enemyX[d] -= 3 - enemySpeedDecrease;
				if(enemyX[d] < -50) {
					enemyX[d] = randomNumberGenerator.nextInt(300) + 1370;
					enemyY[d] = randomNumberGenerator.nextInt(500);
				}
			}
			
			//shield
			if(shieldActive == true) {
				drawShield(beeX + 50, beeY + 20);
			}
			
			//displays the lives
			for(int e = 0; e < lives; e ++) {
				image(bee, 1300-e*50, 600, 14*3, 13*3);
			}
			
			//if game over
			if(lives <= 0) {
				gamePage = false;
				endPage = true;
				checkScore = true;
			}
			
			//draws the bee
			drawBee(beeX, beeY);
		}
		else if(storePage == true) {
			textFont(myFont);
			text("Welcome to the shop!", 450, 0, 500, 500);
			text("Honey : " + amountOfHoney, 0, 600, 500, 500);
			drawHeart(50, 100);
			textFont(myFont1);
			text("One Life ~ 50", 40, 200, 500, 500);
			drawSpeedUp(250, 100);
			text("Increase Speed ~ 50", 180, 200, 500, 500);
			drawShrinkSerum(450, 100);
			text("Shrink Serum ~ 75", 400, 200, 500, 500);
			drawShield(650, 100);
			text("One-use shield ~ 25", 580, 200, 500, 500);
			drawSlowEnemy(850, 100);
			text("Slower enemies ~ 30", 800, 200, 500, 500);
		}
		else if(endPage == true) {
			textFont(myFont);
			text("The end!", 550, 150, 500, 500);
			text("Score: " + score, 400, 250, 500, 500);
			text("'r' to play again", 400, 350, 500, 500);
			if(checkScore == true) {
				highScore = determineHighScore();
				writeScore();
				checkScore = false;
			}
			else {
				if(highScore > score) {
					text("High score: " + determineHighScore(), 500, 100, 500, 500);
				}
				else if(highScore == score) {
					text("You got the high score!", 500, 100, 500, 500);
				}
				else {
					text("You beat the high score of " + highScore, 500, 100, 500, 500);
				}
			}
		}
	}
		
	//determining the highscore
	public int determineHighScore() {
		int highScore = 0;
		try {
			File file = new File("scores");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			while(line != null) {
				try {
					int score = Integer.parseInt(line.trim());
					if(score > highScore) {
						highScore = score;
					}
				} catch(NumberFormatException e1) {
					System.out.println("ignoring invalid score: " + line);
				}
				line = br.readLine();
			}
			br.close();
		} catch(IOException ex) {
		System.out.println("ERROR reading scores from file");
		}
		return highScore;
	}
	
	//saving the score
	public void writeScore() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("scores", true));
			bw.newLine();
			bw.append("" + score);
			System.out.println("score added!");
			bw.close();
		} catch (IOException e) {
			System.out.println("ERROR writing score to file");
		}	
	}
	
	//finding the closest flower
	public int closestFlower() {
		double smallestDistance = 200;
		int closestFlower = 0;
		double distanceX;
		double distanceY;
		double hypotenuse;
		for(int a = 0; a < flowerX.length; a ++) {
			distanceX = Math.abs(beeX - flowerX[a]);
			distanceY = Math.abs(beeY - flowerY[a]);
			hypotenuse = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
			if(hypotenuse < smallestDistance) {
				smallestDistance = hypotenuse;
				closestFlower = a;
			}
		}
		return closestFlower;
	}
	
	//checking that the bee and the flower are touching
	public boolean checkBeeAndFlowerTouch(int a) {
		boolean result;
		if(Math.abs((double)beeX - flowerX[a]) < 80 && Math.abs((double)beeY - flowerY[a]) < 80) {
			result = true;
		}
		else {
			result = false;
		}
		return result;
	}
	
	public boolean checkTouch() {
		boolean result;
		int a = closestFlower();
		result = checkBeeAndFlowerTouch(a);
		return result;
	}		
			//keyListener
	@Override
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_ENTER && instructionsPage == true) {
			instructionsPage = false;
			gamePage = true;
		}
		else if(key == KeyEvent.VK_UP && gamePage == true) {
			beeY -= 10 + speedIncrease;
		}
		else if(key == KeyEvent.VK_DOWN && gamePage == true) {
			beeY += 10 + speedIncrease;
		}
		else if(key == KeyEvent.VK_LEFT && gamePage == true) {
			beeX -= 10 + speedIncrease;
		}
		else if(key == KeyEvent.VK_RIGHT && gamePage == true) {
			beeX += 10 + speedIncrease;
		}
		else if(key == KeyEvent.VK_D && gamePage == true && checkTouch()) {
			amountOfHoney ++;
			int a = closestFlower();
			flowerAmount[a]--;
		}
		else if(key == KeyEvent.VK_SPACE && gamePage == true) {
			if(honeyIterator >= 100) {
				honeyIterator = 0;
			}
			else if(amountOfHoney > 0) {
				honeyX[honeyIterator] = beeX + 70;
				honeyY[honeyIterator] = beeY + 40;
				honeyIterator++;
				amountOfHoney--;
			}
		}
		else if(key == KeyEvent.VK_S && gamePage == true) {
			gamePage = false;
			storePage = true;
		}
		else if(key == KeyEvent.VK_S && storePage == true) {
			gamePage = true;
			storePage = false;
		}
		else if(key == KeyEvent.VK_R && endPage == true) {
			endPage = false;
			startPage = true;
			setup();
		}
	}
	
	//mouse listener
	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println("x-coordinate: " + e.getX() + " y-coordinate: " + e.getY());
		int xCord = e.getX();
		int yCord = e.getY();
		
		//store upgrades
		if(inBetween(xCord, 85, 110) && inBetween(yCord, 90, 105) && canAfford(50) && lives < 5) {
			lives++;
			amountOfHoney-=50;
			System.out.println("Bought a life!");
		}
		else if(inBetween(xCord, 265, 305) && inBetween(yCord, 95, 120) && canAfford(50) && speedIncrease <= 100) {
			speedIncrease += 10;
			amountOfHoney-= 50;
			System.out.println("Bee speed increased!");
		}
		else if(inBetween(xCord, 480, 520) && inBetween(yCord, 95, 135) && canAfford(75) && sizeDecrease <= 80) {
			sizeDecrease+=20;
			amountOfHoney-=75;
			System.out.println("Bee size decreased!");
		}
		else if(inBetween(xCord, 670, 740) && inBetween(yCord, 80, 140) && canAfford(25) && shieldActive == false) {
			shieldActive = true;
			amountOfHoney-=25;
			System.out.println("Shield active!");
		}
		else if(inBetween(xCord, 850, 940) && inBetween(yCord, 70, 130) && canAfford(30) && enemySpeedDecrease == 0) {
			enemySpeedDecrease = 1;
			amountOfHoney-=35;
			System.out.println("Enemy speed decreased for a little while!");
		}			
	}
	
	public boolean inBetween(int orig, int bot, int top) {
		boolean result;
		if(orig > bot && orig < top) {
			result = true;
		}
		else {
			result = false;
		}
		return result;
	}
	
	public boolean canAfford(int price) {
		boolean result;
		if(amountOfHoney < price) {
			result = false;
		}
		else {
		result = true;
		}
		return result;
	}
	
	//drawing methods
	
	public void drawBackground(int x, int y) {
		image(background, x, y, 1360, 660);
	}
	
	public void drawTitle(int x, int y) {
		image(title, x, y, 600, 500);
	}
	
	public void drawStartButton(int x, int y) {
		image(startButton, x, y, 600, 500);
	}
	
	public void drawInstructions(int x, int y) {
		image(instructions, x, y, 1360, 660);
	}
	
	public void drawBee(int x, int y) {
		image(bee, x, y, 100-sizeDecrease, 100-sizeDecrease);
	}
	
	public void drawFlower(int x, int y){
		image(flower, x, y, 200, 200);
	}
	
	public void drawHoney(int x, int y){
		image(honey, x, y, 30, 30);
	}
	
	public void drawEnemy(int x, int y){
		image(enemy, x, y, 30, 30);
	}
	
	public void drawHeart(int x, int y){
		image(shopHeart, x, y, 100, 100);
	}
	
	public void drawSpeedUp(int x, int y){
		image(shopSpeedUp, x, y, 100, 100);
	}
	
	public void drawShrinkSerum(int x, int y){
		image(shopShrinkSerum, x, y, 100, 100);
	}
	
	public void drawShield(int x, int y){
		image(shopShield, x, y, 100, 100);
	}
	public void drawSlowEnemy(int x, int y){
		image(shopSlowEnemy, x, y, 100, 100);
		}
}