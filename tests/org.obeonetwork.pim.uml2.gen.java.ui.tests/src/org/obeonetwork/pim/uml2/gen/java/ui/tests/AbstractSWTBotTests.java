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
package org.obeonetwork.pim.uml2.gen.java.ui.tests;

import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swtbot.eclipse.finder.SWTWorkbenchBot;
import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.osgi.framework.Bundle;

public class AbstractSWTBotTests {
	/**
	 * The SWTBot.
	 */
	protected static SWTWorkbenchBot bot;
	
	/**
	 * The SWTBot tests bundle.
	 */
	private static Bundle bundle;

	/**
	 * Initialize the SWTBot.
	 */
	@BeforeClass
	public static void setUp() {
		bundle = Platform.getBundle("org.obeonetwork.pim.uml2.gen.java.ui.tests"); //$NON-NLS-1$	
		
		bot = new SWTWorkbenchBot();
		List<SWTBotView> views = bot.views();
		for (SWTBotView view : views) {
			if ("Welcome".equals(view.getTitle())) {
				view.close();
			}
		}
		bot.perspectiveByLabel("Java").activate();
		
		// Create "test" Java project
		bot.menu("File").menu("New").menu("Project...").click();

		SWTBotShell shell = bot.shell("New Project");
		shell.activate();
		bot.tree().getTreeItem("Java Project").select(); //$NON-NLS-1$
		bot.button("Next >").click();

		bot.textWithLabel("Project name:").setText("javatestproject"); //$NON-NLS-1$
		bot.button("Finish").click();
		
		// put the uml model inside
		File file = createFile("/data/example.uml");
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("javatestproject");
		IFile model = project.getFile("example.uml");
		
		
		try {
			model.create(new FileInputStream(file), IFile.FORCE, new NullProgressMonitor());
		} catch (FileNotFoundException e) {
			fail(e.getMessage());
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}
	
	private static File createFile(String pathName) {
		try {
			String fileLocation = FileLocator.resolve(bundle.getEntry(pathName)).getPath();
			return new File(fileLocation);
		} catch (IOException e) {
			throw new AssertionFailedError(e.getMessage());
		} catch (NullPointerException e) {
			/*
			 * on the server the unit test fails with an NPE :S
			 */
			throw new AssertionFailedError(e.getMessage());
		}
	}
	
	@AfterClass
	public static void tearDown() throws CoreException {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject iProject : projects) {
			iProject.delete(true, new NullProgressMonitor());
		}
	}
}
