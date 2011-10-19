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


import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.obeonetwork.pim.uml2.gen.java.ui.UML2JavaUIActivator;

/**
 * The shortcut launcher for UML to Java launch configurations.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class LaunchShortcut implements ILaunchShortcut {

	/**
	 * UML models for the generation.
	 */
	Set<IFile> files = new LinkedHashSet<IFile>();
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.jface.viewers.ISelection, java.lang.String)
	 */
	public void launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			List<?> list = ((IStructuredSelection) selection).toList();
			for (Object object : list) {
				if (object instanceof IFile && ((IFile)object).getFileExtension() != null && "uml".equals(((IFile)object).getFileExtension())) {
					files.add((IFile) object);
				}
			}
		}
		
		if (files.size() > 0) {
			generate(mode);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart, java.lang.String)
	 */
	public void launch(IEditorPart editor, String mode) {
		IEditorInput input = editor.getEditorInput();
		if (input instanceof IAdaptable) {
			IFile file = (IFile) ((IAdaptable)input).getAdapter(IFile.class);
			if (file != null && file.getFileExtension() != null && "uml".equals(file.getFileExtension())) {
				files.add(file);
			}
		}
		
		if (files.size() > 0) {
			generate(mode);
		}
	}
	
	/**
	 * Launches the generation.
	 * 
	 * @param mode The generation mode (run always)
	 */
	private void generate(String mode) {
		// Finds or creates a launch configuration for these UML models.
		ILaunchConfiguration launchConfiguration = this.findLaunchConfiguration();
		if (launchConfiguration == null) {
			launchConfiguration = this.createConfiguration();
		}
		
		// Launch it
		if (launchConfiguration != null && launchConfiguration.exists()) {
			DebugUITools.launch(launchConfiguration, mode);
		}
	}
	
	/**
	 * Returns a newly created launch configuration for the available ".uml" models.
	 * 
	 * @return A newly created launch configuration for the available ".uml" models.
	 */
	@SuppressWarnings("deprecation")
	protected ILaunchConfiguration createConfiguration() {
		ILaunchConfiguration config = null;
		ILaunchConfigurationWorkingCopy wc = null;
		try {
			Set<String> computedModelPaths = new LinkedHashSet<String>();
			for (IFile file : files) {
				computedModelPaths.add(file.getFullPath().toString());
			}
			
			ILaunchConfigurationType configType = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurationType("org.obeonetwork.pim.uml2.gen.java.ui.launchConfigurationType");
			wc = configType.newInstance(null, DebugPlugin.getDefault().getLaunchManager().generateUniqueLaunchConfigurationNameFrom("UML2Java"));
			wc.setAttribute(IUML2JavaContants.ATTR_TARGET_FOLDER_PATH, "");
			wc.setAttribute(IUML2JavaContants.ATTR_MODEL_PATHS, computedModelPaths);
			wc.setMappedResources(files.toArray(new IResource[files.size()]));
			config = wc.doSave();
			
			IStructuredSelection selection;
			if (config == null) {
				selection = new StructuredSelection();
			} else {
				selection = new StructuredSelection(config);
			}
			DebugUITools.openLaunchConfigurationDialogOnGroup(PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getShell(), selection, IDebugUIConstants.ID_RUN_LAUNCH_GROUP);
		} catch (CoreException e) {
			IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
			if (window != null) {
				MessageDialog.openError(window.getShell(), "", e.getStatus().getMessage());
			} else {
				UML2JavaUIActivator.getDefault().getLog().log(e.getStatus());
			}
		}
		return config;
	}
	
	/**
	 * Returns the first UML to Java launch configuration using all the selected ".uml" models.
	 * 
	 * @return The first UML to Java launch configuration using all the selected ".uml" models.
	 */
	protected ILaunchConfiguration findLaunchConfiguration() {
		Set<String> computedModelPaths = new LinkedHashSet<String>();
		for (IFile file : files) {
			computedModelPaths.add(file.getFullPath().toString());
		}
		
		ILaunchConfigurationType configurationType = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurationType("org.obeonetwork.pim.uml2.gen.java.ui.launchConfigurationType");
		try {
			ILaunchConfiguration[] launchConfigurations = DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurations(configurationType);
			for (ILaunchConfiguration iLaunchConfiguration : launchConfigurations) {
				Set<String> modelPaths = new LinkedHashSet<String>();
				Set<?> attributes = iLaunchConfiguration.getAttribute(IUML2JavaContants.ATTR_MODEL_PATHS, new LinkedHashSet<String>());
				for (Object attribute : attributes) {
					if (attribute instanceof String) {
						modelPaths.add((String) attribute);
					}
				}
				
				if (modelPaths.size() == computedModelPaths.size() && modelPaths.containsAll(computedModelPaths)) {
					return iLaunchConfiguration;
				}
			}
		} catch (CoreException e) {
			IStatus status = new Status(IStatus.ERROR, UML2JavaUIActivator.PLUGIN_ID, e.getMessage(), e);
			UML2JavaUIActivator.getDefault().getLog().log(status);
		}
		return null;
	}

}
