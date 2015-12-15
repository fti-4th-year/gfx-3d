package vector;

public class Matrix {
	public double[] data = {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
	public Matrix() {
		
	}
	public void multiply(Vector v) {
		Vector nv = multiply(this, v);
		v.set(nv);
	}
	static public Vector multiply(Matrix m, Vector v) {
		Vector nv = new Vector();
		for(int i = 0; i < 4; ++i) {
			nv.data[i] = 0.0;
			for(int j = 0; j < 4; ++j) {
				nv.data[i] += m.data[i*4 + j]*v.data[j];
			}
		}
		return nv;
	}
}
