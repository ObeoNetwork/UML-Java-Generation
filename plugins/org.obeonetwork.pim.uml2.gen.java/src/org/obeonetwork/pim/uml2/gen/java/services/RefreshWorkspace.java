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
