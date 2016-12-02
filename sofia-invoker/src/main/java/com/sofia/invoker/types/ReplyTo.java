package com.sofia.invoker.types;

/**
 * 
 * @author rsalvo
 *
 */
public class ReplyTo {
    
    private final String address;
    private final String serviceName;
    private final String servicePort;
    private final String nameSpace;
    
    public ReplyTo( String address, String serviceName, String servicePort, String nameSpace ) {

        super();
        this.address = address;
        this.serviceName = serviceName;
        this.servicePort = servicePort;
        this.nameSpace = nameSpace;
    }

    public String getAddress() {
    
        return address;
    }
    
    public String getServiceName() {
    
        return serviceName;
    }
    
    public String getServicePort() {
    
        return servicePort;
    }

    
    public String getNameSpace() {
    
        return nameSpace;
    }
    
}
