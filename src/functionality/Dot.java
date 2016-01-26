package functionality;

import java.awt.Point;

public class Dot 
	extends Point{
	
	private static final long serialVersionUID = 1L;
	
	public boolean is_bezier;
	public boolean is_selected;
	
	public Dot(int x, int y) {
		this.x = x;
		this.y = y;
		is_bezier = false;
	}
	
	public Dot(int x, int y, boolean is_bezier) {
		this.x = x;
		this.y = y;
		this.is_bezier = is_bezier;
	}

}
