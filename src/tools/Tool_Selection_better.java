package tools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.IMP;
import main.Menuelement;

import file.Img;
import functionality.Selection;
import gui_system.GuiIntValue;
import gui_system.Message;

public class Tool_Selection_better 
extends Tool{
	
	// values 
	
	GuiIntValue v0; 
	GuiIntValue v1; 
	
	public Tool_Selection_better() {
		super();
		//GUI
		set_name("Auswahlwerkeug2");
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
		createRectSelection(e.getX(),e.getY());
		selectingInProgress=true;	
	}
	
	@Override
	public void mouse_dragged(MouseEvent e){
		updateRectangle(e.getX(),e.getY());
	}
	
	@Override
	public void mouse_released(MouseEvent e){
		selectingInProgress=false;
		selectionOn=true;
		createSelection();
	}
	
	public void paint_on_screen(Graphics g) {
		if(selectingInProgress){
			paintRectangleSelection(g);
		}
		else if(selectionOn){
			paintSelection(g);
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
	int startX;
	int startY;
	int length;
	int height;
	Color selectingColor =new Color(40,40,40);
	public static boolean selectingInProgress;
	


	
	
	public void paintRectangleSelection(Graphics g){
		g.setColor(new Color(160,160,250,60));
		g.fillPolygon(preview_selection2);
		g.setColor(selectingColor);
//		g.drawPolygon(temporary_selection);
		
		Polygon selectionDisplay=new Polygon();
		for(int i=0;i<preview_selection.size();i++){
			selectionDisplay.addPoint((int) (IMP.active_image_tile.view.img_pos_x()+selection.get_point_x(i)*IMP.active_image_tile.view.zoom),
					(int) (IMP.active_image_tile.view.img_pos_y()+selection.get_point_y(i)*IMP.active_image_tile.view.zoom));
		}
		g.drawPolygon(selectionDisplay);
		
		if(length>0){
			g.drawString("" + (int) (length/IMP.active_image_tile.view.zoom), IMP.opened_image.mouse_x+25, IMP.opened_image.mouse_y);
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

	public void createRectSelection(int x, int y) {
		selection = new Selection();
		selection.add_point(IMP.opened_image.mouse_x,IMP.opened_image.mouse_y);
		selection.add_point(IMP.opened_image.mouse_x,IMP.opened_image.mouse_y);
		selection.add_point(IMP.opened_image.mouse_x,IMP.opened_image.mouse_y);
		selection.add_point(IMP.opened_image.mouse_x,IMP.opened_image.mouse_y);
		
		preview_selection = new Selection();
		preview_selection.add_point(x,y);
		preview_selection.add_point(x,y);
		preview_selection.add_point(x,y);
		preview_selection.add_point(x,y);
		
		preview_selection2=new Polygon();
		preview_selection2.addPoint(x,y);
		preview_selection2.addPoint(x,y);
		preview_selection2.addPoint(x,y);
		preview_selection2.addPoint(x,y);
		
		selection2=new Polygon();
		selection2.addPoint(IMP.opened_image.mouse_x,IMP.opened_image.mouse_y);
		selection2.addPoint(IMP.opened_image.mouse_x,IMP.opened_image.mouse_y);
		selection2.addPoint(IMP.opened_image.mouse_x,IMP.opened_image.mouse_y);
		selection2.addPoint(IMP.opened_image.mouse_x,IMP.opened_image.mouse_y);
		
		//check if mouse_x is smaller than the image position
		if(IMP.opened_image.mouse_x<0){
			preview_selection2.xpoints[0]=IMP.active_image_tile.view.img_pos_x();
			preview_selection2.xpoints[1]=IMP.active_image_tile.view.img_pos_x();
			preview_selection2.xpoints[2]=IMP.active_image_tile.view.img_pos_x();
			preview_selection2.xpoints[3]=IMP.active_image_tile.view.img_pos_x();
			
			selection2.xpoints[0]=0;
			selection2.xpoints[1]=0;
			selection2.xpoints[2]=0;
			selection2.xpoints[3]=0;
		}
		//check if mouse_y is smaller than the image position
		if(IMP.opened_image.mouse_y<0){
			preview_selection2.ypoints[0]=IMP.active_image_tile.view.img_pos_y();
			preview_selection2.ypoints[1]=IMP.active_image_tile.view.img_pos_y();
			preview_selection2.ypoints[2]=IMP.active_image_tile.view.img_pos_y();
			preview_selection2.ypoints[3]=IMP.active_image_tile.view.img_pos_y();
			
			selection2.ypoints[0]=0;
			selection2.ypoints[1]=0;
			selection2.ypoints[2]=0;
			selection2.ypoints[3]=0;
		}	
		//check if mouse_x is bigger than the image position
		if(IMP.opened_image.mouse_x>IMP.opened_image.getWidth()){
			preview_selection2.xpoints[0]=IMP.active_image_tile.view.img_pos_x()+IMP.active_image_tile.view.img_width();
			preview_selection2.xpoints[1]=IMP.active_image_tile.view.img_pos_x()+IMP.active_image_tile.view.img_width();
			preview_selection2.xpoints[2]=IMP.active_image_tile.view.img_pos_x()+IMP.active_image_tile.view.img_width();
			preview_selection2.xpoints[3]=IMP.active_image_tile.view.img_pos_x()+IMP.active_image_tile.view.img_width();
			
			selection2.xpoints[0]=IMP.opened_image.getWidth();
			selection2.xpoints[1]=IMP.opened_image.getWidth();
			selection2.xpoints[2]=IMP.opened_image.getWidth();
			selection2.xpoints[3]=IMP.opened_image.getWidth();
		}
		//check if mouse_y is bigger than the image position
		if(IMP.opened_image.mouse_y>IMP.opened_image.getHeight()){
			preview_selection2.ypoints[0]=IMP.active_image_tile.view.img_pos_y()+IMP.active_image_tile.view.img_height();
			preview_selection2.ypoints[1]=IMP.active_image_tile.view.img_pos_y()+IMP.active_image_tile.view.img_height();
			preview_selection2.ypoints[2]=IMP.active_image_tile.view.img_pos_y()+IMP.active_image_tile.view.img_height();
			preview_selection2.ypoints[3]=IMP.active_image_tile.view.img_pos_y()+IMP.active_image_tile.view.img_height();
			
			selection2.ypoints[0]=IMP.opened_image.getHeight();
			selection2.ypoints[1]=IMP.opened_image.getHeight();
			selection2.ypoints[2]=IMP.opened_image.getHeight();
			selection2.ypoints[3]=IMP.opened_image.getHeight();
		}
	}

	public void updateRectangle(int x,int y) {
		
		//updates the polygons of the rectangle selection clockwise
		
		if((IMP.opened_image.mouse_x<0 || IMP.opened_image.mouse_x>IMP.opened_image.getWidth()) && 
				(IMP.opened_image.mouse_y<0 || IMP.opened_image.mouse_y>IMP.opened_image.getHeight())){
		}	
		else if(IMP.opened_image.mouse_x<0 || IMP.opened_image.mouse_x>IMP.opened_image.getWidth()){
			preview_selection2.ypoints[1]=y;
			
			preview_selection2.ypoints[2]=y;
		}
		else if(IMP.opened_image.mouse_y<0 || IMP.opened_image.mouse_y>IMP.opened_image.getHeight()){
			preview_selection2.xpoints[2]=x;
			
			preview_selection2.xpoints[3]=x;
		}	
		
		else {
			preview_selection2.ypoints[1]=y;
			
			preview_selection2.xpoints[2]=x;
			preview_selection2.ypoints[2]=y;
			
			preview_selection2.xpoints[3]=x;
		}
		System.out.println(preview_selection.size());
		preview_selection.set_point_y(1, y);
		
		preview_selection.set_point_x(2, x);
		preview_selection.set_point_y(2, y);
		
		preview_selection.set_point_x(3, x);
		

	}
	
	public void createSelection(){
//		selection2.xpoints[1]=1;	//stays
		selection2.ypoints[1]=IMP.opened_image.mouse_y;
		
		selection2.xpoints[2]=IMP.opened_image.mouse_x;
		selection2.ypoints[2]=IMP.opened_image.mouse_y;
		
		selection2.xpoints[3]=IMP.opened_image.mouse_x;
//		selection2.ypoints[3]=1;	//stays
		
		selection.set_point_y(1, IMP.opened_image.mouse_y);
		
		selection.set_point_x(2, IMP.opened_image.mouse_x);
		selection.set_point_y(2, IMP.opened_image.mouse_y);
		
		selection.set_point_x(3, IMP.opened_image.mouse_x);
	}

	public void paintSelection(Graphics g) {
		Polygon selectionDisplay=new Polygon();
		for(int i=0;i<selection.size();i++){
			selectionDisplay.addPoint((int) (IMP.active_image_tile.view.img_pos_x()+selection.get_point_x(i)*IMP.active_image_tile.view.zoom),
					(int) (IMP.active_image_tile.view.img_pos_y()+selection.get_point_y(i)*IMP.active_image_tile.view.zoom));
		}
		g.drawPolygon(selectionDisplay);
	}

	@Override
	public void receive_message(Message m) {
		// TODO Auto-generated method stub
		
	}

}
