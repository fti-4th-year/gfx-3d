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


public class SidePanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private MainPanel mainPanel;
	private Projector proj;
	public SidePanel(MainPanel mp) {
		this.mainPanel = mp;
		proj = mainPanel.getProjector();
		
		setPreferredSize(new Dimension(100,0));
		JPanel panel;
		JSpinner spinner;
		JButton button;
		
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
		
		button = new JButton("Move");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Surface last = mainPanel.getLastSurface();
				double[] c = {
				  last.getModelMatrix().data[3], 
				  last.getModelMatrix().data[7], 
				  last.getModelMatrix().data[11]
				  };
				PositionPanel pp = new PositionPanel(mainPanel, last, c);
				int ok = JOptionPane.showOptionDialog(
				  null, pp, "Set position", JOptionPane.OK_CANCEL_OPTION, 
				  JOptionPane.PLAIN_MESSAGE, null, null, null);
				if(ok != JOptionPane.OK_OPTION) {
					last.getModelMatrix().data[3] = c[0];
					last.getModelMatrix().data[7] = c[1];
					last.getModelMatrix().data[11] = c[2];
					mainPanel.repaint();
				}
			}
		});
		add(button);
	}
}
