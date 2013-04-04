/*******************************************************************************
 * All rights reserved.
 *******************************************************************************/
package org.obeonetwork.pim.uml2.gen.java.tests.interfaces.methods.parameters;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
// Start of user code (user defined imports)

// End of user code

/**
 * Description of CardinalityParameterMethodInterface.
 * 
 * @author sbegaudeau
 */
public interface CardinalityParameterMethodInterface {
	// Start of user code (user defined attributes for CardinalityParameterMethodInterface)

	// End of user code

	/**
	 * Description of the method cardinalityParameterMethod.
	 * @param orderedUniqueParameter 
	 * @param notOrderedUniqueParameter 
	 * @param notOrderedNotUniqueParameter 
	 * @param orderedNotUniqueParameter 
	 * @return 
	 */
	public Date cardinalityParameterMethod(
			LinkedHashSet<Boolean> orderedUniqueParameter,
			HashSet<Integer> notOrderedUniqueParameter,
			ArrayList<String> notOrderedNotUniqueParameter,
			ArrayList<Date> orderedNotUniqueParameter);

	// Start of user code (user defined methods for CardinalityParameterMethodInterface)

	// End of user code

}
