package object;

import vector.Vector;

public abstract class Surface extends Object {
	public abstract Vector getModelPoint(double u, double v);
	public Vector getPoint(double u, double v) {
		Vector p = getModelPoint(u, v);
		getModelMatrix().multiply(p);
		return p;
	}
	public abstract Vector getModelNorm(double u, double v);
	public Vector getNorm(double u, double v) {
		Vector p = getModelNorm(u, v);
		p.data[3] = 0;
		getModelMatrix().multiply(p);
		p.normalize();
		return p;
	}
}
