package tools;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import backgroundThreads.Thumbnail_Reader;
import main.Data_Access;
import main.IMP;
import file.Input;
import gui.Design_Preferences;
import gui.Image_Tile;
import gui_system.GuiIntValue;
import gui_system.GuiStringValue;
import gui_system.PE_Button2;
import gui_system.PE_File_Preview;
import gui_system.PE_File_Preview_Element;
import gui_system.PE_File_Preview_Folders;
import gui_system.PE_Slider;
import gui_system.PE_Textfield;
import gui_system.Tile;

public class Routine_Open_Image 
extends Routine
implements Data_Access, Design_Preferences{
	
	//gui
	Tile main_tile;
	PE_File_Preview image_preview;
	PE_File_Preview_Folders folder_preview;
	private List<PE_File_Preview_Element> file_tiles;
	PE_Textfield location_bar_textfield;
	GuiIntValue preview_size_value;
	GuiIntValue scroll_value;
	
	//Threads
	Thumbnail_Reader thumbnail_reader;
	
	public Routine_Open_Image() {
		super();
		//functionality
		directory=new File(System.getProperty("user.home"));
		
		//GUI
		set_name("Öffne ein Bild");
		category = "Datei";
		key_shortcut = 'o';
		main_tile=new Tile(Tile.VERTICAL);
		
//		PE_Button2 open_button = new PE_Button2("Öffne das Bild", "open", this);
//		open_button.set_height(40);
//		open_button.height_is_fixed=true;
//		open_button.set_width(300);
//		open_button.width_is_fixed=true;
//		open_button.borders_enabled=false;
//		main_tile.add(open_button);
		
		Tile location_bar = new Tile(Tile.HORIZONTAL);
		location_bar.set_height(30);
		location_bar.height_is_fixed=true;
		search_bar_text=new GuiStringValue(directory.getAbsolutePath());
		location_bar_textfield=new PE_Textfield(search_bar_text);
		location_bar.add(new PE_Button2("directory up", "directory_up", this));
		location_bar.add(location_bar_textfield);
//		location_bar.add(open_button);
		main_tile.add(location_bar);
		
		preview_size_value=new GuiIntValue(0,1800,200);
		scroll_value=new GuiIntValue(0,4000,0);
		
		folder_preview = new PE_File_Preview_Folders();
		image_preview = new PE_File_Preview(preview_size_value, scroll_value);
		file_tiles=new ArrayList<PE_File_Preview_Element>();
		

		Tile horizontal_tile=new Tile(Tile.HORIZONTAL);
		horizontal_tile.add(folder_preview);
		horizontal_tile.add(image_preview);
		
		main_tile.add(horizontal_tile);
		PE_Slider zoom_slider = new PE_Slider("zoom",preview_size_value, this);
		main_tile.add(zoom_slider);
		main_tile.refresh_children();
		
		//-----
		needs_open_image = false;
	}
	
	@Override
	public void activate(){
		super.activate();
		scan_directory(directory);
	}

	@Override
	public void apply(){
		thumbnail_reader.interrupt();
        Input.scan_document(selected_image);
		super.apply();
		IMP.active_image_tile.view.filled_view();
	}
	
	@Override
	public void abort(){
		super.abort();
	}
	
	@Override
	public void mouse_pressed(MouseEvent e){
		main_tile.mouse_pressed(e);
	}
	
	@Override
	public void mouse_dragged(MouseEvent e){
		main_tile.mouse_dragged(e);
	}
	
	@Override
	public void mouse_released(MouseEvent e){
		main_tile.mouse_released(e);
	}
	
	public void mouse_released_anywhere(MouseEvent e){
		main_tile.mouse_released_anywhere(e);
	}
	
	@Override
	public void mouse_clicked(MouseEvent e){
		for(int i=0;i<file_tiles.size();i++){
			file_tiles.get(i).selected=false;
		}
		main_tile.mouse_clicked(e);
	}
	
	@Override
	public void key_pressed(KeyEvent e){
		main_tile.key_pressed(e);
	}
	
	@Override
	public void key_typed(KeyEvent e){
		main_tile.key_typed(e);
	}
	
	@Override
	public void mouse_wheel(MouseWheelEvent e){
		main_tile.mouse_wheel(e);
	}
	
	@Override
	public void take_action(String action) {
		if(action=="open"){
			if (selected_image!=null)
				apply();
		}
		else if(action=="selected"){
			selected_image=null;
			for(int i=0;i<file_tiles.size();i++){
				if(file_tiles.get(i).selected)
					selected_image=file_tiles.get(i).get_file();
			}
			if(selected_image!=null)
				location_bar_textfield.set_text(selected_image.getAbsolutePath());
			else
				location_bar_textfield.set_text(directory.getAbsolutePath());
		}
		else if(action=="directory_up"){
			this.directory=directory.getParentFile();
			scan_directory(this.directory);
		}
			
		else{
			File directory = new File(action);
			if(directory.isDirectory()){
				this.directory=directory;
				scan_directory(this.directory);
			}
		}
	}
	
	@Override
	public void paint_on_screen(Graphics g, Image_Tile tile) {
		main_tile.set_position(IMP.active_image_tile.get_position());
		main_tile.set_size(IMP.active_image_tile.get_size());
		main_tile.refresh_children();
		
		g.setClip(main_tile.get_position().x,main_tile.get_position().y,main_tile.getWidth(),main_tile.getHeight());
		main_tile.paint(g);
	}
	
	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
	//------------------------------------------------------------------------------------------------------------------------------
			//funcionality
			
//			Function parameters that the user can change in the dialogue. The calculated  values are set to make the filter work in a default way.
	static int width=1000;	//width of the new image
	static int height=1000;	//height of the new image
	private File directory;
	private File selected_image;
	private List<File> files;
	private List<File> directories;
	private GuiStringValue search_bar_text;
	
	
	private void scan_directory(File directory){
		//read all directory entries
		File[] elements=directory.listFiles();
		
		//filter image files into a new list called files
		files=new ArrayList<File>();
		directories=new ArrayList<File>();
		for(int i=0;i<elements.length;i++){
			if(elements[i].isFile() && file_contains_image_extension(elements[i]))
				files.add(elements[i]);
			else if(elements[i].isDirectory() && !elements[i].isHidden())
				directories.add(elements[i]);
		}
		//empty old preview file objects and load the new ones
		file_tiles.clear();
		image_preview.remove_all_children();
		folder_preview.remove_all_children();
		for(int i=0;i<files.size();i++){
			PE_File_Preview_Element file_to_add=new PE_File_Preview_Element(files.get(i), "action", this);
			file_tiles.add(file_to_add);
			image_preview.add(file_to_add);
		}
		
		for(int i=0;i<directories.size();i++){
			folder_preview.add(new PE_Button2(directories.get(i).getName(), directories.get(i).getAbsolutePath(), this));
		}
		
		//Threads
		thumbnail_reader=new Thumbnail_Reader(file_tiles);
		thumbnail_reader.start();
	}

	private boolean file_contains_image_extension(File file) {
		if(file.getName().endsWith(".jpg") || file.getName().endsWith(".png")  || file.getName().endsWith(".bmp")  || file.getName().endsWith(".gif") ||
			file.getName().endsWith(".jpeg") || file.getName().endsWith(".JPG"))
			return true;
		else
			return false;
	}
}
