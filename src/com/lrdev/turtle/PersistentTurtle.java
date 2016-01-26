/* PersistentTurtle.java -- Graphics Turtle.
**
** Copyright (C) 1997-2003 Eric Laroche.  All rights reserved.
**
** @author Eric Laroche <laroche@lrdev.com>
** @version 1.2 @(#)$Id: PersistentTurtle.java,v 1.2 2003/02/16 12:36:54 laroche Exp $
**
** This program is free software;
** you can redistribute it and/or modify it.
**
** This program is distributed in the hope that it will be useful,
** but WITHOUT ANY WARRANTY; without even the implied warranty of
** MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
**
*/

package com.lrdev.turtle;

// note: no wildcard imports

import com.lrdev.turtle.Turtle;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Vector;

/** PersistentTurtle: Graphics Turtle.
*
* @author Eric Laroche <laroche@lrdev.com>
* @version 1.2 @(#)$Id: PersistentTurtle.java,v 1.2 2003/02/16 12:36:54 laroche Exp $
* @url http://www.lrdev.com/lr/java/javaturtle.html
*
* <p>
* PersistentTurtle is based on com.lrdev.turtle.Turtle.
*/
public class PersistentTurtle
	extends Turtle
{
	/** Version.
	*/
	private final static String m_version =
		"Version: 1.2 @(#)$Id: PersistentTurtle.java,v 1.2 2003/02/16 12:36:54 laroche Exp $";

	/** Author.
	*/
	private final static String m_author =
		"Author: Eric Laroche <laroche@lrdev.com>";

	/** Copyright.
	*/
	private final static String m_copyright =
		"Copyright (C) 1997-2003 Eric Laroche.  All rights reserved.";

	// note: have this disclaimer as one string literal,
	// i.e. no '+' string concatenation (C string concatenation not supported by Java).
	/** Disclaimer.
	*/
	private final static String m_disclaimer =
		"This program is distributed in the hope that it will be useful,\nbut WITHOUT ANY WARRANTY; without even the implied warranty of\nMERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.";

	// Constants for commands.

	// note: can't have function pointers, neither want the overhead of
	// an additional set of classes

	private final static int COMMAND_UNDEFINED = 0;
	private final static int COMMAND_PENUP = 1;
	private final static int COMMAND_PENDOWN = 2;
	private final static int COMMAND_FORWARD = 3;
	private final static int COMMAND_BACK = 4;
	private final static int COMMAND_LEFT = 5;
	private final static int COMMAND_RIGHT = 6;
	private final static int COMMAND_SETXY = 7;
	private final static int COMMAND_SETX = 8;
	private final static int COMMAND_SETY = 9;
	private final static int COMMAND_SETHEADING = 10;
	private final static int COMMAND_TOWARDS = 11;
	private final static int COMMAND_HOME = 12;

	/** Registered commands.
	*
	* <p>
	* The Vector's default size increment strategy (doubling)
	* should be ok for us.
	*/
	private Vector m_commands = new Vector();

	/** Maximal and minimal X and Y coordinates.
	*
	* <p>
	* minX, minY, maxX, maxY
	*
	* <p>
	* Used to calculate extents, scaling, etc.
	*/
	private double[] m_minMaxXY = null;

	/** Draw centered.
	*/
	private boolean m_center = false;

	/** Consider pen up/down state for extents.
	*
	* @since 1.2
	* @see #updateExtents
	* @see #m_center
	*
	* <p>
	* Default is true, to have m_center as useful as can be.
	*/
	private boolean m_considerPenState = true;

	/** Consider possible clipping and wrapping for extents.
	*
	* @since 1.2
	* @see #updateExtents
	* @see #m_center
	*
	* <p>
	* Default is true, to have m_center as useful as can be.
	*/
	private boolean m_considerClippingWrapping = true;

	/** Return source information.
	* This includes version with date, author with email address,
	* copyright and disclaimer.
	*/
	public static String getInfo()
	{
		return
			m_version + "\n" +
			m_author + "\n" +
			m_copyright + "\n\n" +
			m_disclaimer + "\n";
	}

	// Note: we only offer a constructor with unspecified Graphics,
	// to make the difference to the non-persistent Turtle evident.

	/** PersistentTurtle: get a new PersistentTurtle.
	*/
	public PersistentTurtle()
	{
		// unspecified Graphics, etc in base Turtle
		super(null, null);

		// invalidate starting coordinates
		m_minMaxXY = null;
	}

	// -------- Overwritten Turtle functions.

	// only calling updateExtents() after actions that moved the Turtle

	/** Set (put) penstate in 'up' position.
	* Overwrites Turtle.penup.
	*/
	@Override
	public Turtle penup()
	{
		registerCommand(new Integer(COMMAND_PENUP));

		return super.penup();
	}

	/** Set (put) penstate in 'down' position.
	* Overwrites Turtle.pendown.
	*/
	@Override
	public Turtle pendown()
	{
		registerCommand(new Integer(COMMAND_PENDOWN));

		Turtle t = super.pendown();

		// Note: we're not updating extents (i.e call updateExtents())
		// at this point.  Jumping around with pen up and just do
		// pendown/penup pairs will not update the extents.

		return t;
	}

	/** Advance in current direction.
	* Overwrites Turtle.forward.
	*/
	@Override
	public Turtle forward(double distance)
	{
		registerCommand(new Integer(COMMAND_FORWARD));
		registerCommand(new Double(distance));

		updateExtents();

		Turtle t = super.forward(distance);

		updateExtents();

		return t;
	}

	/** Back up in current direction.
	* Overwrites Turtle.back.
	*/
	@Override
	public Turtle back(double distance)
	{
		registerCommand(new Integer(COMMAND_BACK));
		registerCommand(new Double(distance));

		updateExtents();

		Turtle t = super.back(distance);

		updateExtents();

		return t;
	}

	/** Change heading (direction) to the left
	* Overwrites Turtle.left.
	*/
	@Override
	public Turtle left(double omega)
	{
		registerCommand(new Integer(COMMAND_LEFT));
		registerCommand(new Double(omega));

		// note: updateExtents() not needed

		return super.left(omega);
	}

	/** Change heading (direction) to the right
	* Overwrites Turtle.right.
	*/
	@Override
	public Turtle right(double omega)
	{
		registerCommand(new Integer(COMMAND_RIGHT));
		registerCommand(new Double(omega));

		// note: updateExtents() not needed

		return super.right(omega);
	}

	/** Move to a specified point.
	* Overwrites Turtle.setxy.
	*/
	@Override
	public Turtle setxy(double x, double y)
	{
		registerCommand(new Integer(COMMAND_SETXY));
		registerCommand(new Double(x));
		registerCommand(new Double(y));

		updateExtents();

		Turtle t = super.setxy(x, y);

		updateExtents();

		return t;
	}

	/** Move to a specified point (only X changes).
	* Overwrites Turtle.setx.
	*/
	@Override
	public Turtle setx(double x)
	{
		registerCommand(new Integer(COMMAND_SETX));
		registerCommand(new Double(x));

		updateExtents();

		Turtle t = super.setx(x);

		updateExtents();

		return t;
	}

	/** Move to a specified point (only Y changes).
	* Overwrites Turtle.sety.
	*/
	@Override
	public Turtle sety(double y)
	{
		registerCommand(new Integer(COMMAND_SETY));
		registerCommand(new Double(y));

		updateExtents();

		Turtle t = super.sety(y);

		updateExtents();

		return t;
	}

	/** Set the heading.
	* Overwrites Turtle.setheading.
	*/
	@Override
	public Turtle setheading(double phi)
	{
		registerCommand(new Integer(COMMAND_SETHEADING));
		registerCommand(new Double(phi));

		// note: updateExtents() not needed

		return super.setheading(phi);
	}

	/** Set heading towards a point.
	* Overwrites Turtle.towards.
	*/
	@Override
	public Turtle towards(double x, double y)
	{
		registerCommand(new Integer(COMMAND_TOWARDS));
		registerCommand(new Double(x));
		registerCommand(new Double(y));

		// note: updateExtents() not needed

		return super.towards(x, y);
	}

	/** Go home; heading to zero.
	* Overwrites Turtle.home.
	*/
	@Override
	public Turtle home()
	{
		registerCommand(new Integer(COMMAND_HOME));

		updateExtents();

		Turtle t = super.home();

		// note: super.home() did not change pen state
		// [if pen now was up, updateExtents() would have to be skipped]
		updateExtents();

		return t;
	}

	// note: wrap() and nowrap() not overwritten!

	// note; abbreviated versions not needed to be overwritten.
	// neither are the argument-less versions

	// --------

	/** Register a Turtle command or its argument.
	*/
	private void registerCommand(Object what)
	{
		m_commands.addElement(what);
	}

	/** Undo Turtle command.
	*
	* <p>
	* Already drawn things will not be erased (since that might already
	* overlap with other lines).  An invaldate()/repaint()/etc message
	* will additionally be required.
	*/
	public Turtle undo()
	{
		return undo(1);
	}

	/** Undo Turtle commands.
	*
	* <p>
	* Already drawn things will not be erased (since that might already
	* overlap with other lines).  An invaldate()/repaint()/etc message
	* will additionally be required.
	*
	* <p>
	* Missing feature: As recalculateExtents() is not implemented, the
	* extents won't be adjusted after an undo operation.
	*/
	public Turtle undo(int n)
	{
		int size = m_commands.size();

		try {
			for (int i = 0; i < n; i++) {
				// skip (double type) arguments
				while (m_commands.elementAt(size - 1) instanceof Double) {
					size--;
				}
				size--;
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			// just reset
			return reset();
		}

		m_commands.setSize(size);

		// recalculateExtents();

		return this;
	}

	/** Reset Turtle commands.
	*
	* <p>
	* Already drawn things will not be erased (since that might already
	* overlap with other lines).  An invaldate()/repaint()/etc message
	* will additionally be required.
	*/
	public Turtle reset()
	{
		m_commands.setSize(0);

		// invalidate starting coordinates
		m_minMaxXY = null;

		// recalculateExtents();

		// reset scale and transformation
		setScale(1.0, 1.0);
		setTransform(0.0, 0.0);

		return this;
	}

	/** Run the registered Turtle commands.
	*/
	public Turtle run()
	{
		// go home first
		// note: can't stack these calls since we're working around polymorphism here
		super.penup();
		super.home();
		super.pendown();

		try {

			// do the registered command list
			for (int i = 0; i < m_commands.size(); i++) {

				switch (((Integer)m_commands.elementAt(i)).intValue()) {
				case COMMAND_PENUP :
					super.penup();
					break;
				case COMMAND_PENDOWN :
					super.pendown();
					break;
				case COMMAND_FORWARD :
					super.forward(((Double)m_commands.elementAt(i + 1)).doubleValue());
					i += 1;
					break;
				case COMMAND_BACK :
					super.back(((Double)m_commands.elementAt(i + 1)).doubleValue());
					i += 1;
					break;
				case COMMAND_LEFT :
					super.left(((Double)m_commands.elementAt(i + 1)).doubleValue());
					i += 1;
					break;
				case COMMAND_RIGHT :
					super.right(((Double)m_commands.elementAt(i + 1)).doubleValue());
					i += 1;
					break;
				case COMMAND_SETXY :
					super.setxy(
						((Double)m_commands.elementAt(i + 1)).doubleValue(),
						((Double)m_commands.elementAt(i + 2)).doubleValue());
					i += 2;
					break;
				case COMMAND_SETX :
					super.setx(((Double)m_commands.elementAt(i + 1)).doubleValue());
					i += 1;
					break;
				case COMMAND_SETY :
					super.sety(((Double)m_commands.elementAt(i + 1)).doubleValue());
					i += 1;
					break;
				case COMMAND_SETHEADING :
					super.setheading(((Double)m_commands.elementAt(i + 1)).doubleValue());
					i += 1;
					break;
				case COMMAND_TOWARDS :
					super.towards(
						((Double)m_commands.elementAt(i + 1)).doubleValue(),
						((Double)m_commands.elementAt(i + 2)).doubleValue());
					i += 2;
					break;
				case COMMAND_HOME :
					super.home();
					break;
				}
			}

		} catch (ArrayIndexOutOfBoundsException eb) {
			throw new RuntimeException("number of command arguments mismatch");
		} catch (ClassCastException ec) {
			throw new RuntimeException("type of command arguments mismatch");
		}

		return this;
	}

	// --------

	/** Update the maxX/minX/maxY/minY fields.
	*/
	private void updateExtents()
	{
		if (m_considerPenState) {
			// ignore new coordinate if pen is up
			if (!super.drawstate()) {
				return;
			}
		}

		double x = xcor();
		double y = ycor();

		if (m_minMaxXY == null) {
			m_minMaxXY = new double[4];
			m_minMaxXY[0] = x; // m_minX
			m_minMaxXY[1] = y; // m_minY
			m_minMaxXY[2] = x; // m_maxX
			m_minMaxXY[3] = y; // m_maxY
		} else {
			m_minMaxXY[0] = Math.min(m_minMaxXY[0], x); // m_minX
			m_minMaxXY[1] = Math.min(m_minMaxXY[1], y); // m_minY
			m_minMaxXY[2] = Math.max(m_minMaxXY[2], x); // m_maxX
			m_minMaxXY[3] = Math.max(m_minMaxXY[3], y); // m_maxY
		}
	}

	/** Return raw extents as {m_minX, m_minY, m_maxX, m_maxY}, or null
	* if not given.
	*/
	private double[] getRawMinMax()
	{
		if (m_minMaxXY == null) {
			return null;
		}

		// copying the array of doubles also serves the purpose of protecting
		// it from being modifyed by the caller

		return arrayCopy(m_minMaxXY);
	}

	/** Return extents as {m_minX, m_minY, m_maxX, m_maxY}, or null if
	* not given.
	*/
	public double[] getMinMax()
	{
		double[] minMax = getRawMinMax();

		// if we don't have any extents yet, return nothing
		if (minMax == null) {
			return null;
		}

		// return plain data if not considering clipping or wrapping
		if (!m_considerClippingWrapping) {
			return minMax;
		}

		// return the wrapping region if wrapping
		double[] wrapping = super.getWrapping();
		if (wrapping != null) {
			return wrapping;
		}

		// apply cliping then
		double[] clipping = super.getClipping();

		// no clipping: return plain data
		if (clipping == null) {
			return minMax;
		}

		double minX = minMax[0];
		double minY = minMax[1];
		double maxX = minMax[2];
		double maxY = minMax[3];

		if (minX > clipping[2]) { // upperX
			minX = clipping[2]; // upperX
		} else if (minX < clipping[0]) { // lowerX
			minX = clipping[0]; // lowerX
		}

		if (minY > clipping[3]) { // upperY
			minY = clipping[3]; // upperY
		} else if (minY < clipping[1]) { // lowerY
			minY = clipping[1]; // lowerY
		}

		if (maxX > clipping[2]) { // upperX
			maxX = clipping[2]; // upperX
		} else if (maxX < clipping[0]) { // lowerX
			maxX = clipping[0]; // lowerX
		}

		if (maxY > clipping[3]) { // upperY
			maxY = clipping[3]; // upperY
		} else if (maxY < clipping[1]) { // lowerY
			maxY = clipping[1]; // lowerY
		}

		// return a copy
		minMax = new double[4];
		minMax[0] = minX;
		minMax[1] = minY;
		minMax[2] = maxX;
		minMax[3] = maxY;

		return minMax;
	}

	/** Return extent as {extentX, extentY}, or null if not given.
	*/
	public double[] getExtent()
	{
		double[] minMax = getMinMax();
		if (minMax == null) {
			return null;
		}

		double extentX = minMax[2] - minMax[0]; // maxX, minX
		double extentY = minMax[3] - minMax[1]; // maxY, minY

		double[] extent = new double[2];
		extent[0] = extentX;
		extent[1] = extentY;

		return extent;
	}

	/** Return pivot as {pivotX, pivotY} or null, if not given.
	*/
	public double[] getPivot()
	{
		double[] minMax = getMinMax();
		if (minMax == null) {
			return null;
		}

		double pivotX = (minMax[2] + minMax[0]) / 2; // maxX, minX
		double pivotY = (minMax[3] + minMax[1]) / 2; // maxY, minY

		double[] pivot = new double[2];
		pivot[0] = pivotX;
		pivot[1] = pivotY;

		return pivot;
	}

	/** Draw centered.
	*/
	public Turtle center()
	{
		return center(true);
	}

	/** Draw uncentered.
	*/
	public Turtle uncenter()
	{
		return center(false);
	}

	/** Set centered mode.
	*/
	private Turtle center(boolean how)
	{
		m_center = how;
		return this;
	}

	/** Paint.
	*/
	public void paint(Graphics graphics, Rectangle bounds)
	{
		if (m_center) {
			double scaleX, scaleY, transformX, transformY;

			// defaults [none]
			scaleX = scaleY = 1.0;
			transformX = transformY = 0.0;

			// get extent as {extentX, extentY}
			double[] extent = getExtent();
			if (extent != null) {
				double scale;
				if (extent[0] > 0.0 || extent[1] > 0.0) { // extentX, extentY
					if (extent[0] > 0.0 && extent[1] > 0.0) { // extentX, extentY
						scale = Math.min(
							bounds.width / extent[0], // extentX
							bounds.height / extent[1]); // extentY
					} else if (extent[0] > 0.0) { // extentX, extentY
						scale = bounds.width / extent[0]; // extentX
					} else {
						scale = bounds.height / extent[1]; // extentY
					}
					scaleX = scaleY = scale;
				}
			}

			// get pivot as {pivotX, pivotY}
			double[] pivot = getPivot();
			if (pivot != null) {
				transformX = -pivot[0] * scaleX; // pivotX
				transformY = -pivot[1] * scaleY; // pivotY
			}

			// set scale
			setScale(scaleX, scaleY);
			setTransform(transformX, transformY);
		}

		setGraphics(graphics, bounds);

		run();

		// reset (invalidate) graphics
		setGraphics(null, null);
	}
}

