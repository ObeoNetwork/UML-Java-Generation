/*******************************************************************************
 * Copyright (c) 2008, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.obeonetwork.pim.uml2.gen.java.ui.launch.tabs;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.obeonetwork.pim.uml2.gen.java.ui.UML2JavaUIActivator;
import org.obeonetwork.pim.uml2.gen.java.ui.utils.UML2JavaMessages;
import org.obeonetwork.pim.uml2.gen.java.utils.IUML2JavaConstants;

/**
 * The type tab of the launch configuration.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 2.0
 */
public class UML2JavaTypeLaunchConfigurationTab extends AbstractUML2JavaLaunchConfigurationTab {

	/**
	 * Ordered and unique collection Java type.
	 */
	private Text orderedAndUniqueText;

	/**
	 * Ordered and not unique collection Java type.
	 */
	private Text orderedAndNotUniqueText;

	/**
	 * Not ordered and unique collection Java type.
	 */
	private Text notOrderedAndUniqueText;

	/**
	 * Not ordered and not unique collection Java type.
	 */
	private Text notOrderedAndNotUniqueText;

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

		this.createCollectionGroup(composite, font);
		this.createConversionGroup(composite, font);

		this.setControl(composite);
		this.update();
	}

	/**
	 * Creates the group containing the collection options of the type generation.
	 * 
	 * @param composite
	 *            The composite containing the group
	 * @param font
	 *            The font used by the parent of the group
	 */
	private void createCollectionGroup(Composite composite, Font font) {
		GridData gd;
		Group collectionsGroup = createGroup(composite, UML2JavaMessages
				.getString("UML2JavaTypeLaunchConfigurationTab.CollectionsGroupName"), 3, 1,
				GridData.FILL_HORIZONTAL);
		Composite comp = new Composite(collectionsGroup, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		comp.setLayout(layout);
		comp.setFont(font);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		comp.setLayoutData(gd);

		// Ordered and unique
		Label orderedAndUniqueLabel = new Label(comp, SWT.NONE);
		orderedAndUniqueLabel.setText(UML2JavaMessages
				.getString("UML2JavaTypeLaunchConfigurationTab.OrderedAndUniqueLabel"));

		this.orderedAndUniqueText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		this.orderedAndUniqueText.setFont(composite.getFont());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		this.orderedAndUniqueText.setLayoutData(gd);
		this.orderedAndUniqueText.setText(IUML2JavaConstants.Default.DEFAULT_ORDERED_UNIQUE);
		this.orderedAndUniqueText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				update();
			}
		});

		createHelpButton(comp, UML2JavaMessages
				.getString("UML2JavaTypeLaunchConfigurationTab.OrderedAndUniqueHelp"));

		// Ordered and not unique
		Label orderedAndNotUniqueLabel = new Label(comp, SWT.NONE);
		orderedAndNotUniqueLabel.setText(UML2JavaMessages
				.getString("UML2JavaTypeLaunchConfigurationTab.OrderedAndNotUniqueLabel"));

		this.orderedAndNotUniqueText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		this.orderedAndNotUniqueText.setFont(composite.getFont());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		this.orderedAndNotUniqueText.setLayoutData(gd);
		this.orderedAndNotUniqueText.setText(IUML2JavaConstants.Default.DEFAULT_ORDERED_NOT_UNIQUE);
		this.orderedAndNotUniqueText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				update();
			}
		});

		createHelpButton(comp, UML2JavaMessages
				.getString("UML2JavaTypeLaunchConfigurationTab.OrderedAndNotUniqueHelp"));

		// Not ordered and unique
		Label notOrderedAndUniqueLabel = new Label(comp, SWT.NONE);
		notOrderedAndUniqueLabel.setText(UML2JavaMessages
				.getString("UML2JavaTypeLaunchConfigurationTab.NotOrderedAndUniqueLabel"));

		this.notOrderedAndUniqueText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		this.notOrderedAndUniqueText.setFont(composite.getFont());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		this.notOrderedAndUniqueText.setLayoutData(gd);
		this.notOrderedAndUniqueText.setText(IUML2JavaConstants.Default.DEFAULT_NOT_ORDERED_UNIQUE);
		this.notOrderedAndUniqueText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				update();
			}
		});

		createHelpButton(comp, UML2JavaMessages
				.getString("UML2JavaTypeLaunchConfigurationTab.NotOrderedAndUniqueHelp"));

		// Not ordered and not unique
		Label notOrderedAndNotUniqueLabel = new Label(comp, SWT.NONE);
		notOrderedAndNotUniqueLabel.setText(UML2JavaMessages
				.getString("UML2JavaTypeLaunchConfigurationTab.NotOrderedAndNotUniqueLabel"));

		this.notOrderedAndNotUniqueText = new Text(comp, SWT.SINGLE | SWT.BORDER);
		this.notOrderedAndNotUniqueText.setFont(composite.getFont());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		this.notOrderedAndNotUniqueText.setLayoutData(gd);
		this.notOrderedAndNotUniqueText.setText(IUML2JavaConstants.Default.DEFAULT_NOT_ORDERED_NOT_UNIQUE);
		this.notOrderedAndNotUniqueText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				update();
			}
		});

		createHelpButton(comp, UML2JavaMessages
				.getString("UML2JavatypeLaunchConfigurationTab.NotOrderedAndNotUniqueHelp"));
	}

	/**
	 * Creates the group containing the conversion options of the type generation.
	 * 
	 * @param composite
	 *            The composite containing the group
	 * @param font
	 *            The font used by the parent of the group
	 */
	private void createConversionGroup(Composite composite, Font font) {

	}

	/**
	 * Update the launch configuration and check potential errors.
	 */
	private void update() {
		// do nothing

		this.getLaunchConfigurationDialog().updateButtons();
		this.getLaunchConfigurationDialog().updateMessage();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		// Ordered unique
		configuration.setAttribute(IUML2JavaConstants.ORDERED_UNIQUE_TYPE,
				IUML2JavaConstants.Default.DEFAULT_ORDERED_UNIQUE);
		if (this.orderedAndUniqueText != null) {
			this.orderedAndUniqueText.setText(IUML2JavaConstants.Default.DEFAULT_ORDERED_UNIQUE);
		}

		// Ordered not unique
		configuration.setAttribute(IUML2JavaConstants.ORDERED_NOT_UNIQUE_TYPE,
				IUML2JavaConstants.Default.DEFAULT_ORDERED_NOT_UNIQUE);
		if (this.orderedAndNotUniqueText != null) {
			this.orderedAndNotUniqueText.setText(IUML2JavaConstants.Default.DEFAULT_ORDERED_NOT_UNIQUE);
		}

		// Not ordered unique
		configuration.setAttribute(IUML2JavaConstants.NOT_ORDERED_UNIQUE_TYPE,
				IUML2JavaConstants.Default.DEFAULT_NOT_ORDERED_UNIQUE);
		if (this.notOrderedAndUniqueText != null) {
			this.notOrderedAndUniqueText.setText(IUML2JavaConstants.Default.DEFAULT_NOT_ORDERED_UNIQUE);
		}

		// NOt ordered not unique
		configuration.setAttribute(IUML2JavaConstants.NOT_ORDERED_NOT_UNIQUE_TYPE,
				IUML2JavaConstants.Default.DEFAULT_NOT_ORDERED_NOT_UNIQUE);
		if (this.notOrderedAndNotUniqueText != null) {
			this.notOrderedAndNotUniqueText
					.setText(IUML2JavaConstants.Default.DEFAULT_NOT_ORDERED_NOT_UNIQUE);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			// Ordered unique
			String attribute = configuration.getAttribute(IUML2JavaConstants.ORDERED_UNIQUE_TYPE, "");
			this.orderedAndUniqueText.setText(attribute);

			// Ordered not unique
			attribute = configuration.getAttribute(IUML2JavaConstants.ORDERED_NOT_UNIQUE_TYPE, "");
			this.orderedAndNotUniqueText.setText(attribute);

			// Not ordered unique
			attribute = configuration.getAttribute(IUML2JavaConstants.NOT_ORDERED_UNIQUE_TYPE, "");
			this.notOrderedAndUniqueText.setText(attribute);

			// Not ordered not unique
			attribute = configuration.getAttribute(IUML2JavaConstants.NOT_ORDERED_NOT_UNIQUE_TYPE, "");
			this.notOrderedAndNotUniqueText.setText(attribute);
		} catch (CoreException e) {
			IStatus status = new Status(IStatus.ERROR, UML2JavaUIActivator.PLUGIN_ID, e.getMessage(), e);
			UML2JavaUIActivator.getDefault().getLog().log(status);
		}
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		// Ordered unique
		String orderedUnique = this.orderedAndUniqueText.getText();
		configuration.setAttribute(IUML2JavaConstants.ORDERED_UNIQUE_TYPE, orderedUnique);

		// Ordered not unique
		String orderedNotUnique = this.orderedAndNotUniqueText.getText();
		configuration.setAttribute(IUML2JavaConstants.ORDERED_NOT_UNIQUE_TYPE, orderedNotUnique);

		// Not ordered unique
		String notOrderedUnique = this.notOrderedAndUniqueText.getText();
		configuration.setAttribute(IUML2JavaConstants.NOT_ORDERED_UNIQUE_TYPE, notOrderedUnique);

		// Not ordered not unique
		String notOrderedNotUnique = this.notOrderedAndNotUniqueText.getText();
		configuration.setAttribute(IUML2JavaConstants.NOT_ORDERED_NOT_UNIQUE_TYPE, notOrderedNotUnique);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#isValid(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		return super.isValid(launchConfig);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return UML2JavaMessages.getString("UML2JavaTypeLaunchConfigurationTab.Name");
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getImage()
	 */
	@Override
	public Image getImage() {
		return UML2JavaUIActivator.getDefault().getImage("icons/datatype_obj.gif");
	}

}
