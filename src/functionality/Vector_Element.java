package functionality;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

public class Vector_Element {
	
	public Point position;
	public Dimension size;
	public Polygon form;
	public Color color;
	
	public static Vector_Element square(int x, int y, int w, int h){
		Vector_Element result = new Vector_Element();
		result.position = new Point(x,y);
		result.size = new Dimension(w,h);
		result.form = new Polygon();
		result.form.addPoint(0, 0);
		result.form.addPoint(w, 0);
		result.form.addPoint(w, h);
		result.form.addPoint(0, h);
		result.color = Color.lightGray;
		return result;
	}
	
	public void draw(Graphics g){
		g.setColor(color);
		Polygon p = new Polygon();
		for(int i=0; i<form.npoints; i++){
			p.addPoint(form.xpoints[i] + position.x, form.ypoints[i] + position.y);
		}
		g.fillPolygon(p);
	}
	
}

