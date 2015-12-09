package object;

import matrix.Vector;

public class RotatedBezier extends Surface {
	private double[] data;
	static private class Vec2 {
		public double x, y;
	}
	public RotatedBezier(double[] pts) {
		data = pts;
	}
	private Vec2 getBezierPoint(double t) {
		Vec2 p = new Vec2();
		p.x = 
		  t*t*t*data[2*0 + 0] + 3*t*t*(1 - t)*data[2*1 + 0] + 
		  3*t*(1 - t)*(1 - t)*data[2*2 + 0] + (1 - t)*(1 - t)*(1 - t)*data[2*3 + 0];
		p.y =
		  t*t*t*data[2*0 + 1] + 3*t*t*(1 - t)*data[2*1 + 1] + 
		  3*t*(1 - t)*(1 - t)*data[2*2 + 1] + (1 - t)*(1 - t)*(1 - t)*data[2*3 + 1];
		return p;
	}
	@Override
	public Vector getModelPoint(double u, double v) {
		double phi = 2*Math.PI*u;
		Vec2 p = getBezierPoint(v);
		return new Vector(
		  p.y*Math.sin(phi),
		  p.y*Math.cos(phi),
		  p.x
		  );
	}
}
