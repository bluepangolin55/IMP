package tools;

import gui_system.GuiIntValue;
import gui_system.Message;
import gui_system.PE_Apply_Abort;
import gui_system.PE_Slider;
import main.IMP;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

public class Routine_Resize
extends Routine{

	// values
	GuiIntValue widthSlider;
	GuiIntValue heightSlider;

	public Routine_Resize() {
		super();
		set_name("Skalieren");
		category = "Bild/Farbe";
		
		widthSlider = new GuiIntValue(1,510,255);
		heightSlider = new GuiIntValue(1,510,255);

		add_gui_element(new PE_Slider("Breite", widthSlider, this, ""));
		add_gui_element(new PE_Slider("Höhe", heightSlider, this, ""));
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
		widthSlider.reconfigure(1,currentWidth*4, currentWidth);
		heightSlider.reconfigure(1,currentHeight*4, currentHeight);

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
			IMP.preview_image = new BufferedImage(widthSlider.getInt(),
				heightSlider.getInt(), IMP.opened_image.getImage().getType());

			Raster from = IMP.opened_image.getCompleteImage().getRaster();
			WritableRaster to = IMP.preview_image.getRaster();


			int width = IMP.preview_image.getWidth();
			int height = IMP.preview_image.getHeight();
			float widthRatio = (width + 0f) / (IMP.opened_image.getWidth()+0f);
			float heightRatio = (height+0f) / (IMP.opened_image.getHeight()+0f);

			int i = 0;
			int j = 0;
			int bufferSize = 10;
			double[] buffer = new double[bufferSize];
			for(int t=0; t<bufferSize; t++){
				buffer[t] = 200;
			}
			for(int x=0; x<width; x++){
				j = 0;
				while(x > i*widthRatio){
					i++;
				}

				for(int y=0; y<height; y++){
					while(y > j*heightRatio){
						j++;
					}
//					System.out.println("i is: " + i);
//					System.out.println("maxWidth is: " + from.getWidth());
//					System.out.println("i*widht ratio was : " + (i-1)*widthRatio);
//					System.out.println("new Width is: " + width);
//					System.out.println("x is: " + x);
//					System.out.println("width ratio is: " + widthRatio);

					from.getPixel(i,j,buffer);
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




