package Main;

import Enums.EffectResult;
import Enums.EffectTarget;
import Enums.EffectTimeframe;
import Enums.EffectTrigger;
import Enums.StatusID;

public class Effect {
	
	private StatusID status_id;
	
	private EffectTrigger trigger;
	private EffectTarget target;
	private EffectResult result;
	private EffectTimeframe timeframe;
	
	public Effect(){
		
	}
	
	/**
	 * Returns the status id used for rendering purposes. For example, an effect which inflicts
	 * burning damage may have a StatusID value of BURN.
	 * @return
	 */
	public StatusID getStatusID(){
		return status_id;
	}
	
	public EffectTrigger getTrigger(){
		return trigger;
	}
	
	public EffectTarget getTarget(){
		return target;
	}
	
	public EffectResult getResult(){
		return result;
	}
	
	public EffectTimeframe getTimeframe(){
		return timeframe;
	}
	
	

}
