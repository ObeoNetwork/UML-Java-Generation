/*******************************************************************************
 * All rights reserved.
 *******************************************************************************/
package org.obeonetwork.pim.uml2.gen.java.tests.interfaces.properties;

import java.util.Date;
// Start of user code (user defined imports)

// End of user code

/**
 * Description of VisibilityPropertiesInterface.
 * 
 * @author sbegaudeau
 */
public interface VisibilityPropertiesInterface {
	/**
	 * Description of the property publicProperty.
	 */
	public Boolean publicProperty = Boolean.FALSE;

	/**
	 * Description of the property privateProperty.
	 */
	private Byte privateProperty = Byte.valueOf("+0");

	/**
	 * Description of the property protectedProperty.
	 */
	protected Date protectedProperty = new Date();

	/**
	 * Description of the property packageProperty.
	 */
	/*package*/Float packageProperty = Float.valueOf(0F);

	// Start of user code (user defined attributes for VisibilityPropertiesInterface)

	// End of user code

	// Start of user code (user defined methods for VisibilityPropertiesInterface)

	// End of user code

}
