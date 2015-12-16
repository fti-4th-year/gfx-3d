package util;

public class Color {
	public final static Color 
	  BLACK = new Color(0,0,0),
	  WHITE = new Color(1,1,1);
	
	public double r, g, b;
	
	public Color() {
		set(0.0, 0.0, 0.0);
	}
	public Color(double ar, double ag, double ab) {
		set(ar, ag, ab);
	}
	public Color(int ic) {
		setInt(ic);
	}
	public Color(Color c) {
		copy(c);
	}
	
	private static double clamp(double a) {
		return Math.max(Math.min(a, 1.0), 0.0);
	}
	
	public void set(double ar, double ag, double ab) {
		r = ar;
		g = ag;
		b = ab;
	}
	public void copy(Color c) {
		r = c.r;
		g = c.g;
		b = c.b;
	}
	public void clear() {
		r = 0.0;
		g = 0.0;
		b = 0.0;
	}
	
	public void setInt(int ic) {
		r = (double) ((ic >> 2*8) & 0xFF)/0xFF;
		g = (double) ((ic >> 1*8) & 0xFF)/0xFF;
		b = (double) ((ic >> 0*8) & 0xFF)/0xFF;
	}
	public int getInt() {
		return ((int) (clamp(b)*0xFF) << 0*8) | ((int) (clamp(g)*0xFF) << 1*8) | ((int) (clamp(r)*0xFF) << 2*8) | (0xFF << 3*8);
	}
	
	public Color add(Color c) {
		r += c.r;
		g += c.g;
		b += c.b;
		return this;
	}
	public Color subtract(Color c) {
		r -= c.r;
		g -= c.g;
		b -= c.b;
		return this;
	}
	public Color multiply(double f) {
		r *= f;
		g *= f;
		b *= f;
		return this;
	}
	public Color multiply(Color c) {
		r *= c.r;
		g *= c.g;
		b *= c.b;
		return this;
	}
	
	public void clamp() {
		if(r < 0.0)
			r = 0.0;
		if(r > 1.0)
			r = 1.0;
		if(g < 0.0)
			g = 0.0;
		if(g > 1.0)
			g = 1.0;
		if(b < 0.0)
			b = 0.0;
		if(b > 1.0)
			b = 1.0;
	}
}
