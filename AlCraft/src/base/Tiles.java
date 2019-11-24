package base;

import org.newdawn.slick.opengl.Texture;
import static effects.Graphics.*;
public class Tiles {
	private float x, y, width, height, xo, yo;
	private Texture texture;
	private TileType tile;
	public Tiles(float x, float y, float width, float height, TileType tile) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.texture = shortcut(tile.texName);
		//This returns the xo and yo places from the TileType enum, so it functions as the sprite sheet due to the stuff I mentioned in the Graphics class.
		this.xo = tile.tileXO();
		this.yo = tile.tileYO();
	}
	public void drawShortcut() {
		DrawObjectTexture(texture, x, y, width, height, xo, yo);
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
		return width;
	}
	public void setWidth(float width) {
		this.width = width;
	}
	public float getHeight() {
		return height;
	}
	public void setHeight(float height) {
		this.height = height;
	}
	public Texture getTexture() {
		return texture;
	}
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	public TileType getTile() {
		return tile;
	}
	public void setTile(TileType tile) {
		this.tile = tile;
	}
	public float getXO() {
		return xo;
	}
	public void setXO(float xo) {
		this.xo = xo;
	}
	public float getYO() {
		return yo;
	}
	public void setYO(float yo) {
		this.yo = yo;
	}
}
