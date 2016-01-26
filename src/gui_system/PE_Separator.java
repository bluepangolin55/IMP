package gui_system;

import java.awt.Graphics;
import gui.Design_Preferences;

public class PE_Separator 
extends Tile
implements Design_Preferences{
	
	public boolean stroked;
	
	public PE_Separator(int size, int alignment, boolean stroked) {
		if(alignment==HORIZONTAL){
			this.size.width=size;
			width_is_fixed=true;
		}
		else{
			this.size.height=size;
			height_is_fixed=true;
		}
		this.stroked=stroked;
	}

	@Override
	public void paint(Graphics g) {
		if(stroked){
			g.setColor(color_borders);
			int stroke_length=5;
			int stroke_height=1;
			int strokes=this.getWidth()/stroke_length;
			int border=this.getWidth()/16/stroke_length;
			for(int i=border;i<strokes-border;i+=2)
				g.fillRect(get_position().x+stroke_length*i, get_position().y+this.getHeight()/2,stroke_length, stroke_height);
		}
		else{
//			g.setColor(color_borders);
//			int stroke_height=1;
//			int border=this.getWidth()/16;
//			g.fillRect(get_position().x+border, get_position().y+this.getHeight()/2,this.getWidth()-2*border, stroke_height);
		}
		super.paint(g);
	}
}
