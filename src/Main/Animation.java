package Main;
import java.awt.image.BufferedImage;

import Enums.ActorID;
import Enums.AnimationID;
import Enums.AttackAnimationID;
import Enums.AttackID;


public class Animation {
	
	private ActorID actor_id;
	private AttackID attack_id;
	
	public static final int STANDARD_TICKS_PER_FRAME = 40;
	
	float ticks_per_frame = STANDARD_TICKS_PER_FRAME;
	float clock = ticks_per_frame;
	int frame_index = 0;
	int animation_index = 0;
	int direction = 0;
	
	int loopnum = -1;
	int default_animation = 0;
	
	//[anim_type][anim_frame][direction]
	BufferedImage[][][] animation_chart;
		
	static BufferedImage[][][][] actor_charts;
	static BufferedImage[][][][] attack_charts;
	
	static{
		ActorID[] actor_ids = ActorID.values();
		AttackID[] attack_ids = AttackID.values();
		actor_charts = new BufferedImage[actor_ids.length][][][];
		attack_charts = new BufferedImage[attack_ids.length][][][];
		
		for(int i = 0; i < ActorID.values().length; i++){
			actor_charts[i] = constructAnimationChart(actor_ids[i]);
		}
		
		for(int i = 0; i < AttackID.values().length; i++){
			attack_charts[i] = constructAttackAnimationChart(attack_ids[i]);
		}
	}
	
	static BufferedImage[][][] constructAnimationChart(ActorID actor){
		BufferedImage[][][] res = new BufferedImage[2][4][2];
		
		res[0][0][0] = ImageHandler.getImage(actor, AnimationID.WALK_0, AnimationID.Direction.RIGHT);
		res[0][1][0] = ImageHandler.getImage(actor, AnimationID.WALK_1, AnimationID.Direction.RIGHT);
		res[0][2][0] = ImageHandler.getImage(actor, AnimationID.WALK_2, AnimationID.Direction.RIGHT);
		res[0][3][0] = ImageHandler.getImage(actor, AnimationID.WALK_3, AnimationID.Direction.RIGHT);
		
		res[0][0][1] = ImageHandler.getImage(actor, AnimationID.WALK_0, AnimationID.Direction.LEFT);
		res[0][1][1] = ImageHandler.getImage(actor, AnimationID.WALK_1, AnimationID.Direction.LEFT);
		res[0][2][1] = ImageHandler.getImage(actor, AnimationID.WALK_2, AnimationID.Direction.LEFT);
		res[0][3][1] = ImageHandler.getImage(actor, AnimationID.WALK_3, AnimationID.Direction.LEFT);
		
		res[1][0][0] = ImageHandler.getImage(actor, AnimationID.ATT_0, AnimationID.Direction.RIGHT);
		res[1][1][0] = ImageHandler.getImage(actor, AnimationID.ATT_1, AnimationID.Direction.RIGHT);
		res[1][2][0] = ImageHandler.getImage(actor, AnimationID.ATT_2, AnimationID.Direction.RIGHT);
		res[1][3][0] = ImageHandler.getImage(actor, AnimationID.ATT_3, AnimationID.Direction.RIGHT);
		                                                        
		res[1][0][1] = ImageHandler.getImage(actor, AnimationID.ATT_0, AnimationID.Direction.LEFT);
		res[1][1][1] = ImageHandler.getImage(actor, AnimationID.ATT_1, AnimationID.Direction.LEFT);
		res[1][2][1] = ImageHandler.getImage(actor, AnimationID.ATT_2, AnimationID.Direction.LEFT);
		res[1][3][1] = ImageHandler.getImage(actor, AnimationID.ATT_3, AnimationID.Direction.LEFT);
		
		return res;
	}
	
	static BufferedImage[][][] constructAttackAnimationChart(AttackID attack){
		BufferedImage[][][] res = new BufferedImage[2][4][2];
		
		res[0][0][0] = ImageHandler.getImage(attack, AttackAnimationID.MOVE_0);
		res[0][1][0] = ImageHandler.getImage(attack, AttackAnimationID.MOVE_1);
		res[0][2][0] = ImageHandler.getImage(attack, AttackAnimationID.MOVE_2);
		res[0][3][0] = ImageHandler.getImage(attack, AttackAnimationID.MOVE_3);
		
		res[1][0][0] = ImageHandler.getImage(attack, AttackAnimationID.END_0);
		res[1][1][0] = ImageHandler.getImage(attack, AttackAnimationID.END_1);
		res[1][2][0] = ImageHandler.getImage(attack, AttackAnimationID.END_2);
		res[1][3][0] = ImageHandler.getImage(attack, AttackAnimationID.END_3);
		
		return res;
	}
	
	public Animation(ActorID actor_id){
		this.actor_id = actor_id;
		animation_chart = actor_charts[actor_id.ordinal()];
	}
	
	public Animation(AttackID attack_id){
		this.attack_id = attack_id;
		animation_chart = attack_charts[attack_id.ordinal()];
	}
	
	public void incrementFrame(int num_frames){
		int temp_anim_index = animation_index;
		if(temp_anim_index == -1)
			return;
		
		int temp_frame_index = frame_index + num_frames;
		
		if(temp_frame_index >= animation_chart[temp_anim_index].length){
			startNewAnimationCycle();
		}
		else{
			frame_index = temp_frame_index;
		}
	}
	
	public void update(int ticks){
		clock -= ticks;
		if(clock < 0){
			clock = ticks_per_frame;
			
			//atomically changing frame_index
			incrementFrame(1);
		}
	}
	
	private void startNewAnimationCycle(){
		if(loopnum > 0){
			loopnum--;
		}
		
		if(loopnum == 0){
			animation_index = default_animation;
		}
		
		frame_index = 0;
	}
	
	public BufferedImage getImage(){
		//solving concurreny issues
		int temp_anim_index = animation_index;
		if(temp_anim_index == -1)
			return null;
		
		return animation_chart[temp_anim_index][frame_index][direction];
		
	}
	
	public void setDirection(AnimationID.Direction dir){
		if(dir == AnimationID.Direction.RIGHT){
			direction = 0;
		}
		else 
			direction = 1;
			
	}
	
	public void playAnimation(AnimationID animation, int loopnum){
		frame_index = 0;
	
		switch(animation){
			case WALK_0: 
				animation_index = 0;
				break;
			case ATT_0:
				animation_index = 1;
				break;
			default:
				animation_index = default_animation;
		}
		
		this.loopnum = loopnum;
	}
	
	public void playAnimation(AttackAnimationID animation, int loopnum){
		frame_index = 0;
		
		switch(animation){
			case MOVE_0: 
				animation_index = 0;
				break;
			case END_0:
				animation_index = 1;
				break;
			default:
				animation_index = default_animation;
		}
		
		this.loopnum = loopnum;
	}
	
	public AnimationID getCurrentAnimationID(){
		switch(animation_index){
		case 0:
			return AnimationID.WALK_0;
		case 1:
			return AnimationID.ATT_0;
		default:
			return null;
		}
	}
	
	public void setDefaultAnimation(AttackAnimationID id){
		if(id == null){
			default_animation = -1;
			return;
		}
		
		switch(id){
		case MOVE_0:
			default_animation = 0;
			break;
		case END_0:
			default_animation = 1;
			break;
		default:
			default_animation = -1;
		}
	}

}
