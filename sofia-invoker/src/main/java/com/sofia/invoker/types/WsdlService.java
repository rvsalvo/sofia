package com.sofia.invoker.types;

/**
 * 
 * @author rsalvo
 *
 */
public class WsdlService {
    
    private String serviceName;
    private String endpointName;
    
    public String getServiceName() {
    
        return serviceName;
    }
    
    public void setServiceName( String serviceName ) {
    
        this.serviceName = serviceName;
    }
    
    public String getEndpointName() {
    
        return endpointName;
    }
    
    public void setEndpointName( String endpointName ) {
    
        this.endpointName = endpointName;
    }

}
