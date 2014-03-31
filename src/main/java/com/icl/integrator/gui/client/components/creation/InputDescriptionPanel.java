package com.icl.integrator.gui.client.components.creation;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.icl.integrator.dto.source.EndpointDescriptor;
import com.icl.integrator.gui.client.util.Creator;

/**
 * Created by e.shahmaev on 31.03.2014.
 */
public abstract class InputDescriptionPanel extends Composite
        implements Creator<EndpointDescriptor> {

    public abstract FlexTable getTable();
}
