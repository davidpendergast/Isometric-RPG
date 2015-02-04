package Main;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import Enums.ActorID;
import Enums.AnimationID;
import Enums.AttackAnimationID;
import Enums.AttackID;
import Enums.HotbarButtonState;
import Enums.StatusID;
import Enums.Team;
import Enums.TileID;


public class ImageHandler {
	
	static BufferedImage[] tile_imgs;
	static BufferedImage[][][] actor_imgs;
	static BufferedImage[][] attack_imgs;
	static BufferedImage[][] attack_icon_imgs;
	static BufferedImage[][] status_imgs;
	
	static BufferedImage[][] health_bar_imgs;
	
	static BufferedImage hotbar_background;
	static BufferedImage[] hotbar_bars;
	
	static{
		//[tile type]
		tile_imgs = new BufferedImage[40];
		
		//[Actor type][Animation frame][left or right facing]
		actor_imgs = new BufferedImage[20][8][2];
		
		//[attack type][animation frame]
		attack_imgs = new BufferedImage[20][8];
		
		//[attack type][state (active, disabled, on cooldown etc.)
		attack_icon_imgs = new BufferedImage[20][2];
		
		//[status type][animation frame]
		status_imgs = new BufferedImage[20][8];
		
		//[health bar type][front or back img]
		health_bar_imgs = new BufferedImage[3][2];
		
		try {
			BufferedImage img = ImageIO.read(new File("data/assets.png"));
			
			//getting tile images
			for(int i = 0; i < tile_imgs.length/2; i++){
				tile_imgs[i] 					= img.getSubimage(0, i*32, 64, 32);
				tile_imgs[i+tile_imgs.length/2] = img.getSubimage(64, i*32, 64, 32);
			}
			
			//getting actor images
			for(int i = 0; i < actor_imgs.length; i++){
				for(int j = 0; j < actor_imgs[0].length; j++){
					actor_imgs[i][j][0] = img.getSubimage(128 + j*16, i*32, 16, 32);
					actor_imgs[i][j][1] = flipHorizontally(actor_imgs[i][j][0]);
				}
			}
			
			//getting attack images
			for(int i = 0; i < attack_imgs.length; i++){
				for(int j = 0; j < attack_imgs[0].length; j++){
					attack_imgs[i][j] = img.getSubimage(256 + j*16, i*32, 16, 32);
				}
			}
			
			//getting hotbar icons for attacks
			for(int i = 0; i < attack_icon_imgs.length; i++){
				for(int j = 0; j < attack_icon_imgs[0].length; j++){
					attack_icon_imgs[i][j] = img.getSubimage(384 + j*32, i*32, 32, 32);
				}
			}
			
			//getting status images
			for(int i = 0; i < attack_imgs.length; i++){
				for(int j = 0; j < attack_imgs[0].length; j++){
					status_imgs[i][j] = img.getSubimage(416 + j*16, i*32, 16, 32);
				}
			}
			
			//getting health bar images
			for(int i = 0; i < health_bar_imgs.length; i++){
				for(int j = 0; j < health_bar_imgs[0].length; j++){
					health_bar_imgs[i][j] = img.getSubimage(304 + i*16, 608 + j*16, 16, 16);
				}
			}
			
		} catch (IOException e) {
			System.out.println("Error: data/assets.png not found.");
			e.printStackTrace();
			System.exit(0);
		}	
		
		hotbar_background = null;
		
		hotbar_bars = new BufferedImage[2];
		
		try {
			BufferedImage img2 = ImageIO.read(new File("data/gui_assets.png"));
			
			//getting the gui hotbar's background image
			hotbar_background = img2.getSubimage(0, 8*32, 15*32, 64);
			
			//getting the health and mana bars for gui
			for(int i = 0; i < hotbar_bars.length; i++){
				hotbar_bars[i] = img2.getSubimage(32*i, 7*32, 32, 32);
			}
			
			
		} catch (IOException e) {
			System.out.println("Error: data/gui_assets.png not found.");
			e.printStackTrace();
			System.exit(0);
		}	
		
	}
	
	private static BufferedImage flipHorizontally(BufferedImage img){
		BufferedImage flip_img = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
		int x_size = img.getWidth();
		int y_size = img.getHeight();
		
		for(int x = 0; x < x_size; x++){
			for(int y = 0; y < y_size; y++){
				flip_img.setRGB(x_size-x-1, y, img.getRGB(x, y));
			}
		}
		
		return flip_img;
	}
	
	public static BufferedImage getImage(ActorID actor, AnimationID animation, AnimationID.Direction direction){
		return actor_imgs[actor.ordinal()][animation.ordinal()][direction.ordinal()];
	}
	
	public static BufferedImage getImage(TileID tile_id){
		return tile_imgs[tile_id.ordinal()];
	}
	
	public static BufferedImage getImage(StatusID status_id, int frame, boolean front){
		if(front){
			return status_imgs[status_id.ordinal()][frame];
		}
		else 
			return status_imgs[status_id.ordinal()][frame + 4];
		
	}
	
	public static BufferedImage getImage(AttackID attack_id, AttackAnimationID aa_id){
		return attack_imgs[attack_id.ordinal()][aa_id.ordinal()];
	}
	
	public static BufferedImage getImage(AttackID attack_id, HotbarButtonState hbbs){
		if(hbbs == HotbarButtonState.EMPTY)
			return null;
		return attack_icon_imgs[attack_id.ordinal()][hbbs.ordinal()];
	}
	
	public static BufferedImage getHotbarBackground(){
		return hotbar_background;
	}
	
	public static BufferedImage getHotbarBar(int id){
		return hotbar_bars[id];
	}
	
	public static BufferedImage getHealthBar(Team t, int front_or_back){
		if(t == Team.BAD){
			return health_bar_imgs[0][front_or_back];
		}
		else if(t == Team.GOOD){
			return health_bar_imgs[1][front_or_back];
		}
		else {
			return health_bar_imgs[2][front_or_back];
		}
	}

}
