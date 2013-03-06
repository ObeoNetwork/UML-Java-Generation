/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stephane Begaudeau (Obeo) - initial API and implementation
 *******************************************************************************/
package org.obeonetwork.pim.uml2.gen.java.services;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

/**
 * The common services utility class.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.1
 */
public final class CommonServices {

	/**
	 * The constructor.
	 */
	private CommonServices() {
		// prevent instantiation.
	}
	
	/**
	 * Surrounds the value with double quote if is not done (abcd to "abcd")<br/>
	 * Replace double quote with \" (ab"cd to "ab\"cd")<br/> Remove simple quote
	 * starting and ending in value ('abcd' to "abcd")<br/> Usefull for default string value.
	 * 
	 * @param value
	 *            the value
	 * 
	 * @return the string
	 */
	public static String addQuotes(String value) {
		if (value.endsWith("'") && value.startsWith("'")) {
			value = value.substring(1, value.length() - 1);
		}
		if (value.endsWith("\"") && value.startsWith("\"")) {
			value = value.substring(1, value.length() - 1);
		}
		return "\"" + value.trim().replaceAll("\"", "\\\\\\\"") + "\"";
	}

	/**
	 * Gets the date in a long format : January 12, 1952
	 * 
	 * @return String representing the long format date
	 */
	public static String getLongDate() {
		Date date = new Date(); // to get the date
		Locale locale = Locale.getDefault();// to get the language of the system
		DateFormat dateFormatShort = DateFormat.getDateInstance(DateFormat.LONG, locale);
		return dateFormatShort.format(date);
	}

	/**
	 * Gets the date in a short format : 06/08/07
	 * 
	 * @return String representing the short format date
	 */
	public static String getShortDate() {
		Date date = new Date(); // to get the date
		Locale locale = Locale.getDefault();// to get the language of the system
		DateFormat dateFormatShort = DateFormat.getDateInstance(DateFormat.SHORT, locale);
		return dateFormatShort.format(date);
	}
	/**
	 * Returns the current time.
	 * 
	 * @return The current time.
	 */
	public static String getTime() {
		Date date = new Date();
		return DateFormat.getTimeInstance(DateFormat.LONG).format(date);
	}
	
	
	/**
	 * Formats a documentation string to be used in a Javadoc comment.<br/>
	 * " * " will be added at the beginning of each lines in the documentation string.<br/>
	 * 
	 * @param doc
	 *            the documentation String to be formated as Javadoc lines
	 * 
	 * @return stared Javadoc lines from the doc's lines
	 */
	public static String javadocStar(String doc) {
        StringBuffer res = new StringBuffer("");
        Scanner scanner = new Scanner(doc);
        while (scanner.hasNextLine()) {
          String line = scanner.nextLine();
          res.append(" * " + line + "<br />\n");
        }
        return res.toString();
	}

}
