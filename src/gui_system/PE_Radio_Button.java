package gui_system;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import gui.Design_Preferences;

public class PE_Radio_Button
extends Tile
implements Design_Preferences{
	
	//value storage
	private String[] values;
	private String chosen_value;
	
	//GUI
	String name;
	double drawing_factor;
	int slider_width=200;
	int close_button_pos_x;
	boolean button_pressed;
	LinkedList<PE_Radio_Check> radios = new LinkedList<PE_Radio_Check>();
	
	//functionality
	
	Color[] menuColors={new Color(20,20,20),new Color(60,60,60),new Color(100,100,100),new Color(140,140,140),new Color(180,180,180)};

	
	public PE_Radio_Button(String name, String[] values){		
		this.name=name;
		this.values=values;
		size.width=50;
		size.height=30;
		alignment = VERTICAL;
		//GUI
		close_button_pos_x=get_position().x+size.width-(size.width/12);
		children.add(new PE_Label(new GuiStringValue(name)));
		for(String s : values){
			PE_Radio_Check radio = new PE_Radio_Check(s);
			radio.add_listener(this);
			children.add(radio);
			radios.add(radio);
		}
		radios.getFirst().checked = true;
	}
	
	public String get_value(){
		return chosen_value;
		
	}
	
	@Override
	public void mouse_clicked(MouseEvent e) {
		for(PE_Radio_Check radio : radios){
			radio.checked = false;
		}
		super.mouse_clicked(e);
	}
	
	@Override
	public void mouse_pressed(MouseEvent e) {
		super.mouse_pressed(e);
		button_pressed=true;
	}
	
	@Override
	public void mouse_released_anywhere(MouseEvent e) {
		super.mouse_released_anywhere(e);
		button_pressed=false;
	}
	
	@Override
	public void receive_message(String message) {
		System.out.println("message received");
		chosen_value = message;
		super.receive_message(message);
	}
	
@Override
	public void paint(Graphics g){	
		//apply/abort-bar
		g.fillRect(get_position().x, get_position().y, size.width, size.height);
		g.setColor(menuColors[2]);
		g.drawRect(get_position().x, get_position().y, size.width, size.height);
		
		g.setColor(Color.white);
		Font f = new Font("Calibri", Font.BOLD, 11);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(name,get_position().x+size.width/2-(fm.stringWidth(name))/2,
				get_position().y+size.height- (fm.getAscent()+fm.getDescent()));
		super.paint(g);
	}
}