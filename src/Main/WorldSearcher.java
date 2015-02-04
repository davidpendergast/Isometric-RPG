package Main;
import java.util.ArrayList;

import Actors.Actor;
import Enums.Team;


//i dont know if this is how i'd like to aproach this
public class WorldSearcher {
	
	private World world;
	private static float tile_size = World.TILE_SIZE;
	
	public WorldSearcher(World w){
		this.world = w;
	}
	
	public Actor[] getActorsInCircle(Circular c){
		Tile[] tiles = getTilesInCircle(c);
		ArrayList<Actor> actors = new ArrayList<Actor>();
		for(Tile t: tiles){
			if(t == null)
				continue;
			for(WorldObject a: t){
				if(a != null && a instanceof Actor && c.distTo(a) <= c.r() + a.r()){
					actors.add((Actor)a);
				}
			}
		}
		
		return actors.toArray(new Actor[0]);
	}
	
	/**
	 * Returns a rough list of tiles which given circle may contian. Not all tiles
	 * in list are guaranteed to intersect circle, but all intersecting tiles in
	 * world are guaranteed to be found by this method.
	 * @param c given circle
	 * @return list of tiles which circle contains or partially contains.
	 */
	public Tile[] getTilesInCircle(Circular c){
//		//expanding circle's radius by sqrt(2)*tile_size.
//		if(c.r() > 2*tile_size){
//			c = new Circle(c.x(), c.y(), c.r()*1.21f*tile_size);
//		}
		
		int x_start = (int)((c.x() - c.r())/tile_size);
		int x_finish= (int)((c.x() + c.r())/tile_size + 1);
		int y_start = (int)((c.y() - c.r())/tile_size);
		int y_finish= (int)((c.y() + c.r())/tile_size + 1);
		
		ArrayList<Tile> res = new ArrayList<Tile>();
		
		for(int x = x_start; x <= x_finish; x++){
			for(int y = y_start; y <= y_finish; y++){
				Tile t = world.getTile(x, y);
				if(t != null){
					res.add(t);
				}
			}
		}
		
		return res.toArray(new Tile[0]);
	}
	
//	public static void sortByDistanceFromVector(Circular[] c, ){
//		
//	}

}
