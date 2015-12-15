import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.sql.PooledConnection;
import javax.swing.JPanel;

import object.Surface;
import util.FPoint;
import vector.Vector;



public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private BufferedImage image;
	private double[] depth;
	private int width, height;
	
	private ArrayList<Surface> surfaces;
	private Projector proj;
	private Camera camera;
	double phi = 0.0, theta = Math.PI/4, radius = 1.0;
	double delta = 1e-2, sense = 1e-2, rspd = 0.1;
	Point mouse;
	
	public MainPanel(int width, int height) {
		super();
		
		surfaces = new ArrayList<Surface>();
		setPreferredSize(new Dimension(width, height));
		addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseMoved(MouseEvent e) {
				Point newMouse = e.getPoint();
				mouse = newMouse;
			}
			@Override
			public void mouseDragged(MouseEvent e) {
				Point newMouse = e.getPoint();
				if(mouse != null) {
					phi += sense*(newMouse.x - mouse.x);
					theta += sense*(newMouse.y - mouse.y);
					double min_theta = -Math.PI/2 + delta;
					double max_theta = Math.PI/2 - delta;
					if(theta > max_theta) {
						theta = max_theta;
					} else if(theta < min_theta) {
						theta = min_theta;
					}
				}
				mouse = newMouse;
				repaint();
			}
		});
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				radius *= Math.exp(rspd*e.getWheelRotation());
				repaint();
			}
		});
		camera = new Camera();
		proj = new Projector();
		
		resizeBuffers(width, height);
	}
	
	void resizeBuffers(int w, int h) {
		this.width = w;
		this.height = h;
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		depth = new double[w*h];
	}
	
	void clearBuffers() {
		for(int iy = 0; iy < height; ++iy) {
			for(int ix = 0; ix < width; ++ix) {
				image.setRGB(ix, iy, 0);
				depth[iy*width + ix] = 1.0;
			}
		}
	}
	
	void addSurface(Surface surface) {
		surfaces.add(surface);
	}
	Surface removeSurface() {
		if(surfaces.isEmpty())
			return null;
		int last = surfaces.size() - 1;
		Surface surface = surfaces.get(last);
		surfaces.remove(last);
		return surface;
	}
	Surface getLastSurface() {
		if(surfaces.size() < 1) {
			return null;
		}
		return surfaces.get(surfaces.size() - 1);
	}
	public Projector getProjector() {
		return proj;
	}
	public void updateProjection() {
		proj.w = (proj.h*getWidth())/getHeight();
		proj.update();
	}
	public void project(Vector v) {
		proj.getProjMatrix().multiply(v);
		v.data[0] /= v.data[3];
		v.data[1] /= v.data[3];
		v.data[2] /= v.data[3];
	}
	Point PtoS(Vector p) {
		FPoint pf = PtoSf(p);
		return new Point((int) pf.x, (int) pf.y);
	}
	FPoint PtoSf(Vector p) {
		int b = 10;
		return new FPoint(
		  0.5*p.data[0]*(getWidth() - b) + 0.5*getWidth(), 
		  0.5*p.data[1]*(getHeight() - b) + 0.5*getHeight()
		  );
	}
	boolean clipPlane(Vector p0, Vector p1, int comp, double val, double sgn) {
		if((p0.data[comp] - val)*(p1.data[comp] - val) < 0.0) {
			if(sgn*(p0.data[comp] - val) > 0) {
				Vector pt = p0;
				p0 = p1;
				p1 = pt;
			}
			double f = (p0.data[comp] - val)/(p0.data[comp] - p1.data[comp]);
			for(int i = 0; i < 3; ++i) {
				p1.data[i] = p0.data[i]*(1.0 - f) + p1.data[i]*f;
			}
		} else {
			if(sgn*(p0.data[comp] - val) > 0) {
				return false;
			}
		}
		return true;
	}
	boolean clipProj(Vector p0, Vector p1) {
		return
		  clipPlane(p0, p1, 2, -1, -1) && // near
		  clipPlane(p0, p1, 2, 1, 1) && // far
		  clipPlane(p0, p1, 0, -1, -1) && // left
		  clipPlane(p0, p1, 0, 1, 1) && // right
		  clipPlane(p0, p1, 1, -1, -1) && // bottom
		  clipPlane(p0, p1, 1, 1, 1); // top
	}
	boolean clipView(Vector p0, Vector p1) {
		return clipPlane(p0, p1, 2, -proj.n, -1);
	}
	void drawLine(Graphics g, Vector from, Vector to) {
		Vector[] vs = {from, to};
		camera.getViewMatrix().multiply(vs[0]);
		camera.getViewMatrix().multiply(vs[1]);
		if(clipView(vs[0], vs[1])) {
			project(vs[0]);
			project(vs[1]);
			if(clipProj(vs[0], vs[1])) {
				Point[] ps = {PtoS(vs[0]), PtoS(vs[1])};
				g.drawLine(ps[0].x, ps[0].y, ps[1].x, ps[1].y);
			}
		}
	}
	void drawTriangle(Vector[] vs, Vector[] ns) {
		// Project
		FPoint[] ps = new FPoint[3];
		double[] ds = new double[3];
		for(int i = 0; i < 3; ++i) {
			camera.getViewMatrix().multiply(vs[i]);
			project(vs[i]);
			ds[i] = vs[i].data[2];
			ps[i] = PtoSf(vs[i]);
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
		if(x1 >= getWidth())
			x1 = getWidth() - 1;
		if(y1 >= getHeight())
			y1 = getHeight() - 1;
		// Coordinates
		double[] cs = new double[3];
		for(int iy = y0; iy <= y1; ++iy) {
			for(int ix = x0; ix <= x1; ++ix) {
				FPoint p = new FPoint((double) ix, (double) iy);
				for(int i = 0; i < 3; ++i) {
					FPoint dir = FPoint.sub(ps[(i + 1)%3], ps[i]);
					FPoint ort = FPoint.sub(ps[(i + 1)%3], ps[(i + 2)%3]).normalize();
					double len = FPoint.cross(ort, dir);
					FPoint dp = FPoint.sub(p, ps[i]);
					double cl = FPoint.cross(ort, dp);
					cs[i] = 1.0 - cl/len;
				}
				if(cs[0] >= 0 && cs[0] <= 1 && cs[1] >= 0 && cs[1] <= 1 && cs[2] >= 0 && cs[2] <= 1) {
					double dpt = ds[0]*cs[0] + ds[1]*cs[1] + ds[2]*cs[2];
					if(dpt < depth[iy*width + ix]) {
						depth[iy*width + ix] = dpt;
						Vector n = new Vector(0, 0, 0);
						n.add(Vector.multiply(ns[0], cs[0]));
						n.add(Vector.multiply(ns[1], cs[1]));
						n.add(Vector.multiply(ns[2], cs[2]));
						n.normalize();
						double comp = n.data[2];
						if(comp < 0.0)
							comp = 0.0;
						if(comp > 1.0)
							comp = 1.0;
						int color = (int) (0xff*comp) | (0xff << 24);
						image.setRGB(ix, iy, color);
					}
				}
			}
		}
	}
	@Override
	protected void paintComponent(Graphics g) {
		updateProjection();
		
		Vector pos = new Vector(
		  radius*Math.cos(phi)*Math.cos(theta), 
		  radius*Math.sin(phi)*Math.cos(theta), 
		  radius*Math.sin(theta)
		  );
		camera.lookFrom(pos);
		
		/*
		Vector cv = surfaces.get(0).getPoint(0.5, 0.5);
		camera.getViewMatrix().multiply(cv);
		project(cv);
		System.out.println("{" + cv.data[0] + ", " + cv.data[1] + ", " + cv.data[2] + "}");
		*/
		
		super.paintComponent(g);
		clearBuffers();
		
		g.setColor(Color.BLACK);
		Vector[] vs0 = new Vector[3];
		Vector[] vs1 = new Vector[3];
		Vector[] ns0 = new Vector[3];
		Vector[] ns1 = new Vector[3];
		for(int k = 0; k < surfaces.size(); ++k) {
			Surface surface = surfaces.get(k);
			int n = 16, m = 16;
			for(int i = 0; i <= n; ++i) {
				for(int j = 0; j < m; ++j) {
					double u = (double) i/n;
					double v = (double) j/m;
					double un = (double) (i + 1)/n;
					double vn = (double) (j + 1)/m;
					vs0[0] = surface.getPoint(u, v);
					vs0[1] = surface.getPoint(un, v);
					vs0[2] = surface.getPoint(un, vn);
					ns0[0] = surface.getNorm(u, v);
					ns0[1] = surface.getNorm(un, v);
					ns0[2] = surface.getNorm(un, vn);
					drawTriangle(vs0, ns0);
					vs1[0] = surface.getPoint(un, vn);
					vs1[1] = surface.getPoint(u, vn);
					vs1[2] = surface.getPoint(u, v);
					ns1[0] = surface.getNorm(un, vn);
					ns1[1] = surface.getNorm(u, vn);
					ns1[2] = surface.getNorm(u, v);
					drawTriangle(vs1, ns1);
				}
			}
		}
		g.drawImage(image, 0, 0, null);
	}
}
