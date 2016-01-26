package tools;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.jhlabs.image.EqualizeFilter;
import main.IMP;
import main.Menuelement;
import file.Img;
import gui_system.GuiIntValue;
import gui_system.PE_Apply_Abort;

public class Routine_1 
extends Routine{

	public Routine_1() {
		super();
		set_name("Vermischen");
		
		set_menu(new Menuelement("menu",null));
		get_menu().create("Anwenden","apply");
		get_menu().create("Abbrechen","abort");
		
		brightness=new GuiIntValue(1,200,100);
		contrast=new GuiIntValue(1,200,100);
		hud_panel.add(new PE_Apply_Abort(this));
		
		sidepanel.add(new PE_Apply_Abort(this));
	}

	@Override
	public void activate(){
		super.activate();
		IMP.image_informant.inform_about_preview_available(true);
		IMP.image_informant.inform_about_preview_changed();
		
		
		IMP.preview_image = new BufferedImage(IMP.opened_image.getWidth(), 
				IMP.opened_image.getHeight(), IMP.opened_image.getImage().getType());
		//IMP.preview_image=new Img(IMP.opened_image.getWidth(),IMP.opened_image.getHeight(),1);
		original_reference=new Img(IMP.opened_image.getWidth(),IMP.opened_image.getHeight(),1);
		Graphics g=original_reference.getImage().getGraphics();
		g.drawImage(IMP.opened_image.getImage(), 0, 0, original_reference.getWidth(), original_reference.getHeight(), null);
		paint_preview();
	}
	
	@Override
	public void apply(){
		super.apply();
		EqualizeFilter op = new EqualizeFilter();
		IMP.opened_image.set_image(op.filter(IMP.opened_image.getImage(),  IMP.opened_image.getImage()));
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
	}


	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
	//------------------------------------------------------------------------------------------------------------------------------
		//funcionality
//		Function parameters that the user can change in the dialogue. The calculated  values are set to make the filter work in a default way.
	static GuiIntValue brightness;
		static GuiIntValue contrast;
		Img original_reference;
		
		@Override
		public void paint_preview() {
			EqualizeFilter op = new EqualizeFilter();
			//IMP.preview_image.set_image(op.filter(original_reference.getImage(),  IMP.preview_image.getImage()));
		}
	}
