/* TurtleCore.java -- Graphics Turtle core functions.
**
** Copyright (C) 1997-2003 Eric Laroche.  All rights reserved.
**
** @author Eric Laroche <laroche@lrdev.com>
** @version 1.1 @(#)$Id: TurtleCore.java,v 1.1 2003/02/16 12:37:46 laroche Exp $
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

// This Turtle graphics implementation is AWT based.
import java.awt.Graphics;
import java.awt.Rectangle;

// TurtleCore is package-accessible, i.e. _not_ public.

/** TurtleCore: Graphics Turtle core functions.
*
* @author Eric Laroche <laroche@lrdev.com>
* @version 1.1 @(#)$Id: TurtleCore.java,v 1.1 2003/02/16 12:37:46 laroche Exp $
* @url http://www.lrdev.com/lr/java/javaturtle.html
* @see Turtle
*
* <p>
* Coordinates and ranges are represented as arrays (size two or four) of
* double floating point values.  The orders are: X before Y, lower
* before higher; X/Y have precedence over lower/higher.  The use of
* these arrays is typically commented, to make it more readable.
*
* <p>
* Scale, translate, clip and wrap modes are implemented.  Rotate, shear
* and flip, as well as generalized matrix transformations, aren't, at
* the moment.  Use the underlying graphics (Graphics2D) modes.
*
* <p>
* General API differences to com.lrdev.turtle.Turtle: TurtleCore does
* not return 'this' on most methods; it does not provide abbreviations
* (it does not even have to use the Logo language function names [see
* com.lrdev.turtle.Turtle for these names], only their semantics).
*/
class TurtleCore
{
	// -------- version information

	/** Version.
	*/
	private final static String m_version =
		"Version: 1.1 @(#)$Id: TurtleCore.java,v 1.1 2003/02/16 12:37:46 laroche Exp $";

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

	/** Radians heading mode (unit of mesurement).
	*
	* @see #DEGREE
	* @see #GRAD
	*/
	final static int RADIANS = 1;

	/** Degree heading mode (unit of mesurement).
	*
	* @see #RADIANS
	* @see #GRAD
	*/
	final static int DEGREE = 2;

	/** Grad heading mode (unit of mesurement).
	*
	* @see #RADIANS
	* @see #DEGREE
	*/
	final static int GRAD = 3;

	// --------

	/** Graphics context to work on.
	*
	* @see #getGraphics
	*
	* <p>
	* m_graphics can be null, in which case the draw operations are no-ops.
	*/
	private Graphics m_graphics = null;

	/** Bounding rectangle.
	*
	* <p>
	* m_bounds can be null, in which case the draw operations are no-ops.
	*/
	private Rectangle m_bounds = null;

	/** Scaling.
	* Represented as {scaleX, scaleY}.
	*
	* <p>
	* Default is {1.0, 1.0} (means no scaling).
	*/
	private double[] m_scale = {1.0, 1.0};

	/** Transforming.
	* Represented as {transformX, transformY}.
	*
	* <p>
	* Default is {0.0, 0.0} (means no transforming).
	*/
	private double[] m_transform = {0.0, 0.0};

	/** Clipping.
	*
	* @see #m_clip
	* @see #setClipping
	* @see #getClipping
	*
	* <p>
	* Default is false.
	*/
	private boolean m_clipping = false;

	/** Clipping range.
	* Represented as {lowerX, lowerY, upperX, upperY}.
	*
	* @see #m_clipping
	*
	* <p>
	* m_clip is only considered if m_clipping is true.
	*
	* <p>
	* Default is {-1.0, -1.0, 1.0, 1.0}.
	*/
	private double[] m_clip = {-1.0, -1.0, 1.0, 1.0};

	/** Wrapping.
	*
	* @see #m_wrap
	* @see #setWrapping
	* @see #getWrapping
	*
	* <p>
	* Default is false.
	*/
	private boolean m_wrapping = false;

	/** Wrapping range.
	* Represented as {lowerX, lowerY, upperX, upperY}.
	*
	* @see #m_wrapping
	*
	* <p>
	* m_wrap is only considered if m_wrapping is true.
	*
	* <p>
	* Default is {-1.0, -1.0, 1.0, 1.0}.
	*
	* <p>
	* Note: m_wrap must not be reset, since it is reused with wrap()
	* calls.
	*/
	private double[] m_wrap = {-1.0, -1.0, 1.0, 1.0};

