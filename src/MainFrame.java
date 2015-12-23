import graphics.Material;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;


import object.Plane;
import object.Sphere;
import util.Color;
import vector.Matrix;


public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private MainPanel mainPanel;
	public MainFrame() {
		super();
		mainPanel = new MainPanel(800, 600);
		
		Matrix model;
		Material[] mats;
		
		Plane plane = new Plane();
		model = plane.getModelMatrix();
		model.data[0] = 2;
		model.data[5] = 2;
		model.data[11] = -0.5;
		mats = new Material[2];
		mats[1] = new Material();
		mats[0] = new Material();
		mats[0].diffuse = new Color(0.4, 0.4, 0.4);
		plane.setMaterial(mats);
		mainPanel.addSurface(plane);
		
		Sphere sphere = new Sphere();
		model = sphere.getModelMatrix();
		model.data[0] = 0.5;
		model.data[5] = 0.5;
		model.data[10] = 0.5;
		mats = new Material[2];
		mats[0] = new Material();
		mats[1] = new Material();
		mats[0].specFactor = 16;
		mats[1].specFactor = 16;
		sphere.setMaterial(mats);
		mainPanel.addSurface(sphere);
		
		add(mainPanel, BorderLayout.CENTER);
		
		add(new SidePanel(mainPanel), BorderLayout.LINE_END);
		pack();
		setTitle("3D");
		setMinimumSize(new Dimension(640, 480));
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		repaint();
	}
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				MainFrame frame = null;
				try {
					frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
