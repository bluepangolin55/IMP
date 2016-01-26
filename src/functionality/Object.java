package functionality;

import gui.Design_Preferences;
import gui_system.GuiColorValue;
import gui_system.GuiStringValue;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

import tools.Tool_3D;

public class Object 
implements Design_Preferences{
	
	ArrayList<Plane> planes;
	ArrayList<Vector> position_log;
	boolean selected;
	public GuiStringValue name;
	public GuiColorValue color;
	
	public Object() {
		planes=new ArrayList<Plane>();
		position_log=new ArrayList<Vector>();
		name=new GuiStringValue();
		color=new GuiColorValue();
	}
	
	public void paint(Graphics g, Vector camera_position, Matrix camera_transformation, Vector view_distance, int mode) {
		double[] distances=new double[planes.size()];
		
		for(int i=0;i<planes.size();i++){
			distances[i]=planes.get(i).distance_to_camera(camera_position);
		}		
		
		int index;
		for(int i=0;i<planes.size();i++){
			index=find_biggest_value(distances);
			distances[index]=0;
			Polygon plane_in_2D=planes.get(index).project_to_2D(camera_position, camera_transformation, view_distance);
			
			
			if(mode==Tool_3D.COLOR){
				g.setColor(color.get_color());
//				g.setColor(planes.get(index).color);
				g.fillPolygon(plane_in_2D);
				if(selected)
					g.setColor(color_second_contrast);
				else
					g.setColor(Color.black);
//				g.drawPolygon(plane_in_2D);
				
				if(plane_in_2D.npoints==2)
					if(selected)
						g.setColor(color_second_contrast);
					else
						g.setColor(color.get_color());
//						g.setColor(planes.get(index).color);
					g.drawPolygon(plane_in_2D);
			}
			else if(mode==Tool_3D.WIREFRAME){
				if(selected)
					g.setColor(color_second_contrast);
				else
					g.setColor(Color.black);
				g.drawPolygon(plane_in_2D);
			}
		}
	}
	
	public Polygon get_shape(Vector camera_position, Matrix camera_transformation, Vector view_distance) {
		Polygon polygon_sum = new Polygon();
		Polygon polygon = new Polygon();
		for(int i=0;i<planes.size();i++){
			polygon=planes.get(i).project_to_2D(camera_position, camera_transformation, view_distance);
			for(int j=0;i<polygon.npoints;i++){
				polygon_sum.addPoint(polygon.xpoints[i], polygon.ypoints[i]);
				
			}
		}
		return polygon_sum;
	}
	
	public boolean is_hovered(Point point_on_screen, Vector camera_position, Matrix camera_transformation, Vector view_distance) {
		for(int i=0;i<planes.size();i++){
			if(planes.get(i).project_to_2D(camera_position, camera_transformation, view_distance).contains(point_on_screen)){
				return true;
			}
		}
		return false;
	}
	
	private int find_biggest_value(double[] array){
		int index=0;
		double highest_value=0;
		for(int i=0;i<array.length;i++){
			if(array[i]>highest_value){
				highest_value=array[i];
				index=i;
			}
		}
		return index;
	}
	
	public void log_position(){
		position_log.add(position());
	}
	
	public void move_to_last_position(){
		if(position_log.size()>0){
			move_to_position(position_log.get(position_log.size()-1));
			position_log.remove(position_log.size()-1);
		}
	}
	
	public double distance_to_camera(Vector camera_position){
		double distance=0;
		for(int i=0;i<planes.size();i++){
			distance=distance+planes.get(i).distance_to_camera(camera_position);
		}
		distance=distance/planes.size();
		return distance;
	}
	
	public void add_plane(Plane new_plane){
		planes.add(new_plane);
	}
	
	public void add_object(Object new_object){
		for(int i=0;i<new_object.planes.size();i++){
			planes.add(new_object.planes.get(i));
		}
	}
	
	public void set_color(Color new_color){
		for(int i=0;i<planes.size();i++){
			planes.get(i).color=new_color;
		}
		color.set_color(new_color);
	}
	
	public void set_name(String new_name){
			name.set_string(new_name);
	}
	
	public Color get_color(){
		return planes.get(0).color;
	}
	
	public void move(double x, double y, double z){
		Vector transformation_vector = new Vector(new double[]{x,y,z});
		for(int i=0;i<planes.size();i++){
			planes.get(i).move(transformation_vector);
		}
	}
	
	public Vector position(){
		Vector center = new Vector(3);
		for(int i=0;i<planes.size();i++){
			center.add(planes.get(i).center());
		}
		center.multiply_by_scalar(1.0/planes.size());
		return center;
	}
	
	public void move_to_origin(){
		Vector position=position();
		this.move(-position.get(1),-position.get(2), -position.get(3));
	}
	
	public void move_to_position(Vector new_position){
		Vector distance=Vector.difference(new_position, position());
		this.move(distance.get(1),distance.get(2), distance.get(3));
	}
	
	public void scale(double x){
		Vector old_position=position();
		move_to_origin();
		for(int i=0;i<planes.size();i++){
			planes.get(i).stretch(x);
		}
		move_to_position(old_position);
	}
	
	public void rotate(double x, double y, double z){
		Vector old_position=position();
		move_to_origin();
		
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
			for(int i=0;i<planes.size();i++){
				planes.get(i).transform(transformatin_matrix);
			}
		}
		if(y!=0){
			Matrix transformatin_matrix = new Matrix(new double[][]{
			{Math.cos(y),0,-Math.sin(y)},
			{0,1,0},
			{Math.sin(y),0,Math.cos(y)}});
			for(int i=0;i<planes.size();i++){
				planes.get(i).transform(transformatin_matrix);
			}
		}
		if(x!=0){
			Matrix transformatin_matrix = new Matrix(new double[][]{
			{Math.cos(z),Math.sin(z),0},
			{-Math.sin(z),Math.cos(z),0},
			{0,0,1}});
			for(int i=0;i<planes.size();i++){
				planes.get(i).transform(transformatin_matrix);
			}
		}
		
		move_to_position(old_position);
	}

	
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	//various static object constructors
	
	public static Object cube(int x, int y, int z){
		double px=0;
		double py=0;
		double pz=0;
		Object cube = new Object();
		
		cube.add_plane(new Plane(new Vector(new double[]{px+x,py,pz+z}), new Vector(new double[]{px,py+y,pz+z}), new Vector(new double[]{px,py,pz+z})));
		cube.add_plane(new Plane(new Vector(new double[]{px+x,py,pz+z}), new Vector(new double[]{px,py+y,pz+z}), new Vector(new double[]{px+x,py+y,pz+z})));
		
		cube.add_plane(new Plane(new Vector(new double[]{px+x,py+y,pz}), new Vector(new double[]{px+x,py,pz+z}), new Vector(new double[]{px+x,py,pz})));
		cube.add_plane(new Plane(new Vector(new double[]{px+x,py+y,pz}), new Vector(new double[]{px+x,py,pz+z}), new Vector(new double[]{px+x,py+y,pz+z})));
		
		cube.add_plane(new Plane(new Vector(new double[]{px+x,py+y,pz}), new Vector(new double[]{px,py+y,pz+z}), new Vector(new double[]{px,py+y,pz})));
		cube.add_plane(new Plane(new Vector(new double[]{px+x,py+y,pz}), new Vector(new double[]{px,py+y,pz+z}), new Vector(new double[]{px+x,py+y,pz+z})));

		cube.add_plane(new Plane(new Vector(new double[]{px+x,py,pz}), new Vector(new double[]{px,py,pz+z}), new Vector(new double[]{px,py,pz})));
		cube.add_plane(new Plane(new Vector(new double[]{px+x,py,pz}), new Vector(new double[]{px,py,pz+z}), new Vector(new double[]{px+x,py,pz+z})));
		
		cube.add_plane(new Plane(new Vector(new double[]{px,py+y,pz}), new Vector(new double[]{px,py,pz+z}), new Vector(new double[]{px,py,pz})));
		cube.add_plane(new Plane(new Vector(new double[]{px,py+y,pz}), new Vector(new double[]{px,py,pz+z}), new Vector(new double[]{px,py+y,pz+z})));
		
		cube.add_plane(new Plane(new Vector(new double[]{px+x,py,pz}), new Vector(new double[]{px,py+y,pz}), new Vector(new double[]{px,py,pz})));
		cube.add_plane(new Plane(new Vector(new double[]{px+x,py,pz}), new Vector(new double[]{px,py+y,pz}), new Vector(new double[]{px+x,py+y,pz})));
		
		
		cube.set_color(Color.blue);
		cube.planes.get(0).color=Color.white;
		cube.planes.get(1).color=Color.white;
		cube.planes.get(2).color=Color.red;
		cube.planes.get(3).color=Color.red;
		cube.planes.get(4).color=Color.green;
		cube.planes.get(5).color=Color.green;
		cube.planes.get(6).color=Color.blue;
		cube.planes.get(7).color=Color.blue;
		cube.planes.get(8).color=Color.orange;
		cube.planes.get(9).color=Color.orange;
		cube.planes.get(10).color=Color.yellow;
		cube.planes.get(11).color=Color.yellow;
		cube.set_name("Cube");
		return cube;
	}
	
	public static Object cube2(int x, int y, int z){
		double px=0;
		double py=0;
		double pz=0;
		Object cube = new Object();
		cube.add_plane(new Plane(new Vector[]{
				new Vector(new double[]{px,py,pz}),
				new Vector(new double[]{px,py+y,pz}),
				new Vector(new double[]{px+x,py+y,pz}),
				new Vector(new double[]{px+x,py,pz})
		}));
		
		cube.add_plane(new Plane(new Vector[]{
				new Vector(new double[]{px,py,pz}),
				new Vector(new double[]{px+x,py,pz}),
				new Vector(new double[]{px+x,py,pz+z}),
				new Vector(new double[]{px,py,pz+z})
		}));
		
		cube.add_plane(new Plane(new Vector[]{
				new Vector(new double[]{px,py,pz}),
				new Vector(new double[]{px,py+y,pz}),
				new Vector(new double[]{px,py+y,pz+z}),
				new Vector(new double[]{px,py,pz+z})
		}));
		
		cube.add_plane(new Plane(new Vector[]{
				new Vector(new double[]{px,py,pz+z}),
				new Vector(new double[]{px,py+y,pz+z}),
				new Vector(new double[]{px+x,py+y,pz+z}),
				new Vector(new double[]{px+x,py,pz+z})
		}));
		
		cube.add_plane(new Plane(new Vector[]{
				new Vector(new double[]{px,py+y,pz}),
				new Vector(new double[]{px+x,py+y,pz}),
				new Vector(new double[]{px+x,py+y,pz+z}),
				new Vector(new double[]{px,py+y,pz+z})
		}));
		
		cube.add_plane(new Plane(new Vector[]{
				new Vector(new double[]{px+x,py,pz}),
				new Vector(new double[]{px+x,py+y,pz}),
				new Vector(new double[]{px+x,py+y,pz+z}),
				new Vector(new double[]{px+x,py,pz+z})
		}));
		
		cube.set_color(Color.blue);
		cube.planes.get(0).color=Color.white;
		cube.planes.get(1).color=Color.red;
		cube.planes.get(2).color=Color.green;
		cube.planes.get(3).color=Color.blue;
		cube.planes.get(4).color=Color.orange;
		cube.planes.get(5).color=Color.yellow;
		cube.planes.get(0).color=new Color(100,100,180,80);
		cube.planes.get(1).color=new Color(100,120,180,80);
		cube.planes.get(2).color=new Color(100,140,180,80);
		cube.planes.get(3).color=new Color(100,160,180,80);
		cube.planes.get(4).color=new Color(100,180,180,80);
		cube.planes.get(5).color=new Color(100,200,180,80);
		cube.set_color (new Color(100,200,180));
		cube.set_name("Cube");
		return cube;
	}
	
	public static Object sphere(double x, double y, double z){
		x=5;
		y=5;
		z=5;
		
		double px=0;
		double py=0;
		double pz=0;
		int h_divisions=40;
		int v_divisions=40;
		Object sphere = new Object();
		
		Vector north_pole = new Vector(new double[]{0, y, 0});
		double horizontal_angle=360.0/h_divisions;
		double vertical_angle=180.0/v_divisions;

		Matrix horizontal_rotation = new Matrix(new double[][]{
			{Math.cos(Math.toRadians(horizontal_angle)),0,-Math.sin(Math.toRadians(horizontal_angle))},
			{0,1,0},
			{Math.sin(Math.toRadians(horizontal_angle)),0,Math.cos(Math.toRadians(horizontal_angle))}});
		
		Matrix vertical_rotation = new Matrix(new double[][]{
			{Math.cos(Math.toRadians(vertical_angle)),Math.sin(Math.toRadians(vertical_angle)),0},
			{-Math.sin(Math.toRadians(vertical_angle)),Math.cos(Math.toRadians(vertical_angle)),0},
			{0,0,1}});
		
		Vector[] key_vectors= new Vector[v_divisions+2];
		Vector vertical_rotator=new Vector(new double[]{0, y, 0});
		for(int i=0;i<v_divisions+1;i++){
			key_vectors[i]=vertical_rotator;
			vertical_rotator=vertical_rotator.transform(vertical_rotation);
		}
		
		//step 2: build the sphere
		Vector point1;
		Vector point2;
		Vector point3;
		
		//north tip
		
		for (int k=0;k<v_divisions-1;k++){
			point1=key_vectors[k].transform(horizontal_rotation);
			point2=key_vectors[k+1];
			point3=key_vectors[k+1].transform(horizontal_rotation);
			for(int i=0;i<h_divisions;i++){
				point1=point1.transform(horizontal_rotation);
				point2=point2.transform(horizontal_rotation);
				point3=point3.transform(horizontal_rotation);
				sphere.add_plane(new Plane(new Vector[]{
						point1,
						point2,
						point3
				}));
			}
			
			point1=key_vectors[k+1];
			point2=key_vectors[k+2];
			point3=key_vectors[k+1].transform(horizontal_rotation);
			for(int i=0;i<h_divisions;i++){
				point1=point1.transform(horizontal_rotation);
				point2=point2.transform(horizontal_rotation);
				point3=point3.transform(horizontal_rotation);
				sphere.add_plane(new Plane(new Vector[]{
						point1,
						point2,
						point3
				}));
			}
		}
		
		
		sphere.set_color (new Color(60,70,160));
		sphere.set_name("Sphere");
		return sphere;
	}
	
	public static Object line(int x){
		double px=0;
		double py=0;
		double pz=0;
		Object line = new Object();
		line.add_plane(new Plane(new Vector[]{
				new Vector(new double[]{px,py,pz}),
				new Vector(new double[]{px+x,py,pz}),
		}));
		line.set_color(color_second_contrast);
		line.set_name("Line");
		
		return line;
	}

	public void select() {
		selected=true;
	}

	public void deselect() {
		selected=false;
	}
}
