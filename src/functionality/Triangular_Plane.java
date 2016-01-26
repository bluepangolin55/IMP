package functionality;

import java.awt.Color;
import java.awt.Polygon;

public class Triangular_Plane {
//3D-coordinates
	Vector x;
	Vector y;
	Vector z;
	
	Color color;
	
	public Triangular_Plane(Vector x, Vector y, Vector z) {
		this.x=x;
		this.y=y;
		this.z=z;
		color=Color.black;
	}
	
	public Polygon project_to_2D(Vector camera_position, Matrix camera_transformation, Vector view_distance){
		Polygon polygon = new Polygon();
		Vector x2 = new Vector(2);
		Vector y2 = new Vector(2);
		Vector z2 = new Vector(2);
		
		Vector d=Vector.difference(x, camera_position).transform(camera_transformation);
		double g=view_distance.get(3)/d.get(3);
		x2.set(1, (d.get(1)-view_distance.get(1)) * g);
		x2.set(2, (d.get(2)-view_distance.get(2)) * g);

		d=Vector.difference(y, camera_position).transform(camera_transformation);
		g=view_distance.get(3)/d.get(3);
		y2.set(1, (d.get(1)-view_distance.get(1)) * g);
		y2.set(2, (d.get(2)-view_distance.get(2)) * g);
		
		d=Vector.difference(z, camera_position).transform(camera_transformation);
		g=view_distance.get(3)/d.get(3);
		z2.set(1, (d.get(1)-view_distance.get(1)) * g);
		z2.set(2, (d.get(2)-view_distance.get(2)) * g);

		int scale=1000;
		x2.multiply_by_scalar(scale);
		y2.multiply_by_scalar(scale);
		z2.multiply_by_scalar(scale);
		
		int shift_x=400;
		int shift_y=300;

		
		polygon.addPoint((int) (x2.get(1)) +shift_x, (int) (x2.get(2)) +shift_y);
		polygon.addPoint((int) (y2.get(1)) +shift_x, (int) (y2.get(2)) +shift_y);
		polygon.addPoint((int) (z2.get(1)) +shift_x, (int) (z2.get(2)) +shift_y);
		
		return polygon;
	}
	
	public double distance_to_camera(Vector camera){
		return (Vector.difference(x, camera).euclidean_norm()+
				Vector.difference(y, camera).euclidean_norm()+
				Vector.difference(z, camera).euclidean_norm())
				/3;
	}
	
	
	//basic transformations
	public void move(Vector move_vector){
		this.x.add(move_vector);
		this.y.add(move_vector);
		this.z.add(move_vector);
	}
	
	public void transform(Matrix transformation_matrix){
		this.x=this.x.transform(transformation_matrix);
		this.y=this.y.transform(transformation_matrix);
		this.z=this.z.transform(transformation_matrix);
	}
}
