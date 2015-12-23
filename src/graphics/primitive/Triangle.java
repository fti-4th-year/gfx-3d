package graphics.primitive;

import graphics.Material;
import vector.Matrix;
import vector.Vector;

public class Triangle implements Primitive {
	public Vector[] vertices;
	public Vector[] normals;
	public Material[] faces;
	public Triangle() {
		this.vertices = new Vector[3];
		this.normals = new Vector[3];
		for(int i = 0; i < 3; ++i) {
			this.vertices[i] = new Vector();
			this.normals[i] = new Vector();
		}
	}
	public Triangle(Vector[] vertices, Vector[] normals, Material[] ms) {
		this();
		for(int i = 0; i < 3; ++i) {
			this.vertices[i].set(vertices[i]);
			this.normals[i].set(normals[i]);
		}
		faces = ms;
	}
	public Triangle(Triangle triangle) {
		this(triangle.vertices, triangle.normals, triangle.faces);
	}
	@Override
	public Primitive[] clip(Vector pos, Vector norm) {
		Triangle[] tri = new Triangle[0];
		int out = 0;
		int[] idx = {-1, -1, -1};
		for(int i = 0; i < 3; ++i) {
			if(Vector.dot(Vector.subtract(vertices[i], pos), norm) < 0.0) {
				idx[out] = i;
				out += 1;
			}
		}
		if(out == 0) {
			tri = new Triangle[1];
			tri[0] = this;
		} else if(out == 1 || out == 2) {
			Vector from = null;
			Vector[] to = new Vector[2];
			int[] vidx = new int[3];
			if(out == 1) {
				vidx[0] = idx[0];
				vidx[1] = (idx[0] + 1) % 3;
				vidx[2] = (idx[0] + 2) % 3;
			} else if(out == 2) {
				vidx[1] = idx[0];
				vidx[2] = idx[1];
				for(int i = 0; i < 3; ++i) {
					if(i != idx[0] && i != idx[1])
						vidx[0] = i;
				}
			}
			from = vertices[vidx[0]];
			to[0] = vertices[vidx[1]];
			to[1] = vertices[vidx[2]];
			
			Vector[] middle = new Vector[2];
			Vector[] mnorm = new Vector[2];
			for(int i = 0; i < 2; ++i) {
				Vector path = Vector.subtract(to[i], from);
				double f = 
						Vector.dot(Vector.subtract(pos, from), norm) /
						Vector.dot(path, norm);
				middle[i] = Vector.add(Vector.multiply(path, f), from);
				mnorm[i] = Vector.add(Vector.multiply(normals[vidx[0]], 1.0 - f), Vector.multiply(normals[vidx[1 + i]], f));
				mnorm[i].normalize();
			}
			
			if(out == 1) {
				tri = new Triangle[2];
				Vector[] va0 = {middle[0], to[0], to[1]};
				Vector[] na0 = {mnorm[0], normals[vidx[1]], normals[vidx[2]]};
				tri[0] = new Triangle(va0, na0, faces);
				Vector[] va1 = {middle[1], middle[0], to[1]};
				Vector[] na1 = {mnorm[1], mnorm[0], normals[vidx[2]]};
				tri[1] = new Triangle(va1, na1, faces);
			} else if(out == 2) {
				tri = new Triangle[1];
				Vector[] va = {from, middle[0], middle[1]};
				Vector[] na = {normals[vidx[0]], mnorm[0], mnorm[1]};
				tri[0] = new Triangle(va, na, faces);
			}
		}
		return tri;
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
		out.setMaterials(faces);
		return out;
	}
	public void setMaterials(Material[] ms) {
		faces = ms;
	}
	@Override
	public Material[] getMaterials() {
		return faces;
	}
}
