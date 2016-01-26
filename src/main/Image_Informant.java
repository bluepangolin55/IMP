package main;

import java.util.ArrayList;
import java.util.List;

import file.Img;
import gui_system.Tile;


public class Image_Informant {
	
	public static List<Tile> clients;
	
	public Image_Informant() {
		clients=new ArrayList<Tile>();
	}
	
	
	public void inform_about_new_image(Img new_image){
		for(int i=0;i<clients.size();i++){
			clients.get(i).new_image_loaded(new_image);
		}
		IMP.menu.refresh_menu();
	}
	
	public void inform_about_image_changed(){
		for(int i=0;i<clients.size();i++){
			clients.get(i).image_changed();
		}
	}
	
	public void inform_about_selection_changed(){
		for(int i=0;i<clients.size();i++){
			clients.get(i).selection_changed();
		}
	}
	
	public void inform_about_preview_changed(){
		for(int i=0;i<clients.size();i++){
			clients.get(i).preview_changed();
		}
	}
	
	public void inform_about_preview_available(boolean preview_available){
		for(int i=0;i<clients.size();i++){
			clients.get(i).set_preview_available(preview_available);
		}
	}
	
	public void add_client(Tile new_client){
		clients.add(new_client);
	}
	
	public void remove_client(Tile client){
		clients.remove(client);
	}

}
