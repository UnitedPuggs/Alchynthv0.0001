package entities;

public class SkeleTest extends EnemyCreatures implements EnemyAI{

	public SkeleTest(CreatureType creature, float x, float y, float enemyWidth, float enemyHeight, float enemySpeed,
			float hp, float damage) {
		super(creature, x, y, enemyWidth, enemyHeight, 2, hp, damage);
	}
	public void update() {
		playerChecker();
		trackPlayer();
	}
}
