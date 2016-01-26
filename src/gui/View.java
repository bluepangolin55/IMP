package gui;

import java.awt.Point;
import file.Img;

public class View {
	
	public double zoom;
	protected double zoomConstant;		//how fast is zooming
	public int IHomePosX;		//image position centered with calculated zoom
	public int IHomePosY;	//image position centered with calculated zoom
	
	public int shiftX=0;		//image shifting
	public int shiftY;		//image shifting
	Image_Tile tile;
	public Img img;
	
	//for HUD Elements
	public Point tool_hud_panel_position;	
	
	
	public int img_pos_x(){
		return (int) ((tile.get_position().x)*zoom+IHomePosX+shiftX);	//not the real solution
	}
	
	public int img_pos_y(){
		return tile.get_position().y+IHomePosY+shiftY;
	}
	
	public int img_width(){
		return (int) (img.getWidth()*zoom);
	}
	
	public int img_height(){
		return (int) (img.getHeight()*zoom);
	}
	
	public View(Image_Tile image_tile) {
		this.tile=image_tile;
		zoom=1;
		zoomConstant=1.2;
		IHomePosX=0;
		IHomePosY=0;
		shiftX=0;
		shiftY=0;
	}
	
	public void centered_view(){
		IHomePosX = (int) ((tile.getWidth()-img.getWidth()*zoom)/2);
		IHomePosY = (int) ((tile.getHeight()-img.getHeight()*zoom)/2);
		shiftX=0;
		shiftY=0;
	}
	
	public void filled_view(){
		if((double) (tile.getWidth())/(double) ((img.getWidth()))<(double) (tile.getHeight())/(double) ((img.getHeight())))
			zoom=(double) (tile.getWidth())/(double) ((img.getWidth()));
		else
			zoom=(double) (tile.getHeight())/(double) ((img.getHeight()));
		
		centered_view();
	}
	
	public void real_view(){
		zoom=1;
		centered_view();
	}
	
	public void zoom_in(){
		if(img!=null){
			if(zoom<200){
				zoom=zoom*zoomConstant;
				IHomePosX = (int) ((tile.getWidth()-img.getWidth()*zoom)/2);
				IHomePosY = (int) ((tile.getHeight()-img.getHeight()*zoom)/2);
				
				shiftX=(int) (shiftX*zoomConstant);
				shiftY=(int) (shiftY*zoomConstant);
	
				//centering while zooming in case the picture has been moved far away from the center
				if(tile.getWidth()-img.getWidth()*zoom > img.getWidth()/3){
					shiftX=shiftX/2;
				}
				else {
					//centering the picture to the mouse position
					shiftX=shiftX+(tile.getWidth()/2-tile.mouse_x)/5;
					
					//keepeng the edges around the image from beeing bigger than 1/2 of the screen space
					if(IHomePosX+shiftX > tile.getWidth()/2){
						shiftX=tile.getWidth()/2-IHomePosX;
					}
					if(tile.getWidth()-IHomePosX-shiftX-img.getWidth()*zoom > tile.getWidth()/2){
						shiftX=(int) (tile.getWidth()/2-IHomePosX-img.getWidth()*zoom);
					}
				}
				
				if(tile.getHeight()-img.getHeight()*zoom > img.getHeight()/3){
					shiftY=shiftY/2;
				}
				else {
					//centering the picture to the mouse position
					shiftY=shiftY+(tile.getHeight()/2-tile.mouse_y)/5;
					
					if(IHomePosY+shiftY > tile.getHeight()/2){
						shiftY=tile.getHeight()/2-IHomePosY;
					}
					if(tile.getHeight()-IHomePosY-shiftY-img.getHeight()*zoom > tile.getHeight()/2){
						shiftY=(int) (tile.getHeight()/2-IHomePosY-img.getHeight()*zoom);
					}
				}
			}
		}
	}
	
	public void zoom_out(){
		if(img!=null){
			if(zoom>0.1){
				zoom=zoom/zoomConstant;
				IHomePosX = (int) ((tile.getWidth()-img.getWidth()*zoom)/2);
				IHomePosY = (int) ((tile.getHeight()-img.getHeight()*zoom)/2);
				shiftX=(int) (shiftX/zoomConstant);
				shiftY=(int) (shiftY/zoomConstant);
				
				//centering while zooming in case the picture has been moved far away from the center
				if(tile.getWidth()-img.getWidth()*zoom > img.getWidth()/3){
					shiftX=shiftX/2;
				}
				else {
					//centering the picture to the mouse position
					shiftX=shiftX-(tile.getWidth()/2-tile.mouse_x)/6;
					
					//keepeng the edges around the image from beeing bigger than 1/5 of the screen space
					if(IHomePosX+shiftX > tile.getWidth()/2){
						shiftX=tile.getWidth()/2-IHomePosX;
					}
					if(tile.getWidth()-IHomePosX-shiftX-img.getWidth()*zoom > tile.getWidth()/2){
						shiftX=(int) (tile.getWidth()/2-IHomePosX-img.getWidth()*zoom);
					}
				}
				
				
				if(tile.getHeight()-img.getHeight()*zoom > img.getHeight()/3){
					shiftY=shiftY/2;
				}
				else {
					//centering the picture to the mouse position
					shiftY=shiftY-(tile.getHeight()/2-tile.mouse_y)/6;
					
					if(IHomePosY+shiftY > tile.getHeight()/2){
						shiftY=tile.getHeight()/5-IHomePosY;
					}
					if(tile.getHeight()-IHomePosY-shiftY-img.getHeight()*zoom > tile.getHeight()/2){
						shiftY=(int) (tile.getHeight()/2-IHomePosY-img.getHeight()*zoom);
					}
				}
			}
		}
	}
}
