package tools;

import functionality.Selection;
import gui_system.*;
import main.IMP;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.*;

public class Routine_EdgeDetection
extends Routine{

	// values
	GuiIntValue v0;
	GuiIntValue v1;
	GuiBooleanValue sharpen;

	public Routine_EdgeDetection() {
		super();
		set_name("Edge Detection");
		category = "Bild/Farbe";
		
		v0 = new GuiIntValue(0,100,0);
		v1 = new GuiIntValue(-255,255,0);
		sharpen = new GuiBooleanValue(false);

		add_gui_element(new PE_Check_Button("Sharpen before", sharpen, this));
		add_gui_element(new PE_Slider("Multiplikative Helligkeit", v0, this, ""));
		add_gui_element(new PE_Slider("Additive Helligkeit", v1, this, ""));
		add_gui_element(new PE_Apply_Abort(this));
	}

	@Override
	public void activate(){
		super.activate();
		IMP.image_informant.inform_about_preview_available(true);
		IMP.image_informant.inform_about_preview_changed();
		
		//functionality
		size_maxBrightness_ratio=(double) (IMP.opened_image.getWidth())/510;

		IMP.preview_image = new BufferedImage(IMP.opened_image.getWidth(),
				IMP.opened_image.getHeight(), IMP.opened_image.getImage().getType());
		Graphics g = IMP.preview_image.getGraphics();
		g.drawImage(IMP.opened_image.getCompleteImage(), 0, 0, null);

		if(IMP.opened_image.active_selections != null
				&& IMP.opened_image.active_selections.size() > 0
				&& IMP.opened_image.active_selections.get(0) != null) {
			Selection selection = IMP.opened_image.active_selections.get(0);
			clip = selection.points;
			bounds = selection.bounds;
		}
		else{
			bounds = new Rectangle(0,0, IMP.preview_image.getWidth(), IMP.preview_image.getHeight());
		}
		subimage = IMP.opened_image.getImage().getSubimage(
				bounds.x, bounds.y,
				bounds.width, bounds.height);

		paint_preview();
	}

	@Override
	public void apply(){
		super.apply();
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
	BufferedImage subimage;
	Rectangle bounds;
	Shape clip;

	private int difference(int[] a, int[] b, int size){
		assert(size < a.length && size < b.length);
		int result = 0;
		for(int i=0; i<size; i++){
			result += Math.abs(a[i] - b[i]);
		}
		return result;
	}

	@Override
	public void paint_preview() {
		BufferedImage modified = new BufferedImage(
				subimage.getWidth(), subimage.getHeight(), subimage.getType());

		float[] data = {
				0, 1/5f, 0,
				1/5f, 1/5f, 1/5f,
				0, 1/5f, 0
		};
//		float[] data = {
//				0, -1, 0,
//				-1, 4, -1,
//				0, -1, 0
//		};

		Kernel kernel = new Kernel(3, 3, data);

		BufferedImageOp edge = new ConvolveOp(kernel);
		Raster from;
		if(sharpen.getValue()) {
			BufferedImage sharpened = new BufferedImage(
					subimage.getWidth(), subimage.getHeight(), subimage.getType());
			sharpened = edge.filter(subimage, null);
			from = sharpened.getRaster();
		}
		else{
			from = subimage.getRaster();
		}

		WritableRaster to = modified.getRaster();

		int[] bufferL = new int[4];
		int[] bufferR = new int[4];
		int[] bufferU = new int[4];
		int[] bufferD = new int[4];
		int[] bufferWhite = {255, 255, 255, 255};
		int[] bufferBlack = {0, 0, 0, 255};
		int[] buffer = {0, 0, 0, 255};


		float c = v0.getInt()/100f;
		System.out.println(c);
		int width = subimage.getWidth();
		int height = subimage.getHeight();
		for(int x=1; x<width-1; x++){
			for(int y=1; y<height-1; y++) {
				from.getPixel(x-1, y, bufferL);
				from.getPixel(x+1, y, bufferR);
				from.getPixel(x, y-1, bufferU);
				from.getPixel(x, y+1, bufferD);
				int diff = difference(bufferL, bufferR, 4)
						+ difference(bufferU, bufferD, 4);
				diff /= 3;
				diff *= c;
//				diff = Math.min(diff, 255);
				buffer[0] = buffer[1] = buffer[2] = diff;
				to.setPixel(x,y,buffer);
//				if(diff > 10){
//					to.setPixel(x,y,bufferWhite);
//				}
//				else{
//					to.setPixel(x,y,bufferBlack);
//				}

			}
		}

		Graphics g = IMP.preview_image.getGraphics();
		if(clip != null){
			g.setClip(clip);
		}
		g.drawImage(modified, bounds.x, bounds.y, bounds.width, bounds.height, null);
	}


	public void receive_message(Message message) {
		paint_preview();
	}
}
