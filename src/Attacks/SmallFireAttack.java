package Attacks;

import Actors.Actor;
import Enums.AttackID;
import Enums.Team;
import Main.Vector;

public class SmallFireAttack extends BasicProjectileAttack {

	public SmallFireAttack( Vector start_pos, Vector direction,
			Team target, Actor source) {
		super(AttackID.SMALL_FIRE_PROJECTILE, 8, start_pos, direction, target, source);
	
		cast_tick_time = 50;
	
	}
	
	public void onTermination(){
		FireAOEAttack end_explosion = new FireAOEAttack(center(), new Vector(0,0), target_team, source);
		end_explosion.tick_limit = BasicAOEAttack.AOE_TICK_LIMIT / 4;
		end_explosion.damage = damage/2;
		end_explosion.hit_actors = hit_actors;
		end_explosion.growth_rate = BasicAOEAttack.AOE_GROWTH_RATE*2;
		source.world.addAttack(end_explosion);
	}

}
