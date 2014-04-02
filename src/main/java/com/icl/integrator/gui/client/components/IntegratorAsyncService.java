package com.icl.integrator.gui.client.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.icl.integrator.dto.*;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.destination.ServiceDestinationDescriptor;
import com.icl.integrator.dto.registration.*;
import com.icl.integrator.gui.client.GenericCallback;
import com.icl.integrator.gui.client.GreetingService;
import com.icl.integrator.gui.client.GreetingServiceAsync;
import com.icl.integrator.httpclient.IntegratorClientException;

import java.util.List;
import java.util.Map;

/**
 * Created by e.shahmaev on 28.03.2014.
 */
public class IntegratorAsyncService {

    private final GreetingServiceAsync service = GWT.create(GreetingService.class);

    private AppLoadingView createLoadingView() {
        final AppLoadingView loading = new AppLoadingView();
        loading.center();
        return loading;
    }

    public void greetServer(String name, final AsyncCallback<String> async) {
        service.greetServer(name, new LoadingCallback<>(createLoadingView(), async));
    }

    public void initClient(String host, String deployPath, int port, AsyncCallback<Void> async) {
        service.initClient(host, deployPath, port, new LoadingCallback<>(createLoadingView(), async));
    }

    public <T extends DestinationDescriptor> void deliver(IntegratorPacket<DeliveryDTO, T> delivery,
                                                          AsyncCallback<Map<String, ResponseDTO<String>>> async)
            throws IntegratorClientException {
        service.deliver(delivery, new IntegratorLoadingCallback<>(createLoadingView(), async));
    }

    public <T extends ActionDescriptor, Y extends DestinationDescriptor> void registerService(
            IntegratorPacket<TargetRegistrationDTO<T>, Y> registrationDTO,
            AsyncCallback<RegistrationResultDTO> async)
            throws IntegratorClientException {
        service.registerService(registrationDTO, new IntegratorLoadingCallback<>(createLoadingView(), async));

    }

    public <T extends DestinationDescriptor> void getServiceList(IntegratorPacket<Void, T> packet,
                                                                 AsyncCallback<List<ServiceDTO>> async)
            throws IntegratorClientException {
        service.getServiceList(packet, new IntegratorLoadingCallback<>(createLoadingView(), async));
    }

    public <T extends DestinationDescriptor, Y extends ActionDescriptor> void getSupportedActions(
            IntegratorPacket<ServiceDTO, T> serviceDTO,
            AsyncCallback<List<ActionEndpointDTO<Y>>> async)
            throws IntegratorClientException {
        service.getSupportedActions(serviceDTO, new IntegratorLoadingCallback<>(createLoadingView(), async));
    }

    public <ADType extends ActionDescriptor, DDType extends DestinationDescriptor> void getServiceInfo(
            IntegratorPacket<ServiceDTO, DDType> serviceDTO,
            AsyncCallback<FullServiceDTO<ADType>> async)
            throws IntegratorClientException {
        service.getServiceInfo(serviceDTO, new IntegratorLoadingCallback<>(createLoadingView(), async));
    }

    public <T extends DestinationDescriptor, Y extends ActionDescriptor> void addAction(
            IntegratorPacket<AddActionDTO<Y>, T> actionDTO, AsyncCallback<Void> async)
    throws IntegratorClientException {
        service.addAction(actionDTO, new IntegratorLoadingCallback<>(createLoadingView(), async));
    }

    public <T extends DestinationDescriptor> void getActionsForDelivery(
            IntegratorPacket<Void, T> packet,
            AsyncCallback<List<DeliveryActionsDTO>> async)
    throws IntegratorClientException {
        service.getActionsForDelivery(packet, new IntegratorLoadingCallback<>(createLoadingView(), async));
    }

    public <T extends DestinationDescriptor, Y extends ActionDescriptor> void getServicesSupportingActionType(
            IntegratorPacket<ActionMethod, T> packet,
            AsyncCallback<Map<String, ServiceAndActions<Y>>> async)
    throws IntegratorClientException {
        service.getServicesSupportingActionType(packet,
                                                new IntegratorLoadingCallback<>(createLoadingView(), async));
    }

