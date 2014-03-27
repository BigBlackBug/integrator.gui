package untitled6.client.gui.creation.dialog;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.icl.integrator.dto.*;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionMethod;
import untitled6.client.GenericCallback;
import untitled6.client.GreetingServiceAsync;
import untitled6.client.gui.CreationListener;
import untitled6.client.gui.descriptions.DeliveryDialog;

import java.util.List;
import java.util.Map;

/**
 * Created by e.shahmaev on 25.03.2014.
 */
public class DeliveryButtonClickHandler implements ClickHandler {

    private final GreetingServiceAsync
            service = GreetingServiceAsync.Util.getInstance();

    @Override
    public void onClick(ClickEvent event) {
        service.getActionsForDelivery(new IntegratorPacket<Void,
                DestinationDescriptor>(), new GetServicesCallback());

    }

    private class GetServicesCallback extends
            GenericCallback<ResponseDTO<List<DeliveryActionsDTO>>> {

        protected GetServicesCallback() {
            super("generic");
        }

        @Override
        public void onSuccess(ResponseDTO<List<DeliveryActionsDTO>> result) {
            final List<DeliveryActionsDTO> responseValue =
                    result.getResponse().getResponseValue();
            service.getServicesSupportingActionType(
                    new IntegratorPacket<>(ActionMethod.HANDLE_RESPONSE_FROM_TARGET),
                    new ShowDeliveryDialogCallback(responseValue));
        }
    }

    private class ShowDeliveryDialogCallback extends GenericCallback<
            ResponseDTO<Map<String,ServiceAndActions<ActionDescriptor>>>>{

        private final List<DeliveryActionsDTO> deliveryActions;

        protected ShowDeliveryDialogCallback(List<DeliveryActionsDTO> deliveryActions) {
            super("supp");
            this.deliveryActions = deliveryActions;
        }

        @Override
        public void onSuccess(ResponseDTO<Map<String, ServiceAndActions<ActionDescriptor>>> result) {
            Map<String, ServiceAndActions<ActionDescriptor>>
                    serviceAndActions =result.getResponse().getResponseValue();
            new DeliveryDialog(deliveryActions,serviceAndActions,
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
                new GenericCallback<ResponseDTO<Map<String, ResponseDTO<String>>>>("delivery") {
                    @Override
                    public void onSuccess(
                            ResponseDTO<Map<String, ResponseDTO<String>>> result) {
                        System.out.println(result);
                    }
                });
        }
    }
}
