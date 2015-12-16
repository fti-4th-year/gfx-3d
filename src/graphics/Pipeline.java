package graphics;

import graphics.primitive.Emitter;
import graphics.primitive.Primitive;
import graphics.primitive.Projection;
import graphics.primitive.Triangle;
import object.Surface;
import util.Color;
import vector.Vector;

public class Pipeline {
	private Camera camera;
	private Projector projector;
	
	private Color ambient;
	private Emitter[] emitters;
	
	Triangulator triangulator;
	
	public Pipeline() {
		camera = new Camera();
		projector = new Projector();
		
		triangulator = new Triangulator(32, 32);
		
		ambient = new Color(0.2, 0.2, 0.2);
		emitters = new Emitter[2];
		emitters[0] = new Emitter(new Color(4, 4, 6), new Vector(0, 0, 4));
		emitters[1] = new Emitter(new Color(4, 4, 2), new Vector(0, 4, 0));
	}
	
	public Camera getCamera() {
		return camera;
	}
	public void setCamera(Camera camera) {
		this.camera = camera;
	}
	public Projector getProjector() {
		return projector;
	}
	public void setProjector(Projector projector) {
		this.projector = projector;
	}
	public void draw(Surface[] surfaces, FrameBuffer framebuffer) {
		FragmentShader shader = new FragmentShader(ambient, emitters, camera.getViewMatrix());
		for(int is = 0; is < surfaces.length; ++is) {
			Surface s = surfaces[is];
			Triangle[] triangles = triangulator.triangulate(s);
			for(int it = 0; it < triangles.length; ++it) {
				Triangle t = triangles[it];
				Primitive p = t.transform(camera.getViewMatrix());
				Projection pr = p.project(projector.getProjMatrix());
				pr.rasterize(framebuffer, shader);
			}
		}
	}
}
