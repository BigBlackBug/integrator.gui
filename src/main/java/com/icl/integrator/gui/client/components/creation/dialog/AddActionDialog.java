package com.icl.integrator.gui.client.components.creation.dialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.registration.ActionRegistrationDTO;
import com.icl.integrator.dto.util.EndpointType;
import com.icl.integrator.gui.client.components.CreationListener;
import com.icl.integrator.gui.client.components.descriptions.HttpActionInputDescriptionPanel;
import com.icl.integrator.gui.client.components.descriptions.JmsActionInputDescriptorPanel;
import com.icl.integrator.gui.client.util.Creator;

//TODO засунуть сюда addActionpanel
/**
 * Created by e.shahmaev on 19.03.14.
 */
public class AddActionDialog extends DialogBox {

    private final CheckBox forceRegisterCB;

    private Creator<ActionEndpointDTO<ActionDescriptor>> actionDescriptorCreator;

    public AddActionDialog(EndpointType endpointType, final
    CreationListener<ActionRegistrationDTO<ActionDescriptor>>
            creationListener) {
        setText("Создание Действия");
        DockPanel dock = new DockPanel();
        dock.setSpacing(4);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(3);
        Button createButton = new Button("Создать", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
                ActionEndpointDTO<ActionDescriptor> action = actionDescriptorCreator.create();
                boolean forceRegister = forceRegisterCB.getValue();
                creationListener.onCreated(new ActionRegistrationDTO<>(action, forceRegister));
            }
        });
        Button closeButton = new Button("Не создать", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        buttonPanel.add(closeButton);
        buttonPanel.add(createButton);
        dock.add(buttonPanel, DockPanel.SOUTH);
        if (endpointType == EndpointType.HTTP) {
            actionDescriptorCreator = new HttpActionInputDescriptionPanel();
        } else {
            actionDescriptorCreator = new JmsActionInputDescriptorPanel();
        }
        forceRegisterCB = new CheckBox("Зарегать даже если действие недоступно?");
        FlexTable table = new FlexTable();
        table.setWidget(0, 0, forceRegisterCB);
        table.setWidget(1, 0, (Composite) actionDescriptorCreator);
        dock.add(table, DockPanel.CENTER);
        setWidget(dock);
    }

}
