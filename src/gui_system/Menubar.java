package gui_system;

import java.awt.event.*;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import main.IMP;


public class Menubar 
extends JMenuBar{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JMenu m;
	public static JMenuItem newImage;
	public static JMenuItem openImage;
	public static JMenuItem saveImage;
	public static JMenuItem brightness;
	public static JMenuItem caro;
	public static JMenuItem mandelbrot;
	public static JMenuItem triangle;
	public static JMenuItem pythagorasTree;
	public static JMenuItem showSidepanel;
	public static JMenuItem zoomPanel;
	public static JMenuItem zoomIn;
	public static JMenuItem zoomOut;
	public static JMenuItem realView;
	public static JMenuItem fillView;
	
	public Menubar(ActionListener listener){
		newImage = new JMenuItem("Neu");
		newImage.setActionCommand("Neu");
		newImage.addActionListener(listener);
		
		openImage = new JMenuItem("Datei öffnen");
		openImage.setActionCommand("Datei öffnen");
		openImage.addActionListener(listener);

		saveImage = new JMenuItem("Datei speichern");
		saveImage.setActionCommand("Datei speichern");
		saveImage.addActionListener(listener);
		//-----------------------------------------
		brightness = new JMenuItem("Helligkeit/Kontrast");
		brightness.setActionCommand("Helligkeit/Kontrast");
		brightness.addActionListener(listener);
		//-----------------------------------------
		caro = new JMenuItem("caro");
		caro.setActionCommand("caro");
		caro.addActionListener(listener);
		
		triangle = new JMenuItem("triangle");
		triangle.setActionCommand("triangle");
		triangle.addActionListener(listener);
		
		mandelbrot = new JMenuItem("Mandelbrot");
		mandelbrot.setActionCommand("Mandelbrot");
		mandelbrot.addActionListener(listener);
		
		pythagorasTree = new JMenuItem("Pythagoras-Baum");
		pythagorasTree.setActionCommand("Pythagoras-Baum");
		pythagorasTree.addActionListener(listener);
		//-----------------------------------------		
		showSidepanel = new JMenuItem("Zeige Werkzeugleite");
		showSidepanel.setActionCommand("Zeige Werkzeugleite");
		showSidepanel.addActionListener(listener);
		
		zoomPanel = new JMenuItem("Zoom");
		zoomPanel.setActionCommand("Zoom");
		zoomPanel.addActionListener(listener);
		
		zoomIn = new JMenuItem("Vergrössern");
		zoomIn.setActionCommand("Vergrössern");
		zoomIn.addActionListener(listener);
		
		zoomOut = new JMenuItem("Verkleinern");
		zoomOut.setActionCommand("Verkleinern");
		zoomOut.addActionListener(listener);
		
		realView = new JMenuItem("Tatsächliche Auflösung");
		realView.setActionCommand("Tatsächliche Auflösung");
		realView.addActionListener(listener);
		
		fillView = new JMenuItem("Bild strecken");
		fillView.setActionCommand("Bild strecken");
		fillView.addActionListener(listener);
		
		
		refreshMenu();
		
		//Datei
		m = new JMenu ("Datei");
		m.add(newImage);
		m.add(openImage);
		m.add(saveImage);
		add(m);
		//Bild
		m= new JMenu("Bild");
		m.add(brightness);
		add(m);
		//Muster
		m= new JMenu("Muster");
		m.add(caro);
		m.add(triangle);
		m.add(mandelbrot);
		m.add(pythagorasTree);
		add(m);
		//Ansicht
		m= new JMenu("Ansicht");
		m.add(showSidepanel);
		m.add(zoomPanel);
		m.add(zoomIn);
		m.add(zoomOut);
		m.add(realView);
		m.add(fillView);
		add(m);
	}

	public static void refreshMenu(){
		if(IMP.opened_image!=null){
			if(1==1){
				saveImage.setEnabled(false);
				brightness.setEnabled(false);
				caro.setEnabled(false);
				triangle.setEnabled(false);
				mandelbrot.setEnabled(false);
				pythagorasTree.setEnabled(false);
				
				zoomPanel.setEnabled(true);
				zoomIn.setEnabled(true);
				zoomOut.setEnabled(true);
				realView.setEnabled(true);
				fillView.setEnabled(true);
			}
			else{
				saveImage.setEnabled(true);
				brightness.setEnabled(true);
				caro.setEnabled(true);
				triangle.setEnabled(true);
				mandelbrot.setEnabled(true);
				pythagorasTree.setEnabled(true);
				zoomPanel.setEnabled(true);
				zoomIn.setEnabled(true);
				zoomOut.setEnabled(true);
				realView.setEnabled(true);
				fillView.setEnabled(true);
			}

		}
		else{
			saveImage.setEnabled(false);
			brightness.setEnabled(false);
			caro.setEnabled(false);
			triangle.setEnabled(false);
			mandelbrot.setEnabled(false);
			pythagorasTree.setEnabled(false);
			zoomPanel.setEnabled(false);
			zoomIn.setEnabled(false);
			zoomOut.setEnabled(false);
			realView.setEnabled(false);
			fillView.setEnabled(false);
		}
	}

}
