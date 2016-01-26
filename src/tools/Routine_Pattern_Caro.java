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

public class Routine_Pattern_Caro 
extends Routine{
	
	// values
	
	GuiIntValue v0;
	GuiIntValue v1;

	public Routine_Pattern_Caro() {
		super();
		set_name("Karomuster");
		category = "Muster";
		
		v0 = new GuiIntValue(1,100,20);
		v1 = new GuiIntValue(1,100,20);
		
		add_gui_element(new PE_Slider("Breite", v0,this, ""));
		add_gui_element(new PE_Slider("Höhe", v1,this, ""));
		add_gui_element(new PE_Apply_Abort(this));
	}

	@Override
	public void activate(){
		super.activate();
		IMP.image_informant.inform_about_preview_available(true);
		
		//reconfigure sliders
		v0.reconfigure(1,IMP.opened_image.getWidth(),20);
		v1.reconfigure(1,IMP.opened_image.getHeight(),20);
		
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
		v0.set_value(IMP.opened_image.mouse_x);
		v1.set_value(IMP.opened_image.mouse_y);
		paint_preview();
	}
	
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
	//------------------------------------------------------------------------------------------------------------------------------
			//funcionality
//			Function parameters that the user can change in the dialogue. The calculated  values are set to make the filter work in a default way.
			private int a=20;	//height of a rectangle
			private int b=20;	//length of a square
			private Color cOne=new Color(40,40,100);
			private Color cTwo=new Color(250,255,150);

		@Override
		public void paint_preview() {
			Graphics g=IMP.preview_image.getGraphics();
			a=v0.getInt();
			b=v1.getInt();
			
			g.drawImage(IMP.opened_image.getImage(), 0, 0, IMP.opened_image.getWidth(), IMP.opened_image.getHeight(), IMP.main_panel);
			if(IMP.opened_image.active_selections.size()>0)
				g.setClip(IMP.opened_image.active_selections.get(0).points);
				
				
			// TODO Auto-generated method stub
				g.setColor(cOne);//square color 1
				g.fillRect(0,0,
						IMP.opened_image.getWidth(),IMP.opened_image.getHeight());
				
				g.setColor(cTwo);//square color 2
				int aCount=IMP.opened_image.getWidth()/a+1;
				int bCount=IMP.opened_image.getHeight()/b+1;

				for(int j=0;j<bCount;j=j+2){
					for(int i=0;i<aCount;i=i+2){
						g.fillRect(i*a, j*b,a, b);
					}
					for(int i=1;i<aCount;i=i+2){
						g.fillRect(i*a, (j+1)*b,a, b);
					}
				}
		}
}
