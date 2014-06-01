package com.icl.integrator.gui.client.gxt;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.icl.integrator.dto.source.EndpointDescriptor;
import com.icl.integrator.dto.source.JMSEndpointDescriptorDTO;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * Created by BigBlackBug on 01.06.2014.
 */
public class JmsServiceDescriptionPanel implements Refreshable<EndpointDescriptor> {

	private final Label connectionFactoryLabel;

	private final Label jndiPropsLabel;

	private VerticalLayoutContainer vlc;

	public JmsServiceDescriptionPanel() {
		vlc = new VerticalLayoutContainer();
		connectionFactoryLabel = new Label();
		jndiPropsLabel = new Label();
		vlc.add(new Label("CF"));
		vlc.add(connectionFactoryLabel);
		vlc.add(new Label("JNDI"));
		vlc.add(jndiPropsLabel);
	}

	@Override
	public void refresh(EndpointDescriptor item) {
		JMSEndpointDescriptorDTO descriptor = (JMSEndpointDescriptorDTO) item;
		connectionFactoryLabel.setText(descriptor.getConnectionFactory());
		jndiPropsLabel.setText(String.valueOf(descriptor.getJndiProperties()));
	}

	@Override
	public Widget asWidget() {
		return vlc;
	}
}
