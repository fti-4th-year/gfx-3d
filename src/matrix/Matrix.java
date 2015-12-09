package matrix;

public class Matrix {
	public double[] buffer = {0, 0, 0, 0};
	public double[] data = {1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1};
	public Matrix() {
		
	}
	public void multiply(Vector v) {
		for(int i = 0; i < 4; ++i) {
			buffer[i] = 0.0;
			for(int j = 0; j < 4; ++j) {
				buffer[i] += data[i*4 + j]*v.data[j];
			}
		}
		v.set(buffer);
	}
}
