package Action;

import Actors.Actor;
import Enums.ActionStatus;
import Main.World;

public class IdleAction extends Action {

	public IdleAction(Actor a, World w, int tick_limit) {
		super(a, w, tick_limit);
	}
	
	public ActionStatus execute(float dt){
		if(clock >= tick_limit){
			status = ActionStatus.SUCCESS;
			return status;
		}
		else{
			return super.execute(dt);
		}
	}

}
