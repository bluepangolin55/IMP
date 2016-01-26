package file;

import java.io.*;

public class Output 
extends Input{

	public static void save_file(File file){
	    try {
			save_sifp(file);
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
	
    private static void save_sif(File file) throws Exception, IOException,
	UnsupportedEncodingException {
	FileOutputStream outputstream = new FileOutputStream(file);
	
	int b=Input.image_data[0][0][0];
	int h=Input.image_data[0][0][1];
	int l=b*h*3+8;	//very important!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!repair!!
	byte[] data = new byte[l];
	int i=8;
	for(int y=1;y<h+1;y++){
		for(int x=1;x<b+1;x++){	
			data[i]=(byte) image_data[x][y][0];
			i++;
			data[i]=(byte) image_data[x][y][1];
			i++;
			data[i]=(byte) image_data[x][y][2];
			i++;
		}
	}
	//Breite und Höhe speichern
	if (b%65536!=b){
		data[2]= (byte) ((b-b%65536)/65536);}
	if (b%65536%256!=b){
		data[3]= (byte) ((b%65536-b%65536%256)/256);}
	data[4]= (byte) (b%65536%256);
		
	if (h%65536!=h){
		data[5]= (byte) ((h-h%65536)/65536);}
	if (h%65536%256!=h){
		data[6]= (byte) ((h%65536-h%65536%256)/256);}
	data[7]= (byte) (h%65536%256);
	//---------------------------------
	outputstream.write(data);
	outputstream.close();
	System.out.println("image saved");
    }
    
    private static void save_sifp(File file) throws Exception, IOException,
	UnsupportedEncodingException {
	FileOutputStream outputstream = new FileOutputStream(file);
	
	int b=Input.image_data[0][0][0];
	int h=Input.image_data[0][0][1];
	int l=b*h*3+8;	//very important!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!repair!!
	int imax=extrude_colors();
	int i=8;
	byte[] data = new byte[imax];
		while(i<imax){
			data[i]=(byte) compressed_image_data[i];
			i++;
	}
	//Breite und Höhe speichern
	if (b%65536!=b){
		data[2]= (byte) ((b-b%65536)/65536);}
	if (b%65536%256!=b){
		data[3]= (byte) ((b%65536-b%65536%256)/256);}
	data[4]= (byte) (b%65536%256);
		
	if (h%65536!=h){
		data[5]= (byte) ((h-h%65536)/65536);}
	if (h%65536%256!=h){
		data[6]= (byte) ((h%65536-h%65536%256)/256);}
	data[7]= (byte) (h%65536%256);
	//---------------------------------
	outputstream.write(data);
	outputstream.close();
	System.out.println("image saved");
    }
    
//    Methoden für Komprimierverfahren
    
    static int extrude_colors(){						//builds compressed_image by making placing all colors at the beginning of the array and making a reference for each pixel
    	int[] color_locations = new int[1000000];
    	
    	int b=Input.image_data[0][0][0];
    	int h=Input.image_data[0][0][1];
    	int i=8;
    	int j=0;
    	for(int y=1;y<h;y++){
    		for(int x=1;x<b;x++){	
    			int pos=search_int(image_data[x][y][0],image_data[x][y][1],image_data[x][y][2], compressed_image_data);
    			if(pos==0){
//    				System.out.println("writing color:" + image_data[x][y][0] + "at position" + i);
        			color_locations[j]=i;
        			j++;
        			compressed_image_data[i]=image_data[x][y][0];
        			i++;
        			compressed_image_data[i]=image_data[x][y][1];
        			i++;
        			compressed_image_data[i]=image_data[x][y][2];
    				i++;
    			}
    			else{color_locations[j]=pos;
    				j++;
    			}

    		}
    	}
    	int loc=i;		//where the pixel locations are written
    	compressed_image_data[7]=loc;
    	int jmax = j;
    	System.out.println("jmax = " + jmax);
  		for(j=0;j<jmax;j++){	
			compressed_image_data[i]=color_locations[j];
			i++;
		}
		return i-1;
    }
    
    boolean search_int(int value, int[] array){
    	boolean found=false;
    	for (int i=8;i<array.length;i++){
    		if (array[i]==value){
    			found=true;
    			break;
    		}
    	}
    	return found;
    }
    
    static int search_int(int v1,int v2,int v3, int[] array){
    	int found=0;
    	for (int i=8;i<array.length;i=i+3){
    		if (array[i]==v1){
    			if (array[i+1]==v2){
    				if (array[i+2]==v3){
            			found=i;
            			break;
            		}
        		}
    		}
    	}
    	if(found==11)		System.out.println(v1);
    	return found;
    }
    		
    		
    		
}
