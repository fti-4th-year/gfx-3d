package object;

import vector.Vector;

public class Sphere extends Surface {
	@Override
	public Vector getModelPoint(double u, double v) {
		double phi = 2*Math.PI*u, theta = Math.PI*(v - 0.5);
		return new Vector(
		  Math.sin(phi)*Math.cos(theta),
		  Math.cos(phi)*Math.cos(theta),
		  Math.sin(theta)
		  );
	}
	@Override
	public Vector getModelNorm(double u, double v) {
		double phi = 2*Math.PI*u, theta = Math.PI*(v - 0.5);
		return new Vector(
		  Math.sin(phi)*Math.cos(theta),
		  Math.cos(phi)*Math.cos(theta),
		  Math.sin(theta)
		  );
	}
}
