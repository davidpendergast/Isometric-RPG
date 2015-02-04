package Main;

import java.awt.Color;
import java.awt.Graphics;

import Enums.RenderStrategy;

public class Circle implements Circular {
	private float x,y,r;
	
	public Circle(float r){
		this(0,0,r);
	}
	
	public Circle(Vector c, float r){
		this.x = c.x();
		this.y = c.y();
		this.r = r;
	}
	
	public Circle(float x, float y, float r){
		this.x = x;
		this.y = y;
		this.r = r;
	}
	
	public float x(){return x;}
	public float y(){return y;}
	public float r(){return r;}
	public Vector center(){return new Vector(x,y);}
	
	public void setR(float r){
		this.r = r;
	}
	
	//d
//	public static void solveCollision(Circle c1, Circle c2, int w1, int w2){
//		float dist = c1.distTo(c2);
//		
//		//if the circles aren't actually colliding
//		if(dist > c1.r() + c2.r())
//			return;
//		
//		if(w1 <= 0 || w2 <= 0){
//			throw new IllegalArgumentException("Circle weight is non-posive.");
//		}
//		
//		float ratio = (float)w1 / (float)w2;
//		
//		//not too sure about this line
//		//edit: kinda sure about this line
//		float overlap_dist = c1.r() + c2.r() - dist;
//		float c2_shift = ratio * overlap_dist;
//		float c1_shift = overlap_dist - c2_shift;
//		
//		c2.shift(c2_shift, c2.vectorTo(c1));
//		c1.shift(c1_shift, c1.vectorTo(c2));
//	}
	
	//d
//	public static void solveCollision(Circle c1, Circle c2){
//		solveCollision(c1, c2, 1, 1);
//	}
	
	//d
	public float distTo(Circular c){
		float dx = x - c.x();
		float dy = y - c.y();
		return (float)Math.sqrt(dx*dx + dy*dy);
	}
	
	//d
	public Vector vectorTo(Circular c){
		return new Vector(c.x() - x, c.y() - y);
	}
	
//	public void setPosition(float x, float y){
//		this.x = x;
//		this.y = y;
//	}

	@Override
	public boolean moveTo(float x, float y) {
		this.x = x;
		this.y = y;
		return true;
	}

	@Override
	public boolean moveTo(Vector v) {
		this.x = v.x();
		this.y = v.y();
		return true;
	}

	@Override
	public boolean moveInDir(float dist, Vector v) {
		v = v.normalize();
		this.x = this.x + v.x()*dist;
		this.y = this.y + v.y()*dist;
		return true;
	}
	
	public void render(Graphics g, Color c, int x_offset, int y_offset, RenderStrategy render_strategy){
		if(render_strategy == RenderStrategy.CARTESIAN_WIREFRAME){
			g.setColor(c);
			g.drawOval((int)(x-r) - x_offset, (int)(y-r) - y_offset, (int)(r*2), (int)(r*2));
		}
		else {
			g.setColor(c);
			Vector v = center().toIsometric();
			int xr = (int)(r*1.4);
			int yr = (int)(r/1.4f);
			g.drawOval((int)(v.x() - xr) - x_offset, (int)(v.y() - yr) - y_offset, (int)xr*2, (int)yr*2);
		}
	}
	
//	//d
//	public void shift(Vector v){
//		x += v.x();
//		y += v.y();
//	}
//	
//	//d
//	public void shift(float dist, Vector v){
//		Vector norm = v.normalize();
//		
//		//if given vector is 0,0
//		if(norm == null)
//			return;
//		
//		x += dist * norm.x();
//		y += dist * norm.y();
//	}
	

}
