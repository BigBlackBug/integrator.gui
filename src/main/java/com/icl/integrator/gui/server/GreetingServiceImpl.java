package com.icl.integrator.gui.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.icl.integrator.dto.*;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.destination.ServiceDestinationDescriptor;
import com.icl.integrator.dto.registration.*;
import com.icl.integrator.gui.client.GreetingService;
import com.icl.integrator.httpclient.IntegratorClientSettings;
import com.icl.integrator.httpclient.IntegratorHttpClient;
import com.icl.integrator.httpclient.exceptions.IntegratorClientException;

import java.util.List;
import java.util.Map;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements GreetingService {

    private Client client = new Client();

    @Override
    public <T extends DestinationDescriptor> ResponseDTO<List<ServiceDTO>> getServiceList(
            IntegratorPacket<Void, T> packet) throws IntegratorClientException {
        return client.getInstance().getServiceList(packet);
    }

    @Override
    public <T extends DestinationDescriptor, Y extends ActionDescriptor>
    ResponseDTO<List<ActionEndpointDTO<Y>>> getSupportedActions(
            IntegratorPacket<String, T> serviceDTO) throws IntegratorClientException {
        return client.getInstance().getSupportedActions(serviceDTO);
    }

    @Override
    public <ADType extends ActionDescriptor, DDType extends DestinationDescriptor>
    ResponseDTO<FullServiceDTO<ADType>> getServiceInfo(
            IntegratorPacket<String, DDType> serviceDTO) throws IntegratorClientException {
        return client.getInstance().getServiceInfo(serviceDTO);
    }

    @Override
    public <T extends DestinationDescriptor, Y extends ActionDescriptor> ResponseDTO<Void> addAction(
            IntegratorPacket<AddActionDTO<Y>, T> actionDTO) throws IntegratorClientException {
        return client.getInstance().addAction(actionDTO);
    }

    @Override
    public <T extends DestinationDescriptor> ResponseDTO<List<DeliveryActionsDTO>> getActionsForDelivery(
            IntegratorPacket<Void, T> packet) throws IntegratorClientException {
        return client.getInstance().getActionsForDelivery(packet);
    }


    @Override
    public <T extends DestinationDescriptor, Y extends ActionDescriptor>
    ResponseDTO<List<ServiceAndActions<Y>>>
    getServicesSupportingActionType(IntegratorPacket<ActionMethod, T> packet)
            throws IntegratorClientException {
        return client.getInstance().getServicesSupportingActionType(packet);
    }

    @Override
    public <T extends DestinationDescriptor> ResponseDTO<Boolean> isAvailable(
            IntegratorPacket<ServiceDestinationDescriptor, T> packet)
            throws IntegratorClientException {
        return client.getInstance().isAvailable(packet);
    }

	@Override
	public void login(String username, String password) throws IntegratorClientException {
		client.getInstance().login(username,password);
	}

	@Override
	public void logout() throws IntegratorClientException {
		client.getInstance().logout();
	}

	@Override
    public void initClient(String host, String deployPath, int port)  throws IntegratorClientException {
        client.init(host, port, deployPath);
		client.getInstance().ping();
    }

    @Override
    public <T extends DestinationDescriptor>
    ResponseDTO<Map<String, ResponseDTO<String>>>
    deliver(IntegratorPacket<DeliveryDTO, T> delivery) throws IntegratorClientException {
        return client.getInstance().deliver(delivery);
    }

    @Override
    public <T extends ActionDescriptor, Y extends DestinationDescriptor>
    ResponseDTO<List<ActionRegistrationResultDTO>> registerService(
            IntegratorPacket<TargetRegistrationDTO<T>, Y> registrationDTO)
            throws IntegratorClientException {
        return client.getInstance().registerService(registrationDTO);
    }

    private static class Client {

        public static final int DEFAULT_TIMEOUT = 7500;

        private volatile IntegratorHttpClient instance = null;

        private Client() {
        }

        public synchronized IntegratorHttpClient getInstance() {
            return instance;
        }

        public synchronized void init(String host, int port, String deployPath) {
            instance = new IntegratorHttpClient(host, deployPath, port,
                                                new IntegratorClientSettings(
                                                        DEFAULT_TIMEOUT, DEFAULT_TIMEOUT)
            );
        }

    }

}
