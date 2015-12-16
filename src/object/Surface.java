package object;

import graphics.Material;
import vector.Vector;

public abstract class Surface extends Object {
	public Material face;
	public Surface() {
		face = new Material();
	}
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
	public Material getMaterial() {
		return face;
	}
	public void setMaterial(Material m) {
		face = m;
	}
}
