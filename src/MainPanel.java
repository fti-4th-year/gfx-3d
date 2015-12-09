import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import object.Surface;

import matrix.Vector;


public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
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
		int b = 10;
		return new Point(
		  (int) (0.5*p.data[0]*(getWidth() - b) + 0.5*getWidth()), 
		  (int) (0.5*p.data[1]*(getHeight() - b) + 0.5*getHeight())
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
		g.setColor(Color.BLACK);
		for(int k = 0; k < surfaces.size(); ++k) {
			Surface surface = surfaces.get(k);
			int n = 16, m = 16;
			for(int i = 0; i <= n; ++i) {
				for(int j = 0; j < m; ++j) {
					double u = (double) i/n;
					double v = (double) j/m;
					double vn = (double) (j + 1)/m;
					drawLine(g, surface.getPoint(u, v), surface.getPoint(u, vn));
				}
			}
			for(int j = 0; j <= m; ++j) {
				for(int i = 0; i < n; ++i) {
					double u = (double) i/n;
					double v = (double) j/m;
					double un = (double) (i + 1)/n;
					drawLine(g, surface.getPoint(u, v), surface.getPoint(un, v));
				}
			}
		}
	}
}
