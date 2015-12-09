package matrix;

public class Vector {
	public double[] data = {0, 0, 0, 1};
	public Vector() {
		
	}
	public Vector(double x, double y, double z) {
		this();
		set(x, y, z);
	}
	public Vector(Vector v) {
		this();
		set(v);
	}
	public void set(double[] a) {
		for(int i = 0; i < 4; ++i) {
			data[i] = a[i];
		}
	}
	public void set(double x, double y, double z) {
		data[0] = x;
		data[1] = y;
		data[2] = z;
		data[3] = 1.0;
	}
	public void set(Vector a) {
		set(a.data);
	}
	public void multiply(double factor) {
		data[0] *= factor;
		data[1] *= factor;
		data[2] *= factor;
	}
	public double dot(Vector in) {
		return data[0]*in.data[0] + data[1]*in.data[1] + data[2]*in.data[2];
	}
	public void cross(Vector a, Vector b) {
		set(
		  a.data[1]*b.data[2] - a.data[2]*b.data[1],
		  a.data[2]*b.data[0] - a.data[0]*b.data[2],
		  a.data[0]*b.data[1] - a.data[1]*b.data[0]
		  );
	}
	public double length() {
		return Math.sqrt(dot(this));
	}
	public void normalize() {
		multiply(1.0/length());
	}
}
