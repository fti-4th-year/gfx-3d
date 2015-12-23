import graphics.Pipeline;
import graphics.Projector;
import graphics.Triangulator;
import graphics.primitive.Emitter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import object.RotatedBezier;
import object.Surface;
import vector.Vector;


public class SidePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private MainPanel mainPanel;
	private Pipeline pipeline;
	private Projector proj;
	private Triangulator tria;
	
	public SidePanel(MainPanel mp) {
		this.mainPanel = mp;
		pipeline = mp.getPipeline();
		proj = pipeline.getProjector();
		tria = pipeline.getTriangulator();
		
		setPreferredSize(new Dimension(100,0));
		JPanel panel;
		JSpinner spinner;
		JButton button;
		
		panel = new JPanel();
		panel.add(new JLabel("sn:"));
		spinner = new JSpinner(new SpinnerNumberModel(tria.n, 1, 100, 1));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				tria.n = ((Integer) ((JSpinner)e.getSource()).getValue()).intValue();
				mainPanel.repaint();
			}
		});
		panel.add(spinner);
		add(panel);
		
		panel = new JPanel();
		panel.add(new JLabel("sm:"));
		spinner = new JSpinner(new SpinnerNumberModel(tria.m, 1, 100, 1));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				tria.m = ((Integer) ((JSpinner)e.getSource()).getValue()).intValue();
				mainPanel.repaint();
			}
		});
		panel.add(spinner);
		add(panel);
		
		panel = new JPanel();
		panel.add(new JLabel("h:"));
		spinner = new JSpinner(new SpinnerNumberModel(proj.h, 0.1, 1.0, 0.1));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				proj.h = ((Double) ((JSpinner)e.getSource()).getValue()).doubleValue();
				proj.update();
				mainPanel.repaint();
			}
		});
		panel.add(spinner);
		add(panel);
		
		panel = new JPanel();
		panel.add(new JLabel("n:"));
		spinner = new JSpinner(new SpinnerNumberModel(-proj.n, 0.1, 1.0, 0.1));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				proj.n = -((Double) ((JSpinner)e.getSource()).getValue()).doubleValue();
				proj.update();
				mainPanel.repaint();
			}
		});
		panel.add(spinner);
		add(panel);
		
		panel = new JPanel();
		panel.add(new JLabel("f:"));
		spinner = new JSpinner(new SpinnerNumberModel(-proj.f, 10.0, 990.0, 10.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				proj.f = -((Double) ((JSpinner)e.getSource()).getValue()).doubleValue();
				proj.update();
				mainPanel.repaint();
			}
		});
		panel.add(spinner);
		add(panel);
		
		add(new JLabel("Surfaces: "));
		
		button = new JButton("Add");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EditorPanel ep = new EditorPanel();
				int ok = JOptionPane.showOptionDialog(
				  null, ep, "Editor", JOptionPane.OK_CANCEL_OPTION, 
				  JOptionPane.PLAIN_MESSAGE, null, null, null);
				if(ok == JOptionPane.OK_OPTION) {
					mainPanel.addSurface(new RotatedBezier(ep.data));
					mainPanel.repaint();
				}
			}
		});
		add(button);
		
		button = new JButton("Remove");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(mainPanel.getSurfaceCount() > 1) {
					mainPanel.removeSurface();
					mainPanel.repaint();
				}
			}
		});
		add(button);
		
		button = new JButton("Move");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final Surface last = mainPanel.getLastSurface();
				Vector oc = new Vector(
				  last.getModelMatrix().data[3], 
				  last.getModelMatrix().data[7], 
				  last.getModelMatrix().data[11]
				  );
				Vector nc = new Vector(oc);
				PositionPanel pp = new PositionPanel(nc, new PositionPanel.Callback() {
					@Override
					public void change(Vector c) {
						last.getModelMatrix().data[3] = c.data[0];
						last.getModelMatrix().data[7] = c.data[1];
						last.getModelMatrix().data[11] = c.data[2];
						mainPanel.repaint();
					}
				});
				int ok = JOptionPane.showOptionDialog(
				  null, pp, "Set position", JOptionPane.OK_CANCEL_OPTION, 
				  JOptionPane.PLAIN_MESSAGE, null, null, null);
				if(ok != JOptionPane.OK_OPTION) {
					last.getModelMatrix().data[3] = oc.data[0];
					last.getModelMatrix().data[7] = oc.data[1];
					last.getModelMatrix().data[11] = oc.data[2];
					mainPanel.repaint();
				}
			}
		});
		add(button);
		
		button = new JButton("Material");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Surface last = mainPanel.getLastSurface();
				MaterialPanel pp = new MaterialPanel(mainPanel, last);
				JOptionPane.showOptionDialog(
				  null, pp, "Set material", JOptionPane.DEFAULT_OPTION, 
				  JOptionPane.PLAIN_MESSAGE, null, null, null);
			}
		});
		add(button);
		
		add(new JLabel("Emitters: "));
		
		button = new JButton("Add");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pipeline.addEmitter(new Emitter());
				mainPanel.repaint();
			}
		});
		add(button);
		
		button = new JButton("Remove");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(pipeline.getEmitterCount() > 1) {
					pipeline.removeEmitter();
					mainPanel.repaint();
				}
			}
		});
		add(button);
		
		button = new JButton("Move");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Emitter last = pipeline.getLastEmitter();
				Vector oc = last.pos;
				Vector nc = new Vector(oc);
				last.pos = nc;
				PositionPanel pp = new PositionPanel(nc, new PositionPanel.Callback() {
					@Override public void change(Vector c) {
						mainPanel.repaint();
					}
				});
				int ok = JOptionPane.showOptionDialog(
				  null, pp, "Set position", JOptionPane.OK_CANCEL_OPTION, 
				  JOptionPane.PLAIN_MESSAGE, null, null, null);
				if(ok != JOptionPane.OK_OPTION) {
					last.pos = oc;
					mainPanel.repaint();
				}
			}
		});
		add(button);
		
		button = new JButton("Color");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				final Emitter last = pipeline.getLastEmitter();
				
				JSpinner spinner;
				JPanel cc = new JPanel();
				cc.add(new JLabel("Color: "));
				spinner = new JSpinner(new SpinnerNumberModel(last.color.r, 0.0, 100.0, 1.0));
				spinner.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						last.color.r = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue();
						mainPanel.repaint();
					}
				});
				cc.add(spinner);
				spinner = new JSpinner(new SpinnerNumberModel(last.color.g, 0.0, 100.0, 1.0));
				spinner.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						last.color.g = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue();
						mainPanel.repaint();
					}
				});
				cc.add(spinner);
				spinner = new JSpinner(new SpinnerNumberModel(last.color.b, 0.0, 100.0, 1.0));
				spinner.addChangeListener(new ChangeListener() {
					@Override
					public void stateChanged(ChangeEvent e) {
						last.color.b = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue();
						mainPanel.repaint();
					}
				});
				cc.add(spinner);
				
				JOptionPane.showOptionDialog(
				  null, cc, "Set position", JOptionPane.DEFAULT_OPTION, 
				  JOptionPane.PLAIN_MESSAGE, null, null, null);
			}
		});
		add(button);
	}
}
