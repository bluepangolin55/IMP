package functionality;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

/**
 * Created by dimitri on 27/01/16.
 *
 * A SelectionMap represents a selection using a binary map. Every pixel
 * is either selected or not depending on its value in the map.
 */

public class SelectionMap {
	public int[][] data;
	public int offsetX;
	public int offsetY;
	public int width;
	public int height;

	public SelectionMap(Polygon polygon){
		Rectangle rect = polygon.getBounds();
		offsetX = rect.x;
		offsetY = rect.y;
		width = rect.width;
		height = rect.height;
		data = new int[width][height];
		for(int x = 0; x<width; x++){
			for(int y = 0; y<height; y++){
				if(polygon.contains(offsetX + x, offsetY + y)){
					data[x][y] = 255;
				}
				else{
					data[x][y] = 0;
				}
			}
		}
	}
}
