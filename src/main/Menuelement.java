package main;

import gui_system.Menulizable;

import java.util.ArrayList;
import java.util.LinkedList;

import tools.Command;
import tools.Routine;
import tools.Tool;


public class Menuelement{
	
	private boolean is_folder;
	private boolean focusable;
	private boolean focused;
	private String name;
	private String action;
//	private Tool routine_reference;
//	private T content;
	public Menulizable content;
	
	public ArrayList<Menuelement> elements;

	
	public Menuelement(String nameT, String actionT){
		name=nameT;
		action=actionT;
		elements=new ArrayList<Menuelement>() ;
		setFolder(false);
		focused=false;
		set_focusable(true);
		is_folder=false;
	}
	
	public Menuelement(Menulizable command){
		content = command;
		name=command.get_name();
		action="use_tool";
		elements=new ArrayList<Menuelement>() ;
		setFolder(false);
		focused=false;
		set_focusable(true);
		is_folder=false;
	}

//	public Menuelement(Tool routine){
//		content = routine;
//		name=routine.name;
//		action="use_tool";
//		elements=new ArrayList<Menuelement>() ;
//		setFolder(false);
//		focused=false;
//		set_focusable(true);
//		is_folder=false;
//	}
	
	public Menuelement(String nameT, String actionT, boolean focusable){
		name=nameT;
		action=actionT;
		elements=new ArrayList<Menuelement>() ;
		setFolder(false);
		focused=false;
		set_focusable(focusable);
		is_folder=false;
	}
	
	public void add(Menuelement element){
		elements.add(element);
		is_folder=true;
	}

	public void add(Menuelement element, LinkedList<String> categories){
		if(categories.isEmpty()){
			elements.add(element);
			System.out.println("made it !");
			return;
		}
		String c = categories.pop();
		Menuelement me = get_menu_by_name(c);
		if(me == null){
			me = new Menuelement(c, "nothing");
			me.is_folder = true;
			this.add(me);
		}
        me.add(element, categories);
	}
	
	public void create(String nameT, String actionT){
		elements.add(new Menuelement(nameT,actionT));
	}
	
	public String getName(){
		return name;
	}
	
	public String get_action(){
		return action;
	}
	
	public Menuelement get_menu_by_name(String name){
		Menuelement result = null;
		for(Menuelement me : elements){
			if(me.name.equals(name)){
				result = me;
				break;
			}
		}
		return result; 
	}

	public boolean is_focused() {
		return focused;
	}

	public void setFocused(boolean b) {
		if(focusable)
			focused=b;
	}

	public void unfocus_all() {
		for(int i=0;i<elements.size();i++){
			elements.get(i).setFocused(false);
		}
	}

	public boolean is_folder() {
		return is_folder;
	}

	public void setFolder(boolean isFolder) {
		this.is_folder = isFolder;
	}

	public boolean is_focusable() {
		return focusable;
	}

	public void set_focusable(boolean isFocusable) {
		this.focusable = isFocusable;
	}

	public int focused_element(){
		int focusedElement=0;
			for(int i=0;i<elements.size();i++){
				if(this.elements.get(i).is_focused()){
					focusedElement=i;
					break;
				}
			}
		return focusedElement;
	}
	
	public void focus(){
		focused=true;
	}
	
	public void unfocuse() {
		focused=false;
	}
	
	public Menuelement get_focused_element(){
		return this.elements.get((this.focused_element()));
	}

	public Menuelement get(int i) {
		return elements.get(i);
	}

//	public Tool get_routine() {
//		return routine_reference;
//	}
	
//	-	-	-	-	-	-	-	-	-	-	-	-	-	apply action
	public void apply_action(){
		content.activate();
	}
}
