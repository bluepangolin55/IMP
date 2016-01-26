package tools;

import java.awt.Graphics;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.IMP;
import file.Img;
import gui_system.PE_Apply_Abort;
import gui_system.PE_Radio_Button;

public class Routine_Export_File 
extends Routine{

	private PE_Radio_Button radio_button;
	private String[] file_formats = {"bmp", "png", "jpeg", "gif"};

	public Routine_Export_File() {
		super();
		set_name("Bild Exportieren");
		category = "Datei";
		key_shortcut = 'e';
		
		radio_button = new PE_Radio_Button("Bildformat", file_formats);
		add_gui_element(radio_button);
		add_gui_element(new PE_Apply_Abort(this));
	}

	@Override
	public void activate(){
		super.activate();
		IMP.image_informant.inform_about_preview_available(true);
		IMP.image_informant.inform_about_preview_changed();
		
		
		IMP.preview_image= IMP.opened_image.getImage();
		original_reference=IMP.opened_image;
		Graphics g=original_reference.getImage().getGraphics();
		g.drawImage(IMP.opened_image.getImage(), 0, 0, original_reference.getWidth(), original_reference.getHeight(), null);
		paint_preview();
	}
	
	@Override
	public void apply(){
		super.apply();
		File file = new File(IMP.opened_image.path + "-"
				+ IMP.opened_image.filename + "." + radio_button.get_value());
		file = new File("/home/dimitri/testimage." + radio_button.get_value());
		try {
			ImageIO.write(IMP.opened_image.getImage(), radio_button.get_value(), file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(file.getAbsolutePath());
	}

	@Override
	public void abort(){
		super.abort();
	}
	

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
	//------------------------------------------------------------------------------------------------------------------------------
		//funcionality
//		Function parameters that the user can change in the dialogue. The calculated  values are set to make the filter work in a default way.
	Img original_reference;
}
