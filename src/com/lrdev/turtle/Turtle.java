/* Turtle.java -- Graphics Turtle.
**
** Copyright (C) 1997-2003 Eric Laroche.  All rights reserved.
**
** @author Eric Laroche <laroche@lrdev.com>
** @version 1.3 @(#)$Id: Turtle.java,v 1.3 2003/02/16 12:37:38 laroche Exp $
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

import com.lrdev.turtle.TurtleCore; // [needs to be in the same package]

// This Turtle graphics implementation is AWT based.
import java.awt.Graphics;
import java.awt.Rectangle;

/** Turtle: Graphics Turtle.
*
* @author Eric Laroche <laroche@lrdev.com>
* @version 1.3 @(#)$Id: Turtle.java,v 1.3 2003/02/16 12:37:38 laroche Exp $
* @url http://www.lrdev.com/lr/java/javaturtle.html
* @see TurtleCore
*
* <p>
* The Graphics Turtle is a cursor that has a position
* (X and Y coordinates), a heading and a pen up/down state.
* Procedures as forward, left, etc are used as drawing idioms.
*
* <p>
* Turtle Graphics are typically known from the Logo programming
* language.  All Logo Turtle primitives are implemented, including their
* abbreviations.
*
* <p>
* Linewidth, linejoin, linecap, etc are implicit part of
* the used graphics (java.awt.Graphics) context.
* These properties can be changed while using the Turtle.
*
* <p>
* Different heading modes (units of mesurement) are provided:
* RADIANS (circle is 2 pi), DEGREE (circle is 360), GRAD (circle is 400).
*
* <p>
* com.lrdev.turtle.Turtle only offers Turtle methods with double float
* arguments; use com.lrdev.turtle.StringTurtle to allow String arguments
* and certain control structures (e.g. repeat).
*
* <p>
* Most Turtle methods return the Turtle itself, so Turtle calls can be
* stacked: forward(10).left(90);
*
* <p>
* The com.lrdev.turtle.Turtle public API is rather fat (87 methods),
* however many of the methods are abbreviations and other kind of aliases.
* The core Turtle primitives consist of 18 methods.
*
* <p>
* A set of internal Turtle functions is used too, to allow overloading
* the public API without reentering derived methods.
* See e.g. class PersistentTurtle.
*
* <p>
* Scale, translate, clip and wrap modes are implemented.  Rotate, shear
* and flip, as well as generalized matrix transformations, aren't, at
* the moment.  Use the underlying graphics (Graphics2D) modes.
*/
public class Turtle
{
	// -------- version information

	/** Version.
	*/
	private final static String m_version =
		"Version: 1.3 @(#)$Id: Turtle.java,v 1.3 2003/02/16 12:37:38 laroche Exp $";

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

	// -------- heading modes / unit of mesurement

	// Note: these constants need not be identical to the
	// com.lrdev.turtle.TurtleCore constants [which carry the same name].

	/** Radians heading mode (unit of mesurement).
	*
	* @see #DEGREE
	* @see #GRAD
	*/
	public final static int RADIANS = 1;

	/** Degree heading mode (unit of mesurement).
	*
	* @see #RADIANS
	* @see #GRAD
	*/
	public final static int DEGREE = 2;

	/** Grad heading mode (unit of mesurement).
	*
	* @see #RADIANS
	* @see #DEGREE
	*/
	public final static int GRAD = 3;

	/** Radians heading mode (unit of mesurement) alias.
	*
	* @see #RADIANS
	*/
	public final static int RAD = RADIANS;

	/** Degree heading mode (unit of mesurement) alias.
	*
	* @see #DEGREE
	*/
	public final static int DEG = DEGREE;

	// --------

	/** Core Turtle [does the main Turtle functionality].
	*
	* @since 1.3
	*/
	private TurtleCore m_turtle = null;

	/** Arguments used by the argument-less function versions.
	*/
	private double[] m_implicitArguments = {0.0, 0.0,};

	// --------

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

