package com.icl.integrator.gui.client.gxt;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.icl.integrator.dto.FullServiceDTO;
import com.icl.integrator.dto.ServiceDTO;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.dto.registration.ActionEndpointDTO;
import com.icl.integrator.gui.client.GenericCallback;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.ListViewSelectionModel;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

import java.util.List;

/**
 * Created by BigBlackBug on 26.05.2014.
 */
public class ServicesPanel implements IsWidget {

	private final ListView<ServiceDTO, String> listView;

	private final CachingStore store;

	private final ServiceInfoPanel serviceInfoPanel;

	private HorizontalLayoutContainer hlc;

	private Refreshable<List<ActionEndpointDTO<ActionDescriptor>>> actionDisplay;

	public ServicesPanel(final Refreshable<List<ActionEndpointDTO<ActionDescriptor>>> actionDisplay) {
		this.actionDisplay = actionDisplay;
		store = new CachingStore();
		listView = new ListView<>(
				store,
				new ImmutableValueProvider<ServiceDTO, String>() {
					@Override
					public String getValue(ServiceDTO object) {
						return object.getServiceName();
					}
				}
		);
		store.setListView(listView);
		store.reload();
		listView.getSelectionModel().addSelectionHandler(new SelectionHandler<ServiceDTO>() {
			@Override
			public void onSelection(SelectionEvent<ServiceDTO> event) {
				System.out.println(event.getSelectedItem());
				CachingStore store =
						(CachingStore) ((ListViewSelectionModel) event.getSource())
								.getListView().getStore();

				store.getFullService(
						event.getSelectedItem().getServiceName(),
						new GenericCallback<FullServiceDTO<ActionDescriptor>>() {
							@Override
							public void onSuccess(FullServiceDTO<ActionDescriptor> result) {
								serviceInfoPanel.refresh(result);
								actionDisplay.refresh(result.getActions());
							}
						}
				);
			}
		});
		hlc = new HorizontalLayoutContainer();
		serviceInfoPanel = new ServiceInfoPanel();

		VerticalLayoutContainer vlc = new VerticalLayoutContainer();
		HorizontalLayoutContainer buttons = new HorizontalLayoutContainer();
		buttons.add(new TextButton("ADD"),
		            new HorizontalLayoutData(0.33, -1, new Margins(4, 4, 4, 4)));
		buttons.add(new TextButton("EDIT"),
		            new HorizontalLayoutData(0.34, -1, new Margins(4, 4, 4, 4)));
		buttons.add(new TextButton("REMOVE"),
		            new HorizontalLayoutData(0.33, -1, new Margins(4, 4, 4, 4)));
		vlc.add(buttons,
		        new VerticalLayoutContainer.VerticalLayoutData(1, 40, new Margins(4, 4, 4, 4)));
		vlc.add(listView,
		        new VerticalLayoutContainer.VerticalLayoutData(1, 1, new Margins(4, 4, 0, 4)));
		hlc.add(vlc, new HorizontalLayoutData(0.3, 1, new Margins(4, 4, 0, 4)));
		hlc.add(serviceInfoPanel, new HorizontalLayoutData(0.7, 1, new Margins(4, 4, 0, 4)));
	}

	@Override
	public Widget asWidget() {
		return hlc;
	}
}
