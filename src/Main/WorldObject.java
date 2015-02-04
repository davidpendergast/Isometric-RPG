package Main;

import java.awt.Graphics;

import Enums.RenderStrategy;

public interface WorldObject extends Circular {

	public void render(Graphics g, int x_offset, int y_offset, RenderStrategy render_type);
	public void update(InputHandler input_handler,float dt);
}
