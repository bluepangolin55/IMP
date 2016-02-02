package functionality;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Set;

/**
 * Created by dimitri on 27/01/16.
 * A shape efficiently stores the information of an image, that is not rectangular.
 * It can be used for example to represent image layers that only cover parts of the
 * full image or to save the parts of an image that have been changed during an
 * operations, so that they can be undone.
 * The History class is implemented as a stack of shapes.
 *
 * Implementation:
 * A shape is implemented as a vector that stores all pixels in
 * left-to-right, top-to-bottom order, a vector that stores all the rows
 * that contain pixels and a vector that stores the start end end points of
 * pixel arrays for each column.
 *
 * A shape can be constructed using a selection and an image.
 */
public class Shape {

	private int[] data;
	private int[] rows;
	private int[] columns;

	public Shape(BufferedImage image, SelectionMap selection){
	}

}
