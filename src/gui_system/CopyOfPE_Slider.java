package gui_system;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import tools.Routine;
import gui.Design_Preferences;

public class CopyOfPE_Slider 
extends Tile
implements Design_Preferences{

	//value storage
	GuiIntValue value;
	
	//GUI
	String name;
	double drawing_factor;
	int slider_length;
	int left_space=65;
	int right_space=80;
	
	int posY;

	private Routine routine;
	
	public CopyOfPE_Slider(String name, GuiIntValue new_value){	
		this.name=name;
		this.value=new_value;
		
		size.width=360;
		size.height=30;
		slider_length=size.width-left_space-right_space;
		drawing_factor=slider_length/(value.max_value-value.min_value);
		
		height_is_fixed=true;
	}
	
	public CopyOfPE_Slider(String name, GuiIntValue new_value, Routine routine){	
		this.routine=routine;
		this.name=name;
		this.value=new_value;
		
		size.width=360;
		size.height=30;
		slider_length=size.width-left_space-right_space;
		drawing_factor=slider_length/(value.max_value-value.min_value);

		height_is_fixed=true;
	}
	
	public int drawing_value() {
		return (int) (value.getDouble()*drawing_factor);
	}

	public void setMaxValue(int i) {
	}
	
	public void set_value(double new_value) {
			value.set_value(new_value);
	}
	
	public void set_value_with_slider(double slider_value) {
		slider_value = slider_value/drawing_factor;
		set_value(slider_value);
	}
	
	@Override
	public void paint(Graphics g){
		fill_background(g, color_dark_background);

		slider_length=size.width-left_space-right_space;
		drawing_factor=slider_length/(value.max_value-value.min_value);
		int slider_width=6;
		
		int position_y=this.get_position().y;
		g.setColor(new Color(220,220,220));				//for sliderbackground
		g.fillRect(get_position().x+left_space,position_y+size.height/2,slider_length,slider_width);
		g.setColor(color_contrast);				//for sliderforeground
		g.fillRect(get_position().x+left_space,position_y+size.height/2,(drawing_value()), slider_width);
		g.setColor(new Color(40,40,40));		//for slideredges
		g.drawRect(get_position().x+left_space,position_y+size.height/2,slider_length,slider_width);
		g.setColor(new Color(250,250,250));		//for slider
		g.fillOval(get_position().x+left_space+drawing_value()-7,position_y+size.height/2-slider_width/2,14,14);
		g.setColor(color_menu_background);		//for slideredge
		g.drawOval(get_position().x+left_space+drawing_value()-7,get_position().y+size.height/2-slider_width/2,14,14);

		//text
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.white);
		g.drawString("" + value.getInt(), get_position().x+15, position_y+size.height/2+6);
		g.drawString(name, get_position().x+slider_length+((120-fm.stringWidth(name))), position_y+size.height/2+6);
	}
	
	@Override
	public void mouse_clicked(MouseEvent e){
	}
	
	@Override
	public void mouse_pressed(MouseEvent e){
		set_value_with_slider((e.getX()-65-get_position().x));
		if(routine!=null){
			routine.paint_preview();
		}
//		CursorControl.changeCursor("move");
	}
	
	@Override
	public void mouse_released(MouseEvent e){
		
	}
	
	@Override
	public void mouse_moved(MouseEvent e){
		
	}
	
	@Override
	public void mouse_dragged(MouseEvent e){
		set_value_with_slider((e.getX()-65-get_position().x));
		if(routine!=null){
			routine.paint_preview();
		}
//		CursorControl.changeCursor("move");
	}
}
