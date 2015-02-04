package Actors;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import Action.Action;
import Enums.ActionStatus;
import Enums.ActorID;
import Enums.AnimationID;
import Enums.RenderStrategy;
import Enums.Team;
import Main.ActionQueue;
import Main.ActorStats;
import Main.Animation;
import Main.Circle;
import Main.Circular;
import Main.ImageHandler;
import Main.InputHandler;
import Main.Tile;
import Main.Vector;
import Main.World;
import Main.WorldObject;


public abstract class Actor implements WorldObject {
	
	//stores size and location data of the actor
	private Circle circle;
	
	//used for collision solving with other entities
	protected int weight;
	
	public World world;
	public Tile prev_traversable_tile;
	public Tile tile;
	
	public ActionQueue action_queue;
	
	protected float move_speed;
	protected Color draw_color;
	
	protected Animation animation;
	
	protected Team team;
	
	protected ActorStats actor_stats;
	
	public Actor(ActorID id, Circle circle, int weight, World world){
		this.circle = circle;
		this.weight = weight;
		this.world = world;
		
		action_queue = new ActionQueue();
		move_speed = 0.5f;
		draw_color = Color.GRAY;
		moveTo(circle.x(), circle.y());
		
		animation = new Animation(id);
		animation.incrementFrame((int)(Math.random()*2));
		team = Team.NEUTRAL;
		
		actor_stats = new ActorStats(this);
	}
	
	public float x() { return circle.x();}
	public float y() { return circle.y();}
	public float r() { return circle.r();}
	public Vector center() { return circle.center();}
	public float moveSpeed() { return move_speed;}
	public void setMoveSpeed(float ms){ move_speed = ms;}
	public float distTo(Circular c){ return c.center().subtract(center()).getMagnitude();}
	public Team getTeam(){ return team;}
	public Tile getTile(){ return tile;}
	
	public void render(Graphics g, int x_offset, int y_offset, RenderStrategy render_type){
		if(render_type == RenderStrategy.CARTESIAN_WIREFRAME){
			g.setColor(draw_color);
			g.fillOval((int)(x() - r()) - x_offset, (int)(y() - r()) - y_offset, (int)(2*r()), (int)(2*r()));
			g.setColor(draw_color.darker());
			g.drawOval((int)(x() - r()) - x_offset, (int)(y() - r()) - y_offset, (int)(2*r()), (int)(2*r()));
		}
		else if( render_type == RenderStrategy.ISOMETRIC_WIREFRAME){
			Vector iso_v = circle.center().toIsometric();
			int xr = (int)(circle.r()*1.4);
			int yr = (int)(circle.r()/1.4f);
			g.setColor(draw_color);
			g.fillOval((int)iso_v.x() - xr - x_offset, (int)iso_v.y() - yr - y_offset, xr*2, yr*2);
			g.setColor(draw_color.darker());
			g.drawOval((int)iso_v.x() - xr - x_offset, (int)iso_v.y() - yr - y_offset, xr*2, yr*2);
		}
		else if( render_type == RenderStrategy.ISOMETRIC_SPRITES || render_type == RenderStrategy.ISOMETRIC_SPRITES_WITH_HITBOXES)
		{
			//rendering_hitboxes
			if(render_type == RenderStrategy.ISOMETRIC_SPRITES_WITH_HITBOXES)
				circle.render(g, draw_color, x_offset, y_offset, render_type);
			
			Vector iso_v = circle.center().toIsometric();
			BufferedImage img = animation.getImage();
			if(img == null)
				return;
			
			g.drawImage(img, (int)iso_v.x() - 16 - x_offset, (int)iso_v.y() - 64 - y_offset + 4, 32, 64, null);
			
			
		}
	}
	
	public void renderHealthBar(Graphics g, int x_offset, int y_offset, Team team, RenderStrategy render_type){
		if( render_type == RenderStrategy.ISOMETRIC_SPRITES || render_type == RenderStrategy.ISOMETRIC_SPRITES_WITH_HITBOXES)
		{		
			float percent = actor_stats.percentHealthRemaining();
			if(percent == 1)
				return;
			
			Vector iso_v = circle.center().toIsometric();
			BufferedImage img_back = ImageHandler.getHealthBar(team, 1);
			BufferedImage img_front = ImageHandler.getHealthBar(team, 0);
		
			g.drawImage(img_back, (int)iso_v.x() - 16 - x_offset, (int)iso_v.y() - 64 - 32 - y_offset + 4, 32, 32, null);
			g.drawImage(img_front, (int)iso_v.x() - 16 - x_offset, (int)iso_v.y() - 64 - 32 - y_offset + 4, (int)(32*percent), 32, null);
		}
	}
	
	public void update(InputHandler input_handler, float dt){
		animation.update(1);
		handleInputs(input_handler);
		
		if(!actor_stats.isAlive()){
			tile.removeActor(this);
		}
		
		Action current_action = action_queue.peek();
		if(current_action != null){
			ActionStatus status = current_action.execute(dt);
			if(status == ActionStatus.SUCCESS || status == ActionStatus.FAILURE || status == ActionStatus.OUT_OF_TIME){
				action_queue.pop();
			}
		}
		else{
			//there is no current action, so choose another one.
			chooseNextAction();
		}
	}
	
