package Main;

public interface Circular {
		
	public float x();
	public float y();
	public float r();
	
	public boolean moveTo(float x, float y);
	public boolean moveTo(Vector v);
	public boolean moveInDir(float dist, Vector v);
	
	public float distTo(Circular c);
	
	public Vector center();
}
