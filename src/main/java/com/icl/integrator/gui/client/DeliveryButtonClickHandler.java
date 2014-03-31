package com.icl.integrator.gui.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.icl.integrator.dto.*;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionMethod;
import com.icl.integrator.gui.client.components.CreationListener;
import com.icl.integrator.gui.client.components.IntegratorAsyncService;
import com.icl.integrator.gui.client.components.creation.dialog.DeliveryDialog;
import java.util.List;
import java.util.Map;

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
                        System.out.println(result);
                        //TODO ну харош  Popup
                    }
                });
        }
    }
}
