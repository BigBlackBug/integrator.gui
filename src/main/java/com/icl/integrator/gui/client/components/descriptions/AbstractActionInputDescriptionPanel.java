package com.icl.integrator.gui.client.components.descriptions;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.gui.client.components.FixedBorderTextBox;
import com.icl.integrator.gui.client.util.Creator;

/**
 * Created by e.shahmaev on 25.03.2014.
 */
public abstract class AbstractActionInputDescriptionPanel extends Composite implements
        Creator<ActionEndpointDTO<ActionDescriptor>> {

    protected final FlexTable table;

    protected final FixedBorderTextBox actionNameTB;

    protected AbstractActionInputDescriptionPanel() {
        table = new FlexTable();
        table.setCellSpacing(3);
        HTML widget = new HTML("<b>Название действия:</b>");
        widget.setWordWrap(false);
        table.setWidget(0, 0, widget);
        actionNameTB = new FixedBorderTextBox();
        table.setWidget(0, 1, actionNameTB);
        initWidget(table);
    }

    protected AbstractActionInputDescriptionPanel(ActionEndpointDTO actionEndpointDTO){
        this();
        actionNameTB.setText(actionEndpointDTO.getActionName());
    }

    @Override
    public ActionEndpointDTO<ActionDescriptor> create() {
        ActionDescriptor actionDescriptor = createActionDescriptor();
        return new ActionEndpointDTO<>(actionNameTB.getText(),actionDescriptor);
    }

    protected abstract ActionDescriptor createActionDescriptor();

}
