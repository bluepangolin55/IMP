package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;

import main.Data_Access;
import main.IMP;
import file.Img;
import gui.Design_Preferences;
import gui.Image_Tile;
import gui_system.GuiIntValue;
import gui_system.Message;
import gui_system.PE_Apply_Abort;
import gui_system.PE_Slider;

public class Routine_New_Image 
extends Routine
implements Data_Access, Design_Preferences{
	
	// values 
	
	GuiIntValue v0;
	GuiIntValue v1;
	
	public Routine_New_Image() {
		super();
		//GUI
		set_name("Erstelle ein neues Bild");
		category = "Datei";
		key_shortcut = 'n';
		
		v0 = new GuiIntValue(1,4000,400);
		v1 = new GuiIntValue(1,4000,400);
		
		
		add_gui_element(new PE_Slider("Breite", v0,this, ""));
		add_gui_element(new PE_Slider("Höhe", v1,this, ""));
		add_gui_element(new PE_Apply_Abort(this));
		
		needs_open_image = false;
	}

	@Override
	public void apply(){
		super.apply();
		IMP.opened_image=new Img(v0.getInt(),v1.getInt(),1);
		IMP.opened_image.set_filename(new File("/home/dimitri/Pictures/untitled.bmp"));
		
//		IMP.main_image_panel.load_image(IMP.opened_image);
		IMP.image_informant.inform_about_new_image(IMP.opened_image);
		IMP.active_image_tile.view.filled_view();
	}
	
	@Override
	public void abort(){
		super.abort();
	}
	
	@Override
	public void mouse_pressed(MouseEvent e){
//		if(IMP.active_image_tile.mouse_x>100 && IMP.active_image_tile.mouse_y>100){
			
		width=Math.abs(IMP.main_panel.mouse_position.x-IMP.active_image_tile.get_size().width/2)*2;
		height=Math.abs(IMP.main_panel.mouse_position.y-IMP.active_image_tile.get_size().height/2)*2;			
			
			v0.set_value(width+additional_length);
			v1.set_value(height+additional_length);
	}
	
	@Override
	public void mouse_dragged(MouseEvent e){
//		if(IMP.active_image_tile.mouse_x>100 && IMP.active_image_tile.mouse_y>100){
			
		width=Math.abs(IMP.main_panel.mouse_position.x-IMP.active_image_tile.get_size().width/2)*2;
		height=Math.abs(IMP.main_panel.mouse_position.y-IMP.active_image_tile.get_size().height/2)*2;		
		
		v0.set_value(width+additional_length);
		v1.set_value(height+additional_length);
		
	}
	
	@Override
	public void mouse_wheel(MouseWheelEvent e){
		if (e.getWheelRotation()==-1){
			additional_length=additional_length+width/10;
		}
		else if (additional_length-50>0){
			additional_length=additional_length-width/10;
		}
		else{
			additional_length=0;
		}
		v0.set_value(width+additional_length);
		v1.set_value(height+additional_length);
	}
	
	@Override
	public void paint_on_screen(Graphics g, Image_Tile tile) {
		int square_x=tile.getWidth()/2-width/2;
		int square_y=tile.getHeight()/2-height/2;;
		int square_width=width;
		int square_height=height;
		
		//background
		g.setColor(color_dark_background);
		g.fillRect(tile.get_position().x, tile.get_position().y, tile.getWidth(), IMP.main_panel.getHeight());
		
		g.setColor(color_dark_background);
		g.fillRect(tile.get_position().x, tile.get_position().y, tile.getWidth()/2, 30);
		
		g.setColor(color_dark_background);
		g.fillRect(IMP.active_image_tile.getWidth()/2, 0, IMP.active_image_tile.getWidth()/2, 30);
		
		//square filling
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(tile.get_position().x+square_x, square_y, width, height);
		//size display in the center of the square
		int display_width=120;
		int display_height=60;
		g.setColor(color_menu_background);
		g.fillRoundRect(tile.get_position().x+square_x+width/2-display_width/2, square_y+height/2-display_height/2, display_width, display_height, 20, 20);
		g.setColor(color_borders);
		g.drawRoundRect(tile.get_position().x+square_x+width/2-display_width/2, square_y+height/2-display_height/2, display_width, display_height, 20, 20);
		
		g.setColor(color_text_bright);
		g.setFont(new Font("Arial", Font.PLAIN, 16));
		FontMetrics fm = g.getFontMetrics();
		String string="" + (v0.getInt()) + " x " + (v1.getInt());
		g.drawString(string, tile.get_position().x+square_x+square_width/2-(fm.stringWidth(string))/2, square_y+square_height/2+(fm.getAscent())/2);
		
		//square borders
		g.setColor(color_contrast);
		g.drawRect(tile.get_position().x+square_x, square_y, width, height);
		
		//title
		g.setColor(color_text_bright);
		g.setFont(new Font("Arial", Font.PLAIN, 36));
		fm = g.getFontMetrics();
		g.drawString("Neues Bild", tile.get_position().x+IMP.active_image_tile.getWidth()/2-(fm.stringWidth("Neues Bild"))/2, 60);
	}
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
	//------------------------------------------------------------------------------------------------------------------------------
			//funcionality
			
//			Function parameters that the user can change in the dialogue. The calculated  values are set to make the filter work in a default way.
	static int width=1000;	//width of the new image
	static int height=1000;	//height of the new image
	private int additional_length;
	
	@Override
	public void receive_message(Message message) {
		
		width = v0.getInt();
		height = v1.getInt();
	}
	
}
