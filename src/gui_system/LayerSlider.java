package gui_system;

public class LayerSlider {

	int value;
	int maxValue=255;
	double factor;
	String name;
	boolean focused=false;
	
	int posY;
	
	public LayerSlider(String nameT, int positionY){
		factor=(maxValue)/100.0;
		name=nameT;
		posY=positionY;
	}
	
	public int realValue(){
		return (int) (factor*value);
		
	}

	public void setMaxValue(int i) {
		maxValue=i;
		factor=maxValue/100.0;
		
	}
}
