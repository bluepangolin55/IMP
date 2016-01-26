package tools;

import gui_system.Menulizable;
import gui_system.Message;
import main.IMP;

public class Gadget 
extends Tool
implements Menulizable{

	@Override
	public void activate() {
		IMP.gadget_sidepanel.remove_all_children();
		IMP.gadget_sidepanel.add(sidepanel);
	}

	@Override
	public void receive_message(Message m) {
		// TODO Auto-generated method stub
		
	}

}