	/** Wrapping-adjusts-coordinates flag.
	* If not set, only the output device experiences wrapping.
	* m_wrappingAdjustsCoordinates affects the behavior of Turtle
	* commands that use absolute coordinates (e.g. setxy, setx, sety,
	* towards, home).
	*/
	private boolean m_wrappingAdjustsCoordinates = true;

	// The coordinates and the heading have to be
	// more exact than the underlying screen resolution
	// to omit intermediate rounding errors.

	/** Current coordinate.
	* Represented as {currentX, currentY}.
	*
	* @see #xcor
	* @see #ycor
	*
	* <p>
	* Starting coordinate is {0.0, 0.0}.
	*/
	private double[] m_current = {0.0, 0.0};

	/** Heading.
	*
	* @see #heading
	* @see #setheading
	* @see #m_headingMode
	*
	* <p>
	* Zero heading means in X direction (i.e. to the right).
	*
	* <p>
	* Starting heading is 0.0, i.e. to the right.
	*
	* <p>
	* Different heading modes (units of mesurement) are provided
	* (functions heading, left, right, setheading):
	* RADIANS (circle is 2 pi), DEGREE (circle is 360), GRAD (circle is 400).
	* Default heading mode is RADIANS.
	* The internal heading mode is always radians.
	*/
	private double m_heading = 0.0;

	/** Heading mode / unit of measurement.
	*
	* @see #setHeadingMode
	* @see #getHeadingMode
	*
	* <p>
	* Default is RADIANS.
	*/
	private int m_headingMode = RADIANS;

	/** Pen state.
	*
	* @see #penup
	* @see #pendown
	* @see #drawstate
	*
	* <p>
	* Default is 'down' (true).
	*/
	private boolean m_penDown = true;

	// --------

	/** Return source information.
	* This includes version with date, author with email address,
	* copyright and disclaimer.
	*/
	static String getInfo()
	{
		return
			m_version + "\n" +
			m_author + "\n" +
			m_copyright + "\n\n" +
			m_disclaimer + "\n";
	}

	/** TurtleCore: get a new TurtleCore.
	*
	* <p>
	* Scale and transformation are default (i.e. none).
	*/
	TurtleCore(Graphics graphics, Rectangle bounds)
	{
		// allow null graphics here, e.g. for unit tests, etc
		m_graphics = graphics;

		// allow null bounds here too
		m_bounds = bounds;
	}

	/** Get the Graphics object.
	*
	* @see #m_graphics
	*/
	Graphics getGraphics()
	{
		return m_graphics;
	}

	/** Change (set/reset) scale.
	*
	* @see #m_scale
	* @see PersistentTurtle
	*/
	void setScale(double scaleX, double scaleY)
	{
		m_scale[0] = scaleX; // scaleX
		m_scale[1] = scaleY; // scaleY
	}

	/** Change (set/reset) transform.
	*
	* @see #m_transform
	* @see PersistentTurtle
	*/
	void setTransform(double transformX, double transformY)
	{
		m_transform[0] = transformX; // transformX
		m_transform[1] = transformY; // transformY
	}

	/** Change (set/reset) graphics and bounds.
	*
	* @see #m_graphics
	* @see #m_bounds
	* @see PersistentTurtle
	*/
	void setGraphics(Graphics graphics, Rectangle bounds)
	{
		m_graphics = graphics;
		m_bounds = bounds;
	}

	// --------

	// Note: these (package-public) functions may call one another,
	// unlike in the com.lrdev.turtle.Turtle case.

	/** Set (put) penstate in 'up' position.
	*
	* @see #pendown
	* @see #drawstate
	*/
	void penup()
	{
		m_penDown = false;
	}

	/** Set (put) penstate in 'down' position.
	*
	* @see #penup
	* @see #drawstate
	*/
	void pendown()
	{
		m_penDown = true;
	}

	/** Advance in current direction.
	*
	* @see #back
	*/
	void forward(double distance)
	{
		// note: can't have sin and cos in one call?

		penMoveToRelative(
			distance * Math.cos(m_heading),
			distance * Math.sin(m_heading));
	}

