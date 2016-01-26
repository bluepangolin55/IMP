package tools;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
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

public class Tool_3D 
extends Tool{

	
	static int i=100;
	static int test=100;
	static boolean iUP=true;
	static boolean	B_fill=true;
	
	ArrayList<Object> objects;
	Object selected_object;
	
	Vector camera_position;
	Vector orientation;
	Matrix camera_transformation;
	Vector view_distance;
	
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
	
	public Tool_3D() {
		super();
		set_name("3D Werkzeug");
		cursor=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		try {
			icon_default=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/brush_a.png"));
			icon_selected=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/brush_b.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
		camera_position=new Vector(new double[]{0,0,-10});
		view_distance=new Vector(new double[]{1,1,1});
		orientation=new Vector(new double[]{0,0,0});
		calculate_camera_transformation();
		mouse_press=new Point();


		//objects
		objects=new ArrayList<Object>();
		for(int i=0;i<20;i++){
			Object x_line=Object.line(50);
			x_line.move_to_origin();
			x_line.move(0, 0, i);
			objects.add(x_line);
		}
		for(int i=0;i<20;i++){
			Object y_line=Object.line(50);		
			y_line.rotate(0, 270, 0);
			y_line.move_to_origin();
			y_line.move(i, 0, 25);
			objects.add(y_line);
		}

		selected_object=Object.cube2(4, 4, 4);
		
//		for(int i=0;i<1;i++){
//			Object cube=Object.sphere(3, 3, 3);
//			cube.move_to_origin();
//			cube.move(5*(i%10), 5*(i/10), Math.random()*20);
////			cube.rotate(Math.random()*360, Math.random()*360, Math.random()*360);
////			cube.scale(Math.random()*1);
//			objects.add(cube);
//		}

		Object sphere=Object.sphere(3, 3, 3);
		objects.add(sphere);
		Object cube=Object.cube2(3, 3, 3);
		cube.move_to_origin();
		cube.move(0,0, 2);
		objects.add(cube);
	}
	
	private void select_object(Point point_on_screen){
		for(int i=0;i<objects.size();i++){
			if(objects.get(i).is_hovered(point_on_screen, camera_position, camera_transformation, view_distance)){
				selected_object.deselect();
				selected_object=objects.get(i);
				selected_object.select();
				object_color.set_color(selected_object.get_color());
				name_textfield.set_text(selected_object.name);
				color_choser.set_color(selected_object.color);
				color_choser2.set_color(selected_object.color);
				break;
			}
		}	
	}
	
	private void calculate_camera_transformation(){
		camera_transformation=new Matrix(new double[][]{{1,0,0},
				{0, Math.cos(orientation.get(1)),-Math.sin(orientation.get(1))},
				{0,Math.sin(orientation.get(1)), Math.cos(orientation.get(1))}});
		camera_transformation=Matrix.product(camera_transformation,
				new Matrix(new double[][]{{Math.cos(orientation.get(2)),0,Math.sin(orientation.get(2))},
				{0, 1, 0},
				{-Math.sin(orientation.get(2)), 0, Math.cos(orientation.get(2))}}));
		camera_transformation=Matrix.product(camera_transformation,
				new Matrix(new double[][]{{Math.cos(orientation.get(3)),-Math.sin(orientation.get(3)),0},
				{Math.sin(orientation.get(3)), Math.cos(orientation.get(3)), 0},
				{0, 0, 1}}));
	}

	@Override
	public void mouse_pressed(MouseEvent e){
		select_object(e.getPoint());
		mouse_press=e.getPoint();
		if(e.isControlDown()){
		}
		else{
			selected_object.log_position();
		}
	}
	
	@Override
	public void mouse_dragged(MouseEvent e){
		
		if(e.getModifiers()==8){
			camera_position.set(1, camera_position.get(1)+(e.getPoint().x-mouse_press.x)/-30.0);
			camera_position.set(2, camera_position.get(2)+(e.getPoint().y-mouse_press.y)/-30.0);
			mouse_press=e.getPoint();
		}
		else{
			if(e.isControlDown()){
				selected_object.rotate(0,(e.getPoint().x-mouse_press.x)/3.0,0);
				selected_object.rotate((mouse_press.y-e.getPoint().y)/3.0,0,0);
				mouse_press=e.getPoint();
			}
			else if(e.isShiftDown()){
				selected_object.scale(1+(e.getPoint().x-mouse_press.x)/100.0);
				mouse_press=e.getPoint();
			}
			else{
				double distance_to_camera=selected_object.distance_to_camera(camera_position);
				selected_object.move((e.getPoint().x-mouse_press.x+0.0)*distance_to_camera/3330,0,0);
				selected_object.move(0,(e.getPoint().y-mouse_press.y+0.0)*distance_to_camera/3330,0);
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
			view_distance.set(1, view_distance.get(1)+e.getWheelRotation());
			view_distance.set(2, view_distance.get(2)+e.getWheelRotation());
			view_distance.set(3, view_distance.get(3)+e.getWheelRotation());
//			camera_position.set(2, camera_position.get(2)+e.getWheelRotation());
			
//			orientation.set(3, orientation.get(3)+e.getWheelRotation()/10.0);
//			orientation.set(2, orientation.get(2)+e.getWheelRotation());
//			orientation.set(3, orientation.get(3)+e.getWheelRotation());
//			calculate_camera_transformation();
		}
		else if (e.isShiftDown()){
			orientation.set(2, orientation.get(2)+e.getWheelRotation()/10.0);
//			orientation.set(2, orientation.get(2)+e.getWheelRotation());
//			orientation.set(3, orientation.get(3)+e.getWheelRotation());
			calculate_camera_transformation();
		}
		else{
			camera_position.set(3, camera_position.get(3)-e.getWheelRotation());
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
			selected_object.move_to_last_position();
		}
	}
	else{
		if(e.getKeyCode()==KeyEvent.VK_LEFT){
	//		test_object.move(-1,0,0);
			selected_object.rotate(0,-10,0);
			for(int i=0;i<objects.size();i++){
				objects.get(i).rotate(0,-10,0);
			}
		}
		else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
	//		test_object.move(1,0,0);
			selected_object.rotate(0,10,0);
			for(int i=0;i<objects.size();i++){
				objects.get(i).rotate(0,10,0);
			}		
		}
		if(e.getKeyCode()==KeyEvent.VK_UP){
	//		test_object.move(0,-1,0);
			selected_object.rotate(10,0,0);
//			for(int i=0;i<objects.size();i++){
//				objects.get(i).rotate(10,0,0);
//			}
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN){
	//		test_object.move(0,1,0);
			selected_object.rotate(-10,0,0);
			for(int i=0;i<objects.size();i++){
				objects.get(i).rotate(-10,0,0);
			}		
	
		}
		else if(e.getKeyCode()==KeyEvent.VK_G){
	//		view_distance.set(1,  view_distance.get(1)+1);
	//		view_distance.set(2,  view_distance.get(2)+1);
			view_distance.set(3,  view_distance.get(3)+1);
		}
		else if(e.getKeyCode()==KeyEvent.VK_H){
	//		view_distance.set(1, view_distance.get(1)-1);
	//		view_distance.set(2, view_distance.get(2)-1);
			view_distance.set(3, view_distance.get(3)-1);
		}
		else if(e.getKeyCode()==KeyEvent.VK_W){
			camera_position.set(3, camera_position.get(3)+1);
		}
		else if(e.getKeyCode()==KeyEvent.VK_S){
			camera_position.set(3, camera_position.get(3)-1);
		}
		else if(e.getKeyCode()==KeyEvent.VK_A){
			camera_position.set(1, camera_position.get(1)-1);
		}
		else if(e.getKeyCode()==KeyEvent.VK_D){
			camera_position.set(1, camera_position.get(1)+1);
		}
		else if(e.getKeyCode()==KeyEvent.VK_Q){
			if(painting_mode==COLOR)
				painting_mode=WIREFRAME;
			else
				painting_mode=COLOR;
		}
		else if(e.getKeyCode()==KeyEvent.VK_O){
			selected_object.move_to_origin();
		}
		else if(e.getKeyCode()==KeyEvent.VK_1){
			selected_object.scale(1.1);
		}
	}
}
	
	@Override
	public void paint_on_screen(Graphics g, Image_Tile tile) {
		double[] distances=new double[objects.size()];
		
		//apply stuff to objects
//		selected_object.set_color(object_color.get_color());
		
		for(int i=0;i<objects.size();i++){
			distances[i]=objects.get(i).distance_to_camera(camera_position);
		}		
		int index;
		for(int i=0;i<objects.size();i++){
			index=find_biggest_value(distances);
			distances[index]=0;
			objects.get(index).paint(g, camera_position, camera_transformation, view_distance, painting_mode);
		}		
		
		g.setColor(color_text_bright);
		g.drawString("Camera: ", IMP.active_image_tile.getWidth()-100,IMP.active_image_tile.getHeight()-220);
		g.drawString("x=" + camera_position.get(1), IMP.active_image_tile.getWidth()-100,IMP.active_image_tile.getHeight()-200);
		g.drawString("y=" + camera_position.get(2), IMP.active_image_tile.getWidth()-100,IMP.active_image_tile.getHeight()-180);
		g.drawString("z=" + camera_position.get(3), IMP.active_image_tile.getWidth()-100,IMP.active_image_tile.getHeight()-160);
		
		g.drawString("rotation: ", IMP.active_image_tile.getWidth()-200,IMP.active_image_tile.getHeight()-220);
		g.drawString("x=" + orientation.get(1), IMP.active_image_tile.getWidth()-200,IMP.active_image_tile.getHeight()-200);
		g.drawString("y=" + orientation.get(2), IMP.active_image_tile.getWidth()-200,IMP.active_image_tile.getHeight()-180);
		g.drawString("z=" + orientation.get(3), IMP.active_image_tile.getWidth()-200,IMP.active_image_tile.getHeight()-160);
		
		g.drawString("selected object: ", IMP.active_image_tile.getWidth()-300,IMP.active_image_tile.getHeight()-220);
		g.drawString("x=" + selected_object.position().get(1), IMP.active_image_tile.getWidth()-300,IMP.active_image_tile.getHeight()-200);
		g.drawString("y=" + selected_object.position().get(2), IMP.active_image_tile.getWidth()-300,IMP.active_image_tile.getHeight()-180);
		g.drawString("z=" + selected_object.position().get(3), IMP.active_image_tile.getWidth()-300,IMP.active_image_tile.getHeight()-160);
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
