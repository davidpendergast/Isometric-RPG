package Main;

import Actors.Actor;
import Enums.EffectResult;
import Enums.EffectTarget;
import Enums.EffectTimeframe;
import Enums.EffectTrigger;
import Enums.StatusID;

public class Effect {
	
	private StatusID status_id;
	private Actor source;
	private boolean use_source_as_other;
	
	private EffectTrigger trigger;
	private EffectTarget target;
	private EffectResult result;
	private EffectTimeframe timeframe;
	
	public Effect(StatusID status_id, EffectTrigger trigger, EffectTarget target, EffectResult result, EffectTimeframe timeframe, int time_num, Actor source, boolean use_source_as_other){
		this.status_id = status_id;
		
		this.trigger = trigger;
		this.target = target;
		this.result = result;
		this.timeframe = timeframe;
		
		this.source = source;
		this.use_source_as_other = use_source_as_other;
	}
	
	public Effect(StatusID status_id, EffectTrigger trigger, EffectTarget target, EffectResult result, EffectTimeframe timeframe, int time_num){
		this(status_id, trigger, target, result, timeframe, time_num, null, false);
	}
	
	/**
	 * Creates a new burn effect, which deals damage based on given source actor's stats.
	 * @param seconds how long the burn lasts.
	 * @param source origin of the effect.
	 * @return new burn effect.
	 */
	public static Effect getBurnEffect(int seconds, Actor source){
		return new Effect(StatusID.BURN, EffectTrigger.EVERY_1_SEC, EffectTarget.SELF, EffectResult.RECIEVE_BURN_DAMAGE, EffectTimeframe.TIME, seconds, source, true);
	}
	
	public static Effect getPoisonEffect(int seconds, Actor source){
		return new Effect(StatusID.POISON, EffectTrigger.EVERY_1_SEC, EffectTarget.SELF, EffectResult.RECIEVE_POISON_DAMAGE, EffectTimeframe.TIME, seconds, source, true);
	}
	
	/**
	 * Performs the result of the Effect. Note that every effect must have a source actor, and a target actor.
	 * The source actor is the actor who has the effect, and other is the actor who triggered the effect.
	 * It is possible for both actors to be the same, but neither can be null.
	 * @param source Actor who has effect
	 * @param other Actor who triggered the effect.
	 */
	public void execute(Actor source, Actor other){
		
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
	
	public boolean isTriggeredBy(EffectTrigger trigger){
		return this.trigger == trigger;
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
