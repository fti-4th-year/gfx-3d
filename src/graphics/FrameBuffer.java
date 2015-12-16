package graphics;

import java.awt.image.BufferedImage;

public class FrameBuffer {
	private BufferedImage colorBuffer;
	private double[] depthBuffer;
	int width, height;
	
	public FrameBuffer(int width, int height) {
		resize(width, height);
	}
	
	public void resize(int w, int h) {
		this.width = w;
		this.height = h;
		colorBuffer = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		depthBuffer = new double[w*h];
	}
	
	public void clear() {
		for(int iy = 0; iy < height; ++iy) {
			for(int ix = 0; ix < width; ++ix) {
				colorBuffer.setRGB(ix, iy, 0);
				depthBuffer[iy*width + ix] = 1.0;
			}
		}
	}
	
	public BufferedImage getColorBuffer() {
		return colorBuffer;
	}
	
	public double[] getDepthBuffer() {
		return depthBuffer;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
