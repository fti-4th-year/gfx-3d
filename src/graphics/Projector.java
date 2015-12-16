package graphics;
import vector.Matrix;


public class Projector {
	private Matrix proj;
	public double w = 0.2, h = 0.2, f = -100, n = -0.2; 
	public Projector() {
		proj = new Matrix();
	}
	public Matrix getProjMatrix() {
		return proj;
	}
	public void update() {
		proj = new Matrix();
		proj.data[0] = n/w;
		proj.data[5] = n/h;
		proj.data[10] = -(f + n)/(f - n);
		proj.data[11] = -2*f*n/(f - n);
		proj.data[14] = -1;
		proj.data[15] = 0;
	}
}
