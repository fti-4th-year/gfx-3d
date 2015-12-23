package vector;

public class Vector {
	public double[] data = {0, 0, 0, 1};
	public Vector() {
		
	}
	public Vector(double x, double y, double z) {
		this();
		set(x, y, z);
	}
	public Vector(double x, double y, double z, double w) {
		this();
		set(x, y, z, w);
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
		set(x, y, z, 1.0);
	}
	public void set(double x, double y, double z, double w) {
		data[0] = x;
		data[1] = y;
		data[2] = z;
		data[3] = w;
	}
	public void set(Vector a) {
		set(a.data);
	}
	public void add(Vector v) {
		data[0] += v.data[0];
		data[1] += v.data[1];
		data[2] += v.data[2];
	}
	static public Vector add(Vector a, Vector b) {
		return new Vector(
			a.data[0] + b.data[0],
			a.data[1] + b.data[1],
			a.data[2] + b.data[2]
			);
	}
	static public Vector subtract(Vector a, Vector b) {
		return new Vector(
			a.data[0] - b.data[0],
			a.data[1] - b.data[1],
			a.data[2] - b.data[2]
			);
	}
	public void multiply(double factor) {
		data[0] *= factor;
		data[1] *= factor;
		data[2] *= factor;
	}
	static public Vector multiply(Vector a, double b) {
		return new Vector(
				a.data[0]*b,
				a.data[1]*b,
				a.data[2]*b
				);
	}
	public double dot(Vector in) {
		return data[0]*in.data[0] + data[1]*in.data[1] + data[2]*in.data[2];
	}
	static public double dot(Vector a, Vector b) {
		return a.data[0]*b.data[0] + a.data[1]*b.data[1] + a.data[2]*b.data[2];
	}
	static public Vector cross(Vector a, Vector b) {
		return new Vector(
		  a.data[1]*b.data[2] - a.data[2]*b.data[1],
		  a.data[2]*b.data[0] - a.data[0]*b.data[2],
		  a.data[0]*b.data[1] - a.data[1]*b.data[0]
		  );
	}
	public double length() {
		return Math.sqrt(dot(this));
	}
	static double length(Vector v) {
		return v.length();
	}
	public void normalize() {
		multiply(1.0/length());
	}
	static public Vector normalize(Vector v) {
		return multiply(v, 1.0/length(v));
	}
	public String toString() {
		return "{" + data[0] + ", " + data[1] + ", " + data[2] + "}";
	}
}
