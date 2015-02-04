package Actors;

import java.awt.event.KeyEvent;

import Action.WalkAction;
import Enums.ActorID;
import Enums.Team;
import Main.Circle;
import Main.InputHandler;
import Main.Vector;
import Main.World;

public class NeutralActor extends Actor {

	public NeutralActor(ActorID id, Circle circle, int weight, World world) {
		super(id, circle, weight, world);
		team = Team.NEUTRAL;
	}

	@Override
	public void handleInputs(InputHandler input_handler) {
		if(input_handler.held_keys.contains(KeyEvent.VK_9)){
			action_queue.clearWalkActions();
		}
		
	}

	@Override
	public void chooseNextAction() {
		Vector target = new Vector(1,0);
		target = target.rotate((float)(Math.random()*Math.PI*2));
		target = target.multiply((float)Math.random()*250);
		target = target.add(center());
		
		action_queue.push(new WalkAction(this,world,target,600));
	}

}
