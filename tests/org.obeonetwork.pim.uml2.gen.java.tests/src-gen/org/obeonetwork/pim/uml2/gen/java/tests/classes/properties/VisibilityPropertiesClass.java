/**
 * File generated from the model::org::obeonetwork::pim::uml2::gen::java::tests::classes::properties::VisibilityPropertiesClass uml Class
 * Generated by the Acceleo 3 <i>UML to Java</i> generator.
 */
package org.obeonetwork.pim.uml2.gen.java.tests.classes.properties;

import java.util.Date;
// Start of user code imports	

// End of user code

/**
 * Description of VisibilityPropertiesClass.
 */
public class VisibilityPropertiesClass {
    /**
     * Description of the property publicProperty.
     */
    public Boolean publicProperty = true;
    
    /**
     * Description of the property privateProperty.
     */
    private Date privateProperty = new Date();
    
    /**
     * Description of the property protectedProperty.
     */
    protected Integer protectedProperty = 0;
    
    /**
     * Description of the property packageProperty.
     */
    /*package*/ Integer packageProperty = 0;
    
    // Start of user code (user defined attributes)
    
    // End of user code
    
    /**
     * The constructor.
     */
    public VisibilityPropertiesClass() {
    	// Start of user code constructor
    	super();
    	// End of user code
    }
    
    // Start of user code (user defined methods)
    
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
     * @param publicProperty
     */
    public void setPublicProperty(Boolean publicProperty) {
    	this.publicProperty = publicProperty;
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
     * @param privateProperty
     */
    public void setPrivateProperty(Date privateProperty) {
    	this.privateProperty = privateProperty;
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
     * @param protectedProperty
     */
    public void setProtectedProperty(Integer protectedProperty) {
    	this.protectedProperty = protectedProperty;
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
     * @param packageProperty
     */
    public void setPackageProperty(Integer packageProperty) {
    	this.packageProperty = packageProperty;
    }
    
    
}