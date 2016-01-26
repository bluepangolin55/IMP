package gui_system;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import gui.Design_Preferences;

public class PE_Label
extends Tile
implements Design_Preferences{
	
	//value storage
	GuiIntValue value;
	boolean changed;

	//GUI
	GuiStringValue text;
	int cursor_position;
	
	//functionality
	
	public PE_Label(GuiStringValue text){		
		this.text=text;
		size=new Dimension(50,30);
		height_is_fixed=true;
	}
	
	@Override
	public void paint(Graphics g){
		g.setColor(color_dark_background);
		
		g.fillRect(get_position().x, get_position().y, size.width, size.height);
		
		g.setColor(color_text_bright);

		Font f = new Font("Calibri", Font.BOLD, 11);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(text.get_string(),get_position().x+
				size.width/2-(fm.stringWidth(text.get_string()))/2,
				get_position().y+size.height- (fm.getAscent()+fm.getDescent()));
	}
}
