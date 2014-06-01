package com.icl.integrator.gui.client.gxt;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.registration.QueueDTO;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

/**
 * Created by BigBlackBug on 01.06.2014.
 */
public class JmsActionDescriptionPanel implements Refreshable<ActionEndpointDTO<ActionDescriptor>> {

	private final Label queueNameLabel;

	private final Label usernameLabel;

	private final Label passwordLabel;

	private VerticalLayoutContainer vlc;

	public JmsActionDescriptionPanel() {
		vlc = new VerticalLayoutContainer();
		queueNameLabel = new Label();
		usernameLabel = new Label();
		passwordLabel = new Label();
		vlc.add(new Label("queuename"));
		vlc.add(queueNameLabel);
		vlc.add(new Label("username"));
		vlc.add(usernameLabel);
		vlc.add(new Label("password"));
		vlc.add(passwordLabel);
	}

	@Override
	public void refresh(ActionEndpointDTO<ActionDescriptor> item) {
		QueueDTO descriptor = (QueueDTO) item.getActionDescriptor();
		queueNameLabel.setText(descriptor.getQueueName());
		usernameLabel.setText(descriptor.getUsername());
		passwordLabel.setText(descriptor.getPassword());
	}

	@Override
	public Widget asWidget() {
		return vlc;
	}
}
