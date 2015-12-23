package graphics.primitive;

import vector.Vector;
import graphics.FrameBuffer;
import graphics.Material;
import graphics.Shader;

public interface Projection {
	public void rasterize(FrameBuffer framebuffer, Shader<Fragment> shader);
	public Projection[] clip(Vector pos, Vector norm);
	public Material[] getMaterials();
}
