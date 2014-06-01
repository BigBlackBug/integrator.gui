package com.icl.integrator.gui.client.gxt;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.DateLabel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.icl.integrator.dto.ErrorDTO;
import com.icl.integrator.dto.IntegratorPacket;
import com.icl.integrator.dto.ServiceDTO;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.destination.ServiceDestinationDescriptor;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.util.ErrorCode;
import com.icl.integrator.gui.client.GenericCallback;
import com.icl.integrator.gui.client.GreetingServiceAsync;
import com.icl.integrator.gui.client.components.IntegratorAsyncService;
import com.icl.integrator.gui.client.util.Creator;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.event.SelectEvent;

import java.util.Date;

/**
 * Created by BigBlackBug on 29.05.2014.
 */
public class ActionInfoPanel implements Refreshable<ActionEndpointDTO<ActionDescriptor>>{

	private final IntegratorAsyncService service = GreetingServiceAsync.Util.getInstance();

	private final Label statusLabel;

	private final Label actionNameLabel;

	private final Label actionMethodLabel;

	private final DateLabel lastCheckedLabel;

	private final Creator<ServiceDTO> servicesPanel;

	private ContentPanel cp;

	private Refreshable<ActionEndpointDTO<ActionDescriptor>> descriptorWidget;

	private ActionEndpointDTO<ActionDescriptor> descriptor;

	private VBoxLayoutContainer container;
	private ContentPanel check;
	public ActionInfoPanel(final Creator<ServiceDTO> servicesPanel) {
		this.servicesPanel = servicesPanel;
		descriptorWidget = new HttpActionDescriptorPanel();
		actionNameLabel = new Label();
		actionMethodLabel = new Label();
		statusLabel = new Label();
		lastCheckedLabel = new DateLabel(DateTimeFormat.getFormat("HH:mm:ss dd.MM.yyyy"));

		cp = new ContentPanel();
		cp.setHeaderVisible(false);
		container = new VBoxLayoutContainer();
		container.setPadding(new Padding(5));
		container.setVBoxLayoutAlign(VBoxLayoutContainer.VBoxLayoutAlign.CENTER);
		container.setPack(BoxLayoutContainer.BoxLayoutPack.CENTER);

		container.add(new Label("Название действия:"));
		container.add(actionNameLabel);
		container.add(new Label("Тип действия:"));
		container.add(actionMethodLabel);
		descriptorWidget = new ActionDescriptorPanel();

		check = new ContentPanel();
		VBoxLayoutContainer checkVBOX = new VBoxLayoutContainer();
		checkVBOX.add(statusLabel);
		checkVBOX.add(new Label("Последняя проверка"));
		checkVBOX.add(lastCheckedLabel);
		check.add(checkVBOX);
		check.setVisible(false);
		check.setHeaderVisible(false);

		TextButton pingButton = new TextButton("Пингануть");
		pingButton.addSelectHandler(new SelectEvent.SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				ServiceDTO serviceDTO = servicesPanel.create();
				ServiceDestinationDescriptor serviceDestinationDescriptor =
						new ServiceDestinationDescriptor(serviceDTO.getServiceName(),
						                                 descriptor.getActionName(),
						                                 descriptor.getActionDescriptor()
								                                 .getEndpointType());
				service.isAvailable(new IntegratorPacket<ServiceDestinationDescriptor,
						                    DestinationDescriptor>(serviceDestinationDescriptor),
				                    new GenericCallback<Boolean>() {
					                    @Override
					                    public void onSuccess(Boolean result) {
						                    check.setVisible(true);
						                    statusLabel.setText("Действие доступно");
						                    lastCheckedLabel.setValue(new Date());
						                    container.forceLayout();
					                    }

					                    @Override
					                    public void onError(ErrorDTO errorDTO) {
						                    if (errorDTO.getErrorCode() ==
								                    ErrorCode.SERVICE_NOT_AVAILABLE) {
							                    check.setVisible(true);
							                    statusLabel.setText("Действие недоступно");
							                    lastCheckedLabel.setValue(new Date());
							                    container.forceLayout();
						                    }
					                    }
				                    });
			}
		});
		container.add(descriptorWidget);
		container.add(pingButton);
		container.add(check);
	}

	@Override
	public void refresh(ActionEndpointDTO<ActionDescriptor> descriptor) {
		this.descriptor = descriptor;
		ActionDescriptor actionDescriptor = descriptor.getActionDescriptor();
		this.actionMethodLabel.setText(String.valueOf(actionDescriptor.getActionMethod()));
		this.actionNameLabel.setText(descriptor.getActionName());
		descriptorWidget.refresh(descriptor);
		cp.clear();
		cp.add(container);
		cp.forceLayout();
	}

	@Override
	public Widget asWidget() {
		return cp;
	}
}
