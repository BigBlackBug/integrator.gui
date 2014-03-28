package com.icl.integrator.gui.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.icl.integrator.dto.*;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.registration.*;
import com.icl.integrator.gui.client.GreetingService;
import com.icl.integrator.gui.shared.FieldVerifier;
import com.icl.integrator.httpclient.IntegratorClientException;
import com.icl.integrator.httpclient.IntegratorHttpClient;

import java.util.List;
import java.util.Map;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
        GreetingService {

    private Client client = new Client();

    @Override
    public <T extends DestinationDescriptor> ResponseDTO<List<ServiceDTO>> getServiceList(
            IntegratorPacket<Void, T> packet) throws IntegratorClientException {
        return client.getInstance().getServiceList(packet);
    }

    @Override
    public <T extends DestinationDescriptor, Y extends ActionDescriptor>
    ResponseDTO<List<ActionEndpointDTO<Y>>> getSupportedActions(
            IntegratorPacket<ServiceDTO, T> serviceDTO) throws IntegratorClientException {
        return client.getInstance().getSupportedActions(serviceDTO);
    }

    @Override
    public <ADType extends ActionDescriptor, DDType extends DestinationDescriptor>
    ResponseDTO<FullServiceDTO<ADType>> getServiceInfo(
            IntegratorPacket<ServiceDTO, DDType> serviceDTO) throws IntegratorClientException {
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
    ResponseDTO<Map<String, ServiceAndActions<Y>>>
    getServicesSupportingActionType(IntegratorPacket<ActionMethod, T> packet)
            throws IntegratorClientException {
        return client.getInstance().getServicesSupportingActionType(packet);
    }

    public String greetServer(String input) throws IllegalArgumentException {
        // Verify that the input is valid.
        if (!FieldVerifier.isValidName(input)) {
            // If the input is not valid, throw an IllegalArgumentException back to
            // the client.
            throw new IllegalArgumentException(
                    "Name must be at least 4 characters long");
        }

        String serverInfo = getServletContext().getServerInfo();
        String userAgent = getThreadLocalRequest().getHeader("User-Agent");

        // Escape data from the client to avoid cross-site script vulnerabilities.
        input = escapeHtml(input);
        userAgent = escapeHtml(userAgent);

        return "Hello, " + input + "!<br><br>I am running " + serverInfo
                + ".<br><br>It looks like you are using:<br>" + userAgent;
    }

    /**
     * Escape an html string. Escaping data received from the client helps to
     * prevent cross-site script vulnerabilities.
     *
     * @param html the html string to escape
     * @return the escaped string
     */
    private String escapeHtml(String html) {
        if (html == null) {
            return null;
        }
        return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;");
    }

    @Override
    public void initClient(String host, String deployPath, int port) {
        client.init(host, port, deployPath);
    }

    @Override
    public <T extends DestinationDescriptor>
    ResponseDTO<Map<String, ResponseDTO<String>>>
    deliver(IntegratorPacket<DeliveryDTO, T> delivery) throws IntegratorClientException {
        return client.getInstance().deliver(delivery);
    }

    @Override
    public <T extends ActionDescriptor, Y extends DestinationDescriptor>
    ResponseDTO<RegistrationResultDTO> registerService(
            IntegratorPacket<TargetRegistrationDTO<T>, Y> registrationDTO)
            throws IntegratorClientException {
        return client.getInstance().registerService(registrationDTO);
    }

    private static class Client {

        private volatile IntegratorHttpClient instance = null;

        private Client() {
        }

        public synchronized IntegratorHttpClient getInstance() {
            return instance;
        }

        public synchronized void init(String host, int port, String deployPath) {
            instance = new IntegratorHttpClient(host, deployPath, port);
        }

    }

}
