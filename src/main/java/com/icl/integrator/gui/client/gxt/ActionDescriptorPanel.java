package com.icl.integrator.gui.client.gxt;

import com.google.gwt.user.client.ui.Widget;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.util.EndpointType;
import com.sencha.gxt.widget.core.client.ContentPanel;

/**
 * Created by BigBlackBug on 01.06.2014.
 */
public class ActionDescriptorPanel implements Refreshable<ActionEndpointDTO<ActionDescriptor>> {

	private ContentPanel cp;

	private Refreshable<ActionEndpointDTO<ActionDescriptor>> descriptorWidget;

	public ActionDescriptorPanel() {
		cp = new ContentPanel();
		cp.setHeaderVisible(false);
		descriptorWidget = new HttpActionDescriptorPanel();
		cp.add(descriptorWidget);
	}

	@Override
	public void refresh(ActionEndpointDTO<ActionDescriptor> descriptor) {
		ActionDescriptor actionDescriptor = descriptor.getActionDescriptor();
		if (actionDescriptor.getEndpointType() == EndpointType.HTTP) {
			descriptorWidget = new HttpActionDescriptorPanel();
		} else {
			descriptorWidget = new JmsActionDescriptionPanel();
		}
		descriptorWidget.refresh(descriptor);
		cp.clear();
		cp.add(descriptorWidget);
		cp.forceLayout();
	}

	@Override
	public Widget asWidget() {
		return cp;
	}
}