	/** Turtle: get a new Turtle.
	*
	* <p>
	* Scale and transformation are default (i.e. none).
	*/
	public Turtle(Graphics graphics, Rectangle bounds)
	{
		// allow null graphics and null bounds, e.g. for unit tests, etc

		m_turtle = new TurtleCore(graphics, bounds);
	}

	/** Turtle: get a new Turtle.
	*
	* <p>
	* Transformation is default (i.e. none).
	*/
	public Turtle(
		Graphics graphics,
		Rectangle bounds,
		double scaleX,
		double scaleY)
	{
		this(graphics, bounds);
		m_turtle.setScale(scaleX, scaleY);
	}

	/** Turtle: get a new Turtle.
	*/
	public Turtle(
		Graphics graphics,
		Rectangle bounds,
		double scaleX,
		double scaleY,
		double transformX,
		double transformY)
	{
		this(graphics, bounds, scaleX, scaleY);
		m_turtle.setTransform(transformX, transformY);
	}

	/** Get the Graphics object.
	*/
	public Graphics getGraphics()
	{
		return m_turtle.getGraphics();
	}

	/** Change (set/reset) scale.
	*
	* @since 1.3
	* @see PersistentTurtle
	*/
	public Turtle setScale(double scaleX, double scaleY)
	{
		m_turtle.setScale(scaleX, scaleY);
		return this;
	}

	/** Change (set/reset) scale.
	*
	* @since 1.2
	* @deprecated use setScale
	*/
	@Deprecated
	public Turtle resetScale(double scaleX, double scaleY)
	{
		return setScale(scaleX, scaleY);
	}

	/** Change (set/reset) transform.
	*
	* @since 1.3
	* @see PersistentTurtle
	*/
	public Turtle setTransform(double transformX, double transformY)
	{
		m_turtle.setTransform(transformX, transformY);
		return this;
	}

	/** Change (set/reset) transform.
	*
	* @since 1.2
	* @deprecated use setTransform
	*/
	@Deprecated
	public Turtle resetTransform(double transformX, double transformY)
	{
		return setTransform(transformX, transformY);
	}

	/** Change (set/reset) graphics and bounds.
	*
	* @since 1.3
	* @see PersistentTurtle
	*/
	public Turtle setGraphics(Graphics graphics, Rectangle bounds)
	{
		m_turtle.setGraphics(graphics, bounds);
		return this;
	}

	/** Change (set/reset) graphics and bounds.
	*
	* @since 1.2
	* @deprecated use setGraphics
	*/
	@Deprecated
	public Turtle resetGraphics(Graphics graphics, Rectangle bounds)
	{
		return setGraphics(graphics, bounds);
	}

	// -------- Logo programming language Turtle functions.

	// Note: these public core functions should not call one another,
	// to allow to be overwritten by derived classes without reentering
	// that derived API.  This should not even happen indirectly
	// (e.g. by hypothetical left --> leftInternal --> setheading).
	// This pattern is new since 1.2.

	/** Set (put) penstate in 'up' position.
	* [logo language turtle function]
	*
	* @see #pendown
	* @see #drawstate
	*/
	public Turtle penup()
	{
		m_turtle.penup();
		return this;
	}

	/** Set (put) penstate in 'down' position.
	* [logo language turtle function]
	*
	* @see #penup
	* @see #drawstate
	*/
	public Turtle pendown()
	{
		m_turtle.pendown();
		return this;
	}

	/** Advance in current direction.
	* [logo language turtle function]
	*
	* @see #back
	*/
	public Turtle forward(double distance)
	{
		m_turtle.forward(distance);
		return this;
	}

	/** Back up in current direction.
	* [logo language turtle function]
	*
	* @see #forward
	*/
	public Turtle back(double distance)
	{
		m_turtle.back(distance);
		return this;
	}

	/** Change heading (direction) to the left
	* (counterclockwise, positive direction).
	* [logo language turtle function]
	*
	* @see #right
	*
	* <p>
	* omega can be rad, deg, or grad, depending on the heading mode.
	*/
	public Turtle left(double omega)
	{
		m_turtle.left(omega);
		return this;
	}

