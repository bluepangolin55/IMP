/* StringTurtle.java -- Graphics Turtle controlled by string directives.
**
** Copyright (C) 1997-2003 Eric Laroche.  All rights reserved.
**
** @author Eric Laroche <laroche@lrdev.com>
** @version 1.3 @(#)$Id: StringTurtle.java,v 1.3 2003/02/16 12:37:08 laroche Exp $
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
import java.util.StringTokenizer;

/**  StringTurtle: Graphics Turtle controlled by string directives.
*
* @author Eric Laroche <laroche@lrdev.com>
* @version 1.3 @(#)$Id: StringTurtle.java,v 1.3 2003/02/16 12:37:08 laroche Exp $
* @url http://www.lrdev.com/lr/java/javaturtle.html
*
* @see com.lrdev.turtle.Turtle
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
*/
public class StringTurtle
{
	/** Version.
	*/
	private final static String m_version =
		"Version: 1.3 @(#)$Id: StringTurtle.java,v 1.3 2003/02/16 12:37:08 laroche Exp $";

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

	/** Opening braket character.
	*
	* @see #CLOSING_BRAKET
	*/
	public final static Character OPENING_BRAKET = new Character('[');

	/** Closing braket character.
	*
	* @see #OPENING_BRAKET
	*/
	public final static Character CLOSING_BRAKET = new Character(']');

	/** Turtle.
	*
	* @see #getTurtle
	*/
	private Turtle m_turtle = null;

	/** Return source information.
	* This includes version with date, author with email address,
	* copyright and disclaimer.
	*/
	public static String getInfo()
	{
		return m_version + "\n" +
			m_author + "\n" +
			m_copyright + "\n\n" +
			m_disclaimer + "\n";
	}

	/** StringTurtle: get a new StringTurtle.
	*/
	public StringTurtle(Graphics graphics, Rectangle bounds)
	{
		m_turtle = new Turtle(graphics, bounds);
		m_turtle.degree();
	}

	/** StringTurtle: get a new StringTurtle.
	*/
	public StringTurtle(
		Graphics graphics,
		Rectangle bounds,
		double scaleX,
		double scaleY)
	{
		m_turtle = new Turtle(graphics, bounds, scaleX, scaleY);
		m_turtle.degree();
	}

	/** StringTurtle: get a new StringTurtle.
	*/
	public StringTurtle(
		Graphics graphics,
		Rectangle bounds,
		double scaleX,
		double scaleY,
		double transformX,
		double transformY)
	{
		m_turtle = new Turtle(graphics, bounds, scaleX, scaleY, transformX, transformY);
		m_turtle.degree();
	}

	/** StringTurtle: get a new StringTurtle.
	*
	* @since 1.2
	*/
	public StringTurtle(Turtle turtle)
	{
		m_turtle = turtle;
		m_turtle.degree();
	}

	/** Get the Turtle object.
	*
	* @see #m_turtle
	*/
	public Turtle getTurtle()
	{
		return m_turtle;
	}

	/** Execute a string of Turtle commands.
	*/
	public StringTurtle execute(String commands)
	{
		return
			executeTokenized(
				makeHierarchical(
					convertTokenizer(
						// The StringTokenizer used is fair enough,
						// typically no quoting is needed here.
						// Default delimiters (" \t\n\r") are ok.
						new StringTokenizer(
							preTokenize(commands)))));
	}

	/** Consider brakets as standalone tokens and space accordingly.
	*
	* @see #OPENING_BRAKET
	* @see #CLOSING_BRAKET
	*/
	private static String preTokenize(String s)
	{
		return
			enforceSpacing(
				enforceSpacing(
					s,
					OPENING_BRAKET.charValue()),
				CLOSING_BRAKET.charValue());
	}

	/** Enforce spacing around specified character.
	*/
	private static String enforceSpacing(String s, char c)
	{
		// start search with s, at first character
		String t = s;
		for (int i = 0;;) {
			// find next occurence
			int j = t.indexOf(c, i);
			// check if done
			if (j == -1) {
				return t;
			}
			// check left spacing, if not at beginning
			if (j != 0) {
				if (!Character.isSpace(t.charAt(j - 1))) {
					// space it
					t = t.substring(0, j) + " " + t.substring(j);
					j++;
				}
			}
			// check right spacing, if not at end
			if (j != t.length() - 1) {
				if (!Character.isSpace(t.charAt(j + 1))) {
					// space it
					t = t.substring(0, j + 1) + " " + t.substring(j + 1);
				}
			}
			// update index
			i = j + 1;
			// check if done.
			// this test supresses a t.indexOf() call with index quasi out of bounds.
			if (i >= t.length()) {
				return t;
			}
		}
	}

	/** Convert StringTokenizer enumeration to String array.
	*/
	private static String[] convertTokenizer(StringTokenizer tokenizer)
	{
		int n = tokenizer.countTokens();
		String[] s = new String[n];

		for (int i = 0; tokenizer.hasMoreTokens(); i++) {
			s[i] = tokenizer.nextToken();
		}

		return s;
	}

