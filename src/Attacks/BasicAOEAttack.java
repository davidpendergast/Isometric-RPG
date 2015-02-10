package Attacks;

import java.awt.Color;
import java.awt.Graphics;

import Actors.Actor;
import Enums.AttackID;
import Enums.DamageType;
import Enums.RenderStrategy;
import Enums.Team;
import Main.Circle;
import Main.Vector;
import Main.World;

/**
 * Parent of area-of-effect attacks in the game. Is an attack which may grow or shrink, 
 * and doesn't remove itself after touching an actor.
 * @author dpendergast
 *
 */
public class BasicAOEAttack extends BasicProjectileAttack {
	
	static final int AOE_TICK_LIMIT = 300;
	static final float AOE_GROWTH_RATE = 0.5f;
	
	protected float growth_rate = 0;

	public BasicAOEAttack(AttackID id, float radius, float growth_rate, Vector start_pos, Vector direction, Team target, Actor source, boolean render_below) {
		super(id, radius, start_pos, direction, target, source);
		this.tick_limit = AOE_TICK_LIMIT;
		this.damage = 100;
		this.damage_type = DamageType.BURN;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void hitActor(Actor a) {
		if(!hit_actors.contains(a) && a.getTeam().isTeam(target_team)){
			a.stats().giveDamage(DamageType.MELEE, this, true);
		}
		
		hit_actors.add(a);
		
	}

	@Override
	public void move(float dt) {
		// TODO Auto-generated method stub
		
	}
	
	public void render(Graphics g, int x_offset, int y_offset, RenderStrategy render_type){
		circle.render(g, Color.RED, x_offset, y_offset, render_type);
	}

}