	public boolean moveTo(float x, float y){
		
		boolean res = false;
		
		Tile new_x_tile = world.getTileAtCoords(x, y());
		if(new_x_tile != null){
			if(new_x_tile == tile){
				circle.moveTo(x,y());
				res = true;
			}
			else{
				if(new_x_tile.isTraversable() && new_x_tile.addActor(this)){
					if(tile != null)
						tile.removeActor(this);
					tile = new_x_tile;
					circle.moveTo(x, y());
					res = true;
				}
			}
		}
		
		Tile new_y_tile = world.getTileAtCoords(x(), y);
		if(new_y_tile != null){
			if(new_y_tile == tile){
				circle.moveTo(x(),y);
				res = true;
			}
			else{
				if(new_y_tile.isTraversable() && new_y_tile.addActor(this)){
					if(tile != null)
						tile.removeActor(this);
					tile = new_y_tile;
					circle.moveTo(x(), y);
					res = true;
				}
			}
		}
		
		return res;
		
//		if(new_tile == tile){
//			circle.setPosition(x,y);
//			return true;
//		}
		
//		//if actor is trying to move into a corner made by 3 non-traversable blocks, do nothing
//		if(isInvalidDiagonalMove(tile, new_tile) && !new_tile.isTraversable()){
//			System.out.println("Actor - isInvalid & new tile is non_traversable.");
//			return false;
//		}
		
//		if(tile != null && new_tile != null && !tile.isTraversable() && !new_tile.isTraversable()){
//			return false;
//		}
//			
//		if(!isInvalidDiagonalMove(tile, new_tile) && new_tile.addActor(this)){
//			if(tile != null && new_tile != null)
//				System.out.println("Actor - not invalid:"+tile.id()+" --> "+new_tile.id());
//			//if original tile was not null, remove from original tile
//			if(tile != null)
//				tile.removeActor(this);
//			
//			//and set tile to new tile, and finally move the actor
//			if(tile != null && tile.isTraversable())
//				prev_traversable_tile = tile;
//			
//			tile = new_tile;
//			circle.setPosition(x,y);
//			return true;
//		}
//		
//		return false;
		
	}
	
//	/**
//	 * If the actor is attempting to "jump" between two diagonal traversable tiles, when the other 
//	 * diagonal is consists of two non-traversable tiles, this method will return true.
//	 * @param cur_tile
//	 * @param new_tile
//	 * @return
//	 */
//	private boolean isInvalidDiagonalMove(Tile cur_tile, Tile new_tile){
//		
//		if(cur_tile == null || new_tile == null)
//			return false;
//		
//		int cx = cur_tile.x();
//		int cy = cur_tile.y();
//		int nx = new_tile.x();
//		int ny = new_tile.y();
//		
//		if(cx == nx + 1 && cy == ny + 1){
//			return 	(world.getTile(nx + 1, ny) == null || !world.getTile(nx + 1, ny).isTraversable())
//					&& (world.getTile(nx, ny + 1) == null || !world.getTile(nx, ny + 1).isTraversable());
//		}
//		else if(cx == nx - 1 && cy == ny + 1){
//			return 	(world.getTile(nx - 1, ny) == null || !world.getTile(nx - 1, ny).isTraversable())
//					&& (world.getTile(nx, ny + 1) == null || !world.getTile(nx, ny + 1).isTraversable());
//		}
//		else if(cx == nx + 1 && cy == ny - 1){
//			return 	(world.getTile(nx + 1, ny) == null || !world.getTile(nx + 1, ny).isTraversable())
//					&& (world.getTile(nx, ny - 1) == null || !world.getTile(nx, ny - 1).isTraversable());
//		}
//		else if(cx == nx - 1 && cy == ny - 1){
//			return 	(world.getTile(nx - 1, ny) == null || !world.getTile(nx - 1, ny).isTraversable())
//					&& (world.getTile(nx, ny - 1) == null || !world.getTile(nx, ny - 1).isTraversable());
//		}
//		
//		return false;
//	}
	
//	public boolean moveTo(float x, float y){
//		return moveTo(x, y, false);
//	}
	
	public void setNewDirection(float new_x, float new_y){
		
		Vector v = new Vector(new_x, new_y).subtract(center()).toIsometric();
		if(v.x() < 0)
			animation.setDirection(AnimationID.Direction.RIGHT);
		else if(v.x() > 0)
			animation.setDirection(AnimationID.Direction.LEFT);
		
	}
	
	public void playAnimation(AnimationID id, int num_cycles){
		animation.playAnimation(id, num_cycles);
	}
	
	public AnimationID currentAnimation(){
		return animation.getCurrentAnimationID();
	}
	
	/**
	 * Moves actor a specified distance in given direction.
	 * @param dist
	 * @param dir
	 * @return
	 */
	public boolean moveInDir(float dist, Vector dir){
		dir = dir.normalize().multiply(dist);
		Vector new_pos = center().add(dir);
		return moveTo(new_pos);
		
	}
	
//	public boolean moveInDir(float dist, Vector dir){
//		return moveInDir(dist, dir, false);
//	}
	
//	public boolean moveTo(Vector v, boolean change_dir){
//		return moveTo(v.x(), v.y(), change_dir);
//	}
	
	public boolean moveTo(Vector v){
		return moveTo(v.x(), v.y());
//		return moveTo(v, false);
	}
	
	/**
	 * Returns the ActorStats object of actor.
	 * @return
	 */
	public ActorStats stats(){
		return actor_stats;
	}
	
	public abstract void handleInputs(InputHandler input_handler);
	public abstract void chooseNextAction();

	
}
