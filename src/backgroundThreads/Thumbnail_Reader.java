package backgroundThreads;

import gui_system.PE_File_Preview_Element;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import main.IMP;

public class Thumbnail_Reader 
extends Thread implements ImageObserver{
	
	private List<PE_File_Preview_Element> file_tiles;

	public Thumbnail_Reader(List<PE_File_Preview_Element> file_tiles) {
		this.file_tiles=file_tiles;
	}

	@Override
	public void run() {
		for(int i=0;i<file_tiles.size();i++){
		    BufferedImage source=null;
		    BufferedImage destination=null;
		    int width;
		    int height;
		    double ratio;
			try {
				source=ImageIO.read(file_tiles.get(i).image_file);
				if(source == null){
					continue;
				}
				file_tiles.get(i).image_file.length();
				ratio=1+source.getWidth()*source.getHeight()/1000000;
				
				width=(int) (source.getWidth()/ratio);
				height=(int) (source.getHeight()/ratio);
			    destination= new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			    Graphics2D g = destination.createGraphics();
			    AffineTransform at =
			    	      AffineTransform.getScaleInstance((double)width/source.getWidth(),
			    	          (double)height/source.getHeight());
			    g.drawRenderedImage(source,at);
			    
			    file_tiles.get(i).preview_icon = destination;
			    
			    if( IMP.main_panel != null)
			    	IMP.main_panel.repaint();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y,
			int width, int height) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
