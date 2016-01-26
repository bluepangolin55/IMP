package file;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import functionality.Transformations;

public class Image_Layer extends Layer{

	public BufferedImage image;
	
	public Image_Layer(int width, int height, int imageType, String nameT,
			Img mother_image) {
		super(width, height, imageType, nameT, mother_image);

		image=new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
		
		Graphics g=image.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, width, height);
	}
	
	@Override
	public void setTransparency(int t) {
		super.setTransparency(t);
		int alpha = t;
	    for (int cx=0;cx<image.getWidth();cx++) {          
	        for (int cy=0;cy<image.getHeight();cy++) {
	            int rgb = image.getRGB(cx, cy);
	            Color color = new Color(rgb);
	            
	            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	            image.setRGB(cx, cy, color.getRGB());            

	        }
		}
	}
	
	@Override
	public void set_image(BufferedImage new_image) {
		super.set_image(new_image);
		image=new_image;
	}
	
	@Override
	public BufferedImage get_image() {
		return image;
	}
	
	@Override
	public void draw(Graphics g, int x, int y, int w, int h) {
		g.drawImage(image, x, y, w, h, null);
		
	}

}