	/** Change heading (direction) to the right
	* (clockwise, negative direction).
	* [logo language turtle function]
	*
	* @see #left
	*
	* <p>
	* omega can be rad, deg, or grad, depending on the heading mode.
	*/
	public Turtle right(double omega)
	{
		m_turtle.right(omega);
		return this;
	}

	/** Move to a specified point.
	* [logo language turtle function]
	*
	* @see #setx
	* @see #sety
	*/
	public Turtle setxy(double x, double y)
	{
		m_turtle.setxy(x, y);
		return this;
	}

	/** Move to a specified point (only X changes).
	* [logo language turtle function]
	*
	* @see #sety
	* @see #setxy
	*/
	public Turtle setx(double x)
	{
		m_turtle.setx(x);
		return this;
	}

	/** Move to a specified point (only Y changes).
	* [logo language turtle function]
	*
	* @see #setx
	* @see #setxy
	*/
	public Turtle sety(double y)
	{
		m_turtle.sety(y);
		return this;
	}

	/** Set the heading.
	* [logo language turtle function]
	*
	* @see #heading
	*
	* <p>
	* phi can be rad, deg, or grad, depending on the heading mode.
	*/
	public Turtle setheading(double phi)
	{
		m_turtle.setheading(phi);
		return this;
	}

	/** Set heading towards a point.
	* [logo language turtle function]
	*/
	public Turtle towards(double x, double y)
	{
		m_turtle.towards(x, y);
		return this;
	}

	/** Go home; heading to zero.
	* [logo language turtle function]
	*
	* @see #setxy
	*/
	public Turtle home()
	{
		m_turtle.home();
		return this;
	}

	/** Set wrapping mode.
	* [logo language turtle function]
	*
	* @since 1.3
	* @see #nowrap
	*/
	public Turtle wrap()
	{
		m_turtle.wrap();
		return this;
	}

	/** Unset wrapping mode.
	* [logo language turtle function]
	*
	* @since 1.3
	* @see #wrap
	*/
	public Turtle nowrap()
	{
		m_turtle.nowrap();
		return this;
	}

	// Logo programming language Turtle functions that return other than Turtle.

	/** Get X coordinate.
	* [logo language turtle function]
	*
	* @see #ycor
	*/
	public double xcor()
	{
		return m_turtle.xcor();
	}

	/** Get Y coordinate.
	* [logo language turtle function]
	*
	* @see #xcor
	*/
	public double ycor()
	{
		return m_turtle.ycor();
	}

	/** Get heading.
	* [logo language turtle function]
	*
	* @see #setheading
	*
	* <p>
	* The mesurement unit depends on the heading mode and can be rad, deg, or grad.
	*/
	public double heading()
	{
		return m_turtle.heading();
	}

	/** Get pen state.
	* [logo language turtle function]
	*
	* <p>
	* Note: only the turtle relevant stuff given.
	*/
	public boolean drawstate()
	{
		return m_turtle.drawstate();
	}

	// -------- Logo language Turtle function abbreviations.

	// These methods _must_ call this class' public API,
	// in order to eventually route through decorator derived classes.

	/** Abbreviation for penup.
	* [logo language turtle function abbreviation]
	*
	* @see #penup
	*/
	public Turtle pu()
	{
		return penup();
	}

	/** Abbreviation for pendown.
	* [logo language turtle function abbreviation]
	*
	* @see #pendown
	*/
	public Turtle pd()
	{
		return pendown();
	}

	/** Abbreviation for forward.
	* [logo language turtle function abbreviation]
	*
	* @see #forward
	*/
	public Turtle fd(double distance)
	{
		return forward(distance);
	}

	/** Abbreviation for back.
	* [logo language turtle function abbreviation]
	*
	* @see #back
	*/
	public Turtle bk(double distance)
	{
		return back(distance);
	}

