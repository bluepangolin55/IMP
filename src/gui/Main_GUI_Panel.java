package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.JPanel;

import main.IMP;
import gui_system.Context_Menu;
import gui_system.Tile;

public class Main_GUI_Panel 
extends JPanel 
implements ActionListener, KeyListener, MouseListener, MouseWheelListener, MouseMotionListener, ComponentListener
{
	private static final long serialVersionUID = 1L;

	
	public boolean preview=false;
	
	//Mouseposition
	public Point mouse_position=new Point();
	protected boolean mouse_dragged=false;
	private boolean shift_is_pressed;
	private boolean control_is_pressed;
	private boolean alt_is_pressed;
	private boolean space_is_pressed;
	
    //right-click menu
	public Context_Menu context_menu;
	
	private Tile mama_panel;
	
	
	public Main_GUI_Panel(){
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
		addComponentListener(this);
		setBackground(Color.DARK_GRAY);
        
		//hud elements
		mama_panel=new Mama_Tile(Tile.VERTICAL, new Dimension(1600,800));
		
		//context menu
		context_menu=new Context_Menu(IMP.menu.main_menu);
        this.requestFocus();
        repaint();
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		mama_panel.paint(g);
		
		g.setClip(0,0,this.getWidth(),this.getHeight());
		if(context_menu.enabled)
			context_menu.paint(g);
		IMP.active_tool.hud_panel.paint(g);
	}
	
	@Override
	public void actionPerformed(ActionEvent e){
		String cmd = e.getActionCommand();
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		if(context_menu.enabled)
			context_menu.key_typed(e);
		else if(IMP.active_tool.hud_panel.is_hovered(mouse_position))
			IMP.active_tool.hud_panel.key_typed(e);
		else
			mama_panel.key_typed(e);
		repaint();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		IMP.global_keys_informant.inform_about_key_pressed(e);
		int key = e.getKeyCode();

		if(context_menu.enabled)
			context_menu.key_pressed(e);
		else if(IMP.active_tool.hud_panel.is_hovered(mouse_position)){
			IMP.active_tool.hud_panel.key_pressed(e);
		}
		else{
			if (key == KeyEvent.VK_SPACE){
				if(IMP.active_tool.hud_panel.enabled)
					IMP.active_tool.hud_panel.enabled=false;
				else
					IMP.active_tool.hud_panel.enabled=true;
				
				space_is_pressed=true;
			}
			
			mama_panel.key_pressed(e);
		}
		repaint();
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		IMP.global_keys_informant.inform_about_key_released(e);
		mama_panel.key_released(e);
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton()==MouseEvent.BUTTON3 && !context_menu.enabled){
			//trigger context menu
		}
		else{
			if(context_menu.enabled)
				context_menu.mouse_clicked(e);
			else if(IMP.active_tool.hud_panel.is_hovered(e)){
				IMP.active_tool.hud_panel.mouse_clicked(e);
			}
			else
				mama_panel.mouse_clicked(e);
		}
		repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouse_position=e.getPoint();
		if(context_menu.enabled)
			context_menu.mouse_moved(e);
		else if(IMP.active_tool.hud_panel.is_hovered(e)){
			IMP.active_tool.hud_panel.mouse_moved(e);
		}
		else
			mama_panel.mouse_moved(e);
		
		mouse_position=e.getPoint();
		repaint();
	}
	
	
	@Override
	public void mousePressed(MouseEvent e) {
		this.requestFocus();
		mouse_position=e.getPoint();

		if(context_menu.enabled)
			context_menu.mouse_pressed(e);
		else if(IMP.active_tool.hud_panel.enabled && IMP.active_tool.hud_panel.is_hovered(e)){
			IMP.active_tool.hud_panel.mouse_pressed(e);
		}
		else
			mama_panel.mouse_pressed(e);
		repaint();
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {

		mouse_dragged=true;
		if(view_button_pressed()){		//the control button shouldn't have anything to do with tools
		}	
		
		if(context_menu.enabled)
			context_menu.mouse_dragged(e);
		else if((IMP.active_tool.hud_panel.enabled && IMP.active_tool.hud_panel.is_hovered(e)) || IMP.active_tool.hud_panel.pressed){
			IMP.active_tool.hud_panel.mouse_dragged(e);
		}
		else
			mama_panel.mouse_dragged(e);
		repaint();
		
		mouse_position=e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(context_menu.enabled)
			context_menu.mouse_released(e);
		else if(IMP.active_tool.hud_panel.enabled && IMP.active_tool.hud_panel.is_hovered(e)){
			IMP.active_tool.hud_panel.mouse_released(e);
		}
		else
			mama_panel.mouse_released(e);
		
		mouse_dragged=false;
		repaint();
	}
		
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mama_panel.mouse_wheel(e);
		repaint();
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		alt_is_pressed=false;
		this.requestFocus();
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		repaint();
	}
	
	//key mappings
	private boolean view_button_pressed(){
		return alt_is_pressed;
	}
	
	private boolean menu_button_pressed(){
		return space_is_pressed;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		mama_panel.set_size(new Dimension(this.getWidth(), this.getHeight()));
		mama_panel.refresh_children();
		repaint();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void componentHidden(ComponentEvent e) {
		// TODO Auto-generated method stub
	}
}
