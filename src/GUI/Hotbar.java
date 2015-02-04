package GUI;

import java.awt.Graphics;

import Actors.Actor;
import Enums.AttackID;
import Enums.HotbarButtonState;
import Main.ImageHandler;

public class Hotbar extends GUIElement{
	
	private Actor actor;
	private AttackID[] attack_ids;
	private HotbarButtonState[] button_states;
	
	public Hotbar(Actor a){
		this.actor = a;
		attack_ids = new AttackID[]{AttackID.SMALL_BONE_PROJECTILE, AttackID.SMALL_FIRE_PROJECTILE, AttackID.SMALL_POISON_PROJECTILE, null, null};
		button_states = new HotbarButtonState[]{	HotbarButtonState.READY,
													HotbarButtonState.READY,
													HotbarButtonState.READY,
													HotbarButtonState.EMPTY,
													HotbarButtonState.EMPTY};
	}
	
	public int width(){
		return 2*15*32;
	}
	
	public int height(){
		return 2*2*32;
	}
	
	public void render(Graphics g, int x_offset, int y_offset){
		g.drawImage(ImageHandler.getHotbarBackground(), x_offset, y_offset, width(), height(), null);
		
		Actor actor = this.actor;
		if(actor == null)
			return;
		
		float percent_hp = actor.stats().percentHealthRemaining();
		float percent_mana = 1;
		
		//rendering hp bar
		g.drawImage(ImageHandler.getHotbarBar(0), 2*4 + x_offset, 2*(32) +y_offset, 2*(int)(percent_hp*152), 2*(32), null);
		
		//rendering mana bar
		g.drawImage(ImageHandler.getHotbarBar(1), 2*324 + x_offset, 2*(32) +y_offset, 2*(int)(percent_hp*152), 2*(32), null);
		
		for(int i = 0; i < 5; i++){
			g.drawImage(ImageHandler.getImage(attack_ids[i], button_states[i]), x_offset + 160*2 + 64*i, y_offset + 32, 2*32, 2*32, null);
		}
	}

}
