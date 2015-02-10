package Attacks;

import java.awt.Color;
import java.awt.Graphics;

import Actors.Actor;
import Enums.AttackID;
import Enums.DamageType;
import Enums.RenderStrategy;
import Enums.Team;
import Main.Circle;
import Main.InputHandler;
import Main.Vector;

public class BasicProjectileAttack extends Attack {

	static final int PROJECTILE_TICK_LIMIT = 300;
	Vector direction;
	float speed = 1f;
	
	public BasicProjectileAttack(AttackID id, float radius, Vector start_pos, Vector direction, Team target, Actor source) {
		super(new Circle(start_pos.x(), start_pos.y(), radius), source.world, target, PROJECTILE_TICK_LIMIT, id, true, source);
		if(source != null){
			damage = source.stats().getDamageValue(DamageType.RANGED);
			source.setNewDirection(start_pos.add(direction).x(), start_pos.add(direction).y());
		}
		draw_color = Color.BLUE;
		this.direction = direction.normalize();
		render_above_actors = false;
	}
	
	@Override
	public void hitActor(Actor a) {
		if(!hit_actors.contains(a) && a.getTeam().isTeam(target_team)){
			terminate();
			a.stats().giveDamage(damage_type, this, true);
//			a.stats().giveBurn(source.stats().burnDamage(), 5, burn_chance);
//			a.stats().givePoison(source.stats().poisonDamage(), 5, poison_chance);
		}
		
		hit_actors.add(a);
		
	}
	
	public void update(InputHandler input_handler, float dt){
		
		super.update(input_handler, dt);
		
		
	}

	@Override
	public void move(float dt) {
		Vector v0 = center();
		moveInDir(dt*speed,direction);
//		boolean tile_trans_successful = handleTileTransition(v0, center());
//		
//		if(!tile_trans_successful){
//			moveTo(v0);
//		}
	}

	@Override
	public void onTermination() {
		//do nothing
	}

	@Override
	public void beforeDeletion() {
		// TODO Auto-generated method stub
		
	}
	
//	public void render(Graphics g, int x_offset, int y_offset, RenderStrategy render_type){
//		circle.render(g, Color.red, x_offset, y_offset, render_type);
//		super.render(g, x_offset, y_offset, render_type);
//	}
}
