package Main;

public class Updater implements Runnable {
	
	private InputHandler input_handler;
	private World world;
	private Viewport viewport;
	
	private long nano_per_frame = 16666666/4;
	
	public Updater(InputHandler input_handler, World world, Viewport viewport){
		this.input_handler = input_handler;
		this.world = world;
		this.viewport = viewport;
	}

	@Override
	public void run() {
		while(true){
			long curr_time = System.nanoTime();
			
			viewport.update(input_handler);
			world.updateAll(input_handler, .5f);
			//sometimes inputs are lost between updateAll and reset.
			//TODO implement locking mechanism for input_handler, so you can
			//prevent new inputs 
			input_handler.reset();	
			world.solveAllCollisions();
			
			//negative wait time is handled by sleep method
			sleep(nano_per_frame - (System.nanoTime() - curr_time));
		}	
	}
	
	private void sleep(long nano){
//		System.out.println("Updater - sleep time = "+nano);
		if(nano <= 0)
			return;
		
		try {
			Thread.sleep(nano/1000000, (int) (nano % 1000000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	

}
