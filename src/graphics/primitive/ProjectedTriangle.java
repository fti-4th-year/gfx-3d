package graphics.primitive;

import java.awt.Point;
import java.awt.image.BufferedImage;

import util.Color;
import util.FPoint;
import vector.Vector;
import graphics.FrameBuffer;
import graphics.Material;
import graphics.Shader;

public class ProjectedTriangle implements Projection {
	private Vector[] viewVertices;
	private Vector[] viewNormals;
	public Vector[] vertices;
	public Material face;
	public ProjectedTriangle(Vector[] vertices, Vector[] viewVertices, Vector[] viewNormals) {
		this.vertices = new Vector[3];
		this.viewVertices = new Vector[3];
		this.viewNormals = new Vector[3];
		for(int i = 0; i < 3; ++i) {
			this.vertices[i] = new Vector(vertices[i]);
			this.viewVertices[i] = new Vector(viewVertices[i]);
			this.viewNormals[i] = new Vector(viewNormals[i]);
		}
	}
	@Override
	public Projection[] clip(Vector pos, Vector norm) {
		// TODO add clipping
		ProjectedTriangle[] same = new ProjectedTriangle[1];
		same[0] = this;
		return same;
	}
	Point PtoS(Vector p, FrameBuffer fb) {
		FPoint pf = PtoSf(p, fb);
		return new Point((int) Math.round(pf.x), (int) Math.round(pf.y));
	}
	FPoint PtoSf(Vector p, FrameBuffer fb) {
		int b = 10;
		return new FPoint(
		  0.5*p.data[0]*(fb.getWidth() - b) + 0.5*fb.getWidth(), 
		  0.5*p.data[1]*(fb.getHeight() - b) + 0.5*fb.getHeight()
		  );
	}
	@Override
	public void rasterize(FrameBuffer fb, Shader<Fragment> sh) {
		BufferedImage image = fb.getColorBuffer();
		double[] depth = fb.getDepthBuffer();
		int width = fb.getWidth(), height = fb.getHeight();
		
		// Project
		FPoint[] ps = new FPoint[3];
		double[] ds = new double[3];
		for(int i = 0; i < 3; ++i) {
			ps[i] = PtoSf(vertices[i], fb);
			ds[i] = vertices[i].data[2];
		}
		
		// Bounding box
		int
		x0 = (int) Math.floor(ps[0].x), 
		y0 = (int) Math.floor(ps[0].y), 
		x1 = (int) Math.ceil(ps[0].x), 
		y1 = (int) Math.ceil(ps[0].y);
		for(int i = 1; i < 3; ++i) {
			int
			lx0 = (int) Math.floor(ps[i].x), 
			ly0 = (int) Math.floor(ps[i].y), 
			lx1 = (int) Math.ceil(ps[i].x), 
			ly1 = (int) Math.ceil(ps[i].y);
			if(lx0 < x0)
				x0 = lx0;
			if(ly0 < y0)
				y0 = ly0;
			if(lx1 > x1)
				x1 = lx1;
			if(ly1 > y1)
				y1 = ly1;
		}
		if(x0 < 0)
			x0 = 0;
		if(y0 < 0)
			y0 = 0;
		if(x1 >= width)
			x1 = width - 1;
		if(y1 >= height)
			y1 = height - 1;
		
		// Coordinates
		double[] cs = new double[3];
		for(int iy = y0; iy <= y1; ++iy) {
			for(int ix = x0; ix <= x1; ++ix) {
				// Barycentric
				FPoint p = new FPoint((double) ix, (double) iy);
				FPoint[] dps = new FPoint[3];
				dps[0] = new FPoint(FPoint.sub(ps[1], ps[0]));
				dps[1] = new FPoint(FPoint.sub(ps[2], ps[0]));
				dps[2] = new FPoint(FPoint.sub(p, ps[0]));
				double d00 = FPoint.dot(dps[0], dps[0]);
				double d01 = FPoint.dot(dps[0], dps[1]);
				double d11 = FPoint.dot(dps[1], dps[1]);
				double d20 = FPoint.dot(dps[2], dps[0]);
				double d21 = FPoint.dot(dps[2], dps[1]);
				double den = d00*d11 - d01*d01;
				cs[1] = (d11*d20 - d01*d21)/den;
				cs[2] = (d00*d21 - d01*d20)/den;
				cs[0] = 1.0 - cs[1] - cs[2];
				if(cs[0] < 0.0 && cs[0] > -1e-3)
					cs[0] = 0.0;
				
				if(cs[0] >= 0 && cs[0] <= 1 && cs[1] >= 0 && cs[1] <= 1 && cs[2] >= 0 && cs[2] <= 1) {
					double dpt = ds[0]*cs[0] + ds[1]*cs[1] + ds[2]*cs[2];
					Vector n = new Vector(0, 0, 0);
					Vector v = new Vector(0, 0, 0);
					for(int i = 0; i < 3; ++i) {
						v.add(Vector.multiply(viewVertices[i], cs[i]));
						n.add(Vector.multiply(viewNormals[i], cs[i]));
					}
					n.normalize();
					
					Fragment frag = new Fragment();
					frag.color = new Color(image.getRGB(ix, iy));
					frag.depth = depth[iy*width + ix];
					frag.new_depth = dpt;
					frag.view_norm = n;
					frag.view_pos = v;
					frag.material = face;
					
					sh.evaluate(frag);
					
					depth[iy*width + ix] = frag.depth;
					image.setRGB(ix, iy, frag.color.getInt());
				}
			}
		}
	}
	public void setMaterial(Material m) {
		face = m;
	}
	@Override
	public Material getMaterial() {
		return face;
	}
}
