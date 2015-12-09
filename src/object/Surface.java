package object;

import matrix.Vector;

public abstract class Surface extends Object {
	public abstract Vector getModelPoint(double u, double v);
	public Vector getPoint(double u, double v) {
		Vector p = getModelPoint(u, v);
		getModelMatrix().multiply(p);
		return p;
	}
}
