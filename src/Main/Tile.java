package Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;

import Actors.Actor;
import Actors.PlayerActor;
import Attacks.Attack;
import Enums.RenderStrategy;
import Enums.TileID;


public class Tile implements Iterable<WorldObject> {
	//type of tile (grass, stone, sand, etc)
	private TileID id;
	//grid coordinates
	private int x,y;
	//actual bound. All contained actors' coordinates should be within this rectangle.
	private Rectangle bounds;
	private Color rect_color;
	
	//array of all actors in tile. This array is of fixed length, which allows for concurrent
	//access to it by the renderer/updater.
	private WorldObject[] actor_array;
	//int of last actor in tile. if no actors are in array, last_index_used is set to zero. This way,
	//last_index_used will always be a valid index of the array, so no checks are needed.
	private int num_actors;
	private boolean change_occured = false;
	
	private boolean is_traversable = true;
	
	private World world;
	
	//max number of actors per tile. This number should be large enough that 
	//it is impossible to fit that many actors in the tile.  If more than this
	//number of actors need to be placed in a tile, behavior is unspecified. (e.g.
	//actors may be deleted.)
	public static final int ACTOR_ARRAY_SIZE = 64;
	
	public Tile(TileID id, World world, int x, int y, Rectangle bounds){
		this.id = id;
		rect_color = TileID.getIDColor(id).darker();
		this.is_traversable = id.isTraversable();
		this.x = x;
		this.y = y;
		this.bounds = bounds;
		this.world = world;
		
		actor_array = new WorldObject[ACTOR_ARRAY_SIZE];
		num_actors = 0;
	}
	
	public int x(){ return x;}
	public int y(){ return y;}
	public TileID id(){ return id;}
	
	/**
	 * Returns whether or not actors may walk across (move onto) this tile. For example, HOLE tiles
	 * may not be traversed by actors.
	 * @return
	 */
	public boolean isTraversable(){
		return is_traversable;
	}
	
	/**
	 * Adds actor to tile's actor list. Will return false only if tile is seemingly full. 
	 * IE, if it was full anytime after the last sorting. Does not alter any data within
	 * given actor, or any other tiles. So it is possible to have one actor in two different
	 * tiles, or an actor who thinks it is in a different tile than the one it's actually in.
	 * @param a Actor to be added
	 */
	public boolean addActor(WorldObject a){
	
		change_occured = true;
		if(num_actors >= ACTOR_ARRAY_SIZE )
			return false;
//		System.out.println("Tile - "+actor_array);
//		System.out.println("Tile - trying to add:"+a);
		actor_array[num_actors++] = (WorldObject)a;
		return true;
		
	}
	
