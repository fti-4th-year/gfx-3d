package util;

public class FPoint {
	public double x, y;
	public FPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}
	public FPoint(FPoint pt) {
		x = pt.x;
		y = pt.y;
	}
	public double length() {
		return Math.sqrt(x*x + y*y);
	}
	public FPoint normalize() {
		double l = length();
		return new FPoint(x/l, y/l);
	}
	public static FPoint mul(FPoint a, double b) {
		return new FPoint(a.x*b, a.y*b);
	}
	public static FPoint add(FPoint a, FPoint b) {
		return new FPoint(a.x + b.x, a.y + b.y);
	}
	public static FPoint sub(FPoint a, FPoint b) {
		return new FPoint(a.x - b.x, a.y - b.y);
	}
	public static double dot(FPoint a, FPoint b) {
		return a.x*b.x + a.y*b.y;
	}
	public static double cross(FPoint a, FPoint b) {
		return a.x*b.y - a.y*b.x;
	}
}