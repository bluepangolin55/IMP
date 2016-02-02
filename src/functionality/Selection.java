package functionality;

import gui.Image_Tile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.LinkedList;

import sun.awt.image.ImageWatched.Link;
import main.IMP;

public class Selection {

	public Polygon points;
	public LinkedList<Dot> dots;

	public SelectionMap map;
	public Rectangle bounds;
	public BufferedImage subimage;
	
	public Selection previous_selection;
	public Selection next_selection;
	
	public Selection() {
		points=new Polygon();
		dots = new LinkedList<Dot>();
	}
	
	public void add_point(Dot point){
		dots.add(point);
		points.addPoint(point.x, point.y);
	}
	
	public void add_point(int x, int y){
		dots.add(new Dot(x,y));
		points.addPoint(x, y);
		map = new SelectionMap(points);
		bounds = points.getBounds();
	}
	
	public void remove_last_point(){
		if(points.npoints>1){
			Polygon new_points = new Polygon();
			for(int i=0; i<points.npoints-1;i++){
				new_points.addPoint(points.xpoints[i], points.ypoints[i]);	
			}
			points=new_points;
		}
	}
	
	public void set_point(int index, Dot point){
		points.xpoints[index]=point.x;
		points.ypoints[index]=point.y;
	}
	
	public void set_point(int index, int x, int y){
		points.xpoints[index]=x;
		points.ypoints[index]=y;
	}
	
	public void set_point_x(int index, int x){
		points.xpoints[index]=x;
	}
	
	public void set_point_y(int index, int y){
		points.ypoints[index]=y;
	}
	
	public void set_last_point(Dot point){
		points.xpoints[points.npoints-1]=point.x;
		points.ypoints[points.npoints-1]=point.y;
	}
	
	public Dot get_last_point(){
		return new Dot(points.xpoints[points.npoints-1],points.ypoints[points.npoints-1]);
	}
	
	public int size(){
		return points.npoints;
	}
	
	public int get_point_x(int index){
		return dots.get(index).x;
//		return points.xpoints[index];
	}
	
	public int get_point_y(int index){
		return dots.get(index).y;
//		return points.ypoints[index];
	}
	
	public void draw_line(Graphics g, Image_Tile image_Tile){
		Graphics2D g2=(Graphics2D) g;
		Polygon selectionDisplay=new Polygon();
		
		float dash1[] = {10};
	    BasicStroke dashed =
	        new BasicStroke(2,
	                        BasicStroke.JOIN_BEVEL,
	                        BasicStroke.JOIN_MITER,
	                        10.0f, dash1, 0.0f);
	    
	    
		for(int i=0;i<this.size();i++){
			selectionDisplay.addPoint((int) (image_Tile.view.img_pos_x()+this.get_point_x(i)*image_Tile.view.zoom),
					(int) (image_Tile.view.img_pos_y()+this.get_point_y(i)*image_Tile.view.zoom));
		}
		g2.setColor(new Color(240,240,240));
		g2.drawPolyline(selectionDisplay.xpoints, selectionDisplay.ypoints, selectionDisplay.npoints);
		g2.setColor(new Color(60,60,60));
		g2.setStroke(dashed);
		g2.drawPolyline(selectionDisplay.xpoints, selectionDisplay.ypoints, selectionDisplay.npoints);
		g2.setStroke(new BasicStroke());
	}
	
	public void draw_points(Graphics g){
		Polygon selectionDisplay=new Polygon();
		for(int i=0;i<this.size();i++){
			selectionDisplay.addPoint((int) (IMP.active_image_tile.view.img_pos_x()+this.get_point_x(i)*IMP.active_image_tile.view.zoom),
					(int) (IMP.active_image_tile.view.img_pos_y()+this.get_point_y(i)*IMP.active_image_tile.view.zoom));
		}
		//other points
		g.setColor(new Color(20,20,20));
		
		for(int i=0; i<points.npoints;i++){
			g.fillOval(selectionDisplay.xpoints[i]-3,selectionDisplay.ypoints[i]-3, 6, 6);
		}
	}
}
