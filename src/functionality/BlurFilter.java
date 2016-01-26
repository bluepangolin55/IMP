package functionality;

import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;


public class BlurFilter extends ConvolveOp{
	
	public BlurFilter(int width, int height, float strength) {
		super(getBlurKernel(width, height, strength));
	}
	
	
	
	public static Kernel getBlurKernel(int w, int h, float s){
		if(w != h || w%2 == 1){
			System.out.println("invalid kernel parameters");
			return null;
		}
		float[] data = {
				1/14f, 2/14f, 1/14f,
				2/14f, 2/14f, 2/14f,
				1/14f, 2/14f, 1/14f
			};
		//float[] data = new float[w*h];
		return new Kernel(3, 3, data);
	}
	
}

