package com.sofia.invoker.context.service;

import java.util.List;

import com.sofia.invoker.types.Operation;
import com.sofia.invoker.types.WsdlService;


/**
 * 
 * @author rsalvo
 *
 */
public class WSDLService {

    private Long timestamp;
    
    private final String key;
    
    private final List< Operation > operations;
    
    private final List< WsdlService > wsdlServices;
    
    private final WsdlService defaultService;

    private final int serviceTtl;

    public WSDLService( int serviceTtl, String key, List< Operation > operations, List< WsdlService > wsdlServices, WsdlService defaultService ) {
        this.serviceTtl = serviceTtl;
        this.operations = operations;
        this.key = key;
        this.timestamp = System.currentTimeMillis();
        this.wsdlServices = wsdlServices;
        this.defaultService = defaultService;
    }

    public boolean isExpired() {
        if ( timestamp == null ) {
            return true;
        }
        return (System.currentTimeMillis()-getTimestamp()) > serviceTtl*1000;
    }    
    
    public Long getTimestamp() {
    
        return timestamp;
    }

    
    public void setTimestamp( Long timestamp ) {
    
        this.timestamp = timestamp;
    }

    
    public int getServiceTtl() {
    
        return serviceTtl;
    }

    
    public List< Operation > getOperations() {
        writeTimestamp();        
        return operations;
    }

    private void writeTimestamp(){
        this.timestamp = System.currentTimeMillis();
    }

    
    public String getKey() {
    
        return key;
    }

    
    public List< WsdlService > getWsdlServices() {
    
        return wsdlServices;
    }

    
    public WsdlService getDefaultService() {
    
        return defaultService;
    }

}
