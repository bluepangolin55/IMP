package file;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import functionality.Selection;

import main.IMP;

public class Img {
	
	public static boolean fileMade=false;
	
	public String filename;
	public String path;
	public ArrayList<Layer> layers;
	public Selection active_selection;
	public ArrayList<Selection> active_selections;
	public int amountOfLayers=0;
	private Layer selected_layer;
	private BufferedImage completeImage;
	public Layer previouslySelectedLayer;
	private boolean workInProgress=false;
	private int width;
	private int height;
	//mouse position
	public int mouse_x;
	public int mouse_y;
	
	public Img(int width, int height, int zoom){
		layers=new ArrayList<Layer>();
		active_selection=null;
		active_selections=new ArrayList<Selection>();
		this.width=width;
		this.height=height;
		createLayer(width,height);
		completeImage = get_selected_layer().get_image();
	}
	
	public Img(BufferedImage image){
		layers=new ArrayList<Layer>();
		active_selection=null;
		active_selections=new ArrayList<Selection>();
		this.width=image.getWidth();
		this.height=image.getHeight();
		createLayer(image);
		completeImage = get_selected_layer().get_image();
	}
	
	public void createLayer(int width, int height){
		Layer new_layer = new Image_Layer(width,height,1,"Ebene " + (amountOfLayers+1),this);
		new_layer.select();
		layers.add(new_layer);
		amountOfLayers++;
	}
	public void createLayer(int width, int height, String name){
		Layer new_layer =new Image_Layer(width,height,1,name,this);
		new_layer.select();
		layers.add(new_layer);
		amountOfLayers++;
	}
	public void createLayer(int width, int height, int imageType){
		Layer new_layer = new Image_Layer(width,height,imageType,"Ebene "  + (amountOfLayers+1),this);
		new_layer.select();
		layers.add(new_layer);
		amountOfLayers++;
	}
	public void createLayer(int width, int height, int imageType, String name){
		Layer new_layer = new Image_Layer(width,height,imageType,name,this);
		new_layer.select();
		layers.add(new_layer);
		amountOfLayers++;
	}
	public void createLayer(BufferedImage image){
		Image_Layer new_layer = new Image_Layer(image.getWidth(),image.getHeight(),1,"Ebene "  + (amountOfLayers+1),this);
		Graphics g = new_layer.image.getGraphics();
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
//		new_layer.set_image(image);
		new_layer.select();
		layers.add(new_layer);
		amountOfLayers++;
	}
	
	public int getWidth(){
		return width;
	}
	
	public int getHeight(){
		return height;
	}
	
	public BufferedImage getImage(){
		return get_selected_layer().get_image();
	}
	
	public BufferedImage getCompleteImage(){
		return completeImage;
	}
	
	public void set_image(BufferedImage new_image){
		get_selected_layer().set_image(new_image);
	}
	
	public void unfocusAll(){
		for(int i=0;i<amountOfLayers;i++){
			layers.get(i).deselect();
		}
	}
	
	public void composeCompleteImage(){
		Graphics g = completeImage.getGraphics();
		for(int i=amountOfLayers-1;i>-1;i--){
			if(layers.get(i).isVisible()){
				layers.get(i).draw(g, 0, 0, width, height);
			}
		}
	}
	

	public boolean workInProgress() {
		return workInProgress;
	}
	public void workInProgress(boolean b) {
		workInProgress=b;
	}


	public void deleteLayer(Layer layer_to_delete) {
		layers.remove(layer_to_delete);
		amountOfLayers--;
	}
	
	public void switchLayer(Layer a, Layer b) {
		Layer temp=a;
		a=b;
		b=temp;
	}

	public void duplicateLayer(int selectedLayer2) {
		IMP.opened_image.createLayer(IMP.opened_image.getWidth(), IMP.opened_image.getHeight());
		Graphics g=this.getImage().getGraphics();
		g.drawImage(previouslySelectedLayer.get_image(),0,0,
				getWidth(),getHeight(),null);
	}
	
	public void set_filename(File file){
		filename=file.getName();
		path=file.getPath();
		path=path.replace(filename, "");
		if(filename.contains(".")){
			String temp=new String();
			for(int i=filename.length()-1; filename.charAt(i)!='.';i--){
				temp=filename.charAt(i) + temp;
			}
			temp='.' + temp;
			filename=filename.replace(temp, "");
		}
		filename=filename.replaceFirst("hoad", "");
		
		System.out.println(path);
		System.out.println(filename);
	}

	public Layer get_selected_layer() {
		return selected_layer;
	}

	public void set_selected_layer(Layer selected_layer) {
		this.selected_layer = selected_layer;
	}
}
