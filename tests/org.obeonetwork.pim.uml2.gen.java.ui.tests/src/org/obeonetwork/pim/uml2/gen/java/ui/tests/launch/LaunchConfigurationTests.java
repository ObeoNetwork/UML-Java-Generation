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
package org.obeonetwork.pim.uml2.gen.java.ui.tests.launch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.junit.Test;
import org.obeonetwork.pim.uml2.gen.java.ui.tests.AbstractSWTBotTests;

public class LaunchConfigurationTests extends AbstractSWTBotTests {
	
	@Test
	public void testBasicLaunchConfiguration() {
		SWTBotShell[] shells = bot.shells();
		if (shells.length > 0) {
			shells[0].activate();
		}
		bot.menu("Run").menu("Run Configurations...").click();
		SWTBotShell shell = bot.shell("Run Configurations");
		shell.activate();

		bot.tree().getTreeItem("Acceleo UML2 to Java Generation").click().select().setFocus();
		bot.tree().getTreeItem("Acceleo UML2 to Java Generation").click().select().doubleClick();
		
		bot.buttonWithTooltip("Add a new UML model to the table").click();
		bot.button("OK").click();
		
		bot.button("Browse...").click();
		shell = bot.shell("Folder Selection");
		shell.activate();
		bot.text().setText("javatestproject/src");
		bot.button("OK").click();
		
		bot.button("Run").click();
		bot.sleep(10000);
		
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject("javatestproject");
		assertTrue(project.exists() && project.isAccessible());
		IFolder folder = project.getFolder("src");
		folder = folder.getFolder("org");
		folder = folder.getFolder("eclipse");
		folder = folder.getFolder("acceleo");
		folder = folder.getFolder("java");
		assertTrue(folder.exists() && folder.isAccessible());
		try {
			assertEquals(6, folder.members().length);
		} catch (CoreException e) {
			fail(e.getMessage());
		}
	}
}
