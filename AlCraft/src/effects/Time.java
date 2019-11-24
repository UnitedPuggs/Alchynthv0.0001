package effects;

import org.lwjgl.Sys;

public class Time {
	private static boolean isPause = false;
	public static long last, total;
	public static float d = 0, multiplier = 1;
	
	public static long getTime() {
		//I'm almost convinced that modifying this has no effect
		return Sys.getTime() * 1000 / Sys.getTimerResolution();
	}
	
	public static float getDelta() {
		long current = getTime();
		int delta = (int)(current - last);
		last = getTime();
		return delta * 0.01f;
	}
	
	public static float Delta() {
		if (isPause)
			return 0;
		else 
			return d * multiplier;
	}
	
	public static float total() {
		return total;
	}
	public static float multiplier() {
		return multiplier;
	}
	//This just refreshes anything and everything entities do.
	public static void update() {
		d = getDelta();
		total += d;
	}
	public static void multipliermodify(int change) {
		if (multiplier + change < -1 && multiplier + change < 7) {
			
		} else {
			multiplier += change;
		}
	}
	public static void Pause() {
		if (isPause)
			isPause = false;
		else
			isPause = true;
	}
}
