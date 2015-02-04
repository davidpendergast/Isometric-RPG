package Attacks;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import Actors.Actor;
import Enums.AttackAnimationID;
import Enums.AttackID;
import Enums.DamageType;
import Enums.RenderStrategy;
import Enums.Team;
import Main.Animation;
import Main.Circle;
import Main.Circular;
import Main.InputHandler;
import Main.Tile;
import Main.Vector;
import Main.World;
import Main.WorldObject;

public abstract class Attack implements WorldObject {
	
	protected Circle circle;
	/**
	 * attack removes self after this number of ticks if termination state is not reached.
	 */
	protected int tick_limit; 
	
	/**
	 * Time it takes for actor's attack cycle to end. 
	 */
	protected int cast_tick_time = 150;
	
	protected Tile tile;
	
	protected World world;
	protected Team target_team;
	
	protected Color draw_color = Color.RED;
	
	public boolean render_above_actors = true;
	
	public int clock = 0;
	
	protected Actor source;
	
	Set<Actor> hit_actors;
	
	protected AttackID id;
	Animation animation;
	
	private boolean terminating = false;
	
	protected boolean keep_moving_while_terminating = false;
	
	protected DamageType damage_type = DamageType.MELEE;
	protected int damage = 0;
	protected float burn_chance = 0;
	protected float poison_chance = 0;
	
	public Attack(Circle circle, World world, Team target_team, int tick_limit, AttackID id, boolean render_above, Actor source){
		this.damage = source.stats().getDamageValue(DamageType.MELEE);
		this.circle = circle;
		this.world = world;
		this.target_team = target_team;
		this.tick_limit = tick_limit;
		this.render_above_actors = render_above;
		
		this.id = id;
		this.animation = new Animation(id);
		this.source = source;
		this.hit_actors = new HashSet<Actor>();
		
		moveTo(circle.center());
	}
	
	/**
	 * Handling what happens when an actor is hit by this attack.
	 * @param a
	 */
	public abstract void hitActor(Actor a);
	public abstract void move(float dt);
	public abstract void onTermination();
	public abstract void beforeDeletion();
	
	public int damage() { return damage;}
	public int tickLimit(){	return tick_limit;}
	public int castTime(){ return cast_tick_time;}
	public Actor getSource(){return source;}
	public boolean isRenderedAbove(){ return render_above_actors;}
	public Tile getTile(){ return tile;}
	
	public float x(){ return circle.x();}
	public float y(){ return circle.y();}
	public float r(){ return circle.r();}
	public Vector center(){ return circle.center();}
	public float distTo(Circular c){ return c.center().subtract(center()).getMagnitude();}
	
	public boolean moveTo(Vector v){
		if(handleTileTransition(center(), v)){
			circle.moveTo(v);
			return true;
		}
		
		return false;
	}
	
	public boolean moveTo(float x, float y) {
		return moveTo(new Vector(x,y));
	}
	
	public boolean moveInDir(float dist, Vector v){	
		v = v.normalize();
		return moveTo(x()+dist*v.x(), y()+dist*v.y());
	}
	
	/**
	 * Method which updates the attack over time. Moving/expanding its hitbox, among other thing, are performed here.
	 * @param dt
	 */
	public void update(InputHandler input_handler, float dt){
		animation.update(1);
		
		if(!terminating || keep_moving_while_terminating)
			move(dt);
		
		if(!terminating){
			for(Actor a: getActorCollisions()){
				hitActor(a);
			}
		}
		
		if(clock > tick_limit){
			beforeDeletion();
			removeSelfFromWorld();
		}
		else{
			clock++;
		}
	}
	
	protected boolean handleTileTransition(Vector v0, Vector v1){
		Tile t0 = world.getTileAtCoords(v0);
		Tile t1 = world.getTileAtCoords(v1);
		
		if(t0 == t1){
			tile = t0;
			return true;
		}
		else{
			if(t1 != null){
				boolean successful_addition = t1.addActor(this);
				if(successful_addition){
					System.out.println("Attack: Setting value of tile to "+t1);
					tile = t1;
					if(t0 != null){
						t0.removeActor(this);
					}
				}
				return successful_addition;
			}
		}
		
		return false;
	}
	
	public Actor[] getActorCollisions(){
		return world.getWorldSearcher().getActorsInCircle(this);
	}
	
	public void render(Graphics g, int x_offset, int y_offset, RenderStrategy render_type){
		if(render_type == RenderStrategy.CARTESIAN_WIREFRAME){
			int cx = (int)circle.x();
			int cy = (int)circle.y();
			int cr = (int)circle.r();
			g.setColor(draw_color);
			g.drawOval(cx - cr - x_offset, cy - cr - y_offset, cr*2, cr*2);
		}
		else if(render_type == RenderStrategy.ISOMETRIC_WIREFRAME || render_type == RenderStrategy.ISOMETRIC_SPRITES_WITH_HITBOXES){
			circle.render(g, Color.RED, x_offset, y_offset, RenderStrategy.ISOMETRIC_SPRITES);
		}
		
		if(render_type == RenderStrategy.ISOMETRIC_SPRITES || render_type == RenderStrategy.ISOMETRIC_SPRITES_WITH_HITBOXES){
			Vector iso_v = center().toIsometric();
			BufferedImage img = animation.getImage();
			if(img == null)
				return;
			g.drawImage(img, (int)iso_v.x() - 16 - x_offset, (int)iso_v.y() - 64 - y_offset, 32, 64, null);
		}
	}
	
	public void removeSelfFromWorld(){
//		moveTo(this.center());
//		world.removeAttack(this);
		System.out.println("Attack: pre del tile="+tile);
		
		if(tile != null)
			tile.removeActor(this);
		
		System.out.println("Attack: postdel tile="+tile);
	}
	
	public void terminate(){
		onTermination();
		terminating = true;
		clock = tick_limit - Animation.STANDARD_TICKS_PER_FRAME*4 + 15;
		//after next animation cycle, the default animation will be invisible, so after
		//termination cycle, no image will be rendered for attack.
		animation.setDefaultAnimation(null);
		animation.playAnimation(AttackAnimationID.END_0, 1);
	}
	
	public void setRadius(float r){
		circle.setR(r);
	}
	
}
