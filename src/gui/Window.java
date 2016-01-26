package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import file.Input;
import file.Output;
import gui_system.Menubar;
import main.IMP;
import main.Preferences;
import main.WindowClosingAdapter;

public class Window
extends JFrame
implements ActionListener, Design_Preferences
{
	int PosX=300;
	int PosY=100;
	int Width=1200;
	int Height=600;
	public boolean leftPanelHasFocus=true;

	private static final long serialVersionUID = 1L;
    public JPanel main_panel;	//left ImagePanel where the image is displayed
//    public Sidepanel sidepanel;
    public Preferences preferences;
    
    static private final String newline = "\n";
    JButton openButton, saveButton;
    JFileChooser fc;
    
    
	public Window(JPanel main_image_panel) {
		super("IMP");
		main_panel=main_image_panel;
		addWindowListener(new WindowClosingAdapter());
		preferences = new Preferences();

		setSize(Width,Height);
		setLocation(PosX, PosY);
		setVisible(true);
        //Layout
		if(IMP.preferences.menubar_on)
			setJMenuBar(new Menubar(this));	//menubar
//        sidepanel = new Sidepanel();		//sidepanel which displays tools and options
        
		Container cp = getContentPane();
		GridBagLayout gbl = new GridBagLayout();
		GridBagConstraints gbc = new GridBagConstraints();
		cp.setLayout(gbl);
		gbc.gridx=0;
		gbc.gridy=0;
		gbc.gridwidth=1;
		gbc.gridheight=1;
		gbc.weightx=100;
//		gbc.insets=new Insets(1,1,1,1); 
		gbc.weighty=100;
		gbc.fill=GridBagConstraints.BOTH;
		gbl.setConstraints(main_panel, gbc);
        cp.add(main_panel);
 
		this.setExtendedState(this.getExtendedState() | Frame.MAXIMIZED_BOTH);	//to maximize the window
		this.setExtendedState(this.getExtendedState() | Frame.NORMAL);	//to draw the window in its given size
	}
	
	@Override
	public void paint(Graphics g) {
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		
		String cmd = e.getActionCommand();

		if (cmd.equals("Neu")){			
//			sidepanel.upperPanel.load_routine(IMP.routines.get(0));
			}
		
		else if (cmd.equals("Datei öffnen")){
		    //Create a file chooser
		    fc = new JFileChooser();
		    
			int returnVal = fc.showOpenDialog(Window.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                Input.scan_document(file);
            } 
            else {
            }
		}
		
		else if (cmd.equals("Datei speichern")){
		    //Create a file chooser
		    fc = new JFileChooser();
		    
            int returnVal = fc.showSaveDialog(Window.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
    			Output.save_file(file);
            } else {
            }
		}
	}
}