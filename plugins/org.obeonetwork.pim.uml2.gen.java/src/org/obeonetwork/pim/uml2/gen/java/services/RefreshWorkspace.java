/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stephane Begaudeau (Obeo) - initial API and implementation
 *     Mathieu Cartaud (Obeo) - OSGi implementation
 *******************************************************************************/
package org.obeonetwork.pim.uml2.gen.java.services;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.uml2.uml.Component;


public class RefreshWorkspace {

	public void addProject(Component aComponent){
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		try
		{
			IWorkspaceRoot workspaceRoot = workspace.getRoot();
			IProjectDescription descr = workspace.loadProjectDescription(workspaceRoot.getLocation().append(aComponent.getLabel()).append(".project"));
			IProject project = workspaceRoot.getProject(aComponent.getLabel());
			project.create(descr, new NullProgressMonitor());
			project.open(new NullProgressMonitor());
		} catch (CoreException ce)
		{
			ce.printStackTrace();
		}
	}
	
}
