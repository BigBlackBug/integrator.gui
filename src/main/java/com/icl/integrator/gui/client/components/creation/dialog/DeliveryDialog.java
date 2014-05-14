package com.icl.integrator.gui.client.components.creation.dialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.*;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.gui.client.components.CreationListener;
import com.icl.integrator.gui.client.components.EnumListBox;
import com.icl.integrator.gui.client.components.creation.ResponseHandlerSelectorPanel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by e.shahmaev on 20.03.14.
 */
public class DeliveryDialog extends DialogBox {

    private final List<DeliveryActionsDTO> actionToServiceMap;

    private final ListBox actionsLB;

    private final ListBox servicesLB;

    private final TextArea deliveryDataTA;

    private final CheckBox isAutoDetectCB;

    private final EnumListBox<DeliveryPacketType> deliveryTypesLB;

    private final ResponseHandlerSelectorPanel responseHandlerSelectorPanel;

    private final CheckBox integratorResponseCB;

    public DeliveryDialog(final List<DeliveryActionsDTO> actionToService,
                          List<ServiceAndActions<ActionDescriptor>> services,
                          final CreationListener<DeliveryDTO> creationListener) {
        setModal(true);
        DockPanel dockPanel = new DockPanel();
        dockPanel.add(new HTML("<center>Диалоги о доставке</center>"), DockPanel.NORTH);
        this.actionToServiceMap = actionToService;
        actionsLB = new ListBox();
        actionsLB.setVisibleItemCount(10);
        actionsLB.setMultipleSelect(false);
        servicesLB = new ListBox();
        servicesLB.setVisibleItemCount(10);
        servicesLB.setMultipleSelect(true);
        for (DeliveryActionsDTO action : actionToService) {
            actionsLB.addItem(action.getActionName());
        }
        if (actionsLB.getItemCount() != 0) {
            fillServicesLB(0);
            actionsLB.setSelectedIndex(0);
        }
        actionsLB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int selectedIndex = actionsLB.getSelectedIndex();
                fillServicesLB(selectedIndex);
            }
        });

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(5);
        Button createButton = new Button("Доставить!", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                DeliveryDTO value;
                String deliveryData = deliveryDataTA.getText();
                RequestDataDTO requestDataDTO;
                JSONValue jsonValue;
                try{
                    jsonValue = JSONParser.parseStrict(deliveryData);
                    if (jsonValue.isObject() == null) {
                        throw new IllegalArgumentException();
                    }
                }catch(Exception ex){
                    PopupPanel widgets = new PopupPanel(true,false);
                    widgets.setWidget(new Label("У Вас кривой JSON"));
                    widgets.center();
                    return;
                }
                Map<String, Object> data = (Map<String, Object>) toMap(jsonValue);
                DestinationDescriptor dd = null;
                if (integratorResponseCB.getValue()) {
                    dd = responseHandlerSelectorPanel.create();
                }
                if (isAutoDetectCB.getValue()) {
                    requestDataDTO = new RequestDataDTO(deliveryTypesLB.create(), data);
                    value = new DeliveryDTO(requestDataDTO,dd);
                } else {
                    int selectedIndex = actionsLB.getSelectedIndex();
                    String selectedAction = actionsLB.getValue(selectedIndex);
                    List<String> destinations = new ArrayList<>();
                    for (int i = 0; i < servicesLB.getItemCount(); i++) {
                        if (servicesLB.isItemSelected(i)) {
                            List<ServiceDTO> services1 =
                                    actionToServiceMap.get(selectedIndex).getServices();
                            destinations.add(services1.get(i).getServiceName());
                        }
                    }
                    if(destinations.isEmpty()){
                        PopupPanel widgets = new PopupPanel(true,false);
                        widgets.setWidget(new Label("Выбери сервисы"));
                        widgets.center();
                        return;
                    }
                    requestDataDTO = new RequestDataDTO(DeliveryPacketType.UNDEFINED, data);
                    value = new DeliveryDTO(requestDataDTO, dd, selectedAction, destinations);
                }
                creationListener.onCreated(value);
                hide();
            }
        });
        Button closeButton = new Button("Не доставлять", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });

        deliveryTypesLB = new EnumListBox<>(DeliveryPacketType.class, DeliveryPacketType.UNDEFINED);
        buttonPanel.add(closeButton);
        buttonPanel.add(createButton);
        final FlexTable flexTb = new FlexTable();
        flexTb.setBorderWidth(3);
        isAutoDetectCB = new CheckBox("Автоопределение таргета");
        isAutoDetectCB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
