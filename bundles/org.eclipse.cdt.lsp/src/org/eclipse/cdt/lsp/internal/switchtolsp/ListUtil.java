/*******************************************************************************
 * Copyright (c) 2000, 2024 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.cdt.lsp.internal.switchtolsp;

import java.util.Comparator;

import org.eclipse.jface.viewers.IBasicPropertyConstants;
import org.eclipse.jface.viewers.ViewerComparator;

public class ListUtil {

	private static final Comparator<String> stringComparator = String::compareToIgnoreCase;

	static class NameComparator extends ViewerComparator {
		public NameComparator() {
			// when comparing names, always use the comparator above to do a String comparison
			super(stringComparator);
		}

		public boolean isSorterProperty(Object element, Object propertyId) {
			return propertyId.equals(IBasicPropertyConstants.P_TEXT);
		}
	}

	public static final ViewerComparator NAME_COMPARATOR = new NameComparator();
}
