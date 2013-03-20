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
package org.obeonetwork.pim.uml2.gen.java.services;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.acceleo.engine.AcceleoEnginePlugin;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.EMFPlugin;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jdt.launching.environments.IExecutionEnvironment;
import org.eclipse.jdt.launching.environments.IExecutionEnvironmentsManager;
import org.obeonetwork.pim.uml2.gen.java.utils.IUML2JavaConstants;

/**
 * Services for workspace related operations.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 2.0
 */
public class WorkspaceServices {

	/**
	 * Returns <code>true</code> if the file exists, <code>false</code> otherwise.
	 * 
	 * @param path
	 *            The absolute path of the file on the file system
	 * @return <code>true</code> if the file exists, <code>false</code> otherwise.
	 */
	public boolean resourceExists(String path) {
		return new File(path).exists();
	}

	/**
	 * Imports a new project, created outside of the workspace, to the workspace.
	 * 
	 * @param projectName
	 *            The name of the project.
	 */
	public void importProject(String projectName) {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			return;
		}

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		try {
			IWorkspaceRoot workspaceRoot = workspace.getRoot();
			IProjectDescription descr = workspace.loadProjectDescription(workspaceRoot.getLocation().append(
					projectName).append(".project"));
			IProject project = workspaceRoot.getProject(projectName);
			if (project.exists()) {
				if (!project.isOpen()) {
					project.open(new NullProgressMonitor());
				}
			} else {
				project.create(descr, new NullProgressMonitor());
				project.open(new NullProgressMonitor());
			}
		} catch (CoreException ce) {
			AcceleoEnginePlugin.log(ce, true);
		}
	}

	/**
	 * Creates a project from scratch in the workspace.
	 * 
	 * @param eObject
	 *            The model element
	 */
	public void createDefaultProject(EObject eObject) {
		if (!EMFPlugin.IS_ECLIPSE_RUNNING) {
			return;
		}

		IProgressMonitor monitor = new NullProgressMonitor();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		try {
			IWorkspaceRoot workspaceRoot = workspace.getRoot();

			String projectName = UML2JavaConfigurationHolder.getDefaultProjectName(eObject);
			IProject project = workspaceRoot.getProject(projectName);

			if (project.exists() && project.isAccessible()) {
				if (!project.isOpen()) {
					project.open(monitor);
				}
			} else {
				project.create(new NullProgressMonitor());
				project.open(new NullProgressMonitor());

				IContainer intputContainer = project;

				String sourceFolderName = UML2JavaConfigurationHolder.getSourceFolderPath(eObject);
				StringTokenizer stringTokenizer = new StringTokenizer(sourceFolderName, "/");
				while (stringTokenizer.hasMoreTokens()) {
					String token = stringTokenizer.nextToken();
					IFolder src = intputContainer.getFolder(new Path(token));
					src.create(true, true, monitor);

					intputContainer = src;
				}

				IContainer outputContainer = project;

				String outputFolderName = UML2JavaConfigurationHolder.getOutputFolderPath(eObject);
				stringTokenizer = new StringTokenizer(outputFolderName, "/");
				while (stringTokenizer.hasMoreTokens()) {
					String token = stringTokenizer.nextToken();
					IFolder out = outputContainer.getFolder(new Path(token));
					out.create(true, true, monitor);

					outputContainer = out;
				}

				IProjectDescription description = project.getDescription();
				String[] natures = new String[] {};
				if (IUML2JavaConstants.Default.DEFAULT_COMPONENT_ARTIFACTS_TYPE_OSGI
						.equals(UML2JavaConfigurationHolder.getComponentBasedArchitecture(eObject))
						|| IUML2JavaConstants.Default.DEFAULT_COMPONENT_ARTIFACTS_TYPE_ECLIPSE
								.equals(UML2JavaConfigurationHolder.getComponentBasedArchitecture(eObject))) {
					natures = new String[] {JavaCore.NATURE_ID, IUML2JavaConstants.PDE_PLUGIN_NATURE_ID };
				} else {
					natures = new String[] {JavaCore.NATURE_ID, };
				}
				description.setNatureIds(natures);
				project.setDescription(description, monitor);

				IJavaProject javaProject = JavaCore.create(project);

				List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
				IExecutionEnvironmentsManager executionEnvironmentsManager = JavaRuntime
						.getExecutionEnvironmentsManager();
				IExecutionEnvironment[] executionEnvironments = executionEnvironmentsManager
						.getExecutionEnvironments();

				String defaultJREExecutionEnvironment = UML2JavaConfigurationHolder
						.getJREExecutionEnvironment(eObject);
				for (IExecutionEnvironment iExecutionEnvironment : executionEnvironments) {
					if (defaultJREExecutionEnvironment.equals(iExecutionEnvironment.getId())) {
						entries.add(JavaCore.newContainerEntry(JavaRuntime
								.newJREContainerPath(iExecutionEnvironment)));
						break;
					}
				}

				javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), null);

				IClasspathEntry[] oldEntries = javaProject.getRawClasspath();
				IClasspathEntry[] newEntries = new IClasspathEntry[oldEntries.length + 1];
				System.arraycopy(oldEntries, 0, newEntries, 0, oldEntries.length);

				javaProject.setOutputLocation(outputContainer.getFullPath(), monitor);

				IPackageFragmentRoot packageRoot = javaProject.getPackageFragmentRoot(intputContainer
						.getFullPath().toString());
				newEntries[oldEntries.length] = JavaCore.newSourceEntry(packageRoot.getPath(), new Path[] {},
						new Path[] {}, outputContainer.getFullPath());

				javaProject.setRawClasspath(newEntries, null);
			}
		} catch (CoreException coreException) {
			AcceleoEnginePlugin.log(coreException, true);
		}
	}
}
