package main;

import java.util.ArrayList;
import java.util.List;

import tools.Tool;

public class Tool_Broadcaster {
	
	public static List<Tool> clients;
	
	public Tool_Broadcaster() {
		clients=new ArrayList<Tool>();
	}
	
	
	public void broadcast_message(String message){
		for(int i=0;i<clients.size();i++){
			clients.get(i).take_action(message);
		}
		IMP.menu.refresh_menu();
	}
	
	public void add_client(Tool new_client){
		clients.add(new_client);
	}
	
	public void remove_client(Tool client){
		clients.remove(client);
	}

}
