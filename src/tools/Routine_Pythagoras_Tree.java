package tools;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import com.lrdev.turtle.Turtle;

import main.IMP;
import file.Img;
import gui_system.GuiBooleanValue;
import gui_system.GuiIntValue;
import gui_system.PE_Apply_Abort;
import gui_system.PE_Check_Button;
import gui_system.PE_Slider;

public class Routine_Pythagoras_Tree 
extends Routine{

	// values 
	GuiIntValue v1;
	GuiIntValue v2;
	GuiIntValue v3;
	GuiIntValue v4;
	GuiIntValue v5;
	GuiIntValue v6;
	GuiBooleanValue v7;
	
	public Routine_Pythagoras_Tree() {
		super();
		set_name("Pythagoras Baum");
		category = "Muster";
		
		v1 = new GuiIntValue(1,200,20);
		v2 = new GuiIntValue(1,100,10);
		v3 = new GuiIntValue(1,100,50);
		v4 = new GuiIntValue(1,100,30);
		v5 = new GuiIntValue(1,90,50);
		v6 = new GuiIntValue(1,90,50);
		v7 = new GuiBooleanValue(false);
		
		add_gui_element(new PE_Slider("lmax", v1,this, ""));
		add_gui_element(new PE_Slider("lmin", v2,this, ""));
		add_gui_element(new PE_Slider("posX", v3,this, ""));
		add_gui_element(new PE_Slider("posY", v4,this, ""));
		add_gui_element(new PE_Slider("degree", v5,this, ""));
		add_gui_element(new PE_Slider("degree", v6,this, ""));
		add_gui_element(new PE_Check_Button("HIntergrundbild", v7,this));
		
		add_gui_element(new PE_Apply_Abort(this));
		
	}

	@Override
	public void activate(){
		super.activate();
		IMP.image_informant.inform_about_preview_changed();
		IMP.image_informant.inform_about_preview_available(true);
		
		//reconfigure sliders
		v1.reconfigure(1,200,20);
		v2.reconfigure(1,100,10);
		v3.reconfigure(1,IMP.opened_image.getHeight(),50);
		v4.reconfigure(1,IMP.opened_image.getWidth(),30);
		v5.reconfigure(1,90,50);
		v6.reconfigure(1,90,50);
		
		//IMP.preview_image=new Img(IMP.opened_image.getWidth(),IMP.opened_image.getHeight(),1);
		IMP.preview_image = new BufferedImage(IMP.opened_image.getWidth(), 
				IMP.opened_image.getHeight(), IMP.opened_image.getImage().getType());
		worker = new Worker();
		IMP.thread_pool.execute(worker);
		//paint_preview();
	}
	
	@Override
	public void apply(){
		super.apply();
		IMP.opened_image.get_selected_layer().set_image(IMP.preview_image);
	}
	@Override
	public void abort(){
		super.abort();
	}
	
	@Override
	public void mouse_dragged(MouseEvent e){
		v3.set_value(IMP.opened_image.mouse_x);
		v4.set_value(IMP.opened_image.mouse_y);

		worker.cancel(true);
		worker.reinitialize();
		worker.fork();
		//paint_preview();
	}
	
	@Override
	public void mouse_pressed(MouseEvent e){
		v3.set_value(IMP.opened_image.mouse_x);
		v4.set_value(IMP.opened_image.mouse_y);

		worker.cancel(true);
		worker.reinitialize();
		worker.fork();
		//paint_preview();
	}
	
	
//	public void key_released(KeyEvent e) {
//		super.key_released(e);
//		int key = e.getKeyCode();
//
//		
//		
//		if (key == KeyEvent.VK_SPACE){
//			panel.applyButtonPressed=false;
//			apply();
//		}
//	}
	
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
	//------------------------------------------------------------------------------------------------------------------------------
			//funcionality
			
//			Function parameters that the user can change in the dialogue. The calculated  values are set to make the filter work in a default way.
			static int lmax=80;	//side length of the biggest cube
			static int lmin=1;	//side length of the smallest cube
			static int posX=300;	//x value of the position of the biggest cube's bottom right corner
			static int posY=300;	//y value of the position of the biggest cube's bottom right corner
			static boolean randomDegree=false;	//set the degrees to bet calculated randomly for each cube;
			static int degree1=45;	//degree of the left corner of the triangle over a cube
			static int degree2=45;	//degree of the right corner of the triangle over a cube
			static boolean fill=false;	//if true, the function will fill the cubes, otherwise it will just draw them
			static boolean background=true;	//if true, the selected color will make the background, if false, the function will take the loaded image as background.
			static Color Background = new Color(255,255,255);	//background color of the Image
			static boolean ColorsMerge=true;	//if true, it enables the usert to pick two colors and merges them one into the other
			static Color One = new Color(0,0,0); //color of the biggest cube
			static Color Two = new Color(50,255,50);	//color of the smallest cubes
			static int AmmountOfTrees=5;	//the amount of trees, that are beeing created over each other
			
			

			
			@Override
			public void paint_preview(){
				Graphics g=IMP.preview_image.getGraphics();
				
				g.drawImage(IMP.opened_image.getImage(), 0, 0, IMP.opened_image.getWidth(), IMP.opened_image.getHeight(), IMP.main_panel);
				if(IMP.opened_image.active_selections.size()>0)
					g.setClip(IMP.opened_image.active_selections.get(0).points);
				
		//!!!!!!!!!!!!!!!!!!!!!!!!!
				if(v7.getValue()){
					g.drawImage(IMP.opened_image.getImage(),1,1,null);	//Draws the image as Background
				}
				else{
					
					g.fillRect(0,0,IMP.opened_image.getWidth(),IMP.opened_image.getHeight());	//Background color
					g.setColor(Background);
				}
				lmin=v2.getInt();
				posX=v3.getInt();
				posY=v4.getInt();
				degree1=v5.getInt();

				
				//paint start rectangle
				Polygon poly1=new Polygon();
				Point p11 = new Point(v3.getInt()+v1.getInt(),v4.getInt());
				Point p12 = new Point(v3.getInt()+v1.getInt(),v4.getInt()+v1.getInt());
				Point p13 = new Point(v3.getInt(),v4.getInt()+v1.getInt());
				Point p14 = new Point(v3.getInt(),v4.getInt());
				poly1.addPoint(p11.x,p11.y);
				poly1.addPoint(p12.x,p12.y);
				poly1.addPoint(p13.x,p13.y);
				poly1.addPoint(p14.x,p14.y);
				Color Tree = new Color (25,20,20);
				g.setColor(Tree);
				g.fillPolygon(poly1);

//				paintTree_filled(g,l,sliderArray[1].get_integer(),p11,p12,p13,p14,0,Tree);
				Tree = new Color (1,1,1);
				g.setColor(Tree);
				for(int i=0;i<AmmountOfTrees;i++)
				paintTree(g,v1.getInt(),v2.getInt(),p11,p12,p13,p14,0,Tree);
				
				IMP.image_informant.inform_about_image_changed();
			}
			
			static void paintTree_filled(Graphics g, int l1, int lmin,Point p11,Point p12,Point p13,Point p14,int direction,Color Tree){
				int w1=((int)(Math.random() * 100%40+30));
				int w2=90-w1;
				g.setColor(Tree);
				int l2=(int) (java.lang.Math.sin(Math.toRadians(w1))*l1);
				l1=(int) (java.lang.Math.sin(Math.toRadians(w2))*l1);
				
				
				
				if(l2>lmin){
					Rectangle rect = new Rectangle(1,49,20-400,20-49);				//?
			        Turtle turtle = new Turtle(g, rect);
					turtle.setHeadingMode(Turtle.DEGREE);
			        turtle.penup();
			
			        turtle.setxy(p11.x,p11.y);
			        turtle.setheading(direction);
					Point p22 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
			        turtle.right(w1);
			        turtle.forward(l2);
					Point p21 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
					int direction2=(int) turtle.heading()+90;
			        turtle.right(90);
			        turtle.forward(l2);
					Point p24 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
			        turtle.right(90);
			        turtle.forward(l2);
					Point p23 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
					p12 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
					int direction1=(int) turtle.heading()+180;
			        turtle.left(90);
			        turtle.forward(l1);
					p11 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
			        turtle.right(90);
			        turtle.forward(l1);
					p14 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
			        turtle.right(90);
			        turtle.forward(l1);
					p13 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));

					Polygon poly1=new Polygon();
					poly1.addPoint(p11.x,p11.y);
					poly1.addPoint(p12.x,p12.y);
					poly1.addPoint(p13.x,p13.y);
					poly1.addPoint(p14.x,p14.y);
					
					Polygon poly2=new Polygon();
					poly2.addPoint(p21.x,p21.y);
					poly2.addPoint(p22.x,p22.y);
					poly2.addPoint(p23.x,p23.y);
					poly2.addPoint(p24.x,p24.y);
					
					Polygon poly3=new Polygon();
					poly3.addPoint(p12.x,p12.y);
					poly3.addPoint(p13.x,p13.y);			
					poly3.addPoint(p22.x,p22.y);

					g.fillPolygon(poly1);
					g.fillPolygon(poly2);
					g.fillPolygon(poly3);
					
					Color Tree1 = new Color(Tree.getRed(),Tree.getGreen(),Tree.getBlue());
					Color Tree2 = new Color(Tree.getRed(),Tree.getGreen(),Tree.getBlue());

					paintTree_filled(g,l1,lmin,p11,p12,p13,p14,direction1,Tree1);
					paintTree_filled(g,l2,lmin,p21,p22,p23,p24,direction2,Tree2);
				}
			}
			
			static void paintTree(Graphics g, int l1, int lmin,Point p11,Point p12,Point p13,Point p14,int direction,Color Tree){
				if(randomDegree==true){
					degree1=((int)(Math.random() * 100%40+30));
					degree2=90-degree1;
				}

				g.setColor(Tree);
				int l2=(int) (java.lang.Math.sin(Math.toRadians(degree1))*l1);
				l1=(int) (java.lang.Math.sin(Math.toRadians(degree2))*l1);
				
				
				
				if(l2>lmin){
					Rectangle rect = new Rectangle(1,49,20-400,20-49);				//?
			        Turtle turtle = new Turtle(g, rect);
					turtle.setHeadingMode(Turtle.DEGREE);
			        turtle.penup();
			
			        turtle.setxy(p11.x,p11.y);
			        turtle.setheading(direction);
					Point p22 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
			        turtle.right(degree1);
			        turtle.forward(l2);
					Point p21 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
					int direction2=(int) turtle.heading()+90;
			        turtle.right(90);
			        turtle.forward(l2);
					Point p24 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
			        turtle.right(90);
			        turtle.forward(l2);
					Point p23 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
					p12 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
					int direction1=(int) turtle.heading()+180;
			        turtle.left(90);
			        turtle.forward(l1);
					p11 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
			        turtle.right(90);
			        turtle.forward(l1);
					p14 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));
			        turtle.right(90);
			        turtle.forward(l1);
					p13 = new Point((int) (turtle.xcor()),(int) (turtle.ycor()));

					Polygon poly1=new Polygon();
					poly1.addPoint(p11.x,p11.y);
					poly1.addPoint(p12.x,p12.y);
					poly1.addPoint(p13.x,p13.y);
					poly1.addPoint(p14.x,p14.y);
					
					Polygon poly2=new Polygon();
					poly2.addPoint(p21.x,p21.y);
					poly2.addPoint(p22.x,p22.y);
					poly2.addPoint(p23.x,p23.y);
					poly2.addPoint(p24.x,p24.y);
					
					Polygon poly3=new Polygon();
					poly3.addPoint(p12.x,p12.y);
					poly3.addPoint(p13.x,p13.y);			
					poly3.addPoint(p22.x,p22.y);

					g.drawPolygon(poly1);
					g.drawPolygon(poly2);
					g.drawPolygon(poly3);
					
					Color Tree1 = new Color(Tree.getRed(),Tree.getGreen(),Tree.getBlue());
					Color Tree2 = new Color(Tree.getRed(),Tree.getGreen(),Tree.getBlue());

					paintTree(g,l1,lmin,p11,p12,p13,p14,direction1,Tree1);
					paintTree(g,l2,lmin,p21,p22,p23,p24,direction2,Tree2);
				}
			}
}
