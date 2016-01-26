package file;

import gui_system.Menubar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import main.IMP;


public abstract class Layer {

	private Img mother_image;
	//public BufferedImage image;
	private boolean visible=true;
	private boolean closed=false;
	private boolean selected=false;
	private int transparency=100;
	private int position=0;
	private String name;
	private boolean valueChanged;
	
	public Layer(int width, int height, int imageType, String nameT, Img mother_image) {
		this.mother_image=mother_image;
		position=position++;
		//image=new BufferedImage(width,height, BufferedImage.TYPE_INT_ARGB);
		name=nameT;
		
		//Graphics g=image.getGraphics();
		//g.setColor(Color.white);
		//g.fillRect(0, 0, width, height);
		Img.fileMade=true;
		if(IMP.preferences.menubar_on)
			Menubar.refreshMenu();
		IMP.menu.refresh_menu();
	}
	
	public boolean isVisible(){
		return visible;
	}
	
	public void set_visible(boolean b){
		visible=b;
	}
	
	public boolean isClosed(){
		return closed;
	}
	public void isClosed(boolean b){
		closed=b;
	}
	
	public int getTransparency(){
		return transparency;
	}
	public void setTransparency(int t){
		transparency=t;
		int alpha = t;
//	    alpha %= 0xff; 
//	    for (int cx=0;cx<image.getWidth();cx++) {          
//	        for (int cy=0;cy<image.getHeight();cy++) {
//	            int color = image.getRGB(cx, cy);
//
//	            int mc = (alpha << 24) | 0x00ffffff;
//	            int newcolor = color & mc;
//	            image.setRGB(cx, cy, newcolor);            
//
//	        }
//		}
	    
//	    for (int cx=0;cx<image.getWidth();cx++) {          
//	        for (int cy=0;cy<image.getHeight();cy++) {
//	            int rgb = image.getRGB(cx, cy);
//	            Color color = new Color(rgb);
//	            
//	            color = new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
//	            image.setRGB(cx, cy, color.getRGB());            
//
//	        }
//		}
	    
	    
	    
	    
//	    for (int x=0;x<image.getWidth();x++) {          
//	        for (int y=0;y<image.getHeight();y++) {
//	            int color = image.getRGB(x, y);
//
//		    	  int argb = image.getRGB(x, y);
//		    	  int oldAlpha = (argb >>> 24);
//		    	  if (oldAlpha == 100) {
//		    	    argb = (80 << 24) | (argb & 0xffffff);
//		    	    image.setRGB(x, y, argb);
//		    	  }         
//	        }
//		}
	}
	
	public int getPosition(){
		return position;
	}
	public String getName(){
		return name;
	}

	public boolean is_selected() {
		return selected;
	}
	
	public void select(){
		mother_image.unfocusAll();
		mother_image.previouslySelectedLayer=mother_image.get_selected_layer();
		mother_image.set_selected_layer(this);
		selected=true;
	}
	
	public void deselect(){
		selected=false;
	}
	
	public boolean valueChanged() {
		return valueChanged;
	}
	
	public void valueChanged(boolean b) {
		valueChanged=b;
	}

	public void set_image(BufferedImage new_image){
//		image=new_image;
	}

	public BufferedImage get_image() {
		return null;
	}
	
	public void draw(Graphics g, int x, int y, int w, int h){
	}

}