	/** Back up in current direction.
	* [logo language turtle function]
	*
	* @see #forward
	*/
	void back(double distance)
	{
		forward(-distance);
	}

	/** Move pen relative.
	*
	* @see #penMoveTo
	*/
	private void penMoveToRelative(double dX, double dY)
	{
		penMoveTo(
			m_current[0] + dX, // currentX
			m_current[1] + dY); // currentY
	}

	/** Move pen absolute.
	*
	* @see #penMoveToRelative
	*/
	private void penMoveTo(double x, double y)
	{
		// draw if the pen is down
		if (m_penDown) {
			draw(m_current[0], m_current[1], x, y); // currentX, currentY
		}

		// update Turtle cursor
		m_current[0] = x; // currentX
		m_current[1] = y; // currentY

		// consider wrapping
		if (m_wrappingAdjustsCoordinates) {
			wrappingAdjust();
		}
	}

	/** Adjust coordinates in context to wrapping.
	*/
	private void wrappingAdjust()
	{
		if (!m_wrapping) {
			return;
		}

		double dX = m_wrap[2] - m_wrap[0]; // upperX, lowerX
		double dY = m_wrap[3] - m_wrap[1]; // upperY, lowerY

		while (m_current[0] > m_wrap[2]) { // upperX
			m_current[0] -= dX;
		}

		while (m_current[0] < m_wrap[0]) { // lowerX
			m_current[0] += dX;
		}

		while (m_current[1] > m_wrap[3]) { // upperY
			m_current[1] -= dY;
		}

		while (m_current[1] < m_wrap[1]) { // lowerY
			m_current[1] += dY;
		}
	}

	/** Change heading (direction) to the left
	* (counterclockwise, positive direction).
	*
	* @see #right
	*
	* <p>
	* omega can be rad, deg, or grad, depending on the heading mode.
	*/
	void left(double omega)
	{
		// note: use heading() call to get it in
		// the appropriate heading mode
		setheading(heading() + omega);
	}

	/** Change heading (direction) to the right
	* (clockwise, negative direction).
	*
	* @see #left
	*
	* <p>
	* omega can be rad, deg, or grad, depending on the heading mode.
	*/
	void right(double omega)
	{
		left(-omega);
	}

	/** Move to a specified point.
	*
	* @see #setx
	* @see #sety
	*/
	void setxy(double x, double y)
	{
		penMoveTo(x, y);
	}

	/** Move to a specified point (only X changes).
	*
	* @see #sety
	* @see #setxy
	*/
	void setx(double x)
	{
		penMoveTo(x, m_current[1]); // currentY
	}

	/** Move to a specified point (only Y changes).
	*
	* @see #setx
	* @see #setxy
	*/
	void sety(double y)
	{
		penMoveTo(m_current[0], y); // currentX
	}

	/** Set the heading.
	*
	* @see #heading
	*
	* <p>
	* phi can be rad, deg, or grad, depending on the heading mode.
	*/
	void setheading(double phi)
	{
		setAndNormalizeHeading(headingE2I(phi));
	}

	/** Set and normalize heading.
	*/
	private void setAndNormalizeHeading(double phi)
	{
		m_heading = normalizeHeading(phi);
	}

	/** Normalize heading.
	*/
	static double normalizeHeading(double phi)
	{
		// Note: denormalized typically by less than two circles,
		// so the loops won't take too long.

		double newHeading = phi;
		double twoPi = Math.PI * 2.0;

		// cut while larger or equal 2 pi.
		while (newHeading >= twoPi) {
			newHeading -= twoPi;
		}

		// increment while less zero.
		while (newHeading < 0.0) {
			newHeading += twoPi;
		}

		return newHeading;
	}

	/** Translate external heading mode to internal.
	*
	* @see #headingI2E
	*/
	private double headingE2I(double phi)
	{
		switch (m_headingMode) {
		case RADIANS :
			return phi;
		case DEGREE :
			return phi * Math.PI / 180.0;
		case GRAD :
			return phi * Math.PI / 200.0;
		}
		return phi;
	}

