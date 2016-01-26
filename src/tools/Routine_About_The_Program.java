package tools;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;

import main.IMP;
import gui.Image_Tile;

public class Routine_About_The_Program 
extends Routine{

	public Routine_About_The_Program() {
		super();
		set_name("Über das Programm");
		category = "Info";
		
		get_menu().create("Anwenden","apply");
		get_menu().create("Abbrechen","abort");
		
	}

	@Override
	public void activate(){
		super.activate();
		IMP.image_informant.inform_about_preview_available(true);
		
		if(IMP.opened_image!=null)
			IMP.preview_image=IMP.opened_image.getImage();
		
		//-------
		works_during_other_routines = true;
		needs_open_image = false;
	}
	
	@Override
	public void mouse_dragged(MouseEvent e){
	}
	
	@Override
	public void paint_on_screen(Graphics g, Image_Tile tile) {
		if(IMP.opened_image!=null)
			g.setColor(new Color(0,0,0,140));
		else
			g.setColor(color_dark_background);
		g.fillRect(tile.get_position().x, tile.get_position().y, tile.getWidth(), tile.getHeight());
		g.setColor(Color.white);

		//title
		String string="IMP";
		g.setFont(new Font("Arial", Font.PLAIN, 36));
		FontMetrics fm = g.getFontMetrics();
		g.drawString(string,
				tile.get_position().x+tile.getWidth()/2-(fm.stringWidth(string)/2),
				tile.get_position().y+tile.getHeight()/2-50);
	
		//version number		
		string=program_version;
		g.setFont(new Font("Helvetica", Font.PLAIN, 24));
		fm = g.getFontMetrics();
		g.drawString(string,
				tile.get_position().x+tile.getWidth()/2-(fm.stringWidth(string)/2),
				tile.get_position().y+tile.getHeight()/2);
		
		//credits
		string="Dimitri Stanojevic";
		g.setFont(new Font("Helvetica", Font.PLAIN, 14));
		fm = g.getFontMetrics();
		g.drawString(string,
				tile.get_position().x+tile.getWidth()/2-(fm.stringWidth(string)/2),
				tile.get_position().y+tile.getHeight()/2+100);
		
		
		
		
		g.setFont(new Font("Helvetica", Font.PLAIN, 12));
	}
	
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
	//------------------------------------------------------------------------------------------------------------------------------
			//funcionality
			
//			Function parameters that the user can change in the dialogue. The calculated  values are set to make the filter work in a default way.

	@Override
	public void paint_preview() {

	}
	
	
	
}
