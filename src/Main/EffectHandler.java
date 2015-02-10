package Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import Enums.EffectTrigger;
import Enums.StatusID;

public class EffectHandler{
//	public static final int MAX_NUM_EFFECTS = 32;
	
	public Map<StatusID, Integer> statuses;
	public Map<EffectTrigger, ArrayList<Effect>> effects;
	
//	private Effect[] effects;
	private int num_effects;
	
	public EffectHandler(){
		effects = new HashMap<EffectTrigger, ArrayList<Effect>>();
		statuses = new HashMap<StatusID, Integer>();
		num_effects = 0;
	}
	
	/**
	 * Takes all effects, and pushes them towards the front of the array
	 * so that no null entries are between any non-null entries. Retains 
	 * ordering of entries.
	 */
//	private void sortArray(){
//		Effect[] temp = new Effect[MAX_NUM_EFFECTS];
//		int temp_index = 0;
//		for(Effect ae : this){
//			if(ae != null){
//				temp[temp_index++] = ae;
//			}
//		}
//		
//		num_effects = temp_index;
//		effects = temp;
//		
//	}
	
//	@Override
//	public Iterator<Effect> iterator() {
//		// TODO Auto-generated method stub
//		return new AEH_Iterator(effects, num_effects);
//	}
//	
//	/**
//	 * Returns an array of all the StatusIDs in the effects.
//	 * @return
//	 */
//	public StatusID[] getStatuses(){
//		ArrayList<StatusID> array = new ArrayList<StatusID>();
//		StatusID current_status = null;
//		
//		for(Effect ae : this){
//			current_status = ae.getStatusID();
//			
//			/* if repeatedly calling contains is too slow, could very easily use
//			a hashset to store statuses that have already been added. I'm
//			not too concerned about calling contains because 95% of actors will have
//			<2 statuses at any given time. */
//			if(current_status != null && !array.contains(current_status)){
//				array.add(current_status);
//			}
//		}
//		
//		return array.toArray(new StatusID[0]);
//	}
	
	/**
	 * Inserts a new effect into the handler.
	 * @param e
	 * @return
	 */
	public boolean addEffect(Effect e){
		EffectTrigger trigger = e.getTrigger();
		
		if(!effects.containsKey(trigger)){
			effects.put(trigger, new ArrayList<Effect>());
		}
		
		effects.get(trigger).add(e);
		
		addStatus(e.getStatusID());
		
		return true;
	}
	
	private boolean addStatus(StatusID id){
		if(id == null)
			return false;
		
		if(!statuses.containsKey(id)){
			statuses.put(id, 1);
		}
		else{
			int i = statuses.get(id);
			statuses.put(id, i+1);
		}
		return true;
	}
	
	/**
	 * Removes an effect.
	 * @param e
	 * @return
	 */
	public boolean removeEffect(Effect e){
		EffectTrigger trigger = e.getTrigger();
		
		if(effects.containsKey(trigger)){
			return false;
		}
		
		return effects.get(trigger).remove(e);
	}
	
	private boolean removeStatus(StatusID id){
		if(id == null)
			return false;
		
		if(statuses.containsKey(id) == false)
			return false;
		
		int i = statuses.get(id);
		if(i <= 1)
			statuses.remove(id);
		else 
			statuses.put(id, i-1);
		return true;
	}
	
	/**
	 * Gets all the renderable status effects in this handler.
	 * @return
	 */
	public StatusID[] getStatuses(){
		Set<StatusID> keys = statuses.keySet();
		return keys.toArray(new StatusID[0]);
	}
	
//	private class AEH_Iterator implements Iterator<Effect>{
//		
//		private Effect[] array;
//		private int size;
//		private int current_index;
//		public AEH_Iterator(Effect[] array, int size){
//			this.array = array;
//			this.size = size;
//			this.current_index = 0;
//		}
//
//		@Override
//		public boolean hasNext() {
//			return current_index < size;
//		}
//
//		@Override
//		public Effect next() {
//			return array[current_index++];
//		}
//
//		@Override
//		public void remove() {
//			//not implemented.  
//			
//		}
//		
//	}
}
