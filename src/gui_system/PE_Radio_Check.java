package gui_system;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import tools.Routine;
import gui.Design_Preferences;

public class PE_Radio_Check
extends Tile
implements Design_Preferences{
	
	
	//GUI
	String name;
	boolean checked = false;
	double drawing_factor;
	boolean button_pressed;
	
	//functionality

	public PE_Radio_Check(String name){	
		this.name=name;
		size.width=50;
		size.height=button_height;
		height_is_fixed = true;
	}
	
	@Override
	public void mouse_clicked(MouseEvent e) {
		checked = !checked;
		broadcast(name);
	}
	
	@Override
	public void mouse_pressed(MouseEvent e) {
		button_pressed=true;
	}
	
	@Override
	public void mouse_released_anywhere(MouseEvent e) {
		super.mouse_released_anywhere(e);
		button_pressed=false;
	}
	
@Override
	public void paint(Graphics g){
		//apply/abort-bar
		if(checked){
			if(button_pressed)
				g.setColor(menuColors[1]);
			else
				g.setColor(color_contrast);
		}
		else{
			if(button_pressed)
				g.setColor(new Color(80,120,180));
			else
				g.setColor(menuColors[1]);
		}
		g.fillRect(get_position().x, get_position().y, size.width, size.height);
		g.setColor(menuColors[2]);
		g.drawRect(get_position().x, get_position().y, size.width, size.height);
		
		g.setColor(Color.white);
		Font f = button_font;
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		System.out.println(size.height);
		System.out.println(fm.getAscent() + fm.getDescent());
		g.drawString(name,get_position().x+size.width/2-(fm.stringWidth(name))/2,
				get_position().y + size.height - (size.height - (fm.getAscent() + fm.getDescent()))/2);
	}
}
