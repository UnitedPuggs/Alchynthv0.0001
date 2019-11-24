package entities;

import entities.*;

public enum CreatureType {
	PTest("spritestuff", true, 0, 0),CTest("spritestuff", false, 1, 1), STest("spritestuff", false, 0, 1);
	String texName;
	int xo, yo;
	boolean isPassive;
	CreatureType(String texName, boolean isPassive, int xo, int yo) {
		this.texName = texName;
		this.isPassive = isPassive;
		this.xo = xo;
		this.yo = yo;
	}
	public int getXO() {
		return xo;
	}
	public int getYO() {
		return yo;
	}
}
