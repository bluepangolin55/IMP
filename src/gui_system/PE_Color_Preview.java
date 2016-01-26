package gui_system;

import java.awt.Color;
import java.awt.Graphics;

public class PE_Color_Preview 
extends Tile{
	
	private GuiColorValue color;

	
	public PE_Color_Preview(GuiColorValue color) {
		this.color=color;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.gray);
		g.fillRoundRect(get_position().x, get_position().y, size.width, size.height,20,20);
		int stripes=20;
		int square_size=(size.width)/(stripes);
		g.setColor(Color.lightGray);
		for(int i=1;i<stripes-1;i+=2)
			g.fillRect(get_position().x+square_size*i+square_size/2, get_position().y, square_size, size.height);
		g.setColor(color.get_color());
		g.fillRoundRect(get_position().x, get_position().y, size.width, size.height,20,20);
		g.setColor(color_borders);
		g.drawRoundRect(get_position().x, get_position().y, size.width-1, size.height-1,20,20);
		super.paint(g);
	}
}
