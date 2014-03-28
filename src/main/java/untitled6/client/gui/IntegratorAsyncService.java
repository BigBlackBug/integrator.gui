package untitled6.client.gui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.icl.integrator.dto.*;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.registration.*;
import com.icl.integrator.httpclient.IntegratorClientException;
import untitled6.client.GreetingService;
import untitled6.client.GreetingServiceAsync;

import java.util.List;
import java.util.Map;

/**
 * Created by e.shahmaev on 28.03.2014.
 */
public class IntegratorAsyncService implements GreetingServiceAsync {

    private final GreetingServiceAsync service = GWT.create(GreetingService.class);

    private AppLoadingView getLoadingView() {
        final AppLoadingView loading = new AppLoadingView();
        loading.startProcessing();
        return loading;
    }

    @Override
    public void greetServer(String name, final AsyncCallback<String> async) {
        service.greetServer(name, new LoadingCallback<>(getLoadingView(), async));
    }

    @Override
    public void initClient(String host, String deployPath, int port, AsyncCallback<Void> async) {
        service.initClient(host, deployPath, port, new LoadingCallback<>(getLoadingView(), async));
    }

    @Override
    public <T extends DestinationDescriptor> void deliver(IntegratorPacket<DeliveryDTO, T> delivery,
                                                          AsyncCallback<ResponseDTO<Map<String, ResponseDTO<String>>>> async)
            throws IntegratorClientException {
        service.deliver(delivery, new LoadingCallback<>(getLoadingView(), async));
    }

    @Override
    public <T extends ActionDescriptor, Y extends DestinationDescriptor> void registerService(
            IntegratorPacket<TargetRegistrationDTO<T>, Y> registrationDTO,
            AsyncCallback<ResponseDTO<RegistrationResultDTO>> async)
            throws IntegratorClientException {
        service.registerService(registrationDTO, new LoadingCallback<>(getLoadingView(), async));

    }

    @Override
    public <T extends DestinationDescriptor> void getServiceList(IntegratorPacket<Void, T> packet,
                                                                 AsyncCallback<ResponseDTO<List<ServiceDTO>>> async)
            throws IntegratorClientException {
        service.getServiceList(packet, new LoadingCallback<>(getLoadingView(), async));
    }

    @Override
    public <T extends DestinationDescriptor, Y extends ActionDescriptor> void getSupportedActions(
            IntegratorPacket<ServiceDTO, T> serviceDTO,
            AsyncCallback<ResponseDTO<List<ActionEndpointDTO<Y>>>> async)
            throws IntegratorClientException {
        service.getSupportedActions(serviceDTO, new LoadingCallback<>(getLoadingView(), async));
    }

    @Override
    public <ADType extends ActionDescriptor, DDType extends DestinationDescriptor> void getServiceInfo(
            IntegratorPacket<ServiceDTO, DDType> serviceDTO,
            AsyncCallback<ResponseDTO<FullServiceDTO<ADType>>> async)
            throws IntegratorClientException {
        service
                .getServiceInfo(
                        serviceDTO,
                        new LoadingCallback<>(
                                getLoadingView(),
                                async)
                               );
    }

    @Override
    public <T extends DestinationDescriptor, Y extends ActionDescriptor> void addAction(
            IntegratorPacket<AddActionDTO<Y>, T> actionDTO, AsyncCallback<ResponseDTO<Void>> async)
            throws IntegratorClientException {
        service.addAction(actionDTO, new LoadingCallback<>(getLoadingView(), async));
    }

    @Override
    public <T extends DestinationDescriptor> void getActionsForDelivery(
            IntegratorPacket<Void, T> packet,
            AsyncCallback<ResponseDTO<List<DeliveryActionsDTO>>> async)
            throws IntegratorClientException {
        service.getActionsForDelivery(packet, new LoadingCallback<>(getLoadingView(), async));
    }

    @Override
    public <T extends DestinationDescriptor, Y extends ActionDescriptor> void getServicesSupportingActionType(
            IntegratorPacket<ActionMethod, T> packet,
            AsyncCallback<ResponseDTO<Map<String, ServiceAndActions<Y>>>> async)
            throws IntegratorClientException {
        service.getServicesSupportingActionType(packet,
                                                new LoadingCallback<>(getLoadingView(), async));
    }

    private static final class LoadingCallback<T> implements AsyncCallback<T> {

        private final AsyncCallback<T> async;

        private final AppLoadingView loading;

        private LoadingCallback(AppLoadingView loading, AsyncCallback<T> async) {
            this.async = async;
            this.loading = loading;
        }

        @Override
        public void onFailure(Throwable caught) {
            loading.stopProcessing();
            async.onFailure(caught);
        }

        @Override
        public void onSuccess(T result) {
            loading.stopProcessing();
            async.onSuccess(result);
        }
    }
}
