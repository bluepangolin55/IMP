package gui_system;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.JPanel;

public class Panel_Optimize 
extends JPanel
implements ActionListener, MouseListener, MouseMotionListener
{
	//mouse
	private int mouseX;
	private int mouseY;
	public int n =9;
	
	//menu
	private int menuFocusedX=-1;	//which menu root is focused
	private int menuFocusedY=-1;	//which element of this menu root focused
	private boolean menuOn=false;
	private boolean menuOnHold=false;
	private String[][] menuElements;
	private int[][] menuElementsIndex;
	private int[] numberOfMenuElements;
	private Color[] menuColors={new Color(20,20,20),new Color(60,60,60),new Color(100,100,100)};
	private Color[] menuColors2={new Color(255,64,64),new Color(255,95,40),new Color(255,127,36)};//optional
	private int menuElementWidth;
	private int[] activeFiltersIndex=new int[]{0,0,0,0,0,0,0,0,0,0};		//the names of active effects in order
	private int[] activeFiltersSize=new int[]{0,0,0,0,0,0,0,0,0,0};		//the Y size of active effects in order
	public int numberOfActiveFilters=0;
	private int effectsTotalHeight=20;
	private int[] effectsHeightlighted=new int[]{0,0,0,0,0,0,0,0,0,0};
	
	private int[][] filter1ParValues;	//the first value represents the type of the bars: 1->buttons 2->bar 3->control dial
										//the second value represents the value of the parameter
										//the length of the array shows the number of parameters
	private String[] filter1ParNames;	//names of the parameters to be displayed
	
	private int[][] filter2ParValues;
	private String[] filter2ParNames;
	private int[][] filter3ParValues;
	private String[] filter3ParNames;
	private int[][] filter4ParValues;
	private String[] filter4ParNames;
	private int[][] filter5ParValues;
	private String[] filter5ParNames;
	
	//for development only!
	private int[][] filter0ParValues=new int[][]{{1,2,1},{50,1,52}};
	private String[] filter0ParNames=new String[]{"bar1","button1","bar2"};
	
	
	//scripts
	private String[] paths = new String[]{"/scripts/brightness.js","/scripts/contrast.js","/scripts/mosaic.js","/scripts/resolution.js"
			,"/scripts/sharpness.js","/scripts/size.js","/scripts/saturation.js"};
	private int numberOfScripts=paths.length;
	private String[] filterNames = new String[numberOfScripts];
	private String[] filterCategories = new String[numberOfScripts];
	private int[] filterSizeY = new int[numberOfScripts];
	
	//filter
	private int filter;
	//variables for drawing filters
	private int barSize=20;
	private int buttonSize=20;


	private static final String[] formats ={"%","width in mm","width in cm","width in dm"};
	
	public Panel_Optimize() {
		super();
		addMouseListener(this);
		addMouseMotionListener(this);
		try {
			scriptReader();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void scriptReader() throws FileNotFoundException, ScriptException{	//reads in scripts from the script folder
	       // create a script engine manager
        ScriptEngineManager factory = new ScriptEngineManager();
        // create a JavaScript engine
        ScriptEngine engine = factory.getEngineByName("JavaScript");

        numberOfMenuElements=new int[]{0,0,0};
        menuElements = new String[3][numberOfScripts+1];
        menuElementsIndex = new int[3][numberOfScripts+1];
        menuElements[0][0]="Bild";
        menuElements[1][0]="Farbe";
        menuElements[2][0]="Effekte";
        
        for(int i=0;i<numberOfScripts;i++){
            // evaluate JavaScript code from String
        	java.net.URL url = Class.class.getResource(paths[i]);
        	File scriptFile = new File(url.getPath());
        	
            engine.eval(new java.io.FileReader(scriptFile));
            filterNames[i] = (String) engine.get("name");
//            filterSizeY[i]=(int) (double) engine.get("size");
            filterCategories[i]=(String) engine.get("category");
            
            if(filterCategories[i].equalsIgnoreCase("Bild")){
            	numberOfMenuElements[0]++;
            	menuElements[0][numberOfMenuElements[0]]=filterNames[i];
            	menuElementsIndex[0][numberOfMenuElements[0]]=i;
            }
            else if(filterCategories[i].equalsIgnoreCase("Farbe")){
            	numberOfMenuElements[1]++;
            	menuElements[1][numberOfMenuElements[1]]=filterNames[i];
            	menuElementsIndex[1][numberOfMenuElements[1]]=i;
            }
            else {
            	numberOfMenuElements[2]++;
            	menuElements[2][numberOfMenuElements[2]]=filterNames[i];
            	menuElementsIndex[2][numberOfMenuElements[2]]=i;
            }
        }
        numberOfMenuElements[0]++;
        numberOfMenuElements[1]++;
        numberOfMenuElements[2]++;
	}
	
	@Override
	public void paintComponent(Graphics g){
		g.setColor(new Color(255,255,255));	//background
		g.fillRect(1,1,380,1000);
		//draws menu roots
		Font f = new Font("Calibri", Font.BOLD, 11);
		g.setFont(f);
		int i;
		menuElementWidth=360/menuElements.length;
		for(i=0;i<menuElements.length;i++){//draws root menu
			g.setColor(menuColors[i]);
			g.fillRect(i*menuElementWidth, 0, menuElementWidth, 20);
			g.setColor(Color.white);
			FontMetrics fm = g.getFontMetrics();
			g.drawString(menuElements[i][0],i*menuElementWidth+(menuElementWidth-fm.stringWidth(menuElements[i][0]))/2,
					fm.getAscent() + (20 - (fm.getAscent() + fm.getDescent())) / 2);
		}
		//draws filter
		int locationY=20;
		for (int n=0;n<numberOfActiveFilters;n++){
			paintFilter(g, locationY,n,activeFiltersIndex[n]);
			locationY=locationY+activeFiltersSize[n];
		}
		//draws menu contents if needed
		if(menuOn==true){
			g.setColor(new Color(60,60,60,180));
			g.fillRect(1,20,400,1000);
			paintMenuContents(g);
		}
		
	}
	
	public void paintMenuContents(Graphics g){
		int i;
		int menuWidth=360/menuElements.length;
		for(i=1;i<numberOfMenuElements[menuFocusedX];i++){//draws menu contents
			if(i==menuFocusedY+1)
				g.setColor(new Color(229,200,150));	//color for the focused element
			else
				g.setColor(new Color(220,220,220));//color for unfocused elements
			g.fillRect(menuFocusedX*menuElementWidth, i*20, menuWidth, 20);
			g.setColor(Color.black);
			FontMetrics fm = g.getFontMetrics();
			g.drawString(menuElements[menuFocusedX][i], menuFocusedX*menuElementWidth+(menuWidth - fm.stringWidth(menuElements[menuFocusedX][i])) / 2,
					20+i*20 - (fm.getAscent() + fm.getDescent()) / 2);
			}
		}
	
		public void paintFilter(Graphics g,int  locationY, int n, int i){
			g.setColor(new Color(200,200,200));
			g.fillRect(1,locationY,400,filterSizeY[i]);
			g.setColor(new Color(0,0,0));
			g.drawString(filterNames[i], 30, locationY+10);
			

			int y=locationY+20;
			for(int j=0;j<filter0ParValues[0].length;j++){
				if(filter0ParValues[0][j]==1){
					g.drawString(filter0ParNames[j], 150, y-5);
					g.drawString("" + filter0ParValues[1][j], 250, y-5);
					paintSlider(g,60, y, filter0ParValues[1][j]);
					y=y+50;
				}
				else if(filter0ParValues[0][j]==2){
					y=y-10;
					paintButton(g,60, y, filter0ParValues[1][j], filter0ParNames[j]);
					y=y+50;
				}
			}
			
			paintYes(g, locationY, 80, effectsHeightlighted[n]);
//			g.drawLine(0, locationY, 400, locationY);
//			g.drawLine(0, locationY+80, 400, locationY+80);
		}
		
		public void paintYes(Graphics g, int locationY, int sizeY, int heightlight){
			if(heightlight==1)
				g.setColor(new Color(200,100,100));	//red for abbort
			else
				g.setColor(new Color(50,50,50));
			g.fillRect(0,locationY,20,sizeY/2);
			g.setColor(new Color(100,100,100));	
			g.drawRect(0,locationY,20,sizeY/2);
			
			if(heightlight==2)
				g.setColor(new Color(130,200,70));	//green for render
			else
				g.setColor(new Color(200,200,200));
			g.fillRect(0,locationY+40,20,sizeY/2);
			g.setColor(new Color(100,100,100));	
			g.drawRect(0,locationY+40,20,sizeY/2);
	}
		
		public void paintSlider(Graphics g, int x, int y, int value){
			g.setColor(new Color(255,255,255));		//for sliderbackground
			g.fillRect(x,y,300,barSize);
			g.setColor(new Color(150,150,150));		//for sliderforeground
			g.fillRect(x,y,value*3,barSize);
			g.setColor(new Color(100,100,100));		//for slideredges
			g.drawRect(x,y,300,barSize);
	}
		
		public void paintButton(Graphics g, int x, int y, int value, String text){
			FontMetrics fm = g.getFontMetrics();		//to calculate text/button length
			if(value==1){
				g.setColor(new Color(255,255,255));		//for buttonforeground
			}
			else{
				g.setColor(new Color(200,200,200));		//for buttonforeground
			}
			
			g.fillRect(x,y,fm.stringWidth(text)+10,buttonSize);
			g.setColor(new Color(100,100,100));			//for buttonedges
			g.drawRect(x,y,fm.stringWidth(text)+10,buttonSize);
			g.setColor(new Color(0,0,0));		
			g.drawString(text, x+5, y+15);
			//buttonText
		}

	private static final long serialVersionUID = 1L;
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
	}
	
	public void keyPressed(KeyEvent event) {
		int key = event.getKeyCode();
	}






	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(menuOn==false && numberOfActiveFilters!=0 && e.getY()<effectsTotalHeight){
			int locationY=effectsTotalHeight;
			int n=numberOfActiveFilters-1;
			while(e.getY()<locationY && n!=-1){		//checks over which effect the Y value of the cursor is
				locationY=locationY-activeFiltersSize[n];
				n--;
			}
			n++;
			if(e.getX()<20){
				if(e.getY()<locationY+activeFiltersSize[n]/2){	//close the effect
					effectsTotalHeight=effectsTotalHeight-activeFiltersSize[n];
					for(int i=n;i<numberOfActiveFilters-1;i++){
						activeFiltersIndex[i]=activeFiltersIndex[i+1];
						activeFiltersSize[i]=activeFiltersSize[i+1];
					}
					System.out.println(effectsTotalHeight);
					numberOfActiveFilters--;
					repaint();
				}
				else{	//render the effect
				}
			}
		}
		if(e.getY()<20){
			if(menuOnHold==false){
				menuOnHold=true;
				menuOn=true;
			}
			else{
				menuOnHold=false;
				menuOn=false;
			}
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getY()<20){
			menuOn=true;
			menuFocusedX=e.getX()/menuElementWidth;
			repaint();
		}
		else	if(menuOn==false && numberOfActiveFilters!=0){
			int locationY=effectsTotalHeight;
			int n=numberOfActiveFilters-1;
			while(e.getY()<locationY && n!=-1){		//checks over which effect the Y value of the cursor is
				locationY=locationY-activeFiltersSize[n];
				n--;
			}
			n++;
			if(e.getX()<20){
				if(e.getY()<locationY+activeFiltersSize[n]/2){	//lower button turns green
					effectsHeightlighted[n]=1;
					repaint();
				}
				else{	//upper button turns red
					effectsHeightlighted[n]=2;
					repaint();
				}
			}
		}
		else if(menuOnHold==true){
			menuOnHold=false;
		}
	}


	@Override
	public void mouseReleased(MouseEvent e) {
		if (menuOn==true){
			if(menuFocusedY>-1 && menuFocusedY<numberOfMenuElements[menuFocusedX]-1){//alles provisorisch, sollte mit existierenden effekten verglichen werden
				numberOfActiveFilters++;
				activeFiltersIndex[numberOfActiveFilters-1]=menuElementsIndex[menuFocusedX][menuFocusedY+1];
				activeFiltersSize[numberOfActiveFilters-1]=80;	//provisorisch!
				effectsTotalHeight=effectsTotalHeight+activeFiltersSize[numberOfActiveFilters-1];
				}	
		}

		menuOn=false;
		
		
		menuFocusedY=-1;
		
		for(n=0;n<effectsHeightlighted.length;n++){
			effectsHeightlighted[n]=0;
		}
		repaint();
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX=e.getX();
		if(menuOn==true && e.getX()>0 && e.getX()<399){
			if(menuFocusedX!=e.getX()/menuElementWidth){
				menuFocusedX=e.getX()/menuElementWidth;
				if(menuFocusedY!=e.getY()/20-1 && numberOfMenuElements[menuFocusedX] >e.getY()/20-1){
					menuFocusedY=e.getY()/20-1;
					}
				repaint();
			}
			else if(menuFocusedY!=e.getY()/20-1 && numberOfMenuElements[menuFocusedX] >e.getY()/20-1){
				menuFocusedY=e.getY()/20-1;
				repaint();
			}
		}
	}


	@Override
	public void mouseMoved(MouseEvent e) {
		if(menuOn==false && numberOfActiveFilters!=0){
			int locationY=effectsTotalHeight;
			int n=numberOfActiveFilters-1;
			while(e.getY()<locationY && n!=-1){		//checks over which effect the Y value of the cursor is
				locationY=locationY-activeFiltersSize[n];
				n--;
			}
			n++;
			if(e.getX()<20){
				if(e.getY()<locationY+activeFiltersSize[n]/2){	//render the effect
				}
				else{	//close the effect
				}
			}
		}
		else if(menuOn==true && e.getX()>0 && e.getX()<399){
			if(menuFocusedX!=e.getX()/menuElementWidth){
				mouseX=e.getX();
				menuFocusedX=e.getX()/menuElementWidth;
				if(menuFocusedY!=e.getY()/20-1 && numberOfMenuElements[menuFocusedX] >e.getY()/20-1){
					menuFocusedY=e.getY()/20-1;
					}
				repaint();
			}
			else if(menuFocusedY!=e.getY()/20-1 && numberOfMenuElements[menuFocusedX] >e.getY()/20-1){
				menuFocusedY=e.getY()/20-1;
				repaint();
			}
		}
		
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}


