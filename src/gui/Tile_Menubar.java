package gui;

import java.awt.Graphics;
import main.IMP;
import gui_system.PE_Menu;
import gui_system.Tile;

public class Tile_Menubar 
extends Tile{
	
	public Tile_Menubar() {
		set_height(25);
		height_is_fixed=true;
		
		for(int i=0;i<IMP.menu.main_menu.elements.size();i++){
			children.add(new PE_Menu(IMP.menu.main_menu.get(i)));
		}
	}

	@Override
	public void paint(Graphics g) {
		fill_background(g, color_dark_background);
		super.paint(g);
	}
}
