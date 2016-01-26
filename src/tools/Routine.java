package tools;

import gui_system.Menulizable;
import gui_system.Message;

import java.awt.event.KeyEvent;
import java.util.concurrent.RecursiveAction;

import main.IMP;

/**
 * A routine extends the Tool class. Routines should be used as one-time applicable functions. 
 * They do not appear in the toolbar like tools.

 */
public class Routine 
extends Tool 
implements Appliable, Menulizable{
	
	public Routine() {
		super();
		get_menu().create("Anwenden","apply");
		get_menu().create("Abbrechen","abort");
	}
	
	@Override
	public void activate() {
		super.activate();
		IMP.routine_in_process=true;
	}

	
	public void apply() {
		IMP.image_informant.inform_about_preview_available(false);
		IMP.routine_in_process=false;
		IMP.menu.refresh_menu();
	}

	public void abort() {
		IMP.image_informant.inform_about_preview_available(false);
		IMP.routine_in_process=false;
		IMP.menu.refresh_menu();
	}
	
	public void paint_preview() {
	}
	
	@Override
	public void key_typed(KeyEvent event) {
		int key =  event.getKeyChar();	
	}
	
	@Override
	public void key_released(KeyEvent e) {
		int key = e.getKeyCode();

		if (key == KeyEvent.VK_ENTER){
			apply();
			IMP.previous_tool.activate();
		}
		else if (key == KeyEvent.VK_ESCAPE){
			abort();
			IMP.previous_tool.activate();
		}
	}
	
	@Override
	public void recieve_message(String message) {
		super.recieve_message(message);
		if(message.equalsIgnoreCase("paint_preview"))
			paint_preview();
	}

	protected class Worker extends RecursiveAction{

		@Override
		protected void compute() {
			paint_preview();
		}
	}

	@Override
	public void receive_message(Message message) {
		// TODO Auto-generated method stub
		
	}

}
