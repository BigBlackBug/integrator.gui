package com.icl.integrator.gui.client.gxt;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.icl.integrator.gui.client.GreetingServiceAsync;
import com.icl.integrator.gui.client.components.IntegratorAsyncService;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Padding;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.CenterLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.Viewport;


/**
 * Created by BigBlackBug on 26.05.2014.
 */
public class MainContentPanel implements IsWidget {
	private final IntegratorAsyncService service = GreetingServiceAsync.Util.getInstance();
	private final Viewport viewport;

	public MainContentPanel() {
		this.viewport = new Viewport();
		CenterLayoutContainer centerLayoutContainer = new CenterLayoutContainer();

		centerLayoutContainer.setBorders(true);
//		ContentPanel panel = new ContentPanel();
//		panel.setHeadingText("CenterLayout");
//		panel.add(new Label("I should be centered"));
		VBoxLayoutContainer a = a();
		a.setWidth(600);
		a.setHeight(600);
		centerLayoutContainer.add(a);

		viewport.add(centerLayoutContainer);
	}

	private VBoxLayoutContainer a() {
		VBoxLayoutContainer c = new VBoxLayoutContainer();
		c.setPadding(new Padding(5));
		c.setVBoxLayoutAlign(VBoxLayoutContainer.VBoxLayoutAlign.STRETCH);

		BoxLayoutData flex = new BoxLayoutData(new Margins(0, 0, 5, 0));
		flex.setFlex(0);
		c.add(new TextButton("Достааавка"), flex);
		BoxLayoutData flex2 = new BoxLayoutData(new Margins(0));
		flex2.setFlex(1);

		ActionsPanel actionsPanel = new ActionsPanel();
		c.add(new com.icl.integrator.gui.client.gxt.ServicesPanel(actionsPanel), flex2);
		return c;
	}
//	private VBoxLayoutContainer a() {
//		VBoxLayoutContainer c = new VBoxLayoutContainer();
//		c.setPadding(new Padding(5));
//		c.setVBoxLayoutAlign(VBoxLayoutContainer.VBoxLayoutAlign.STRETCH);
//
//		BoxLayoutData flex = new BoxLayoutData(new Margins(0, 0, 5, 0));
////		flex.setFlex(1);
//		c.add(new TextButton("Достааавка"), flex);
//
//		BoxLayoutData flex2 = new BoxLayoutData(new Margins(0));
////		flex2.setFlex(1);
//		ActionsPanel actionsPanel = new ActionsPanel();
//		ServicesPanel servicesPanel = new ServicesPanel(actionsPanel);
//		HorizontalLayoutContainer buttons = new HorizontalLayoutContainer();
//		buttons.add(servicesPanel,
//		            new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1, new Margins(4, 4, 4, 4)));
//		c.add(servicesPanel,flex2);
////		buttons.add(actionsPanel,
////		            new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1, new Margins(4, 4, 4, 4)));
//
////		c.add(buttons,flex2);
//		return c;
//	}


	@Override
	public Widget asWidget() {
		return viewport;
	}
}
