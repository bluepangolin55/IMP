package tools;

import main.IMP;

public class Command_exit extends Command {

	public Command_exit() {
		super("Programm beenden", "exit");
		category = "Datei";
		key_shortcut = 'q';
		needs_open_image = false;
	}

	public void apply() {
		IMP.apply_action(action);
	}
}