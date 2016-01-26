package tools;

import gui_system.*;
import main.IMP;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class Routine_Blur
		extends Routine{


	public Routine_Blur() {
		super();
		set_name("Weichzeichnen");
		category = "Bild/Schärfe";

		filter = new GuiIntValue(3,20,5);
		vertical = new GuiIntValue(1,100,100);
		sigma = new GuiIntValue(1,100,1);

		add_gui_element(new PE_Slider("size", filter, this, ""));
		add_gui_element(new PE_Slider("Verticale Verzerrung", vertical , this, ""));
		add_gui_element(new PE_Slider("Sigma", sigma, this, ""));
		add_gui_element(new PE_Apply_Abort(this));


		op = new ConvolveOp(createKernel(filter.getInt()));
	}

	@Override
	public void activate(){
		super.activate();

		IMP.image_informant.inform_about_preview_available(true);
		IMP.image_informant.inform_about_preview_changed();


//		IMP.preview_image = new BufferedImage(IMP.opened_image.getWidth(),
//				IMP.opened_image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
		IMP.preview_image = new BufferedImage(IMP.opened_image.getWidth(),
				IMP.opened_image.getHeight(), IMP.opened_image.getImage().getType());


		//IMP.preview_image=new Img(IMP.opened_image.getWidth(),IMP.opened_image.getHeight(),1);
//		original_reference=new Img(IMP.opened_image.getWidth(),IMP.opened_image.getHeight(),1);
		paint_preview();
	}

	@Override
	public void apply(){
		super.apply();
		//op.setIntoB(strength.getInt());
		//IMP.opened_image.set_image(op.filter(IMP.opened_image.getImage(),  IMP.opened_image.getImage()));
		BufferedImage copy = op.createCompatibleDestImage(IMP.opened_image.getImage()
				, IMP.opened_image.getImage().getColorModel());
		op.filter(IMP.opened_image.getImage(), IMP.preview_image);

		IMP.opened_image.set_image(IMP.preview_image);
		IMP.opened_image.composeCompleteImage();
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
	static GuiIntValue filter;
	static GuiIntValue vertical;
	static GuiIntValue sigma;
	ConvolveOp op;
	float[][] filterMatrix;
//	float c;


	float gauss(float x, float y, float sigma){
		float sigmaSquare = sigma*sigma;
		float c = vertical.getInt();
		y = (c/20)*y;
		return (1/((float)Math.PI*sigmaSquare))*(-(x*x + y*y))/(2*sigmaSquare);
	}

	private Kernel createKernel(int size){
		float r = 0f;
		if(size%2 == 0){
			size--;
		}
		if(size < 3){
			size = 3;
		}

		float[] data = new float[size*size];
		int i=0;
		int halfSize = size/2;
		float smallest = Float.MAX_VALUE;
		float s = sigma.float_value();
		for(int y=0; y<size; y++){
			for(int x=0; x<size; x++){
				data[i] = gauss(x-halfSize,y-halfSize, s);
				smallest = Float.min(smallest, data[i]);
				i++;
			}
			System.out.println();
		}


		i = 0;
		for(int y=0; y<size; y++){
			for(int x=0; x<size; x++){
				System.out.print(data[i] + " ");
				i++;
			}
			System.out.println();
		}

		for(i=0; i<size*size; i++){
			data[i] -= smallest;
			r += data[i];
		}

		for(i=0; i<size*size; i++){
			data[i] = data[i]/r;
		}

		i = 0;
		for(int y=0; y<size; y++){
			for(int x=0; x<size; x++){
				System.out.print(data[i] + " ");
				i++;
			}
			System.out.println();
		}

		return new Kernel(size, size, data);
	}

	@Override
	public void paint_preview() {
		op = new ConvolveOp(createKernel(filter.getInt()));

		IMP.preview_image = op.filter(IMP.opened_image.getImage(), IMP.preview_image);
	}

	@Override
	public void receive_message(Message message) {
		paint_preview();
	}
}
