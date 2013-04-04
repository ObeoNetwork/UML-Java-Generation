/*******************************************************************************
 * All rights reserved.
 *******************************************************************************/
package org.obeonetwork.pim.uml2.gen.java.tests.interfaces.methods;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
// Start of user code (user defined imports)

// End of user code

/**
 * Description of CardinalityMethodInterface.
 * 
 * @author sbegaudeau
 */
public interface CardinalityMethodInterface {
	// Start of user code (user defined attributes for CardinalityMethodInterface)

	// End of user code

	/**
	 * Description of the method orderedUniqueMethod.
	 * @return 
	 */
	public LinkedHashSet<Integer> orderedUniqueMethod();

	/**
	 * Description of the method notOrderedUniqueMethod.
	 * @return 
	 */
	public HashSet<Date> notOrderedUniqueMethod();

	/**
	 * Description of the method notOrderedNotUniqueMethod.
	 * @return 
	 */
	public ArrayList<Boolean> notOrderedNotUniqueMethod();

	/**
	 * Description of the method OrderedNotUniqueMethod.
	 * @return 
	 */
	public ArrayList<String> OrderedNotUniqueMethod();

	// Start of user code (user defined methods for CardinalityMethodInterface)

	// End of user code

}
