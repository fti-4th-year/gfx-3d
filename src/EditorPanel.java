import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;


public class EditorPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private int pos = 0;
	public double[] data = {0, 0, 0, 0, 0, 0, 0, 0};
	public EditorPanel() {
		setPreferredSize(new Dimension(480, 240));
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				super.mouseClicked(e);
				data[2*pos + 0] = 2.0*((double) e.getX()/getWidth() - 0.5);
				data[2*pos + 1] = (double) e.getY()/getHeight();
				pos += 1;
				if(pos >= 4) {
					pos = 0;
				}
				repaint();
			}
		});
	}
	
	private Point getBezierPoint(double t) {
		Point p = new Point();
		p.x = (int) ((
		  0.5*(t*t*t*data[2*0 + 0] + 3*t*t*(1 - t)*data[2*1 + 0] + 
		  3*t*(1 - t)*(1 - t)*data[2*2 + 0] + (1 - t)*(1 - t)*(1 - t)*data[2*3 + 0])
		  + 0.5)*getWidth());
		p.y = (int) ((
		  t*t*t*data[2*0 + 1] + 3*t*t*(1 - t)*data[2*1 + 1] + 
		  3*t*(1 - t)*(1 - t)*data[2*2 + 1] + (1 - t)*(1 - t)*(1 - t)*data[2*3 + 1]
		  )*getHeight());
		return p;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		int w = 2, h = 2;
		for(int i = 0; i < 4; ++i) {
			g.drawRect((int) ((data[2*i + 0] + 1)*0.5*getWidth()) - w, (int) (data[2*i + 1]*getHeight()) - h, 2*w, 2*h);
		}
		
		for(int i = 0; i < 3; ++i) {
			g.drawLine(
			  (int) ((0.5*data[2*i + 0] + 0.5)*getWidth()), (int) (data[2*i + 1]*getHeight()),
			  (int) ((0.5*data[2*(i + 1) + 0] + 0.5)*getWidth()), (int) (data[2*(i + 1) + 1]*getHeight())
			  );
		}
		
		int mi = 0x100;
		for(int i = 0; i < mi; ++i) {
			double t0 = (double) i/mi;
			double t1 = (double) (i + 1)/mi;
			Point p0, p1;
			p0 = getBezierPoint(t0);
			p1 = getBezierPoint(t1);
			g.drawLine(p0.x, p0.y, p1.x, p1.y);
		}
	}
}
