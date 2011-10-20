package org.obeonetwork.pim.uml2.gen.java.ui.launch;

import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jface.viewers.ArrayContentProvider;

public class PropertiesTableContentProvider extends ArrayContentProvider {
	
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof Properties) {
			Properties properties = (Properties) inputElement;
			Set<Entry<Object,Object>> entrySet = properties.entrySet();
			return entrySet.toArray(new Entry[entrySet.size()]);
		}
		return super.getElements(inputElement);
	}
}
