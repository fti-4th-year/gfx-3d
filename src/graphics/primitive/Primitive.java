package graphics.primitive;

import graphics.Material;
import vector.Matrix;
import vector.Vector;

public interface Primitive {
	public Primitive transform(Matrix map);
	public Projection project(Matrix proj);
	public Primitive[] clip(Vector pos, Vector norm);
	public Material[] getMaterials();
}
