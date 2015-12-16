import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import graphics.FrameBuffer;
import graphics.Pipeline;
import graphics.Projector;

import javax.swing.JPanel;

import object.Surface;
import vector.Vector;


public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	Pipeline pipeline;
	FrameBuffer framebuffer;
	
	private ArrayList<Surface> surfaces;
	
	double phi = 0.0, theta = Math.PI/4, radius = 2.0;
	double delta = 1e-2, sense = 1e-2, rspd = 0.1;
	Point mouse;
	
	public MainPanel(int w, int h) {
		super();
		
		pipeline = new Pipeline();
		framebuffer = new FrameBuffer(w, h);
		
		surfaces = new ArrayList<Surface>();
		setPreferredSize(new Dimension(w, h));
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
	
	@Override
	protected void paintComponent(Graphics g) {
		Projector proj = pipeline.getProjector();
		proj.w = (proj.h*getWidth())/getHeight();
		proj.update();
		
		Vector pos = new Vector(
		  radius*Math.cos(phi)*Math.cos(theta), 
		  radius*Math.sin(phi)*Math.cos(theta), 
		  radius*Math.sin(theta)
		  );
		pipeline.getCamera().lookFrom(pos);
		
		super.paintComponent(g);
		framebuffer.clear();
		
		pipeline.draw(surfaces.toArray(new Surface[1]), framebuffer);
		
		g.drawImage(framebuffer.getColorBuffer(), 0, 0, null);
	}
	
	public Pipeline getPipeline() {
		return pipeline;
	}
}
