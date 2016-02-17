package tools;

import main.IMP;

public class Command_close_file 
extends Command{

	public Command_close_file() {
		super("Bild schliessen", "close_file", "Datei");
		key_shortcut = 'w';
	}
	
	public void apply(){
			file.Img.fileMade = false;
			IMP.opened_image = null;
			// IMP.main_image_panel.load_image(null);
			IMP.image_informant.inform_about_new_image(null);
			IMP.menu.refresh_menu();
	}

}
