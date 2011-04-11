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

import org.eclipse.core.runtime.Status;
import org.obeonetwork.pim.uml2.gen.java.UML2JavaActivator;

/**
 * The Logger service class.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.1
 */
public final class Logger {
	
	/**
	 * The constructor.
	 */
	private Logger() {
		// prevent instantiation
	}
	
	/**
	 * Logs an error with the given message.
	 * 
	 * @param message The message.
	 * @return An empty string.
	 */
	public static String logError(String message) {
		Status status = new Status(Status.ERROR, UML2JavaActivator.PLUGIN_ID, message, null);
		UML2JavaActivator.log(status);
		return "";
	}
	
	/**
	 * Logs a warning with the given message.
	 * @param message The message.
	 * @return An empty string.
	 */
	public static String logWarning(String message) {
		Status status = new Status(Status.WARNING, UML2JavaActivator.PLUGIN_ID, message, null);
		UML2JavaActivator.log(status);
		return "";
	}
	
	/**
	 * Logs an information with the given message.
	 * @param message The message.
	 * @return An empty string.
	 */
	public static String logInformation(String message) {
		Status status = new Status(Status.OK, UML2JavaActivator.PLUGIN_ID, message, null);
		UML2JavaActivator.log(status);
		return "";
	}
}