	/** Translate internalheading mode to external.
	*
	* @see #headingE2I
	*/
	private double headingI2E(double phi)
	{
		switch (m_headingMode) {
		case RADIANS :
			return phi;
		case DEGREE :
			return phi * 180.0 / Math.PI;
		case GRAD :
			return phi * 200.0 / Math.PI;
		}
		return phi;
	}

	/** Set heading towards a point.
	*/
	void towards(double x, double y)
	{
		double dX = x - m_current[0]; // currentX
		double dY = y - m_current[1]; // currentY

		double phi = 0.0;

		// check if both zero.
		if (dX != 0.0 || dY != 0.0) {
			// Note: a mismatch with some manual page concerning
			// the atan2 argument order has been observed.
			phi = Math.atan2(dY, dX);
		}

		setAndNormalizeHeading(phi);
	}

	/** Go home; heading to zero.
	*
	* @see #setxy
	*/
	void home()
	{
		penMoveTo(0.0, 0.0);
		setAndNormalizeHeading(0.0);
	}

	/** Set wrapping mode.
	*
	* @see #nowrap
	*/
	void wrap()
	{
		m_wrapping = true;

		// Note: m_wrap is still (more specific: always) set.
	}

	/** Unset wrapping mode.
	*
	* @see #wrap
	*/
	void nowrap()
	{
		m_wrapping = false;

		// Note: we must not reset m_wrap,
		// since that gets used with later wrap() calls.
	}

	/** Get X coordinate.
	*
	* @see #ycor
	*/
	double xcor()
	{
		return m_current[0]; // currentX
	}

	/** Get Y coordinate.
	*
	* @see #xcor
	*/
	double ycor()
	{
		return m_current[1]; // currentY
	}

	/** Get heading.
	*
	* @see #setheading
	*
	* <p>
	* The mesurement unit depends on the heading mode and can be rad, deg, or grad.
	*/
	double heading()
	{
		return headingI2E(m_heading);
	}

	/** Get pen state.
	*
	* <p>
	* Note: only the turtle relevant stuff given.
	*/
	boolean drawstate()
	{
		return m_penDown;
	}

	// --------

	/** Set heading mode.
	*
	* @see #getHeadingMode
	* @see #RADIANS
	* @see #DEGREE
	* @see #GRAD
	*/
	void setHeadingMode(int mode)
	{
		switch (mode) {
		case RADIANS :
		case DEGREE :
		case GRAD :
			m_headingMode = mode;
			break;
		}

		// Note: does not throw on illegal mode,
		// just ignores it.
	}

	/** Get heading mode.
	*
	* @see #setHeadingMode
	* @see #RADIANS
	* @see #DEGREE
	* @see #GRAD
	*/
	int getHeadingMode()
	{
		return m_headingMode;
	}

	/** Set clipping.
	*
	* @see #getClipping
	* @see #setWrapping
	* @see #getWrapping
	* @see #m_clipping
	* @see #m_clip
	*/
	void setClipping(
		double lowerX,
		double lowerY,
		double upperX,
		double upperY)
	{
		m_clipping = true;
		m_clip = lowerUpper(lowerX, lowerY, upperX, upperY);
	}

	/** Get clipping as {lowerX, lowerY, upperX, upperY} or null, if not
	* given.
	*
	* @see #setClipping
	* @see #setWrapping
	* @see #getWrapping
	*/
	double[] getClipping()
	{
		if (!m_clipping) {
			return null;
		}

		return arrayCopy(m_clip);
	}

	/** Set wrapping.
	*
	* @see #getWrapping
	* @see #setWrappingRange
	* @see #setClipping
	* @see #getClipping
	* @see #m_wrapping
	* @see #m_wrap
	*/
	void setWrapping(
		double lowerX,
		double lowerY,
		double upperX,
		double upperY)
	{
		m_wrapping = true;
		setWrappingRange(lowerX, lowerY, upperX, upperY);
	}

	/** Get wrapping as {lowerX, lowerY, upperX, upperY} or null, if not
	* given.
	*
	* @see #setWrapping
	* @see #getWrappingRange
	* @see #setClipping
	* @see #getClipping
	* @see #m_wrapping
	* @see #m_wrap
	*/
	double[] getWrapping()
	{
		if (!m_wrapping) {
			return null;
		}

		return getWrappingRange();
	}

