package gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import file.Img;
import gui_system.CursorControl;
import gui_system.HUD_Ruler;
import gui_system.HUD_Start;
import gui_system.Context_Menu;
import gui_system.Tile;

import main.IMP;

public class Image_Tile 
extends Tile{
	
	public Img img;
	public View view;
	
	//mouse
	public int mouse_x;
	public int mouse_y;
	protected boolean mouse_dragged=false;
	private boolean middle_mouse_button_pressed=false;

	//keys
	private boolean alt_is_pressed;
	private boolean shift_is_pressed;
	private boolean control_is_pressed;
	private boolean space_is_pressed;
	
	
	//gui
	//hud elements
	HUD_Ruler ruler = new HUD_Ruler(this);
	HUD_Start start_dialogue = new HUD_Start(this);
	
	public boolean preview_available;
	
	public Image_Tile() {
		IMP.image_informant.add_client(this);
		view=new View(this);
	}
	

	@Override
	public void paint(Graphics g) {
		if(preview_available){
			fill_background(g, color_image_background);
			if(file.Img.fileMade==true){
				g.drawImage(IMP.preview_image,view.img_pos_x(), view.img_pos_y(),
						(int) (IMP.preview_image.getWidth()*view.zoom),(int) (IMP.preview_image.getHeight()*view.zoom),null);	//Draws the Previewimage, (img,posx,posy,sizex,sizey);
			}
			if(ruler.enabled)
				ruler.paint(g);
		}
		else if(IMP.opened_image!=null){
			fill_background(g, color_image_background);
			
//			g.fillRect(view.img_pos_x()+view.img_width(), 0,
//					this.getWidth()-view.img_pos_x()+view.img_width(), this.getHeight());
//			g.fillRect(view.img_pos_x(), 0,view.img_width(), view.img_pos_y());
//			g.fillRect(view.img_pos_x(), view.img_pos_y()+view.img_height(),
//					view.img_width(), this.getHeight()-view.img_pos_y()+view.img_height());
			
			//+++++++++++++++paints the layers one over the other
			//img.composeCompleteImage();
			g.drawImage(img.getCompleteImage(), view.img_pos_x(), 
					view.img_pos_y(), view.img_width(), view.img_height(), null);
//			for(int i=img.amountOfLayers-1;i>-1;i--){
//				if(img.layers.get(i).isVisible()){
//					img.layers.get(i).draw(g, view.img_pos_x(), view.img_pos_y(), view.img_width(), view.img_height());
//					//this change is done, to support different kinds of layers (i.e. vector layers, groups, ...)
//					//g.drawImage(img.layers.get(i).get_image(),view.img_pos_x(),view.img_pos_y(),
//					//		view.img_width(),view.img_height(),null);	//Draws the image, (img,posx,posy,sizex,sizey);
//				}
//			}
			
			//paint selection
			if(img.active_selections!=null){
				int i;
				for(int e=0;e<img.active_selections.size();e++){
					img.active_selections.get(e).draw_line(g, this);
//					img.active_selections.get(e).draw_points(g);
					g.setColor(new Color(40,40,40));
					Polygon selectionDisplay=new Polygon();
					g.setColor(new Color(40,40,40));
					g.drawPolygon(selectionDisplay);
				}
			}
			if(ruler.enabled)
				ruler.paint(g);
		}
		else
			start_dialogue.paint(g);
		
		//paints elements from the active_tool over the image
		IMP.active_tool.paint_on_screen(g, this);
	}
	
	
@Override
	public void key_pressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SHIFT){
			shift_is_pressed=true;
		}
		else if (key == KeyEvent.VK_CONTROL){
			control_is_pressed=true;
		}
		else if (key == KeyEvent.VK_ALT){
			alt_is_pressed=true;
			CursorControl.changeCursor(5);
		}
		else{
			IMP.active_tool.key_pressed(e);	//route event to the active tool or routine 
		}
	}

@Override
	public void key_released(KeyEvent e) {
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_SHIFT){
			shift_is_pressed=false;
		}
		else if (key == KeyEvent.VK_CONTROL){
			control_is_pressed=false;
		}
		else if (key == KeyEvent.VK_ALT){
			CursorControl.changeCursor(IMP.active_tool.cursor);
			alt_is_pressed=false;
		}
		else if (key == KeyEvent.VK_SPACE){
			space_is_pressed=false;
			CursorControl.changeCursor(IMP.active_tool.cursor);
		}
		
		IMP.active_tool.key_released(e);	//route event to the active tool or routine 
	}

@Override
	public void key_typed(KeyEvent event) {
		int key =  event.getKeyChar();		
		IMP.active_tool.key_typed(event);	//route event to the active tool or routine 
		if (key == 'g'){
//			view.zoom_in();
//			view.centered_view();
		}
		if (key == 'h'){
//			view.zoom_out();
//			view.centered_view();
		}
		
		else if (key == '9'){
			view.filled_view();
		}
		
		else if (key == '0'){
			view.real_view();
		}
	}
	
