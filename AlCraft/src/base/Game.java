package base;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.opengl.Texture;

import effects.Time;
import entities.CreatureType;
import entities.EnemyCreatures;
import entities.Player;
import entities.SkeleTest;
import entities.SlimeTest;
import static org.lwjgl.opengl.GL11.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import floorgeneration.*;

import static effects.Graphics.*;
public class Game {
	public static NewDungeon dungeon = new NewDungeon();
	public static int size = 57; //This is related to the 2D array in grid.. make sure to adjust for size as you expand the other shtuff
	public static String[][] world;
	public static Player player;
	public static SlimeTest test;
	public static SkeleTest skele;
	public static EnemyCreatures creature;
	private boolean isSpawn = true, isCSpawn = true, isSSpawn = true;
	private static Clip clip, slimehurt;
	private static boolean alive = true;
	private int rng1, rng2, rng1a, rng2a, rng1b, rng2b;
	public Game() throws IOException, Exception {
		Start();
		//Believe or not, these methods load the world off the file and create the dungeon.
		dungeon.createFloor(50, 50);
		loadWorld();
		clip = AudioSystem.getClip();
		clip.open(AudioSystem.getAudioInputStream(new File("resources/sweden.WAV")));
		slimehurt = AudioSystem.getClip();
		slimehurt.open(AudioSystem.getAudioInputStream(new File("resources/slime.WAV")));
		//randomly create player spawn as long as it's a floorTile
		//please do this better
		do {
			rng1 = (int)(Math.random()*world.length);
			rng2 = (int)(Math.random()*world[0].length);
			if (world[rng1][rng2].equals(Grid.floorTile)) {
				isSpawn = false;
			} else {
				isSpawn = true;
			}
		} while (isSpawn);
		//same thing for the enemy though.
		do {
			rng1a = (int)(Math.random()*world.length);
			rng2a = (int)(Math.random()*world[0].length);
			if (world[rng1a][rng2a].equals(Grid.floorTile)) {
				isCSpawn = false;
			} else {
				isCSpawn = true;
			}
		} while(isCSpawn);
		do {
			rng1b = (int)(Math.random()*world.length);
			rng2b = (int)(Math.random()*world.length);
			if (world[rng1b][rng2b].equals(Grid.floorTile)) {
				isSSpawn = false;
			} else {
				isSSpawn = true;
			}
		} while(isSSpawn);
		//Type, x, y, width, height, speed. Need to find a way to modify x and y.
		player = new Player(CreatureType.PTest, rng2 * 64, rng1 * 64, 64, 64, 0, 500, 10);
		//These are both intended to create global instances of both entities so they can be modified and interact with each other
		//I'm retarded. All methods are readily available in the EnemyCreatures class, so just convert them to static and make the method one of the EnemyCreatures class
		test = new SlimeTest(CreatureType.CTest, rng2 * 64, rng1 * 64, 64, 64, 2, 200, 2);
		skele = new SkeleTest(CreatureType.STest, rng2 * 64, rng1 * 64, 64, 64, 2 , 200, 2);
		//This creates a new object of the grid class
		Grid grid = new Grid(world);
		//While the game is running, essentially.
		while(!Display.isCloseRequested()) {
			if (!clip.isRunning()) {
					clip.loop(clip.LOOP_CONTINUOUSLY);
			}
			//This shows the world on the screen. It needs to be called constantly because it needs to be constantly "drawn" so the objects appear to have permanence.
			grid.drawWorld();
			//This just works as ticks.
			Time.update();
			//This calculates turns
			determineTurn();
			//This constantly "draws" the enemy entity so it seems like it's permanently on
			if (alive) {
				EnemyCreatures.drawEnemy();
			}
			//This does the same as the .drawEnemy() method
			player.drawPlayer();
			//This calculates whatever the player is gonna do after like a key is pressed and whatever. Just look at the method in the Player class
			player.update();
			//I had to put this here because values and whatever. Whenever the player presses z and the enemy is within the radius, then the enemy takes damage.
			if(Keyboard.isKeyDown(Keyboard.KEY_Z)) {
				if  (player.getX() == test.getEnemyX() - 64 && player.getY() == test.getEnemyY() 
					|| player.getX() == test.getEnemyX() + 64 && player.getY() == test.getEnemyY() 
					|| player.getX() == test.getEnemyX() && player.getY() - 64 == test.getEnemyY() 
					|| player.getX() == test.getEnemyX() && player.getY() + 64 == test.getEnemyY()
					|| player.getX() == test.getEnemyX() + 64 && player.getY() == test.getEnemyY() + 64
					|| player.getX() == test.getEnemyX() - 64 && player.getY() == test.getEnemyY() + 64
					|| player.getX() == test.getEnemyX() + 64 && player.getY() == test.getEnemyY() - 64
					|| player.getX() == test.getEnemyX() - 64 && player.getY() == test.getEnemyY() - 64) {
					slimehurt.start();
					test.setEnemyHP(test.getEnemyHP() - player.getDamage());
					//once the enemy is dead, life is false. So, alive is false and that means that the slime won't update or draw anymore. Essentially removing it.
					if (test.getEnemyHP() <= 0)
					setLife(false);
				}
			}
			//This refreshes the screen constantly.
			Display.update();
			//This works as determining the behaviors and whatever for the enemy entity
			//Maybe modify the boolean to consider presence on the map as well? Just let them all update whenever.
			if (alive) {
				test.update();
				skele.update();
			}
			//Side note: Performance is actually a thing. Very noticeable if you can't run this lmao
			//This shows 60 frames per second
			Display.sync(60);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			//If escape is pressed, the display is destroyed, believe or not.
			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				Display.destroy();
			}
		}
		Display.destroy();
	}
	public void determineTurn() {
		//If the turn is not even, the player has his turn.
		if (player.getTurnCount() % 2 != 0) {
			player.setPlayerTurn(true);
			EnemyCreatures.setEnemyTurn(false);
		}
		else {
			player.setPlayerTurn(false);
			EnemyCreatures.setEnemyTurn(true);
		}
	}
	
	public static void main(String[] args) throws IOException, Exception {
		new Game();
	}
	//This are all mostly setters and getters
	public static void setLife(boolean alive) {
		Game.alive = alive;
	}
	public static boolean getLife() {
		return alive;
	}
	public static int getRadius() {
		return player.getRadius();
	}
	
	public static int getTurnCount() {
		return player.getTurnCount();
	}
	
	public static void setTurnCount() {
		player.setTurns(player.getTurnCount() + 1);
	}
	public static boolean getPlayerTurn() {
		return player.getPlayerTurn();
	}
	
	public static float getPlayerX() {
		return player.getX();
	}
	
	public static float getPlayerY() {
		return player.getY();
	}

	public static float getPlayerWidth() {
		return player.getWidth();
	}
	
	public static float getPlayerHeight() {
		return player.getHeight();
	}
	
	public static float getEnemyX() {
		return EnemyCreatures.getEnemyX();
	}
	
	public static float getEnemyY() {
		return EnemyCreatures.getEnemyY();
	}
	//This just uses an array to load all the data off the file.
	
	public static void loadWorld() throws IOException {
		FileInputStream fstream = new FileInputStream("worlds/floor");
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String str;
		world = new String[size][size];
		int index = 0;
		while ((str = br.readLine()) != null) {
			if (index >= size - 1) {
				System.out.println(" ");
				break;
			}
			world[index] = str.split("\\s+");
			index++;
		}
	}
}