	/** Set wrapping range.
	*
	* @see #getWrappingRange
	* @see #setWrapping
	* @see #m_wrap
	*/
	void setWrappingRange(
		double lowerX,
		double lowerY,
		double upperX,
		double upperY)
	{
		m_wrap = lowerUpper(lowerX, lowerY, upperX, upperY);

		// Note: not setting/changing m_wrapping.
	}

	/** Get wrapping range as {lowerX, lowerY, upperX, upperY}.
	*
	* @see #setWrappingRange
	* @see #getWrapping
	* @see #m_wrap
	*/
	double[] getWrappingRange()
	{
		// Note: not considering m_wrapping.

		return arrayCopy(m_wrap);
	}

	/** Create a lower/upper data structure, for clipping or wrapping.
	*/
	static double[] lowerUpper(
		double lowerX,
		double lowerY,
		double upperX,
		double upperY)
	{
		double t;

		// check range limits order, allow mismatches
		if (upperX < lowerX) {
			t = upperX;
			upperX = lowerX;
			lowerX = t;
		}
		if (upperY < lowerY) {
			t = upperY;
			upperY = lowerY;
			lowerY = t;
		}

		double[] lu = new double[4];
		lu[0] = lowerX; // lowerX
		lu[1] = lowerY; // lowerY
		lu[2] = upperX; // upperX
		lu[3] = upperY; // upperY

		return lu;
	}

	/** Copy an array of doubles.
	* Used to suppress side effects on returned data.
	*/
	static double[] arrayCopy(double[] a)
	{
		if (a == null) {
			return null;
		}

		double[] b = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			b[i] = a[i];
		}

