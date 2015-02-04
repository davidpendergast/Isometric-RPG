package Action;
import Actors.Actor;
import Enums.ActionStatus;
import Enums.AnimationID;
import Main.Vector;
import Main.World;


public class WalkAction extends Action {

	Vector target;
	Vector prev_position;
	public static final float dist_threshold = 0.001f;
	
	public WalkAction(Actor a, World w, Vector target, int tick_limit) {
		super(a, w, tick_limit);
		this.target = target;
	}

	@Override
	public ActionStatus execute(float dt) {
			
		prev_position = actor.center();
		float dist = actor.center().distTo(target);
		
		//if actor is within one step from finish
		float move_dist = dt * actor.moveSpeed();
		if(dist <= move_dist){
			actor.moveTo(target);
			status = ActionStatus.SUCCESS;
		}
		else{
			actor.setNewDirection(target.x(), target.y());
			actor.moveInDir(move_dist, target.subtract(actor.center()));
			if(actor.currentAnimation() != AnimationID.WALK_0){
				actor.playAnimation(AnimationID.WALK_0, 6);
			}
			
			//checking to see if actor has failed to move
			//could probably use a better fail condition, if any. Once OOB collision is fixed,
			//this failure will almost never occur.
			if(actor.center().distTo(prev_position) == 0){
				status = ActionStatus.FAILURE;
			}
			else
				status = ActionStatus.RUNNING;
		}
		
		return super.execute(dt);
	}
	
	public boolean equals(Object o){
		if( o instanceof WalkAction)
			return true;
		
		return false;
	}

}
