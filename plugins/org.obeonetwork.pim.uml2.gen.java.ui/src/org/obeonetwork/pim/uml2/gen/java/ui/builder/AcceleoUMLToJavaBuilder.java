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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.acceleo.common.utils.ModelUtils;
import org.eclipse.acceleo.model.mtl.resource.AcceleoResourceSetImpl;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.obeonetwork.pim.uml2.gen.java.main.Uml2java;
import org.obeonetwork.pim.uml2.gen.java.ui.UML2JavaUIActivator;

/**
 * The builder of AcceleoUMLToJava projects.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class AcceleoUMLToJavaBuilder extends IncrementalProjectBuilder {

	/**
	 * The ID of the builder.
	 */
	public static final String BUILDER_ID = "org.obeonetwork.pim.uml2.gen.java.ui.acceleoUMLToJavaBuilder";

	/**
	 * Indicates if the build has generated some content.
	 */
	private boolean hasGenerated;

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.core.resources.IncrementalProjectBuilder#build(int, java.util.Map,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	protected IProject[] build(int kind, Map args, final IProgressMonitor monitor) throws CoreException {
		hasGenerated = false;

		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}

		if (hasGenerated) {
			this.refresh(monitor);
		}

		return null;
	}

	/**
	 * Refresh the project after the generation.
	 * 
	 * @param monitor
	 *            The progress monitor.
	 */
	protected void refresh(final IProgressMonitor monitor) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					final int sleepTime = 500;
					Thread.sleep(sleepTime);
				} catch (InterruptedException e1) {
					// do not log
				}
				try {
					getProject().build(IncrementalProjectBuilder.FULL_BUILD,
							"org.eclipse.jdt.core.javabuilder", new HashMap<String, String>(), monitor);
				} catch (CoreException e) {
					IStatus status = new Status(IStatus.ERROR, UML2JavaUIActivator.PLUGIN_ID, e.getMessage(),
							e);
					UML2JavaUIActivator.getDefault().getLog().log(status);
				}
			}
		};

		thread.setPriority(Thread.MIN_PRIORITY);

		thread.start();
	}

	/**
	 * Build the complete project.
	 * 
	 * @param monitor
	 *            The progress monitor.
	 */
	protected void fullBuild(final IProgressMonitor monitor) {
		IProject project = getProject();
		List<IFile> umlFiles = new ArrayList<IFile>();
		umlFiles = computeUMLFiles(umlFiles, project);

		for (IFile iFile : umlFiles) {
			if (!monitor.isCanceled()) {
				generate(iFile, monitor);
			}
		}
	}

	/**
	 * Returns the list of uml files contained in the project.
	 * 
	 * @param files
	 *            The files to build
	 * @param container
	 *            The container where the files should be built
	 * @return The list of uml files contained in the project.
	 */
	private List<IFile> computeUMLFiles(List<IFile> files, IContainer container) {
		try {
			IResource[] members = container.members();
			for (IResource member : members) {
				if (member instanceof IContainer) {
					computeUMLFiles(files, (IContainer)member);
				} else if (member instanceof IFile) {
					IFile memberFile = (IFile)member;
					if ("uml".equals(memberFile.getFileExtension())) {
						files.add(memberFile);
					}
				}
			}
		} catch (CoreException e) {
			IStatus status = new Status(IStatus.ERROR, UML2JavaUIActivator.PLUGIN_ID, e.getMessage(), e);
			UML2JavaUIActivator.getDefault().getLog().log(status);
		}
		return files;
	}

	/**
	 * Launch an incremental build of the project.
	 * 
	 * @param delta
	 *            The resource delta.
	 * @param monitor
	 *            The progress monitor.
	 */
	protected void incrementalBuild(IResourceDelta delta, final IProgressMonitor monitor) {
		try {
			delta.accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) throws CoreException {
					IResource resource = delta.getResource();
					if (resource instanceof IFile && "uml".equals(resource.getFileExtension())) {
						IFile file = (IFile)resource;
						switch (delta.getKind()) {
							case IResourceDelta.ADDED:
								// handle added resource
								generate(file, monitor);
								break;
							case IResourceDelta.REMOVED:
								// handle removed resource
								break;
							case IResourceDelta.CHANGED:
								// handle changed resource
								generate(file, monitor);
								break;
							default:
								break;
						}
					}
					return true;
				}
			});
		} catch (CoreException e) {
			IStatus status = new Status(IStatus.ERROR, UML2JavaUIActivator.PLUGIN_ID, e.getMessage(), e);
			UML2JavaUIActivator.getDefault().getLog().log(status);
		}

	}

	/**
	 * Launches the generation with the given model.
	 * 
	 * @param model
	 *            The uml model.
	 * @param progressMonitor
	 *            The progress monitor.
	 */
	protected void generate(IFile model, IProgressMonitor progressMonitor) {
		IProject project = getProject();
		IFile propertiesFile = project.getFile(ToggleNatureAction.PROPERTIES_FILE);
		Properties properties = new Properties();
		try {
			properties.load(propertiesFile.getContents());
			Object output = properties.get(model.getProjectRelativePath().toString());
			if (output instanceof String) {
				String outputPath = (String)output;
				Object clear = properties.get(outputPath + "__clear");

				IFolder targetFolder = project.getFolder(new Path(outputPath));

				if (targetFolder.exists()) {
					if (clear instanceof String && "true".equals(((String)clear).trim())) {
						IResource[] members = targetFolder.members();
						for (IResource member : members) {
							member.delete(true, progressMonitor);
						}
					}
				}

				ResourceSet resourceSet = new AcceleoResourceSetImpl();
				EObject eObject = ModelUtils.load(model.getLocation().toFile(), resourceSet);
				Uml2java workflow = new Uml2java(eObject, targetFolder.getLocation().toFile(),
						new ArrayList<String>());
				workflow.doGenerate(BasicMonitor.toMonitor(progressMonitor));

				targetFolder.refreshLocal(IResource.DEPTH_INFINITE, progressMonitor);
				this.refresh(progressMonitor);
				this.hasGenerated = true;
			}
		} catch (IOException e) {
			IStatus status = new Status(IStatus.ERROR, UML2JavaUIActivator.PLUGIN_ID, e.getMessage(), e);
			UML2JavaUIActivator.getDefault().getLog().log(status);
		} catch (CoreException e) {
			IStatus status = new Status(IStatus.ERROR, UML2JavaUIActivator.PLUGIN_ID, e.getMessage(), e);
			UML2JavaUIActivator.getDefault().getLog().log(status);
		}
	}

}
