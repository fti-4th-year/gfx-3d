package graphics.primitive;

import util.Color;
import vector.Vector;

public class Emitter {
	public Color color;
	public Vector pos;
	public Emitter() {
		color = new Color();
		pos = new Vector();
	}
	public Emitter(Color c, Vector p) {
		color = c;
		pos = p;
	}
	public Emitter(Emitter emitter) {
		color = new Color(emitter.color);
		pos = new Vector(emitter.pos);
	}
}
