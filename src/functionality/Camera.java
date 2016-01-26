package functionality;

/**
 * A camera consists of a point, called the eye and a plane. Points in the 3D-world are being projected to the plane by
 * calculating the intersection between the vector that leads to from the camera eye to the point and the plane.
 */

public class Camera {
	
	Vector eye;
	
	Vector v0;
	Vector v1;
	Vector v2;

	
	public Camera(Vector eye, Vector v0, Vector v1, Vector v2) {
		this.eye=eye;
		this.v0=v0;
		this.v1=v1;
		this.v2=v2;
	}
}