	/** Abbreviation for left.
	* [logo language turtle function abbreviation]
	*
	* @see #left
	*/
	public Turtle lt(double omega)
	{
		return left(omega);
	}

	/** Abbreviation for right.
	* [logo language turtle function abbreviation]
	*
	* @see #right
	*/
	public Turtle rt(double omega)
	{
		return right(omega);
	}

	/** Abbreviation for setheading.
	* [logo language turtle function abbreviation]
	*
	* @see #setheading
	*/
	public Turtle seth(double phi)
	{
		return setheading(phi);
	}

	// -------- Argument-less versions of the Logo programming language Turtle functions.

	/** Advance in current direction.
	*
	* @see #back
	*
	* <p>
	* Requires distance to be set by a call to setArgument().
	*/
	public Turtle forward()
	{
		return forward(getArgument());
	}

	/** Back up in current direction.
	*
	* @see #forward
	*
	* <p>
	* Requires distance to be set by a call to setArgument().
	*/
	public Turtle back()
	{
		return back(getArgument());
	}

	/** Change heading (direction) to the left
	* (counterclockwise, positive direction).
	*
	* @see #right
	*
	* <p>
	* Requires omega to be set by a call to setArgument().
	*/
	public Turtle left()
	{
		return left(getArgument());
	}

	/** Change heading (direction) to the right
	* (clockwise, negative direction).
	*
	* @see #left
	*
	* <p>
	* Requires omega to be set by a call to setArgument().
	*/
	public Turtle right()
	{
		return right(getArgument());
	}

	/** Move to a specified point.
	*
	* @see #setx
	* @see #sety
	*
	* <p>
	* Requires X and Y to be set by calls to setArgument().
	*/
	public Turtle setxy()
	{
		// get (pop) y, then x
		double y = getArgument();
		double x = getArgument();

		return setxy(x, y);
	}

	/** Move to a specified point (only X changes).
	*
	* @see #sety
	* @see #setxy
	*
	* <p>
	* Requires X to be set by a call to setArgument().
	*/
	public Turtle setx()
	{
		return setx(getArgument());
	}

	/** Move to a specified point (only Y changes).
	*
	* @see #setx
	* @see #setxy
	*
	* <p>
	* Requires Y to be set by a call to setArgument().
	*/
	public Turtle sety()
	{
		return sety(getArgument());
	}

	/** Set the heading.
	*
	* @see #heading
	*
	* <p>
	* Requires phi to be set by a call to setArgument().
	*/
	public Turtle setheading()
	{
		return setheading(getArgument());
	}

	/** Set heading towards a point.
	*
	* <p>
	* Requires X and Y to be set by calls to setArgument().
	*/
	public Turtle towards()
	{
		// get (pop) y, then x
		double y = getArgument();
		double x = getArgument();

		return towards(x, y);
	}

	// -------- Argument-less versions of the Logo language Turtle function abbreviations.

	/** Abbreviation for forward.
	*
	* @see #forward
	*/
	public Turtle fd()
	{
		return forward();
	}

	/** Abbreviation for back.
	*
	* @see #back
	*/
	public Turtle bk()
	{
		return back();
	}

	/** Abbreviation for left.
	*
	* @see #left
	*/
	public Turtle lt()
	{
		return left();
	}

	/** Abbreviation for right.
	*
	* @see #right
	*/
	public Turtle rt()
	{
		return right();
	}

	/** Abbreviation for setheading.
	*
	* @see #setheading
	*/
	public Turtle seth()
	{
		return setheading();
	}

	// -------- Java style naming for the Turtle functions.

	/** Java style name for penup.
	*
	* @see #penup
	*/
	public Turtle penUp()
	{
		return penup();
	}

	/** Java style name for pendown.
	*
	* @see #pendown
	*/
	public Turtle penDown()
	{
		return pendown();
	}

	/** Java style name for setxy.
	*
	* @see #setxy
	*/
	public Turtle setXY(double x, double y)
	{
		return setxy(x, y);
	}

