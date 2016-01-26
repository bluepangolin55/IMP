package main;
/*WindowClosingAdapter.java*/

import java.awt.event.*;

public class WindowClosingAdapter extends WindowAdapter
{
	private boolean exitSystem;
	
	/**
	 * Erzeugt einen WindowClosingAdapter zu Schliessen 
	 * des Fensters. Ist exitSystem true, wird das komplette 
	 * Programm beendet.
	 */
	public WindowClosingAdapter(boolean exitSystem)
	{
		this.exitSystem=exitSystem;
	}
	
	public WindowClosingAdapter()
	{
		this(false);
	}
	
	@Override
	public void windowClosing(WindowEvent event)
	{
		event.getWindow().setVisible(false);
		event.getWindow().dispose();
		if (exitSystem) {
			System.exit(0);
		}
	}
}
