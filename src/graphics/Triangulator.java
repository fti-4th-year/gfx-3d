package graphics;

import object.Surface;
import vector.Vector;
import graphics.primitive.Triangle;

public class Triangulator {
	private int n, m;
	public Triangulator(int n, int m) {
		this.n = n;
		this.m = m;
	}
	public Triangle[] triangulate(Surface surface) {
		Vector[] vs = new Vector[3];
		Vector[] ns = new Vector[3];
		
		Triangle[] array = new Triangle[2*(n + 1)*(m + 1)];
		int count = 0;
		
		Material mat = surface.getMaterial();
		
		for(int i = 0; i <= n; ++i) {
			for(int j = 0; j <= m; ++j) {
				Triangle t;
				
				double u = (double) i/n;
				double v = (double) j/m;
				double un = (double) (i + 1)/n;
				double vn = (double) (j + 1)/m;
				
				vs[0] = surface.getPoint(u, v);
				vs[1] = surface.getPoint(un, v);
				vs[2] = surface.getPoint(un, vn);
				ns[0] = surface.getNorm(u, v);
				ns[1] = surface.getNorm(un, v);
				ns[2] = surface.getNorm(un, vn);
				t = new Triangle(vs, ns, mat);
				array[count++] = t;
				
				vs[0] = surface.getPoint(un, vn);
				vs[1] = surface.getPoint(u, vn);
				vs[2] = surface.getPoint(u, v);
				ns[0] = surface.getNorm(un, vn);
				ns[1] = surface.getNorm(u, vn);
				ns[2] = surface.getNorm(u, v);
				t = new Triangle(vs, ns, mat);
				array[count++] = t;
			}
		}
		
		return array;
	}
}
