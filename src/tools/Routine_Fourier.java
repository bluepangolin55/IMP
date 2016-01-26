package tools;

import file.Img;
import functionality.Transformations;
import gui_system.*;
import main.IMP;
import org.jtransforms.fft.FloatFFT_2D;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.image.WritableRaster;

public class Routine_Fourier
extends Routine{


	public Routine_Fourier() {
		super();
		set_name("Weichzeichnen");
		category = "Bild/Farbe";

		inner = new GuiIntValue(1,400,1);
		outer = new GuiIntValue(1,400,1);
		representation = new GuiBooleanValue(true);
		
		add_gui_element(new PE_Slider("Inner", inner, this, ""));
		add_gui_element(new PE_Slider("Outer", outer, this, ""));
		add_gui_element(new PE_Check_Button("Representation", representation, this));
		add_gui_element(new PE_Apply_Abort(this));

		
		op = new ConvolveOp(createKernel(5,1));
	}

	@Override
	public void activate(){
		super.activate();

		IMP.image_informant.inform_about_preview_available(true);
		IMP.image_informant.inform_about_preview_changed();
		
		
		IMP.preview_image = new BufferedImage(IMP.opened_image.getWidth(), 
				IMP.opened_image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

		//IMP.preview_image=new Img(IMP.opened_image.getWidth(),IMP.opened_image.getHeight(),1);
		original_reference=new Img(IMP.opened_image.getWidth(),IMP.opened_image.getHeight(),1);
		Graphics g=original_reference.getImage().getGraphics();
		g.drawImage(IMP.opened_image.getImage(), 0, 0, original_reference.getWidth(), original_reference.getHeight(), null);
//		fourier = Transformations.fourier(IMP.opened_image.getImage());
		fourier(IMP.opened_image.getImage());
		go();
		paint_preview();
	}
	
	public void fourier(BufferedImage image) {
		System.out.println("--------------------------------");
		int w = image.getWidth();
		int h = image.getHeight();
		int w2p = 1;
		int h2p = 1;
		while(w2p < w){
			w2p *= 2;
		}
		while(h2p < h){
			h2p *= 2;
		}
		int mask = 0x000000ff;
		fourier = new float[w2p][h2p];
		for(int x=0; x<w; x++){
			for(int y=0; y<h; y++){
				int a = image.getRGB(x, y);
				a = (a & mask) + (a>>8 & mask) + (a>>16 & mask);
				fourier[x][y] = a/3;
			}
		}
		
		fft = new FloatFFT_2D(w2p,h2p);
		fft.realForward(fourier);

		filter = new float[w2p][h2p];
//		filter[0][1] = 1/6f;
//		filter[1][0] = 1/6f;
//		filter[1][1] = 2/6f;
//		filter[1][2] = 1/6f;
//		filter[2][1] = 1/6f;

		filter[0][1] = 1f;

		fft.realForward(filter);

		fourierRepresentation = new BufferedImage(w2p, 
				h2p, BufferedImage.TYPE_BYTE_GRAY);

		int s = (int) Math.sqrt(Math.pow(w2p, 2)+Math.pow(h2p, 2)) / 2;
		inner.reconfigure(1, s, 1);
		outer.reconfigure(1, s, 1);
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
	static GuiIntValue inner;
	static GuiIntValue outer;
	private GuiBooleanValue representation;
	ConvolveOp op;
	BufferedImage fourierRepresentation;
	Img original_reference;
	public float[][] fourier;
	FloatFFT_2D fft;
	float[][] filter;
	float c;
	
	private void go(){
		
		double measureFrac = 0.00000001;

		int w = IMP.preview_image.getWidth();
		int h = IMP.preview_image.getHeight();
		int n = w*h;
		int w2p = 1;
		int h2p = 1;
		int np2 = 1;
		while(w2p < w){
			w2p *= 2;
		}
		while(h2p < h){
			h2p *= 2;
		}
		while(np2 < n){
			np2 *= 2; 
		}
		double t1 = System.nanoTime();

		float[][] data = new float[w2p][h2p];
		float max = 0;
		for(int x=0; x<w2p; x++){
			for(int y=0; y<h2p; y++){
				data[x][y] = fourier[x][y];
				if(Math.abs(data[x][y]) > max){
					max = Math.abs(data[x][y]);
				}
			}
		}
		c = (float) (255/Math.log10(max));

		
		System.out.println("time1:" + (System.nanoTime()-t1)*measureFrac);
		t1 = System.nanoTime();

		

		System.out.println("time2:" + (System.nanoTime()-t1)*measureFrac);
		t1 = System.nanoTime();

		int s2 = inner.getInt();
		int s1 = outer.getInt();

		// filtering
//		data = Transformations.circshift(data);
//		DenseMatrix32F d = new DenseMatrix32F(data);
//		DenseMatrix32F f = new DenseMatrix32F(data);
//		DenseMatrix32F r = new DenseMatrix32F(data);
//		CommonOps32.mult(d, f, r);
//		double mult = (s/100)/c;
		for(int x=0; x<w2p; x++){
			for(int y=0; y<h2p; y++){
//				data[x][y] = r.get(x, y);
				data[x][y] = data[x][y]*filter[x][y];
			}
		}

		data = Transformations.circshift(data);
//		int mx = w2p/2;
//		int my = h2p/2;
//		for(int y=0; y<h2p; y++){
//			int ydiff = Math.abs(my-y);
//			int s1Start = mx - (int) Math.sqrt(Math.pow(s1, 2)-Math.pow(ydiff, 2));
//			int s1End = mx + (int) Math.sqrt(Math.pow(s1, 2)-Math.pow(ydiff, 2));
//			int s2Start = mx - (int) Math.sqrt(Math.pow(s2, 2)-Math.pow(ydiff, 2));
//			int s2End = mx + (int) Math.sqrt(Math.pow(s2, 2)-Math.pow(ydiff, 2));
//			for(int x=0; x<s1Start; x++){
//				data[x][y] = 0;
//			}
//			for(int x=s2Start; x<s2End; x++){
//				data[x][y] = 0;
//			}
//			for(int x=s1End; x<w2p; x++){
//				data[x][y] = 0;
//			}
//		}

		
		
		// to display the fourier spectrum
		
		System.out.println("time3:" + (System.nanoTime()-t1)*measureFrac);
		t1 = System.nanoTime();


		System.out.println("time4:" + (System.nanoTime()-t1)*measureFrac);
		t1 = System.nanoTime();
		

		

		if(representation.getValue()){
			showResult(data);
		}
		else{
			showFourier(data);
		}
//		IMP.opened_image.set_image(fourierRepresentation);
	
		System.out.println("time5:" + (System.nanoTime()-t1)*measureFrac);
		t1 = System.nanoTime();
	}
	
	private void showFourier(float[][] data){

		int w2p = data.length;
		int h2p = data[0].length;		
		for(int y=0; y<h2p; y++){
			for(int x=0; x<w2p; x++){
				data[x][y] = c*(float)Math.log10(1+Math.abs(data[x][y]));
			}
		}
	
		int[] intData = new int[w2p*h2p];
		int i=0;
		for(int y=0; y<h2p; y++){
			for(int x=0; x<w2p; x++){
				int a = (int) data[x][y];
				a = a + (a<<8) + (a<<16) + 0xFF000000;
				intData[i] = a + (a<<8) + (a<<16) + 0xFF000000;
				i++;
			}
		}
		WritableRaster raster = fourierRepresentation.getRaster();
		raster.setPixels(0, 0, w2p, h2p, intData);
		IMP.preview_image = fourierRepresentation;
		IMP.opened_image.set_image(fourierRepresentation);;
	}

	private void showResult(float[][] data){
		int w = IMP.preview_image.getWidth();
		int h = IMP.preview_image.getHeight();

		data = Transformations.circshift(data);
		fft.realInverse(data, true);
		int[] intData = new int[w*h];
		int i=0;
		for(int y=0; y<h; y++){
			for(int x=0; x<w; x++){
				int a = (int) data[x][y];
				a = a + (a<<8) + (a<<16) + 0xFF000000;
				intData[i] = a + (a<<8) + (a<<16) + 0xFF000000;
				i++;
			}
		}
		WritableRaster raster = IMP.preview_image.getRaster();
		raster.setPixels(0, 0, w, h, intData);
		
	}
	
	
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
		//IMP.preview_image.set_image(op.filter(original_reference.getImage(),  IMP.preview_image.getImage()));
		//op.filter(IMP.opened_image.getImage(), IMP.preview_image2);
		//op.filter(IMP.opened_image.getImage(), IMP.preview_image);
	}
	
	@Override
	public void receive_message(Message message) {
		go();
	}
}
