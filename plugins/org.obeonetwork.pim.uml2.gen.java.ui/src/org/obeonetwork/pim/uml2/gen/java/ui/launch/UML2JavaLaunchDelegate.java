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
package org.obeonetwork.pim.uml2.gen.java.ui.launch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.obeonetwork.pim.uml2.gen.java.main.Workflow;

/**
 * The UML to Java launch configuration launcher.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class UML2JavaLaunchDelegate implements ILaunchConfigurationDelegate {

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration, java.lang.String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor) throws CoreException {
		String targetFolder = "";
		try {
			targetFolder = configuration.getAttribute(IUML2JavaContants.ATTR_TARGET_FOLDER_PATH, "");
		} catch (CoreException e) {
			
		}
		
		Set<String> umlModelPaths = new LinkedHashSet<String>();
		try {
			Set<?> attributes = configuration.getAttribute(IUML2JavaContants.ATTR_MODEL_PATHS, new LinkedHashSet<String>());
			for (Object attribute : attributes) {
				if (attribute instanceof String) {
					umlModelPaths.add((String) attribute);
				}
			}
		} catch (CoreException e) {

		}
		
		for (String umlModelPath : umlModelPaths) {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(umlModelPath));
			IContainer container = ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(targetFolder));
			if (file!= null && container != null && file.isAccessible() && container.isAccessible()) {
				URI modelURI = URI.createPlatformResourceURI(file.getFullPath().toString(), true);
				try {
					Workflow workflow = new Workflow(modelURI, container.getLocation().toFile(), new ArrayList<String>());
					workflow.doGenerate(BasicMonitor.toMonitor(monitor));
					
					container.refreshLocal(IResource.DEPTH_INFINITE, monitor);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
