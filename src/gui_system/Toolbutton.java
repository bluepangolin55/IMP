package gui_system;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import tools.Tool;

import main.IMP;



public class Toolbutton
extends Tile{

	private boolean isFocusable;
	private boolean isFocused;
	private String name;
    private Tool tool;
    
    private boolean pressed;
	
	public Toolbutton(Tool tool){
		this.tool=tool;
		name=tool.get_name();
		isFocused=false;
		setFocusable(true);
		size.width=35;
		size.height=35;
		width_is_fixed=true;
		height_is_fixed=true;
	}
	
	@Override
	public void mouse_clicked(MouseEvent e) {
		if(!IMP.routine_in_process){
			activate();
		}
	}
	
	@Override
	public void mouse_pressed(MouseEvent e) {
		if(!IMP.routine_in_process){
			press();
		}
	}
	
	@Override
	public void mouse_released_anywhere(MouseEvent e) {
		pressed=false;
	}

	public void activate(){
		tool.activate();
		isFocused=true;
	}
	
	public void press(){
		pressed=true;
	}
	
	public void release(){
		pressed=false;
	}
	
	@Override
	public void paint(Graphics g) {
		if(pressed || isFocused)
			fill_round_background(g, color_contrast);
		else
			fill_background(g, color_dark_background);
		if(isFocused)
			g.drawImage(tool.icon_selected, get_position().x, get_position().y, size.width, size.height,null);
		else
			g.drawImage(tool.icon_default, get_position().x, get_position().y, size.width, size.height,null);
		
		if(is_hovered(new Point(IMP.main_panel.mouse_position))){
			fill_round_background(g, color_bright_opaque_layer);
		}
	}
	
	void fill_round_background(Graphics g, Color color){
		g.setColor(color);
		g.fillRoundRect(get_position().x+2,get_position().y+2, size.width-4, size.height-4, 10, 10);
	}
	
	public boolean isFocusable() {
		return isFocusable;
	}
	
	public Tool get_tool() {
		return tool;
	}
	
	public void setFocusable(boolean isFocusable) {
		this.isFocusable = isFocusable;
	}
	
	public boolean isFocused() {
		return isFocused;
	}
	
	public void setFocused(boolean b) {
		isFocused=b;
	}

	public String get_name() {
		return name;
	}
}
