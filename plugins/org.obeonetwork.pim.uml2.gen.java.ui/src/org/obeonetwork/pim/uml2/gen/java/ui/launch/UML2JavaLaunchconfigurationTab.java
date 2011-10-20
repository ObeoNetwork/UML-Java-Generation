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
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;
import org.obeonetwork.pim.uml2.gen.java.ui.UML2JavaUIActivator;

/**
 * The UML to Java launch configuration tab.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 1.0
 */
public class UML2JavaLaunchconfigurationTab extends AbstractLaunchConfigurationTab {

	/**
	 * The table containing all the workspace relative path of the UML models.
	 */
	private Table modelTable;
	
	/**
	 * The workspace relative path of the output folder.
	 */
	private Text targetText;
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Font font = parent.getFont();
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setFont(font);
    	GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		composite.setLayoutData(gd);
		
		Group modelGroup = createGroup(composite, "UML Models", 3, 1, GridData.FILL_HORIZONTAL);
				
		modelTable = new Table(modelGroup, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		gridData.heightHint = 100;
		gridData.minimumHeight = 100;
		modelTable.setLayoutData(gridData);
		
		Composite tableButtonComposite = new Composite(modelGroup, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		tableButtonComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		tableButtonComposite.setLayout(layout);
		
		Button addButton = new Button(tableButtonComposite, SWT.PUSH);
		Image addImage = UML2JavaUIActivator.getDefault().getImage("icons/add_obj.gif"); //$NON-NLS-1$
		addButton.setImage(addImage);
		addButton.setToolTipText("Add a new UML model to the table");
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse();
				update();
				updateLaunchConfigurationDialog();
			}
		});
		Button removeButton = new Button(tableButtonComposite, SWT.PUSH);
		Image removeImage = UML2JavaUIActivator.getDefault().getImage("icons/delete_obj.gif"); //$NON-NLS-1$
		removeButton.setImage(removeImage);
		removeButton.setToolTipText("Remove the selected entry from the table");
		removeButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int[] selectionIndices = modelTable.getSelectionIndices();
				modelTable.remove(selectionIndices);
				update();
				updateLaunchConfigurationDialog();
			}
		});
		this.createHelpButton(tableButtonComposite, "The list of the UML models for which Java source code will be generated.");
		
		Group outputGroup = createGroup(composite, "Output Folder", 2, 1, GridData.FILL_HORIZONTAL);
		Composite comp = new Composite(outputGroup, SWT.NONE);
		layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		comp.setLayout(layout);
		comp.setFont(font);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		comp.setLayoutData(gd);
		
		targetText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		targetText.setFont(composite.getFont());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		targetText.setLayoutData(gd);
		
		targetText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				updateLaunchConfigurationDialog();
			}
		});
		final Button targetButton = createPushButton(comp, "Browse...", null);
		targetButton.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
			}

			public void widgetSelected(SelectionEvent e) {
				ContainerSelectionDialog dialog = new ContainerSelectionDialog(getShell(), ResourcesPlugin
						.getWorkspace().getRoot(), true, "Select the output folder");
				dialog.showClosedProjects(false);
				if (dialog.open() == Window.OK) {
					Object[] result = dialog.getResult();
					if (result.length == 1 && result[0] instanceof Path) {
						targetText.setText(((Path)result[0]).toString());
					}
				}
				updateLaunchConfigurationDialog();
				update();
			}
		});
		createHelpButton(comp, "Select the output folder where the code will be generated.");
		
		this.setControl(composite);
		update();
		
		//Group propertiesGroup = createGroup(parent, "Additional Properties", 2, 1, GridData.FILL_HORIZONTAL);
	}
	
	/**
	 * Browse the available ".uml" models in the workspace.
	 */
	private void browse() {
		Set<IFile> files = new LinkedHashSet<IFile>();
		
		FilteredResourcesSelectionDialog dialog = new FilteredResourcesSelectionDialog(getShell(), false,
				ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
		dialog.setTitle("Select UML Models");
		dialog.setInitialPattern("*.uml");
		dialog.open();
		if (dialog.getResult() != null && dialog.getResult().length > 0) {
			Object[] results = dialog.getResult();
			for (Object result : results) {
				if (result instanceof IFile) {
					files.add((IFile) result);
				}
			}
		}
		
		for (IFile file : files) {
			boolean exists = false;
			TableItem[] items = this.modelTable.getItems();
			for (TableItem tableItem : items) {
				if (tableItem.getText().equals(file.getFullPath().toString())) {
					exists = true;
				}
			}
			if (!exists) {
				TableItem item = new TableItem(this.modelTable, SWT.NONE);
				item.setText(file.getFullPath().toString());
			}
		}
	}
	
	/**
	 * Update the launch configuration and check potential errors.
	 */
	private void update() {
		this.setErrorMessage(null);
		if (modelTable != null && modelTable.getItemCount() == 0) {
			this.setErrorMessage("At least one UML model is required to launch the generation");
		} else if (targetText != null) {
			String text = targetText.getText();
			if (text != null && text.length() > 0) {				
				IFolder folder = ResourcesPlugin.getWorkspace().getRoot().getFolder(new Path(text));
				if (folder != null && !folder.exists()) {
					this.setErrorMessage("Missing target folder");
				}
			}
		}
	}
	
	/**
	 * Creates a Group widget.
	 * 
	 * @param parent
	 *            the parent composite to add this group to
	 * @param text
	 *            the text for the heading of the group
	 * @param columns
	 *            the number of columns within the group
	 * @param hspan
	 *            the horizontal span the group should take up on the parent
	 * @param fill
	 *            the style for how this composite should fill into its parent Can be one of
	 *            <code>GridData.FILL_HORIZONAL</code>, <code>GridData.FILL_BOTH</code> or
	 *            <code>GridData.FILL_VERTICAL</code>
	 * @return the new group
	 */
	private Group createGroup(Composite parent, String text, int columns, int hspan, int fill) {
		Group g = new Group(parent, SWT.NONE);
		g.setLayout(new GridLayout(columns, false));
		g.setText(text);
		g.setFont(parent.getFont());
		GridData gd = new GridData(fill);
		gd.horizontalSpan = hspan;
		g.setLayoutData(gd);
		return g;
	}
	
	/**
	 * Creates a help button.
	 * 
	 * @param parent
	 *            The composite parent
	 * @param helpMessage
	 *            the help message
	 */
	private void createHelpButton(Composite parent, String helpMessage) {
		Image image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_LCL_LINKTO_HELP);
		ToolBar result = new ToolBar(parent, SWT.FLAT | SWT.NO_FOCUS);
		result.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		ToolItem item = new ToolItem(result, SWT.NONE);
		item.setImage(image);
		if (helpMessage != null && !"".equals(helpMessage)) { //$NON-NLS-1$
			item.setToolTipText(helpMessage);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(IUML2JavaContants.ATTR_MODEL_PATHS, new LinkedHashSet<String>());
		configuration.setAttribute(IUML2JavaContants.ATTR_TARGET_FOLDER_PATH, "");
		if (targetText != null && modelTable != null) {			
			targetText.setText("");
			for (int i = 0; i < modelTable.getItemCount(); i++) {
				modelTable.remove(0);
			}
		}
		this.update();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration configuration) {
		// Clear
		targetText.setText("");
		int itemCount = this.modelTable.getItemCount();
		for (int i = 0; i < itemCount; i++) {
			modelTable.remove(0);
		}
		
		String targetFolder = "";
		try {
			targetFolder = configuration.getAttribute(IUML2JavaContants.ATTR_TARGET_FOLDER_PATH, "");
		} catch (CoreException e) {
			
		}
		targetText.setText(targetFolder);
		
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
			boolean exists = false;
			TableItem[] items = this.modelTable.getItems();
			for (TableItem tableItem : items) {
				if (tableItem.getText().equals(umlModelPath)) {
					exists = true;
				}
			}
			if (!exists) {
				TableItem item = new TableItem(this.modelTable, SWT.NONE);
				item.setText(umlModelPath);
			}
		}
		this.update();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(IUML2JavaContants.ATTR_TARGET_FOLDER_PATH, targetText.getText());
		Set<String> umlModelPaths = new LinkedHashSet<String>();
		TableItem[] items = modelTable.getItems();
		for (TableItem tableItem : items) {
			umlModelPaths.add(tableItem.getText());
		}
		configuration.setAttribute(IUML2JavaContants.ATTR_MODEL_PATHS, umlModelPaths);
		this.update();
	}	

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return "Main";
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getImage()
	 */
	@Override
	public Image getImage() {
		return UML2JavaUIActivator.getDefault().getImage("icons/acceleo_module_16x16.png");
	}
	
}
