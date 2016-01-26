package gui_system;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import gui.Design_Preferences;

public class PE_Color_Choser2 
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
	
	public PE_Color_Choser2(String name, GuiColorValue color, GuiClient client){
		this.name=name;
		this.color=color;
		this.client = client;
		
		alignment=VERTICAL;
		
		size.width=360;
		size.height=120;
		
//		height_is_fixed=true;
		
		Tile color_preview = new PE_Color_Preview(color);
		color_preview.set_height(100);
		color_preview.height_is_fixed=true;
		Tile controls = new Tile(VERTICAL);
		children.add(controls);
		children.add(color_preview);

		values = new ArrayList<GuiIntValue>();
		values.add(new GuiIntValue(0,255,100));
		values.add(new GuiIntValue(0,255,100));
		values.add(new GuiIntValue(0,255,100));
		values.add(new GuiIntValue(0,255,255));
		controls.add(new PE_Slider("Rot", values.get(0), client));
		controls.add(new PE_Slider("Grün", values.get(1), client));
		controls.add(new PE_Slider("Blau", values.get(2), client));
		controls.add(new PE_Slider("Deckkraft", values.get(3), client));
		refresh_children();
	}
	
	public void set_color(GuiColorValue new_color) {
		color=new_color;
		values.get(0).set_value(color.get_red());
		values.get(1).set_value(color.get_green());
		values.get(2).set_value(color.get_blue());
		values.get(3).set_value(color.get_alpha());
	}

}
