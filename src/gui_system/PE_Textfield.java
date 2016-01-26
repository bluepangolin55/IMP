package gui_system;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import gui.Design_Preferences;

public class PE_Textfield
extends Tile
implements Design_Preferences{
	
	//value storage
	GuiIntValue value;
	boolean changed;

	//GUI
	GuiStringValue text;
	boolean selected;
	int cursor_position;
	
	//functionality
	
	public PE_Textfield(GuiStringValue text){		
		this.text=text;
		size=new Dimension(50,30);
		height_is_fixed=true;
		cursor_position=text.get_string().length();
	}
	
	@Override
	public void mouse_clicked(MouseEvent e) {
		selected=true;
	}
	
	@Override
	public void mouse_pressed(MouseEvent e) {
	}
	
	@Override
	public void mouse_released_anywhere(MouseEvent e) {
		super.mouse_released_anywhere(e);
		selected=false;
		changed=true;
	}
	
	@Override
	public void key_pressed(KeyEvent e) {
		if(selected){
			if(e.getKeyCode()==KeyEvent.VK_BACK_SPACE){
				if(!text.get_string().isEmpty())
					if(e.isControlDown())
						text.set_string("");
					else{
						text.set_string(text.get_string().substring(0, cursor_position-1) + text.get_string().substring(cursor_position, text.get_string().length()));
						cursor_position--;
					}
			}
			else if(e.getKeyCode()==KeyEvent.VK_ENTER){
				text.set_string(text.get_string().trim());
				changed=true;
				selected=false;
			}
			else if(e.getKeyCode()==KeyEvent.VK_LEFT){
				if(cursor_position>0)
					cursor_position--;
			}
			else if(e.getKeyCode()==KeyEvent.VK_RIGHT){
				if(cursor_position<text.get_string().length())
					cursor_position++;
			}
		}
	}
	
	@Override
	public void key_typed(KeyEvent e) {
		if(selected){
			if(valid_character(e)){
				text.set_string(text.get_string().substring(0, cursor_position)+ e.getKeyChar() + text.get_string().substring(cursor_position, text.get_string().length()));
				cursor_position++;
			}
		}
	}

		private boolean valid_character(KeyEvent e){
			if("abcdefghijklmnopqrstuvwxyz1234567890 ".contains("" + e.getKeyChar()))
				return true;
			else if("ABCDEFGHIJKLMNOPQRSTUVWXYZ".contains("" + e.getKeyChar()))
				return true;
			else if("öäüéàèêâô+*ç%&/()=:;,.|@#¼½½¬|¢]}?".contains("" + e.getKeyChar()))
				return true;
			else
				return false;
		}
	
	@Override
	public void paint(Graphics g){
		if(selected)
			g.setColor(color_bright_background);
		else
			g.setColor(color_dark_background);
		
		g.fillRect(get_position().x, get_position().y, size.width, size.height);
		
		if(selected)
			g.setColor(color_text_dark);
		else
			g.setColor(color_text_bright);

		Font f = new Font("Calibri", Font.BOLD, 11);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		g.drawString(text.get_string(),get_position().x+
				size.width/2-(fm.stringWidth(text.get_string()))/2,
				get_position().y+size.height- (fm.getAscent()+fm.getDescent()));
		if(selected){
			g.setColor(color_contrast);
			int cursor=get_position().x+size.width/2-(fm.stringWidth(text.get_string()))/2 + fm.stringWidth(text.get_string().substring(0, cursor_position));
			g.drawLine(cursor, get_position().y+size.height/5, cursor, size.height/3*2);
			
		}
	}
	
	public void set_text(String new_text){
		text.set_string(new_text);
	}
	
	public void set_text(GuiStringValue new_text){
		text=new_text;
	}
}
