package gui_system;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import tools.Routine;
import main.IMP;

import gui.Design_Preferences;

public class PE_File_Preview_Element
extends Tile
implements Design_Preferences{
	
	//value storage
	GuiIntValue value;
	String action;
	public File image_file;

	//GUI
	String name;
	double drawing_factor;
	int slider_width=200;
	int close_button_pos_x;
	boolean button_pressed;
	public boolean selected;
    public BufferedImage preview_icon=null;
    
    //mouse
    long mouse_click_delay;
	
	//functionality
	private Routine routine;

	public PE_File_Preview_Element(File image_file, String action, Routine routine){		
		this.image_file=image_file;
		this.name=image_file.getName();
		this.action=action;
		this.routine=routine;
		size.width=50;
		size.height=30;
		//GUI
		close_button_pos_x=get_position().x+size.width-(size.width/12);
//		try {
//			preview_icon=ImageIO.read(image_file);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	@Override
	public void mouse_clicked(MouseEvent e) {
		routine.take_action(action);
		if(selected)
			selected=false;
		else
			selected=true;
		routine.take_action("selected");
		
		if(e.getWhen()-mouse_click_delay<220)
			routine.take_action("open");
		mouse_click_delay=e.getWhen();
	}
	
	@Override
	public void mouse_pressed(MouseEvent e) {
		button_pressed=true;
	}
	
	@Override
	public void mouse_released_anywhere(MouseEvent e) {
		super.mouse_released_anywhere(e);
		button_pressed=false;
	}
	
@Override
	public void paint(Graphics g){
	g.setClip(mother.get_position().x, mother.get_position().y, mother.size.width-2, mother.size.height);
	
	int inner_border=10;
	int outer_border=6;
	int image_title_height=30;
	inner_border=inner_border+outer_border;
	
		//background
		if(button_pressed)
			g.setColor(color_contrast);
		else
			g.setColor(color_menu_background);
		
		if(selected)
			g.setColor(color_contrast);

		g.fillRoundRect(get_position().x+outer_border, get_position().y+outer_border, size.width-2*outer_border, size.height-2*outer_border,20,20);
		if(is_hovered(IMP.main_panel.mouse_position) || selected){
			g.setColor(color_bright_opaque_layer);
			g.fillRoundRect(get_position().x+outer_border, get_position().y+outer_border, size.width-2*outer_border, size.height-2*outer_border,20,20);
		}
		
		
		//the preview image
		if(preview_icon!=null){
			int image_width;
			int image_height;
			int image_pos_x;
			int image_pos_y;
			
			double ratio=(double) (preview_icon.getHeight())/preview_icon.getWidth();
			if(preview_icon.getWidth()/16*10>preview_icon.getHeight()){
				image_width=size.width-2*inner_border;
				image_height=(int) (image_width*ratio);
			}
			else{
				image_height=size.height-2*inner_border;
				image_width=(int) (image_height/ratio);
			}
			
			image_pos_x=get_position().x+(size.width-image_width)/2;
			image_pos_y=get_position().y+(size.height-image_height)/2;
			
			g.drawImage(preview_icon, image_pos_x, image_pos_y, image_width, image_height, null);
		}
		
//		delete button
//		if(is_hovered(IMP.main_panel.mouse_position)){
//			g.setColor(color_contrast);
//			g.fillOval(get_position().x + size.width/16*15, get_position().y+ size.width/28, size.width/25, size.width/25);
//			g.setColor(color_menu_background);
//			g.drawOval(get_position().x + size.width/16*15, get_position().y+ size.width/28, size.width/25, size.width/25);
//		}
		
		//label
		g.setColor(color_dark_opaque_layer2);
		if(selected)
			g.setColor(color_menu_background);
		g.setClip(get_position().x+outer_border, get_position().y+size.height-outer_border-image_title_height/2,
			size.width-2*outer_border, image_title_height);
		g.fillRoundRect(get_position().x+outer_border, get_position().y+size.height-outer_border-image_title_height,
			size.width-2*outer_border, image_title_height,20,50);
		g.setClip(get_position().x+outer_border, get_position().y+size.height-outer_border-image_title_height,
				size.width-2*outer_border, image_title_height);
		g.fillRect(get_position().x+outer_border, get_position().y+size.height-outer_border-image_title_height,
			size.width-2*outer_border, image_title_height/2);
		

		g.setColor(color_text_bright);
		Font f = new Font("Calibri", Font.BOLD, 11);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		if(IMP.main_panel.mouse_position.x>get_position().x+outer_border+1 && 
				IMP.main_panel.mouse_position.x<get_position().x+outer_border+1 + size.width-2*outer_border-2 &&
				IMP.main_panel.mouse_position.y>get_position().y+size.height-outer_border-image_title_height &&
				IMP.main_panel.mouse_position.y<get_position().y+size.height-outer_border-image_title_height + image_title_height)
		g.drawString("1980x1200",get_position().x+size.width/2-(fm.stringWidth("1980x1200"))/2,
				get_position().y+size.height - outer_border - (fm.getAscent()+fm.getDescent()));
		else
			g.drawString(name,get_position().x+size.width/2-(fm.stringWidth(name))/2,
					get_position().y+size.height - outer_border - (fm.getAscent()+fm.getDescent()));
		
		
		//borders
		g.setClip(mother.get_position().x, mother.get_position().y, mother.size.width-2, mother.size.height);
		g.setColor(color_borders);
		g.drawRoundRect(get_position().x+outer_border, get_position().y+outer_border, size.width-1-2*outer_border, size.height-2*outer_border,20,20);
	}

	public String get_filename(){
		return name;
	}

	public File get_file() {
		return image_file;
	}
}

