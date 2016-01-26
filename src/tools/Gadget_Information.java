package tools;

import java.io.IOException;

import javax.imageio.ImageIO;

import gui_system.PE_Mouse_position_Label;

public class Gadget_Information 
extends Gadget{
	
	public Gadget_Information() {
		name="Informationen";
		sidepanel.add(new PE_Mouse_position_Label());
		
		try {
			icon_default=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/info_a.png"));
			icon_selected=ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("images/icons/info_b.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
