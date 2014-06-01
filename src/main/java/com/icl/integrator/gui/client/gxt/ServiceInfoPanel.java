package com.icl.integrator.gui.client.gxt;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.icl.integrator.dto.FullServiceDTO;
import com.icl.integrator.dto.registration.DeliverySettingsDTO;
import com.icl.integrator.dto.source.EndpointDescriptor;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;

/**
 * Created by BigBlackBug on 26.05.2014.
 */
public class ServiceInfoPanel implements Refreshable<FullServiceDTO> {

//	private final VerticalLayoutContainer vlc;

	private final Label serviceName;

	private final Label serviceType;

	private final Label retryNumberLabel;

	private final Label retryDelayLabel;

	private final Label creatorLabel;

	private VBoxLayoutContainer container;

	private ContentPanel cp;

	private Refreshable<EndpointDescriptor> descriptor;

	public ServiceInfoPanel() {
		cp = new ContentPanel();
		cp.setHeaderVisible(false);

		serviceName = new Label();
		serviceType = new Label();
		retryNumberLabel = new Label();
		retryDelayLabel = new Label();
		creatorLabel = new Label();

		container = new VBoxLayoutContainer();
		container.setPadding(new Padding(5));
		container.setVBoxLayoutAlign(VBoxLayoutContainer.VBoxLayoutAlign.CENTER);
		container.setPack(BoxLayoutContainer.BoxLayoutPack.CENTER);

		BoxLayoutData layoutData = new BoxLayoutContainer.BoxLayoutData(new Margins(0, 0, 5, 0));
		container.add(new Label("название сервиса"), layoutData);
		container.add(serviceName, layoutData);
		container.add(new Label("Тип сервиса"), layoutData);
		container.add(serviceType, layoutData);
		//TODO init
		descriptor = new EndpointDescriptorPanel();
		container.add(descriptor);

		container.add(new Label("доставка"));
		container.add(new Label("количество повторов"), layoutData);
		container.add(retryNumberLabel, layoutData);
		container.add(new Label("интервал"), layoutData);
		container.add(retryDelayLabel, layoutData);
		container.add(new Label("Создатель"), layoutData);
		container.add(creatorLabel, layoutData);
	}

	@Override
	public Widget asWidget() {
		return cp;
	}

	@Override
	public void refresh(FullServiceDTO item) {
		serviceName.setText(item.getServiceName());
		serviceType.setText(item.getEndpoint().getEndpointType().toString());
		DeliverySettingsDTO deliverySettings = item.getDeliverySettings();
		retryNumberLabel.setText(String.valueOf(deliverySettings.getRetryNumber()));
		retryDelayLabel.setText(String.valueOf(deliverySettings.getRetryDelay()));
		creatorLabel.setText(item.getCreatorName());
		descriptor.refresh(item.getEndpoint());
		cp.clear();
		cp.add(container);
		cp.forceLayout();
	}
}
