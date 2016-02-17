package gui;

import gui_system.Tile;

import java.awt.Dimension;
import main.IMP;

public class Mama_Tile 
extends Tile{

	Toolpanel_Tile tool_chooser;
	Gadgetpanel_Tile gadget_chooser;
	Tile sidepanel;
	Image_Tile left_image_panel;
	Image_Tile right_image_panel;
	Tile main_space;
	Tile_Menubar menubar;


	// state
	private boolean menubarVisible;
	private boolean sidepanelVisible;

	public final int MENUBAR = 1;
	public final int SIDEPANEL = 2;

	public Mama_Tile(int alignment, Dimension size) {
		super(alignment, size);
		alignment=HORIZONTAL;
		menubar=new Tile_Menubar();
		main_space=new Tile(HORIZONTAL);
		
		tool_chooser = new Toolpanel_Tile(VERTICAL, new Dimension(200,this.getHeight()));
		gadget_chooser = new Gadgetpanel_Tile(VERTICAL, new Dimension(200,this.getHeight()));
		
		sidepanel=new Tile(VERTICAL, new Dimension(315,300));
		sidepanel.width_is_fixed=true;
		Tile upper_panel=new Tile();
		Tile lower_panel=new Tile();

		left_image_panel=new Image_Tile();
		left_image_panel.set_context_menu(IMP.menu.main_menu);
		right_image_panel=new Image_Tile();
		right_image_panel.set_context_menu(IMP.menu.main_menu);
		IMP.tool_sidepanel=new Tile(VERTICAL, sidepanel_size);
		IMP.tool_sidepanel.set_background_color(color_dark_background);
		IMP.tool_sidepanel.width_is_fixed=true;
		IMP.tool_sidepanel.set_context_menu(IMP.menu.main_menu);
		IMP.tool_sidepanel.set_mother(this);

		IMP.mama = this;
		
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

		menubarVisible = true;
		sidepanelVisible = true;
		placeComponents();
//		toolpanel.set_visible(false);
		right_image_panel.set_visible(false);
		refresh_children();
	}


	// getter and setter for gui component visibility

	public void hideComponent(int component) {
		switch (component) {
			case MENUBAR:
				menubarVisible = false;
				break;
			case SIDEPANEL:
				sidepanelVisible = false;
				break;
		}
		placeComponents();
	}

	public void showComponent(int component){
		switch (component){
			case MENUBAR: menubarVisible = true;
				break;
			case SIDEPANEL: sidepanelVisible = true;
				System.out.println("set: ");
				break;
		}
		placeComponents();
	}

	public void toggleComponent(int component){
		switch (component){
			case MENUBAR: menubarVisible = !menubarVisible;
				break;
			case SIDEPANEL: sidepanelVisible = !sidepanelVisible;
				break;
		}
		placeComponents();
	}

	public boolean isComponentVisible(int component){
		switch (component){
			case MENUBAR: return menubarVisible;
			case SIDEPANEL: return sidepanelVisible;
			default: return false;
		}
	}

	private void placeComponents(){
		main_space.remove_all_children();
		main_space.add(left_image_panel);
		main_space.add(right_image_panel);
		if(sidepanelVisible) {
			System.out.println("placed: ");
			main_space.add(sidepanel);
		}
		main_space.refresh_children();

		children.clear();
		if(menubarVisible){
			children.add(menubar);
		}
		children.add(main_space);
		refresh_children();
	}
}