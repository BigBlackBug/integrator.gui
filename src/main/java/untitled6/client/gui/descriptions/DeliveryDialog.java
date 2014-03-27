package untitled6.client.gui.descriptions;

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
import untitled6.client.gui.CreationListener;
import untitled6.client.gui.creation.EnumListBox;
import untitled6.client.gui.creation.ResponseHandlerSelectorPanel;

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

    private final HorizontalPanel lbPanel;

    private final CheckBox isAutoDetectCB;

    private final EnumListBox<DeliveryPacketType> deliveryTypesLB;

    private final ResponseHandlerSelectorPanel responseHandlerSelectorPanel;

    private final CheckBox integratorResponseCB;

    public DeliveryDialog(final List<DeliveryActionsDTO> actionToService,
                          Map<String, ServiceAndActions<ActionDescriptor>> services,
                          final CreationListener<DeliveryDTO> creationListener) {
        setModal(true);
        DockPanel dockPanel = new DockPanel();
        this.actionToServiceMap = actionToService;
        actionsLB = new ListBox();
        actionsLB.setTitle("select action");
        actionsLB.setVisibleItemCount(10);
        actionsLB.setMultipleSelect(false);
        servicesLB = new ListBox();
        servicesLB.setTitle("select services");
        servicesLB.setVisibleItemCount(10);
        servicesLB.setMultipleSelect(true);
        for (DeliveryActionsDTO action : actionToService) {
            actionsLB.addItem(action.getActionName());
        }
        if (actionsLB.getItemCount() != 0) {
            fillServicesLB(0);
        }
        actionsLB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                int selectedIndex = actionsLB.getSelectedIndex();
                fillServicesLB(selectedIndex);
            }
        });
        lbPanel = new HorizontalPanel();
        lbPanel.add(actionsLB);
        lbPanel.add(servicesLB);
        HorizontalPanel buttonPanel = new HorizontalPanel();
        Button createButton = new Button("Send", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                DeliveryDTO value;
                String deliveryData = deliveryDataTA.getText();
                RequestDataDTO requestDataDTO;
                JSONValue jsonValue;
                try{
                    jsonValue = JSONParser.parseStrict(deliveryData);
                }catch(Exception ex){
                    PopupPanel widgets = new PopupPanel(true,false);
                    widgets.setWidget(new Label("invalid json"));
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
                    List<ServiceDTO> destinations = new ArrayList<>();
                    for (int i = 0; i < servicesLB.getItemCount(); i++) {
                        if (servicesLB.isItemSelected(i)) {
                            List<ServiceDTO> services1 =
                                    actionToServiceMap.get(selectedIndex).getServices();
                            destinations.add(services1.get(i));
                        }
                    }
                    if(destinations.isEmpty()){
                        PopupPanel widgets = new PopupPanel(true,false);
                        widgets.setWidget(new Label("SELECT DESTINATIONS"));
                        widgets.center();
                        return;
                    }
                    requestDataDTO = new RequestDataDTO(DeliveryPacketType.UNDEFINED, data);
                    value = new DeliveryDTO(selectedAction, destinations, requestDataDTO,dd);
                }
                creationListener.onCreated(value);
                hide();
            }
        });
        Button closeButton = new Button("Close", new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });

        deliveryTypesLB = new EnumListBox<>(DeliveryPacketType.class, DeliveryPacketType.UNDEFINED);
        buttonPanel.add(closeButton);
        buttonPanel.add(createButton);
        isAutoDetectCB = new CheckBox("Автоопределение таргета");
        isAutoDetectCB.setValue(false);
        deliveryTypesLB.setVisible(false);
        lbPanel.setVisible(true);
        isAutoDetectCB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                lbPanel.setVisible(!isAutoDetectCB.getValue());
                deliveryTypesLB.setVisible(isAutoDetectCB.getValue());
            }
        });
        HorizontalPanel mainPanel = new HorizontalPanel();
//        mainPanel.add(new HTML("<center>Куда шлём</center>"));
        deliveryDataTA = new TextArea();
        mainPanel.add(lbPanel);
        mainPanel.add(deliveryDataTA);
        mainPanel.add(deliveryTypesLB);
        integratorResponseCB = new CheckBox("Шлём куда-нить ответ от таргета?");
        integratorResponseCB.setValue(false);
        integratorResponseCB.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Boolean selected = integratorResponseCB.getValue();
                responseHandlerSelectorPanel.setVisible(selected);
            }
        });
        responseHandlerSelectorPanel = new ResponseHandlerSelectorPanel(services);
        responseHandlerSelectorPanel.setVisible(false);
        VerticalPanel really = new VerticalPanel();
        really.add(mainPanel);
        really.add(integratorResponseCB);
        really.add(responseHandlerSelectorPanel);

        dockPanel.add(really, DockPanel.CENTER);
        dockPanel.add(isAutoDetectCB, DockPanel.NORTH);
        dockPanel.add(buttonPanel, DockPanel.SOUTH);
        setWidget(dockPanel);
    }

//    private void initGui(){
//        final FlexTable flexTb = new FlexTable();
//        flexTb.setBorderWidth(3);
//        CheckBox toast = new CheckBox("TOAddddddddddddddddddddddST");
//        toast.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
//            public void onValueChange(ValueChangeEvent<Boolean> event) {
//                widget10 = flexTb.getWidget(1, 0);
//                flexTb.getWidget(1,1).setVisible(event.getValue());
////				flexTb.getFlexCellFormatter().setColSpan(1, 1, event.getValue()?1:2);
//            }
//        });
////		Button toast =new Button("234");
//        toast.setWidth("100%");
//        toast.setHeight("100%");
//        flexTb.setWidget(0,0, toast);
//        flexTb.getFlexCellFormatter().setColSpan(0, 0, 3);
////		ListBox lb = new ListBox(); 1 0 2
////		lb.setVisibleItemCount(10);
//        ListBox listBox1 = getListBox();
//        listBox1.setHeight("100%");
//        flexTb.setWidget(1, 0, listBox1);
//        ListBox listBox = getListBox();
//        listBox.addItem("1231231232");
//        flexTb.setWidget(1,1, listBox);
//        flexTb.setWidget(1, 2, new TextArea());
//        flexTb.setWidget(2, 0, new CheckBox("TOAST2"));
//        flexTb.getFlexCellFormatter().setColSpan(2, 0, 3);
//        flexTb.setWidget(3, 0,  getListBox());
//        flexTb.setWidget(3, 1,  getListBox());
//        flexTb.getFlexCellFormatter().setColSpan(3, 0, 2);
//        RootPanel.get().add(flexTb);
//    }
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
            return json.toString();
        }
    }

}
