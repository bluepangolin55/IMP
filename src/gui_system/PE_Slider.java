package gui_system;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import main.IMP;

import gui.Design_Preferences;

public class PE_Slider 
extends Tile
implements Design_Preferences{

	//value storage
	GuiIntValue value;
	
	//GUI
	String name;
	double drawing_factor;
	int slider_length;
	int slider_width=18;
	int left_space=10;
	int right_space=10;
	int top_space;
	boolean mouse_pressed;
	String message;
	
	int posY;

	//private Tool routine;
	
	public PE_Slider(String name, GuiIntValue new_value, GuiClient client){	
		this.name=name;
		this.value=new_value;
		this.message = "";
		this.client = client; 
		
		size.width=360;
		size.height=30;
		slider_length=size.width-left_space-right_space;
		drawing_factor=slider_length/(value.max_value-value.min_value);
		
		height_is_fixed=true;
	}
	
	public PE_Slider(String name, GuiIntValue new_value, GuiClient client, String message){	
		super(client);
		this.name=name;
		this.value=new_value;
		this.message = message;
		
		size.width=360;
		size.height=30;
		slider_length=size.width-left_space-right_space;
		drawing_factor=slider_length/(value.max_value-value.min_value);
		
		height_is_fixed=true;
	}
	
	public void setMaxValue(int i) {
	}
	
	public void set_value(double new_value) {
			value.set_value(new_value);
			send_message(message);
			send_message("paint_preview");
	}
	
	public void set_value(GuiIntValue new_value) {
		System.out.println("set value");
		value = new_value;
		send_message(message);
		message_client("value_changed");
		send_message("paint_preview");
}

	/** Uses the value given by the slider (0 to slider width)
		to set the actual value of the slider. This is dependent
		on the min/max of the value that the slider represents.
	 */
	public void set_value_with_slider(double slider_value) {
		drawing_factor = value.intervalLength/slider_length;
		set_value(value.min_value + slider_value*drawing_factor);
		send_message(message);
		message_client("value_changed");
		send_message("paint_preview");
	}
	
	@Override
	public void paint(Graphics g){
		fill_background(g, color_dark_background);

		top_space=(size.height-slider_width)/2;
		slider_length=size.width-left_space-right_space;
		drawing_factor = value.intervalLength/slider_length;
		int slider_value = (int) ((value.getInt()-value.min_value)/drawing_factor);

		if(is_hovered(IMP.main_panel.mouse_position)){
			g.setColor(new Color(70,70,100));		//for slideredges
			if(mouse_pressed)
				g.setColor(color_second_contrast);
			g.drawRect(get_position().x+left_space,get_position().y+top_space,slider_length,slider_width);
			g.setColor(color_contrast);				//for sliderforeground
			if(mouse_pressed)
				g.setColor(color_second_contrast);
			g.fillRect(get_position().x+left_space,get_position().y+top_space,(slider_value), slider_width);
			
}
		else{
			g.setColor(color_dark_background);				//for sliderbackground
			g.fillRect(get_position().x+left_space,get_position().y+(size.height-slider_width)/2,slider_length,slider_width);
			g.setColor(color_contrast);				//for sliderforeground
			if(mouse_pressed)
				g.setColor(color_second_contrast);
			g.fillRect(get_position().x+left_space,get_position().y+top_space,(slider_value), slider_width);
		}
		
		//text
		FontMetrics fm = g.getFontMetrics();
		g.setColor(Color.white);
		g.drawString("" + value.getInt(), get_position().x+15, get_position().y+size.height/2+6);
		g.setColor(color_text_grey);
		fm = g.getFontMetrics();
		g.drawString(name, get_position().x+(left_space+slider_length -fm.stringWidth(name))/2, get_position().y+size.height/2+6);
	
	}
	
	@Override
	public void mouse_clicked(MouseEvent e){
	}
	
	@Override
	public void mouse_pressed(MouseEvent e){
		mouse_pressed=true;
		set_value_with_slider((e.getX()-left_space-get_position().x));
		//if(routine!=null){
			send_message("paint_preview");
		//}
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
		if(!mouse_pressed)
			mouse_pressed=true;
		set_value_with_slider((e.getX()-left_space-get_position().x));
		//if(routine!=null){
			send_message("paint_preview");
		//}
	}
	@Override
	public void mouse_released_anywhere(MouseEvent e) {
		mouse_pressed=false;
	}
	
	@Override
	public void mouse_wheel(MouseWheelEvent e) {
		if(e.isShiftDown())
			set_value(value.getDouble()-e.getWheelRotation()*10);
		else
			set_value(value.getDouble()-e.getWheelRotation());
	}
	

}
