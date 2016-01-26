package gui_system;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import tools.Routine;
import main.IMP;

import gui.Design_Preferences;

public class PE_Button2
extends Tile
implements Design_Preferences{
	
	//value storage
	GuiIntValue value;
	String action;

	//GUI
	String name;
	double drawing_factor;
	int slider_width=200;
	int close_button_pos_x;
	boolean button_pressed;
	public boolean borders_enabled;
	
	//functionality
	private Routine routine;

	public PE_Button2(String name, String action, Routine routine){		
		this.name=name;
		this.action=action;
		this.routine=routine;
		size.width=50;
		size.height=24;
		height_is_fixed=true;
		//GUI
		close_button_pos_x=get_position().x+size.width-(size.width/12);
		borders_enabled=true;
	}
	
	@Override
	public void mouse_clicked(MouseEvent e) {
		routine.take_action(action);
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
		if(button_pressed)
			g.setColor(color_second_contrast);
		else if(is_hovered(IMP.main_panel.mouse_position))
			g.setColor(color_contrast);
		else
			g.setColor(color_bright_background);
		
		g.fillRect(get_position().x, get_position().y, size.width, size.height);
		if(borders_enabled){
			g.setColor(color_borders);
			g.drawRect(get_position().x, get_position().y, size.width-1, size.height-1);
		}
		
		g.setColor(color_text_dark);
		Font f = new Font("Calibri", Font.BOLD, 11);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(name,get_position().x+size.width/2-(fm.stringWidth(name))/2,
				get_position().y+(size.height+fm.getAscent())/2);
	}
}
