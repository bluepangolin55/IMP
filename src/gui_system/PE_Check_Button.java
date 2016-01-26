package gui_system;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import tools.Routine;


import gui.Design_Preferences;

public class PE_Check_Button
extends Tile
implements Design_Preferences{
	
	//value storage
	//Number_Value value;
	GuiBooleanValue value;

	//GUI
	String name;
	double drawing_factor;
	int slider_width=200;
	int close_button_pos_x;
	boolean button_pressed;
	
	//functionality
	
	Color[] menuColors={new Color(20,20,20),new Color(60,60,60),new Color(100,100,100),new Color(140,140,140),new Color(180,180,180)};

	private Routine routine;

	
	public PE_Check_Button(String name, GuiBooleanValue value, GuiClient client){		
		super(client);
		this.name=name;
		this.value=value;
		size.width=50;
		size.height=30;
		//GUI
		close_button_pos_x=get_position().x+size.width-(size.width/12);
	}
	
	@Override
	public void mouse_clicked(MouseEvent e) {
		if(!value.locked){
			if(value.getValue()){
				value.setValue(false);
			}
			else{
				value.setValue(true);
			}
		message_client("value_changed");
		}
		if(routine!=null){
			routine.paint_preview();
		}
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
		if(value.getValue()){
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
		Font f = new Font("Calibri", Font.BOLD, 11);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(name,get_position().x+size.width/2-(fm.stringWidth(name))/2,
				get_position().y+size.height- (fm.getAscent()+fm.getDescent()));
	}
}
