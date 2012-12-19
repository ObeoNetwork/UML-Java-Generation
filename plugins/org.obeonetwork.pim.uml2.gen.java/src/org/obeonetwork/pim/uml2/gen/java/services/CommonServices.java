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

/**
 * General purpose services.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class CommonServices {
	/**
	 * Returns the current date.
	 * 
	 * @return The date.
	 */
	public String reqDate() {
		Date date = new Date(); // to get the date
		Locale locale = Locale.getDefault(); // to get the language of the system
		DateFormat dateFormatShort = DateFormat.getDateInstance(DateFormat.LONG, locale);
		return dateFormatShort.format(date);
	}

	/**
	 * Returns the current time.
	 * 
	 * @return The time.
	 */
	public String reqTime() {
		Date date = new Date();
		return DateFormat.getTimeInstance(DateFormat.LONG).format(date);
	}
}
