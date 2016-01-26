package functionality;

import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;

import main.IMP;

public class Plane {
//3D-coordinates
	ArrayList<Vector> coordinates;
	
	Color color;
	
	public Plane(Vector x, Vector y, Vector z) {
		coordinates=new ArrayList<Vector>();
		
		coordinates.add(x);
		coordinates.add(y);
		coordinates.add(z);
		
		color=Color.black;
	}
	
	public Plane(Vector[] new_coordinates) {
		coordinates=new ArrayList<Vector>(new_coordinates.length);
		for(int i=0;i<new_coordinates.length;i++){
			coordinates.add(new_coordinates[i]);
		}
		color=Color.black;
	}
	
	public Polygon project_to_2D(Vector camera_position, Matrix camera_transformation, Vector view_distance){
		Polygon polygon = new Polygon();
		int scale=1000;
		int shift_x=IMP.active_image_tile.getWidth()/2;
		int shift_y=IMP.active_image_tile.getHeight()/2;
		
		for(int i=0;i<coordinates.size();i++){
			Vector d=Vector.difference(coordinates.get(i), camera_position).transform(camera_transformation);
			double g=view_distance.get(3)/d.get(3);
			Vector point=new Vector(new double[]{(d.get(1)-view_distance.get(1)) * g, (d.get(2)-view_distance.get(2)) * g});
			point.multiply_by_scalar(scale);
			polygon.addPoint((int) (point.get(1)) +shift_x, (int) (point.get(2)) +shift_y);
		}
		return polygon;
	}
	
	public double distance_to_camera(Vector camera){
		double difference=0;
		for(int i=0;i<coordinates.size();i++){
			difference=difference+Vector.difference(coordinates.get(i), camera).euclidean_norm();
		}
		return difference;
	}
	
	//basic transformations
	public void move(Vector move_vector){
		for(int i=0;i<coordinates.size();i++){
			coordinates.get(i).add(move_vector);
		}
	}
	
	public void transform(Matrix transformation_matrix){
		for(int i=0;i<coordinates.size();i++){
			coordinates.set(i, coordinates.get(i).transform(transformation_matrix));
		}
	}
	
	public void stretch(double x) {
		for(int i=0;i<coordinates.size();i++){
			coordinates.get(i).multiply_by_scalar(x);
		}
	}

	public Vector center() {
		Vector center = new Vector(3);
		for(int i=0;i<coordinates.size();i++){
			center.add(coordinates.get(i));
		}
		center.multiply_by_scalar(1.0/coordinates.size());
		return center;
	}
	
	public void rotate(double x, double y, double z){
		x=Math.toRadians(x);
		y=Math.toRadians(y);
		z=Math.toRadians(z);
//		Matrix transformatin_matrix = new Matrix(new double[][]{
//				{Math.cos(y)*Math.cos(z),-Math.cos(x)*Math.sin(z)+Math.sin(x)*Math.sin(y)*Math.cos(z),Math.sin(x)*Math.sin(z)+Math.cos(x)*Math.sin(y)*Math.cos(z)},
//				{Math.cos(y)*Math.cos(z),Math.cos(x)*Math.sin(z)+Math.sin(x)*Math.sin(y)*Math.sin(z),-Math.sin(x)*Math.cos(z)+Math.cos(x)*Math.sin(y)*Math.sin(z)},
//				{-Math.sin(y),Math.sin(x)*Math.cos(y),Math.cos(x)*Math.cos(y)}});
		if(x!=0){
			Matrix transformatin_matrix = new Matrix(new double[][]{
			{1,0,0},
			{0,Math.cos(x),Math.sin(x)},
			{0,-Math.sin(x),Math.cos(x)}});
			this.transform(transformatin_matrix);
		}
		if(y!=0){
			Matrix transformatin_matrix = new Matrix(new double[][]{
			{Math.cos(y),0,-Math.sin(y)},
			{0,1,0},
			{Math.sin(y),0,Math.cos(y)}});
			this.transform(transformatin_matrix);
		}
		if(x!=0){
			Matrix transformatin_matrix = new Matrix(new double[][]{
			{Math.cos(z),Math.sin(z),0},
			{-Math.sin(z),Math.cos(z),0},
			{0,0,1}});
			this.transform(transformatin_matrix);
		}
	}
}
