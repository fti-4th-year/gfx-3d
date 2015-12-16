package graphics.primitive;

import graphics.Material;
import util.Color;
import vector.Vector;

public class Fragment {
	public Color color;
	public double depth, new_depth;
	public Material material;
	public Vector view_pos;
	public Vector view_norm;
}
