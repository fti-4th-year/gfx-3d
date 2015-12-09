import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import object.Surface;


public class PositionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	JPanel panel;
	Surface surface;
	public PositionPanel(JPanel p, Surface s, double[] c) {
		panel = p;
		surface = s;
		
		JSpinner spinner;
		
		spinner = new JSpinner(new SpinnerNumberModel(c[0], -100.0, 100.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getModelMatrix().data[3] = ((Double) ((JSpinner)e.getSource()).getValue()).doubleValue();
				panel.repaint();
			}
		});
		add(spinner);
		
		spinner = new JSpinner(new SpinnerNumberModel(c[1], -100.0, 100.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getModelMatrix().data[7] = ((Double) ((JSpinner)e.getSource()).getValue()).doubleValue();
				panel.repaint();
			}
		});
		add(spinner);
		
		spinner = new JSpinner(new SpinnerNumberModel(c[2], -100.0, 100.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				surface.getModelMatrix().data[11] = ((Double) ((JSpinner)e.getSource()).getValue()).doubleValue();
				panel.repaint();
			}
		});
		add(spinner);
	}
}
