package gui;

import gui_system.Tile;

import java.awt.Dimension;
import main.IMP;

public class Mama_Tile 
extends Tile{
	
	public Mama_Tile(int alignment, Dimension size) {
		super(alignment, size);
		alignment=HORIZONTAL;
		Tile_Menubar menubar=new Tile_Menubar();
		Tile main_space=new Tile(HORIZONTAL);
		
		Toolpanel_Tile tool_chooser=new Toolpanel_Tile(VERTICAL, new Dimension(200,this.getHeight()));
		Gadgetpanel_Tile gadget_chooser=new Gadgetpanel_Tile(VERTICAL, new Dimension(200,this.getHeight()));
		
		Tile sidepanel=new Tile(VERTICAL, new Dimension(315,300));
		sidepanel.width_is_fixed=true;
		Tile upper_panel=new Tile();
		Tile lower_panel=new Tile();
		

		
		Image_Tile left_image_panel=new Image_Tile();
		left_image_panel.set_context_menu(IMP.menu.main_menu);
		Image_Tile right_image_panel=new Image_Tile();
		right_image_panel.set_context_menu(IMP.menu.main_menu);
		IMP.tool_sidepanel=new Tile(VERTICAL, sidepanel_size);
		IMP.tool_sidepanel.set_background_color(color_dark_background);
		IMP.tool_sidepanel.width_is_fixed=true;
		IMP.tool_sidepanel.set_context_menu(IMP.menu.main_menu);
		IMP.tool_sidepanel.set_mother(this);
		
		IMP.gadget_sidepanel=new Tile(VERTICAL, sidepanel_size);
		IMP.gadget_sidepanel.set_background_color(color_dark_background);
		IMP.gadget_sidepanel.width_is_fixed=true;
		IMP.gadget_sidepanel.set_context_menu(IMP.menu.main_menu);
		IMP.gadget_sidepanel.set_mother(this);
		

		upper_panel.add(IMP.tool_sidepanel);
		upper_panel.add(tool_chooser);

		lower_panel.add(IMP.gadget_sidepanel);
		lower_panel.add(gadget_chooser);
		
		sidepanel.add(upper_panel);
		sidepanel.add(lower_panel);

		

		main_space.add(left_image_panel);
		main_space.add(right_image_panel);
		main_space.add(sidepanel);
		
		children.add(menubar);
		children.add(main_space);
//		toolpanel.set_visible(false);
		right_image_panel.set_visible(false);
		refresh_children();
	}
}