	public boolean removeActor(WorldObject a){
		for(int i = 0; i < num_actors; i++){
			if(actor_array[i] == a){
				actor_array[i] = null;
				change_occured = true;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Method used by non-traversable tiles to clear out actors inside of them. With this method,
	 * Actors will not have to worry about colliding with tiles, and can move freely. Their mistakes
	 * will be corrected by this method.
	 */
//	private void pushActorsOut(){
//		if(num_actors == 0)
//			return;
//		
//		List<Tile> adj_tiles = world.getAdjacentTiles(this, false);
//		
//		//remove adjacent tiles which cannot be pushed to
//		for(int i = adj_tiles.size()-1; i >= 0; i--){
//			if(adj_tiles.get(i).isTraversable() == false){
//				adj_tiles.remove(i);
//			}
//		}
//		
//		if(adj_tiles.size() == 0)
//			return;
//		
//		for(Actor a : this){
//			if(a == null)
//				continue;
//			
////			Tile prev_tile = a.prev_traversable_tile;
////			if(prev_tile != null && prev_tile.isTraversable()){
////				pushActorToTile(a, prev_tile);
////			}
////			else{
//				Tile closest_tile = getClosestTileToActor(a, adj_tiles);
////				if(a instanceof PlayerActor)
////					System.out.println("Tile - pushing to: "+closest_tile.id());
//				pushActorToTile(a, closest_tile);
////			}
//		}
//	}
	
	/**
	 * Helper method for pushActorsOut. Given an Actor and a list of tiles, will determine 
	 * which tile is closest to the actor. In this instance, closeness means minimum distance from
	 * center of actor to center of tile.
	 * @param a Actor
	 * @param tiles List of tiles
	 * @return Tile closest to actor
	 */
	private Tile getClosestTileToActor(Actor a, List<Tile> tiles){
		if(tiles.size() == 0) 
			return null;
		
		float min_dist = Float.POSITIVE_INFINITY;
		Tile min_tile = null;
		for(Tile t : tiles){
//			System.out.println(a);
			float dist = a.center().distTo(t.getCenter());
			if(dist < min_dist){
				min_dist = dist;
				min_tile = t;
			}
		}
		return min_tile;
	}
	
	private void pushActorToTile(Actor a, Tile t){
		Vector center = a.center();
		Vector c00 = t.getCorner(0, 0);
		Vector c01 = t.getCorner(0, 1);
		Vector c10 = t.getCorner(1, 0);
		Vector c11 = t.getCorner(1, 1);
		
		if(c00.y() <= center.y() && center.y() <= c01.y()){
			//horizontal shift
			a.moveTo(center.distTo(c00) < center.distTo(c10)? c00.x()+1f : c10.x()-1f, center.y());
		}
		else if(c00.x() <= center.x() && center.x() <= c10.x()){
			//vertical shift
			a.moveTo(center.x(), center.distTo(c00) < center.distTo(c01)? c00.y()+1f : c01.y()-1f);
		}
		else{
			Vector closest_corner = center.closestTo(c00, c01, c10, c11);
			
			if(closest_corner.equals(c00)){
				closest_corner = closest_corner.add(new Vector(0.1f,0.1f));
			}
			else if(closest_corner.equals(c01)){
				closest_corner = closest_corner.add(new Vector(0.1f,-0.1f));
			}
			else if(closest_corner.equals(c10)){
				closest_corner = closest_corner.add(new Vector(-0.1f,0.1f));
			}
			else if(closest_corner.equals(c11)){
				closest_corner = closest_corner.add(new Vector(-0.1f,-0.1f));
			}
			
			a.moveTo(closest_corner);
		}	
	}
	
	public boolean contains(WorldObject a){
		for(int i = 0; i < num_actors; i++){
			if(actor_array[i] == a){
				return true;
			}
		}
		return false;
	}
	
	public void renderTile(Graphics g, int x_offset, int y_offset, RenderStrategy render_type){
		if(render_type == RenderStrategy.CARTESIAN_WIREFRAME){
			g.setColor(rect_color.brighter());
			g.fillRect(bounds.x - x_offset, bounds.y - y_offset, bounds.width-1, bounds.height-1);
			g.setColor(rect_color);
			g.drawRect(bounds.x - x_offset, bounds.y - y_offset, bounds.width-1, bounds.height-1);
		}
		else if(render_type == RenderStrategy.ISOMETRIC_WIREFRAME){
			Graphics2D g2 = (Graphics2D)g;
			g2.setColor(rect_color.brighter());
			g2.fillPolygon(getIsometricPolygon(x_offset, y_offset));
			g2.setColor(rect_color);
			g2.drawPolygon(getIsometricPolygon(x_offset, y_offset));
		}
		else if(render_type == RenderStrategy.ISOMETRIC_SPRITES || render_type == RenderStrategy.ISOMETRIC_SPRITES_WITH_HITBOXES){
			Vector v = getCorner(0,0).toIsometric().subtract(new Vector(0,32));
			g.drawImage(ImageHandler.getImage(id), (int)v.x() - x_offset, (int)v.y() - y_offset, 128, 64, null);
			
//			Graphics2D g2 = (Graphics2D)g;
//			g2.setColor(rect_color);
//			g2.drawPolygon(getIsometricPolygon(x_offset, y_offset));
		}
	}
	
	public void renderActors(Graphics g, int x_offset, int y_offset, RenderStrategy render_type){
		WorldObject temp;
		//snapshotting current actors, in case a sort occurs during rendering.
		int temp_num_actors = num_actors;
		WorldObject[] temp_array = actor_array;
		
		for(int i = 0; i < temp_num_actors; i++){
			temp = temp_array[i];
			if(temp != null){
				temp.render(g, x_offset, y_offset, render_type);
				
				//dealing with health bar rendering
				if(temp instanceof Actor){
					if(temp instanceof PlayerActor){
						if(RenderSettings.showPlayerHealth()){
							((Actor) temp).renderHealthBar(g, x_offset, y_offset, ((Actor)temp).getTeam(), render_type);
						}
					}
					else if(RenderSettings.showHealthBar(((Actor) temp).getTeam())){
						((Actor) temp).renderHealthBar(g, x_offset, y_offset, ((Actor)temp).getTeam(), render_type);
					}
				}
			}
		}
	}
	
//	public void update(InputHandler input_handler, float dt){
//		updateActors(input_handler, dt);
//		
//		if(!is_traversable){
//			pushActorsOut();
//		}
//	}
	
	public void updateSelf(InputHandler input_handler, float dt){
		if(change_occured || num_actors > 1){
			sortActors();
		}
	}
	
	public void updateActors(InputHandler input_handler, float dt){		
		WorldObject temp;
		for(int i = 0; i < num_actors; i++){
			temp = actor_array[i];
			if(temp != null){
				temp.update(input_handler, dt);
				
//				if(temp instanceof Attack){
//					System.out.println("Tile - "+this+" updating "+temp);
//				}
			}
		}
	}
	
	/**
	 * Sorts actors such that actors with lower y values come first. This way, higher actors will be rendered first.
	 */
	private void sortActors(){
		WorldObject[] new_array = new WorldObject[ACTOR_ARRAY_SIZE];
		int new_num_actors = 0;
		
		//putting non-null actors into new array. also setting value of new_last_index
		for(int i = 0; i < num_actors; i++){
			if(actor_array[i] != null){
				new_array[new_num_actors++] = actor_array[i];
			}
		}
		
		//insertion sort over the new array
		for(int i = 0; i < new_num_actors; i++){
			for(int j = i; j > 0; j--){
				if(compare(new_array[j], new_array[j-1]) > 0)
					break;
				else
					swap(new_array, j, j-1);
			}
		}
		
		//setting new values
		actor_array = new_array;
		num_actors = new_num_actors;
		change_occured = false;
	}
	
	private int compare(Circular a1, Circular a2){
		return (int)((-a2.y() + a2.x()) - (-a1.y() + a1.x()));
	}
	
	private void swap(WorldObject[] arr, int i, int j){
		WorldObject a = arr[i];
		arr[i] = arr[j];
		arr[j] = a;
	}
	
	/**
	 * Returns the cartesian vector cooresponding to the specified corner of the tile.
	 * @param x 0 for left corner, 1 for right
	 * @param y 0 for top corner, 1 for bottom
	 * @return vector to specified corner's location.
	 */
	public Vector getCorner(int x, int y){
		if( (x != 1 && x != 0) || (y != 1 && y != 0))
			throw new IllegalArgumentException("Corner specification is invalid.");
		
		int res_x = bounds.x;
		int res_y = bounds.y;
		
		if(x == 1)
			res_x += bounds.width;
		if(y == 1)
			res_y += bounds.height;
			
		return new Vector(res_x, res_y);
	}
	
	public Vector getCenter(){
		return getCorner(0,0).add((getCorner(1,1).subtract(getCorner(0,0))).multiply(0.5f));
	}
	
	public Polygon getIsometricPolygon(int x_offset, int y_offset){
		Vector offset = new Vector(x_offset, y_offset);
		Vector[] corners = new Vector[] {	getCorner(0,0).toIsometric().subtract(offset), 
											getCorner(0,1).toIsometric().subtract(offset), 
											getCorner(1,1).toIsometric().subtract(offset), 
											getCorner(1,0).toIsometric().subtract(offset)};
		
		Polygon res = new Polygon();
		for(int i = 0; i < 4; i++){
			res.addPoint((int)corners[i].x(), (int)corners[i].y());
		}
		
		return res;
	}
	
	public String toString(){
		return "Tile: id="+id+", ["+x+", "+y+"], rect="+bounds+", num_actors="+num_actors;
	}

	@Override
	public Iterator<WorldObject> iterator() {
		return new TileIterator(this);
	}
	
	private class TileIterator implements Iterator<WorldObject>{
		Tile t;
		int index;
		public TileIterator(Tile t){
			this.t = t;
			index = 0;
		}

		@Override
		public boolean hasNext() {
			return index < t.num_actors;
		}

		@Override
		public WorldObject next() {
			return t.actor_array[index++];
		}

		@Override
		public void remove() {
			//opperation not supported
		}
	}
}
