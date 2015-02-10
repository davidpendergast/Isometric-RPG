package Action;
import Actors.Actor;
import Attacks.Attack;
import Attacks.BasicProjectileAttack;
import Enums.ActionStatus;
import Enums.AnimationID;
import Enums.Team;
import Main.World;

/**
 * An action which places an Attack object into the world. Holds attacking actor stationary
 * for the duration of the cast time of the attack.
 * @author dpendergast
 *
 */
public class AttackAction extends Action {

	Attack att;
	
	public AttackAction(Actor act, Attack att, World w) {
		super(act, w, att.castTime());	
		this.att = att;
	}

	@Override
	public ActionStatus execute(float dt) {
//		if(actor.currentAnimation() != AnimationID.ATT_0)
//			actor.playAnimation(AnimationID.ATT_0, 1);
		
		if(clock == 1){
//			if(actor.currentAnimation() != AnimationID.ATT_0)
			
			actor.playAnimation(AnimationID.ATT_0, 1);
			world.addAttack(att);
		}

		return super.execute(dt);
	}

}
