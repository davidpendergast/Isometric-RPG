package Enums;

public enum EffectResult {
	
	GIVE_POISON, 		//gives a poison status with dmg and time length equal to actor's poison stats.
	GIVE_BURN, 			//gives a burn status with dmg and time length equal to actor's burn stats.
	
	REMOVE_POISON, 		
	REMOVE_BURN,
	REMOVE_ALL_STATUSES,
	
	GIVE_SMALL_HEAL,	//heals for 25% of actor's heal stat
	GIVE_MEDIUM_HEAL,	//heals for 50% of actor's heal stat
	GIVE_LARGE_HEAL,	//heals for 100% of actor's heal stat
	
	CAST_SMALL_FIRE_ATTACK,
	CAST_SMALL_BONE_ATTACK,
	CAST_SMALL_POISON_ATTACK,
	
	RECIEVE_BURN_DAMAGE,
	RECIEVE_POISON_DAMAGE,
}
