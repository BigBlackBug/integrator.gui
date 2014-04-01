package com.icl.integrator.gui.client.components.display;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.FullServiceDTO;
import com.icl.integrator.dto.IntegratorPacket;
import com.icl.integrator.dto.ServiceDTO;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.registration.ActionRegistrationDTO;
import com.icl.integrator.dto.registration.AddActionDTO;
import com.icl.integrator.gui.client.GenericCallback;
import com.icl.integrator.gui.client.GreetingServiceAsync;
import com.icl.integrator.gui.client.components.CreationListener;
import com.icl.integrator.gui.client.components.IntegratorAsyncService;
import com.icl.integrator.gui.client.components.creation.dialog.AddActionDialog;

import java.util.List;

public class ActionsPanel extends Composite {

    private final IntegratorAsyncService service = GreetingServiceAsync.Util.getInstance();

    private final ListBox listBox;

    private final VerticalPanel infoPanel;

    private List<ActionEndpointDTO<ActionDescriptor>> actions;

    private FullServiceDTO<ActionDescriptor> fullService;

    public ActionsPanel() {
        Button addActionButton = new Button("+");
        addActionButton.setWidth("100%");
        addActionButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new AddActionDialog(fullService.getEndpoint().getEndpointType(),
                                    new ActionCreatedListener()).center();
            }
        });
        Button removeAction = new Button("-");
        removeAction.setWidth("100%");
        removeAction.setEnabled(false);
        listBox = new ListBox();
        listBox.setVisibleItemCount(10);
        listBox.addClickHandler(new ActionsListBoxClickHandler());
        infoPanel = new VerticalPanel();

        FlexTable table = new FlexTable();
        table.setWidget(0, 0, addActionButton);
        table.setWidget(0, 1, removeAction);
        table.setWidget(1, 0, listBox);
        table.getFlexCellFormatter().setColSpan(1, 0, 2);
        table.setWidget(0, 2, infoPanel);
        table.getFlexCellFormatter().setRowSpan(0, 2, 3);
        table.setBorderWidth(1);
        initWidget(table);
    }

    public void setActions(FullServiceDTO<ActionDescriptor> service) {
        this.fullService = service;
        setAllActions(service.getActions());
    }

    private void setAllActions(List<ActionEndpointDTO<ActionDescriptor>> actions) {
        this.actions = actions;
        listBox.clear();
        for (ActionEndpointDTO item : actions) {
            listBox.addItem(item.getActionName());
        }
        if (!actions.isEmpty()) {
            listBox.setSelectedIndex(0);
            fillViews(0);
        } else {
            infoPanel.clear();
        }
    }

    private void fillViews(int index) {
        infoPanel.clear();
        infoPanel.add(new ActionDescriptorPanel(actions.get(index),fullService.getServiceName()));
    }

    private class ActionCreatedListener implements
            CreationListener<ActionRegistrationDTO<ActionDescriptor>> {

        @Override
        public void onCreated(ActionRegistrationDTO<ActionDescriptor> value) {
            final ServiceDTO serviceDTO =
                    new ServiceDTO(fullService.getServiceName(),
                                   fullService.getEndpoint()
                                           .getEndpointType()
                    );
            AddActionDTO<ActionDescriptor> addActionDTO =
                    new AddActionDTO<>(serviceDTO, value);
            service.addAction(new IntegratorPacket<>(addActionDTO),
                              new RefillPanelsCallback(serviceDTO));
        }
    }

    private class RefillPanelsCallback extends GenericCallback<Void> {

        private final ServiceDTO serviceDTO;

        protected RefillPanelsCallback(ServiceDTO serviceDTO) {
            this.serviceDTO = serviceDTO;
        }

        @Override
        public void onSuccess(Void result) {
            PopupPanel widgets = new PopupPanel(true, false);
            widgets.setWidget(new Label("Действие добавлено"));
            widgets.center();
            service.getSupportedActions(
                new IntegratorPacket<>(serviceDTO),
                new GenericCallback<
                        List<ActionEndpointDTO<ActionDescriptor>>>() {
                    @Override
                    public void onSuccess(List<ActionEndpointDTO<ActionDescriptor>> result) {
                        setAllActions(result);
                    }
                }
            );
        }
    }

    private class ActionsListBoxClickHandler implements ClickHandler {

        @Override
        public void onClick(ClickEvent clickEvent) {
            int selectedIndex = listBox.getSelectedIndex();
            fillViews(selectedIndex);
        }
    }
}
