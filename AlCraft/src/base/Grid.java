package base;
import static effects.Graphics.*;

import effects.Graphics;
import entities.Player;
public class Grid {
	public Tiles[][] world;
	private int tile[][];
	public Graphics gfx;
	public Player player;
	public int floorWidth, floorHeight, playerSpawnX, playerSpawnY;
	public static String wallTile = "1a", floorTile = "0a";
	public void setTile(int xC, int yC, TileType type) {
		world[xC][yC] = new Tiles(xC * 64, yC & 64, 64, 64, type);
	}
	
	public Tiles getTile(int xC, int yC) {
		return world[xC][yC];
	}
	//This assigns all the values to floor or wall or whatever.
	public Grid(String[][] genWorld) {
		world = new Tiles[Game.dungeon.getHeight()][Game.dungeon.getLength()];
		for (int i = 0; i < world.length; i++) {
			for (int r = 0; r < world[i].length; r++) {
				if (genWorld[r][i].equals(floorTile)) {
					world[i][r] = new Tiles (i * 64, r * 64, 64, 64, TileType.Test);
				}
				else if (genWorld[r][i].equals(wallTile)) {
					world[i][r] = new Tiles (i * 64, r * 64, 64, 64, TileType.Test2);
				}
			}
		}
	}
	//This draws the array based on the data and whatever the coordinates, width, height, texture, etc are.
	public void drawWorld() {
		for (int i = 0; i < world.length; i++) {
			for (int r = 0; r < world[i].length; r++) {
				Tiles tile = world[i][r];
				DrawObjectTexture(tile.getTexture(), tile.getX(), tile.getY(), tile.getWidth(), tile.getHeight(), tile.getXO(), tile.getYO());
			}
		}
	}
}