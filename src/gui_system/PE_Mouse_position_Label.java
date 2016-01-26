package gui_system;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import main.IMP;

import gui.Design_Preferences;

public class PE_Mouse_position_Label
extends Tile
implements Design_Preferences{
	
	public PE_Mouse_position_Label(){		
		size=new Dimension(50,30);
		height_is_fixed=true;
	}
	
	@Override
	public void paint(Graphics g){
		if(IMP.opened_image!=null){
			g.setColor(color_dark_background);
			
			g.fillRect(get_position().x, get_position().y, size.width, size.height);
			
			g.setColor(color_text_bright);
	
			String text=("Mausposition im Bild :  " + IMP.opened_image.mouse_x + "x  und  " + IMP.opened_image.mouse_y + "y");
			Font f = new Font("Calibri", Font.BOLD, 11);
			g.setFont(f);
			FontMetrics fm = g.getFontMetrics();
			g.drawString(text,get_position().x,
					get_position().y+size.height- (fm.getAscent()+fm.getDescent()));
		}
	}
}
