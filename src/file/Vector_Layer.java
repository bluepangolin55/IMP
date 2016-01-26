package file;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import functionality.Vector_Element;

public class Vector_Layer extends Layer{

	public LinkedList<Vector_Element> vector_elements;
	
	public Vector_Layer(int width, int height, int imageType, String nameT,
			Img mother_image) {
		super(width, height, imageType, nameT, mother_image);
		vector_elements = new LinkedList<Vector_Element>();
	}
	
	@Override
	public BufferedImage get_image() {
		return super.get_image();
	}
	
	@Override
	public void draw(Graphics g, int x, int y, int w, int h) {
		for(Vector_Element ve : vector_elements){
			g.drawPolygon(ve.form);
		}
	}

}
