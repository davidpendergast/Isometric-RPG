package Actors;

import Enums.ActorID;
import Main.Circle;
import Main.World;

/**
 * Class which generates certain enemy types based off their ActorIDs and levels. This 
 * class stores the data which differentiates the NPCs, including move speeds, sizes, 
 * and team orientation.
 * @author dpendergast
 *
 */
public class ActorFactory {
	
	public static Actor getNewActor(ActorID id, float x, float y, World w, int level){
		Actor result;
		
		switch(id){
		case BUNNY:
			result = new NeutralActor(Math.random()<0.25? ActorID.TURTLE : ActorID.BUNNY, new Circle(x,y,8), 5, w);
			result.stats().setLevel(level);
			result.setMoveSpeed(.4f);
			result.stats().setMaxHealth(15);
			return result;
		case SCARY:
			result = new EnemyActor(id, new Circle(x,y,12), 5, w);
			result.stats().setLevel(level);
			result.setMoveSpeed(.2f);
			return result;
		default:
			return null;
		}
	}

}
