package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.Texture;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import base.Game;
import base.Grid;
import base.TileType;

import static effects.Graphics.*;
import static effects.Time.*;
public class Player {
	private Texture texture;
	private float x, y, playerWidth, playerHeight, playerSpeed, xo, yo, hp, damage;
	private CreatureType creature;
	private SlimeTest slime;
	//Conditionals
	private boolean start = true, isInDungeon = true, isSprinting, isTurn, inBattle; //Note because this is going to be conditional. Just disable player's ability to move when calculating anything else. Can't test effectively with Thread.sleep
	private int turns = 1, radius = 320;
	private String wallTile = Grid.wallTile;
	//Directional stuff
	private boolean up, down, left, right, enemyDirRight, enemyDirUp, enemyDirLeft, enemyDirDown;
	private Game game, player;
	private Clip clip;
	public Player(CreatureType creature, float x, float y, float playerWidth, float playerHeight, float playerSpeed, float hp, float damage) {
		this.x = x;
		this.y = y;
		this.hp = hp;
		this.damage = damage;
		this.xo = creature.getXO();
		this.yo = creature.getYO();
		this.playerWidth = playerWidth;
		this.playerHeight = playerHeight;
		this.playerSpeed = playerSpeed;
		this.texture = shortcut(creature.texName);
	}
	
	public void drawPlayer() {
		DrawObjectTexture(texture, x, y, playerWidth, playerHeight, xo, yo);
	}
	
	public void update() {
	checker();
	if (isInDungeon) { 
		if (Keyboard.isKeyDown(Keyboard.KEY_D) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			right = true;
			playerSpeed = 64;
			isMove(256 * 64, 0);
			turns += 1;
			right = false;
		}  
		if (Keyboard.isKeyDown(Keyboard.KEY_A) || Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			left = true;
			playerSpeed = 64;
			isMove(256 * 64, 0);
			turns += 1;
			left = false;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S) || Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			down = true;
			playerSpeed = 64;
			isMove(0, 256 * 64);
			turns += 1;
			down = false;
		} 
		if (Keyboard.isKeyDown(Keyboard.KEY_W) || Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			up = true;
			playerSpeed = 64;
			isMove(0, 256 * 64);
			turns += 1;
			up = false;
		} 
	} else {
		// TODO : Actual town movement
	}
}
	//This just checked to see if the world at a place in front of the player was solid. Unnecessary for anything else, but keeping it just in case.
	public void checker() {
		//This gets the slime pos and determines where the enemy is in relation to the player. This actually works. Very janky when not grid based.
		if (getX() == game.getEnemyX() - 64 && getY() == game.getEnemyY() && game.getLife() == true) {
			enemyDirRight = true;
		} else { 
			enemyDirRight = false;
		}
		if (getX() == game.getEnemyX() + 64 && getY() == game.getEnemyY() && game.getLife() == true) {
			enemyDirLeft = true;
		} else {
			enemyDirLeft = false;
		}
		if (getX() == game.getEnemyX() && getY() == game.getEnemyY() + 64 && game.getLife() == true) {
			enemyDirUp = true;
		} else {
			enemyDirUp = false;
		}
		if (getX() == game.getEnemyX() && getY() == game.getEnemyY() - 64 && game.getLife() == true) {
			enemyDirDown = true;
		} else { 
			enemyDirDown = false;
		}
	}
	
	public void isMove(int newX, int newY) {
	try {
			clip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			clip.open(AudioSystem.getAudioInputStream(new File("resources/walk2.WAV")));
		} catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
			e.printStackTrace();
	}
	if (isTurn) {
	if (right) { //The collision with entities works, but if you go at the right angle you can go through it.
	if (!game.world[(int)getY() / 64][((int)getX() / 64) + 1].equals(wallTile) && enemyDirRight == false) {
		clip.start();
		if (x != newX) {
			if (x < newX)
				x += playerSpeed;
			else
				x -= playerSpeed;
		}
	  }
	}
	if (left) {
	if (!game.world[(int)getY() / 64][((int)getX() - 1) / 64].equals(wallTile) && enemyDirLeft == false) {
		clip.start();
		if (x != newX) { 
			if (x > newX)
				x += playerSpeed;
			else
				x -= playerSpeed;
			}
		}
	}
	if (up) {
		if (!game.world[((int)getY() / 64) - 1][(int)getX() / 64].equals(wallTile) && enemyDirUp == false) {
		clip.start();
		if (y != newY) {
			if (y > newY)
				y += playerSpeed;
			else
				y -= playerSpeed;
			}
		}
	}
	if (down) { 
	if (!game.world[((int)getY() / 64) + 1][(int)getX() / 64].equals(wallTile ) && enemyDirDown == false) {
		clip.start();
		if (y != newY) {
			if (y < newY)
				y += playerSpeed;
			else
				y -= playerSpeed;
	   }
	  }		
	 }	
    }
   }
	
	public void isTurnMove() {
		if(start) 
			start = false;
		else
			x += Delta() * playerSpeed;
	}
	//Setters and getters
	public float getDamage() {
		return damage;
	}
	public void setDamage(float damage) {
		this.damage = damage;
	}
	public float getHP() {
		return hp;
	}
	public void setHP(float hp) {
		this.hp = hp;
	}
	public int getRadius() {
		return radius;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public int getTurnCount() {
		return turns;
	}
	
	public void setTurns(int turns) {
		this.turns = turns;
	}
	
	public boolean getBattleState() {
		return inBattle;
	}
	
	public void setBattleState(boolean inBattle) {
		this.inBattle = inBattle;
	}
	
	public boolean getPlayerTurn() {
		return isTurn;
	}
	
	public void setPlayerTurn(boolean isTurn) {
		this.isTurn = isTurn;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public float getX() {
		return x;
	}
	
	public void setX(float x) {
		this.x = x;
	}
	
	public float getY() {
		return y;
	}
	
	public void setY(float y) {
		this.y = y;
	}
	
	public float getWidth() {
		return playerWidth;
	}
	
	public void setWidth(float playerWidth) {
		this.playerWidth = playerWidth;
	}
	
	public float getHeight() {
		return playerHeight;
	}
	
	public void setHeight(float playerHeight) {
		this.playerHeight = playerHeight;
	}
	
	public CreatureType getCreature() {
		return creature;
	}
	
	public void setCreature(CreatureType creature) {
		this.creature = creature;
	}
}