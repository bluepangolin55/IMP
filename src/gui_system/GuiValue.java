package gui_system;

public abstract class GuiValue{
	
	public boolean locked;
	
	public void lock(){
		locked=true;
	}
	
	public void unlock(){
		locked=false;
	}

}
