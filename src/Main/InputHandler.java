package Main;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;

public class InputHandler extends MouseAdapter implements KeyListener {
		
	public HashSet<Integer> held_keys;
	public HashSet<Integer> new_keys;
	
	public ArrayList<MouseEvent> clicks;
	public Vector last_click_in_world;
	public MouseEvent last_click;
	public MouseEvent last_click_no_reset;
	public ArrayList<MouseEvent> moves;
	public Vector last_move_in_world;
	public MouseEvent last_move;
	public ArrayList<MouseEvent> drags;
	public Vector last_drag_in_world;
	public MouseEvent last_drag;
	public MouseEvent last_drag_no_reset;
	
	public Vector current_mouse_position;
	public Vector current_mouse_position_in_world;
	
	public int drag_x = 0;
	public int drag_y = 0;
	
	public boolean dragging = false;
	
	public InputHandler(){
		held_keys = new HashSet<Integer>();
		new_keys = new HashSet<Integer>();
		
		clicks = new ArrayList<MouseEvent>();
		moves = new ArrayList<MouseEvent>();
		drags = new ArrayList<MouseEvent>();
		
		last_click = null;
		last_click_no_reset = null;
		last_move = null;
		last_drag = null;
		last_drag_no_reset = null;
		
		current_mouse_position = new Vector(0,0);
		current_mouse_position_in_world = new Vector(0,0);
	}
	
	public void reset(){
		clicks.clear();
		moves.clear();
		drags.clear();
		
		new_keys.clear();
		
		last_click = null;
		last_move = null;
		last_drag = null;
		
		drag_x = 0;
		drag_y = 0;
	}
	
	public void mousePressed(MouseEvent e){
		super.mousePressed(e);
		clicks.add(e);
		last_click = e;
		last_click_no_reset = e;
		dragging = true;
	}
	
	public void mouseMoved(MouseEvent e){
		super.mouseMoved(e);
		moves.add(e);
		last_move = e;
		current_mouse_position = new Vector(e.getX(), e.getY());
	}
	
	public void mouseDragged(MouseEvent e){
		super.mouseMoved(e);
		drags.add(e);
		last_drag = e;
		
		if(last_drag_no_reset == null){
			drag_x = e.getX() - last_click_no_reset.getX();
		}
		else{
			drag_x = e.getX() - last_drag_no_reset.getX();
		}
		
		last_drag_no_reset = e;
		current_mouse_position = new Vector(e.getX(), e.getY());
	}
	
	public void mouseReleased(MouseEvent e){
		dragging = false;
		last_drag_no_reset = null;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(!held_keys.contains(e.getKeyCode())){
			new_keys.add(e.getKeyCode());
			held_keys.add(e.getKeyCode());
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		held_keys.remove(e.getKeyCode());
		
	}

}
