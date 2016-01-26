package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

public interface Design_Preferences {
	//	-	-	-	-	-	-	colors	-	-	-	-	-	-	-	-	-	-	-	
	Color color_dark_background=new Color(40,40,50);
	Color color_menu_background=new Color(20,20,30);
	Color color_image_background=new Color(20,20,30);
	Color color_borders=new Color(150,150,160);
	Color color_contrast=new Color(100,120,180);
//	Color color_contrast=new Color(160,80,80);
	Color color_contrast_opaque=new Color(100,120,180,200);
	Color color_second_contrast=new Color(180,120,100);
	Color color_apply=new Color(140,200,140);
	Color color_abort=new Color(200,100,100);
	Color color_text_title=Color.white;
	Color color_text_bright=new Color(255,245,235);
	Color color_text_grey=new Color(200,200,200);
	Color color_text_dark=new Color(40,40,40);
	Color color_bright_opaque_layer=new Color(255,255,255,30);
	Color color_dark_opaque_layer=new Color(0,0,0,30);
	Color color_dark_opaque_layer2=new Color(0,0,0,180);
	Color color_bright_background=new Color(180,180,180);
	
	Color[] menuColors={new Color(20,20,20),new Color(60,60,60),new Color(100,100,100),new Color(140,140,140),new Color(180,180,180)};
//	Color color_dark_background=new Color(180,180,180);
//	Color color_menu_background=new Color(20,20,30);
//	Color color_image_background=new Color(80,80,80);
//	Color color_borders=new Color(150,150,160);
//	Color color_contrast=new Color(120,160,80);
//	Color color_contrast_opaque=new Color(100,120,180,200);
//	Color color_second_contrast=new Color(180,120,100);
//	Color color_apply=new Color(140,200,140);
//	Color color_abort=new Color(200,100,100);
//	Color color_text_title=Color.black;
//	Color color_text_bright=new Color(40,40,40);
//	Color color_text_grey=new Color(60,60,60);
//	Color color_text_dark=new Color(255,245,235);
//	Color color_bright_opaque_layer=new Color(0,0,0,30);
//	Color color_dark_opaque_layer=new Color(255,255,255,30);
//	Color color_dark_opaque_layer2=new Color(255,255,255,180);
//	Color color_bright_background=new Color(40,40,40);
	
	//	-	-	-	-	-	-	sizes	-	-	-	-	-	-	-	-	-	-	-		
	Dimension sidepanel_size = new Dimension(280,400);
	int button_height= 32;
	Font button_font = new Font("Calibri", Font.BOLD, 10);
	
	
	//keyboard and mouse input
	String[] global_keys=new String[]{"1","2","3","4","5"};
	
	
	
	//program information
	String program_version="version 0.40";
}
