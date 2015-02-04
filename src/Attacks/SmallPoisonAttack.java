package Attacks;

import Actors.Actor;
import Enums.AttackID;
import Enums.Team;
import Main.Vector;

public class SmallPoisonAttack extends BasicProjectileAttack {

	public SmallPoisonAttack(Vector start_pos,
			Vector direction, Team target, Actor source) {
		super(AttackID.SMALL_POISON_PROJECTILE, 8, start_pos, direction, target, source);
		// TODO Auto-generated constructor stub
		cast_tick_time = 50;
	
	}

}
