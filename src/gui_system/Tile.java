package gui_system;

import file.Img;
import gui.Design_Preferences;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

import main.IMP;
import main.Menuelement;

public class Tile
implements Design_Preferences{
	
//	protected Main_GUI_Panel image_panel;
	
	protected Context_Menu context_menu;
	protected Menuelement menu;
	
	protected GuiClient client;
	protected String clientMessage; 
	
	//GUI
	private Point position;
	protected Dimension size;
	public boolean visible;
	protected int last_element_x;
	protected int last_element_y;
	public boolean fixed_size_ratio;
	public boolean width_is_fixed;
	public boolean height_is_fixed;
	public int alignment;
	protected int size_ratio;	//how much space this panel wants to take in relation to the other panels
	protected int margin_top;
	protected int margin_bottom;
	protected int margin_left;
	protected int margin_right;
	protected Color background_color;
	
	public boolean size_is_fixed(){
		return (width_is_fixed && height_is_fixed);
	}
	
	//constants
	final static public int HORIZONTAL=0;
	final static public int VERTICAL=1;
	final static public int BLOCK=2;
	
	protected ArrayList<Tile> children;
	Tile mother;
	
	protected ArrayList<Tile> listeners = new ArrayList<Tile>();
	
	public Tile() {
		set_position(new Point());
		size=new Dimension();
		children=new ArrayList<Tile>();
		size_ratio=1;
		visible=true;
	}
	
	public Tile(GuiClient client) {
		this.client = client;
		set_position(new Point());
		size=new Dimension();
		children=new ArrayList<Tile>();
		size_ratio=1;
		visible=true;
	}
	
	public Tile(int alignment) {
		set_position(new Point());
		size=new Dimension(0,0);
		children=new ArrayList<Tile>();
		size_ratio=1;
		this.alignment=alignment;
		visible=true;
	}
	
	public Tile(int alignment, Dimension size) {
		set_position(new Point());
		this.size=size;
		children=new ArrayList<Tile>();
		size_ratio=1;
		this.alignment=alignment;
		visible=true;
	}
	
	public void set_context_menu(Menuelement menu){
		context_menu = new Context_Menu(menu);
		this.menu=menu;
	}
	
	public void paint(Graphics g){	
		if(background_color!=null)
			fill_background(g, background_color);
		for(int i=0;i<children.size();i++){
			if(children.get(i).visible){
				g.setClip(new Rectangle(children.get(i).get_position().x, children.get(i).get_position().y, children.get(i).size.width, children.get(i).size.height));
				children.get(i).paint(g);
			}
		}
	}
	
	protected void fill_background(Graphics g, Color color){
		g.setColor(color);
		g.fillRect(get_position().x,get_position().y, size.width, size.height);
	}
	
	void draw_borders(Graphics g, Color color){
		g.setColor(color);
		g.drawRect(get_position().x,get_position().y, size.width-1, size.height-1);
	}

	public void mouse_moved(MouseEvent e){
		for(int i=0; i<children.size();i++){
			if(children.get(i).visible && children.get(i).is_hovered(e.getPoint()))
				children.get(i).mouse_moved(e);
		}
	}
	
	public void mouse_clicked(MouseEvent e){
		for(int i=0; i<children.size();i++)
			if(children.get(i).visible && children.get(i).is_hovered(e.getPoint()))
				children.get(i).mouse_clicked(e);
	}
	
	public void mouse_pressed(MouseEvent e){
		for(int i=0; i<children.size();i++)
			if(children.get(i).visible && children.get(i).is_hovered(e.getPoint()))
				children.get(i).mouse_pressed(e);
	}
	
	public void mouse_dragged(MouseEvent e){
		for(int i=0; i<children.size();i++){
			children.get(i).mouse_dragged_anywhere(e);
			if(children.get(i).visible && children.get(i).is_hovered(e.getPoint()))
				children.get(i).mouse_dragged(e);
		}
	}
	
	public void mouse_dragged_anywhere(MouseEvent e){
		for(int i=0; i<children.size();i++)
			children.get(i).mouse_dragged_anywhere(e);
	}
	
	public void mouse_released(MouseEvent e){
		for(int i=0; i<children.size();i++){
			children.get(i).mouse_released_anywhere(e);
			if(children.get(i).visible && children.get(i).is_hovered(e.getPoint()))
				children.get(i).mouse_released(e);
		}
	}
	
	public void mouse_released_anywhere(MouseEvent e) {
		for(int i=0; i<children.size();i++){
				children.get(i).mouse_released_anywhere(e);
		}
	}

	public void mouse_wheel(MouseWheelEvent e){
		for(int i=0; i<children.size();i++){
			if(children.get(i).visible && children.get(i).is_hovered(e.getPoint()))
				children.get(i).mouse_wheel(e);
		}
	}
	
	public void key_pressed(KeyEvent e) {
		for(int i=0; i<children.size();i++){
			if(children.get(i).visible && children.get(i).is_hovered(IMP.main_panel.mouse_position))
				children.get(i).key_pressed(e);
		}
	}
	
	public void key_released(KeyEvent e) {
		for(int i=0; i<children.size();i++){
			if(children.get(i).visible && children.get(i).is_hovered(IMP.main_panel.mouse_position))
				children.get(i).key_released(e);
		}
	}
	
	public void key_typed(KeyEvent e) {
		for(int i=0; i<children.size();i++){
			if(children.get(i).visible && children.get(i).is_hovered(IMP.main_panel.mouse_position))
				children.get(i).key_typed(e);
		}
	}
	
	protected boolean is_hovered(Point point) {
		if(point.x>get_position().x && point.x<get_position().x+size.width &&
				point.y>get_position().y && point.y<get_position().y+size.height)
			return true;
		else
			return false;
	}

	public boolean is_hovered(MouseEvent e){
		if(e.getX()>get_position().x && e.getX()<get_position().x+size.width &&
				e.getY()>get_position().y && e.getY()<get_position().y+size.height)
			return true;
		else
			return false;
	}
	
	public void remove_this(){
		if(mother!=null)
			mother.remove(this);
	}

	public void remove(Tile panel_to_remove) {
		panel_to_remove.set_mother(null);
		children.remove(panel_to_remove);
	}
	
	public void remove_all_children() {
		children.clear();
	}
	
	public void add(Tile panel_to_add){
		panel_to_add.set_mother(this);
		children.add(panel_to_add);
		refresh_children();
	}
	
	public void refresh_children() {
		if(children.size()>0 && getWidth()>0 && getHeight()>0){
			if(alignment==HORIZONTAL){
				last_element_x=0;
				int average_width=getWidth();
				for(int i=0; i<children.size();i++){
					if(children.get(i).width_is_fixed==true)
							average_width=average_width-children.get(i).getWidth();
				}
				average_width=average_width/count_children_with_fixed_size(false, false);
				for(int i=0; i<children.size();i++){
	//				average_width=(this.size.width-margin_right-margin_left-last_element_x)/(count_visible_children_after_i(i));
					if(!children.get(i).width_is_fixed)
						children.get(i).set_width((average_width*size_ratio));
					children.get(i).get_position().x=this.get_position().x+last_element_x+margin_left;
					if(children.get(i).visible)
						last_element_x=last_element_x+children.get(i).size.width;
					children.get(i).get_position().y=this.get_position().y;
					
					if(!children.get(i).height_is_fixed)
						children.get(i).set_height(this.size.height);
				}
			}
			else if(alignment==VERTICAL){
				last_element_y=0;
				int average_height=getHeight();
				for(int i=0; i<children.size();i++){
					if(children.get(i).height_is_fixed==true)
						average_height=average_height-children.get(i).getHeight();
				}
				average_height=average_height/count_children_with_fixed_size(false, false);
				for(int i=0; i<children.size();i++){
	//				average_height=(this.size.height-last_element_y)/(count_visible_children_after_i(i));
					children.get(i).get_position().x=this.get_position().x+margin_left;
					if(!children.get(i).height_is_fixed)
						children.get(i).size.height=average_height*size_ratio;
					children.get(i).get_position().y=this.get_position().y+last_element_y;
					if(children.get(i).visible)
						last_element_y=last_element_y+children.get(i).size.height;
					
					if(!children.get(i).width_is_fixed)
						children.get(i).set_width(this.size.width-margin_left-margin_right);
				}
			}
			else if(alignment==BLOCK){
				last_element_y=0;
				
				double width=getWidth();
				double height=getHeight();
				int x_elements=(int) Math.sqrt(children.size()*width/height);
				int y_elements=children.size()/x_elements+1;
				int average_width=getWidth()/x_elements;
				int average_height=getHeight()/y_elements;
				
				for(int i=0; i<children.size();i++){
					if(!children.get(i).width_is_fixed)
						children.get(i).set_width((average_width*size_ratio));
					if(!children.get(i).height_is_fixed)
						children.get(i).size.height=average_height*size_ratio;
					
					if(children.get(i).visible){
						children.get(i).get_position().x=this.get_position().x+margin_left + i%x_elements*average_width;
						children.get(i).get_position().y=this.get_position().y+margin_top +  i/x_elements*average_height;
					}
				}
			}
			for(int i=0; i<children.size();i++){
				children.get(i).refresh_children();
			}
		}
	}
	
	private int count_children_with_fixed_size(boolean fixed_width, boolean fixed_height){
		int amount=0;
		if(fixed_width && fixed_height)
			for(int i=0; i<children.size();i++){
				if(children.get(i).visible && children.get(i).width_is_fixed==true && children.get(i).height_is_fixed==true)
					amount++;
			}
		else if(fixed_width)
			for(int i=0; i<children.size();i++){
				if(children.get(i).visible && children.get(i).width_is_fixed==true)
					amount++;
			}
		else if(fixed_height)
			for(int i=0; i<children.size();i++){
				if(children.get(i).visible && children.get(i).height_is_fixed==true)
					amount++;
			}
		else
			for(int i=0; i<children.size();i++){
				if(children.get(i).visible && children.get(i).width_is_fixed==false && children.get(i).height_is_fixed==false)
					amount++;
			}
		if (amount==0)
			amount=1;	
		return amount;
	}
	
	public void set_mother(Tile mother) {
		this.mother=mother;
	}
	
	public void set_position(Point position){
		this.position=position;
	}
	
	public void set_size(Dimension size){
		this.size=size;
		refresh_children();
	}
	
	public void set_width(int width){
		if(!width_is_fixed)
			size.width=width;
	}
	
	public void set_height(int height){
		if(!height_is_fixed)
			size.height=height;
	}
	
	public void set_visible(boolean b){
		visible=b;
	}
	
	public void set_background_color(Color new_background_color) {
		background_color=new_background_color;
	}
	
	public int getWidth(){
		return size.width;
	}
	
	public int getHeight(){
		return size.height;
	}

	public Point get_position() {
		return position;
	}
	
	public Dimension get_size() {
		return size;
	}
	
	public Tile get_child(int index){
		return children.get(index);
	}

	public int children_size() {
		return children.size();
	}
	
	public void hide(){
		System.out.println("haladfo");
		set_visible(false);
		System.out.println(mother.alignment);
	}
	
	public void send_message(String action){
		
	}
	
	public void add_listener(Tile listener){
		listeners.add(listener);
	}
	
	public void broadcast(String message){
		for(Tile t : listeners){
			t.receive_message(message);
		}
	}
	
	public void receive_message(String message){
		
	}

	public void new_image_loaded(Img new_image) {
		// TODO Auto-generated method stub
		
	}

	public void image_changed() {
		// TODO Auto-generated method stub
		
	}

	public void selection_changed() {
		// TODO Auto-generated method stub
		
	}

	public void preview_changed() {
		// TODO Auto-generated method stub
		
	}

	public void set_preview_available(boolean preview_available) {
	}
	
	public void message_client(){
		Message m = new Message(this, "");
		client.receive_message(m);
	}
	
	public void message_client(String custom_message){
		Message m = new Message(this, custom_message);
		client.receive_message(m);
	}
	
}
