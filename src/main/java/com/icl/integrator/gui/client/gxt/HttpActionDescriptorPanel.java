package com.icl.integrator.gui.client.gxt;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.registration.HttpActionDTO;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * Created by BigBlackBug on 01.06.2014.
 */
public class HttpActionDescriptorPanel implements Refreshable<ActionEndpointDTO<ActionDescriptor>> {

	private final Label pathLabel;

	private VerticalLayoutContainer vlc;

	public HttpActionDescriptorPanel() {
		vlc = new VerticalLayoutContainer();
		pathLabel = new Label();
		vlc.add(new Label("path"));
		vlc.add(pathLabel);
	}

	@Override
	public void refresh(ActionEndpointDTO<ActionDescriptor> item) {
		HttpActionDTO descriptor = (HttpActionDTO) item.getActionDescriptor();
		pathLabel.setText(descriptor.getPath());
	}

	@Override
	public Widget asWidget() {
		return vlc;
	}
}
