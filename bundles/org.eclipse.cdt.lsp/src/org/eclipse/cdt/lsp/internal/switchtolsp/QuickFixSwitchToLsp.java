/*******************************************************************************
 * Copyright (c) 2024 Kichwa Coders Canada Inc. and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.cdt.lsp.internal.switchtolsp;

import org.eclipse.cdt.codan.ui.ICodanMarkerResolution;
import org.eclipse.cdt.lsp.switchtolsp.SwitchToLspWizard;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

public class QuickFixSwitchToLsp implements ICodanMarkerResolution {

	@Override
	public boolean isApplicable(IMarker marker) {
		return true;
	}

	@Override
	public String getLabel() {
		return "Switch to using C/C++ Editor (LSP)";
	}

	@Override
	public void run(IMarker marker) {
		IProject project = null;
		IResource resource = marker.getResource();
		if (resource != null) {
			project = resource.getProject();
		}

		new SwitchToLspWizard().doSwitch(project);

		// todo close and reopen editors
	}
}
