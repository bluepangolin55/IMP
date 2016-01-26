package main;

import java.util.List;

import file.Img;

public interface Data_Access {
	
	static List<Img> loaded_images=null;
	public Img opened_image=null;

	public int i=0;
}
