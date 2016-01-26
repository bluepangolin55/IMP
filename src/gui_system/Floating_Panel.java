package gui_system;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import tools.Routine;
import tools.Tool;

import gui.Design_Preferences;

import main.IMP;

public class Floating_Panel 
extends HUD_Element
implements Design_Preferences{
	
	//GUI
	private Point position;
	private static Point fixed_position;
	boolean position_fixed;
	private int width=500;
	private int height=200;
	private int title_height=20;
	public boolean pressed;
	boolean close_button_pressed;
	
	public boolean grabbed;
	private Point grabbing_point;
	
	Tool tool;
	Routine routine;
	
	//for drawing the GUI
	protected String title;
	public ArrayList<Tile> elements;
	protected int last_element_x=0;
	protected int last_element_y=20;
	private int scroll_y;
	
	public Floating_Panel(Tool tool) {
		set_enabled(false);
		this.tool=tool;
		elements=new ArrayList<Tile>();
		position=new Point();
		
		last_element_y=20;
	}
	
	public Floating_Panel(Routine routine) {
		set_enabled(false);
		elements=new ArrayList<Tile>();
		position=new Point();
		
		last_element_y=20;
	}
	
	@Override
	public void mouse_dragged(MouseEvent e){
		super.mouse_dragged(e);
		if(grabbed)
			reset_position(e.getX(), e.getY());
		else{
			for(int i=0;i<elements.size();i++){
				if(e.getX()>elements.get(i).get_position().x && 
						e.getX()<elements.get(i).get_position().x+elements.get(i).size.width &&
						e.getY()+scroll_y>elements.get(i).get_position().y && 
						e.getY()+scroll_y<elements.get(i).get_position().y+elements.get(i).size.height){
					elements.get(i).mouse_dragged(e);
				}
			}
		}
	}
	
	@Override
	public void mouse_clicked(MouseEvent e) {
		super.mouse_clicked(e);
		
		if(e.getY()<position.y+title_height){
			if(e.getX()>position.x+width-15 && e.getY()<position.y+10){
				if(position_fixed){
					position_fixed=false;
				}
				else{
					position_fixed=true;
					fixed_position=new Point(position.x, position.y);
				}	
			}
			else if(e.getX()<position.x+20){
				enabled=false;
			}
		}
		else{
			for(int i=0;i<elements.size();i++){
				if(e.getX()>elements.get(i).get_position().x && 
						e.getX()<elements.get(i).get_position().x+elements.get(i).size.width &&
						e.getY()+scroll_y>elements.get(i).get_position().y && 
						e.getY()+scroll_y<elements.get(i).get_position().y+elements.get(i).size.height){
					elements.get(i).mouse_clicked(e);
				}
			}
		}
	}
	
	@Override
	public void mouse_pressed(MouseEvent e){
		pressed=true;
		if(e.getY()<position.y+title_height){
			if(e.getX()<position.x+20){
				close_button_pressed=true;
			}
			else{
				grabbed=true;
				grabbing_point=new Point(e.getX()-position.x,e.getY()-position.y);		//grabbing point in relation to position_x and position_y
			}
		}
		
		for(int i=0;i<elements.size();i++){
			if(e.getX()>elements.get(i).get_position().x && 
					e.getX()<elements.get(i).get_position().x+elements.get(i).size.width &&
					e.getY()+scroll_y>elements.get(i).get_position().y && 
					e.getY()+scroll_y<elements.get(i).get_position().y+elements.get(i).size.height){
				elements.get(i).mouse_pressed(e);
			}
		}
	}
	
	@Override
	public void mouse_released(MouseEvent e){
		pressed=false;
		grabbed=false;
		close_button_pressed=false;
		for(int i=0;i<elements.size();i++){
			if(e.getX()>elements.get(i).get_position().x && 
					e.getX()<elements.get(i).get_position().x+elements.get(i).size.width &&
					e.getY()+scroll_y>elements.get(i).get_position().y && 
					e.getY()+scroll_y<elements.get(i).get_position().y+elements.get(i).size.height){
				elements.get(i).mouse_released(e);
			}
			elements.get(i).mouse_released_anywhere(e);
		}
	}
	
	public void reset_position(){
		//reset position of the panel
		height=title_height;
		for(int i=0;i<elements.size();i++){
			height+=elements.get(i).size.height;
		}
		if(!position_fixed && IMP.active_image_tile!=null){
			position.x=IMP.active_image_tile.mouse_x-width/2;
			position.y=IMP.active_image_tile.mouse_y-height/2;
		}
		last_element_y=title_height;
		
		for(int i=0;i<elements.size();i++){
			elements.get(i).get_position().x=position.x;
			elements.get(i).get_position().y=position.y+last_element_y;
			last_element_y=last_element_y+elements.get(i).size.height;
		}
	}
	
	public void reset_size(){
		//reset size of the panel
		height=title_height;
		for(int i=0;i<elements.size();i++){
			height+=elements.get(i).size.height;
		}
	}
	
	public void reset_position(int x, int y){
		//reset position of the panel
		height=title_height;
		for(int i=0;i<elements.size();i++){
			height+=elements.get(i).size.height;
		}
		position.x=x-grabbing_point.x;
		position.y=y-grabbing_point.y;
		last_element_y=title_height;
		
		for(int i=0;i<elements.size();i++){
			elements.get(i).get_position().x=position.x;
			elements.get(i).get_position().y=position.y+last_element_y;
			last_element_y=last_element_y+elements.get(i).size.height;
			elements.get(i).refresh_children();
		}
	}
	
	public boolean is_hovered(MouseEvent e){
		if(enabled && e.getX()>position.x-5 && e.getX()<position.x+width
				&& e.getY()>position.y-5 && e.getY()<position.y+height)
			return true;
		else	
			return false;
	}
	
	public boolean is_hovered(Point point){
		if(enabled && point.x>position.x-5 && point.x<position.x+width
				&& point.y>position.y-5 && point.y<position.y+height)
			return true;
		else	
			return false;
	}

	public void add(Tile element){
		element.get_position().x=position.x;
		element.get_position().y=position.y+last_element_y;
		element.set_width(width);
		last_element_y=last_element_y+element.size.height;
		elements.add(element);
		reset_size();
	}
	
	
	@Override
	public void paint(Graphics g){
		if(enabled){
				//border
				g.setColor(color_dark_background);
				g.fillRoundRect(position.x-5, position.y-5, width+10, height+10, 20, 20);
				g.setColor(color_borders);
				g.drawRoundRect(position.x-5, position.y-5, width+10, height+10, 20, 20);

				//close button on the left side
				g.setColor(Color.LIGHT_GRAY);
				if(close_button_pressed){
					g.setColor(color_abort);
					g.fillOval(position.x+4, position.y, 14, 14);
				}
				g.fillOval(position.x+4, position.y, 14, 14);
				g.setColor(color_dark_background);
				g.drawLine(position.x+4, position.y, position.x+18, position.y+14);
				g.drawLine(position.x+18, position.y, position.x+4, position.y+14);
				g.drawOval(position.x+4, position.y, 14, 14);
				
				//pin button on the right side
				g.setColor(Color.LIGHT_GRAY);
				if(position_fixed){
					g.setColor(color_contrast);
					g.fillOval(position.x+width-15, position.y+2, 8, 8);
				}
				g.drawOval(position.x+width-15, position.y+2, 8, 8);
				
				//text
				g.setFont(new Font("Helvetica", Font.BOLD, 12));
				FontMetrics fm = g.getFontMetrics();
				g.setColor(color_text_title);
				g.drawString(tool.get_name(), position.x+((width-fm.stringWidth("Auswahlwerkzeug"))/2),position.y+12);
				g.setFont(new Font("Helvetica", Font.PLAIN, 12));
				
				for(int i=0;i<elements.size();i++){
					elements.get(i).paint(g);
				}
				
//				g.setColor(new Color(220,220,220));
//				g.drawRect(position_x,position_y,width,height);
//				g.drawRect(position_x,position_y,width,20);
			}
	}
	
	@Override
	public void key_pressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(!enabled){
			if (key == KeyEvent.VK_SPACE){
				reset_position();
				enabled=true;
			}
		}
		else{
			if (key == KeyEvent.VK_SHIFT){
			}
			else if (key == KeyEvent.VK_CONTROL){
			}
			else if (key == KeyEvent.VK_ALT){
			}
			else if (key == KeyEvent.VK_SPACE){
				enabled=false;
				System.out.println("asdfasd");
			}
			else if (key == KeyEvent.VK_ESCAPE){
				enabled=false;
			}
			else{
				for(int i=0;i<elements.size();i++){
					elements.get(i).key_pressed(e);
				}
			}
		}
	}

	public void key_typed(KeyEvent e) {
		for(int i=0;i<elements.size();i++){
			elements.get(i).key_typed(e);
		}
	}
}
