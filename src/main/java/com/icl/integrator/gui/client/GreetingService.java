package com.icl.integrator.gui.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.icl.integrator.dto.*;
import com.icl.integrator.dto.destination.DestinationDescriptor;
import com.icl.integrator.dto.registration.*;
import com.icl.integrator.httpclient.IntegratorClientException;

import java.util.List;
import java.util.Map;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {

    String greetServer(String name) throws IllegalArgumentException;

    public void initClient(String host, String deployPath, int port);

    public <T extends DestinationDescriptor>
    ResponseDTO<Map<String, ResponseDTO<String>>>
    deliver(IntegratorPacket<DeliveryDTO, T> delivery) throws IntegratorClientException;

    public <T extends ActionDescriptor, Y extends DestinationDescriptor>
    ResponseDTO<RegistrationResultDTO> registerService(
            IntegratorPacket<TargetRegistrationDTO<T>, Y> registrationDTO)
            throws IntegratorClientException;

    public <T extends DestinationDescriptor> ResponseDTO<List<ServiceDTO>> getServiceList(
            IntegratorPacket<Void, T> packet) throws IntegratorClientException;

    public <T extends DestinationDescriptor, Y extends ActionDescriptor>
    ResponseDTO<List<ActionEndpointDTO<Y>>> getSupportedActions(
            IntegratorPacket<ServiceDTO, T> serviceDTO) throws IntegratorClientException;

    public <ADType extends ActionDescriptor,
            DDType extends DestinationDescriptor> ResponseDTO<FullServiceDTO<ADType>>
    getServiceInfo(IntegratorPacket<ServiceDTO, DDType> serviceDTO)
            throws IntegratorClientException;

    public <T extends DestinationDescriptor, Y extends ActionDescriptor> ResponseDTO<Void> addAction(
            IntegratorPacket<AddActionDTO<Y>, T> actionDTO) throws IntegratorClientException;

    public <T extends DestinationDescriptor>
    ResponseDTO<List<DeliveryActionsDTO>>
    getActionsForDelivery(IntegratorPacket<Void, T> packet) throws IntegratorClientException;

    public <T extends DestinationDescriptor, Y extends ActionDescriptor>
    ResponseDTO<Map<String, ServiceAndActions<Y>>>
    getServicesSupportingActionType(IntegratorPacket<ActionMethod, T> packet)
            throws IntegratorClientException;
}