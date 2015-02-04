package Attacks;

import Actors.Actor;
import Enums.AttackID;
import Enums.Team;
import Main.Vector;
import Main.World;

public class SmallBoneAttack extends BasicProjectileAttack{

	public SmallBoneAttack(Vector start_pos, Vector direction,
			Team target, Actor source) {
		super(AttackID.SMALL_BONE_PROJECTILE, 8, start_pos, direction, target, source);
		
		cast_tick_time = 25;
		
	}

}
