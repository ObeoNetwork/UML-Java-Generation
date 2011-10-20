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
package org.obeonetwork.pim.uml2.gen.java.ui.builder;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.obeonetwork.pim.uml2.gen.java.ui.UML2JavaUIActivator;

/**
 * Utility class to toggle the nature.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class ToggleNatureAction implements IObjectActionDelegate {
	
	/**
	 * The name of the properties file that configures the Acceleo builder.
	 */
	public static final String PROPERTIES_FILE = "uml2java.properties";

	/**
	 * The current selection.
	 */
	private ISelection selection;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(IAction)
	 **/
	@SuppressWarnings("rawtypes")
	public void run(IAction action) {
		if (selection instanceof IStructuredSelection) {
			for (Iterator it = ((IStructuredSelection) selection).iterator(); it
					.hasNext();) {
				Object element = it.next();
				IProject project = null;
				if (element instanceof IProject) {
					project = (IProject) element;
				} else if (element instanceof IAdaptable) {
					project = (IProject) ((IAdaptable) element)
							.getAdapter(IProject.class);
				}
				if (project != null) {
					toggleNature(project);
				}
			}
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 *      org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		// do nothing
	}

	/**
	 * Toggles sample nature on a project
	 * 
	 * @param project
	 *            to have sample nature added or removed
	 */
	private void toggleNature(IProject project) {
		try {
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();

			for (int i = 0; i < natures.length; ++i) {
				if (AcceleoUMLToJavaNature.NATURE_ID.equals(natures[i])) {
					// Remove the nature
					String[] newNatures = new String[natures.length - 1];
					System.arraycopy(natures, 0, newNatures, 0, i);
					System.arraycopy(natures, i + 1, newNatures, i,
							natures.length - i - 1);
					description.setNatureIds(newNatures);
					project.setDescription(description, null);
					return;
				}
			}

			// Add the nature
			String[] newNatures = new String[natures.length + 1];
			newNatures[0] = AcceleoUMLToJavaNature.NATURE_ID;
			System.arraycopy(natures, 0, newNatures, 1, natures.length);
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
			
			// Generate the property file
			IFile propertiesFile = project.getFile(PROPERTIES_FILE);
			if (!propertiesFile.exists()) {
				final String nl = System.getProperty("line.separator");
				
				StringBuffer content = new StringBuffer();
				content.append("##############################################################");
				content.append(nl);
				content.append("# Acceleo UML2 to Java configuration file");
				content.append(nl);
				content.append("#");
				content.append(nl);
				content.append("# For each UML file in your project, enter a source folder");
				content.append(nl);
				content.append("# in which the Java classes will be generated. Example:");
				content.append(nl);
				content.append("# model/subfolder/myUMLModel.uml = src-uml-generated");
				content.append(nl);
				content.append("# ");
				content.append(nl);
				content.append("# You can also specify if you want the output folder to be");
				content.append(nl);
				content.append("# cleared before a new generation. Example:");
				content.append(nl);
				content.append("# src-uml-generated__clear = true");
				content.append(nl);
				content.append("# ");
				content.append(nl);
				content.append("# By default, the output folder is NOT cleared before a new");
				content.append(nl);
				content.append("# generation.");
				content.append(nl);
				content.append("##############################################################");
				content.append(nl);
				content.append(nl);
				
				InputStream source = new ByteArrayInputStream(content.toString().getBytes());
				propertiesFile.create(source, true, new NullProgressMonitor());
			}
		} catch (CoreException e) {
			IStatus status = new Status(IStatus.ERROR, UML2JavaUIActivator.PLUGIN_ID, e.getMessage(), e);
			UML2JavaUIActivator.getDefault().getLog().log(status);
		}
	}

}
