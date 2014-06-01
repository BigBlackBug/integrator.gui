package com.icl.integrator.gui.client.gxt;

import com.google.gwt.user.client.ui.Widget;
import com.icl.integrator.dto.source.EndpointDescriptor;
import com.icl.integrator.dto.util.EndpointType;
import com.sencha.gxt.widget.core.client.ContentPanel;

/**
 * Created by BigBlackBug on 01.06.2014.
 */
public class EndpointDescriptorPanel implements Refreshable<EndpointDescriptor> {

	private ContentPanel cp;

	private Refreshable<EndpointDescriptor> descriptorWidget;

	public EndpointDescriptorPanel() {
		cp = new ContentPanel();
		cp.setHeaderVisible(false);
		descriptorWidget = new HttpServiceDescriptorPanel();
		cp.add(descriptorWidget);
	}

	@Override
	public void refresh(EndpointDescriptor descriptor) {
		if (descriptor.getEndpointType() == EndpointType.HTTP) {
			descriptorWidget = new HttpServiceDescriptorPanel();
		} else {
			descriptorWidget = new JmsServiceDescriptionPanel();
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
