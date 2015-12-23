package graphics;

import java.util.ArrayList;

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
	private ArrayList<Emitter> emitters;
	
	Triangulator triangulator;
	
	public Pipeline() {
		camera = new Camera();
		projector = new Projector();
		
		triangulator = new Triangulator(32, 32);
		
		ambient = new Color(0.2, 0.2, 0.2);
		emitters = new ArrayList<Emitter>();
		emitters.add(new Emitter(new Color(4, 4, 6), new Vector(0, 0, 4)));
		emitters.add(new Emitter(new Color(4, 4, 2), new Vector(0, 4, 0)));
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
	public Triangulator getTriangulator() {
		return triangulator;
	}
	public void draw(Surface[] surfaces, FrameBuffer framebuffer) {
		FragmentShader shader = new FragmentShader(ambient, emitters.toArray(new Emitter[0]), camera.getViewMatrix());
		for(int is = 0; is < surfaces.length; ++is) {
			Surface s = surfaces[is];
			Triangle[] triangles = triangulator.triangulate(s);
			for(int it = 0; it < triangles.length; ++it) {
				Triangle t = triangles[it];
				Primitive p = t.transform(camera.getViewMatrix());
				Primitive[] ps = clip(p);
				for(int ic = 0; ic < ps.length; ++ic) {
					Projection pr = ps[ic].project(projector.getProjMatrix());
					pr.rasterize(framebuffer, shader);
				}
			}
		}
	}
	private Primitive[] clip(Primitive p) {
		return p.clip(new Vector(0, 0, -projector.n), new Vector(0, 0, 1));
	}
	public void setAmbient(Color c) {
		ambient = c;
	}
	public void addEmitter(Emitter em) {
		emitters.add(em);
	}
	public void removeEmitter() {
		if(emitters.size() > 0) {
			emitters.remove(emitters.size() - 1);
		}
	}
	public Emitter getLastEmitter() {
		if(emitters.size() > 0) {
			return emitters.get(emitters.size() - 1);
		}
		return null;
	}
	public int getEmitterCount() {
		return emitters.size();
	}
}
