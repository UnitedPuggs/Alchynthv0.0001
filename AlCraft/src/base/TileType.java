package base;

import base.*;

public enum TileType {
	//xo and yo are where on the sprite sheet. Think of it like cells.
	//Note for the sprites. Just put in the name of the texture, don't worry about the location because I have a lil shortcut method :)
	Test("spritestuff", 1, 0), Test2("stone_bricks", 0, 0);
	String texName;
	boolean isSolid;
	int xo, yo;
	TileType(String texName, int xo, int yo) {
		this.texName = texName;
		this.xo = xo;
		this.yo = yo;
	}
	
	public int tileXO() {
		return xo;
	}
	
	public int tileYO() {
		return yo;
	}
}
