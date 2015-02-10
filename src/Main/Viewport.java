package Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

import Actors.Actor;
import Enums.RenderStrategy;
import GUI.Hotbar;


public class Viewport {
	
	World world;
	Actor focus_actor;
	int cam_x, cam_y;
	int cam_w, cam_h;
	
	int cam_move_speed = 1;
	
	boolean snap_to_actor = false;
//	RenderStrategy render_strategy;
	
	public Viewport(World world, int cam_x, int cam_y, int cam_w, int cam_h){
		this.world = world;
		this.cam_x = cam_x;
		this.cam_y = cam_y;
		this.cam_w = cam_w;
		this.cam_h = cam_h;
	}
	
	public RenderStrategy getRenderStrategy(){
		return RenderSettings.getRenderStrategy();
	}
	
	public void setFocusActor(Actor a){
		focus_actor = a;
	}
	
	public void setActorSnap(boolean boo){
		snap_to_actor = boo;
	}
	
	public void render(Graphics g){
		//taking a snapshot of viewport variables during render cycle to avoid concurrency issues.
		int current_cam_x = cam_x;
		int current_cam_y = cam_y;
//		RenderStrategy current_render_strategy = getRenderStrategy();
		RenderStrategy current_render_strategy = RenderSettings.getRenderStrategy();
		
		//TODO don't need to render every tile in world
//		Vector v = new Vector(cam_x, cam_y).toIsometric();
//		int corner_tile_x = (int)(v.x() / World.TILE_SIZE);
//		int corner_tile_y = (int)(v.y() / World.TILE_SIZE);
		int x_min_index = 0;
		int y_min_index = 0;
//		int render_size = world.xSize();
		int render_size = 19;
		
		//RENDERING TILES
		
		for(int x = x_min_index; x < x_min_index + render_size; x++){
			for(int y = y_min_index; y < y_min_index + render_size; y++){
				Tile t = world.getTile( x, y);
				if(t != null)
					t.renderTile(g, current_cam_x, current_cam_y, current_render_strategy);
			}
		}
		
//		world.getTile( 0,0).renderTile(g, current_cam_x, current_cam_y, current_render_strategy);
//		world.getTile(1,0).renderTile(g, current_cam_x, current_cam_y, current_render_strategy);
//		
//		for(int i = 0; i < render_size; i++){
//			for(int j = i; j >= 0; j--){
//				Tile t = world.getTile( j, i - j);
//				if(t != null)
//					t.renderTile(g, current_cam_x, current_cam_y, current_render_strategy);
//			}
//		}	
//		for(int i = 0; i < render_size; i++){
//			for(int j = i; j >= 0; j--){
//				Tile t = world.getTile(i - j , j);
//				if(t != null)
//					t.renderTile(g, current_cam_x, current_cam_y, current_render_strategy);
//			}
//		}
		
		//rendering low attacks
		world.getAttackHandler().renderBelowAttacks(g, current_cam_x, current_cam_y, current_render_strategy);
		
		//rendering actors
//		for(int i = 0; i < render_size; i++){
//			for(int j = 0; j <= i; j++){
		for(int i = x_min_index; i < x_min_index + render_size; i++){
			for(int j = y_min_index; j <= y_min_index + i - x_min_index; j++){
				Tile t = world.getTile( render_size-1-i + j, j);
				if(t != null)
					t.renderActors(g, current_cam_x, current_cam_y, current_render_strategy);
			}
		}
//		for(int i = 0; i < render_size; i++){
//			for(int j = 0; j < render_size - i; j++){
		for(int i = x_min_index; i < x_min_index + render_size; i++){
			for(int j = y_min_index; j < y_min_index + render_size - (i - x_min_index); j++){
//				System.out.println("Viewport - "+i+", "+(i+j));
				Tile t = world.getTile( j, i + j);
				if(t != null)
					t.renderActors(g, current_cam_x, current_cam_y, current_render_strategy);
			}
		}
//		for(int x = x_min_index; x < render_size; x++){
//			for(int y = y_min_index; y < render_size; y++){
//				world.getTile(x, y).renderActors(g, current_cam_x, current_cam_y, current_render_strategy);
//			}
//		}
		
		//rendering high attacks
		world.getAttackHandler().renderAboveAttacks(g, current_cam_x, current_cam_y, current_render_strategy);
	
		Hotbar h = new Hotbar(focus_actor);
		h.render(g, 960/2 - h.width()/2, 640 - h.height());
	}
	
