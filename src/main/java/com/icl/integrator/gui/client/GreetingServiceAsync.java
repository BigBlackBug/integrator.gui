package com.icl.integrator.gui.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.icl.integrator.dto.*;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.destination.ServiceDestinationDescriptor;
import com.icl.integrator.dto.registration.*;
import com.icl.integrator.gui.client.components.IntegratorAsyncService;

import java.util.List;
import java.util.Map;

/**
 * Created by BigBlackBug on 14.05.2014.
 */
public interface GreetingServiceAsync {

	void login(String username, String password, AsyncCallback<Void> async);

	void logout(AsyncCallback<Void> async);

	void initClient(String host, String deployPath, int port, AsyncCallback<Void> async);

	<T extends DestinationDescriptor>
	void
	deliver(IntegratorPacket<DeliveryDTO, T> delivery,
	        AsyncCallback<ResponseDTO<Map<String, ResponseDTO<String>>>> async);

	<T extends ActionDescriptor, Y extends DestinationDescriptor>
	void registerService(
			IntegratorPacket<TargetRegistrationDTO<T>, Y> registrationDTO,
			AsyncCallback<ResponseDTO<List<ActionRegistrationResultDTO>>> async)
			;

	<T extends DestinationDescriptor> void getServiceList(
			IntegratorPacket<Void, T> packet, AsyncCallback<ResponseDTO<List<ServiceDTO>>> async);

	<T extends DestinationDescriptor, Y extends ActionDescriptor>
	void getSupportedActions(
			IntegratorPacket<String, T> serviceDTO,
			AsyncCallback<ResponseDTO<List<ActionEndpointDTO<Y>>>> async);

	<ADType extends ActionDescriptor,
			DDType extends DestinationDescriptor> void
	getServiceInfo(IntegratorPacket<String, DDType> serviceDTO,
	               AsyncCallback<ResponseDTO<FullServiceDTO<ADType>>> async)
			;

	<T extends DestinationDescriptor, Y extends ActionDescriptor>
	void addAction(IntegratorPacket<AddActionDTO<Y>, T> actionDTO,
	               AsyncCallback<ResponseDTO<Void>> async)
			;

	<T extends DestinationDescriptor>
	void
	getActionsForDelivery(IntegratorPacket<Void, T> packet,
	                      AsyncCallback<ResponseDTO<List<DeliveryActionsDTO>>> async);

	<T extends DestinationDescriptor, Y extends ActionDescriptor>
	void
	getServicesSupportingActionType(IntegratorPacket<ActionMethod, T> packet,
	                                AsyncCallback<ResponseDTO<List<ServiceAndActions<Y>>>> async)
			;

	<T extends DestinationDescriptor>
	void
	isAvailable(IntegratorPacket<ServiceDestinationDescriptor, T> packet,
	            AsyncCallback<ResponseDTO<Boolean>> async);

	/**
	 * Utility class to get the RPC Async interface from client-side code
	 */
	public static final class Util {

		private static IntegratorAsyncService instance;

		private Util() {
		}

		public static final IntegratorAsyncService getInstance() {
			if (instance == null) {
				instance = new IntegratorAsyncService();
			}
			return instance;
		}
	}
}