//                lbPanel.setVisible(!isAutoDetectCB.getValue());
//                deliveryTypesLB.setVisible(isAutoDetectCB.getValue());
                Boolean autoDetectionMode = isAutoDetectCB.getValue();
                flexTb.removeAllRows();
                if (autoDetectionMode) {
                    flexTb.setWidget(0, 0, isAutoDetectCB);
                    flexTb.getFlexCellFormatter().setColSpan(0, 0, 2);
                    flexTb.setWidget(1, 0, deliveryTypesLB);
                    flexTb.setWidget(1, 1, deliveryDataTA);
                    flexTb.getFlexCellFormatter().setColSpan(1, 1, 2);
                    flexTb.setWidget(2, 0, integratorResponseCB);
                    flexTb.getFlexCellFormatter().setColSpan(2, 0, 2);
                } else {
                    flexTb.setWidget(0, 0, isAutoDetectCB);
                    flexTb.getFlexCellFormatter().setColSpan(0, 0, 3);
                    flexTb.setWidget(1, 0, actionsLB);
                    flexTb.setWidget(1, 1, servicesLB);
                    flexTb.setWidget(1, 2, deliveryDataTA);
                    flexTb.setWidget(2, 0, integratorResponseCB);
                    flexTb.getFlexCellFormatter().setColSpan(2, 0, 3);
                }
            }
        });
        isAutoDetectCB.setValue(false);
        isAutoDetectCB.setWidth("100%");
        isAutoDetectCB.setHeight("100%");
        flexTb.setWidget(0, 0, isAutoDetectCB);
        flexTb.getFlexCellFormatter().setColSpan(0, 0, 3);
        flexTb.setWidget(1, 0, actionsLB);
        flexTb.setWidget(1, 1, servicesLB);
        deliveryDataTA = new TextArea();
        flexTb.setWidget(1, 2, deliveryDataTA);
        integratorResponseCB = new CheckBox("Шлём куда-нить ответ от таргета?");
        integratorResponseCB.setValue(false);
        responseHandlerSelectorPanel = new ResponseHandlerSelectorPanel(services);
        integratorResponseCB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Boolean selected = integratorResponseCB.getValue();
                if (selected) {
                    flexTb.setWidget(3, 0, responseHandlerSelectorPanel);
                    flexTb.getFlexCellFormatter().setColSpan(3, 0, 3);
                } else {
                    flexTb.removeRow(3);
                }
            }
        });
        flexTb.setWidget(2, 0, integratorResponseCB);
        flexTb.getFlexCellFormatter().setColSpan(2, 0, 3);
        dockPanel.add(flexTb, DockPanel.CENTER);
        dockPanel.add(buttonPanel, DockPanel.SOUTH);
        setWidget(dockPanel);
    }

    private void fillServicesLB(int selectedIndex) {
        List<ServiceDTO> serviceDTOs = actionToServiceMap.get(selectedIndex).getServices();
        servicesLB.clear();
        for (ServiceDTO serviceDTO : serviceDTOs) {
            servicesLB.addItem(serviceDTO.getServiceName());
        }
    }

    private Object toMap(JSONValue json) {
        if (json.isNull() != null) {
            return null;
        } else if (json.isArray() != null) {
            JSONArray array = json.isArray();
            List list = new ArrayList();
            for (int i = 0; i < array.size(); i++) {
                list.add(toMap(array.get(i)));
            }
            return list;
        } else if (json.isObject() != null) {
            JSONObject object = json.isObject();
            Map<String, Object> result = new HashMap<>();
            for (String ke1y : object.keySet()) {
                result.put(ke1y, toMap(object.get(ke1y)));
            }
            return result;
        } else {
            String value = json.toString();
            if (value.startsWith("\"") && value.endsWith("\"")) {
                return value.substring(1, value.length() - 1);
            }
            return value;
        }
    }
}
