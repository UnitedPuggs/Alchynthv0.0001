package entities;
import static effects.Time.*;

import org.lwjgl.opengl.Display;

import base.Game;
import base.Grid;
public class SlimeTest extends EnemyCreatures implements EnemyAI {
	//Conditionals
	private boolean isTurn;
	public SlimeTest(CreatureType creature, float x, float y, float enemyWidth, float enemyHeight, float enemySpeed, float damage, float hp) {
		super(creature, x, y, enemyWidth, enemyHeight, 2, damage, hp);
	}
	//These are what the slimey needs to constantly look for
	public void update() {
		playerChecker();
		trackPlayer();
	}
}