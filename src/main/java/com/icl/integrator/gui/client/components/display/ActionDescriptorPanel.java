package com.icl.integrator.gui.client.components.display;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.user.client.ui.*;
import com.icl.integrator.dto.ErrorCode;
import com.icl.integrator.dto.ErrorDTO;
import com.icl.integrator.dto.IntegratorPacket;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.destination.ServiceDestinationDescriptor;
import com.icl.integrator.dto.registration.*;
import com.icl.integrator.dto.util.EndpointType;
import com.icl.integrator.gui.client.GenericCallback;
import com.icl.integrator.gui.client.GreetingServiceAsync;
import com.icl.integrator.gui.client.components.IntegratorAsyncService;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: e.shahmaev
 * Date: 18.03.14
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class ActionDescriptorPanel extends Composite {

    private final IntegratorAsyncService service = GreetingServiceAsync.Util.getInstance();

    private final ServiceDestinationDescriptor serviceDestinationDescriptor;

    private final Label statusLabel;

    private final DateLabel lastCheckedLabel;

    private final VerticalPanel vp;

    public ActionDescriptorPanel(ActionEndpointDTO actionEndpoint,String serviceName) {
        serviceDestinationDescriptor =
                new ServiceDestinationDescriptor(serviceName, actionEndpoint.getActionName(),
                                                 actionEndpoint.getActionDescriptor()
                                                         .getEndpointType());
        VerticalPanel mainPanel = new VerticalPanel();
        String actionName = actionEndpoint.getActionName();
        ActionDescriptor actionDescriptor = actionEndpoint.getActionDescriptor();
        EndpointType endpointType = actionDescriptor.getEndpointType();
        ActionMethod actionMethod = actionDescriptor.getActionMethod();

        mainPanel.add(new HTML("<br><b>Название действия:</b>"));
        mainPanel.add(new HTML(actionName));
        mainPanel.add(new HTML("<br><b>Тип действия:</b>"));
        mainPanel.add(new HTML(actionMethod.toString()));

        if (endpointType == EndpointType.HTTP) {
            HttpActionDTO descriptor =
                    (HttpActionDTO) actionDescriptor;
            String path = descriptor.getPath();
            mainPanel.add(new HTML("<br><b>Путь к сервису:</b>"));
            mainPanel.add(new HTML(path));
        } else {
            QueueDTO descriptor = (QueueDTO) actionDescriptor;
            String queueName = descriptor.getQueueName();
            String password = descriptor.getPassword();
            String username = descriptor.getUsername();
            mainPanel.add(new HTML("<br><b>Название очереди:</b>"));
            mainPanel.add(new HTML(queueName));
            if (username != null) {
                mainPanel.add(new HTML("<br><b>Имя пользователя:</b>"));
                mainPanel.add(new HTML(username));
            }
            if (password != null) {
                mainPanel.add(new HTML("<br><b>Пароль:</b>"));
                mainPanel.add(new HTML(password));
            }
        }
        statusLabel = new Label();
        vp = new VerticalPanel();
        vp.add(new HTML("<br>"));
        vp.add(statusLabel);
        vp.add(new HTML("<b>Последняя проверка</b>"));
        lastCheckedLabel = new DateLabel(DateTimeFormat.getFormat("HH:mm:ss dd.MM.yyyy"));
        vp.add(lastCheckedLabel);
        mainPanel.add(vp);
        vp.setVisible(false);

        mainPanel.add(new Button("Пингануть", new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                service.isAvailable(new IntegratorPacket<ServiceDestinationDescriptor,
                                        DestinationDescriptor>(serviceDestinationDescriptor),
                new GenericCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        vp.setVisible(true);
                        statusLabel.setText("Действие доступно");
                        statusLabel.setStyleName("statusAV");
                        lastCheckedLabel.setValue(new Date());
                    }

                    @Override
                    public void onError(ErrorDTO errorDTO) {
                        if(errorDTO.getErrorCode() == ErrorCode.SERVICE_NOT_AVAILABLE){
                            vp.setVisible(true);
                            statusLabel.setText("Действие недоступно");
                            statusLabel.setStyleName("statusNA");
                            lastCheckedLabel.setValue(new Date());
                        }
                    }
                });
            }
        }));
        initWidget(mainPanel);
    }

}
