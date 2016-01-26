package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.IMP;
import file.Img;
import gui_system.GuiIntValue;
import gui_system.PE_Slider;

public class Routine_Mandelbrot 
extends Routine{
	
	// values 
	
	GuiIntValue v0;
	GuiIntValue v1;
	GuiIntValue v2;
	GuiIntValue v3;
	
	//mouse
	int previous_mouse_x = 0;
	int previous_mouse_y = 0;
	
	public Routine_Mandelbrot() {
		super();
		set_name("Mandelbrot-Algorithmus");
		category = "Muster";
		
		v0 = new GuiIntValue(1,10,4);
		v1 = new GuiIntValue(1,10,4);
		v2 = new GuiIntValue(1,200,20);
		v3 = new GuiIntValue(1,50,20);
		
		add_gui_element(new PE_Slider("X-Position", v0, this, ""));
		add_gui_element(new PE_Slider("Y-Position", v2, this, ""));
		add_gui_element(new PE_Slider("Anzahl Farben", v2, this, ""));
		add_gui_element(new PE_Slider("Iterationen", v3, this, ""));
		
	}

	@Override
	public void activate(){
		super.activate();
		IMP.image_informant.inform_about_preview_available(true);
		//reconfigure sliders
		v0.reconfigure(-IMP.opened_image.getWidth(), IMP.opened_image.getWidth(),4);
		v1.reconfigure(-IMP.opened_image.getHeight(), IMP.opened_image.getHeight(),4);
		v2.reconfigure(1,100,4);
		v3.reconfigure(1,50,20);
		
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
	public void mouse_dragged(MouseEvent e){
		v0.set_value(v0.getDouble() + (IMP.opened_image.mouse_x - previous_mouse_x)/zoom/1.33);
		v1.set_value(v1.getDouble() + (IMP.opened_image.mouse_y - previous_mouse_y)/zoom/1.33);
		previous_mouse_x = IMP.opened_image.mouse_x;
		previous_mouse_y = IMP.opened_image.mouse_y;
		paint_preview();
	}
	
	@Override
	public void mouse_pressed(MouseEvent e) {
		previous_mouse_x = IMP.opened_image.mouse_x;
		previous_mouse_y = IMP.opened_image.mouse_y;
	}
	
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
	//------------------------------------------------------------------------------------------------------------------------------
			//funcionality
//			Function parameters that the user can change in the dialogue. The calculated  values are set to make the filter work in a default way.
			static boolean color=true;
			static double zoom=1;
			double MinRe;
			double MaxRe;
			double MinIm;
			double MaxIm;
			double Re_factor;
			double Im_factor;
			int MaxIterations;
			
			
		@Override
		public void paint_preview() {
			Graphics g=IMP.preview_image.getGraphics();
			
			int width = IMP.opened_image.getWidth();
			int height = IMP.opened_image.getHeight();
			
			double pos_x =-v0.getDouble()/100;
			double pos_y =-v1.getDouble()/100;
			zoom=v2.getDouble();
			zoom = zoom*zoom;
			
			
			MinRe = (-2.0+pos_x);
			MaxRe = (1.0+pos_x);
			MinIm = (-1.2-pos_y);
			MaxIm = MinIm+(MaxRe-MinRe)*height/width;
			Re_factor = (MaxRe-MinRe)/(width-1)/zoom;
			Im_factor = (MaxIm-MinIm)/(height-1)/zoom;
			MaxIterations = v3.getInt();
			
			int color=0;
			int mapping_factor = 255 / MaxIterations;
			
			int speedup=1;
			
			for(int y=0; y<height; y=y+speedup)
			{
			    double c_im = MaxIm - y*Im_factor;
			    for(int x=0; x<width; x=x+speedup)
			    {
			        double c_re = MinRe + x*Re_factor;

			        double Z_re = c_re, Z_im = c_im;
			        boolean isInside = true;
			        for(int n=0; n<MaxIterations; ++n)
			        {
			        	color = n*mapping_factor;
			            double Z_re2 = Z_re*Z_re, Z_im2 = Z_im*Z_im;
			            if(Z_re2 + Z_im2 > 4)
			            {
			                isInside = false;
			                break;
			            }
			            Z_im = 2*Z_re*Z_im + c_im;
			            Z_re = Z_re2 - Z_im2 + c_re;
			        }
			        if(isInside) { 
			        	g.drawRect(x, y, speedup,speedup);
			        }
			        else{
						g.setColor(new Color(0, color, 0));
			        	g.drawRect(x, y, speedup,speedup);
						g.setColor(Color.black);
			        }
			    }
			}
//			
			
			
		}	//mandelbrot
		
	}
