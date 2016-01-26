package gui_system;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import tools.Tool;

import main.IMP;

import functionality.Selection;
import gui.Design_Preferences;

public class List_Element
extends Tile
implements Design_Preferences{

	//GUI
	String name;
	double drawing_factor;
	int slider_width=200;
	int close_button_pos_x;
	boolean close_button_pressed;
	
	//functionality
	Selection selection;
	
	Color[] menuColors={new Color(20,20,20),new Color(60,60,60),new Color(100,100,100),new Color(140,140,140),new Color(180,180,180)};

	public List_Element(String name, Selection selection, Tool tool){
		this.name=name;
		this.selection=selection;
		
		size.width=340;
		size.height=25;
		
		//GUI
		close_button_pos_x=get_position().x+size.width-(size.width/12);
	}

	@Override
	public void mouse_clicked(MouseEvent e) {
		IMP.opened_image.active_selection=selection;
		if(e.getX()<close_button_pos_x){
			if(e.isShiftDown()){
				if(IMP.opened_image.active_selections.contains(selection)){
					IMP.opened_image.active_selections.remove(selection);
				}
				else{
					IMP.opened_image.active_selections.add(selection);
				}
				
			}
			else{
				if(IMP.opened_image.active_selections.contains(selection)){
					IMP.opened_image.active_selections.remove(selection);
				}
				else{
					IMP.opened_image.active_selections.removeAll(IMP.opened_image.active_selections);
					IMP.opened_image.active_selections.add(selection);
				}
			}
		}
		else{
			IMP.opened_image.active_selections.remove(selection);
			remove_this();

		}
	}
	
	@Override
	public void mouse_pressed(MouseEvent e) {
		super.mouse_pressed(e);
		if(e.getX()<close_button_pos_x){
			
		}
		else{
			close_button_pressed=true;
		}
	}
	
	@Override
	public void mouse_released_anywhere(MouseEvent e) {
		super.mouse_released_anywhere(e);
		close_button_pressed=false;
	}
	
@Override
	public void paint(Graphics g){
		close_button_pos_x=get_position().x+size.width-(size.width/12);
		
		int position_y=this.get_position().y;
		//text
		FontMetrics fm = g.getFontMetrics();
		
		if(IMP.opened_image.active_selections.contains(selection))
			g.setColor(color_contrast);
		else
			g.setColor(new Color(60,60,60,200));
		g.fillRect(get_position().x, position_y, size.width, size.height);
		
		//delete button
		if(close_button_pressed)
			g.setColor(color_abort);
		else
			g.setColor(new Color(100,100,100));
		g.fillRect(close_button_pos_x, position_y, get_position().x+size.width-close_button_pos_x, size.height);
		
		g.setColor(new Color(40,40,40));
		g.drawRect(get_position().x, position_y, size.width, size.height);
		
		g.setColor(Color.white);
		g.drawString(name, get_position().x+((size.width-fm.stringWidth(name))/2),position_y+16);
	}
}
