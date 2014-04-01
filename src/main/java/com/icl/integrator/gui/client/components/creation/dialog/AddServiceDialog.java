package com.icl.integrator.gui.client.components.creation.dialog;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionRegistrationDTO;
import com.icl.integrator.dto.registration.DeliverySettingsDTO;
import com.icl.integrator.dto.registration.TargetRegistrationDTO;
import com.icl.integrator.dto.source.EndpointDescriptor;
import com.icl.integrator.dto.util.EndpointType;
import com.icl.integrator.gui.client.components.CreationListener;
import com.icl.integrator.gui.client.components.creation.*;
import com.icl.integrator.gui.client.util.CreationException;
import com.icl.integrator.gui.shared.GuiException;

import java.util.ArrayList;
import java.util.List;

//TODO нигде в create е валидируются числа
/**
 * Created by e.shahmaev on 19.03.14.
 */
public class AddServiceDialog extends DialogBox {

    private final ListBox typesBox;

    private final TextBox serviceNamesTB;

    private final DeliverySettingsPanel deliverySettingsPanel;

    private final ListBox addActionsLB;

    FlexTable table = new FlexTable();

    private InputDescriptionPanel inputDescCreator;

    private List<ActionRegistrationDTO<ActionDescriptor>> actionDesctiprors = new ArrayList<>();

    public AddServiceDialog(final CreationListener<TargetRegistrationDTO<ActionDescriptor>>
                                    creationListener) {
        setText("Создание сервиса");
        serviceNamesTB = new TextBox();
        serviceNamesTB.setWidth("100%");
        typesBox = new ListBox();
        typesBox.setVisibleItemCount(2);
        typesBox.addItem("HTTP");
        typesBox.addItem("JMS");
        typesBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int selectedIndex = typesBox.getSelectedIndex();
                table.removeCell(3,0);
                if (selectedIndex == 0) {
                    inputDescCreator = new HttpServiceInputDescriptionPanel();
                    //fill table
                } else {
                    inputDescCreator = new JmsServiceInputDescriptionPanel();
                    //fill table
                }
                table.setWidget(3,0,inputDescCreator);
                table.getFlexCellFormatter().setColSpan(3,0,2);
            }
        });
        inputDescCreator = new HttpServiceInputDescriptionPanel();

        typesBox.setSelectedIndex(0);

        deliverySettingsPanel = new DeliverySettingsPanel();
        Button addActionButton = new Button("Накинуть действий");
        addActionButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            CreationListener<ActionRegistrationDTO<ActionDescriptor>> creationListener1 = new
                    CreationListener<ActionRegistrationDTO<ActionDescriptor>>() {
                        @Override
                        public void onCreated(ActionRegistrationDTO<ActionDescriptor> value) {
                //выкидываем actionPanel
                            table.removeCell(1, 3);//1
                            table.removeCell(0, 2);//1
//                table.getFlexCellFormatter().setRowSpan(1, 2, 2);
                if (value != null) {
                    actionDesctiprors.add(value);
                    addActionsLB.addItem(value.getAction().getActionName());
                }
                        }
                    };
            int selectedIndex = typesBox.getSelectedIndex();
            AddActionPanel actionPanel;
            if (selectedIndex == 0) { //http
                actionPanel = new AddActionPanel(EndpointType.HTTP, creationListener1);
            } else {
                actionPanel = new AddActionPanel(EndpointType.JMS, creationListener1);
            }
            actionPanel.setWidth("100px");
                table.setWidget(0, 2, new HTML("<b><center>Добавление действия</center></b>"));
                table.setWidget(1, 3, actionPanel);
                table.getFlexCellFormatter().setRowSpan(1, 3, 4);
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    public void execute() {
                        center();
                    }
                });
            }
        });
        addActionsLB = new ListBox();
        addActionsLB.setVisibleItemCount(5);
        addActionsLB.setHeight("100%");

        //TODO накидать кучу проверок
        HorizontalPanel buttonPanel = new HorizontalPanel();
        Button createButton = new Button("Создать", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String serviceName;
                DeliverySettingsDTO deliverySettingsDTO;
                EndpointDescriptor endpointDescriptor;
                try {
                    serviceName = getServiceName();
                    endpointDescriptor = inputDescCreator.create();
                    deliverySettingsDTO = deliverySettingsPanel.create();
                } catch (CreationException cex) {
                    PopupPanel widgets = new PopupPanel(true, false);
                    DockPanel panel = new DockPanel();
                    HTML description =
                            new HTML("<b><center>" + cex.getFailedSubjectDescription() +
                                             "</center></b>");
                    panel.add(description, DockPanel.NORTH);
                    panel.add(new Label(cex.getCause().getMessage()), DockPanel.CENTER);
                    widgets.setWidget(panel);
                    widgets.center();
                    return;
                }
                creationListener.onCreated(
                        new TargetRegistrationDTO<>(serviceName, endpointDescriptor,
                                                    deliverySettingsDTO,
                                                    actionDesctiprors)
                                          );
                hide();
            }
        });

        Button closeButton = new Button("Не создать", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        buttonPanel.setSpacing(3);
        buttonPanel.add(closeButton);
        buttonPanel.add(createButton);
        buttonPanel.add(addActionButton);

        DockPanel dock = new DockPanel();
        dock.setSpacing(7);
        dock.add(buttonPanel, DockPanel.SOUTH);
        dock.add(table,DockPanel.CENTER);

        table.setWidget(0, 0, new HTML("<center><b>Сервис</b></center>"));
        table.setWidget(1, 0, new HTML("<b>Название сервиса:</b>"));
        table.setWidget(1, 1, serviceNamesTB);
        table.setWidget(2, 0, new HTML("<b>Тип сервиса:</b>"));
        table.setWidget(2, 1, typesBox);

        typesBox.setHeight("100%");
        typesBox.setWidth("100%");

        table.setWidget(3, 0, inputDescCreator);
        table.getFlexCellFormatter().setColSpan(0, 0, 2);
        table.getFlexCellFormatter().setColSpan(3, 0, 2);
        table.setWidget(0, 1, new HTML("<b>Действия</b>"));
        addActionsLB.setWidth("100%");
        addActionsLB.setHeight("100%");
        table.setWidget(1, 2, addActionsLB);
//        table.getFlexCellFormatter().setRowSpan(1, 2, 3);
        table.setWidget(4, 0, deliverySettingsPanel);
        table.getFlexCellFormatter().setColSpan(4, 0, 2);
        table.getFlexCellFormatter().setRowSpan(1, 2, 4);
        table.setCellSpacing(7);
        table.setWidth("100%");
        table.setBorderWidth(1);
        setWidth("100%");
        setWidget(dock);
    }

    private String getServiceName() {
        String serviceName = serviceNamesTB.getText();
        if (serviceName.isEmpty()) {
            throw new CreationException(new GuiException("Поле не может быть пустым"),
                                        "Название сервиса");
        }
        return serviceName;
    }
}
