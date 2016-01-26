package gui_system;

import java.awt.Graphics;

public class PE_File_Preview_Folders 
extends Tile{
	
	public PE_File_Preview_Folders() {
		set_width(300);
		width_is_fixed=true;
		alignment=VERTICAL;
	}

@Override
public void paint(Graphics g) {
	fill_background(g, color_dark_background);
	super.paint(g);
}
	
}
