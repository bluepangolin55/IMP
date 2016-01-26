package gui_system;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import main.IMP;

import tools.Tool;

public class PE_Tool_Titlebar 
extends Tile{
	
	Tool tool;
	private boolean pressed;
	
	public PE_Tool_Titlebar(Tool tool) {
	this.tool=tool;	
	this.menu=tool.get_menu();
	context_menu=new Context_Menu(menu);
	size.height=25;
	height_is_fixed=true;
	}
	
	@Override
	public void mouse_pressed(MouseEvent e) {
		pressed=true;
		if(!context_menu.enabled){
			context_menu=new Context_Menu(tool.get_menu());
			context_menu.enabled=true;
			context_menu.center_x=get_position().x;
			context_menu.center_y=get_position().y+getHeight()+3;
			context_menu.set_min_width(getWidth());
			IMP.main_panel.context_menu=context_menu;
			System.out.println(menu.getName());
		}
	}
	
	@Override
	public void mouse_released_anywhere(MouseEvent e) {
		pressed=false;
	}
	
	
	@Override
	public void paint(Graphics g) {
//		g.setColor(color_menu_background);
//		g.fillRect(get_position().x,get_position().y, size.width, size.height);
//		g.setColor(color_borders);
//		g.drawRect(get_position().x,get_position().y+1, size.width-1, size.height-2);
		
		if(tool.get_name()!=null){
			if(context_menu.enabled){
				g.setColor(color_contrast);
				g.fillRect(get_position().x, get_position().y, size.width, size.height);
				g.setColor(color_borders);
				g.drawRect(get_position().x, get_position().y, getWidth()-1, getHeight()-1);
			}
				
			g.setColor(color_text_bright);
			g.setFont(new Font("Arial", Font.BOLD, 12));
			FontMetrics fm = g.getFontMetrics();
			g.drawString(tool.get_name(), get_position().x+get_size().width/2-(fm.stringWidth(tool.get_name()))/2, get_position().y+get_size().height/2+(fm.getAscent())/2);
			g.setFont(new Font("Arial", Font.PLAIN, 12));
		}
		
	}
}
