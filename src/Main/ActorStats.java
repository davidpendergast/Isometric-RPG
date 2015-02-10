package Main;

import Actors.Actor;
import Attacks.Attack;
import Enums.DamageType;
import Enums.EffectTrigger;
import Enums.StatusID;

public class ActorStats {
	private Actor actor;
	
	private int level = 0;
	
	private int max_hp = 30;
	private int hp = 30;

	private int[] damage = new int[]{10,10,10,3,2};
	private int[] heal = new int[]{3,5,9};
	
	int burn_pulses = 0;
	int burn_dmg = 0;
	
	int poison_pulses = 0;
	int poison_dmg = 0;
	
	private EffectHandler effect_handler;
	
	public ActorStats(Actor a){
		this.actor = a;
		effect_handler = new EffectHandler();
	}
	
	public void addEffect(Effect effect){
		effect_handler.addEffect(effect);
	}
	
	public void removeEffect(Effect effect){
		effect_handler.removeEffect(effect);
	}
	
	public void sendTrigger(EffectTrigger trigger, Actor source){
		//TODO
	}
	
	public StatusID[] getStatuses(){
		return effect_handler.getStatuses();
	}
	
	/**
	 * Gives a value between 0 and 1 which describes the amount of health the actor has remaining.
	 * @return
	 */
	public float percentHealthRemaining(){
		float res = (float)hp / (float)max_hp;
		if(res > 1)
			return 1;
		else if (res < 0)
			return 0;
		else return res;
	}
	
	/**
	 * Gets the actor's damage value for given damage type.
	 * @param dt damage type
	 * @return
	 */
	public int getDamageValue(DamageType dt){
		int index = dt.ordinal();
		if(index < damage.length){
			return damage[index];
		}
		else
			return 0;
	}
	
	/**
	 * Sets the actor's damage value for given damage type.
	 * @param dt damage type
	 * @return
	 */
	public void setDamageValue(DamageType dt, int val){
		int index = dt.ordinal();
		if(index < damage.length){
			damage[index] = val;
		}
	}
	
	/**
	 * Sets the actor's level. Various stats will be adjusted to align with the new level.
	 * @param level
	 */
	public void setLevel(int level){
		if(level >= 0){
			this.level = level;
		}
	}
	
	public boolean isAlive(){
		return hp > 0;
	}
	
	public void giveDamage(DamageType dt, Attack source, boolean trigger_effects){
//		System.out.println("ActorStats - recieving dmg from "+source);
		hp -= source.damage();
	}
	
//	/**
//	 * Gives actor a burn which deals given damage for given number of turns. Total damage dealt will be dmg*ticks, if burn is successfully given.  
//	 * @param dmg damage recieved per pulse
//	 * @param pulses number of times burn damage is recieved 
//	 * @param chance chance of successfully giving burn.
//	 */
//	public boolean giveBurn(int dmg, int pulses, float chance){
//		if(Math.random() < chance && dmg * pulses >= burn_dmg * burn_pulses){
//			burn_dmg = dmg;
//			burn_pulses = pulses;
//			return true;
//		}
//		
//		return false;
//	}
//	
//	/**
//	 * Gives actor poison which deals given damage for given number of turns. Total damage dealt will be dmg*ticks, if poison is successfully given.  
//	 * @param dmg damage recieved per pulse
//	 * @param pulses number of times burn damage is recieved 
//	 * @param chance chance of successfully giving burn.
//	 */
//	public boolean givePoison(int dmg, int pulses, float chance){
//		if(Math.random() < chance && dmg * pulses >= poison_dmg * poison_pulses){
//			poison_dmg = dmg;
//			poison_pulses = pulses;
//			return true;
//		}
//		
//		return false;
//	}
	
	/**
	 * Returns the actor's burn damage
	 * @return
	 */
	public int burnDamage(){
		return getDamageValue(DamageType.BURN);
	}
	
	public int poisonDamage(){
		return getDamageValue(DamageType.POISON);
	}

	public void setMaxHealth(int new_health) {
		this.max_hp = new_health;
		if(hp > max_hp){
			hp = max_hp;
		}
	}
}
