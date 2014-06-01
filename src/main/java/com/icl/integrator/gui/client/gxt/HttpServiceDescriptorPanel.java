package com.icl.integrator.gui.client.gxt;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.icl.integrator.dto.source.EndpointDescriptor;
import com.icl.integrator.dto.source.HttpEndpointDescriptorDTO;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * Created by BigBlackBug on 01.06.2014.
 */
public class HttpServiceDescriptorPanel implements Refreshable<EndpointDescriptor> {

	private final Label hostLabel;

	private final Label portLabel;

	private VerticalLayoutContainer vlc;

	public HttpServiceDescriptorPanel() {
		vlc = new VerticalLayoutContainer();
		hostLabel = new Label();
		portLabel = new Label();
		vlc.add(new Label("host"));
		vlc.add(hostLabel);
		vlc.add(new Label("port"));
		vlc.add(portLabel);
	}

	@Override
	public void refresh(EndpointDescriptor item) {
		HttpEndpointDescriptorDTO descriptor = (HttpEndpointDescriptorDTO) item;
		hostLabel.setText(descriptor.getHost());
		portLabel.setText(String.valueOf(descriptor.getPort()));
	}

	@Override
	public Widget asWidget() {
		return vlc;
	}
}
