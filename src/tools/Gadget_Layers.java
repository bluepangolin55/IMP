package tools;

import java.io.IOException;

import javax.imageio.ImageIO;

import main.IMP;
import gui_system.GuiClient;
import gui_system.GuiIntValue;
import gui_system.Message;
import gui_system.PE_Layer_Tile;
import gui_system.PE_Slider;

public class Gadget_Layers 
extends Gadget implements GuiClient{
	
	PE_Slider opacity_slider;
	PE_Layer_Tile layer_tile;
	GuiIntValue transparency;
	
	public Gadget_Layers() {
		name="Ebenen";
		transparency = new GuiIntValue(1,255,255);
		opacity_slider = new PE_Slider("Transparenz", transparency, this, "transparency_changed");
		sidepanel.add(opacity_slider);
		sidepanel.add(layer_tile=new PE_Layer_Tile());
		set_menu(IMP.menu.menu_layer);
		
		try {
			icon_default=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/layers_a.png"));
			icon_selected=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/layers_b.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void take_action(String action) {
		System.out.println(action);
		if(action=="layer_new")
			new_layer();
		if(action=="layer_delete")
			delete_layer();
		if(action=="layer_duplicate")
			duplicate_layer();
	}
	
	private void new_layer(){
		IMP.opened_image.createLayer(IMP.opened_image.getWidth(), IMP.opened_image.getHeight());
		layer_tile.scan_for_layers();
	}
	
	private void delete_layer(){
		IMP.opened_image.deleteLayer(IMP.opened_image.get_selected_layer());
		layer_tile.scan_for_layers();
	}
	
	private void duplicate_layer(){
		IMP.opened_image.duplicateLayer(IMP.opened_image.get_selected_layer().getPosition());
		layer_tile.scan_for_layers();
	}
	
	@Override
	public void receive_message(Message message) {
		if(message.getMessage().equals("transparency_changed")){
			if(IMP.opened_image != null)
				IMP.opened_image.get_selected_layer().setTransparency(transparency.getInt());
		}
		
	}

}