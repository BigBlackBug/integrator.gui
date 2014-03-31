package com.icl.integrator.gui.client.components.creation.dialog;

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

                if (selectedIndex == 0) {
                    table.removeCell(3,0);
                    inputDescCreator = new HttpServiceInputDescriptionPanel();
                    mergeTables(inputDescCreator.getTable(), table, 3, 0);
                    table.getFlexCellFormatter().setRowSpan(1, 2, table.getRowCount() - 1);
                    //fill table
                } else {
                    table.removeCells(3,0,2);
                    table.removeCells(3,0,2);

                    inputDescCreator = new JmsServiceInputDescriptionPanel();
                    table.setWidget(2, 0, inputDescCreator);
                    table.getFlexCellFormatter().setColSpan(2,0,2);
                    //fill table
                }

            }
        });
        inputDescCreator = new HttpServiceInputDescriptionPanel();

        typesBox.setSelectedIndex(0);

        deliverySettingsPanel = new DeliverySettingsPanel();
        Button addActionButton = new Button("Накинуть ищо действий");
        addActionButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            final int rowCount = table.getRowCount();
            CreationListener<ActionRegistrationDTO<ActionDescriptor>> creationListener1 = new
                    CreationListener<ActionRegistrationDTO<ActionDescriptor>>() {
                        @Override
                        public void onCreated(ActionRegistrationDTO<ActionDescriptor> value) {
                //выкидываем actionPanel
                table.removeCell(rowCount,0);
                table.getFlexCellFormatter().setRowSpan(1, 2, 2);
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
            table.setWidget(rowCount,0,actionPanel);
            table.getFlexCellFormatter().setColSpan(3,0,2);
            table.getFlexCellFormatter().setRowSpan(1, 2, 3);
            }
        });
        addActionsLB = new ListBox();
        addActionsLB.setVisibleItemCount(5);
        addActionsLB.setHeight("100%");

        HorizontalPanel buttonPanel = new HorizontalPanel();
        Button createButton = new Button("Создать", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                String serviceName = serviceNamesTB.getText();
                EndpointDescriptor endpointDescriptor = inputDescCreator.create();
                DeliverySettingsDTO deliverySettingsDTO;
                try {
                    deliverySettingsDTO = deliverySettingsPanel.create();
                }catch(CreationException cex){
                    PopupPanel widgets = new PopupPanel(true,false);
                    widgets.setWidget(new Label(cex.getCause().getMessage()));
                    widgets.center();
                    return;
                }
                creationListener.onCreated(
                        new TargetRegistrationDTO<>(serviceName, endpointDescriptor,
                                                    deliverySettingsDTO,
                                                    actionDesctiprors));
                hide();
            }
        });
        Button showSettings = new Button("Настройки доставки", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int rowCount = table.getRowCount();
                table.setWidget(rowCount, 0, deliverySettingsPanel);
                table.getFlexCellFormatter().setColSpan(3,0,2);
                table.getFlexCellFormatter().setRowSpan(1, 2, 3);
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
        buttonPanel.add(showSettings);
        buttonPanel.add(addActionButton);

        DockPanel dock = new DockPanel();
        dock.setSpacing(7);
        dock.add(buttonPanel, DockPanel.SOUTH);
        dock.add(table,DockPanel.CENTER);

        table.setWidget(0, 0, new HTML("<center><b>Cервис</b></center>"));
        table.setWidget(1, 0, new HTML("<b>Название сервиса:</b>"));
        table.setWidget(1, 1, serviceNamesTB);
        table.setWidget(2, 0, new HTML("<b>Тип сервиса:</b>"));
        table.setWidget(2, 1, typesBox);
        typesBox.setWidth("100%");
        mergeTables(inputDescCreator.getTable(), table, 3, 0);

//        table.setWidget(2, 0, (Composite) inputDescCreator);
//        table.getFlexCellFormatter().setColSpan(2, 0, 2);
        table.setWidget(0, 2, new HTML("<b>Действия:</b>"));
        addActionsLB.setWidth("100%");
        addActionsLB.setHeight("100%");
        table.setWidget(1, 2, addActionsLB);
        table.getFlexCellFormatter().setRowSpan(1, 2, table.getRowCount() - 1);
//        table.getFlexCellFormatter().setColSpan(0, 0, 2);
        table.setCellSpacing(3);
        table.setBorderWidth(1);
        setWidget(dock);
    }

    private void mergeTables(FlexTable sourceTable, FlexTable targetTable,
                             int startRow, int startCol) {
        for (int i = 0; i < sourceTable.getRowCount(); i++) {
            for (int j = 0; j < sourceTable.getCellCount(i); j++) {
                targetTable.setWidget(startRow + i, startCol + j, sourceTable.getWidget(i, j));
            }
        }
    }
}
