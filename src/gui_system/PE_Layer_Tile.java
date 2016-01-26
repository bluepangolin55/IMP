package gui_system;

import java.util.ArrayList;
import java.util.List;

import file.Img;

import main.IMP;

public class PE_Layer_Tile 
extends Tile{
	private List<PE_Layer_Item> layers;
	public PE_Layer_Tile() {
		layers=new ArrayList<PE_Layer_Item>();
		IMP.image_informant.add_client(this);
		alignment=VERTICAL;
	}
	
	
	@Override
	public void new_image_loaded(Img new_image) {
		scan_for_layers();
	}
	
	public void scan_for_layers(){
		if(IMP.opened_image!=null){
			layers.clear();
			children.clear();
			for(int i=0; i<IMP.opened_image.amountOfLayers;i++){
				children.add(new PE_Layer_Item(IMP.opened_image.layers.get(i)));
				System.out.println(i);
			}
			refresh_children();
		}
	}
	
	
}
