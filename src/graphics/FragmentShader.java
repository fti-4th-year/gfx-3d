package graphics;

import util.Color;
import vector.Matrix;
import vector.Vector;
import graphics.primitive.Emitter;
import graphics.primitive.Fragment;

public class FragmentShader implements Shader<Fragment> {
	public Color ambient;
	public Emitter[] ems;
	public FragmentShader(Color amb, Emitter[] ems, Matrix m) {
		ambient = amb;
		this.ems = new Emitter[ems.length];
		for(int i = 0; i < ems.length; ++i) {
			this.ems[i] = new Emitter(ems[i]);
			m.multiply(this.ems[i].pos);
		}
	}
	@Override
	public void evaluate(Fragment frag) {
		if(frag.new_depth < frag.depth) {
			frag.depth = frag.new_depth;
			Color color = new Color();
			color.add(ambient);
			color.multiply(frag.material.diffuse);
			for(int i = 0; i < ems.length; ++i) {
				Vector ldir = Vector.subtract(ems[i].pos, frag.view_pos);
				double ldist = ldir.length();
				ldir.multiply(1.0/ldist);
				Color diff = new Color(ems[i].color);
				diff.multiply(frag.material.diffuse);
				double df = Vector.dot(frag.view_norm, ldir);
				if(df > 0.0) {
					df /= ldist;
					diff.multiply(df);
					color.add(diff);
				}
				Color spec = new Color(ems[i].color);
				spec.multiply(frag.material.specular);
				Vector dir = Vector.normalize(frag.view_pos);
				Vector rdir = Vector.subtract(dir, Vector.multiply(frag.view_norm, 2*Vector.dot(frag.view_norm, dir)));
				double sf = Vector.dot(rdir, ldir);
				if(sf > 0.0) {
					sf = Math.pow(sf, frag.material.specFactor);
					spec.multiply(sf);
					color.add(spec);
				}
			}
			frag.color = color;
		}
	}
}
