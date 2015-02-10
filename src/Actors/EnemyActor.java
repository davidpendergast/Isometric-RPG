package Actors;
import java.awt.Color;
import java.awt.event.KeyEvent;

import Action.IdleAction;
import Action.WalkAction;
import Enums.ActorID;
import Enums.Team;
import Main.Circle;
import Main.InputHandler;
import Main.Vector;
import Main.World;


/**
 * Class of all enemies in the game.
 *  
 * @author dpendergast
 *
 */
public class EnemyActor extends Actor {

	public EnemyActor(ActorID id, Circle circle, int weight, World world) {
		super(id, circle, weight, world);
		draw_color = Color.RED.darker();
		move_speed = 0.25f;
		
		team = Team.BAD;
	}

	@Override
	public void handleInputs(InputHandler input_handler) {
		if(input_handler.held_keys.contains(KeyEvent.VK_0)){
			action_queue.clearWalkActions();
		}
	}

	@Override
	public void chooseNextAction() {
		
		Actor[] actors_in_range = world.getWorldSearcher().getActorsInCircle(new Circle(x(), y(), r() +10));
		//TODO add some behavior
	
		Vector target = new Vector(1,0);
		target = target.rotate((float)(Math.random()*Math.PI*2));
		target = target.multiply((float)Math.random()*250);
		target = target.add(center());
	
		action_queue.push(new WalkAction(this,world,target,600));
		action_queue.push(new IdleAction(this,world,(int)(Math.random()*300)));
		
	}
}
