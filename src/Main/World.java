package Main;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import Actors.Actor;
import Attacks.Attack;
import Enums.TileID;


public class World {
	
	private Tile[][] tiles;
	private int x_size;
	private int y_size;
	
	public static final int TILE_SIZE = 64;
	
	private AttackHandler attack_handler;
	
	private WorldSearcher world_searcher;
	
	public World(int x_size, int y_size){
		//if given sizes are invalid, just create a 5x5 world;
		if(x_size <= 0 || y_size <= 0){
			x_size = 5;
			y_size = 5;
		}
		
		this.x_size = x_size;
		this.y_size = y_size;
		this.tiles = new Tile[x_size][y_size];
		
		for(int x = 0; x < x_size; x++){
			for(int y = 0; y < y_size; y++){
//				tiles[x][y] = new Tile(TileID.getRandomID(), this, x, y, new Rectangle(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE));
				tiles[x][y] = new Tile(TileID.GRASS, this, x, y, new Rectangle(x*TILE_SIZE, y*TILE_SIZE, TILE_SIZE, TILE_SIZE));
			}
		}
		
		tiles[0][0] = new Tile(TileID.GRASS, this, 0,0, new Rectangle(0, 0, TILE_SIZE, TILE_SIZE));
		
		attack_handler = new AttackHandler(this);
		world_searcher = new WorldSearcher(this);
	}
	
	public int xSize(){ return x_size;}
	public int ySize(){ return y_size;}
	
	public Tile getTile(int x, int y){
		if(x >= 0 && y >= 0 && x < x_size && y < y_size){
			return tiles[x][y];
		}
		
		return null;
	}
	
	public WorldSearcher getWorldSearcher(){
		return world_searcher;
	}
	
	/**
	 * Returns the tile at given cartesian coordinates.
	 * @param x
	 * @param y
	 * @return
	 */
	public Tile getTileAtCoords(float x, float y){
		if(x < 0 || y < 0)
			return null;
		
		int x_index = (int)(x / TILE_SIZE);
		int y_index = (int)(y / TILE_SIZE);
		
		if(x_index >= 0 && y_index >= 0 && x_index < x_size && y_index < y_size){
			return tiles[x_index][y_index];
		}
		
		return null;
	}
	/**
	 * Returns the tile at given cartesian vector coordinates.
	 * @param v
	 * @return
	 */
	public Tile getTileAtCoords(Vector v){
		return getTileAtCoords(v.x(), v.y());
	}
	
	public void updateAll(InputHandler input_handler, float dt){
		Tile t;
		for(int x = 0; x < x_size; x++){
			for(int y = 0; y < y_size; y++){
				t = tiles[x][y];
				if(t != null){
					t.updateActors(input_handler, dt);
					t.updateSelf(input_handler, dt);
				}
				
			}
		}
		
		attack_handler.updateAttacks(input_handler, dt);
		
//		for(int x = 0; x < x_size; x++){
//			for(int y = 0; y < y_size; y++){
//				tiles[x][y].updateSelf(input_handler, dt);
//			}
//		}
	}
	
	public void solveAllCollisions(){
		for(int x = 0; x < x_size; x++){
			for(int y = 0; y < y_size; y++){
				solveCollisionsInTile(tiles[x][y]);
			}
		}
	}
	
	private void solveCollisionsInTile(Tile t){
		List<Tile> adj_tiles = getAdjacentTiles(t, true);
		adj_tiles.add(t);
		
		//for each actor in Tile t, solve collisions with all other actors in t, 
		//as well as all actors in adjacent tiles. Not very efficient, but most 
		//of the time tiles will have < 4 actors at any given time. Usually 0 or
		//1 actually.
		for(WorldObject a : t){
			if(a == null || !(a instanceof Actor)) continue;
			for(WorldObject b : t){
				if(b!= null && a != b && b instanceof Actor)
					solveCollision(a, b);
			}
			for(Tile adj : adj_tiles){
				for(WorldObject c : adj){
					if(c != null && c instanceof Actor)
					solveCollision(a, c);
				}
			}
		}	
	}
	
	private void solveCollision(Circular a1, Circular a2){
		
		Vector c1 = a1.center();
		Vector c2 = a2.center();
		
		float dist = c1.distTo(c2);
		float overlap = dist - a1.r() - a2.r();
		
		//if actors aren't colliding, just return
		if(overlap > 0)
			return;
		
		Vector axis = c2.subtract(c1);
		
		a2.moveInDir(-overlap/2, axis);
		a1.moveInDir(overlap/2, axis);
		
	}
	
	public List<Tile> getAdjacentTiles(Tile t, boolean include_diagonals){
		List<Tile> result = new ArrayList<Tile>();
		int x = t.x();
		int y = t.y();
		
		if(x > 0) 			result.add(tiles[x-1][y]);
		if(x < x_size-1) 	result.add(tiles[x+1][y]);
		if(y > 0) 			result.add(tiles[x][y-1]);
		if(y < y_size-1) 	result.add(tiles[x][y+1]);
		
		if(!include_diagonals) 
			return result;
		
		if(x > 0 && y > 0)					result.add(tiles[x-1][y-1]);
		if(x > 0 && y < y_size-1)			result.add(tiles[x-1][y+1]);
		if(x < x_size-1 && y > 0)			result.add(tiles[x+1][y-1]);
		if(x < x_size-1 && y < y_size-1)	result.add(tiles[x+1][y+1]);
		
		return result;
	}
	
	public boolean addAttack(Attack a){
		Tile t = getTileAtCoords(a.center());
		if(t != null){
			return t.addActor(a);
		}
		
		return false;
			
//		if(attack_handler.isFull())
//			attack_handler.sortArrays();
//		
//		return attack_handler.add(a);
	}
	
	public boolean removeAttack(Attack a){
		System.out.println("World - removing attack: "+a );
//		Tile t = getTileAtCoords(a.center());
//		System.out.println("Wordl - t = "+t);
		Tile t = a.getTile();
		if(t != null){
			return t.removeActor(a);
		}
		
		
		return false;
//		return attack_handler.remove(a);
	}
	
	public AttackHandler getAttackHandler(){
		return attack_handler;
	}
	
	

}
