import java.awt.GridLayout;

import graphics.Material;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import object.Surface;


public class MaterialPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	JPanel panel;
	Surface surface;
	public MaterialPanel(JPanel p, Surface s) {
		panel = p;
		surface = s;
		
		Material[] ms = surface.getMaterials();
		
		setLayout(new GridLayout(0,1));
		
		JPanel pan;
		JSpinner spinner;
		
		pan = new JPanel();
		
		pan.add(new JLabel("Front face: "));
		
		pan.add(new JLabel("diffuse: {"));
		spinner = new JSpinner(new SpinnerNumberModel(10.0*ms[0].diffuse.r, 0.0, 10.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].diffuse.r = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		spinner = new JSpinner(new SpinnerNumberModel(10.0*ms[0].diffuse.g, 0.0, 10.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].diffuse.g = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		spinner = new JSpinner(new SpinnerNumberModel(10.0*ms[0].diffuse.b, 0.0, 10.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].diffuse.b = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		
		pan.add(new JLabel("}, specular: {"));
		spinner = new JSpinner(new SpinnerNumberModel(10.0*ms[0].specular.r, 0.0, 10.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].specular.r = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		spinner = new JSpinner(new SpinnerNumberModel(10.0*ms[0].specular.g, 0.0, 10.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].specular.g = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		spinner = new JSpinner(new SpinnerNumberModel(10.0*ms[0].specular.b, 0.0, 10.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].specular.b = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		pan.add(new JLabel("}, spec factor: "));
		
		spinner = new JSpinner(new SpinnerNumberModel(ms[0].specFactor, 1.0, 100.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].specFactor = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		
		add(pan);
		
		pan = new JPanel();
		
		pan.add(new JLabel("Back face: "));
		
		pan.add(new JLabel("diffuse: {"));
		spinner = new JSpinner(new SpinnerNumberModel(10.0*ms[1].diffuse.r, 0.0, 10.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].diffuse.r = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		spinner = new JSpinner(new SpinnerNumberModel(10.0*ms[1].diffuse.g, 0.0, 10.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].diffuse.g = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		spinner = new JSpinner(new SpinnerNumberModel(10.0*ms[1].diffuse.b, 0.0, 10.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].diffuse.b = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		
		pan.add(new JLabel("}, specular: {"));
		spinner = new JSpinner(new SpinnerNumberModel(10.0*ms[1].specular.r, 0.0, 10.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].specular.r = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		spinner = new JSpinner(new SpinnerNumberModel(10.0*ms[1].specular.g, 0.0, 10.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].specular.g = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		spinner = new JSpinner(new SpinnerNumberModel(10.0*ms[1].specular.b, 0.0, 10.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].specular.b = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		pan.add(new JLabel("}, spec factor: "));
		
		spinner = new JSpinner(new SpinnerNumberModel(ms[1].specFactor, 1.0, 100.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getMaterials()[0].specFactor = ((Double)((JSpinner) e.getSource()).getValue()).doubleValue()*0.1;
				panel.repaint();
			}
		});
		pan.add(spinner);
		
		add(pan);
	}
}
