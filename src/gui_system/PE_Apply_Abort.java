package gui_system;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import tools.Routine;

import gui.Design_Preferences;

public class PE_Apply_Abort
extends Tile
implements Design_Preferences{

	//GUI
	String name;
	double drawing_factor;
	int slider_width=200;
	int close_button_pos_x;
	boolean apply_button_pressed;
	boolean abort_button_pressed;
	
	//functionality
	
	Color[] menuColors={new Color(20,20,20),new Color(60,60,60),new Color(100,100,100),new Color(140,140,140),new Color(180,180,180)};
	private Routine routine;

	public PE_Apply_Abort(Routine routine){		
		this.routine=routine;
		size.width=360;
		size.height=20;
		height_is_fixed=true;
		
		//GUI
		close_button_pos_x=get_position().x+size.width-(size.width/12);
	}
	
	@Override
	public void mouse_clicked(MouseEvent e) {
//		panel=(Routine_Panel) super.panel;
		if(e.getX()>get_position().x && e.getX()<get_position().x+size.width/2)
			routine.apply();
		else
			routine.abort();
	}
	
	@Override
	public void mouse_pressed(MouseEvent e) {
		if(e.getX()>get_position().x && e.getX()<get_position().x+size.width/2)
			apply_button_pressed=true;
		else
			abort_button_pressed=true;
	}
	
	@Override
	public void mouse_released_anywhere(MouseEvent e) {
		super.mouse_released_anywhere(e);
		apply_button_pressed=false;
		abort_button_pressed=false;
	}
	
@Override
	public void paint(Graphics g){
		//apply/abort-bar
		if(apply_button_pressed)	//left
			g.setColor(color_apply);
		else
			g.setColor(menuColors[2]);
		g.fillRect(get_position().x, get_position().y, size.width/2, size.height);
		
		if(abort_button_pressed)	//right
			g.setColor(color_abort);
		else
			g.setColor(menuColors[1]);
		g.fillRect(get_position().x+size.width/2, get_position().y, size.width/2, size.height);
		
		g.setColor(Color.white);
		Font f = new Font("Calibri", Font.BOLD, 11);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		g.drawString("Anwenden",get_position().x+size.width/4-(fm.stringWidth("Anwenden")/2),
				get_position().y+size.height- (fm.getAscent()+fm.getDescent())/2);
		g.drawString("Abbrechen",get_position().x+size.width/2+size.width/4-(fm.stringWidth("Abbrechen")/2),
				get_position().y+size.height- (fm.getAscent()+fm.getDescent())/2);
	}
}
