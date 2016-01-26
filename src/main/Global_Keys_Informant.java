package main;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import gui_system.Tile;


public class Global_Keys_Informant {
	
	public static List<Tile> clients;
	
	public Global_Keys_Informant() {
		clients=new ArrayList<Tile>();
	}
	
	
	public void inform_about_key_pressed(KeyEvent e){
		for(int i=0;i<clients.size();i++){
			clients.get(i).key_pressed(e);
		}
		IMP.key_pressed(e);
	}
	
	public void inform_about_key_released(KeyEvent e) {
		for(int i=0;i<clients.size();i++){
			clients.get(i).key_released(e);
		}
	}
	
	public void inform_about_key_typed(KeyEvent e) {
		for(int i=0;i<clients.size();i++){
			clients.get(i).key_typed(e);
		}
	}
	
	public void add_client(Tile new_client){
		clients.add(new_client);
	}
	
	public void remove_client(Tile client){
		clients.remove(client);
	}



}
