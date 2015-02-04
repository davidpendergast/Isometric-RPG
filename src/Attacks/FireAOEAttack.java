package Attacks;

import Actors.Actor;
import Enums.AttackID;
import Enums.Team;
import Main.Vector;

public class FireAOEAttack extends BasicAOEAttack {

	public FireAOEAttack(Vector start_pos,
			Vector direction, Team target, Actor source) {
		super(AttackID.AOE_FIRE, 8, AOE_GROWTH_RATE, start_pos, direction, target, source, true);
		damage = 10;
	}
	
	public void move(float dt){
		circle.setR(r()+growth_rate*dt);
	}

}
