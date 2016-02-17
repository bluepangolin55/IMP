package gui_system;

import main.Menuelement;

public interface Menulizable {
	
	public String get_name();
	
	public String get_category();
	
	public char get_key_shortcut();
	
	public void activate();
	
	public boolean safe_to_activate();
}
