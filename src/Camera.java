import vector.Matrix;
import vector.Vector;


public class Camera {
	private Matrix view;
	private Vector up, x, y, z;
	public Camera() {
		view = new Matrix();
		up = new Vector(0.0, 0.0, 1.0);
		x = new Vector();
		y = new Vector();
		z = new Vector();
	}
	public Matrix getViewMatrix() {
		return view;
	}
	public void setViewMatrix(Matrix view) {
		this.view = view;
	}
	public void lookFrom(Vector p) {
		z.set(p);
		z.normalize();
		x = Vector.cross(up, z);
		x.normalize();
		y = Vector.cross(z, x);
		for(int i = 0; i < 3; ++i) {
			view.data[0 + i] = -x.data[i];
			view.data[4 + i] = -y.data[i];
			view.data[8 + i] = -z.data[i];
			view.data[3 + 4*i] = 0.0;
		}
		Vector pt = new Vector(p);
		view.multiply(pt);
		for(int i = 0; i < 3; ++i) {
			view.data[3 + 4*i] = -pt.data[i];
		}
	}
}
