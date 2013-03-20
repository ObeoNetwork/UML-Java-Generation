/*******************************************************************************
 * Copyright (c) 2011 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stephane Begaudeau (Obeo) - initial API and implementation
 *******************************************************************************/
package org.obeonetwork.pim.uml2.gen.java.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

/**
 * Service class to compute the imports of a given classifier.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 2.0
 */
public class ImportServices {
	/**
	 * The import keyword.
	 */
	private static final String IMPORT = "import ";

	/**
	 * The end of the line for the imports.
	 */
	private static final String END_IMPORT = ";" + System.getProperty("line.separator");

	/**
	 * All the basic Java types.
	 */
	private static final List<String> JAVA_LANG_TYPES = Arrays.asList(new String[] {"Boolean", "boolean",
			"Integer", "int", "String", "char", "Char", "long", "Long", "double", "Double", "Float", "float",
			"Byte", "byte", });

	/**
	 * Java util types.
	 */
	private static final List<String> JAVA_UTIL_TYPES = Arrays.asList(new String[] {"Date" });

	/**
	 * Returns the string representing the import block for a given classifier.
	 * 
	 * @param aClassifier
	 *            The classifier
	 * @return The string representing the import block for a given classifier.
	 */
	public String reqImport(Classifier aClassifier) {
		Set<String> importedTypes = new LinkedHashSet<String>();

		// Import from attributes
		List<Property> attributes = aClassifier.getAttributes();
		for (Property property : attributes) {
			String qualifiedName = this.qualifiedName(property.getType());
			if (qualifiedName != null) {
				importedTypes.add(qualifiedName);
			}
			if (property.getUpper() != 1) {
				String collectionQualifiedName = this.collectionQualifiedName(aClassifier, property
						.isOrdered(), property.isUnique());
				if (collectionQualifiedName != null) {
					importedTypes.add(collectionQualifiedName);
				}
			}
		}

		// Import from operations
		List<Operation> operations = aClassifier.getOperations();
		for (Operation operation : operations) {
			String qualifiedName = this.qualifiedName(operation.getType());
			if (qualifiedName != null) {
				importedTypes.add(qualifiedName);
			}
			if (operation.getUpper() != 1) {
				String collectionQualifiedName = this.collectionQualifiedName(aClassifier, operation
						.isOrdered(), operation.isUnique());
				if (collectionQualifiedName != null) {
					importedTypes.add(collectionQualifiedName);
				}
			}

			List<Parameter> ownedParameters = operation.getOwnedParameters();
			for (Parameter parameter : ownedParameters) {
				qualifiedName = this.qualifiedName(parameter.getType());
				if (qualifiedName != null) {
					importedTypes.add(qualifiedName);
				}
				if (parameter.getUpper() != 1) {
					String collectionQualifiedName = this.collectionQualifiedName(aClassifier, parameter
							.isOrdered(), parameter.isUnique());
					if (collectionQualifiedName != null) {
						importedTypes.add(collectionQualifiedName);
					}
				}
			}

			List<Type> raisedExceptions = operation.getRaisedExceptions();
			for (Type type : raisedExceptions) {
				String exceptionQualifiedName = this.qualifiedName(type);
				if (exceptionQualifiedName != null) {
					importedTypes.add(exceptionQualifiedName);
				}
			}
		}

		// Class or interfaces inheritance
		List<Generalization> generalizations = aClassifier.getGeneralizations();
		for (Generalization generalization : generalizations) {
			importedTypes.add(this.qualifiedName(generalization.getGeneral()));
		}

		// Classes' interface realizations
		if (aClassifier instanceof org.eclipse.uml2.uml.Class) {
			org.eclipse.uml2.uml.Class aClass = (org.eclipse.uml2.uml.Class)aClassifier;
			List<InterfaceRealization> interfaceRealizations = aClass.getInterfaceRealizations();
			for (InterfaceRealization interfaceRealization : interfaceRealizations) {
				importedTypes.add(this.qualifiedName(interfaceRealization.getContract()));
			}
		}

		List<String> sortedImportedTypes = new ArrayList<String>(importedTypes);
		Collections.sort(sortedImportedTypes);

		StringBuilder stringBuilder = new StringBuilder();
		for (String importedType : sortedImportedTypes) {
			stringBuilder.append(IMPORT + importedType + END_IMPORT);
		}

		return stringBuilder.toString();
	}

	/**
	 * Returns the type of the type of collection looked for.
	 * 
	 * @param aClassifier
	 *            The classifier used to retrieve the configuration holder
	 * @param ordered
	 *            Indicates if the collection should be ordered
	 * @param unique
	 *            Indicates if the collection should be unique
	 * @return The type of the type of collection looked for.
	 */
	private String collectionQualifiedName(Classifier aClassifier, boolean ordered, boolean unique) {
		String result = "";
		if (ordered && unique) {
			result = UML2JavaConfigurationHolder.getOrderedUniqueCollectionsType(aClassifier);
		} else if (ordered && !unique) {
			result = UML2JavaConfigurationHolder.getOrderedNotUniqueCollectionsType(aClassifier);
		} else if (!ordered && unique) {
			result = UML2JavaConfigurationHolder.getNotOrderedUniqueCollectionsType(aClassifier);
		} else if (!ordered && !unique) {
			result = UML2JavaConfigurationHolder.getNotOrderedNotUniqueCollectionsType(aClassifier);
		}
		return result;
	}

	/**
	 * Returns the qualified name of the given type.
	 * 
	 * @param type
	 *            The type
	 * @return The qualified name of the given type.
	 */
	private String qualifiedName(Type type) {
		String result = null;
		if (type != null && !(type instanceof PrimitiveType)) {
			List<String> packagesName = new ArrayList<String>();

			EObject eContainer = type.eContainer();
			while (eContainer != null && eContainer instanceof Package && !(eContainer instanceof Model)) {
				Package umlPackage = (Package)eContainer;
				packagesName.add(umlPackage.getName());

				eContainer = umlPackage.eContainer();
			}

			Collections.reverse(packagesName);

			StringBuilder stringBuilder = new StringBuilder();
			for (String packageName : packagesName) {
				stringBuilder.append(packageName);
				stringBuilder.append('.');
			}

			stringBuilder.append(type.getName());

			result = stringBuilder.toString();
			if (JAVA_LANG_TYPES.contains(type.getName())) {
				result = null;
			} else if (JAVA_UTIL_TYPES.contains(type.getName())) {
				result = "java.util." + type.getName();
			}
		}
		return result;
	}
}