	/** Java style name for setxy.
	*
	* @see #setxy
	*/
	public Turtle setXY()
	{
		return setxy();
	}

	/** Java style name for setx.
	*
	* @see #setx
	*/
	public Turtle setX(double x)
	{
		return setx(x);
	}

	/** Java style name for setx.
	*
	* @see #setx
	*/
	public Turtle setX()
	{
		return setx();
	}

	/** Java style name for sety.
	*
	* @see #sety
	*/
	public Turtle setY(double y)
	{
		return sety(y);
	}

	/** Java style name for sety.
	*
	* @see #sety
	*/
	public Turtle setY()
	{
		return sety();
	}

	/** Java style name for setheading.
	*
	* @see #setheading
	*/
	public Turtle setHeading(double phi)
	{
		return setheading(phi);
	}

	/** Java style name for setheading.
	*
	* @see #setheading
	*/
	public Turtle setHeading()
	{
		return setheading();
	}

	/** Java style name for wrap.
	*
	* @see #wrap
	*/
	public Turtle setWrap()
	{
		return wrap();
	}

	/** Java style name for nowrap.
	*
	* @see #nowrap
	*/
	public Turtle noWrap()
	{
		return nowrap();
	}

	/** Java style name for nowrap.
	*
	* @see #nowrap
	*/
	public Turtle setNoWrap()
	{
		return nowrap();
	}

	/** Java style name for xcor.
	*
	* @see #xcor
	*/
	public double xCor()
	{
		return xcor();
	}

	/** Java style name for xcor.
	*
	* @see #xcor
	*/
	public double xCoordinate()
	{
		return xcor();
	}

	/** Java style name for xcor.
	*
	* @see #xcor
	*/
	public double getXCoordinate()
	{
		return xcor();
	}

	/** Java style name for ycor.
	*
	* @see #ycor
	*/
	public double yCor()
	{
		return ycor();
	}

	/** Java style name for ycor.
	*
	* @see #ycor
	*/
	public double yCoordinate()
	{
		return ycor();
	}

	/** Java style name for ycor.
	*
	* @see #ycor
	*/
	public double getYCoordinate()
	{
		return ycor();
	}

	/** Java style name for heading.
	*
	* @see #heading
	*/
	public double getHeading()
	{
		return heading();
	}

	/** Java style name for drawstate.
	*
	* @see #drawstate
	*/
	public boolean drawState()
	{
		return drawstate();
	}

	/** Java style name for drawstate.
	*
	* @see #drawstate
	*/
	public boolean getDrawState()
	{
		return drawstate();
	}

	// --------

	/** Set heading mode.
	*
	* @see #getHeadingMode
	* @see #RADIANS
	* @see #DEGREE
	* @see #GRAD
	*/
	public Turtle setHeadingMode(int mode)
	{
		return setHeadingModeInternal(mode);
	}

	/** Set heading mode.
	*
	* @since 1.2
	* @see #setHeadingMode
	*/
	private Turtle setHeadingModeInternal(int mode)
	{
		switch (mode) {
		case RADIANS :
			m_turtle.setHeadingMode(TurtleCore.RADIANS);
			break;
		case DEGREE :
			m_turtle.setHeadingMode(TurtleCore.DEGREE);
			break;
		case GRAD :
			m_turtle.setHeadingMode(TurtleCore.GRAD);
			break;
		}

		// Note: does not throw on illegal mode,
		// just ignores it.

		return this;
	}

	/** Get heading mode.
	*
	* @see #setHeadingMode
	* @see #RADIANS
	* @see #DEGREE
	* @see #GRAD
	*/
	public int getHeadingMode()
	{
		switch (m_turtle.getHeadingMode()) {
//		case m_turtle.RADIANS :
//			return RADIANS;
//		case m_turtle.DEGREE :
//			return DEGREE;
//		case m_turtle.GRAD :
//			return GRAD;
		}

		// [don't throw]
		return RADIANS;
	}

	/** Set radians heading mode.
	*
	* @see #RADIANS
	*/
	public Turtle radians()
	{
		return setHeadingModeInternal(RADIANS);
	}

