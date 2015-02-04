package Main;
import java.util.LinkedList;
import java.util.Queue;

import Action.Action;
import Action.WalkAction;


public class ActionQueue {
	
	Queue<Action> action_list;
	
	public ActionQueue(){
		action_list = new LinkedList<Action>();
	}
	
	public void push(Action a){
		action_list.add(a);
	}
	
	public Action pop(){
		return action_list.poll();
	}
	
	public Action peek(){
		return action_list.peek();
	}
	
	public void clear(){
		action_list.clear();
	}
	
	public void clearWalkActions(){
		WalkAction w = new WalkAction(null,null,null,0);
		while(action_list.remove(w)){
			//keep removing
		}
	}
}
