package com.icl.integrator.gui.client.components.creation;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ListBox;
import com.icl.integrator.dto.ServiceAndActions;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.destination.ServiceDestinationDescriptor;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.gui.client.util.Creator;

import java.util.List;

/**
 * Created by e.shahmaev on 21.03.14.
 */
public class ResponseHandlerSelectorPanel extends Composite
        implements Creator<DestinationDescriptor> {

    private final ListBox servicesLB;

    private final ActionDisplayPanel actionsPanel;

    private List<ServiceAndActions<ActionDescriptor>> services;

    public ResponseHandlerSelectorPanel(List<ServiceAndActions<ActionDescriptor>> serviceList) {
        Grid panel = new Grid(1,2);
        this.services = serviceList;
        servicesLB = new ListBox();
        servicesLB.setVisibleItemCount(10);
        servicesLB.setMultipleSelect(false);
        servicesLB.setVisibleItemCount(10);
        servicesLB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                refillViews(servicesLB.getSelectedIndex());
            }
        });
        this.actionsPanel = new ActionDisplayPanel();
        panel.setWidget(0, 0, servicesLB);
        panel.setWidget(0, 1, actionsPanel);
        refresh(serviceList);
        initWidget(panel);
    }

    private void refresh(List<ServiceAndActions<ActionDescriptor>> services) {
        this.services = services;
        servicesLB.clear();
        for (ServiceAndActions<ActionDescriptor> service : services) {
            String serviceName = service.getService().getServiceName();
            servicesLB.addItem(serviceName);
        }
        if (!services.isEmpty()) {
            servicesLB.setSelectedIndex(0);
            refillViews(0);
        }
    }

    private void refillViews(int index) {
	    String serviceName = servicesLB.getValue(index);
        actionsPanel.setActions(serviceName, services.get(index).getActions());
    }

    @Override
    public DestinationDescriptor create() {
        int selectedIndex = servicesLB.getSelectedIndex();
        String serviceName = servicesLB.getValue(selectedIndex);

        ActionEndpointDTO<ActionDescriptor> endpointDTO = actionsPanel.getSelected();
        return new ServiceDestinationDescriptor(serviceName, endpointDTO.getActionName(),
                                                endpointDTO.getActionDescriptor()
                                                        .getEndpointType());
    }
}
