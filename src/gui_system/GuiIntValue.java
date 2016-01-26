package gui_system;

public class GuiIntValue extends GuiValue{
	
	private int int_value;
	private double double_value;
	private boolean boolean_value;
	
	public double min_value;
	public double max_value;
	public boolean locked;
	public float float_value(){
		return (float) double_value;
	}
	
	public GuiIntValue(double min_value, double max_value, double new_value){
		this.min_value=min_value;
		this.max_value=max_value;
		
		if(!locked || new_value>min_value || new_value<max_value){
			int_value=(int) new_value;
			double_value=new_value;
		}
	}
	
	public void reconfigure(double min_value, double max_value, double new_value){
		this.min_value=min_value;
		this.max_value=max_value;
		
		if(!locked || new_value>min_value || new_value<max_value){
			int_value=(int) new_value;
			double_value=new_value;
		}
	}
	
	public GuiIntValue(boolean new_value){
		boolean_value=new_value;
	}
	
	public void set_value(double new_value){
		if(!locked || new_value>min_value || new_value<max_value){
			if(new_value < min_value){
				int_value=(int) (min_value);
				double_value=min_value;
			}
			else if(new_value > max_value){
				int_value=(int) (max_value);
				double_value=max_value;
			}
			else{
				int_value=(int) new_value;
				double_value=new_value;
			}
		}
	}
	
	public int getInt(){
		return int_value;
	}
	
	public double getDouble(){
		return double_value;
	}
	
	public boolean getBoolean(){
		return boolean_value;
	}
	
	public void setValue(boolean new_value){
		if(!locked)
			boolean_value=new_value;
	}
	
	public void lock(){
		locked=true;
	}
	
	public void unlock(){
		locked=false;
	}
}
