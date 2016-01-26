package gui_system;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import main.IMP;
import main.Menuelement;

public class PE_Menu 
extends Tile{
	
	int menu_element_height=25;
	private boolean pressed;
	
	public PE_Menu(Menuelement menu) {
		this.menu=menu;
		set_width(menu.getName().length()*10);
		width_is_fixed=true;
		context_menu=new Context_Menu(menu);
	}
	
	@Override
	public void mouse_pressed(MouseEvent e) {
		pressed=true;
		if(!context_menu.enabled){
			context_menu=new Context_Menu(menu);
			context_menu.enabled=true;
			context_menu.center_x=get_position().x;
			context_menu.center_y=get_position().y+getHeight()+3;
			IMP.main_panel.context_menu=context_menu;
		}
	}
	
	@Override
	public void mouse_dragged(MouseEvent e) {
		pressed=true;
		if(!context_menu.enabled){
			context_menu=new Context_Menu(menu);
			context_menu.enabled=true;
			context_menu.center_x=get_position().x;
			context_menu.center_y=get_position().y+getHeight()+3;
			IMP.main_panel.context_menu=context_menu;
		}
	}
	
	@Override
	public void mouse_released_anywhere(MouseEvent e) {
		pressed=false;
	}
	
	@Override
	public void paint(Graphics g) {
//		g.setColor(color_menu_background);
		if(is_hovered(IMP.main_panel.mouse_position)){
			g.setColor(color_contrast);
			g.fillRect(get_position().x, get_position().y, size.width, menu_element_height);
		}
		if(context_menu.enabled){
			g.setColor(color_contrast);
			g.fillRect(get_position().x, get_position().y, size.width, menu_element_height);
			g.setColor(color_borders);
			g.drawRect(get_position().x, get_position().y, getWidth()-1, getHeight()-1);
		}
		
		g.setColor(color_text_bright);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(menu.getName(),
				get_position().x+((size.width-fm.stringWidth(menu.getName()))/2),
				get_position().y+18);
	}

}
