package Attacks;

import Actors.Actor;
import Enums.AttackID;
import Enums.DamageType;
import Enums.Team;
import Main.Circle;
import Main.Vector;
import Main.World;


/**
 * Parent of all melee attacks.  Is implemented in kind of a strange way right now. A
 * different aproach may be better.
 * @author dpendergast
 *
 */
public class BasicMeleeAttack extends Attack {

	final static int MELEE_TICK_LIMIT = 20;
	
	Actor victim = null;
	
	public BasicMeleeAttack(AttackID id, float radius, Vector start_pos, Vector direction, Team target, Actor source) {
		super(new Circle(start_pos, radius), source.world, target, MELEE_TICK_LIMIT, id, true, source);
		damage = 17;
		damage_type = DamageType.MELEE;
		cast_tick_time = 100;
		keep_moving_while_terminating = true;
		moveInDir(source.r(), direction);
	}

	@Override
	public void hitActor(Actor a) {
		if(victim == null && !hit_actors.contains(a) && a.getTeam().isTeam(target_team)){
			victim = a;
			terminate();
			hit_actors.add(a);
		}
	}

	@Override
	public void move(float dt) {
		if(victim != null){
			Vector v = victim.center();
			v = v.add(-r(),r());
			
			moveTo(v);
		}
		
	}

	@Override
	public void onTermination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void beforeDeletion() {
		if(victim != null){
			victim.stats().giveDamage(damage_type, this, true);
		}
		
	}

}
