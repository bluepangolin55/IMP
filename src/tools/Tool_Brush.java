package tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;

import main.IMP;
import main.Menuelement;

import file.Img;
import gui.Design_Preferences;
import gui.Image_Tile;
import gui_system.GuiClient;
import gui_system.GuiColorValue;
import gui_system.GuiIntValue;
import gui_system.Message;
import gui_system.PE_Color_Choser;
import gui_system.PE_Color_Choser2;
import gui_system.PE_Separator;
import gui_system.PE_Slider;
import gui_system.Tile;

public class Tool_Brush 
extends Tool
implements Design_Preferences, GuiClient{

	
	static int i=100;
	static int test=100;
	static boolean iUP=true;
	static boolean	B_fill=true;
	
	
	// values 
	
	GuiIntValue v0;
	GuiIntValue v1;
	
	public Tool_Brush() {
		super();
		set_name("Pinselwerkzeug");
		cursor=Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
		try {
			icon_default=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/brush_a.png"));
			icon_selected=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/brush_b.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		v0 = new GuiIntValue(1,200,20);
		v1 = new GuiIntValue(1,100,10);
		
		color = new GuiColorValue();
		
		
		

		hud_panel.add(new PE_Slider("Grösse", v0,this));
		hud_panel.add(new PE_Slider("Kantenhärte", v1,this));
		hud_panel.add(new PE_Color_Choser("Farbe", color));
		
		sidepanel.add(new PE_Slider("Grösse", v0,this));
		sidepanel.add(new PE_Separator(12, Tile.VERTICAL,false));
		sidepanel.add(new PE_Slider("Kantenhärte", v1,this));
		sidepanel.add(new PE_Separator(12, Tile.VERTICAL,false));
		sidepanel.add(new PE_Color_Choser2("Farbe", color, this));
		
		set_menu(new Menuelement("menu",null));
		
		//set properties
		brush_stroke_points=new Polygon();
		brush_blur=50;
	}
	
	@Override
	public void activate() {
		super.activate();
	}
	
	@Override
	public void mouse_pressed(MouseEvent e){
		if(IMP.opened_image!=null && e.getModifiers()!=8){
			drawing=true;
			brush_stroke_points.addPoint(IMP.opened_image.mouse_x,IMP.opened_image.mouse_y);
			IMP.image_informant.inform_about_image_changed();
		}
	}
	
	@Override
	public void mouse_dragged(MouseEvent e){
		if(IMP.opened_image!=null && e.getModifiers()!=8){
			brush_stroke_points.addPoint(IMP.opened_image.mouse_x,IMP.opened_image.mouse_y);
			IMP.image_informant.inform_about_image_changed();
		}
	}
	
	@Override
	public void mouse_released(MouseEvent e){
		if(IMP.opened_image!=null){
			drawing=false;
			paint_with_polygon(IMP.opened_image.getImage().getGraphics());
			brush_stroke_points.reset();
		}
	}
	
	@Override
	public void mouse_moved(MouseEvent e) {
		IMP.image_informant.inform_about_image_changed();
	}
	
	@Override
	public void paint_on_screen(Graphics g, Image_Tile tile) {
		super.paint_on_screen(g, tile);
		if(drawing)
			paint_preview(g, tile);
	}
	
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
	//------------------------------------------------------------------------------------------------------------------------------
			//funcionality
			
//			Function parameters that the user can change in the dialogue. The calculated  values are set to make the filter work in a default way.

	public GuiColorValue color;
	public int brush_size(){
		return v0.getInt();
	}
	public static int brush_blur;
	private Polygon brush_stroke_points;
	private boolean drawing;
	
	public void paint_preview(Graphics g, Image_Tile tile){
		Graphics2D g2=(Graphics2D) g;
		
		Polygon brush_display=new Polygon();
		for(i=0;i<brush_stroke_points.npoints;i++){
			brush_display.addPoint((int) (tile.view.img_pos_x()+brush_stroke_points.xpoints[i]*tile.view.zoom),
					(int) (tile.view.img_pos_y()+brush_stroke_points.ypoints[i]*tile.view.zoom));
		}
		
//		if(IMP.opened_image.active_selections.size()>0)
//			g2.setClip(IMP.opened_image.active_selections.get(0).points);
		
	    BasicStroke stroke = new BasicStroke((float) (brush_size()*tile.view.zoom),
	                        BasicStroke.CAP_ROUND,
	                        BasicStroke.CAP_ROUND);
	    
		g2.setStroke(stroke);
		g2.setColor(color.get_color());
		g2.drawPolyline(brush_display.xpoints, brush_display.ypoints, brush_display.npoints);
		g2.setStroke(new BasicStroke());
	}
	
	public void paint_with_polygon(Graphics g){
		Graphics2D g2=(Graphics2D) g;
		
		if(IMP.opened_image.active_selections.size()>0)
			g2.setClip(IMP.opened_image.active_selections.get(0).points);
		
		Polygon stroke_polygon = new Polygon(brush_stroke_points.xpoints,
				brush_stroke_points.ypoints, brush_stroke_points.npoints);
		
	    BasicStroke stroke = new BasicStroke(brush_size(),
	                        BasicStroke.CAP_ROUND,
	                        BasicStroke.CAP_ROUND);
	    
		g2.setStroke(stroke);
		g2.setColor(color.get_color());
		g2.drawPolyline(stroke_polygon.xpoints, stroke_polygon.ypoints, stroke_polygon.npoints);
		g2.setStroke(new BasicStroke());
	}
	
	public void paintPaint(Graphics g, int posX, int posY){
		
		brush_blur=v1.getInt();
		
		g.setColor(new Color(i,i,0,color.get_alpha()));
		
		//for the changing color effect
		if(iUP==true){
			if (i<250){
				i++;
			}
			else{
				iUP=false;
			}
		}
		else{
			if (i>50){
				i--;
			}
			else{
				iUP=true;
			}
		}
		
		
		//painting a filled or empty circle
		if(B_fill==true){
			
			//paint filled circle 
			//paint blurry outer circle
			int size=brush_size();
			g.setColor(new Color(i,i,0,color.get_alpha()/brush_blur*2));
			while(size>=brush_size()-brush_blur){
				g.fillOval(posX-size/2,
						posY-size/2,
						size, size);
				size=size-2;
			}
			//paint inner cirlce
			g.setColor(new Color(i,i,0,color.get_alpha()));
			g.fillOval(posX-size/2,
					posY-size/2,
					size, size);
			
		}
		
		
		
		//drawing the empty circle
		else{
			g.drawOval(posX-brush_size()/2,
					posY-brush_size()/2,
					brush_size(), brush_size());
		}
	}

	@Override
	public void receive_message(Message m) {
		// TODO Auto-generated method stub
		
	}

}
