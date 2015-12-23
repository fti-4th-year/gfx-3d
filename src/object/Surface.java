package object;

import graphics.Material;
import util.Color;
import vector.Vector;

public abstract class Surface extends Object {
	public Material[] faces;
	public Surface() {
		faces = new Material[2];
		faces[0] = new Material();
		faces[0].diffuse = new Color(0.1, 0.1, 0.6);
		faces[1] = new Material();
		faces[1].diffuse = new Color(0.1, 0.6, 0.1);
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
	public Material[] getMaterials() {
		return faces;
	}
	public void setMaterial(Material[] ms) {
		faces = ms;
	}
}
