package com.icl.integrator.gui.client.components.creation;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.icl.integrator.dto.registration.DeliverySettingsDTO;
import com.icl.integrator.gui.client.util.CreationException;
import com.icl.integrator.gui.client.util.Creator;
import com.icl.integrator.gui.shared.FieldVerifier;
import com.icl.integrator.gui.shared.GuiException;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public class DeliverySettingsPanel extends Composite implements Creator<DeliverySettingsDTO> {

    private final TextBox retryNumberTB;

    private final TextBox delayTB;

    public DeliverySettingsPanel() {
        FlexTable table = new FlexTable();
        retryNumberTB = new TextBox();
        retryNumberTB.setWidth("100%");
        delayTB = new TextBox();
        delayTB.setWidth("100%");
        table.setWidget(0, 0, new HTML("<b>Количество повторов:</b>"));
        table.setWidget(0, 1, retryNumberTB);
        table.setWidget(1, 0, new HTML("<b>Интервал между повторами(ms):</b>"));
        table.setWidget(1, 1, delayTB);

        initWidget(table);
        setWidth("100%");
    }

    @Override
    public DeliverySettingsDTO create() throws CreationException {
        try {
            String retryNumber = retryNumberTB.getText();
            int retNumber = FieldVerifier.parseNumber(retryNumber, 0);
            String delay = delayTB.getText();
            int delayNumber = FieldVerifier.parseNumber(delay, 100);
            return new DeliverySettingsDTO(retNumber, delayNumber);
        } catch (GuiException gex) {
            throw new CreationException(gex);
        }
    }
}
