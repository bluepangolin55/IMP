package tools;

import gui_system.*;
import main.IMP;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class Routine_Filter
extends Routine{


	public Routine_Filter() {
		super();
		set_name("Filter");
		category = "Bild/Farbe";

		filter = new GuiIntValue(1,10,1);

		add_gui_element(new PE_Slider("Filter", filter, this, ""));
		add_gui_element(new PE_Apply_Abort(this));

		
		op = new ConvolveOp(createKernel(5,1));
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
	ConvolveOp op;
	float[][] filterMatrix;
	float c;
	
	private Kernel createKernel(int size, int strength){
		float r = 0f;
		if(size%2 == 0){
			size--;
		}
		if(size < 3){
			size = 3;
		}
		else if(size > 11){
			size = 11;
		}

		float[][] D = new float[size][size];
		
		D[size/2][size/2] = strength;
		int m = size/2;
		for(int i=0; i<m; i++){
			D[m][i] = i+1;
		}
		for(int i=m; i<size; i++){
			D[m][i] = (size-i);
		}
		for(int i=m+1; i<size; i++){
			for(int j=0; j<size; j++){
				D[i][j] = D[i-1][j] -1 ;
			}
		}
		for(int i=m-1; i>=0; i--){
			for(int j=0; j<size; j++){
				D[i][j] = D[i+1][j] -1 ;
			}
		}

		float[] data = new float[size*size];
		int i=0;
		for(int x=0; x<size; x++){
			for(int y=0; y<size; y++){
				data[i] = D[x][y];
				r += data[i];
				i++;
			}
		}
		
		for(i=0; i<size*size; i++){
			data[i] = data[i]/r;
		}

		return new Kernel(size, size, data);
	}
	
	@Override
	public void paint_preview() {
		switch (filter.getInt()){
			case 1: op = filter1();
				break;
			case 2: op = filter2();
				break;
			case 3: op = filter3();
				break;
			case 4: op = filter4();
				break;
			case 5: op = filter5();
				break;
			default:
				op = filter5();
		}

		IMP.preview_image = op.filter(IMP.opened_image.getImage(), IMP.preview_image);
		//op.filter(IMP.opened_image.getImage(), IMP.preview_image2);
		//op.filter(IMP.opened_image.getImage(), IMP.preview_image);
	}

	private ConvolveOp filter0(){
		float[] data = {
				0, 0, 0,
				0, 1f, 0,
				0, 0, 0
		};
		return new ConvolveOp(new Kernel(3, 3, data));
	}

	private ConvolveOp filter1(){
		return new ConvolveOp(createKernel(5, 1));
	}

	private ConvolveOp filter2(){
		float[] data = {
				1/14f, 2/14f, 1/14f,
				2/14f, 2/14f, 2/14f,
				1/14f, 2/14f, 1/14f
		};
		return new ConvolveOp(new Kernel(3, 3, data));
	}

	private ConvolveOp filter3(){
		float[] data = {
				-1/2f, 0f, 1/2f,
		};
		return new ConvolveOp(new Kernel(3, 1, data));
	}

	private ConvolveOp filter4(){
		float[] data = {
				-1, -1, -1,
				-1, 8f, -1,
				-1, -1, -1
		};
		return new ConvolveOp(new Kernel(3, 3, data));
	}

	private ConvolveOp filter5(){
		// Prewitt
		float k = filter.getInt()-4;
		float[] data = {
				-1/k, 1/k, 1/k,
				-1/k, 1/k, 1/k,
				-1/k, 1/k, 1/k
		};
		return new ConvolveOp(new Kernel(3, 3, data));
	}

	
	@Override
	public void receive_message(Message message) {
		paint_preview();
	}
}
