/* StringTurtleApplet.java -- StringTurtle Applet.
**
** Copyright (C) 1997-2003 Eric Laroche.  All rights reserved.
**
** @author Eric Laroche <laroche@lrdev.com>
** @version 1.3 @(#)$Id: StringTurtleApplet.java,v 1.3 2003/02/16 12:37:21 laroche Exp $
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

// Use the AWT based graphics turtle.
import com.lrdev.turtle.PersistentTurtle;
import com.lrdev.turtle.StringTurtle;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/** StringTurtleApplet: StringTurtle Applet.
*
* @author Eric Laroche <laroche@lrdev.com>
* @version 1.3 @(#)$Id: StringTurtleApplet.java,v 1.3 2003/02/16 12:37:21 laroche Exp $
* @url http://www.lrdev.com/lr/java/javaturtle.html
*
* @see com.lrdev.turtle.StringTurtle
* @see com.lrdev.turtle.Turtle
*/
public class StringTurtleApplet
	extends Applet
{
	/** Version.
	*/
	private final static String m_version =
		"Version: 1.3 @(#)$Id: StringTurtleApplet.java,v 1.3 2003/02/16 12:37:21 laroche Exp $";

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

	/** Application program name.
	*/
	private final static String m_programName =
		"StringTurtleApplet";

	/** Applet usage.
	*
	* <p>
	* "center": since 1.2.
	*/
	private final static String[][] m_usage = {
		{"execute", "String", "Turtle commands"},
		{"center", "boolean", "center and scale the drawing"},
	};

	/** Turtle commands string.
	*/
	private String m_execute = null;

	/** PersistentTurtle center mode.
	*/
	private boolean m_center = false;

	/** Entry point for a standalone application program.
	*/
	public static void main(String[] args)
	{
		// print usage info
		System.err.println(
			"Sorry, you must run " + m_programName + " as an applet.\n\n" +
			m_version + "\n" +
			m_author + "\n" +
			m_copyright + "\n\n" +
			m_disclaimer + "\n");
	}

	/** Called by the browser.
	* Returns info on this applet.
	*
	* Overwrites Applet.getAppletInfo.
	*/
	@Override
	public String getAppletInfo()
	{
		return
			m_version + "\n" +
			m_author;
	}

	/** Called by the browser.
	* Returns info on the parameters of this applet.
	*
	* Overwrites Applet.getParameterInfo.
	*/
	@Override
	public String[][] getParameterInfo()
	{
		return m_usage;
	}

	/** Constructor.
	*/
	public StringTurtleApplet()
	{
		// NOTE: must postpone getParameter() calls to init()
	}

	/** Called after the constructor and before start().
	* Sets the background color and reads in the applet parameters.
	*
	* Overwrites Applet.init.
	*/
	@Override
	public void init()
	{
		String parameter;

		m_execute = getParameter("execute");

		parameter = getParameter("center");
		if (parameter != null) {
			m_center = Boolean.valueOf(parameter).booleanValue();
		}

		// white background, black foreground
		setBackground(Color.white);
		setForeground(Color.black);
	}

	/** Called by the browser.
	*
	* Overwrites Applet.start.
	*/
	@Override
	public void start()
	{
	}

	/* Called by the browser.
	*
	* Overwrites Applet.stop.
	*/
	@Override
	public void stop()
	{
	}

	/** Called by the browser.
	*
	* Overwrites Applet.destroy.
	*/
	@Override
	public void destroy()
	{
	}

	/** Paint the component.
	*
	* Overwrites Component.paint.
	*/
	@Override
	public void paint(Graphics graphics)
	{
		// get drawing box
		Rectangle b = bounds();

		if (b == null) {
			return;
		}

		// 2 pixel border
		int border = 2;

		b.width -= 2 * border;
		b.height -= 2 * border;
		b.x += border;
		b.y += border;

		// check if the box is large enough
		if (b.width < 0 || b.height < 0) {
			// we're too small
			return;
		}

		if (m_execute == null) {
			return;
		}

		// [version 1.3]
		int wHalfWidth = b.width / 2 - border;
		int wHalfHeight = b.height / 2 - border;
		if (wHalfWidth < 0 || wHalfHeight < 0) {
			// we're too small
			return;
		}

		// [version 1.1]
		// StringTurtle turtle = new StringTurtle(graphics, b);

		// [version 1.2]
		PersistentTurtle persistentTurtle = new PersistentTurtle();
		if (m_center) {
			persistentTurtle.center();
		}

		// [version 1.3]
		// note that this only sets the wrapping _range_ and that
		// wrapping is only active if a "wrap" command has been issued
		persistentTurtle.setWrappingRange(
			-wHalfWidth,
			-wHalfHeight,
			wHalfWidth,
			wHalfHeight);

		// [version 1.2]
		StringTurtle turtle = new StringTurtle(persistentTurtle);

		turtle.execute(m_execute);

		// [version 1.2]
		persistentTurtle.paint(graphics, b);
   	}
}

