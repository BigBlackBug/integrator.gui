package com.icl.integrator.gui.client.gxt;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;

/**
 * Created by BigBlackBug on 29.05.2014.
 */
public class ActionInfoPanel implements Refreshable<ActionEndpointDTO<ActionDescriptor>>{

	@Override
	public void refresh(ActionEndpointDTO<ActionDescriptor> item) {

	}

	@Override
	public Widget asWidget() {
		return new Label("2234");
	}
}