		return b;
	}

	// --------

	/** Draw.
	*
	* @see #m_graphics
	* @see #m_bounds
	* @see #m_scale
	* @see #m_transform
	* @see #wrappedDraw
	* @see #clippedDraw
	* @see #rawDraw
	*
	* <p>
	* The logical Turtle layout is upside down in regards to the
	* java.awt.Graphics layout:
	*
	* <pre>
	* 0----------------> Graphics X
	* |
	* |        ^ Turtle Y
	* |        |
	* |        |
	* |        |
	* | -------0-------> Turtle X
	* |        |
	* |        |
	* |        |
	* |        |
	* |
	* V
	* Graphics Y
	* </pre>
	*
	* <p>
	* The coordinates are scaled and transformed and centered at the
	* middle of the supplied bounds:
	* <pre>
	* (int)(x * scaleX + transformX) + m_bounds.width / 2 + m_bounds.x
	* </pre>
	*
	* <p>
	* draw() first wraps, then clips (makes more sense than the other
	* way around).
	*/
	private Graphics draw(
		double xFrom,
		double yFrom,
		double xTo,
		double yTo)
	{
		// check for null
		if (m_graphics != null && m_bounds != null) {
			wrappedDraw(xFrom, yFrom, xTo, yTo);
		}

		return m_graphics;
	}

	/** Draw with clipping.
	*
	* @see #draw
	* @see #wrappedDraw
	* @see #rawDraw
	*/
	private void clippedDraw(
		double xFrom,
		double yFrom,
		double xTo,
		double yTo)
	{
		// don't calculate anything if not clipping
		if (!m_clipping) {
			rawDraw(xFrom, yFrom, xTo, yTo);
			return;
		}

		// sections relative to the clipping borders
		//
		//  Y   lowerX
		//  ^   |   upperX
		//  |   |   |
		//  | A | B | C
		// -+---+---+---upperY
		//  | D | E | F
		// -+---+---+---lowerY
		//  | G | H | I
		//  |   |   |
		// -0---+---+--> X
		//  |   |   |

		// get section classification

		// Optimization note: the floating point subtractions that
		// might be implied in the comparisions done in section()
		// would be of use in the later calculation steps as to
		// minimize operations.

		// Note: in order to avoid wrong re-classifications
		// after partial cuts, the well known classifications
		// are set and single missing stuff is completed.

		// Optimization note: the above also leads to less
		// floating point comparisons.

		// Optimization note: however an unecessary recalculation
		// appears in certain cases [e.g. ABEHI doesn't really have
		// to be cut on X].  Other sub-calculations may also be repeated.

		// [initial classification]
		int sectionFromX = section(xFrom, m_clip[0], m_clip[2]); // lowerX, upperX
		int sectionFromY = section(yFrom, m_clip[1], m_clip[3]); // lowerY, upperY
		int sectionToX = section(xTo, m_clip[0], m_clip[2]); // lowerX, upperX
		int sectionToY = section(yTo, m_clip[1], m_clip[3]); // lowerY, upperY

		// the 'reduce-loop'
		// Clip the line until it's either totally inside the clipping
		// area or totally outside.  This works both with doing and
		// avoiding unrequired cuts [later case catches the outside case
		// at the end of the loop].
		for (;;) {

			// section would be "GHIDEFABC".charAt(sectionX + sectionY * 3 + 4);

			// don't calculate further if inside clipping region
			if (
				sectionFromX == 0 &&
				sectionToX == 0 &&
				sectionFromY == 0 &&
				sectionToY == 0
			) {
				// inside [EE]
				rawDraw(xFrom, yFrom, xTo, yTo);
				return;
			}

			// don't calculate further if outside
			if (
				sectionFromX == sectionToX &&
				sectionFromX != 0
			) {
				// outside [AA, AD, AG, CC, CF, CI, DD, DG, FF, FI, GG, II]
				return;
			}
			if (
				sectionFromY == sectionToY &&
				sectionFromY != 0
			) {
				// outside [AB, AC, BB, BC, GH, GI, HH, HI]
				return;
			}

			double u, t;
			int s;

			// [Former cutting in one dimension first may introduce
			// additional calculations, however code may be easier to read.]

			// Optimization note: the repeating [even with corrected
			// values!] ((yTo-yFrom)/(xTo-xFrom)) value calculated in
			// intersection() could be reused to minimize operations.

			// There is some repetition in these if/else cases, as well
			// as in the lowerX vs. upperX cases.  However, Java
			// does not easily allow to condense such constructs.

			// cut in X dimension
			if (sectionFromX != sectionToX) {

				// cut on lowerX
				if (sectionFromX < 0 || sectionToX < 0) {
					u = m_clip[0]; // lowerX
					t = intersection(yFrom, yTo, xFrom, xTo, u);
					s = section(t, m_clip[1], m_clip[3]); // lowerY, upperY

					// cut here only if required
					if (s == 0) {
						if (sectionFromX < 0) {
							xFrom = u;
							yFrom = t;
							sectionFromX = 0;
							sectionFromY = s;
						} else {
							xTo = u;
							yTo = t;
							sectionToX = 0;
							sectionToY = s;
						}
						continue;
					}
				}

				// cut on upperX
				if (sectionFromX > 0 || sectionToX > 0) {
					u = m_clip[2]; // upperX
					t = intersection(yFrom, yTo, xFrom, xTo, u);
					s = section(t, m_clip[1], m_clip[3]); // lowerY, upperY

					// cut here only if required
					if (s == 0) {
						if (sectionFromX > 0) {
							xFrom = u;
							yFrom = t;
							sectionFromX = 0;
							sectionFromY = s;
						} else {
							xTo = u;
							yTo = t;
							sectionToX = 0;
							sectionToY = s;
						}
						continue;
					}
				}
			}

			// cut in Y dimension
			// Note: because of the intermediate tests, this condition
			// always holds true here.
			if (sectionFromY != sectionToY) {

				// cut on lowerY
				if (sectionFromY < 0 || sectionToY < 0) {
					u = m_clip[1]; // lowerY
					t = intersection(xFrom, xTo, yFrom, yTo, u);
					s = section(t, m_clip[0], m_clip[2]); // lowerX, upperX

					// cut here only if required
					// Note: because of the intermediate tests, this condition
					// always holds true here.
					if (s == 0) {
						if (sectionFromY < 0) {
							yFrom = u;
							xFrom = t;
							sectionFromY = 0;
							sectionFromX = s;
						} else {
							yTo = u;
							xTo = t;
							sectionToY = 0;
							sectionToX = s;
						}
						continue;
					}
				}

				// cut on upperY
				// Note: because of the intermediate tests, this condition
				// always holds true here.
				if (sectionFromY > 0 || sectionToY > 0) {
					u = m_clip[3]; // upperY
					t = intersection(xFrom, xTo, yFrom, yTo, u);
					s = section(t, m_clip[0], m_clip[2]); // lowerX, upperX

					// cut here only if required
					// Note: because of the intermediate tests, this condition
					// always holds true here.
					if (s == 0) {
						if (sectionFromY > 0) {
							yFrom = u;
							xFrom = t;
							sectionFromY = 0;
							sectionFromX = s;
						} else {
							yTo = u;
							xTo = t;
							sectionToY = 0;
							sectionToX = s;
						}
						continue;
					}
				}
			}

			// no cut applied (required),
			// so we're not intersecting with the clipping region
			return;
		}
	}

	// clippedDraw samples:
	//
	// _all_ from A (including symmetrically redundant ones)
	//
	// (-2, 2, -3, 2) // AA(A)
	// (-2, 2, 0, 2) // AB(AB)
	// (-2, 2, 2, 2) // AC(ABC)
	//
	// redundant
	// (-2, 2, -2, 0) // AD(AD)
	//
	// (-2, 3, 0, 0) // AE(ABE)
	// (-3, 2, 0, 0) // AE(ADE)
	// (-2, 2, 5, 0) // AF(ABCF)
	// (-2, 2, 2, 0) // AF(ABEF)
	// (-5, 2, 2, 0) // AF(ADEF)
	//
	// redundant
	// (-2, 2, -2, -2) // AG(ADG)
	// (-2, 5, 0, -2) // AH(ABEH)
	// (-2, 2, 0, -2) // AH(ADEH)
	// (-2, 2, 0, -5) // AH(ADGH)
	//
	// (-2, 5, 5, -2) // AI(ABCFI)
	// (-2, 2, 3, -2) // AI(ABEFI)
	// (-2, 3, 2, -3) // AI(ABEHI)
	// (-3, 2, 3, -2) // AI(ADEFI)
	// (-2, 2, 2, -3) // AI(ADEHI)
	// (-5, 2, 2, -5) // AI(ADGHI)
	//
	// no symmetrically redundant ones for the rest
	//
	// (0, 2, -3, 0) // BD(BAD)
	// (0, 2, -1.5, 0) // BD(BED)
	// (0, 2, 0, 0) // BE(BE)
	// (0, 2, 0, -2) // BH(BEH)
	//
	// (-0.5, -0.5, 0.5, 0.5) // EE(E)

	/** Draw with wrapping.
	*
	* @see #draw
	* @see #clippedDraw
	* @see #rawDraw
	*/
	private void wrappedDraw(
		double xFrom,
		double yFrom,
		double xTo,
		double yTo)
	{
		// don't calculate anything if not wrapping
		if (!m_wrapping) {
			clippedDraw(xFrom, yFrom, xTo, yTo);
			return;
		}

		double dX = m_wrap[2] - m_wrap[0]; // upperX, lowerX
		double dY = m_wrap[3] - m_wrap[1]; // upperY, lowerY

		// Adjust in a way that xFrom/yFrom is in range.
		// This typically triggers only if the real Turtle coordinates
		// are not adjusted.

		// Optimization note: an expression with a floating point
		// remainder could be used, that would be of benefit for large
		// offsets.  However, about two multiplications would be
		// involved in that.

		while (xFrom > m_wrap[2]) { // upperX
			xFrom -= dX;
			xTo -= dX;
		}

		while (xFrom < m_wrap[0]) { // lowerX
			xFrom += dX;
			xTo += dX;
		}

		while (yFrom > m_wrap[3]) { // upperY
			yFrom -= dY;
			yTo -= dY;
		}

		while (yFrom < m_wrap[1]) { // lowerY
			yFrom += dY;
			yTo += dY;
		}

		// draw the wrapped line segment-wise
		for (;;) {

			// get section classification
			// details: see clippedDraw()
			int sectionToX = section(xTo, m_wrap[0], m_wrap[2]); // lowerX, upperX
			int sectionToY = section(yTo, m_wrap[1], m_wrap[3]); // lowerY, upperY

			// terminate if inside wrapping region
			if (sectionToX == 0 && sectionToY == 0) {
				// [EE]
				clippedDraw(xFrom, yFrom, xTo, yTo);
				return;
			}

			double x = 0.0, y = 0.0;

			if (sectionToX < 0) {
				y = intersection(yFrom, yTo, xFrom, xTo, m_wrap[0]); // lowerX
			} else if (sectionToX > 0) {
				y = intersection(yFrom, yTo, xFrom, xTo, m_wrap[2]); // upperX
			}

			if (sectionToY < 0) {
				x = intersection(xFrom, xTo, yFrom, yTo, m_wrap[1]); // lowerY
			} else if (sectionToY > 0) {
				x = intersection(xFrom, xTo, yFrom, yTo, m_wrap[3]); // upperY
			}

			int sectionX = section(x, m_wrap[0], m_wrap[2]); // lowerX, upperX
			int sectionY = section(y, m_wrap[1], m_wrap[3]); // lowerY, upperY

			if (sectionToX < 0 && sectionY == 0) {
				// [ED{A|D|G}]
				clippedDraw(xFrom, yFrom, m_wrap[0], y); // lowerX
				xFrom = m_wrap[2]; // upperX, reenter at oposite side
				yFrom = y;
				xTo += dX; // shift
				continue;
			}

			if (sectionToX > 0 && sectionY == 0) {
				// [EF{C|F|I}]
				clippedDraw(xFrom, yFrom, m_wrap[2], y); // upperX
				xFrom = m_wrap[0]; // lowerX, reenter at oposite side
				yFrom = y;
				xTo -= dX; // shift
				continue;
			}

			if (sectionToY < 0 && sectionX == 0) {
				// [EH{G|H|I}]
				clippedDraw(xFrom, yFrom, x, m_wrap[1]); // lowerY
				yFrom = m_wrap[3]; // upperY, reenter at oposite side
				xFrom = x;
				yTo += dY; // shift
				continue;
			}

			if (sectionToY > 0 && sectionX == 0) {
				// [EB{A|B|C}]
				clippedDraw(xFrom, yFrom, x, m_wrap[3]); // upperY
				yFrom = m_wrap[1]; // lowerY, reenter at oposite side
				xFrom = x;
				yTo -= dY; // shift
				continue;
			}

			// can't arrive here
		}
	}

	/** Calculate section relative to a high/low range.
	*
	* @return -1, 0 or 1
	*/
	static int section(double a, double low, double high)
	{
		// check range limits order, allow mismatches
		// note: this test of course costs some time,
		// but makes the interface more robust
		if (high < low) {
			double t = high;
			high = low;
			low = t;
		}

		if (a > high) {
			return 1;
		}

		if (a < low) {
			return -1;
		}

		return 0;
	}

	/** Calculate intersection.
	*/
	static double intersection(
		double b1,
		double b2,
		double a1,
		double a2,
		double aI)
	{
		// catch division-by-zero, return some 'default' data then
		// Note: based on the classifications, divisions by zero won't happen.
		double d = (a2 - a1);
		if (d == 0.0) {
			return 0.0;
		}

		return (b1 + (((b2 - b1) / (d)) * (aI - a1)));
	}

	/** Draw.
	*
	* @see #m_graphics
	* @see #m_bounds
	* @see #m_scale
	* @see #m_transform
	* @see #draw
	* @see #wrappedDraw
	* @see #clippedDraw
	*/
	private void rawDraw(
		double xFrom,
		double yFrom,
		double xTo,
		double yTo)
	{
		// note: m_graphics and m_bounds must be set

		m_graphics.drawLine(
			(int)(xFrom * m_scale[0] + m_transform[0]) + // scaleX, transformX
				m_bounds.width / 2 + m_bounds.x,
			(int)-(yFrom * m_scale[1] + m_transform[1]) + // scaleY, transformY
				m_bounds.height / 2 + m_bounds.y,
			(int)(xTo * m_scale[0] + m_transform[0]) + // scaleX, transformX
				m_bounds.width / 2 + m_bounds.x,
			(int)-(yTo * m_scale[1] + m_transform[1]) + // scaleY, transformY
				m_bounds.height / 2 + m_bounds.y);
	}
}

