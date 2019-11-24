package entities;

public interface EnemyAI {
	//This is honestly to use an interface. All enemies should implement it because they all needs to have some sort of basic behaviors.
	public void update();
	public void trackPlayer();
	public boolean determineAggro();
	public void pathFindUD();
	public void pathFindLR();
}
