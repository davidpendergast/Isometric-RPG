package Actors;

import Enums.ActorID;
import Main.Circle;
import Main.World;

public class ActorFactory {
	
	public static Actor getNewActor(ActorID id, float x, float y, World w, int level){
		Actor result;
		
		switch(id){
		case BUNNY:
			result = new NeutralActor(id, new Circle(x,y,8), 5, w);
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
