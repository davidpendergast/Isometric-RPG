package Action;
import Actors.Actor;
import Enums.ActionStatus;
import Main.World;


public abstract class Action {

	protected Actor actor;
	
	protected World world;
	
	protected ActionStatus status;
	
	int tick_limit;
	int clock = 0;
	
	public Action(Actor a, World w, int tick_limit){
		this.actor = a;
		this.world = w;
		this.status = ActionStatus.RUNNING;
		this.tick_limit = tick_limit;
	}
	
	public ActionStatus execute(float dt){
		clock++;
		if(tick_limit != -1 && clock > tick_limit){
			return ActionStatus.OUT_OF_TIME;
		}
		
		return status;
	}
	
	public ActionStatus getStatus(){
		return status;
	}
	
	

}
