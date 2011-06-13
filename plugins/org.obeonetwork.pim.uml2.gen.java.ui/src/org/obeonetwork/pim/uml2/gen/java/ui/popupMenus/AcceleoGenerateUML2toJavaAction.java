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
package org.obeonetwork.pim.uml2.gen.java.ui.popupMenus;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.obeonetwork.pim.uml2.gen.java.ui.UML2JavaUIActivator;
import org.obeonetwork.pim.uml2.gen.java.ui.common.GenerateAll;
import org.eclipse.ui.IActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionDelegate;

/**
 * UML2 to Java code generation.
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 0.1
 */
public class AcceleoGenerateUML2toJavaAction extends ActionDelegate implements IActionDelegate {
	
	/**
	 * Selected model files.
	 */
	protected List<IFile> files;

	/**{@inheritDoc}
	 *
	 * @see org.eclipse.ui.actions.ActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public void selectionChanged(IAction action, ISelection selection) {
		if (selection instanceof IStructuredSelection) {
			files = ((IStructuredSelection) selection).toList();
		}
	}

	/**{@inheritDoc}
	 *
	 * @see org.eclipse.ui.actions.ActionDelegate#run(org.eclipse.jface.action.IAction)
	 * @generated
	 */
	public void run(IAction action) {
		if (files != null) {
			IRunnableWithProgress operation = new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) {
					try {
						Iterator<IFile> filesIt = files.iterator();
						while (filesIt.hasNext()) {
							IFile model = (IFile)filesIt.next();
							URI modelURI = URI.createPlatformResourceURI(model.getFullPath().toString(), true);
							try {
								IContainer target = model.getProject().getFolder("src-gen");
								GenerateAll generator = new GenerateAll(modelURI, target.getLocation().toFile(), getArguments());
								generator.doGenerate(monitor);
							} catch (IOException e) {
								IStatus status = new Status(IStatus.ERROR, UML2JavaUIActivator.PLUGIN_ID, e.getMessage(), e);
								UML2JavaUIActivator.getDefault().getLog().log(status);
							} finally {
								model.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
							}
						}
					} catch (CoreException e) {
						IStatus status = new Status(IStatus.ERROR, UML2JavaUIActivator.PLUGIN_ID, e.getMessage(), e);
						UML2JavaUIActivator.getDefault().getLog().log(status);
					}
				}
			};
			try {
				PlatformUI.getWorkbench().getProgressService().run(true, true, operation);
			} catch (InvocationTargetException e) {
				IStatus status = new Status(IStatus.ERROR, UML2JavaUIActivator.PLUGIN_ID, e.getMessage(), e);
				UML2JavaUIActivator.getDefault().getLog().log(status);
			} catch (InterruptedException e) {
				IStatus status = new Status(IStatus.ERROR, UML2JavaUIActivator.PLUGIN_ID, e.getMessage(), e);
				UML2JavaUIActivator.getDefault().getLog().log(status);
			}
		}
	}

	/**
	 * Computes the arguments of the generator.
	 * 
	 * @return the arguments
	 * @generated
	 */
	protected List<? extends Object> getArguments() {
		return new ArrayList<String>();
	}

}