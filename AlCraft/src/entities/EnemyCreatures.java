package entities;

import org.newdawn.slick.opengl.Texture;
import static effects.Graphics.*;
import base.Game;
import base.Grid;

public abstract class EnemyCreatures implements EnemyAI {
	private static Texture texture;
	protected static float x;
	protected static float y;
	private static float enemyWidth;
	private static float enemyHeight;
	protected float enemySpeed;
	private static float xo;
	private static float yo;
	private float hp, damage;
	private CreatureType creature;
	public static boolean isTurn;
	public boolean enemyDown, enemyUp, enemyLeft, enemyRight;
	private boolean playerDirUp, playerDirDown, playerDirLeft, playerDirRight, playerDirDiagRight, playerDirDiagLeft, playerDirDiagDLeft, playerDirDiagDRight;
	private Game game, player;
	private String wallTile = Grid.wallTile;
	public EnemyCreatures(CreatureType creature, float x, float y, float enemyWidth, float enemyHeight, float enemySpeed, float hp, float damage) { 
		this.x = x;
		this.y = y;
		this.hp = hp;
		this.damage = damage;
		this.xo = creature.getXO(); 
		this.yo = creature.getYO();
		this.enemyWidth = enemyWidth;
		this.enemyHeight = enemyHeight;
		this.enemySpeed = enemySpeed;
		this.texture = shortcut(creature.texName);
	}
	public static void drawEnemy() {
		DrawObjectTexture(texture, x, y, enemyWidth, enemyHeight, xo, yo);
	}
	public void pathFindLR() {
		if (!game.world[(int)(getEnemyY()) / 64][(int)(getEnemyX() - (getEnemyX() - player.getPlayerX())) / 64].equals(Grid.wallTile)) {
			enemyRight = true;
			move();
			player.setTurnCount();
			enemyRight = false;
		} else if (!game.world[(int)getEnemyY() / 64][(int)(getEnemyX() + (player.getPlayerX() - getEnemyX())) / 64].equals(Grid.wallTile)) { 
			enemyLeft = true;
			move();
			player.setTurnCount();
			enemyLeft = false;
		}
	}
	public void pathFindUD() {
		//do math the find out which direction would be best
		if (!game.world[(int)(getEnemyY() - (player.getPlayerY() - getEnemyY())) / 64][(int)getEnemyX() / 64].equals(Grid.wallTile)) {
			enemyDown = true;
			move();
			player.setTurnCount();
			enemyDown = false;
		} else if (!game.world[(int)(getEnemyY() + (getEnemyY() - player.getPlayerY())) / 64][(int)getEnemyX() / 64].equals(Grid.wallTile)){
			enemyUp = true;
			move();
			player.setTurnCount();
			enemyDown = false;
		}
	}
	public void trackPlayer() {
		//This only happens when the player does not have a turn.
				//check left, right, up, down, up and left, up and right, down and left, down and right
				if (player.getPlayerTurn() == false && determineAggro() == true) {
					//if the player is the not within a certain pixel distance of the player, it won't start tracking
					//The slime's turn is true.
					isTurn = true;
					//If the player is going left, do calculations and y is the same
					if (player.getPlayerX() < getEnemyX()) {
						//If the player's x is left of the enemy's x, then it moves left.
						//Try to manipulate movement based on both x and y position, similarly to how player detection works.
						enemyLeft = true;
						move();
						player.setTurnCount();
						enemyLeft = false;
						}
					//IF the player is going right and y is the same
					if (player.getPlayerX() > getEnemyX()) {
						//If the player's x is right of the enemy's x, then it moves right
						enemyRight = true;
						move();
						player.setTurnCount();
						enemyRight = false;
						}
					//If the player is going up and x is the same
					if (player.getPlayerY() < getEnemyY()) {
						//If the player's y is above the enemy's y, then it moves up.
						enemyUp = true;
						move();
						player.setTurnCount();
						enemyUp = false;
					}
					//If the player is going down and x is the same
					if (player.getPlayerY() > getEnemyY()) {
						//If the player's y is below the enemy's y, then it moves down.
						enemyDown = true;
						move();
						player.setTurnCount();
						enemyDown = false;
					}
					if (player.getPlayerX() < getEnemyX() && player.getPlayerY() == getEnemyY() && !checkLeft()) {
						pathFindUD();
						enemyLeft = true;
						move();
						player.setTurnCount();
						enemyLeft = false;
					}
					if (player.getPlayerX() > getEnemyX() && player.getPlayerY() == getEnemyY() && !checkRight()) {
						pathFindUD();
						enemyRight = true;
						move();
						player.setTurnCount();
						enemyRight = false;
					}
					if (player.getPlayerY() < getEnemyY() && player.getPlayerX() == getEnemyX() && !checkUp()) {
						pathFindLR();
						enemyUp = true;
						move();
						player.setTurnCount();
						enemyUp = false;
					}
					if (player.getPlayerY() > getEnemyY() && player.getPlayerX() == getEnemyX() && !checkDown()) {
						pathFindLR();
						enemyDown = true;
						move();
						player.setTurnCount();
						enemyDown = false;
					}
					//Once everything is done and calculated, then the turn is set to false and the player can do whatever it needs to on it's turn.
					isTurn = false;
				}
	}
	public void move() {
		if (enemyUp || enemyDown) {
			enemySpeed = 64;
			isMove(0, 256 * 64);
		}
		if (enemyRight || enemyLeft) {
			enemySpeed = 64;
			isMove(256 * 64, 0);
		}
	}
	public void isMove(int newX, int newY) {
		//Whenever the slime has a turn
		if(isTurn) {
			if (enemyRight) {
				if(checkRight() && playerDirRight == false && playerDirDiagRight == false && playerDirDiagDRight == false) {
					if (x != newX) {
						if (x < newX)
							x += enemySpeed;
						else
							x -= enemySpeed;
					}
				}
			}
			if (enemyLeft) {
				if(checkLeft() && playerDirLeft == false && playerDirDiagLeft == false && playerDirDiagDLeft == false) {
					if (x != newX) {
						if (x > newX)
							x += enemySpeed;
						else
							x -= enemySpeed;
					}
				}
			}
			if (enemyUp) {
				if(checkUp() && playerDirUp == false && playerDirDiagRight == false && playerDirDiagLeft == false) {
					if (y != newY) {
						if (y > newY)
							y += enemySpeed;
						else
							y -= enemySpeed;
					}
				}
			}
			if (enemyDown) {
				if(checkDown() && playerDirDown == false && playerDirDiagDLeft == false && playerDirDiagDRight == false) {
					if (y != newY) {
						if (y < newY)
							y += enemySpeed;
						else
							y -= enemySpeed;
					}
				}
			}
		}
	}
	public boolean determineAggro() {
		if ((getEnemyX() <= player.getPlayerX() - player.getRadius() && getEnemyY() <= player.getPlayerY() 
				|| getEnemyX() <= player.getPlayerX() + player.getRadius() && getEnemyY() <= player.getPlayerY() 
				|| getEnemyX() <= player.getPlayerX() && getEnemyY() <= player.getPlayerY() + player.getRadius() 
				|| getEnemyX() <= player.getPlayerX() && getEnemyY() <= player.getPlayerY() - player.getRadius() 
				|| getEnemyX() <= player.getPlayerX() + player.getRadius() && getEnemyY() <= player.getPlayerY() - player.getRadius()
				|| getEnemyX() <= player.getPlayerX() - player.getRadius() && getEnemyY() <= player.getPlayerY() - player.getRadius()
				|| getEnemyX() <= player.getPlayerX() + player.getRadius() && getEnemyY() <= player.getPlayerY() + player.getRadius() 
				|| getEnemyX() <= player.getPlayerX() - player.getRadius() && getEnemyY() <= player.getPlayerY() + player.getRadius()))
			return true;
		else 
			return false;
	}
	public void playerChecker() {
		//This is like a pseudo-hitbox for players
		if (getEnemyX() == player.getPlayerX() - 64 && getEnemyY() == player.getPlayerY())
			//If the enemy's x coord is exactly 64 pixels left of the enemy and the enemy has the same y, then it signals that a player is there
			playerDirRight = true;
		else
			playerDirRight = false;
		if (getEnemyX() == player.getPlayerX() + 64 && getEnemyY() == player.getPlayerY())
			//Same as above, but 64 to the right
			playerDirLeft = true;
		else
			playerDirLeft = false;
		if (getEnemyX() == player.getPlayerX() && getEnemyY() == player.getPlayerY() + 64)
			//Same as above, but 64 above
			playerDirUp = true;
		else 
			playerDirUp = false;
		if (getEnemyX() == player.getPlayerX() && getEnemyY() == player.getPlayerY() - 64)
			//Same as above, but 64 below
			playerDirDown = true;
		else
			playerDirDown = false;
		//up and right
		if (getEnemyX() == player.getPlayerX() + 64 && getEnemyY() == player.getPlayerY() + 64)
			playerDirDiagRight = true;
		else
			playerDirDiagRight = false;
		//left and up
		if (getEnemyX() == player.getPlayerX() - 64 && getEnemyY() == player.getPlayerY() + 64)
			playerDirDiagLeft = true;
		else 
			playerDirDiagLeft = false;
		//left and down
		if (getEnemyX() == player.getPlayerX() - 64 && getEnemyY() == player.getPlayerY() - 64)
			playerDirDiagDLeft = true;
		else
			playerDirDiagDLeft = false;
		//right and down
		if (getEnemyX() == player.getPlayerX() + 64 && getEnemyY() == player.getPlayerY() - 64)
			playerDirDiagDRight = true;
		else
			playerDirDiagDRight = false;
	}
	public boolean checkLeft() {
		if(!game.world[(int)getEnemyY() / 64][((int)getEnemyX() - 1) / 64].equals(wallTile))
			return true;
		else 
			return false;
	}
	public boolean checkRight() {
		if(!game.world[(int)getEnemyY() / 64][((int)getEnemyX() / 64) + 1].equals(wallTile))
			return true;
		else
			return false;
	}
	public  boolean checkUp() {
		if(!game.world[((int)getEnemyY() / 64) - 1][(int)getEnemyX() / 64].equals(wallTile))
			return true;
		else
			return false;
	}
	public boolean checkDown() {
		if(!game.world[((int)getEnemyY() / 64) + 1][(int)getEnemyX() / 64].equals(wallTile))
			return true;
		else 
			return false;
	}
	public boolean getEnemyTurn() {
		return isTurn;
	}
	
	public static void setEnemyTurn(boolean isTurn) {
		EnemyCreatures.isTurn = isTurn;
	}
	public float getEnemyDamage() {
		return damage;
	}
	public void setEnemyDamage(float damage) {
		this.damage = damage;
	}
	public static float getEnemyX() {
		return x;
	}
	public static float getEnemyY() {
		return y;
	}
	public float getEnemyHP() {
		return hp;
	}
	public void setEnemyHP(float hp) { 
		this.hp = hp;
	}
	public abstract void update();
}
