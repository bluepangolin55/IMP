package tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.imageio.ImageIO;

import main.IMP;
import main.Menuelement;
import file.Img;
import functionality.Dot;
import functionality.Selection;
import gui.Image_Tile;
import gui_system.GuiIntValue;
import gui_system.Message;

public class Tool_Selection 
extends Tool{

//	public static boolean selectionOn=false;
	static Polygon selection1;
//	static Polygon selection2;
	static int startX;
	static int startY;
	static int length;
	static int height;
//	static Color selectingColor =new Color(40,40,40);
	public static boolean selectingInProgress;
	
	// values 
	
	GuiIntValue v0; 
	GuiIntValue v1; 

	
	public Tool_Selection() {
		super();
		//GUI
		set_name("Auswahlwerkeug1");
		cursor=Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
		try {
			icon_default=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/select_a.png"));
			icon_selected=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/select_b.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		v0 = new GuiIntValue(1,2000,30);
		v1 = new GuiIntValue(1,2000,20);
		
//		panel.add(new PE_Slider("Breite", values.get(0)));
//		panel.add(new PE_Slider("Höhe", values.get(1)));
		
		set_menu(new Menuelement("menu",null));
}

	@Override
	public void mouse_pressed(MouseEvent e){
		if(IMP.opened_image!=null){
			if(selecting_in_progress){
				boolean moving = false;
				for (Dot point : selection.dots) {
					if(Math.abs(point.x-IMP.opened_image.mouse_x) <5 && Math.abs(point.y-IMP.opened_image.mouse_y) <10){
						moving = true;
						selected_point = point;
						selected_point.is_selected=true;
						break;
					}
				}
				if(moving){
				}
				else
					add_point();
			}
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
			if(selecting_in_progress){
				if(selected_point == null)
					add_point();
				else{
					selected_point.setLocation(IMP.opened_image.mouse_x, IMP.opened_image.mouse_y);
				}
			}
			else{
				start_selection(e);
			}
		}
	}
	
	@Override
	public void mouse_released(MouseEvent e){
		if(selected_point!=null)
			selected_point.is_selected=false;
		selected_point=null;
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
	
	public void paint_on_screen(Graphics g) {
		if(selectingInProgress){
			paintRectangleSelection(g);
		}
		if(selectionOn){
			paintSelection(g);
		}
	}
	
	
	@Override
	public void paint_on_screen(Graphics g, Image_Tile image_tile) {
		if(selecting_in_progress){
			paint_preview_selection(g, image_tile);
		}
	}
	
	public void paintRectangleSelection(Graphics g){
		g.setColor(new Color(160,160,250,60));
		g.fillPolygon(selection1);
		g.setColor(selectingColor);
		g.drawPolygon(selection1);
		if(length>0){
			g.drawString("" + (int) (length/IMP.active_image_tile.view.zoom), startX+length+25, startY+height);
		}
		else{
			g.drawString("" + (int) (-length/IMP.active_image_tile.view.zoom), startX+length-50, startY+height);
		}
		if(height>0){
			g.drawString("" + (int) (height/IMP.active_image_tile.view.zoom), startX+length-10, startY+height+30);
		}
		else{
			g.drawString("" + (int) (-height/IMP.active_image_tile.view.zoom), startX+length-10, startY+height-30);
		}
	}

	public static void createRectSelection(int mouseX, int mouseY) {
		if(mouseX<IMP.active_image_tile.view.img_pos_x()){
			startX=IMP.active_image_tile.view.img_pos_x();
		}
		else if(mouseX>IMP.active_image_tile.view.img_pos_x()
				+IMP.opened_image.getWidth()*IMP.active_image_tile.view.zoom){
			startX=(int) (IMP.active_image_tile.view.img_pos_x()
					+IMP.opened_image.getWidth()*IMP.active_image_tile.view.zoom);
		}
		else
			startX=mouseX;
		
		if(mouseY<IMP.active_image_tile.view.img_pos_y()){
			startY=IMP.active_image_tile.view.img_pos_y();
		}
		else if(mouseY>IMP.active_image_tile.view.img_pos_y()
				+IMP.opened_image.getHeight()*IMP.active_image_tile.view.zoom){
				startY=(int) (IMP.active_image_tile.view.img_pos_y()
						+IMP.opened_image.getHeight()*IMP.active_image_tile.view.zoom);
			}
		else
			startY=mouseY;
	}

	public static void updateRectangle(int x,int y) {
		
		if(x<IMP.active_image_tile.view.img_pos_x()){
			x=IMP.active_image_tile.view.img_pos_x();
		}
		else if(x>IMP.active_image_tile.view.img_pos_x()
				+IMP.opened_image.getWidth()*IMP.active_image_tile.view.zoom){
			x=(int) (IMP.active_image_tile.view.img_pos_x()
					+IMP.opened_image.getWidth()*IMP.active_image_tile.view.zoom);
		}
		
		if(y<IMP.active_image_tile.view.img_pos_y()){
			y=IMP.active_image_tile.view.img_pos_y();
		}
		else if(y>IMP.active_image_tile.view.img_pos_y()
				+IMP.opened_image.getHeight()*IMP.active_image_tile.view.zoom){
				y=(int) (IMP.active_image_tile.view.img_pos_y()
						+IMP.opened_image.getHeight()*IMP.active_image_tile.view.zoom);
			}
		
		length=x-startX;
		height=y-startY;
		selection1=new Polygon();
		Point p1 = new Point(startX,startY);
		Point p2 = new Point(x,startY);
		Point p3 = new Point(x,y);
		Point p4 = new Point(startX,y);
		selection1.addPoint(p1.x,p1.y);
		selection1.addPoint(p2.x,p2.y);
		selection1.addPoint(p3.x,p3.y);
		selection1.addPoint(p4.x,p4.y);
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
	private Dot selected_point;
	
//	private Point bezier(LinkedList<Point> points, double t){
//		if(points.size()==1)
//			return points.getFirst();
//		else
//			return (1-t)*bezier(points,t);
//		
//	}
//	
	public void paint_preview_selection(Graphics g, Image_Tile image_tile){
		Graphics2D g2=(Graphics2D) g;
		int i;
		
		//bezier curves
		Selection bezier=new Selection();
//		if(selection.size() == 3){
//			double t=0;
//			int P0x=selection.get_point_x(0);
//			int P1x=selection.get_point_x(1);
//			int P2x=selection.get_point_x(2);
//			int P0y=selection.get_point_y(0);
//			int P1y=selection.get_point_y(1);
//			int P2y=selection.get_point_y(2);
//			for (i = 0; i<100; i++){
//				bezier.add_point((int) ((1-t)*((1-t)*P0x+t*P1x)+t*((1-t)*P1x+t*P2x)),
//						(int) ((1-t)*((1-t)*P0y+t*P1y)+t*((1-t)*P1y+t*P2y)));
//				t+=0.01;
//			}
////			int n = selection.size();
////			for(i=n;n>0;i--){
////				Math.pow(t-1, i)*
////			}
//		}
//		else
//			bezier = selection;
		

		
		for(i=0;i+2<selection.size();i+=2){
			double t=0;
			
			int P0x=selection.get_point_x(i);
			int P1x=selection.get_point_x(i+1);
			int P2x=selection.get_point_x(i+2);
			int P0y=selection.get_point_y(i);
			int P1y=selection.get_point_y(i+1);
			int P2y=selection.get_point_y(i+2);
			for (int j = 0; j<25; j++){
				bezier.add_point((int) ((1-t)*((1-t)*P0x+t*P1x)+t*((1-t)*P1x+t*P2x)),
						(int) ((1-t)*((1-t)*P0y+t*P1y)+t*((1-t)*P1y+t*P2y)));
				t+=0.04;
			}
			bezier.add_point((int) ((1-t)*((1-t)*P0x+t*P1x)+t*((1-t)*P1x+t*P2x)),
					(int) ((1-t)*((1-t)*P0y+t*P1y)+t*((1-t)*P1y+t*P2y)));
		}
		
		
		Polygon selectionDisplay=new Polygon();
		for(i=0;i<bezier.size();i++){
			selectionDisplay.addPoint((int) (image_tile.view.img_pos_x()+bezier.get_point_x(i)*image_tile.view.zoom),
					(int) (image_tile.view.img_pos_y()+bezier.get_point_y(i)*image_tile.view.zoom));
		}
		
		Polygon selectionDisplayPoints=new Polygon();
		for(i=0;i<selection.size();i++){
			selectionDisplayPoints.addPoint((int) (image_tile.view.img_pos_x()+selection.get_point_x(i)*image_tile.view.zoom),
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
		g.fillOval(selectionDisplayPoints.xpoints[0]-5,selectionDisplayPoints.ypoints[0]-5, 10, 10);
		g.setColor(new Color(40,40,40));
		g.drawOval(selectionDisplayPoints.xpoints[0]-5,selectionDisplayPoints.ypoints[0]-5, 10, 10);
		//other points
		for(i=1; i<selectionDisplayPoints.npoints;i++){
			g.setColor(Color.yellow);
			g.fillOval(selectionDisplayPoints.xpoints[i]-5,selectionDisplayPoints.ypoints[i]-5, 10, 10);
			g.setColor(new Color(40,40,40));
			g.drawOval(selectionDisplayPoints.xpoints[i]-5,selectionDisplayPoints.ypoints[i]-5, 10, 10);
		}
		
		i=0;
		for (Dot point : selection.dots) {
			if(point.is_selected)
				g.setColor(Color.pink);
			else if(point.is_bezier)
				g.setColor(Color.blue);
			else
				g.setColor(Color.yellow);
			g.fillOval(selectionDisplayPoints.xpoints[i]-5,selectionDisplayPoints.ypoints[i]-5, 10, 10);
			g.setColor(new Color(40,40,40));
			g.drawOval(selectionDisplayPoints.xpoints[i]-5,selectionDisplayPoints.ypoints[i]-5, 10, 10);
			i++;
		}
//		if(image_tile.hasFocus())
//			g.drawLine(selectionDisplay.xpoints[i-1], selectionDisplay.ypoints[i-1],
//					image_tile.mouse_x, image_tile.mouse_y);
	}
	
	public void createSelection(){
		selection2=new Polygon();
		for(int i=0;i<selection1.npoints;i++){
			selection2.addPoint((int) ((selection1.xpoints[i]-IMP.active_image_tile.view.img_pos_x())/IMP.active_image_tile.view.zoom)
					,(int) ((selection1.ypoints[i]-IMP.active_image_tile.view.img_pos_y())/IMP.active_image_tile.view.zoom));
		}
	}

	public void paintSelection(Graphics g) {
		Polygon selectionDisplay=new Polygon();
		for(int i=0;i<selection2.npoints;i++){
			selectionDisplay.addPoint((int) (IMP.active_image_tile.view.img_pos_x()+selection2.xpoints[i]*IMP.active_image_tile.view.zoom),
					(int) (IMP.active_image_tile.view.img_pos_y()+selection2.ypoints[i]*IMP.active_image_tile.view.zoom));
			
		}
		g.drawPolygon(selectionDisplay);
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
		
		if(selection.size()>0){
			Dot last = selection.get_last_point();
			Dot bezier_point = new Dot((point.x+last.x)/2, (point.y+last.y)/2, true);
			selection.add_point(bezier_point);
			selection.add_point(point);
		}
		else{
			selection.add_point(point);
		}
	}
	
	public void remove_last_point(){
		selection.remove_last_point();
	}

	@Override
	public void receive_message(Message m) {
		// TODO Auto-generated method stub
		
	}
}
