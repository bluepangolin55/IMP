package gui_system;

import gui.Design_Preferences;
import gui.Image_Tile;

import java.awt.Graphics;

public class HUD_Start 
extends HUD_Element
implements Design_Preferences{
	
	Image_Tile tile;
	
	public HUD_Start(Image_Tile tile) {
		this.tile=tile;
		set_enabled(true);
	}
	
	@Override
	public void paint(Graphics g){
		if(enabled){
			g.setColor(color_dark_background);
			g.fillRect(tile.get_position().x, tile.get_position().y, tile.getWidth(), tile.getHeight());
		}
	}
}
