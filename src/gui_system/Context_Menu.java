package gui_system;

import gui.Design_Preferences;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import main.Menuelement;

public class Context_Menu 
implements Design_Preferences{

    private static Menuelement root;	//file menu

    
    //right-click menu
	public boolean enabled;
	public int center_x;
	public int center_y;
	private int menu_element_height=25;		//height of a menu element
	public int menu_element_width=240;					//height of a menu element
	private Menuelement current_element;		//currently focused menu element
	private Menuelement current_branch;		//currently focused menu branch (i.e. mother of current_element)
	private boolean first_click;
	
	public boolean release_closes;
	
	public Context_Menu(Menuelement menu) {
        //initialize the right click menu
		Context_Menu.root=menu;
        current_branch=root;
	}
	
	public void set_menu(Menuelement new_menu){
		root=new_menu;
		 current_branch=root;
	}
    
	public void paint(Graphics g){
		int x=center_x;
		int y=center_y;
		current_element=root;
		
		paint_right_click_menu(g,root,x,y);
	}
	
	void paint_right_click_menu(Graphics g, Menuelement current_menu, int x, int y){
		int menu_rectangle_size_y=current_menu.elements.size()*menu_element_height-1;
		
		g.setColor(color_menu_background);
		g.fillRoundRect(x-5, y-5, menu_element_width+10, menu_rectangle_size_y+10, 10, 10);
//		g.fillRect(position_x-5, position_y-5, width+10, height+10);

		//menu border
		g.setColor(color_borders);
		g.drawRoundRect(x-5, y-5, menu_element_width+10, menu_rectangle_size_y+10, 10, 10);
		
		FontMetrics fm = g.getFontMetrics();
		
		for(int i=0;i<current_menu.elements.size();i++){
			if (current_menu.get(i).is_focused()){
				g.setColor(color_contrast);
				g.fillRect(x-4,y+menu_element_height*i,menu_element_width+9,menu_element_height);
				g.setColor(color_text_bright);
				g.drawString(current_menu.get(i).getName(),
						x+((menu_element_width-fm.stringWidth(current_menu.get(i).getName()))/2),
						y+menu_element_height*i+((menu_element_height+10)/2));
				
				if(current_menu.get(i).is_folder()){	//paint next menu layer
						paint_right_click_menu(g,current_menu.get(i),x+menu_element_width,y+menu_element_height*i);
				}
			}
			
			else if (current_menu.get(i).is_focusable()) {
				g.setColor(new Color(200,200,200));
				g.drawString(current_menu.get(i).getName(),
						x+((menu_element_width-fm.stringWidth(current_menu.get(i).getName()))/2),
						y+menu_element_height*i+((menu_element_height+10)/2));
			}
			
			else{
				g.setColor(new Color(120,120,120));
				g.drawString(current_menu.get(i).getName(),
						x+((menu_element_width-fm.stringWidth(current_menu.get(i).getName()))/2),
						y+menu_element_height*i+((menu_element_height+10)/2));
			}
		}
	}
	
	public void mouse_dragged(MouseEvent e){
		go_through_menu(root,center_x,center_y,e);
		
		current_branch.unfocus_all();
		if(current_element.is_focusable())
			current_element.focus();
		
	}
	
	public void mouse_released(MouseEvent e){
		go_through_menu(root,center_x,center_y,e);
		if(current_element.is_focused() && !current_element.is_folder()){
			current_element.apply_action();
			unfocus_all_elements(root);
			enabled=false;
		}
		if(e.getButton()==1 && release_closes)
			enabled=false;
	}
	
	private void unfocus_all_elements(Menuelement current_menu){
		for(int i=0;i<current_menu.elements.size();i++){
			current_menu.unfocus_all();
			if (current_menu.get(i).is_folder())
				unfocus_all_elements(current_menu.get(i));
		}
	}
	
	private void go_through_menu(Menuelement current_menu, int x, int y, MouseEvent e){
		
		if(e.getX()>x && e.getY()>y){
			if(e.getX()<x+menu_element_width){
				for(int i=0;i<current_menu.elements.size();i++){
					if (e.getY()>y+menu_element_height*i && e.getY()<y+menu_element_height*(i+1)){
						current_element=current_menu.elements.get(i);
						current_branch=current_menu;
						break;
					}
				}
			}
			else{
				for(int i=0;i<current_menu.elements.size();i++){
					if (current_menu.get(i).is_focused() && current_menu.get(i).is_folder()) {
						go_through_menu(current_menu.get_focused_element(), x+menu_element_width,y+menu_element_height*i,e);
						break;
					}
				}
			}
		}
	}
	
	public void key_pressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_ESCAPE){
			enabled=false;
		}
	}

	public void mouse_clicked(MouseEvent e) {
		if(first_click){
			enabled=false;
			first_click=false;
		}
		else{
			first_click=true;
		}
	}
	
	public void mouse_moved(MouseEvent e) {
		mouse_dragged(e);
	}

	public void mouse_pressed(MouseEvent e) {
	}

	public void key_released(KeyEvent e) {
	}

	public void key_typed(KeyEvent e) {
	}

	public void mouse_wheel(MouseWheelEvent e) {
	}
	
	public void set_min_width(int min_width){
		menu_element_width=min_width;
	}
}