    public <T extends DestinationDescriptor>
    void
    isAvailable(IntegratorPacket<ServiceDestinationDescriptor,T> packet, AsyncCallback<Boolean> async){
        service.isAvailable(packet,new IntegratorLoadingCallback<>(createLoadingView(), async));
    }

    private static final class LoadingCallback<T> extends HasView<T> {

        private final AsyncCallback<T> async;

        private LoadingCallback(AppLoadingView loading, AsyncCallback<T> async) {
            super(loading);
            this.async = async;
        }

        @Override
        public void onFailure(Throwable caught) {
            super.onFailure(caught);
            async.onFailure(caught);
        }

        @Override
        public void onSuccess(T result) {
            super.onSuccess(result);
            async.onSuccess(result);
        }
    }

    private static class HasView<T> implements AsyncCallback<T> {

        private final AppLoadingView loading;

        protected HasView(AppLoadingView loading) {
            this.loading = loading;
        }

        protected void hide() {
            loading.hide();
        }

        @Override
        public void onFailure(Throwable caught) {
            hide();
        }

        @Override
        public void onSuccess(T result) {
            hide();
        }
    }

    private static final class IntegratorLoadingCallback<T> extends HasView<ResponseDTO<T>> {

        private final AsyncCallback<T> async;

        private IntegratorLoadingCallback(AppLoadingView loading, AsyncCallback<T> async) {
            super(loading);
            this.async = async;
        }

        @Override
        public void onFailure(Throwable caught) {
            super.onFailure(caught);
            async.onFailure(caught);
            Throwable cause = caught.getCause();
            String message = (cause != null ? cause.toString() : caught.toString());
            createDialog(message, null, "Неудачный запрос к интегратору").center();
        }

        @Override
        public void onSuccess(ResponseDTO<T> result) {
            super.onSuccess(result);
            if (result.isSuccess()) {
                SuccessDTO<T> response = result.getResponse();
                async.onSuccess(response != null ? response.getResponseValue() : null);
            } else {
                if(async instanceof GenericCallback){
                    ((GenericCallback)async).onError(result.getError());
                }
                String errorMessage = result.getError().getErrorMessage();
                createDialog(errorMessage, result.getError().getDeveloperMessage(),
                             "Ответ от интегратора").center();
            }
        }

        private DialogBox createDialog(final String message, final String extraMessage,
                                       String dialogHeader) {
            final DialogBox dialogBox = new DialogBox();
            final Button closeButton = new Button("Не верю!");
            closeButton.addClickHandler(new ClickHandler() {
                public void onClick(ClickEvent event) {
                    dialogBox.hide();
                }
            });
            closeButton.getElement().setId("closeButton");
            final VerticalPanel dialogVPanel = new VerticalPanel();
            dialogVPanel.addStyleName("dialogVPanel");
            dialogVPanel.add(new HTML("<b>Суть:</b>"));
            final HTML serverResponseLabel = new HTML();
            serverResponseLabel.addStyleName("serverResponseLabelError");
            serverResponseLabel.setHTML(message);
            dialogVPanel.add(serverResponseLabel);
            if (extraMessage != null && !extraMessage.isEmpty()) {
                final Button button = new Button("Ещё суть");
                ClickHandler handler = new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        HTML w = new HTML(extraMessage);
                        w.setWidth("500px");
                        w.setWordWrap(true);
                        dialogVPanel.add(w);
                        button.setVisible(false);
                        dialogBox.getElement().getStyle().setProperty("width", "auto");
                        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                            public void execute() {
                                dialogBox.center();
                            }
                        });
                    }
                };
                button.addClickHandler(handler);
                dialogVPanel.add(button);
            }

            dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
            dialogVPanel.add(closeButton);
            dialogBox.setWidget(dialogVPanel);
            dialogVPanel.setSpacing(3);
            dialogBox.setText(dialogHeader);
            return dialogBox;
        }
    }
}