	/** Set degree heading mode.
	*
	* @see #DEGREE
	*/
	public Turtle degree()
	{
		return setHeadingModeInternal(DEGREE);
	}

	/** Set grad heading mode.
	*
	* @see #GRAD
	*/
	public Turtle grad()
	{
		return setHeadingModeInternal(GRAD);
	}

	/** Abbreviation for radians.
	*
	* @see #radians
	*/
	public Turtle rad()
	{
		return radians();
	}

	/** Abbreviation for degree.
	*
	* @see #degree
	*/
	public Turtle deg()
	{
		return degree();
	}

	/** Set clipping.
	*
	* @since 1.3
	* @see #getClipping
	* @see #setWrapping
	* @see #getWrapping
	*/
	public Turtle setClipping(
		double lowerX,
		double lowerY,
		double upperX,
		double upperY)
	{
		m_turtle.setClipping(lowerX, lowerY, upperX, upperY);
		return this;
	}

	/** Get clipping as {lowerX, lowerY, upperX, upperY} or null, if not
	* given.
	*
	* @since 1.3
	* @see #setClipping
	* @see #setWrapping
	* @see #getWrapping
	*/
	public double[] getClipping()
	{
		return m_turtle.getClipping();
	}

	/** Set wrapping.
	*
	* @since 1.3
	* @see #getWrapping
	* @see #setWrappingRange
	* @see #setClipping
	* @see #getClipping
	*/
	public Turtle setWrapping(
		double lowerX,
		double lowerY,
		double upperX,
		double upperY)
	{
		m_turtle.setWrapping(lowerX, lowerY, upperX, upperY);
		return this;
	}

	/** Get wrapping as {lowerX, lowerY, upperX, upperY} or null, if not
	* given.
	*
	* @since 1.3
	* @see #setWrapping
	* @see #getWrappingRange
	* @see #setClipping
	* @see #getClipping
	*/
	public double[] getWrapping()
	{
		return m_turtle.getWrapping();
	}

	/** Set wrapping range.
	*
	* @since 1.3
	* @see #getWrappingRange
	* @see #setWrapping
	*/
	public Turtle setWrappingRange(
		double lowerX,
		double lowerY,
		double upperX,
		double upperY)
	{
		m_turtle.setWrappingRange(lowerX, lowerY, upperX, upperY);
		return this;
	}

	/** Get wrapping range as {lowerX, lowerY, upperX, upperY}.
	*
	* @since 1.3
	* @see #setWrappingRange
	* @see #getWrapping
	*/
	public double[] getWrappingRange()
	{
		return m_turtle.getWrappingRange();
	}

	/** Copy an array of doubles.
	* Used to suppress side effects on returned data.
	*/
	public static double[] arrayCopy(double[] a)
	{
		return TurtleCore.arrayCopy(a);
	}

	// --------

	/** Set an implicit argument.
	*
	* @see #getArgument
	*
	* <p>
	* Does not check for stack overflow,
	* earlier arguments just drop out.
	*/
	public double setArgument(double argument)
	{
		// roll stack up
		for (int i = m_implicitArguments.length - 1; i > 0; i--) {
			m_implicitArguments[i] = m_implicitArguments[i - 1];
		}

		// push argument
		m_implicitArguments[0] = argument;

		return argument;
	}

	/** Get an implicit argument.
	*
	* @see #setArgument
	*
	* <p>
	* Does not check for stack underflow,
	* default arguments (0.0) are just returned.
	* This behavior typically makes the stack usage more tolerant.
	*/
	private double getArgument()
	{
		// pop argument
		double argument = m_implicitArguments[0];

		// roll stack down
		for (int i = 0; i < m_implicitArguments.length - 1; i++) {
			m_implicitArguments[i] = m_implicitArguments[i + 1];
		}

		// fill with default (zero)
		m_implicitArguments[m_implicitArguments.length - 1] = 0.0;

		return argument;
	}
}

