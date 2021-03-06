package com.icl.integrator.gui.client.components.display;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.icl.integrator.dto.FullServiceDTO;
import com.icl.integrator.dto.registration.DeliverySettingsDTO;
import com.icl.integrator.dto.source.EndpointDescriptor;
import com.icl.integrator.dto.source.HttpEndpointDescriptorDTO;
import com.icl.integrator.dto.source.JMSEndpointDescriptorDTO;
import com.icl.integrator.dto.util.EndpointType;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: e.shahmaev
 * Date: 18.03.14
 * Time: 10:10
 * To change this template use File | Settings | File Templates.
 */
public class ServiceDescriptionPanel extends Composite {

    public ServiceDescriptionPanel(FullServiceDTO fullServiceDTO) {
        VerticalPanel dialogVPanel = new VerticalPanel();
        String serviceName = fullServiceDTO.getServiceName();
        EndpointDescriptor serviceEndpoint = fullServiceDTO.getEndpoint();
        EndpointType endpointType = serviceEndpoint.getEndpointType();
        dialogVPanel.add(new HTML("<br><b>Название сервиса</b>"));
        dialogVPanel.add(new HTML(serviceName));
        dialogVPanel.add(new HTML("<br><b>Тип сервиса</b>"));
        dialogVPanel.add(new HTML(endpointType.toString()));

        if (endpointType == EndpointType.HTTP) {
            HttpEndpointDescriptorDTO descriptor =
                    (HttpEndpointDescriptorDTO) serviceEndpoint;
            String host = descriptor.getHost();
            int port = descriptor.getPort();
            dialogVPanel.add(new HTML("<br><b>Хост</b>"));
            dialogVPanel.add(new HTML(host));
            dialogVPanel.add(new HTML("<br><b>Порт</b>"));
            dialogVPanel.add(new HTML(port+""));
        } else {
            JMSEndpointDescriptorDTO
                    descriptor = (JMSEndpointDescriptorDTO) serviceEndpoint;
            String connectionFactory = descriptor.getConnectionFactory();
            Map<String,String> jndiProperties = descriptor.getJndiProperties();
            dialogVPanel.add(new HTML("<br><b>ConnectionFactory:</b>"));
            dialogVPanel.add(new HTML(connectionFactory));
            dialogVPanel.add(new HTML("<br><b>Параметры JNDI:</b>"));
            dialogVPanel.add(new HTML(jndiProperties+""));
        }
        dialogVPanel.add(new HTML("<br><center><b><u>Настройки доставки</u></b></center>"));
        dialogVPanel.add(new HTML("<b>Интервал:</b>"));
        DeliverySettingsDTO deliverySettings = fullServiceDTO.getDeliverySettings();
        dialogVPanel.add(new HTML(deliverySettings.getRetryDelay() +""));
        dialogVPanel.add(new HTML("<br><b>Количество повторов:</b>"));
        dialogVPanel.add(new HTML(deliverySettings.getRetryNumber()+""));

        initWidget(dialogVPanel);
    }


}
