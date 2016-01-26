package main;

import gui_system.Menulizable;

import java.util.LinkedList;

public class Menu {

    public Menuelement main_menu;	//file menu
    private LinkedList<Menuelement> menuelements;
    /*
    private Menuelement menu_file;
    private Menuelement save_file;
    private Menuelement export_file;
    private Menuelement export_as_bmp;
    private Menuelement export_as_gif;
    private Menuelement export_as_jpeg;
    private Menuelement export_as_png;
    
    public Menuelement menu_help;	//help menu
    private Menuelement help_about;
    
    */

    public Menuelement menu_view;		//view menu
    private Menuelement view_zoom_in;
    private Menuelement view_zoom_out;
    private Menuelement view_real_size;
    private Menuelement view_stretched;
    private Menuelement show_sidepanel;
    
    
    
    //layer menu
    public static Menuelement menu_layer;		//view menu
    private static Menuelement layer_new;
    private static Menuelement layer_delete;
    private static Menuelement layer_duplicate;
    
        //layer menu
    static{
        menu_layer = new Menuelement("Ebenen","nothing");
        layer_new = new Menuelement("Neue Ebene","layer_new",false);
        menu_layer.add(layer_new);
        layer_delete = new Menuelement("Ebene löschen","layer_delete",false);
        menu_layer.add(layer_delete);
        layer_duplicate = new Menuelement("Ebene duplizieren","layer_duplicate",false);
        menu_layer.add(layer_duplicate);
    }
    public Menu() {
    	menuelements = new LinkedList<Menuelement>();
        //initialize the right click menu
        main_menu = new Menuelement("menu","nothing");
        //menu_file = new Menuelement("Datei","nothing");
        for(Menulizable c : IMP.commands){
        	Menuelement me = new Menuelement(c);
        	menuelements.add(me);
        	if(c.get_category() != null){
                String [] cat = c.get_category().split("/");
                LinkedList<String> cat_list = new LinkedList<String>();
                for(String s : cat){
                	cat_list.add(s);
                }
        		main_menu.add(me, cat_list);
        	}
        	else{
        		main_menu.add(me);
        	}
        }

        /*
        save_file = new Menuelement("Bild speichern","save_file",true);
        menu_file.add(save_file);
        export_file = new Menuelement("Exportieren als","nothing",true);
        menu_file.add(export_file);
        export_as_bmp = new Menuelement("Bmp","export_as_bmp",true);
        export_file.add(export_as_bmp);
        export_as_gif = new Menuelement("Gif","export_as_gif",true);
        export_file.add(export_as_gif);
        export_as_jpeg = new Menuelement("Jpeg","export_as_jpeg",true);
        export_file.add(export_as_jpeg);
        export_as_png = new Menuelement("Png","export_as_png",true);
        export_file.add(export_as_png);
        main_menu.add(menu_file);
        
        menu_view = new Menuelement("Ansicht","nothing");
        view_zoom_in = new Menuelement("Hineinzoomen","view_zoom_in",false);
        menu_view.add(view_zoom_in);
        view_zoom_out = new Menuelement("Herauszoomen","view_zoom_out",false);
        menu_view.add(view_zoom_out);
        view_real_size = new Menuelement("Tatsächliche Pixel","view_real_size",false);
        menu_view.add(view_real_size);
        view_stretched = new Menuelement("Bild strecken","view_stretched",false);
        menu_view.add(view_stretched);
        show_sidepanel = new Menuelement("Zeige Werkzeugleiste","show_sidepanel");
        menu_view.add(show_sidepanel);
        main_menu.add(menu_view);
        
        menu_help = new Menuelement("Hilfe","nothing");
        help_about = new Menuelement(new Routine_About_The_Program());
        menu_help.add(help_about);
        menu_help.add(new Menuelement("Hilfe","nothing",false));
        main_menu.add(menu_help);
        
        
        */
        refresh_menu();
	}
    
	public void refresh_menu(){
		for(Menuelement me : menuelements){
			me.set_focusable(me.content.safe_to_activate());
		}
		if(IMP.opened_image!=null){
			if(IMP.routine_in_process){//is a routine selected
				
//				view_zoom_in.set_focusable((true));
//				view_zoom_out.set_focusable((true));
//				view_real_size.set_focusable((true));
//				view_stretched.set_focusable((true));
//				show_sidepanel.set_focusable((false));
//				
				
//				save_file.set_focusable(false);
//				export_file.set_focusable(false);
				
				layer_new.set_focusable(false);
				layer_delete.set_focusable(false);
				layer_duplicate.set_focusable(false);
			}
			else{

//				view_zoom_in.set_focusable((true));
//				view_zoom_out.set_focusable((true));
//				view_real_size.set_focusable((true));
//				view_stretched.set_focusable((true));
//				show_sidepanel.set_focusable((true));
				
//				save_file.set_focusable(true);
//				close_file.set_focusable(true);
//				export_file.set_focusable(true);
				
				layer_new.set_focusable(true);
				layer_delete.set_focusable(true);
				layer_duplicate.set_focusable(true);
			}
		}
		else{
//			view_zoom_in.set_focusable((false));
//			view_zoom_out.set_focusable((false));
//			view_real_size.set_focusable((false));
//			view_stretched.set_focusable((false));
//			show_sidepanel.set_focusable((true));
//			
//			save_file.set_focusable(false);
//			close_file.set_focusable(false);
//			export_file.set_focusable(false);
			
			layer_new.set_focusable(false);
			layer_delete.set_focusable(false);
			layer_duplicate.set_focusable(false);
		}
	}
}
