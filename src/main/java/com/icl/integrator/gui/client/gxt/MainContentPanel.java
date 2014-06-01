package com.icl.integrator.gui.client.gxt;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.icl.integrator.gui.client.DeliveryButtonClickHandler;
import com.icl.integrator.gui.client.GreetingServiceAsync;
import com.icl.integrator.gui.client.components.IntegratorAsyncService;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.*;
import com.sencha.gxt.widget.core.client.event.SelectEvent;


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
		Container a = a();
//		a.setWidth(400);
		a.setHeight(600);
		centerLayoutContainer.add(a);

		viewport.add(centerLayoutContainer);
	}

	//TODO какого-то черта инфо занимает весь экран
	private Container a() {
		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		TextButton delivery = new TextButton("DELIVERY");
		delivery.addSelectHandler(new SelectEvent.SelectHandler() {
			@Override
			public void onSelect(SelectEvent event) {
				//TODO йоу, стити пропали
				new DeliveryButtonClickHandler().onClick(null);
			}
		});
		vlc.add(delivery, new VerticalLayoutContainer.VerticalLayoutData(1, -1));

		HorizontalLayoutContainer panels = new HorizontalLayoutContainer();
		ActionsPanel actionsPanel = new ActionsPanel();
		panels.add(new ServicesPanel(actionsPanel),
		           new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1));
		panels.add(actionsPanel, new HorizontalLayoutContainer.HorizontalLayoutData(0.5, 1));
		vlc.add(panels, new VerticalLayoutContainer.VerticalLayoutData(1, 1));
		return vlc;
	}


	@Override
	public Widget asWidget() {
		return viewport;
	}
}
