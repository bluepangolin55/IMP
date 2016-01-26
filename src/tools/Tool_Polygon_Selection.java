package tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.IMP;
import main.Menuelement;
import file.Img;
import functionality.Dot;
import functionality.Selection;
import gui.Image_Tile;
import gui_system.Floating_Panel;
import gui_system.GuiIntValue;
import gui_system.Message;

public class Tool_Polygon_Selection 
extends Tool{
	
	// values
	
	GuiIntValue v0; 

	
	public Tool_Polygon_Selection() {
		super();
		//GUI
		set_name("Polygon-Auswahlwerkeug");
		cursor=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		try {
			icon_default=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/select_a.png"));
			icon_selected=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/select_b.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		v0 = new GuiIntValue(1,100,20);
		
		set_menu(new Menuelement("menu",null));
		hud_panel = new Floating_Panel(this);
//		hud_panel.add(new PE_Slider("Breite", values.get(0)));
	}

	@Override
	public void mouse_pressed(MouseEvent e){
		if(IMP.opened_image!=null){
			if(selecting_in_progress)
				add_point();
			else{
				if(e.isControlDown()){
					
				}
				else
				start_selection(e);
			}
			IMP.image_informant.inform_about_selection_changed();
		}
	}
	
	@Override
	public void mouse_dragged(MouseEvent e){
		if(IMP.opened_image!=null){
			if(selecting_in_progress)
				add_point();
			else{
				start_selection(e);
			}
		}
	}
	
	@Override
	public void mouse_released(MouseEvent e){
	}
	
	@Override
	public void key_released(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_ENTER){
			if(selecting_in_progress){
				end_selection();
				IMP.image_informant.inform_about_selection_changed();
			}
		}
		else if (key == KeyEvent.VK_ESCAPE){
			selecting_in_progress=false;
			IMP.image_informant.inform_about_selection_changed();
		}
		else if (key == KeyEvent.VK_BACK_SPACE){
			remove_last_point();
			IMP.image_informant.inform_about_selection_changed();
		}
		 
		else if (key == KeyEvent.VK_LEFT){
			if(selection.previous_selection!=null){
				selection=selection.previous_selection;
				IMP.image_informant.inform_about_selection_changed();
			}
		}
		
		else if (key == KeyEvent.VK_RIGHT){
			if(selection.next_selection!=null){
				selection=selection.next_selection;
				IMP.image_informant.inform_about_selection_changed();
			}
		}
		else if (key == KeyEvent.VK_UP){
			if(selection!=null){
				Graphics g=IMP.opened_image.getImage().getGraphics();
				g.setColor(Color.green);
				g.fillPolygon(selection.points.xpoints,
						selection.points.ypoints, selection.points.npoints);
				IMP.image_informant.inform_about_selection_changed();
			}
		}
	}
	
	@Override
	public void paint_on_screen(Graphics g, Image_Tile image_tile) {
		if(selecting_in_progress){
			paint_preview_selection(g, image_tile);
		}
	}
	
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
	//------------------------------------------------------------------------------------------------------------------------------
			//funcionality
	
//	Function parameters that the user can change in the dialogue. The calculated  values are set to make the filter work in a default way.
	public boolean selectionOn=false;
	Polygon preview_selection2; //used while selecting
	Polygon selection2;
	Selection selection;
	Selection preview_selection;
	Color selectingColor =new Color(40,40,40);
	public boolean selecting_in_progress;
	private boolean append_selection;
	

	
	public void paint_preview_selection(Graphics g, Image_Tile image_tile){
		Graphics2D g2=(Graphics2D) g;
		int i;
		
		g.setColor(new Color(40,40,40));
		Polygon selectionDisplay=new Polygon();
		for(i=0;i<selection.size();i++){
			selectionDisplay.addPoint((int) (image_tile.view.img_pos_x()+selection.get_point_x(i)*image_tile.view.zoom),
					(int) (image_tile.view.img_pos_y()+selection.get_point_y(i)*image_tile.view.zoom));
		}
		float dash1[] = {10};
	    BasicStroke dashed =
	        new BasicStroke(2,
	                        BasicStroke.JOIN_BEVEL,
	                        BasicStroke.JOIN_MITER,
	                        10.0f, dash1, 0.0f);
	    
		g.setColor(new Color(240,240,240));
		g2.drawPolyline(selectionDisplay.xpoints, selectionDisplay.ypoints, selectionDisplay.npoints);
		g2.setColor(new Color(60,60,60));
		g2.setStroke(dashed);
		g2.drawPolyline(selectionDisplay.xpoints, selectionDisplay.ypoints, selectionDisplay.npoints);
		
		g2.setStroke(new BasicStroke());

//		g.setColor(new Color(160,160,250,60));		//too slow
//		g.fillPolygon(selectionDisplay);

		//start point
		g.setColor(Color.red);
		g.fillOval(selectionDisplay.xpoints[0]-5,selectionDisplay.ypoints[0]-5, 10, 10);
		g.setColor(new Color(40,40,40));
		g.drawOval(selectionDisplay.xpoints[0]-5,selectionDisplay.ypoints[0]-5, 10, 10);
		//other points
		for(i=1; i<selectionDisplay.npoints;i++){
			g.setColor(Color.yellow);
			g.fillOval(selectionDisplay.xpoints[i]-5,selectionDisplay.ypoints[i]-5, 10, 10);
			g.setColor(new Color(40,40,40));
			g.drawOval(selectionDisplay.xpoints[i]-5,selectionDisplay.ypoints[i]-5, 10, 10);
		}
//		if(image_tile.hasFocus())
//			g.drawLine(selectionDisplay.xpoints[i-1], selectionDisplay.ypoints[i-1],
//					image_tile.mouse_x, image_tile.mouse_y);
	}

	public void start_selection(MouseEvent e) {
		if(e.isShiftDown())
			append_selection=true;
		
		selecting_in_progress=true;	
		//link with previous selection
		Selection new_selection = new Selection();
		if(selection!=null){
			selection.next_selection=new_selection;
			new_selection.previous_selection=selection;
		}
		//create selection
		selection=new_selection;
		add_point();
	}
	
	public void end_selection() {
		selecting_in_progress=false;	
		selection.add_point(selection.get_point_x(0),selection.get_point_y(0));
		
		selectionOn=true;
		
		if(append_selection){
			IMP.opened_image.active_selections.add(selection);
		}
		else{
			IMP.opened_image.active_selections.removeAll(IMP.opened_image.active_selections);
			IMP.opened_image.active_selections.add(selection);
		}
//		List_Element selection_element=new List_Element("selection " + panel.elements.size(), selection, this);
//		panel.add(selection_element);
//		hud_panel.add(selection_element);
//		panel.repaint();
		append_selection=false;
	}

	public void add_point() {
		Dot point = new Dot(IMP.opened_image.mouse_x, IMP.opened_image.mouse_y);
		//check if the point is inside the image
		if(point.x<0)
			point.x=0;
		else if(point.x>IMP.opened_image.getWidth())
			point.x=IMP.opened_image.getWidth();
		
		if(point.y<0)
			point.y=0;
		else if(point.y>IMP.opened_image.getHeight())
			point.y=IMP.opened_image.getHeight();
		
		selection.add_point(point);
	}
	
	public void remove_last_point(){
		selection.remove_last_point();
	}

	@Override
	public void receive_message(Message m) {
		// TODO Auto-generated method stub
		
	}

}
