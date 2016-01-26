package file;

import java.awt.image.BufferedImage;
import java.io.*;

import javax.imageio.ImageIO;

import main.IMP;

public class Input {
	
	
	
	public static int[][][] image_data = new int[1800][1800][3];
	public static int[] compressed_image_data = new int[1000000];

	
	public static void scan(){
		int y=1;
		image_data[0][0][0]=999;	//Breite
		image_data[0][0][1]=999;	//Höhe
		image_data[0][0][2]=81000;	//Anzahl
		while (y<60){
			for(int x=1;x<130;x++){
			
				image_data[x][y][0]=1*x+1*y;
				image_data[x][y][1]=1*x+1*y;
				image_data[x][y][2]=1*x+50+1*y;
			}
			y++;
		}
	}
	

	public static void scan_document(File file){
		try {
			BufferedImage image=ImageIO.read(file);
			Img loaded_image=new Img(image);
			loaded_image.set_filename(file);
//			loaded_image.deleteLayer(loaded_image.get_selected_layer());
//			loaded_image.createLayer(image);
			IMP.opened_image=loaded_image;
			IMP.image_informant.inform_about_new_image(loaded_image);
    		IMP.menu.refresh_menu();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}
    
    
}
