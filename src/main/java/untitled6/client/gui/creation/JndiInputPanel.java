package untitled6.client.gui.creation;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

/**
 * Created by e.shahmaev on 19.03.14.
 */
public class JndiInputPanel extends Composite implements Creator<JndiItemPair> {

    private final TextBox propValueTB;

    private final TextBox propNameTB;

    public JndiInputPanel(ClickHandler buttonHandler) {
        FlexTable table = new FlexTable();
        this.propNameTB = new TextBox();
        this.propValueTB = new TextBox();
        table.setWidget(0, 0, new HTML("<b>Ключ:</b>"));
        table.setWidget(0, 1, propNameTB);
        table.setWidget(1, 0, new HTML("<b>Значение:</b>"));
        table.setWidget(1, 1, propValueTB);
        table.setWidget(0, 2, new Button("Убрать", buttonHandler));
        table.getFlexCellFormatter().setRowSpan(0, 2, 2);
        initWidget(table);
    }

    @Override
    public JndiItemPair create() {
        String propName= propNameTB.getText();
        String propValue = propValueTB.getText();
        return new JndiItemPair(propName,propValue);
    }
}
