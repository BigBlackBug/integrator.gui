package com.icl.integrator.gui.client.components.descriptions;

import com.google.gwt.user.client.ui.HTML;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.dto.registration.ActionMethod;
import com.icl.integrator.dto.registration.HttpActionDTO;
import com.icl.integrator.gui.client.components.EnumListBox;
import com.icl.integrator.gui.client.components.FixedBorderTextBox;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public class HttpActionInputDescriptionPanel extends AbstractActionInputDescriptionPanel {

    private final FixedBorderTextBox pathTB;

    private final EnumListBox<ActionMethod> actionMethodLB;

    public HttpActionInputDescriptionPanel() {
        super();
        actionMethodLB = new EnumListBox<>(ActionMethod.class);
        pathTB = new FixedBorderTextBox();
        table.setWidget(1, 0, new HTML("<b>Тип действия:</b>"));
        table.setWidget(1, 1, actionMethodLB);
        actionMethodLB.setWidth("200px");
        table.setWidget(2, 0, new HTML("<b>Путь к сервису:</b>"));
        table.setWidget(2, 1, pathTB);
    }

    public HttpActionInputDescriptionPanel(ActionEndpointDTO<HttpActionDTO> actionEndpointDTO){
        this();
        HttpActionDTO actionDescriptor = actionEndpointDTO.getActionDescriptor();
        pathTB.setText(actionDescriptor.getPath());
        actionMethodLB.select(actionDescriptor.getActionMethod());
    }

    @Override
    protected ActionDescriptor createActionDescriptor() {
        return new HttpActionDTO(pathTB.getText(), actionMethodLB.create());
    }

}
