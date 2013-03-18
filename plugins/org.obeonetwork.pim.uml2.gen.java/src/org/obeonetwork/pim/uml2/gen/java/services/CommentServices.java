/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Jean-Pierre PECUCHET (INSA Rouen) - initial API and implementation
 *******************************************************************************/
package org.obeonetwork.pim.uml2.gen.java.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Model;

/**
 * The comment services utility class. Gives methods for collecting all comments
 * for an annotated element.
 * 
 * @author Jean-Pierre.PECUCHET@insa-rouen.fr
 * @since 1.0.4
 */
public final class CommentServices {

	/**
	 * Dictionary of all not void comments in the UML model
	 */
	private static Map<Element, List<Comment>> commentMap = new HashMap<Element, List<Comment>>();

	/**
	 * The constructor.
	 */
	private CommentServices() {
		// prevent instantiation.
	}

	/**
	 * Clears the comments Map
	 */
	public static void clearComments(Model m) {
		commentMap.clear();
	}

	/**
	 * Add a Comment to the comments Map
	 * 
	 * @param c
	 *            is the Comment
	 */
	public static void collect(Comment c) {
		for (Element e : c.getAnnotatedElements()) {
			if (c.getBody() != null && !"".equals(c.getBody().trim())) {
				addEntry(e, c);
			}
		}
	}

	/**
	 * Add an (Element, Comment) entry in the comments Map
	 * 
	 * @param e
	 *            is the annotated Element
	 * @param c
	 *            is a Comment on that Element
	 */
	private static void addEntry(Element e, Comment c) {
		if (!commentMap.containsKey(e)) {
			commentMap.put(e, new ArrayList<Comment>());
		}
		if (c != null) {
			commentMap.get(e).add(c);
		}
	}

	/**
	 * Retrieve all the comments for an Element
	 * 
	 * @param e
	 *            the Element
	 * @return the List of all Comments for that Element
	 */
	public static List<Comment> allComments(Element e) {
		List<Comment> list = commentMap.get(e);
		if (list != null) {
			return list;
		}
		return new ArrayList<Comment>();
	}

}
