package gui_system;

import gui.Design_Preferences;
import gui.Image_Tile;

import java.awt.Font;
import java.awt.Graphics;

public class HUD_Ruler 
extends HUD_Element
implements Design_Preferences{
	
	Image_Tile tile;
	
	public HUD_Ruler(Image_Tile tile) {
		this.tile=tile;
		set_enabled(true);
	}
	
	@Override
	public void paint(Graphics g){
		if(enabled){
			if(tile.img != null){
				//horizontal ruler
				g.setFont(new Font("Helvetica", Font.PLAIN, 8));
				g.setColor(color_dark_background);
				g.fillRect(tile.get_position().x, tile.get_position().y, tile.getWidth(), 30);
				g.setColor(color_text_grey);
				
				int number=tile.img.getWidth()/10;
				double distance=tile.img.getWidth()*tile.view.zoom/number;
				int gap=tile.img.getWidth()/number;
				for(int i=0;i<number+1;i++){
					g.fillRect((int) (tile.view.img_pos_x()+i*distance), tile.get_position().y+26, 1, 4);
					if(i%5==0){
						g.fillRect((int) (tile.view.img_pos_x()+i*distance), 22, 1, 8);
						g.drawString("" + i*gap, (int) (tile.view.img_pos_x()+i*distance)-6,tile.get_position().y+16);
					}
				}
				//vertical ruler
				g.setColor(color_dark_background);
				g.fillRect(tile.get_position().x, tile.get_position().y, 30, tile.getHeight());
				g.setColor(color_text_grey);

				number=tile.img.getHeight()/10;
				distance=tile.img.getHeight()*tile.view.zoom/number;
				for(int i=0;i<number+1;i++){
					g.fillRect(26, (int) (tile.view.img_pos_y()+i*distance), 4, 1);
					if(i%5==0){
						g.fillRect(tile.get_position().x+22, (int) (tile.view.img_pos_y()+i*distance), 8, 1);
						g.drawString("" + i*gap, tile.get_position().x+5,(int) (tile.view.img_pos_y()+i*distance)+3);
					}
				}
				//cursor
				g.setColor(color_text_bright);
				if(tile.img.mouse_x<=0)
					g.fillRect(tile.view.img_pos_x()-2, tile.get_position().y, 2, 20);
				else if(tile.img.mouse_x>=tile.img.getWidth())
					g.fillRect(tile.view.img_pos_x()+tile.view.img_width()-2, tile.get_position().y, 2, 20);
				else
					g.fillRect(tile.mouse_x-2, tile.get_position().y, 2, 20);
				
				if(tile.img.mouse_y<=0)
					g.fillRect(tile.get_position().x, tile.view.img_pos_y()-2, 20, 3);
				else if(tile.img.mouse_y>=tile.img.getHeight())
					g.fillRect(tile.get_position().x, tile.view.img_pos_y()+tile.view.img_height()-2, 20, 2);
				else
					g.fillRect(tile.get_position().x, tile.mouse_y-2, 20, 2);
				
				g.setFont(new Font("Helvetica", Font.PLAIN, 12));
			}
		}
	}

}
