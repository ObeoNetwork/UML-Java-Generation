/*******************************************************************************
 * All rights reserved.
 *******************************************************************************/
package org.obeonetwork.pim.uml2.gen.java.tests.classes.properties;

import java.util.Date;
// Start of user code (user defined imports)

// End of user code

/**
 * Description of VisibilityPropertiesClass.
 * 
 * @author sbegaudeau
 */
public class VisibilityPropertiesClass {
	/**
	 * Description of the property publicProperty.
	 */
	public Boolean publicProperty = Boolean.FALSE;

	/**
	 * Description of the property privateProperty.
	 */
	private Date privateProperty = new Date();

	/**
	 * Description of the property protectedProperty.
	 */
	protected Integer protectedProperty = Integer.valueOf(0);

	/**
	 * Description of the property packageProperty.
	 */
	/*package*/Integer packageProperty = Integer.valueOf(0);

	// Start of user code (user defined attributes for VisibilityPropertiesClass)

	// End of user code

	/**
	 * The constructor.
	 */
	public VisibilityPropertiesClass() {
		// Start of user code constructor for VisibilityPropertiesClass)
		super();
		// End of user code
	}

	// Start of user code (user defined methods for VisibilityPropertiesClass)

	// End of user code
	/**
	 * Returns publicProperty.
	 * @return publicProperty 
	 */
	public Boolean getPublicProperty() {
		return this.publicProperty;
	}

	/**
	 * Sets a value to attribute publicProperty. 
	 * @param newPublicProperty 
	 */
	public void setPublicProperty(Boolean newPublicProperty) {
		this.publicProperty = newPublicProperty;
	}

	/**
	 * Returns privateProperty.
	 * @return privateProperty 
	 */
	public Date getPrivateProperty() {
		return this.privateProperty;
	}

	/**
	 * Sets a value to attribute privateProperty. 
	 * @param newPrivateProperty 
	 */
	public void setPrivateProperty(Date newPrivateProperty) {
		this.privateProperty = newPrivateProperty;
	}

	/**
	 * Returns protectedProperty.
	 * @return protectedProperty 
	 */
	public Integer getProtectedProperty() {
		return this.protectedProperty;
	}

	/**
	 * Sets a value to attribute protectedProperty. 
	 * @param newProtectedProperty 
	 */
	public void setProtectedProperty(Integer newProtectedProperty) {
		this.protectedProperty = newProtectedProperty;
	}

	/**
	 * Returns packageProperty.
	 * @return packageProperty 
	 */
	public Integer getPackageProperty() {
		return this.packageProperty;
	}

	/**
	 * Sets a value to attribute packageProperty. 
	 * @param newPackageProperty 
	 */
	public void setPackageProperty(Integer newPackageProperty) {
		this.packageProperty = newPackageProperty;
	}

}
