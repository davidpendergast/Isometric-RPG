package Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

import Actors.Actor;
import Actors.ActorFactory;
import Actors.EnemyActor;
import Actors.PlayerActor;
import Enums.ActorID;
import Enums.RenderStrategy;


public class _Initializer {
	
	private Window window;
	private InputHandler input_handler;
	private World world;
	private Color bg = new Color(238,238,238);
	private Viewport viewport;
	
	public _Initializer(){
		window = new Window();
		input_handler = window.getInputHandler();
		world = new World(30,30);
		viewport = new Viewport(world, 0, 0, 960, 640);
		Actor a = new PlayerActor(500,500,world);
		for(int i = 0; i < 100; i++){
			ActorFactory.getNewActor(ActorID.SCARY, 400+i, i*50, world, 0);
			ActorFactory.getNewActor(ActorID.SCARY, 600+i, i*50, world, 0);
//			new EnemyActor(400+i, i*50, world);
//			new EnemyActor(600+i, i*50, world);
			ActorFactory.getNewActor(ActorID.BUNNY, 400+i, i*50, world, 0);
		}
		viewport.setFocusActor(a);
		viewport.setActorSnap(true);
	}
	
	public void startRenderLoop(){
		//TODO should probably cap the fps
		
		int count = 0;
		long last_second = System.currentTimeMillis();
		
		while(true){
			
			count++;
			if(System.currentTimeMillis() - last_second >= 1000){
//				System.out.println("_Initializer - FPS: "+count);
				count = 0;
				last_second = System.currentTimeMillis();
			}
			
			Graphics g = window.getGraphics();
			g.setColor(bg);
			g.fillRect(0, 0, 960, 640);
			
			
			viewport.render(g);
			
			window.drawImage();
			
		}
	}
	
	public void startUpdateLoop(){
		new Thread(new Updater(input_handler, world, viewport)).start();
	}
	
	public static void main(String[] cucumber){
		
		_Initializer init = new _Initializer();
		init.startUpdateLoop();
		init.startRenderLoop();
		
	}
	
	
	
	
	

}