@Override
	public void mouse_moved(MouseEvent e) {
	IMP.active_image_tile=this;
		if(img!=null){
//			img.mouse_x=(int) ((e.getX()-26-view.IHomePosX-view.shiftX)/view.zoom-get_position().x);
//			img.mouse_y=(int) ((e.getY()-26-view.IHomePosY-view.shiftY)/view.zoom-get_position().y);
			img.mouse_x=(int) ((e.getX()-view.img_pos_x())/view.zoom);
			img.mouse_y=(int) ((e.getY()-view.img_pos_y())/view.zoom);
		}
	
		if(view_button_pressed()){		//the control button shouldn't have anything to do with tools
			int move=e.getX()-mouse_x;
			if(move!=0){
				view.shiftX=view.shiftX+move;
			}
			move=e.getY()-mouse_y;
			if(move!=0){
				view.shiftY=view.shiftY+move;
			}
		}
		else{
			IMP.active_tool.mouse_moved(e);
		}
		
		mouse_x=e.getX();
		mouse_y=e.getY();
	}

@Override
	public void mouse_dragged(MouseEvent e) {
		if(img!=null){
			img.mouse_x=(int) ((e.getX()-view.img_pos_x())/view.zoom);
			img.mouse_y=(int) ((e.getY()-view.img_pos_y())/view.zoom);
		}
		int i=e.getButton();
		
		mouse_dragged=true;
		if(e.isAltDown() || e.getModifiers()==8){		//the control button shouldn't have anything to do with tools
			int move=e.getX()-mouse_x;
			if(move!=0){
				view.shiftX=view.shiftX+move;
			}
			move=e.getY()-mouse_y;
			if(move!=0){
				view.shiftY=view.shiftY+move;
			}
	
			mouse_x=e.getX();
			mouse_y=e.getY();
		}	
		IMP.active_tool.mouse_dragged(e);
		
//		mouse_x=e.getX();
//		mouse_y=e.getY();
	}

@Override
	public void mouse_pressed(MouseEvent e) {
//		mouse_x=e.getX();
//		mouse_y=e.getY();
		if(e.getButton()==1){
			if(context_menu!=null && context_menu.enabled)
				context_menu.mouse_pressed(e);
			else if(view_button_pressed()){		//the control button shouldn't have anything to do with tools
				CursorControl.changeCursor(5);
			}
			else {
				IMP.active_tool.mouse_pressed(e);
			}
		}
		else if(e.getButton()==2){
			middle_mouse_button_pressed=true;
			IMP.active_tool.mouse_pressed(e);
			CursorControl.changeCursor(5);
		}
		else if(e.getButton()==3){
			if(context_menu!=null){
				
				context_menu=new Context_Menu(menu);
				context_menu.enabled=true;
				context_menu.center_x=mouse_x-context_menu.menu_element_width/2;
				context_menu.center_y=mouse_y-10;
				
				IMP.main_panel.context_menu=context_menu;
			}
		}
	}

@Override
	public void mouse_released(MouseEvent e) {
		mouse_x=e.getX();
		mouse_y=e.getY();
		
		if(e.getButton()==MouseEvent.BUTTON1){
			if(view_button_pressed()){		//the control button shouldn't have anything to do with tools
				CursorControl.changeCursor(5);
			}
			else {
				IMP.active_tool.mouse_released(e);
			}
		}
		else if(e.getButton()==2){
			middle_mouse_button_pressed=false;
			CursorControl.changeCursor(IMP.active_tool.cursor);
		}
}

@Override
	public void mouse_clicked(MouseEvent e) {
			IMP.active_tool.mouse_clicked(e);
	}

@Override
	public void mouse_wheel(MouseWheelEvent e) {
		mouse_x=e.getX();
		mouse_y=e.getY();
		
		if (view_button_pressed()){
			if (e.getWheelRotation()==-1){
				view.zoom_in();			}
			else{
				view.zoom_out();
			}
		}
		else if(menu_button_pressed()){	
			IMP.active_tool.mouse_wheel(e);
		}
		else{
				view.shiftY=view.shiftY+10*e.getWheelRotation();
		}
		IMP.active_tool.mouse_wheel(e);
	}

	@Override
	public void new_image_loaded(Img new_image) {
		img=new_image;
		view.img=new_image;
//		this.paint(IMP.main_panel.getGraphics());
	}

	//key mappings
	private boolean view_button_pressed(){
		return alt_is_pressed;
	}
	
	private boolean menu_button_pressed(){
		return space_is_pressed;
	}
	
	@Override
	public void image_changed() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void selection_changed() {
		// TODO Auto-generated method stub
		
	}

@Override
	public void set_preview_available(boolean preview_available) {
		this.preview_available=preview_available;
	}
	
	@Override
	public void preview_changed() {
		// TODO Auto-generated method stub
		
	}
}