	/** Structure the tokens hierarchically.
	*
	* <p>
	* The Objects are either Strings or a list of such Objects.
	*/
	private static Object[] makeHierarchical(String[] tokens)
	{
		return makeHierarchical(tokens, 0, tokens.length);
	}

	/** Structure the tokens hierarchically.
	*
	* <p> recursive
	*/
	private static Object[] makeHierarchical(String[] tokens, int from, int to)
	{
		if (from > to) {
			throw new IllegalArgumentException("illegal bounds");
		}

		// alloc enough; we'll cut it later
		Object[] out = new Object[to - from];
		int nOut = 0;

		// loop over the remaining tokens
		while (from < to) {

			// check if it's a normal element
			if (!tokens[from].equals(OPENING_BRAKET.toString())) {
				out[nOut] = tokens[from];
				nOut++;

				from++;
				continue;
			}

			// ...or a list

			// skip opening braket
			from++;

			// find the _corresponding_ closing braket
			int to2 = from;
			int braketCount = 1;
			while (to2 < to) {

				if (tokens[to2].equals(OPENING_BRAKET.toString())) {
					braketCount++;
				} else if (tokens[to2].equals(CLOSING_BRAKET.toString())) {
					braketCount--;
				}

				if (braketCount == 0) {
					break;
				}

				to2++;
			}

			// check for mismatch
			if (to2 == to) {
				throw new IllegalArgumentException("unmatched opening braket");
			}

			out[nOut] = makeHierarchical(tokens, from, to2);
			nOut++;

			// skip closing braket
			from = to2 + 1;
		}

		// realloc
		Object[] out2 = new Object[nOut];
		for (int i = 0; i < nOut; i++) {
			out2[i] = out[i];
		}

		return out2;
	}

	/** Execute an enumeration of tokenized Turtle commands.
	*
	* @see #executeTurtleCommand
	* @see #executeControlFlow
	*
	* <p>
	* Indirect recursive.
	*/
	private StringTurtle executeTokenized(Object[] commands)
	{
		for (int i = 0; i < commands.length;) {

			// check if it's a list; just execute that list
			// (usually, lists are arguments to some command)
			if (commands[i] instanceof Object[]) {
				executeTokenized((Object[])commands[i]);

				i++;
				continue;
			}

			// it's a command then
			// this may throw an ClassCastException
			String cmd = (String)commands[i];

			// try core commands
			int consumed = executeTurtleCommand(cmd, commands, i);
			i += consumed;
			if (consumed != 0) {
				continue;
			}

			// try control flow commands
			consumed = executeControlFlow(cmd, commands, i);
			i += consumed;
			if (consumed != 0) {
				continue;
			}

			throw new IllegalArgumentException("unsupported command " + cmd);
		}

		return this;
	}

