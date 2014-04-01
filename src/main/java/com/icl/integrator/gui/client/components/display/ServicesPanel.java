package com.icl.integrator.gui.client.components.display;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.FullServiceDTO;
import com.icl.integrator.dto.IntegratorPacket;
import com.icl.integrator.dto.ResponseDTO;
import com.icl.integrator.dto.ServiceDTO;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.RegistrationResultDTO;
import com.icl.integrator.dto.registration.TargetRegistrationDTO;
import com.icl.integrator.gui.client.GenericCallback;
import com.icl.integrator.gui.client.GreetingServiceAsync;
import com.icl.integrator.gui.client.components.CreationListener;
import com.icl.integrator.gui.client.components.IntegratorAsyncService;
import com.icl.integrator.gui.client.components.creation.dialog.AddServiceDialog;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ServicesPanel extends Composite {

    private final IntegratorAsyncService service = GreetingServiceAsync.Util.getInstance();

    private final ActionsPanel actionsPanel;

    private final VerticalPanel infoPanel;

    private final ListBox list;

    private List<ServiceDTO> services;

    public ServicesPanel(List<ServiceDTO> aservices,
                         final ActionsPanel actionsPanel) {
        this.list = new ListBox();
        list.setVisibleItemCount(10);
        list.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                refillViews(list.getSelectedIndex());
            }
        });
        refresh(aservices);
        this.actionsPanel = actionsPanel;
        Button addServiceButton = new Button("+");
        addServiceButton.setWidth("100%");
        addServiceButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                new AddServiceDialog(new CreationListener<TargetRegistrationDTO<ActionDescriptor>>() {
                    @Override
                    public void onCreated(TargetRegistrationDTO<ActionDescriptor> value) {
                        service.registerService(
                        new IntegratorPacket<>(value),
                        new GenericCallback<RegistrationResultDTO>() {
                            @Override
                            public void onSuccess(RegistrationResultDTO result) {
                                PopupPanel widgets = new PopupPanel(true,false);
                                widgets.setWidget(createReportTable(result.getActionRegistrationResponses()));
                                widgets.center();

                                service.getServiceList(
                                        new IntegratorPacket<Void, DestinationDescriptor>(),
                                        new GenericCallback<List<ServiceDTO>>() {
                                            @Override
                                            public void onSuccess(List<ServiceDTO> result) {
                                                refresh(result);
                                            }
                                        });
                            }
                        });
                    }
                }).center();
            }
        });
        Button removeServiceButton = new Button("-");
        removeServiceButton.setEnabled(false);
        removeServiceButton.setWidth("100%");
        infoPanel = new VerticalPanel();

        FlexTable table = new FlexTable();
        table.setWidget(0, 0, addServiceButton);
        table.setWidget(0, 1, removeServiceButton);
        table.setWidget(1, 0, list);
        table.getFlexCellFormatter().setColSpan(1, 0, 2);
        table.setWidget(0, 2, infoPanel);
        table.getFlexCellFormatter().setRowSpan(0, 2, 3);
        table.setBorderWidth(1);
        initWidget(table);
    }

    private FlexTable createReportTable(Map<String, ResponseDTO<Void>> result) {
        FlexTable table = new FlexTable();
        int i = 1;
        Set<Map.Entry<String, ResponseDTO<Void>>> entries = result.entrySet();
        table.setWidget(0,0,new Label("Сервис зареган"));
        table.getFlexCellFormatter().setColSpan(0,0,2);
        for (Map.Entry<String, ResponseDTO<Void>> entry : entries) {
            String actionName = entry.getKey();
            ResponseDTO<Void> response = entry.getValue();
            table.setWidget(i, 0, new Label(actionName));
            Label statusLabel;
            if (response.isSuccess()) {
                statusLabel = new Label("OK");
                statusLabel.setStyleName("statusAV");
                table.setWidget(i, 1, statusLabel);
            } else {
                statusLabel = new Label(response.getError().getErrorMessage());
                statusLabel.setStyleName("statusNA");
                table.setWidget(i, 1, statusLabel);
            }
            i++;
        }
        return table;
    }

    private void refresh(List<ServiceDTO> services) {
        this.services = services;
        list.clear();
        for (ServiceDTO service : services) {
            list.addItem(service.getServiceName());
        }
        if (!services.isEmpty()) {
            list.setSelectedIndex(0);
            refillViews(0);
        }
    }

    public void refillViews(int index) {
        ServiceDTO serviceDTO = services.get(index);
        service.getServiceInfo(new IntegratorPacket<>(serviceDTO),
           new GenericCallback<FullServiceDTO<ActionDescriptor>>(){
               @Override
               public void onSuccess(FullServiceDTO<ActionDescriptor> result) {
                   actionsPanel.setActions(result);
                   infoPanel.clear();
                   infoPanel.add(new ServiceDescriptionPanel(result));
               }
           });
    }
}
