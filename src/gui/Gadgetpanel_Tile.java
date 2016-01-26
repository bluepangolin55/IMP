package gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import gui_system.Tile;
import gui_system.Toolbutton;

import main.IMP;

public class Gadgetpanel_Tile 
extends Tile{
	
	private List<Toolbutton> toolbuttons;
	
	public Gadgetpanel_Tile(int alignment, Dimension size) {
		super(alignment, size);
		set_width(35);
		width_is_fixed=true;
		
		toolbuttons=new ArrayList<Toolbutton>();
		
		for(int i=0; i<IMP.gadgets.size();i++){
			Toolbutton toolbutton=new Toolbutton(IMP.gadgets.get(i));
			toolbuttons.add(toolbutton);
			children.add(toolbutton);
		}
		IMP.global_keys_informant.add_client(this);
	}
	
	@Override
	public void mouse_clicked(MouseEvent e) {
		for(int i=0; i<toolbuttons.size();i++)
			toolbuttons.get(i).setFocused(false);
		super.mouse_clicked(e);
	}
	
	@Override
	public void key_pressed(KeyEvent e) {
		super.key_pressed(e);
		if(e.getKeyChar()=='1'){
			toolbuttons.get(0).press();
		}
		if(e.getKeyChar()=='2'){
			toolbuttons.get(1).press();
		}
		if(e.getKeyChar()=='3'){
			toolbuttons.get(2).press();
		}
		if(e.getKeyChar()=='4'){
			toolbuttons.get(3).press();
		}
		if(e.getKeyChar()=='5'){
			toolbuttons.get(4).press();
		}
	}
	
	@Override
	public void key_released(KeyEvent e) {
		super.key_pressed(e);
		int selected_tool=-1;
		if(e.getKeyChar()=='1'){
			selected_tool=0;
		}
		else if(e.getKeyChar()=='2'){
			selected_tool=1;
		}
		else if(e.getKeyChar()=='3'){
			selected_tool=2;
		}
		else if(e.getKeyChar()=='4'){
			selected_tool=3;
		}
		else if(e.getKeyChar()=='5'){
			selected_tool=4;
		}
		
		if(selected_tool!=-1){
			for(int i=0; i<toolbuttons.size();i++){
				toolbuttons.get(i).setFocused(false);
				toolbuttons.get(i).release();
			}
		toolbuttons.get(selected_tool).activate();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		fill_background(g, color_dark_background);
		super.paint(g);
	}
}