	/**
	 * Given a position on the screen, returns the coordinates of the associated world position.
	 * @param screen_coords
	 * @return
	 */
	public Vector getWorldCoordinates(Vector screen_coords){
		Vector res = screen_coords.add(cam_x, cam_y);
		if(getRenderStrategy() == RenderStrategy.CARTESIAN_WIREFRAME){
			return res;
		}
		else{
			res = res.toCartesian();
			return res;
		}
	}
	
	public void update(InputHandler input_handler){
		
		//input for toggling rendering mode
		if(input_handler.new_keys.contains(KeyMappings.change_render_strat)){
//			render_strategy++;
//			render_strategy %= render_strategies.length;
			RenderSettings.incrementRenderStrategy();
		}
		
		if(input_handler.new_keys.contains(KeyMappings.toggle_enemy_health)){
			RenderSettings.toggleShowEnemyHealth();
		}
		if(input_handler.new_keys.contains(KeyMappings.toggle_friendly_health)){
			RenderSettings.toggleShowFriendlyHealth();
		}
		if(input_handler.new_keys.contains(KeyMappings.toggle_player_health)){
			RenderSettings.toggleShowPlayerHealth();
		}
		
		//input for toggling camera lock on player
		if(input_handler.new_keys.contains(KeyMappings.lock_on_character)){
			setActorSnap(!snap_to_actor);
		}
		
		//adjusting camera position
		if(snap_to_actor && focus_actor != null){
			if(getRenderStrategy() == RenderStrategy.CARTESIAN_WIREFRAME){
				cam_x = (int)(focus_actor.x() - cam_w/2);
				cam_y = (int)(focus_actor.y() - cam_h/2);
			}
			else{
				Vector v = focus_actor.center();
				v = v.toIsometric();
				cam_x = (int)(v.x() - cam_w/2);
				cam_y = (int)(v.y() - cam_h/2);
			}
		}
		else{
			if(input_handler.held_keys.contains(KeyMappings.cam_move_up)){
				cam_y -= cam_move_speed;
			}
			if(input_handler.held_keys.contains(KeyMappings.cam_move_right)){
				cam_x += cam_move_speed;
			}
			if(input_handler.held_keys.contains(KeyMappings.cam_move_left)){
				cam_x -= cam_move_speed;
			}
			if(input_handler.held_keys.contains(KeyMappings.cam_move_down)){
				cam_y += cam_move_speed;
			}
		}
		
		if(input_handler.new_keys.contains(KeyMappings.take_screenshot)){
			saveScreenShot();
		}
		
		updateWorldMouseEvents(input_handler);
	}
	
	private void updateWorldMouseEvents(InputHandler input_handler){
		MouseEvent last_click = input_handler.last_click;
		MouseEvent last_move  = input_handler.last_move;
		MouseEvent last_drag  = input_handler.last_drag;
		
		input_handler.last_click_in_world = toWorldVector(last_click);
		input_handler.last_move_in_world  = toWorldVector(last_move);
//		if(input_handler.last_move_in_world != null)
//			System.out.println("Viewport - last_move_in_world != null");
		if(last_drag != null)
			input_handler.last_drag_in_world = toWorldVector(last_drag);
		
		input_handler.current_mouse_position_in_world = getWorldCoordinates(input_handler.current_mouse_position);
	}
	
	private Vector toWorldVector(MouseEvent me){
		if(me == null)
			return null;
		
		Point p = me.getPoint();
		return getWorldCoordinates(new Vector(p.x, p.y));
	}
	
	private Vector toWorldVector(Point p){
		return getWorldCoordinates(new Vector(p.x, p.y));
	}
	
	public void saveScreenShot(){
		BufferedImage img = new BufferedImage(cam_w, cam_h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		render(g);
		
		try {
			File screen_shots_folder = new File("screenshots/");
			File[] list = screen_shots_folder.listFiles();
			if(list == null){
					throw new FileNotFoundException("Could not find screenshots folder.");
			}
			int num_files = list.length;
			
			String new_img_name = "screenshot_"+num_files;
			File output_file = new File("screenshots/"+new_img_name+".png");
			ImageIO.write(img, "png", output_file);
		} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
			
		
		
	}

}
