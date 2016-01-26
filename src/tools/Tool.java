package tools;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

import main.IMP;
import main.Menuelement;
import gui.Design_Preferences;
import gui.Image_Tile;
import gui_system.Floating_Panel;
import gui_system.GuiClient;
import gui_system.GuiValue;
import gui_system.Menulizable;
import gui_system.GuiIntValue;
import gui_system.PE_Tool_Titlebar;
import gui_system.Tile;


public abstract class Tool 
implements Design_Preferences, Menulizable, GuiClient{
	
	protected String name;
	private String action;
	protected String category;
	protected char key_shortcut;
	
	protected RecursiveAction worker;
	
	public boolean needs_open_image = true;
	public boolean works_during_other_routines = false;
	
	//properties
	public BufferedImage icon_default=null;
    public BufferedImage icon_selected=null;
	public Cursor cursor;
	
	//GUI
	public Floating_Panel hud_panel;
	private Menuelement menu;
	public Tile sidepanel;
	
public Tool() {
	action = "use_tool";
	IMP.action_informant.add_client(this);
	set_menu(new Menuelement("menu",null));
	hud_panel=new Floating_Panel(this);
	sidepanel=new Tile(Tile.VERTICAL, sidepanel_size);
	
	sidepanel.add(new PE_Tool_Titlebar(this));
	set_menu(new Menuelement("menu",null));
	category = "tool";
}


public void activate(){
	IMP.previous_tool=IMP.active_tool;
	IMP.active_tool=this;
	IMP.tool_sidepanel.remove_all_children();
	IMP.tool_sidepanel.add(sidepanel);
}
	
	public void mouse_moved(MouseEvent e){
		
	}
	
	public void mouse_clicked(MouseEvent e){
		
	}
	
	public void mouse_pressed(MouseEvent e){
		
	}
	
	public void mouse_dragged(MouseEvent e){
		
	}
	
	public void mouse_released(MouseEvent e){
		
	}
	
	public void mouse_wheel(MouseWheelEvent e){
		
	}

	public String get_name() {
		return name;
	}

	public void set_name(String name) {
		this.name = name;
	}
	
	public void add_gui_element(Tile gui_element){
		hud_panel.add(gui_element);
		sidepanel.add(gui_element);
	}
	
	public void paint_on_screen(Graphics g, Image_Tile tile) {
	}
	
	public void key_typed(KeyEvent e) {
		int key =  e.getKeyChar();		
	}
	
	public void key_pressed(KeyEvent e) {
		int key = e.getKeyCode();
	}
	
	public void key_released(KeyEvent e) {
		int key = e.getKeyCode();
	}
	
	public void take_action(String action){
	}


	public Menuelement get_menu() {
		return menu;
	}

	public void set_menu(Menuelement menu) {
		this.menu = menu;
	}
	
	public void recieve_message(String message){
		
	}

	@Override
	public String get_action() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String get_category(){
		return category;
	}
	
	@Override
	public char get_key_shortcut() {
		return key_shortcut;
	}
	
	@Override
	public boolean safe_to_activate() {
		return !(needs_open_image && IMP.opened_image == null) 
				&& !(IMP.routine_in_process && !works_during_other_routines);
	}
	
	protected class Worker extends RecursiveAction{

		@Override
		protected void compute() {
		}
	}
}
