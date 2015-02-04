package Main;
import java.awt.Graphics;

import Attacks.Attack;
import Enums.RenderStrategy;


public class AttackHandler {
	public World world;
	
	Attack[] above_attacks = new Attack[128];
	int num_above_attacks = 0;
	
	Attack[] below_attacks = new Attack[128];
	int num_below_attacks = 0;
	
	public AttackHandler(World world){
		this.world = world;
	}
	
	public boolean add(Attack a){
		if(a.isRenderedAbove()){
			if(num_above_attacks < above_attacks.length){
				above_attacks[num_above_attacks++] = a;
				return true;
			}
			return false;
		}
		else{
			if(num_below_attacks < below_attacks.length){
				below_attacks[num_below_attacks++] = a;
				return true;
			}
			return false;
		}
	}
	
	public boolean remove(Attack a){
		int end_index = a.isRenderedAbove() ? num_above_attacks : num_below_attacks;
		Attack[] array = a.isRenderedAbove() ? above_attacks : below_attacks;
		
		for(int i = 0; i < end_index; i++){
			if(array[i] == a){
				array[i] = null;
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns true if either array is 'full'. Meaning null values may be in the list, but the addition index variable has been shifted 
	 * all the way to the end.
	 * @return
	 */
	public boolean isFull(){
		return num_above_attacks == above_attacks.length || num_below_attacks == below_attacks.length;
	}
	
	/**
	 * Doesn't actually sort attacks. It is named this way to be consistant with the Tile method SortArray().  
	 * This method removes null entries from the Attack arrays by shifting non-null entries down a spot. This opperation cannot 
	 * be done on the fly due to concurrency issues. Instead, whenever the arrays reach capacity, this method is called to make room for new
	 * attacks.
	 */
	public void sortArrays(){
		Attack[] new_above = new Attack[above_attacks.length];
		int counter = 0;
		Attack temp = null;
		for(int i = 0; i < num_above_attacks; i++){
			temp = above_attacks[i];
			if(temp != null)
				new_above[counter++] = temp;
		}
		
		above_attacks = new_above;
		num_above_attacks = counter;
		
		Attack[] new_below = new Attack[below_attacks.length];
		counter = 0;
		for(int i = 0; i < num_below_attacks; i++){
			temp = below_attacks[i];
			if(temp != null)
				new_below[counter++] = temp;
		}
		
		below_attacks = new_below;
		num_below_attacks = counter;
	}
	
	public void updateAttacks(InputHandler input_handler, float dt){
		for(int i = 0; i < num_above_attacks; i++){
			Attack a = above_attacks[i];
			if(a != null)
				a.update(input_handler, dt);
		}
		for(int i = 0; i < num_below_attacks; i++){
			Attack a = below_attacks[i];
			if(a != null)
				a.update(input_handler, dt);
		}
	}
	
	public void renderAboveAttacks(Graphics g, int x_offset, int y_offset, RenderStrategy render_strategy){
		renderArray(above_attacks, num_above_attacks, g, x_offset, y_offset, render_strategy);
	}
	
	public void renderBelowAttacks(Graphics g, int x_offset, int y_offset, RenderStrategy render_strategy){
		renderArray(below_attacks, num_below_attacks, g, x_offset, y_offset, render_strategy);
	}
	
	private void renderArray(Attack[] array, int size, Graphics g, int x_offset, int y_offset, RenderStrategy render_strategy){
		
		for(int i = 0; i < size; i++){
			Attack a = array[i];
			if(a != null){
				a.render(g, x_offset, y_offset, render_strategy);
			}
		}
	}

}
