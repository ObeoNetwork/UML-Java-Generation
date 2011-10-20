package org.obeonetwork.pim.uml2.gen.java.ui.launch;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.eclipse.acceleo.common.internal.utils.workspace.AcceleoWorkspaceUtil;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.obeonetwork.pim.uml2.gen.java.main.Workflow;
import org.obeonetwork.pim.uml2.gen.java.properties.IUML2JavaPropertiesConstants;
import org.obeonetwork.pim.uml2.gen.java.ui.UML2JavaUIActivator;
import org.osgi.framework.Bundle;

public class UML2JavaLaunchConfigurationPropertiesTab extends AbstractLaunchConfigurationTab {
	
	public static final Properties defaultProperties;
	
	private static final String PROPERTIES_FILES_EXTENSION = ".properties"; //$NON-NLS-1$
	
	private static final Bundle bundle;
	
	private Properties userProperties = new Properties();
	
	private TableViewer propertiesTableViewer;
	
	static {
		bundle = AcceleoWorkspaceUtil.getBundle(Workflow.class);
		defaultProperties = new Properties();
		defaultProperties.putAll(loadPropertiesFromBundle(IUML2JavaPropertiesConstants.DEFAULT_PROPERTIES));
		defaultProperties.putAll(loadPropertiesFromBundle(IUML2JavaPropertiesConstants.IMPORTS_PROPERTIES));
		defaultProperties.putAll(loadPropertiesFromBundle(IUML2JavaPropertiesConstants.TYPES_PROPERTIES));
	}
	
	protected static Properties loadPropertiesFromBundle(String pathInBundle) {
		Properties properties = new Properties();
		try {
			if (bundle != null) {
				URL resource = bundle.getResource(pathInBundle);
				if (resource != null) {
					properties.load(resource.openStream());
				} else if (pathInBundle != null) {
					String filename = pathInBundle;
					
					final String dot = "."; //$NON-NLS-1$
					if (!filename.endsWith(PROPERTIES_FILES_EXTENSION) && filename.contains(dot)) {
						filename = filename.substring(filename.lastIndexOf(dot) + 1);
						filename = filename + PROPERTIES_FILES_EXTENSION;
					}
					
					Enumeration<?> entries = bundle.findEntries("/", filename, true); //$NON-NLS-1$
					Object firstEntry = null;
					if (entries != null && entries.hasMoreElements()) {
						firstEntry = entries.nextElement();
					}
					if (firstEntry instanceof URL) {
						properties.load(((URL)firstEntry).openStream());
					}
				}
			}
		} catch (IOException e) {
			return null;
		}
		return properties;
	}
	
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalAlignment = SWT.TOP;
		gridData = new GridData(GridData.FILL_BOTH);
		composite.setLayoutData(gridData);
		
		Group propertiesGroup = new Group(composite, SWT.NONE);
		propertiesGroup.setLayout(new GridLayout(3, false));
		propertiesGroup.setText("Properties");
		propertiesGroup.setFont(composite.getFont());
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		propertiesGroup.setLayoutData(gd);
				
		propertiesTableViewer = new TableViewer(propertiesGroup, SWT.BORDER | SWT.FULL_SELECTION);
		propertiesTableViewer.getTable().setHeaderVisible(true);
		propertiesTableViewer.getTable().setLinesVisible(true);
		
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		gridData.heightHint = propertiesTableViewer.getTable().getItemHeight() * 10;
		propertiesTableViewer.getTable().setLayoutData(gridData);
		
		TableViewerColumn keyColumn = new TableViewerColumn(propertiesTableViewer, SWT.LEFT);
		keyColumn.getColumn().setText("Key");
		TableViewerColumn valueColumn = new TableViewerColumn(propertiesTableViewer, SWT.LEFT);
		valueColumn.getColumn().setText("Value");
		
		keyColumn.getColumn().setWidth(300);
		valueColumn.getColumn().setWidth(300);
		
//		TableColumnLayout layout = new TableColumnLayout();
//		propertiesGroup.setLayout(layout);
//		layout.setColumnData( keyColumn.getColumn(), new ColumnWeightData(50));
//		layout.setColumnData( valueColumn.getColumn(), new ColumnWeightData(50));
		
		valueColumn.setEditingSupport(new EditingSupport(propertiesTableViewer) {
			
			@Override
			protected void setValue(Object element, Object value) {
				if (element instanceof Entry) {
					Entry<?, ?> entry = (Entry<?, ?>) element;
					Object key = entry.getKey();
					if (key instanceof String && value instanceof String) {
						userProperties.put(key, value);
					}
				}
				propertiesTableViewer.refresh();
				updateLaunchConfigurationDialog();
			}
			
			@Override
			protected Object getValue(Object element) {
				if (element instanceof Entry) {
					Entry<?, ?> entry = (Entry<?, ?>) element;
					return entry.getValue();
				}
				return element;
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				return new TextCellEditor(propertiesTableViewer.getTable());
			}
			
			@Override
			protected boolean canEdit(Object element) {
				return true;
			}
		});
		
		userProperties.putAll(defaultProperties);
		
		propertiesTableViewer.setContentProvider(new PropertiesTableContentProvider());
		propertiesTableViewer.setLabelProvider(new PropertiesTableLabelProvider());
		propertiesTableViewer.setInput(userProperties);
		propertiesTableViewer.getTable().pack();
		
		Composite tableButtonComposite = new Composite(propertiesGroup, SWT.NONE);
		layout = new GridLayout();
		layout.numColumns = 1;
		tableButtonComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		tableButtonComposite.setLayout(layout);
		
		String helpMessage = "You can edit the properties used during the generation.";
		Image image = PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_LCL_LINKTO_HELP);
		ToolBar result = new ToolBar(tableButtonComposite, SWT.FLAT | SWT.NO_FOCUS);
		result.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		ToolItem item = new ToolItem(result, SWT.NONE);
		item.setImage(image);
		if (helpMessage != null && !"".equals(helpMessage)) { //$NON-NLS-1$
			item.setToolTipText(helpMessage);
		}
		
		this.setControl(composite);
		update();
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		userProperties.clear();
		try {
			Map<?, ?> map = configuration.getAttribute(IUML2JavaContants.ATTR_PROPERTIES, new HashMap<Object, Object>());
			if (!map.isEmpty()) {
				userProperties.putAll(map);
			} else {
				userProperties.putAll(defaultProperties);
			}
		} catch (CoreException e) {
			
		}
		update();
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		this.saveData(configuration);
		update();
	}
	
	public void saveData(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(IUML2JavaContants.ATTR_PROPERTIES, new HashMap<Object, Object>(userProperties));
	}
	
	private void update() {
		propertiesTableViewer.refresh();
		propertiesTableViewer.setInput(userProperties);
	}

	public String getName() {
		return "Properties";
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#getImage()
	 */
	@Override
	public Image getImage() {
		return UML2JavaUIActivator.getDefault().getImage("icons/properties.gif");
	}

}
