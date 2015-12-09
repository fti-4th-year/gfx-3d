package object;

import matrix.Vector;

public class Plane extends Surface {
	@Override
	public Vector getModelPoint(double u, double v) {
		return new Vector(u - 0.5, v - 0.5, 0.0);
	}
}
