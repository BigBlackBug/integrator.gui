package com.icl.integrator.gui.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.icl.integrator.dto.*;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionMethod;
import com.icl.integrator.gui.client.components.CreationListener;
import com.icl.integrator.gui.client.components.IntegratorAsyncService;
import com.icl.integrator.gui.client.components.creation.dialog.DeliveryDialog;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by e.shahmaev on 25.03.2014.
 */
public class DeliveryButtonClickHandler implements ClickHandler {

    private final IntegratorAsyncService
            service = GreetingServiceAsync.Util.getInstance();

    @Override
    public void onClick(ClickEvent event) {
        service.getActionsForDelivery(new IntegratorPacket<Void,
                DestinationDescriptor>(), new GetServicesCallback());

    }

    private class GetServicesCallback extends GenericCallback<List<DeliveryActionsDTO>> {

        @Override
        public void onSuccess(List<DeliveryActionsDTO> result) {
            service.getServicesSupportingActionType(
                    new IntegratorPacket<>(ActionMethod.HANDLE_RESPONSE_FROM_TARGET),
                    new ShowDeliveryDialogCallback(result));
        }
    }

    private class ShowDeliveryDialogCallback extends GenericCallback<
                Map<String,ServiceAndActions<ActionDescriptor>>> {

        private final List<DeliveryActionsDTO> deliveryActions;

        protected ShowDeliveryDialogCallback(List<DeliveryActionsDTO> deliveryActions) {
            this.deliveryActions = deliveryActions;
        }

        @Override
        public void onSuccess(Map<String, ServiceAndActions<ActionDescriptor>> result) {
            new DeliveryDialog(deliveryActions,result,
               new CreationListener<DeliveryDTO>() {
                   @Override
                   public void onCreated(DeliveryDTO value) {
                       System.out.println("ready to deliver " + value);
                       deliver(value);
                   }
               }
            ).center();
        }

        private void deliver(DeliveryDTO deliveryDTO) {
            service.deliver(
                new IntegratorPacket<>(deliveryDTO),
                new GenericCallback<Map<String, ResponseDTO<String>>>() {
                    @Override
                    public void onSuccess(Map<String, ResponseDTO<String>> result) {
                        PopupPanel widgets = new PopupPanel(true, false);
                        widgets.setWidget(createReportTable(result));
                        widgets.center();
                    }
                });
        }

        private FlexTable createReportTable(Map<String, ResponseDTO<String>> result) {
            FlexTable table = new FlexTable();
            int i = 2;
            Set<Map.Entry<String, ResponseDTO<String>>> entries = result.entrySet();
            table.setWidget(0, 0, new Label("<center><b>Доставка успешно запущена</center></b>"));
            table.setWidget(1, 0, new Label("<center><b>Сервис</center></b>"));
            table.setWidget(1, 1, new Label("<center><b>Статус</center></b>"));
            table.setWidget(1, 2, new Label("<center><b>ID запроса</center></b>"));
            table.getFlexCellFormatter().setColSpan(0, 0, 3);
            for (Map.Entry<String, ResponseDTO<String>> entry : entries) {
                String actionName = entry.getKey();
                ResponseDTO<String> response = entry.getValue();
                table.setWidget(i, 0, new Label(actionName));
                Label statusLabel;
                if (response.isSuccess()) {
                    statusLabel = new Label("OK");
                    statusLabel.setStyleName("statusAV");
                    table.setWidget(i, 1, statusLabel);
                    table.setWidget(i, 2, new Label(response.getResponse().getResponseValue()));
                } else {
                    statusLabel = new Label(response.getError().getErrorMessage());
                    statusLabel.setStyleName("statusNA");
                    table.setWidget(i, 1, statusLabel);
                    table.getFlexCellFormatter().setColSpan(i, 1, 2);
                }
                i++;
            }
            return table;
        }
    }
}
