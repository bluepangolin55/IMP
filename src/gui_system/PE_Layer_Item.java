package gui_system;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import main.IMP;

import file.Layer;

public class PE_Layer_Item 
extends Tile{
	
	Layer layer;
	boolean pressed;
	
	//for dragging
	boolean grabbed;
	int grabbing_position;
	
	//ui elements
	int visibility_box_size=20;
	
	public PE_Layer_Item(Layer layer) {
		this.layer=layer;
		set_height(40);
		height_is_fixed=true;
	}
	
	@Override
	public void mouse_clicked(MouseEvent e) {
		Rectangle visibility_box = new Rectangle(get_position().x+5, get_position().y+(getHeight()-visibility_box_size)/2, visibility_box_size, visibility_box_size);
		if(visibility_box.contains(e.getPoint())){
			layer.set_visible(!layer.isVisible());
		}
		else
			layer.select();
	}
	
	@Override
	public void mouse_pressed(MouseEvent e) {
		pressed=true;
		grabbed=true;
		grabbing_position=e.getPoint().y-get_position().y;
	}
	
	@Override
	public void mouse_dragged(MouseEvent e) {
		pressed=true;
//		if(grabbed)
//			set_position(new Point(get_position().x, e.getY()-grabbing_position));
	}
	
	@Override
	public void mouse_released_anywhere(MouseEvent e) {
		pressed=false;
		grabbed=false;
	}
	
	@Override
	public void paint(Graphics g) {
		if(layer.is_selected()){
			fill_background(g, color_contrast);
		}
		if(pressed && is_hovered(IMP.main_panel.mouse_position)){
			fill_background(g, color_second_contrast);
		}
		
		if(layer.isVisible())
			g.setColor(Color.blue);
		else
			g.setColor(Color.gray);
		g.fillRoundRect(get_position().x+5, get_position().y+(getHeight()-visibility_box_size)/2, visibility_box_size, visibility_box_size,10,10);
		g.setColor(color_borders);
		g.drawRoundRect(get_position().x+5, get_position().y+(getHeight()-visibility_box_size)/2, visibility_box_size, visibility_box_size,10,10);
		
		g.setColor(Color.white);
		Font f = new Font("Calibri", Font.BOLD, 11);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(layer.getName(),get_position().x+size.width/2-(fm.stringWidth(layer.getName()))/2,
				get_position().y+size.height- (fm.getAscent()+fm.getDescent()));
	}
}
