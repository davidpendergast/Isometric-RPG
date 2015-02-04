package Main;

import java.util.ArrayList;
import java.util.Iterator;

import Enums.StatusID;

public class EffectHandler implements Iterable<Effect> {
	public static final int MAX_NUM_EFFECTS = 32;
	
	private Effect[] effects;
	private int num_effects;
	
	public EffectHandler(){
		effects = new Effect[MAX_NUM_EFFECTS];
		num_effects = 0;
	}
	
	/**
	 * Takes all effects, and pushes them towards the front of the array
	 * so that no null entries are between any non-null entries. Retains 
	 * ordering of entries.
	 */
	private void sortArray(){
		Effect[] temp = new Effect[MAX_NUM_EFFECTS];
		int temp_index = 0;
		for(Effect ae : this){
			if(ae != null){
				temp[temp_index++] = ae;
			}
		}
		
		num_effects = temp_index;
		effects = temp;
		
	}
	
	@Override
	public Iterator<Effect> iterator() {
		// TODO Auto-generated method stub
		return new AEH_Iterator(effects, num_effects);
	}
	
	/**
	 * Returns an array of all the StatusIDs in the effects.
	 * @return
	 */
	public StatusID[] getStatuses(){
		ArrayList<StatusID> array = new ArrayList<StatusID>();
		StatusID current_status = null;
		
		for(Effect ae : this){
			current_status = ae.getStatusID();
			
			/* if repeatedly calling contains is too slow, could very easily use
			a hashset to store statuses that have already been added. I'm
			not too concerned about calling contains because 95% of actors will have
			<2 statuses at any given time. */
			if(current_status != null && !array.contains(current_status)){
				array.add(current_status);
			}
		}
		
		return array.toArray(new StatusID[0]);
	}
	
	private class AEH_Iterator implements Iterator<Effect>{
		
		private Effect[] array;
		private int size;
		private int current_index;
		public AEH_Iterator(Effect[] array, int size){
			this.array = array;
			this.size = size;
			this.current_index = 0;
		}

		@Override
		public boolean hasNext() {
			return current_index < size;
		}

		@Override
		public Effect next() {
			return array[current_index++];
		}

		@Override
		public void remove() {
			//not implemented.  
			
		}
		
	}
	
	
	
}
