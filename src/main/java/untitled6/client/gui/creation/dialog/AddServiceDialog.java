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

//    private final HorizontalPanel reallyMainPanel;

    private final ListBox addActionsLB;

    private final VerticalPanel mainPanel;

    private Creator<EndpointDescriptor> inputDescCreator;

    private List<ActionRegistrationDTO<ActionDescriptor>> actionDesctiprors =new ArrayList<>();

    public AddServiceDialog(final CreationListener<TargetRegistrationDTO<ActionDescriptor>>
                                    creationListener) {
        setText("Sample DialogBox");

        HorizontalPanel buttonPanel = new HorizontalPanel();
        Button createButton = new Button("Create", new ClickHandler() {
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
        Button closeButton = new Button("Close", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        HTML msg = new HTML("<center>Create service.</center>", true);

//        reallyMainPanel = new HorizontalPanel();
        final VerticalPanel addServicePanel = new VerticalPanel();
        addServicePanel.add(new HTML("<br><b>ServiceName:</b>"));
        serviceNamesTB = new TextBox();
        addServicePanel.add(serviceNamesTB);
        typesBox = new ListBox();
        typesBox.setVisibleItemCount(2);
        typesBox.addItem("HTTP");
        typesBox.addItem("JMS");
        final HorizontalPanel inputPanel = new HorizontalPanel();
        typesBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int selectedIndex = typesBox.getSelectedIndex();
                if (selectedIndex == 0) {
                    inputPanel.clear();
                    inputDescCreator = new HttpServiceInputDescriptionPanel();
                    inputPanel.add((Composite) inputDescCreator);
                } else {
                    inputPanel.clear();
                    inputDescCreator = new JmsServiceInputDescriptionPanel();
                    inputPanel.add((Composite) inputDescCreator);
                }
            }
        });
        inputDescCreator = new HttpServiceInputDescriptionPanel();
        inputPanel.add((Composite)inputDescCreator);
        typesBox.setSelectedIndex(0);
        addServicePanel.add(typesBox);
        addServicePanel.add(inputPanel);

        deliverySettingsPanel = new DeliverySettingsPanel();
        addServicePanel.add(deliverySettingsPanel);
        Button addActionButton = new Button("ADDACTIONS");
        addActionButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {

                CreationListener<ActionRegistrationDTO<ActionDescriptor>> creationListener1 = new
                        CreationListener<ActionRegistrationDTO<ActionDescriptor>>() {
                            @Override
                            public void onCreated(ActionRegistrationDTO<ActionDescriptor> value) {
                                //выкидываем actionPanel
                                mainPanel.remove(mainPanel.getWidgetCount() - 1);
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

                mainPanel.add(actionPanel);
            }
        });
        addActionsLB = new ListBox();
        addActionsLB.setVisibleItemCount(10);
        addServicePanel.add(addActionButton);
//        reallyMainPanel.add(mainPanel);
//        reallyMainPanel.add(addActionsLB);


        mainPanel = new VerticalPanel();
        HorizontalPanel topPanel = new HorizontalPanel();
        topPanel.add(addServicePanel);
        topPanel.add(addActionsLB);
        mainPanel.add(topPanel);
        DockPanel dock = new DockPanel();
        dock.setSpacing(4);

        buttonPanel.add(closeButton);
        buttonPanel.add(createButton);
        dock.add(buttonPanel, DockPanel.SOUTH);
        dock.add(msg, DockPanel.NORTH);
        dock.add(mainPanel,DockPanel.CENTER);
        dock.setCellHorizontalAlignment(buttonPanel, DockPanel.ALIGN_RIGHT);
        dock.setWidth("100%");
        setWidget(dock);
    }

}
