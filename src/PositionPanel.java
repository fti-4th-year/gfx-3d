import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import vector.Vector;


public class PositionPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	public static interface Callback {
		public void change(Vector c);
	}
	Vector pos;
	Callback cb;
	public PositionPanel(Vector v, Callback c) {
		pos = v;
		cb = c;
		
		JSpinner spinner;
		
		spinner = new JSpinner(new SpinnerNumberModel(pos.data[0], -100.0, 100.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pos.data[0] = ((Double) ((JSpinner)e.getSource()).getValue()).doubleValue();
				cb.change(pos);
			}
		});
		add(spinner);
		
		spinner = new JSpinner(new SpinnerNumberModel(pos.data[1], -100.0, 100.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pos.data[1] = ((Double) ((JSpinner)e.getSource()).getValue()).doubleValue();
				cb.change(pos);
			}
		});
		add(spinner);
		
		spinner = new JSpinner(new SpinnerNumberModel(pos.data[2], -100.0, 100.0, 1.0));
		spinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				pos.data[2] = ((Double) ((JSpinner)e.getSource()).getValue()).doubleValue();
				cb.change(pos);
			}
		});
		add(spinner);
	}
}
