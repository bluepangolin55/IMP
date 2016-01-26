package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.IMP;
import file.Img;
import gui_system.GuiIntValue;
import gui_system.PE_Apply_Abort;
import gui_system.PE_Slider;

public class Routine_Brightness 
extends Routine{
	
	// values 
	GuiIntValue v0;

	public Routine_Brightness() {
		super();
		set_name("Helligkeit");
		category = "Bild/Farbe";
		
		v0 = new GuiIntValue(1,510,255);
		
		add_gui_element(new PE_Slider("Helligkeit", v0, this, ""));
		add_gui_element(new PE_Apply_Abort(this));
	}

	@Override
	public void activate(){
		super.activate();
		IMP.image_informant.inform_about_preview_available(true);
		IMP.image_informant.inform_about_preview_changed();
		
		//functionality
		size_maxBrightness_ratio=(double) (IMP.opened_image.getWidth())/510;
		
		//reconfigure sliders
		v0.reconfigure(1,510,255);
		
		//IMP.preview_image=new Img(IMP.opened_image.getWidth(),IMP.opened_image.getHeight(),1);
		IMP.preview_image = new BufferedImage(IMP.opened_image.getWidth(), 
				IMP.opened_image.getHeight(), IMP.opened_image.getImage().getType());
		paint_preview();
	}
	
	@Override
	public void apply(){
		super.apply();
		//IMP.opened_image.get_selected_layer().set_image(IMP.preview_image.getImage());
	}
	@Override
	public void abort(){
		super.abort();
	}
	
	@Override
	public void mouse_pressed(MouseEvent e){
	}
	
	@Override
	public void mouse_dragged(MouseEvent e){
		if(e.getModifiers()!=8){
			IMP.active_image_tile.view.img_width();
			v0.set_value(IMP.opened_image.mouse_x/size_maxBrightness_ratio);
			paint_preview();
		}
	}


	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
	//------------------------------------------------------------------------------------------------------------------------------
		//funcionality
//		Function parameters that the user can change in the dialogue. The calculated  values are set to make the filter work in a default way.
		static int brightness=0;
		static int darkness=0;
		private double size_maxBrightness_ratio;	//the image size divided through the maximum brightness value
		
		@Override
		public void paint_preview() {
			Graphics g=IMP.preview_image.getGraphics();
						
			g.drawImage(IMP.opened_image.get_selected_layer().get_image(),0,0,
			IMP.opened_image.getWidth(),IMP.opened_image.getHeight(),null);
			if(v0.getInt()>=255){
				g.setColor(new Color(255,255,255,v0.getInt()-255));
			}
			else{
				g.setColor(new Color(0,0,0,255-v0.getInt()));
			}
			if(IMP.opened_image.active_selections.size()>0){
				for(int i=0;i<IMP.opened_image.active_selections.size();i++){
					g.fillPolygon(IMP.opened_image.active_selections.get(i).points);
				}
			}
			else
				g.fillRect(0,0,IMP.opened_image.getWidth(),IMP.opened_image.getHeight());
		}
	}
