package gui_system;

public class GuiBooleanValue extends GuiValue{
	
	private boolean boolean_value;
	
	public GuiBooleanValue(boolean value) {
		this.boolean_value = value;
	}
	
	public boolean getValue() {
		return boolean_value;
	}
	
	public void setValue(boolean value){
		this.boolean_value = value;
	}


}
