package com.icl.integrator.gui.client.components;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.icl.integrator.dto.*;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.destination.ServiceDestinationDescriptor;
import com.icl.integrator.dto.registration.*;
import com.icl.integrator.gui.client.GenericCallback;
import com.icl.integrator.gui.client.GreetingService;
import com.icl.integrator.gui.client.GreetingServiceAsync;
import com.icl.integrator.httpclient.exceptions.AuthException;
import com.icl.integrator.httpclient.exceptions.IntegratorClientException;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.Dialog;

import java.util.List;
import java.util.Map;

/**
 * Created by e.shahmaev on 28.03.2014.
 */
public class IntegratorAsyncService{

	private static String username;

	private final GreetingServiceAsync service = GWT.create(GreetingService.class);

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		IntegratorAsyncService.username = username;
	}

	public void login(String username, String password, AsyncCallback<Void> async)
			throws IntegratorClientException,AuthException {
		service.login(username, password, new LoadingCallback<Void>(async));
	}

	public void logout(AsyncCallback<Void> async) throws IntegratorClientException {
		service.logout(new LoadingCallback<Void>(async));
	}

	public void initClient(String host, String deployPath, int port, AsyncCallback<Void> async)
			throws IntegratorClientException {
		service.initClient(host, deployPath, port, new LoadingCallback<Void>(async));
	}

	public <T extends DestinationDescriptor> void deliver(IntegratorPacket<DeliveryDTO, T> delivery,
                                                          AsyncCallback<Map<String, ResponseDTO<String>>> async)
            throws IntegratorClientException {
        service.deliver(delivery, new IntegratorLoadingCallback<Map<String, ResponseDTO<String>>>(async));
    }

    public <T extends ActionDescriptor, Y extends DestinationDescriptor> void registerService(
            IntegratorPacket<TargetRegistrationDTO<T>, Y> registrationDTO,
            AsyncCallback<List<ActionRegistrationResultDTO>> async)
            throws IntegratorClientException {
        service.registerService(registrationDTO, new IntegratorLoadingCallback<List<ActionRegistrationResultDTO>>(async));

    }

    public <T extends DestinationDescriptor> void getServiceList(IntegratorPacket<Void, T> packet,
                                                                 AsyncCallback<List<ServiceDTO>> async)
            throws IntegratorClientException {
        service.getServiceList(packet, new IntegratorLoadingCallback<List<ServiceDTO>>(async));
    }

    public <T extends DestinationDescriptor, Y extends ActionDescriptor> void getSupportedActions(
            IntegratorPacket<String, T> serviceDTO,
            AsyncCallback<List<ActionEndpointDTO<Y>>> async)
            throws IntegratorClientException {
        service.getSupportedActions(serviceDTO, new IntegratorLoadingCallback<List<ActionEndpointDTO<Y>>>(async));
    }

    public <ADType extends ActionDescriptor, DDType extends DestinationDescriptor> void getServiceInfo(
            IntegratorPacket<String, DDType> serviceDTO,
            AsyncCallback<FullServiceDTO<ADType>> async)
            throws IntegratorClientException {
        service.getServiceInfo(serviceDTO, new IntegratorLoadingCallback<FullServiceDTO<ADType>>(async));
    }

    public <T extends DestinationDescriptor, Y extends ActionDescriptor> void addAction(
            IntegratorPacket<AddActionDTO<Y>, T> actionDTO, AsyncCallback<Void> async)
    throws IntegratorClientException {
        service.addAction(actionDTO, new IntegratorLoadingCallback<Void>(async));
    }

    public <T extends DestinationDescriptor> void getActionsForDelivery(
            IntegratorPacket<Void, T> packet,
            AsyncCallback<List<DeliveryActionsDTO>> async)
    throws IntegratorClientException {
        service.getActionsForDelivery(packet, new IntegratorLoadingCallback<List<DeliveryActionsDTO>>(async));
    }

    public <T extends DestinationDescriptor, Y extends ActionDescriptor> void getServicesSupportingActionType(
            IntegratorPacket<ActionMethod, T> packet,
            AsyncCallback<List<ServiceAndActions<Y>>> async)
    throws IntegratorClientException {
        service.getServicesSupportingActionType(packet,
                                                new IntegratorLoadingCallback<List<ServiceAndActions<Y>>>(async));
    }

    public <T extends DestinationDescriptor>
    void
    isAvailable(IntegratorPacket<ServiceDestinationDescriptor,T> packet, AsyncCallback<Boolean> async){
	    service.isAvailable(packet, new IntegratorLoadingCallback<Boolean>(async));
    }

	public <T extends DestinationDescriptor>
	void
	isAvailable(IntegratorPacket<ServiceDestinationDescriptor,T> packet, AsyncCallback<Boolean> async,Component component){
		service.isAvailable(packet, new IntegratorLoadingCallback<Boolean>(async,component));
	}

    private static final class LoadingCallback<T> extends AbstractCallback<T> {

        private final AsyncCallback<T> async;

	    private LoadingCallback(AsyncCallback<T> async, Component component) {
		    super(component);
		    this.async = async;
	    }

	    private LoadingCallback(AsyncCallback<T> async) {
		    this.async = async;
	    }

	    @Override
	    public void onFailure(Throwable caught) {
		    super.onFailure(caught);
		    async.onFailure(caught);
		    Throwable cause = caught.getCause();
		    String message = (cause != null ? cause.toString() : caught.toString());
		    createDialog(message, null, "Неудачный запрос к интегратору").show();
	    }

	    @Override
        public void onSuccess(T result) {
            super.onSuccess(result);
            async.onSuccess(result);
        }
    }

    private static class HasView<T> implements AsyncCallback<T> {

	    private final Component loading;

	    protected HasView(Component component) {
		    this.loading = component;
		    loading.mask("Загрузка");
	    }

	    protected HasView() {
		    this(new AppLoadingView());
	    }

//	    private AppLoadingView createLoadingView() {
//		    final AppLoadingView loading = new AppLoadingView();
//		    loading.mask();
//		    return loading;
//	    }

	    protected void hide() {
		    loading.unmask();
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

	private static class AbstractCallback<T> extends HasView<T>{

		protected AbstractCallback(Component component) {
			super(component);
		}

		protected AbstractCallback() {
		}

		protected Dialog createDialog(final String message, final String extraMessage,
		                                 String dialogHeader) {
			final Dialog dialog = new Dialog();
			dialog.setWidth(600);
			dialog.setBlinkModal(true);
			dialog.setModal(true);
			dialog.setPredefinedButtons(Dialog.PredefinedButton.CLOSE);
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
						dialog.getElement().getStyle().setProperty("width", "auto");
						Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
							public void execute() {
								dialog.show();
							}
						});
					}
				};
				button.addClickHandler(handler);
				dialogVPanel.add(button);
			}

			dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
			dialog.setWidget(dialogVPanel);
			dialogVPanel.setSpacing(3);
			dialog.setHeadingText(dialogHeader);
			return dialog;
		}
	}

    private static final class IntegratorLoadingCallback<T> extends AbstractCallback<ResponseDTO<T>> {

        private final AsyncCallback<T> async;

	    private IntegratorLoadingCallback(AsyncCallback<T> async, Component component) {
		    super(component);
		    this.async = async;
	    }

	    private IntegratorLoadingCallback(AsyncCallback<T> async) {
		    this.async = async;
	    }

	    @Override
	    public void onFailure(Throwable caught) {
            super.onFailure(caught);
            async.onFailure(caught);
            Throwable cause = caught.getCause();
            String message = (cause != null ? cause.toString() : caught.toString());
            createDialog(message, null, "Неудачный запрос к интегратору").show();
        }

        @Override
        public void onSuccess(ResponseDTO<T> result) {
            super.onSuccess(result);
            if (result.isSuccess()) {
                async.onSuccess(result.getResponse());
            } else {
                if(async instanceof GenericCallback){
                    ((GenericCallback)async).onError(result.getError());
                }
                String errorMessage = result.getError().getErrorMessage();
                createDialog(errorMessage, result.getError().getDeveloperMessage(),
                             "Ответ от интегратора").show();
            }
        }

    }
}
