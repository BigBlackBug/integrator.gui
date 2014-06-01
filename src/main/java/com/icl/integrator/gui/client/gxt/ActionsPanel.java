package com.icl.integrator.gui.client.gxt;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;

import java.util.List;
/**
 * Created by BigBlackBug on 29.05.2014.
 */
public class ActionsPanel implements Refreshable<List<ActionEndpointDTO<ActionDescriptor>>> {

	private final ListView<ActionEndpointDTO<ActionDescriptor>, String> listView;

	private final Refreshable<ActionEndpointDTO<ActionDescriptor>> actionInfoPanel;

	private final ServicesPanel servicesPanel;

	private ListStore<ActionEndpointDTO<ActionDescriptor>> store;

	private HorizontalLayoutContainer hlc;

	public ActionsPanel(ServicesPanel servicesPanel) {
		this.servicesPanel = servicesPanel;
		store = new ListStore<>(new ModelKeyProvider<ActionEndpointDTO<ActionDescriptor>>() {
			@Override
			public String getKey(ActionEndpointDTO<ActionDescriptor> item) {
				return item.getActionName();
			}
		});
		listView = new ListView<>(
				store,
				new ImmutableValueProvider<ActionEndpointDTO<ActionDescriptor>, String>() {
					@Override
					public String getValue(ActionEndpointDTO<ActionDescriptor> object) {
						return object.getActionName();
					}
				}
		);
		listView.getSelectionModel()
				.addSelectionHandler(new SelectionHandler<ActionEndpointDTO<ActionDescriptor>>() {
					@Override
					public void onSelection(
							SelectionEvent<ActionEndpointDTO<ActionDescriptor>> event) {
						ActionEndpointDTO<ActionDescriptor> action = event.getSelectedItem();
						actionInfoPanel.refresh(action);
					}
				});
		hlc = new HorizontalLayoutContainer();
		actionInfoPanel = new ActionInfoPanel(servicesPanel);

		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		HorizontalLayoutContainer buttons = new HorizontalLayoutContainer();
		buttons.add(new TextButton("ADD"),
		            new HorizontalLayoutData(0.33, -1, new Margins(4, 4, 4, 4)));
		buttons.add(new TextButton("EDIT"),
		            new HorizontalLayoutData(0.34, -1, new Margins(4, 4, 4, 4)));
		buttons.add(new TextButton("REMOVE"),
		            new HorizontalLayoutData(0.33, -1, new Margins(4, 4, 4, 4)));
		vlc.add(buttons, new VerticalLayoutData(1, 40, new Margins(4, 4, 4, 4)));
		vlc.add(listView, new VerticalLayoutData(1, 1, new Margins(4, 4, 0, 4)));
		hlc.add(vlc, new HorizontalLayoutData(0.3, 1, new Margins(4, 4, 0, 4)));
		hlc.add(actionInfoPanel, new HorizontalLayoutData(0.7, 1, new Margins(4, 4, 0, 4)));
	}

	@Override
	public Widget asWidget() {
		return hlc;
	}

	@Override
	public void refresh(List<ActionEndpointDTO<ActionDescriptor>> actions) {
		store.clear();
		store.addAll(actions);
		if(!actions.isEmpty()) {
			listView.getSelectionModel().select(0, false);
		}
	}
}
