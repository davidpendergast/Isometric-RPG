package Enums;

public enum EffectTarget {
	SELF, OTHER, ALL_MINIONS, RAND_MINION, CLOSEST_NEARBY_ENEMY, RAND_NEARBY_ENEMY, ALL_NEARBY_ENEMIES;
	
	public static float nearby_dist = 400f;
}
