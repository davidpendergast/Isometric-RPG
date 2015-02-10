package Action;

import Actors.Actor;
import Main.World;

/**
 * Actor will 'wander' around for the duration of the wander action. Actor will 
 * randomly walk to nearby positions, and idle for random periods of time.
 * Used for NPCs with no targets in range.
 * @author dpendergast
 *
 */
public class WanderAction extends Action {

	public WanderAction(Actor a, World w, int tick_limit) {
		super(a, w, tick_limit);
		// TODO Auto-generated constructor stub
	}

}
