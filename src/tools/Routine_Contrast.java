package tools;

import functionality.Selection;
import gui_system.GuiIntValue;
import gui_system.Message;
import gui_system.PE_Apply_Abort;
import gui_system.PE_Slider;
import main.IMP;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.*;

public class Routine_Contrast
extends Routine{

	// values
	GuiIntValue v0;
	GuiIntValue v1;

	public Routine_Contrast() {
		super();
		set_name("Contrast");
		category = "Bild/Farbe";

		v0 = new GuiIntValue(0,100,0);
		v1 = new GuiIntValue(-255,255,0);

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

	@Override
	public void paint_preview() {

		float c0 = v0.getInt()/10f;
		float c1 = v1.getInt();
		float[] scales = {
				c0, c0, c0, 1
		};
		float[] offsets = {
				c1, c1, c1, 0
		};

		short[] threshold = new short[256];
		short[] red = new short[256];
		short[] green = new short[256];
		short[] blue = new short[256];
		for (short i = 0; i < 256; i++) {
			red[i] = (short) (255 - i);
			green[i] = 0;
			blue[i] = 0;
		}
		short[][] data = new short[][] {
				red, green, blue
		};
		LookupTable lookupTable = new ShortLookupTable(0, data);

		BufferedImageOp op = new LookupOp(lookupTable, null);
//		op = new RescaleOp(scales, offsets, null);

		BufferedImage modified = op.filter(subimage, null);

		Graphics g = IMP.preview_image.getGraphics();
		if(clip != null){
			g.setClip(clip);
		}
		g.drawImage(modified, bounds.x, bounds.y, bounds.width, bounds.height, null);
//		IMP.preview_image = op.filter(IMP.preview_image, null);
	}


	public void receive_message(Message message) {
		paint_preview();
	}
}
