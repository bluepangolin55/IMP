package functionality;

import java.awt.image.Kernel;

/**
 * Created by dimitri on 06/02/16.
 */
public class Filters {

	public static final Kernel LAPLACIAN =
		new Kernel(3,3, new float[]{
				0,  1, 0,
				1, -4, 1,
				0,  1, 0
			}
		);

	public static final Kernel HIGH_PASS =
		new Kernel(3,3, new float[]{
				-1f, -1f, -1f,
				-1f,  8f, -1f,
				-1f, -1f, -1f
			}
		);

	public static final Kernel SOBEL_X=
			new Kernel(3,3, new float[]{
					-1f, 0f, 1f,
					-2f, 0f, 2f,
					-1f, 0f, 1f
				}
			);


	public static final Kernel SOBEL_Y=
			new Kernel(3,3, new float[]{
					-1f, -2f, -1f,
					 0f,  0f,  0f,
					 1f,  2f,  1f
				}
			);

//	public static Kernel laplacian(){
//		if(laplacianKernel == null) {
//			float[] data = {
//					0, 1, 0,
//					1, -4, 1,
//					0, 1, 0
//			};
//			laplacianKernel = new Kernel(3, 3, data);
//		}
//		return laplacianKernel;
//	};
//
//	public static Kernel soblelX(){
//		if(laplacianKernel == null) {
//			float[] data = {
//					-1, 0, 1,
//					-2, 0, 2,
//					-1, 0, 1
//			};
//			laplacianKernel = new Kernel(3, 3, data);
//		}
//		return laplacianKernel;
//	};

}
