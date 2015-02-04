package Actors;

import Action.Action;
import Enums.AITypes;
import Main.World;

/**
 * Class which handles the AI behavior of NPC actors.
 * @author dpendergast
 *
 */
public class ActorController {
	
	AITypes ai_type;
	Actor actor;
	
	//used for minion ai behavior
	float teleport_dist = 0;
	float too_close_to_master_dist = 0;
	float too_far_from_master_dist = 0;
	
	float see_target_dist = 0;
	float attack_target_range = 0;
	
	public ActorController(Actor actor, Actor master, AITypes ai_type){
		this.actor = actor;
		this.ai_type = ai_type;	
	}
	
	public Action[] getNextActions(){
		return null;
	}
	
	public void setAIType(AITypes ai){
		this.ai_type = ai;
	}

}
