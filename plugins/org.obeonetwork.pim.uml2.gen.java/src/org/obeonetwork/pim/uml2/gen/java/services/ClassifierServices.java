/*******************************************************************************
 * Copyright (c) 2008, 2012 Obeo.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Obeo - initial API and implementation
 *******************************************************************************/
package org.obeonetwork.pim.uml2.gen.java.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.acceleo.engine.AcceleoEnginePlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Type;

/**
 * Various services for the classifiers.
 * 
 * @author <a href="mailto:stephane.begaudeau@obeo.fr">Stephane Begaudeau</a>
 * @since 2.0
 */
public class ClassifierServices {
	public List<Operation> getAllInheritedOperations(org.eclipse.uml2.uml.Class aClass) {
		LinkedHashSet<Operation> inheritedOperations = new LinkedHashSet<Operation>();

		// Everything inherited from the classes
		List<Class> allGeneralizedClasses = this.getAllGeneralizedClasses(aClass);
		for (Class aGeneralizedClass : allGeneralizedClasses) {
			List<Operation> operations = aGeneralizedClass.getOperations();
			for (Operation operation : operations) {
				if (operation.isAbstract()) {
					inheritedOperations.add(operation);
				}
			}
		}

		List<Interface> implementedInterfaces = aClass.getImplementedInterfaces();
		for (Interface anInterface : implementedInterfaces) {
			inheritedOperations.addAll(anInterface.getAllOperations());
		}

		List<Operation> operationsToRemove = new ArrayList<Operation>();
		for (Operation inheritedOperation : inheritedOperations) {
			boolean shouldRemoveOperation = false;

			// See if it's not implemented
			List<Operation> ownedOperations = aClass.getOwnedOperations();
			for (Operation ownedOperation : ownedOperations) {
				if (this.areEqual(ownedOperation, inheritedOperation)) {
					shouldRemoveOperation = true;
					break;
				}
			}

			// See if a parent is not implementing it
			if (!shouldRemoveOperation) {
				for (Class aGeneralizedClass : allGeneralizedClasses) {
					List<Operation> generalizedClassOperations = aGeneralizedClass.getOwnedOperations();
					for (Operation generalizedClassOperation : generalizedClassOperations) {
						if (generalizedClassOperation != inheritedOperation && !generalizedClassOperation.isAbstract() && this.areEqual(generalizedClassOperation, inheritedOperation)) {
							shouldRemoveOperation = true;
							break;
						}
					}
				}
			}

			if (shouldRemoveOperation) {
				operationsToRemove.add(inheritedOperation);
			}
		}
		inheritedOperations.removeAll(operationsToRemove);

		List<Operation> operations = new ArrayList<Operation>();
		operations.addAll(inheritedOperations);
		return operations;
	}

	private boolean areEqual(Operation generalizedClassOperation, Operation inheritedOperation) {
		if (generalizedClassOperation.getName() != null
				&& generalizedClassOperation.getName().equals(inheritedOperation.getName())) {
			EList<Parameter> generalizedClassOperationParameters = generalizedClassOperation
					.getOwnedParameters();
			int generalizedClassOperationParametersSize = generalizedClassOperationParameters.size();
			EList<Parameter> inheritedOperationParameters = inheritedOperation.getOwnedParameters();

			EList<Parameter> returnResult = generalizedClassOperation.returnResult();
			int returnResultSize = returnResult.size();
			EList<Parameter> inheritedOperationReturnResult = inheritedOperation.returnResult();

			if (generalizedClassOperationParametersSize == inheritedOperationParameters.size()
					&& returnResultSize == inheritedOperationReturnResult.size()) {

				for (int i = 0; i < generalizedClassOperationParametersSize; i++) {
					Type inheritedOperationParameterType = inheritedOperationParameters.get(i).getType();
					Type generalizedClassOperationParameterType = generalizedClassOperationParameters.get(i)
							.getType();

					if (inheritedOperationParameterType == null) {
						if (generalizedClassOperationParameterType != null) {
							return false;
						}
					} else {
						if (!inheritedOperationParameterType
								.conformsTo(generalizedClassOperationParameterType)) {
							return false;
						}
					}
				}

				for (int i = 0; i < returnResultSize; i++) {
					Type inheritedOperationReturnResultType = inheritedOperationReturnResult.get(i).getType();
					Type returnResultType = returnResult.get(i).getType();

					if (inheritedOperationReturnResultType == null) {
						if (returnResultType != null) {
							return false;
						}
					} else {
						if (!inheritedOperationReturnResultType.conformsTo(returnResultType)) {
							return false;
						}
					}
				}

				return true;
			}
		}

		return false;
	}

	private List<org.eclipse.uml2.uml.Class> getAllGeneralizedClasses(org.eclipse.uml2.uml.Class aClass) {
		List<org.eclipse.uml2.uml.Class> generalizedClass = new ArrayList<org.eclipse.uml2.uml.Class>();

		List<Generalization> generalizations = aClass.getGeneralizations();
		for (Generalization generalization : generalizations) {
			Classifier aClassifier = generalization.getGeneral();
			if (aClassifier instanceof org.eclipse.uml2.uml.Class) {
				org.eclipse.uml2.uml.Class anInheritedClass = (org.eclipse.uml2.uml.Class)aClassifier;
				generalizedClass.add(anInheritedClass);
				generalizedClass.addAll(this.getAllGeneralizedClasses(anInheritedClass));
			}
		}

		return generalizedClass;
	}
}
