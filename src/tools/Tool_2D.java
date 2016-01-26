package tools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import main.IMP;
import main.Menuelement;
import file.Img;
import functionality.Matrix;
import functionality.Object;
import functionality.Vector;
import functionality.Vector_Element;
import gui.Image_Tile;
import gui_system.GuiColorValue;
import gui_system.GuiIntValue;
import gui_system.GuiStringValue;
import gui_system.Message;
import gui_system.PE_Color_Choser;
import gui_system.PE_Color_Choser2;
import gui_system.PE_Endless_Slider;
import gui_system.PE_Label;
import gui_system.PE_Textfield;

public class Tool_2D 
extends Tool{

	
	static int i=100;
	static int test=100;
	static boolean iUP=true;
	static boolean	B_fill=true;
	
	ArrayList<Vector_Element> vector_elements;
	Vector_Element selected_object;
	
	Point mouse_press;
	
	PE_Color_Choser color_choser;
	PE_Color_Choser2 color_choser2;
	PE_Label name_label;
	PE_Textfield name_textfield;
	PE_Endless_Slider object_x_position_slider;
	
	//gui
	int painting_mode=2;
	public final static int WIREFRAME=1;
	public final static int COLOR=2;
	
	public Tool_2D() {
		super();
		set_name("3D Werkzeug");
		cursor=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		try {
			icon_default=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/brush_a.png"));
			icon_selected=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/brush_b.png"));
		} catch (IOException e) {
		}
		
		set_menu(new Menuelement("menu",null));
		
		//panel elements
		object_name = new GuiStringValue();
		object_x_position_slider=new PE_Endless_Slider("x-position", new GuiIntValue(1,20,10));
		object_color = new GuiColorValue();
		
		hud_panel.add(name_label = new PE_Label(object_name));
		hud_panel.add(name_textfield = new PE_Textfield(object_name));
		hud_panel.add(object_x_position_slider);		
		hud_panel.add(color_choser=new PE_Color_Choser("Farbe", object_color));
		
		sidepanel.add(name_label = new PE_Label(object_name));
		sidepanel.add(name_textfield = new PE_Textfield(object_name));
		sidepanel.add(object_x_position_slider);	
		sidepanel.add(color_choser2=new PE_Color_Choser2("Farbe", object_color, this));
		
		mouse_press=new Point();


		//objects
		vector_elements=new ArrayList<Vector_Element>();

		Vector_Element square = Vector_Element.square(100, 100, 200, 200);
		selected_object = square;
		

		vector_elements.add(square);
	}
	
	private void select_object(Point point_on_screen){
		for(int i=0;i<vector_elements.size();i++){
//			if(objects.get(i).is_hovered(point_on_screen, camera_position, camera_transformation, view_distance)){
//				selected_object.deselect();
//				selected_object=objects.get(i);
//				selected_object.select();
//				object_color.set_color(selected_object.get_color());
//				name_textfield.set_text(selected_object.name);
//				color_choser.set_color(selected_object.color);
//				color_choser2.set_color(selected_object.color);
//				break;
//			}
		}	
	}
	
	@Override
	public void mouse_pressed(MouseEvent e){
		select_object(e.getPoint());
		mouse_press=e.getPoint();
		if(e.isControlDown()){
		}
		else{
		}
	}
	
	@Override
	public void mouse_dragged(MouseEvent e){
		System.out.println("hi");
		selected_object.position = e.getPoint();
		
		if(e.getModifiers()==8){
			mouse_press=e.getPoint();
		}
		else{
			if(e.isControlDown()){
				mouse_press=e.getPoint();
			}
			else if(e.isShiftDown()){
				mouse_press=e.getPoint();
			}
			else{
				mouse_press=e.getPoint();
			}
		}
		



	}
	
	@Override
	public void mouse_released(MouseEvent e){
	}
	
	@Override
	public void mouse_moved(MouseEvent e) {
	}
	
	@Override
	public void mouse_wheel(MouseWheelEvent e) {
		if(e.isAltDown()){
		}
		else if (e.isShiftDown()){
		}
		else{
		}
//		test_plane.x.set(2, test_plane.x.get(2)+e.getWheelRotation());
	}
	
	@Override
	public void mouse_clicked(MouseEvent e) {

	}
	
@Override
public void key_pressed(KeyEvent e) {
	if(e.isControlDown()){
		if(e.getKeyCode()==KeyEvent.VK_Z){
		}
	}
	else{
		if(e.getKeyCode()==KeyEvent.VK_LEFT){
		}
		else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
		}
		if(e.getKeyCode()==KeyEvent.VK_UP){
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN){
		}
		else if(e.getKeyCode()==KeyEvent.VK_G){
		}
		else if(e.getKeyCode()==KeyEvent.VK_H){
		}
		else if(e.getKeyCode()==KeyEvent.VK_W){
		}
		else if(e.getKeyCode()==KeyEvent.VK_S){
		}
		else if(e.getKeyCode()==KeyEvent.VK_A){
		}
		else if(e.getKeyCode()==KeyEvent.VK_D){
		}
		else if(e.getKeyCode()==KeyEvent.VK_Q){
			if(painting_mode==COLOR)
				painting_mode=WIREFRAME;
			else
				painting_mode=COLOR;
		}
		else if(e.getKeyCode()==KeyEvent.VK_O){
		}
		else if(e.getKeyCode()==KeyEvent.VK_1){
		}
	}
}
	
	@Override
	public void paint_on_screen(Graphics g, Image_Tile tile) {
		double[] distances=new double[vector_elements.size()];
		
		//apply stuff to objects
//		selected_object.set_color(object_color.get_color());
		
		int index;
		for(Vector_Element ve : vector_elements){

			ve.draw(g);
			//g.drawPolygon(ve.form);
		}		
		
		g.setColor(color_text_bright);
		g.drawString("Camera: ", IMP.active_image_tile.getWidth()-100,IMP.active_image_tile.getHeight()-220);
		g.drawString("selected object: ", IMP.active_image_tile.getWidth()-300,IMP.active_image_tile.getHeight()-220);
		g.drawString("x=" + selected_object.position.x, IMP.active_image_tile.getWidth()-300,IMP.active_image_tile.getHeight()-200);
		g.drawString("y=" + selected_object.position.y, IMP.active_image_tile.getWidth()-300,IMP.active_image_tile.getHeight()-180);
	}
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
	//------------------------------------------------------------------------------------------------------------------------------
			//funcionality
			
//			Function parameters that the user can change in the dialogue. The calculated  values are set to make the filter work in a default way.
	GuiColorValue object_color;
	GuiStringValue object_name;

	private int find_biggest_value(double[] array){
		int index=0;
		double highest_value=0;
		for(int i=0;i<array.length;i++){
			if(array[i]>highest_value){
				highest_value=array[i];
				index=i;
			}
		}
		return index;
	}

	@Override
	public void receive_message(Message m) {
		// TODO Auto-generated method stub
		
	}

}
