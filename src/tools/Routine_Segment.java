package tools;

import gui_system.*;
import main.IMP;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class Routine_Segment
extends Routine{

	// values
	GuiColorValue color;
	GuiIntValue threshold;

	public Routine_Segment() {
		super();
		set_name("Segmentieren");
		category = "Bild/Farbe";
		
		color = new GuiColorValue();
		threshold = new GuiIntValue(0, 255, 10);

		add_gui_element(new PE_Slider("Grenze", threshold, this, ""));
		add_gui_element(new PE_Color_Choser2("Zu entfernende Farbe", color, this));
		add_gui_element(new PE_Apply_Abort(this));
	}

	@Override
	public void activate(){
		super.activate();
		IMP.image_informant.inform_about_preview_available(true);
		IMP.image_informant.inform_about_preview_changed();
		
		//functionality

		int currentWidth = IMP.opened_image.getWidth();
		int currentHeight= IMP.opened_image.getHeight();

		//reconfigure sliders
//		widthSlider.reconfigure(1,currentWidth*4, currentWidth);
//		heightSlider.reconfigure(1,currentHeight*4, currentHeight);

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
//		if(e.getModifiers()!=8){
//			IMP.active_image_tile.view.img_width();
//			v0.set_value(IMP.opened_image.mouse_x/size_maxBrightness_ratio);
//			paint_preview();
//		}
	}


	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
	//------------------------------------------------------------------------------------------------------------------------------
		//funcionality
//		Function parameters that the user can change in the dialogue. The calculated  values are set to make the filter work in a default way.

		@Override
		public void paint_preview() {
			Raster from = IMP.opened_image.getCompleteImage().getRaster();
			WritableRaster to = IMP.preview_image.getRaster();


			int width = IMP.preview_image.getWidth();
			int height = IMP.preview_image.getHeight();
			float widthRatio = (width + 0f) / (IMP.opened_image.getWidth()+0f);
			float heightRatio = (height+0f) / (IMP.opened_image.getHeight()+0f);

			int bufferSize = 10;
			double[] buffer = new double[bufferSize];
			for(int t=0; t<bufferSize; t++){
				buffer[t] = 200;
			}
			for(int x=0; x<width; x++){
				for(int y=0; y<height; y++){
					from.getPixel(x,y,buffer);
					int diff = (int) Math.abs(buffer[0] - color.get_red());
					diff += (int) Math.abs(buffer[1] - color.get_green());
					diff += (int) Math.abs(buffer[2] - color.get_blue());
					if(x == 100 && y == 100){
						System.out.println("is");
						System.out.println(buffer[0]);
						System.out.println(buffer[1]);
						System.out.println(buffer[2]);
						System.out.println(color.get_red());
					}
					if(diff < threshold.getInt()){
						buffer[0] = 0;
						buffer[1] = 0;
						buffer[2] = 0;
					}
					to.setPixel(x,y, buffer);
				}

			}

						
//			g.drawImage(IMP.opened_image.get_selected_layer().get_image(),0,0,
//			IMP.opened_image.getWidth(),IMP.opened_image.getHeight(),null);

		}

	@Override
	public void receive_message(Message message) {
		paint_preview();
	}
}




