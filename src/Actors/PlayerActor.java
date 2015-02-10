package Actors;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.MouseEvent;

import Action.AttackAction;
import Action.WalkAction;
import Attacks.BasicMeleeAttack;
import Attacks.BasicProjectileAttack;
import Attacks.FireAOEAttack;
import Attacks.SmallBoneAttack;
import Attacks.SmallFireAttack;
import Attacks.SmallPoisonAttack;
import Enums.ActorID;
import Enums.AttackID;
import Enums.Team;
import Main.Circle;
import Main.InputHandler;
import Main.KeyMappings;
import Main.Vector;
import Main.World;

/**
 * Class for the user-controlled actor of the game. Handles inputs and such.
 * @author dpendergast
 *
 */
public class PlayerActor extends Actor {

	public PlayerActor(float x, float y, World world) {
		super(ActorID.BUBBLE, new Circle(x,y,12), 5, world);
		draw_color = Color.WHITE;
		
		team = Team.GOOD;
		move_speed = 1;
		
	}
	
	public void update(InputHandler input_handler, float dt){
//		System.out.println("PlayerActor - "+currentAnimation());
		super.update(input_handler, dt);
		
//		if(prev_tile != null && tile != null)
//			System.out.println("PlayerActor - "+prev_tile.id()+", "+tile.id());
	}

	@Override
	public void handleInputs(InputHandler input_handler) {
//		Vector click = input_handler.last_click_in_world;
		
		boolean pressing_mouse = input_handler.dragging;
		boolean standing_still = input_handler.held_keys.contains(KeyMappings.stand_still);
		
		boolean attack_1 = input_handler.new_keys.contains(KeyMappings.attack_1);
		boolean attack_2 = input_handler.new_keys.contains(KeyMappings.attack_2);
		boolean attack_3 = input_handler.new_keys.contains(KeyMappings.attack_3);
		boolean attack_4 = input_handler.new_keys.contains(KeyMappings.attack_4);
		boolean attack_5 = input_handler.new_keys.contains(KeyMappings.attack_5);
		
		Vector mouse_location = input_handler.current_mouse_position_in_world;
		
		if(pressing_mouse && !standing_still){
			action_queue.clearWalkActions();
			action_queue.push(new WalkAction(this, world, input_handler.current_mouse_position_in_world,600));
		}
		
		if(standing_still){
			action_queue.clearWalkActions();
		}
		
		if(attack_1){
			action_queue.clearWalkActions();
			if(!(action_queue.peek() instanceof AttackAction)){
				action_queue.push(new AttackAction(	this, new SmallBoneAttack(center(), mouse_location.subtract(center()), Team.ANY_NOT_GOOD, this), world));
			}
		}
		else if(attack_2){
			action_queue.clearWalkActions();
			if(!(action_queue.peek() instanceof AttackAction)){
				action_queue.push(new AttackAction(	this, new SmallFireAttack(center(), mouse_location.subtract(center()), Team.ANY_NOT_GOOD, this), world));
			}
		}
		else if(attack_3){
			action_queue.clearWalkActions();
			if(!(action_queue.peek() instanceof AttackAction)){
				action_queue.push(new AttackAction(	this, new SmallPoisonAttack(center(), mouse_location.subtract(center()), Team.ANY_NOT_GOOD, this), world));
	//			action_queue.push(new AttackAction(	this, new FireAOEAttack(center(), mouse_location.subtract(center()), Team.BAD, this), world));
			}
		}
		else if(attack_4){
			action_queue.clearWalkActions();
			if(!(action_queue.peek() instanceof AttackAction)){
				action_queue.push(new AttackAction(	this, new BasicMeleeAttack(AttackID.BONE_SWIPE, 12, center(), mouse_location.subtract(center()), Team.ANY_NOT_GOOD, this), world));
	//			action_queue.push(new AttackAction(	this, new FireAOEAttack(center(), mouse_location.subtract(center()), Team.BAD, this), world));
			}
		}
		else if(attack_5){
			
		}
		
//		if(click != null){
//			action_queue.clearWalkActions();
//			action_queue.push(new WalkAction(this, world, click,60));
//		}
//		else{
//			//click is null. Look for a mouse being held down.
//			if(input_handler.dragging){
//				
//				click = input_handler.last_drag_in_world;
////				System.out.println("PlayerActor - dragging: "+click);
//				if(click != null){
//					action_queue.clearWalkActions();
//					action_queue.push(new WalkAction(this, world, click,600));
//				}
//			}
//		}
	}

	@Override
	public void chooseNextAction() {
		// TODO Auto-generated method stub
		
	}

}
