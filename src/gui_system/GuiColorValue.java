package gui_system;

import java.awt.Color;

public class GuiColorValue {
	
	private int red;
	private int green;
	private int blue;
	private int alpha;
	
	public GuiColorValue() {
		set_red(0);
		set_green(0);
		set_blue(0);
		set_alpha(255);
	}
	
	public GuiColorValue(Color color) {
		set_red(color.getRed());
		set_green(color.getGreen());
		set_blue(color.getBlue());
		set_alpha(color.getAlpha());
	}

	public GuiColorValue(int red, int green, int blue) {
		set_red(red);
		set_green(green);
		set_blue(blue);
		set_alpha(255);
	}
	
	public GuiColorValue(int red, int green, int blue, int alpha) {
		set_red(red);
		set_green(green);
		set_blue(blue);
		set_alpha(alpha);
	}
	
	public void set_color(Color color) {
		set_red(color.getRed());
		set_green(color.getGreen());
		set_blue(color.getBlue());
		set_alpha(color.getAlpha());
	}
	
	public void set_color(int red, int green, int blue) {
		set_red(red);
		set_green(green);
		set_blue(blue);
	}
	
	public void set_color(int red, int green, int blue, int alpha) {
		set_red(red);
		set_green(green);
		set_blue(blue);
		set_alpha(alpha);
	}
	
	public Color get_color(){
		return new Color(red,green,blue,alpha);
	}

	public int get_red() {
		return red;
	}


	public void set_red(int red) {
		this.red = red;
	}


	public int get_green() {
		return green;
	}


	public void set_green(int green) {
		this.green = green;
	}


	public int get_blue() {
		return blue;
	}


	public void set_blue(int blue) {
		this.blue = blue;
	}


	public int get_alpha() {
		return alpha;
	}


	public void set_alpha(int alpha) {
		this.alpha = alpha;
	}
	

}
