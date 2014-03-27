package untitled6.client.gui.creation.dialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionRegistrationDTO;
import com.icl.integrator.dto.registration.DeliverySettingsDTO;
import com.icl.integrator.dto.registration.TargetRegistrationDTO;
import com.icl.integrator.dto.source.EndpointDescriptor;
import com.icl.integrator.dto.util.EndpointType;
import untitled6.client.gui.CreationListener;
import untitled6.client.gui.creation.Creator;
import untitled6.client.gui.creation.DeliverySettingsPanel;
import untitled6.client.gui.creation.HttpServiceInputDescriptionPanel;
import untitled6.client.gui.creation.JmsServiceInputDescriptionPanel;

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

    private Creator<EndpointDescriptor> inputDescCreator;

    private List<ActionRegistrationDTO<ActionDescriptor>> actionDesctiprors =new ArrayList<>();

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
                table.removeCell(2,0);
                if (selectedIndex == 0) {
                    inputDescCreator = new HttpServiceInputDescriptionPanel();
                    //fill table
                } else {
                    inputDescCreator = new JmsServiceInputDescriptionPanel();
                    //fill table
                }
                table.setWidget(2,0,(Composite)inputDescCreator);
                table.getFlexCellFormatter().setColSpan(2,0,2);
            }
        });
        inputDescCreator = new HttpServiceInputDescriptionPanel();
        typesBox.setSelectedIndex(0);

        deliverySettingsPanel = new DeliverySettingsPanel();
        Button addActionButton = new Button("Накинуть ищо действий");
        addActionButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
            CreationListener<ActionRegistrationDTO<ActionDescriptor>> creationListener1 = new
                    CreationListener<ActionRegistrationDTO<ActionDescriptor>>() {
                        @Override
                        public void onCreated(ActionRegistrationDTO<ActionDescriptor> value) {
                        //выкидываем actionPanel
                            table.removeCell(0,3);
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
                table.setWidget(0,3,actionPanel);
                table.getFlexCellFormatter().setRowSpan(0,3,3);

            }
        });
        addActionsLB = new ListBox();
        addActionsLB.setVisibleItemCount(5);
        addActionsLB.setHeight("100%");

        HorizontalPanel buttonPanel = new HorizontalPanel();
        Button createButton = new Button("Создать", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
                String serviceName = serviceNamesTB.getText();
                EndpointDescriptor endpointDescriptor = inputDescCreator.create();
                DeliverySettingsDTO deliverySettingsDTO = deliverySettingsPanel.create();
                creationListener.onCreated(new TargetRegistrationDTO<>(serviceName,
                                                                       endpointDescriptor,
                                                                       deliverySettingsDTO,actionDesctiprors));
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
        dock.setSpacing(4);
        dock.add(buttonPanel, DockPanel.SOUTH);
        dock.add(table,DockPanel.CENTER);

//        table.setWidget(0,0,new HTML("<center><b>Название сервиса:</b>"));
        table.setWidget(0,0,new HTML("<b>Название сервиса:</b>"));
        table.setWidget(0,1,serviceNamesTB);
        table.setWidget(1,0,new HTML("<b>Тип сервиса:</b>"));
        table.setWidget(1,1,typesBox);
        typesBox.setWidth("100%");
        table.setWidget(2,0,(Composite)inputDescCreator);
        table.getFlexCellFormatter().setColSpan(2,0,2);
        table.setWidget(0,2,new HTML("<b>Действия:</b>"));
        addActionsLB.setWidth("100%");
        table.setWidget(1,2,addActionsLB);
        table.getFlexCellFormatter().setRowSpan(1,2,2);
        setWidget(dock);
    }
    FlexTable table = new FlexTable();
}
