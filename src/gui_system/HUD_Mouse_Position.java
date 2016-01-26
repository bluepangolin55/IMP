package gui_system;

import java.awt.Color;
import java.awt.Graphics;

import main.IMP;

public class HUD_Mouse_Position 
extends HUD_Element{
	
	public HUD_Mouse_Position(Tile tile) {
		this.tile=tile;
		set_enabled(false);
	}
	
	@Override
	public void paint(Graphics g){
		if(enabled){
			if(IMP.opened_image != null){
				g.setColor(color_dark_background);
				g.fillRect(IMP.active_image_tile.mouse_x, IMP.active_image_tile.mouse_y, 80, 50);
				g.setColor(Color.white);
				g.drawString("X: " + IMP.opened_image.mouse_x, IMP.active_image_tile.mouse_x + 20,
						IMP.active_image_tile.mouse_y+20);
				g.drawString("Y: " + IMP.opened_image.mouse_y, IMP.active_image_tile.mouse_x +20,
						IMP.active_image_tile.mouse_y + 40);
			}
		}
	}
	
}
