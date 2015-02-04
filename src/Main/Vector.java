package Main;
import java.awt.Point;

/**
 * Immutable 2D vector class. 
 * @author dpendergast
 *
 */
public class Vector {
	
	private float x,y;
	
	//linear transforms between isometric and cartesian coordinate systems
	public static final float[] to_iso_transform  = new float[]{1, 1, -0.5f, 0.5f}; 
	public static final float[] to_cart_transform = new float[]{.5f, -1, 0.5f, 1};
	
	public Vector(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public Vector(){
		this(0,0);
	}
	
	public float x(){ return x;}
	public float y(){ return y;}
	
	public Vector duplicate(){
		return new Vector(x,y);
	}
	
	public Vector add(Vector v){
		return new Vector(x + v.x, y + v.y);
	}
	
	public Vector add(float x, float y){
		return new Vector(this.x + x, this.y + y);
	}
	
	public Vector subtract(Vector v){
		return new Vector(x - v.x, y - v.y);
	}
	
	public Vector subtract(float x, float y){
		return new Vector(this.x - x, this.y - y);
	}
	
	public float dot(Vector v){
		return x*v.x + y*v.y;
	}
	
	public float distTo(Vector v){
		return this.subtract(v).getMagnitude();
	}
	
	public Vector multiply(float scalar){
		return new Vector(scalar * x, scalar * y);
	}
	
	public Vector multiplyMatrix(float[] matrix){
		if(matrix.length != 4)
			throw new IllegalArgumentException("Given matrix doesn't have 4 elements");
		
		return new Vector(x*matrix[0] + y *matrix[1], x*matrix[2] + y*matrix[3]);
	}
	
	/**
	 * Returns a new vector which points in the same direction as this vector, but with a magnitude of 1.
	 * If no such vector exists (e.g. mag == 0), then (0,0) vector is returned.
	 */
	public Vector normalize(){
		float mag = getMagnitude();
		if(mag < 0.000001f)
			return new Vector(0,0);
		
		return new Vector(x / mag, y / mag);
	}
	
	public Vector closestTo(Vector ... vectors){
		float min_dist = Float.POSITIVE_INFINITY;
		Vector closest_vector = null;
		
		for(Vector v : vectors){
			float dist = this.distTo(v);
			if(dist < min_dist){
				closest_vector = v;
				min_dist = dist;
			}
		}
		
		return closest_vector;
	}
	
	public Vector toIsometric(){
		return this.multiplyMatrix(to_iso_transform);
	}
	
	public Vector toCartesian(){
		return this.multiplyMatrix(to_cart_transform);
	}
	
	public Point toPoint(){
		return new Point((int)x, (int)y);
	}
	
	public Vector rotate(float rad){
		float cos = (float)Math.cos(rad);
		float sin = (float)Math.sin(rad);
		
		return new Vector(cos*x - sin*y, sin*x + cos*y);
	}
	
	public float getMagnitude(){
		return (float)Math.sqrt(x*x + y*y);
	}
	
	public boolean equals(Vector v, float threshold){
		return Math.abs(x() - v.x()) < threshold && Math.abs(y() - v.y()) < threshold;
	}

}
