package main;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

import javax.imageio.ImageIO;
import javax.script.ScriptException;
import javax.swing.*;

import gui.Main_GUI_Panel;
import gui.Mama_Tile;
import tools.*;
import file.Img;
import file.Input;
import gui.Image_Tile;
import gui_system.*;

public class IMP { // main function

	// elements
	static List<Img> loaded_images;
	public static Img opened_image;
	public static BufferedImage preview_image;
	public static Menu menu;
	public static Preferences preferences;

	// functionality
	public static List<Tool> tools;
	public static List<Gadget> gadgets;
	public static List<Tool> routines;
	public static List<Menulizable> commands;
	public static Tool active_tool;
	public static Tool previous_tool;
	public static boolean routine_in_process;
	public static ForkJoinPool thread_pool;

	// Events
	public static Image_Informant image_informant;
	public static Global_Keys_Informant global_keys_informant;
	public static Tool_Broadcaster action_informant;

	// GUI
	public static JFrame main_window;
	public static Main_GUI_Panel main_panel;
	public static Mama_Tile mama;
	public static Image_Tile active_image_tile;
	public static Tile tool_sidepanel;
	public static Tile gadget_sidepanel;

	public static void main(String[] args) {
		thread_pool = new ForkJoinPool();
		// initiate event agents
		image_informant = new Image_Informant();
		global_keys_informant = new Global_Keys_Informant();
		action_informant = new Tool_Broadcaster();

		// initiate important structures
		loaded_images = new ArrayList<Img>();

		// initiate funcionality
		initialize_tools();
		initialize_routines();
		initialize_commands();
		preferences = new Preferences();

		// initiate user interface
		menu = new Menu();
		main_panel = new Main_GUI_Panel();
		main_window = new JFrame();
		main_window.add(main_panel);
		main_window.addWindowListener(new WindowClosingAdapter());
		main_window.setSize(1200, 600);
		main_window.setLocation(100, 100);
		main_window.setVisible(true);

		tools.get(0).activate();
		gadgets.get(0).activate();

		// update miscellaneous
		menu.refresh_menu();
	}

	static void initialize_tools() {
		tools = new ArrayList<>();
		tools.add(new Tool_Brush());
		tools.add(new Tool_Polygon_Selection());
		tools.add(new Tool_Selection());
		tools.add(new Tool_Selection_better());
		tools.add(new Tool_3D());
		tools.add(new Tool_2D());
		active_tool = tools.get(0);

		gadgets = new ArrayList<>();
		gadgets.add(new Gadget_Layers());
		gadgets.add(new Gadget_Information());
	}

	static void initialize_routines() {
		routines = new ArrayList<>();
		routines.add(new Routine_New_Image());
		routines.add(new Routine_Export_File());
		routines.add(new Routine_Pythagoras_Tree());
		routines.add(new Routine_Mandelbrot());
		routines.add(new Routine_Pattern_Caro());
		routines.add(new Routine_Pattern_Triangle());
		routines.add(new Routine_Brightness());
		routines.add(new Routine_Contrast());
		routines.add(new Routine_About_The_Program());
		routines.add(new Routine_Open_Image());
		routines.add(new Routine_Blur());
		routines.add(new Routine_Filter());
		routines.add(new Routine_Resize());
		routines.add(new Routine_Segment());
		routines.add(new Routine_EdgeDetection());
		routines.add(new Routine_EdgeDetection2());
	}

	static void initialize_commands() {
		commands = new ArrayList<Menulizable>();
		commands.add(new Command_close_file());
		commands.add(new Command_exit());
		commands.add(new Command("toggle menubar", "toggle_menubar", "View"));
		commands.add(new Command("toggle sidepanel", "toggle_sidepanel", "View"));
		for (Tool r : routines) {
			commands.add(r);
		}
		for (Tool t : tools) {
			commands.add(t);
		}
	}

	public static void apply_action(String action) {
		if (action.equals("new_file")) {
			// sidepanel.upperPanel.load_routine(IMP.routines.get(0));
		}
		else if (action.equals("toggle_menubar")) {
			mama.toggleComponent(mama.MENUBAR);
		}
		else if (action.equals("toggle_sidepanel")) {
			mama.toggleComponent(mama.SIDEPANEL);
		}
		else if (action.equals("open_file")) {
			// Create a file chooser
			JFileChooser fc = new JFileChooser();

			int returnVal = fc.showOpenDialog(main_window);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				Input.scan_document(file);
			}
			else {
			}
		}
		else if (action.equals("save_file")) {

			// Create a file chooser
			JFileChooser fc = new JFileChooser();

			int returnVal = fc.showSaveDialog(main_window);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				try {
					// save the image
					ImageIO.write(IMP.opened_image.getImage(), "png", file);
				} catch (IOException e) {
				}
			}
			else {
			}
		}

		else if (action.equals("export_as_bmp")) {
			File file = new File(IMP.opened_image.path + "-"
					+ IMP.opened_image.filename + ".bmp");
			try {
				ImageIO.write(IMP.opened_image.getImage(), "bmp", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if (action.equals("export_as_gif")) {
			File file = new File(IMP.opened_image.path + "-"
					+ IMP.opened_image.filename + ".gif");
			try {
				ImageIO.write(IMP.opened_image.getImage(), "gif", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if (action.equals("export_as_jpeg")) {
			File file = new File(IMP.opened_image.path + "-"
					+ IMP.opened_image.filename + ".jpg");
			try {
				ImageIO.write(IMP.opened_image.getImage(), "jpeg", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if (action.equals("export_as_png")) {
			File file = new File(IMP.opened_image.path + "-"
					+ IMP.opened_image.filename + ".png");
			try {
				ImageIO.write(IMP.opened_image.getImage(), "png", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if (action.equals("close_file")) {
			file.Img.fileMade = false;
			IMP.opened_image = null;
			// IMP.main_image_panel.load_image(null);
			IMP.image_informant.inform_about_new_image(null);
			IMP.menu.refresh_menu();
		}
		if (action.equals("show_sidepanel")) {
			if (tool_sidepanel.visible) {
				tool_sidepanel.hide();
			}
			else {
				tool_sidepanel.visible = true;
			}
		}
		else if (action.equals("exit")) {
			System.out.println("exiting");
			main_window.dispose();
		}
	}

	public static Menulizable get_command_by_key(char key) {
		Menulizable result = null;
		for (Menulizable c : commands) {
			if (c.get_key_shortcut() == key) {
				result = c;
				break;
			}
		}
		return result;
	}

	public static void key_pressed(KeyEvent e) {
		if (e.isControlDown()) {
			Menulizable c = get_command_by_key(Character.toLowerCase((char) e.getKeyCode()));
			if (c != null && c.safe_to_activate()) {
				c.activate();
			}
		}
	}

}
