package gui_system;

public class GuiStringValue 
extends GuiValue{
	
	private String string;
	
	public GuiStringValue() {
		set_string("");
	}
	
	public GuiStringValue(String string) {
		set_string(string);
	}
	
	public String get_string(){
		return string;
	}

	public void set_string(String new_string){
		if(!locked)
			this.string=new_string;
	}
	
	public void add_prefix(String prefix){
		if(!locked)
			string = prefix + string;
	}
	
	public void add_suffix(String suffix){
		if(!locked)
			string = string + suffix;
	}
}
