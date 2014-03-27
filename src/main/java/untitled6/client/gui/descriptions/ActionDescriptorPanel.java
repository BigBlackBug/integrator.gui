package untitled6.client.gui.descriptions;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.icl.integrator.dto.registration.*;
import com.icl.integrator.dto.util.EndpointType;

/**
 * Created with IntelliJ IDEA.
 * User: e.shahmaev
 * Date: 18.03.14
 * Time: 11:44
 * To change this template use File | Settings | File Templates.
 */
public class ActionDescriptorPanel extends Composite {

    public ActionDescriptorPanel(ActionEndpointDTO actionEndpoint) {
        VerticalPanel dialogVPanel = new VerticalPanel();
        String actionName = actionEndpoint.getActionName();
        ActionDescriptor actionDescriptor = actionEndpoint.getActionDescriptor();
        EndpointType endpointType = actionDescriptor.getEndpointType();
        ActionMethod actionMethod = actionDescriptor.getActionMethod();

        dialogVPanel.add(new HTML("<br><b>Название действия:</b>"));
        dialogVPanel.add(new HTML(actionName));
        dialogVPanel.add(new HTML("<br><b>Тип действия:</b>"));
        dialogVPanel.add(new HTML(actionMethod.toString()));

        if (endpointType == EndpointType.HTTP) {
            HttpActionDTO descriptor =
                    (HttpActionDTO) actionDescriptor;
            String path = descriptor.getPath();
            dialogVPanel.add(new HTML("<br><b>Путь к сервису:</b>"));
            dialogVPanel.add(new HTML(path));
        } else {
            QueueDTO descriptor = (QueueDTO) actionDescriptor;
            String queueName = descriptor.getQueueName();
            String password = descriptor.getPassword();
            String username = descriptor.getUsername();
            dialogVPanel.add(new HTML("<br><b>Название очереди:</b>"));
            dialogVPanel.add(new HTML(queueName));
            if (username != null) {
                dialogVPanel.add(new HTML("<br><b>Имя пользователя:</b>"));
                dialogVPanel.add(new HTML(username));
            }
            if (password != null) {
                dialogVPanel.add(new HTML("<br><b>Пароль:</b>"));
                dialogVPanel.add(new HTML(password));
            }
        }
        initWidget(dialogVPanel);
    }

}
