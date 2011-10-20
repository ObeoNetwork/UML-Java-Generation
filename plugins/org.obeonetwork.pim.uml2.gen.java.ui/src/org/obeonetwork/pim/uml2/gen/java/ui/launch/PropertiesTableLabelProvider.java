package org.obeonetwork.pim.uml2.gen.java.ui.launch;

import java.util.Map.Entry;

import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

public class PropertiesTableLabelProvider extends LabelProvider implements ITableLabelProvider, ITableColorProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		String text = null;
		
		if (element instanceof Entry) {
			Entry<?, ?> entry = (Entry<?, ?>) element;
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (key instanceof String && value instanceof String) {
				String keyStr = (String) key;
				String valueStr = (String) value;
				
				if (columnIndex == 0) {
					text = keyStr;
				} else if (columnIndex == 1) {
					text = valueStr;
				}
			}
		}
		return text;
	}

	public Color getForeground(Object element, int columnIndex) {
		return null;
	}

	public Color getBackground(Object element, int columnIndex) {
		if (element instanceof Entry) {
			Entry<?, ?> entry = (Entry<?, ?>) element;
			Object key = entry.getKey();
			Object value = entry.getValue();
			if (key instanceof String && value instanceof String) {
				String keyStr = (String) key;
				String valueStr = (String) value;
				Object val = UML2JavaLaunchConfigurationPropertiesTab.defaultProperties.get(keyStr);
				if (val instanceof String && !valueStr.equals(val)) {
					return Display.getCurrent().getSystemColor(SWT.COLOR_GREEN);
				}
			}
		}
		return null;
	}
}
