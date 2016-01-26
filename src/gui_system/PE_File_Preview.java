package gui_system;

import java.awt.Graphics;
import java.awt.event.MouseWheelEvent;

import main.IMP;



public class PE_File_Preview 
extends Tile {
	
	//gui
	GuiIntValue preview_size;
	GuiIntValue scroll_y;
	
	public PE_File_Preview(GuiIntValue preview_size, GuiIntValue scroll_y) {
		super();
		alignment=BLOCK;
		this.preview_size=preview_size;
		this.scroll_y=scroll_y;
		
		int max_scroll=(int) ((children_size()/(getWidth()/(preview_size.getDouble()+100))+1) * ((preview_size.getInt()+100)/16*10)-getHeight());
		if(max_scroll<=0)
			max_scroll=10000;
		scroll_y.reconfigure(0, max_scroll, 0);
	}
	
	@Override
	public void mouse_wheel(MouseWheelEvent e){
		if(e.isAltDown()){
			preview_size.set_value(preview_size.getInt()+(-e.getWheelRotation()*20));
		}
		else{
			scroll_y.set_value(scroll_y.getInt()+e.getWheelRotation()*80);
		}
		
		int h=(preview_size.getInt()+100)/42*30;
		int x_elements=getWidth()/(preview_size.getInt()+100);
		int v_amount=children_size()/x_elements;
		if(children_size()%x_elements!=0)
			v_amount++;
		int max_scroll=200+v_amount*h-IMP.main_panel.getHeight();
		int current_scroll=scroll_y.getInt();
		if(max_scroll<0)
			max_scroll=0;
		if(current_scroll>max_scroll)
			current_scroll=max_scroll;
		scroll_y.reconfigure(0, max_scroll, current_scroll);
	}

@Override
	public void paint(Graphics g){	
		fill_background(g, color_dark_background);
		for(int i=0;i<children.size();i++){
			if(children.get(i).visible){
				children.get(i).paint(g);
			}
		}
	}
	
	@Override
	public void refresh_children() {
		if(children.size()>0 && getWidth()>0 && getHeight()>0){
			int width=getWidth();

			int average_width=preview_size.getInt()+100;
			int average_height=average_width/42*30;
			
			int x_elements=width/average_width;
			
			int empty_space_in_width=(width-(x_elements*average_width))/2;
//			average_width=width/x_elements;
//			average_height=average_width/16*10;
			
			for(int i=0; i<children.size();i++){
				if(!children.get(i).width_is_fixed)
					children.get(i).set_width((average_width*size_ratio));
				if(!children.get(i).height_is_fixed)
					children.get(i).size.height=average_height*size_ratio;
				
				if(children.get(i).visible){
					children.get(i).get_position().x=empty_space_in_width+this.get_position().x+margin_left + i%x_elements*average_width;
					children.get(i).get_position().y=this.get_position().y+margin_top + i/x_elements*average_height-scroll_y.getInt();
				}
			}
		for(int i=0; i<children.size();i++){
			children.get(i).refresh_children();
		}
		}
	}
}
