/*******************************************************************************
 * All rights reserved.
 *******************************************************************************/
package org.obeonetwork.pim.uml2.gen.java.tests.interfaces.methods;

import java.util.Date;
// Start of user code (user defined imports)

// End of user code

/**
 * Description of VisibilityMethodsInterface.
 * 
 * @author sbegaudeau
 */
public interface VisibilityMethodsInterface {
	// Start of user code (user defined attributes for VisibilityMethodsInterface)

	// End of user code

	/**
	 * Description of the method publicMethod.
	 * @return 
	 */
	public Boolean publicMethod();

	/**
	 * Description of the method privateMethod.
	 * @return 
	 */
	private Byte privateMethod();

	/**
	 * Description of the method protectedMethod.
	 * @return 
	 */
	protected Date protectedMethod();

	/**
	 * Description of the method packageMethod.
	 * @return 
	 */
	/*package*/String packageMethod();

	// Start of user code (user defined methods for VisibilityMethodsInterface)

	// End of user code

}
