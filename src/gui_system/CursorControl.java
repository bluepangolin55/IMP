package gui_system;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import main.IMP;

public class CursorControl {

	 static Cursor brushCursor;
	
	 
	public CursorControl(){
	    // Transparent 16 x 16 pixel selecting tool cursor.
	    BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	    Graphics g=cursorImg.getGraphics();
	    g.setColor(Color.black);
	    g.drawRect(7,0,1,16);
	    g.drawRect(0,7,16,1);
	    
	    // Create a new blank cursor.
	    brushCursor = Toolkit.getDefaultToolkit().createCustomCursor(
	        cursorImg, new Point(0, 0), "selecting tool");
	}
	
	public static void updateCustomCursor(int i, BufferedImage cursorImg){
		switch(i){
		case 0:
		    break;
		case 3:
		    brushCursor = Toolkit.getDefaultToolkit().createCustomCursor(
			        cursorImg, new Point(0, 0), "selecting tool");
//		    IMP.main_window.setCursor(brushCursor);
		    break;
		}
	}
	
	public static void changeCursor(int i){
		switch(i){
		case 0:
		    try {
		        IMP.main_window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		    	} finally {
		    	}
		    break;
		case 1:
		    try {
		    	IMP.main_window.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		    	} finally {
		    	}
		    break;
		case 2:
		    try {
		    	IMP.main_window.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		    	} finally {
		    	}
	    break;
		case 3:
		    try {
//		        IMP.main_window.setCursor(brushCursor);
		    	IMP.main_window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		    	} finally {
		    	}
		    break;
		case 4:
		    try {
		    	IMP.main_window.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		    	} finally {
		    	}
		    break;
		case 5:
		    try {
		        IMP.main_window.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		    	} finally {
		    	}
		    break;
		}
	}
	
	public static void changeCursor(Cursor cursor){
		    try {
		       	IMP.main_window.setCursor(cursor);
		    	} finally {
		    	}
		    
	}
	
	public static void changeCursor(String s){

		
	    
		switch(s){
		case "default":
		    try {
		    	IMP.main_window.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		    	} finally {
		    	}
		    break;
		case "crosshair":
		    try {
		    	IMP.main_window.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		    	} finally {
		    	}
		    break;
		case "hand":
		    try {
		    	IMP.main_window.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		    	} finally {
		    	}
		    break;
		case "text":
		    try {
		    	IMP.main_window.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
		    	} finally {
		    	}
		    break;
		case "move":
		    try {
		    	IMP.main_window.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		    	} finally {
		    	}
		    break;
		}
	}
}
