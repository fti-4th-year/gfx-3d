package object;

import vector.Matrix;

public class Object {
	private Matrix model;
	public Object() {
		model = new Matrix();
	}
	public Matrix getModelMatrix() {
		return model;
	}
	public void setModelMatrix(Matrix model) {
		this.model = model;
	}
	
}