	/** Execute a Turtle command.
	*
	* @return number of tokens consumed
	*
	* @see com.lrdev.turtle.Turtle
	*
	* <p>
	* Commands are not case sensitive.
	* Abbreviations (e.g. bk for back) are supported.
	*/
	private int executeTurtleCommand(String command, Object[] tokens, int tokenIndex)
	{
		// A hash table would probably be faster
		// but pre-select by initial switch should be fast too.

		switch (Character.toLowerCase(command.charAt(0))) {

		case 'b' :

			if (
				command.equalsIgnoreCase("back") ||
				command.equalsIgnoreCase("bk")
			) {
				m_turtle.setArgument(doubleFromNextToken(tokens, tokenIndex, command));
				m_turtle.back();
				return 2;
			}

			break;

		case 'f' :

			if (
				command.equalsIgnoreCase("forward") ||
				command.equalsIgnoreCase("fd")
			) {
				m_turtle.setArgument(doubleFromNextToken(tokens, tokenIndex, command));
				m_turtle.forward();
				return 2;
			}

			break;

		case 'h' :

			if (command.equalsIgnoreCase("home")) {
				m_turtle.home();
				return 1;
			}

			break;

		case 'l' :

			if (
				command.equalsIgnoreCase("left") ||
				command.equalsIgnoreCase("lt")
			) {
				m_turtle.setArgument(doubleFromNextToken(tokens, tokenIndex, command));
				m_turtle.left();
				return 2;
			}

			break;

		case 'n' :

			if (command.equalsIgnoreCase("nowrap")) {
				m_turtle.nowrap();
				return 1;
			}

			break;

		case 'p' :

			if (
				command.equalsIgnoreCase("pendown") ||
				command.equalsIgnoreCase("pd")
			) {
				m_turtle.pendown();
				return 1;
			}

			if (
				command.equalsIgnoreCase("penup") ||
				command.equalsIgnoreCase("pu")
			) {
				m_turtle.penup();
				return 1;
			}

			break;

		case 'r' :

			if (
				command.equalsIgnoreCase("right") ||
				command.equalsIgnoreCase("rt")
			) {
				m_turtle.setArgument(doubleFromNextToken(tokens, tokenIndex, command));
				m_turtle.right();
				return 2;
			}

			break;

		case 's' :

			if (
				command.equalsIgnoreCase("setheading") ||
				command.equalsIgnoreCase("seth")
			) {
				m_turtle.setArgument(doubleFromNextToken(tokens, tokenIndex, command));
				m_turtle.setheading();
				return 2;
			}

			if (command.equalsIgnoreCase("setx")) {
				m_turtle.setArgument(doubleFromNextToken(tokens, tokenIndex, command));
				m_turtle.setx();
				return 2;
			}

			if (command.equalsIgnoreCase("setxy")) {
				m_turtle.setArgument(doubleFromNextToken(tokens, tokenIndex, command));
				m_turtle.setArgument(doubleFromNextToken(tokens, tokenIndex + 1, command));
				m_turtle.setxy();
				return 3;
			}

			if (command.equalsIgnoreCase("sety")) {
				m_turtle.setArgument(doubleFromNextToken(tokens, tokenIndex, command));
				m_turtle.sety();
				return 2;
			}

			break;

		case 't' :

			if (command.equalsIgnoreCase("towards")) {
				m_turtle.setArgument(doubleFromNextToken(tokens, tokenIndex, command));
				m_turtle.setArgument(doubleFromNextToken(tokens, tokenIndex + 1, command));
				m_turtle.towards();
				return 3;
			}

			break;

		case 'w' :

			if (command.equalsIgnoreCase("wrap")) {
				m_turtle.wrap();
				return 1;
			}

			break;

		default :

			if (
				command.equalsIgnoreCase("drawstate") ||
				command.equalsIgnoreCase("heading") ||
				command.equalsIgnoreCase("xcor") ||
				command.equalsIgnoreCase("ycor")
			) {
				throw new IllegalArgumentException("unsupported command " + command);
			}

			break;
		}

		return 0;
	}

	/** Execute a control flow command.
	*/
	private int executeControlFlow(String command, Object[] tokens, int tokenIndex)
	{
		if (command.equalsIgnoreCase("repeat")) {

			int n = intFromNextToken(tokens, tokenIndex, command);

			if (tokenIndex + 2 >= tokens.length) {
				throw new IllegalArgumentException(
					command +
					" requires two arguments"
				);
			}

			Object cmds = tokens[tokenIndex + 2];
			if (!(cmds instanceof Object[])) {
				// strange but ok
				// convert to a list
				Object[] c = new Object[1];
				c[0] = cmds;
				cmds = c;
			}

			for (int i = 0; i < n; i++) {
				executeTokenized((Object[])cmds);
			}

			return 3;
		}

		return 0;
	}

	/** Get next token object.
	*/
	private static String fromNextToken(Object[] tokens, int tokenIndex, String command)
	{
		int next = tokenIndex + 1;

		// make sure it's 1 object, not 0, or more than 1

		if (next >= tokens.length) {
			throw new IllegalArgumentException(
				command +
				" requires an argument"
				// + " of type ..."
			);
		}

		// posslibly unfold
		Object t = tokens[next];
		while (!(t instanceof String)) {
			// may throw an ClassCastException
			Object[] t2 = (Object[])t;
			if (t2.length != 1) {
				throw new IllegalArgumentException(
					command +
					" requires exactly one argument");
			}
			t = t2[0];
		}

		String argument = (String)t;

		return argument;
	}

	/** Convert next token object to a double.
	*
	* @see #fromNextToken
	*
	* <p>
	* Arguments of type double and ratio of doubles are supported.
	*/
	private static double doubleFromNextToken(Object[] tokens, int tokenIndex, String command)
	{
		String argument = fromNextToken(tokens, tokenIndex, command);

		int slash = argument.indexOf('/');

		try {

			// check for ratio of doubles
			if (slash != -1) {
				return
					Double.valueOf(argument.substring(0, slash)).doubleValue() /
						Double.valueOf(argument.substring(slash + 1)).doubleValue();
			}

			return Double.valueOf(argument).doubleValue();

		} catch (NumberFormatException exc) {
			throw new IllegalArgumentException(
				command +
				" requires an argument of type double or ratio of doubles, not " +
				argument +
				": " +
				exc.toString());
		}
	}

	/** Convert next token object to an int.
	*
	* @see #fromNextToken
	*/
	private static int intFromNextToken(Object[] tokens, int tokenIndex, String command)
	{
		String argument = fromNextToken(tokens, tokenIndex, command);

		try {
			return Integer.valueOf(argument).intValue();
		} catch (NumberFormatException exc) {
			throw new IllegalArgumentException(
				command +
				" requires an argument of type int, not " +
				argument +
				": " +
				exc.toString());
		}
	}
}

