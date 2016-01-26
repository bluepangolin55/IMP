package tools;

import gui_system.Menulizable;
import main.IMP;

public abstract class Command 
implements Appliable, Menulizable{
	
	public String name;
	public String action;
	public String category;
	public char key_shortcut;
	public boolean needs_open_image;
	public boolean works_during_other_routines;
	
	public Command(String name, String action) {
		this.name = name;
		this.action = action;
	}
	
	@Override
	public void apply(){
		IMP.action_informant.broadcast_message(action);
        IMP.apply_action(action);	
	}
	
	@Override
	public void activate() {
		apply();
	}
	
	@Override
	public void abort() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public boolean safe_to_activate(){
		return !(needs_open_image && IMP.opened_image == null) 
				&& !(IMP.routine_in_process && !works_during_other_routines);
	}
	
	@Override
	public String get_name() {
		return name;
	}
	
	@Override
	public String get_action() {
		return action;
	}
	
	@Override
	public String get_category() {
		return category;
	}
	
	@Override
	public char get_key_shortcut() {
		return key_shortcut;
	}
	
}
