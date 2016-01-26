package gui_system;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import gui.Design_Preferences;

public class PE_Color_Choser 
extends Tile
implements Design_Preferences{

	//value storage
	public List<GuiIntValue> values;
	public GuiColorValue color;
	
	//GUI
	String name;
	double drawing_factor;
	int slider_length=400;
	
	int posY;
	
	@Override
	public void mouse_dragged(MouseEvent e) {
		super.mouse_dragged(e);
		color.set_color(values.get(0).getInt(), values.get(1).getInt(), values.get(2).getInt(), values.get(3).getInt());
	}
	
	@Override
	public void mouse_released_anywhere(MouseEvent e) {
		super.mouse_released_anywhere(e);
		color.set_color(values.get(0).getInt(), values.get(1).getInt(), values.get(2).getInt(), values.get(3).getInt());
	}
	
	public PE_Color_Choser(String name, GuiColorValue color){	
		this.name=name;
		this.color=color;
		
		alignment=VERTICAL;
		
		size.width=360;
		size.height=120;
		
		height_is_fixed=true;
		
		margin_left=200;
		
		values = new ArrayList<GuiIntValue>();
		values.add(new GuiIntValue(0,255,100));
		values.add(new GuiIntValue(0,255,100));
		values.add(new GuiIntValue(0,255,100));
		values.add(new GuiIntValue(0,255,255));
		children.add(new PE_Slider("Rot", values.get(0), client));
		children.add(new PE_Slider("Grün", values.get(1), client));
		children.add(new PE_Slider("Blau", values.get(2), client));
		children.add(new PE_Slider("Deckkraft", values.get(3), client));
		refresh_children();
	}
	
	@Override
	public void paint(Graphics g){
		g.setColor(Color.gray);
		g.fillRoundRect(get_position().x, get_position().y, margin_left, size.height,20,20);
		int stripes=20;
		int square_size=(margin_left)/(stripes);
		g.setColor(Color.lightGray);
		for(int i=1;i<stripes-1;i+=2)
			g.fillRect(get_position().x+square_size*i+square_size/2, get_position().y, square_size, size.height);
		g.setColor(color.get_color());
		g.fillRoundRect(get_position().x, get_position().y, margin_left, size.height,20,20);
		g.setColor(color_borders);
		g.drawRoundRect(get_position().x, get_position().y, margin_left, size.height,20,20);
		super.paint(g);
	}

	public void set_color(GuiColorValue new_color) {
		color=new_color;
		values.get(0).set_value(color.get_red());
		values.get(1).set_value(color.get_green());
		values.get(2).set_value(color.get_blue());
		values.get(3).set_value(color.get_alpha());
	}

}
