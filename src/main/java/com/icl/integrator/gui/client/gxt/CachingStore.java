package com.icl.integrator.gui.client.gxt;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.icl.integrator.dto.FullServiceDTO;
import com.icl.integrator.dto.IntegratorPacket;
import com.icl.integrator.dto.ServiceDTO;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.registration.ActionDescriptor;
import com.icl.integrator.gui.client.GenericCallback;
import com.icl.integrator.gui.client.GreetingServiceAsync;
import com.icl.integrator.gui.client.components.IntegratorAsyncService;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BigBlackBug on 26.05.2014.
 */
public class CachingStore extends ListStore<ServiceDTO> {

	private final IntegratorAsyncService service = GreetingServiceAsync.Util.getInstance();

	private Map<String, FullServiceDTO> cache = new HashMap<>();

	private List<ServiceDTO> data = new ArrayList<>();

	private ListView<ServiceDTO, String> listView;

	public CachingStore() {
		super(new ModelKeyProvider<ServiceDTO>() {
			@Override
			public String getKey(ServiceDTO item) {
				return item.getServiceName();
			}
		});
	}

	public void getFullService(final String serviceName,
	                           final AsyncCallback<FullServiceDTO<ActionDescriptor>> async) {
		final FullServiceDTO fullServiceDTO = cache.get(serviceName);
		if (fullServiceDTO == null) {
			service.getServiceInfo(
					new IntegratorPacket<>(serviceName),
					new GenericCallback<FullServiceDTO<ActionDescriptor>>() {
						@Override
						public void onSuccess(FullServiceDTO<ActionDescriptor> result) {
							async.onSuccess(result);
							cache.put(serviceName, result);
						}

						@Override
						public void onFailure(Throwable caught) {
							async.onFailure(caught);
						}
					}
			);
		} else {
			async.onSuccess(fullServiceDTO);
		}
	}

	public void reload() {
		service.getServiceList(
				new IntegratorPacket<Void, DestinationDescriptor>(),
				new GenericCallback<List<ServiceDTO>>() {
					@Override
					public void onSuccess(List<ServiceDTO> result) {
						data = result;
						addAll(data);
						//TODO good for now
						cache.clear();
						if (!result.isEmpty()) {
							listView.getSelectionModel().select(0, false);
						}
					}
				}
		);
	}

	public ListView<ServiceDTO, String> getListView() {
		return listView;
	}

	public void setListView(ListView<ServiceDTO, String> listView) {
		this.listView = listView;
	}
}
