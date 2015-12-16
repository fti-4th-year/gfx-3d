package graphics.primitive;

import graphics.Material;
import vector.Matrix;
import vector.Vector;

public class Triangle implements Primitive {
	public Vector[] vertices;
	public Vector[] normals;
	public Material face;
	public Triangle() {
		this.vertices = new Vector[3];
		this.normals = new Vector[3];
		for(int i = 0; i < 3; ++i) {
			this.vertices[i] = new Vector();
			this.normals[i] = new Vector();
		}
	}
	public Triangle(Vector[] vertices, Vector[] normals, Material m) {
		this();
		for(int i = 0; i < 3; ++i) {
			this.vertices[i].set(vertices[i]);
			this.normals[i].set(normals[i]);
		}
		face = m;
	}
	public Triangle(Triangle triangle) {
		this(triangle.vertices, triangle.normals, triangle.face);
	}
	@Override
	public Primitive[] clip(Vector pos, Vector norm) {
		// TODO add clipping
		Triangle[] same = new Triangle[1];
		same[0] = this;
		return same;
	}
	@Override
	public Primitive transform(Matrix map) {
		// TODO transform normals differently
		Triangle out = new Triangle(this);
		for(int i = 0; i < 3; ++i) {
			map.multiply(out.vertices[i]);
			map.multiply(out.normals[i]);
		}
		return out;
	}
	@Override
	public Projection project(Matrix proj) {
		ProjectedTriangle out = new ProjectedTriangle(vertices, vertices, normals);
		for(int i = 0; i < 3; ++i) {
			proj.multiply(out.vertices[i]);
			double divisor = 1.0/out.vertices[i].data[3];
			out.vertices[i].multiply(divisor);
		}
		out.setMaterial(face);
		return out;
	}
	public void setMaterial(Material m) {
		face = m;
	}
	@Override
	public Material getMaterial() {
		return face;
	}
}
