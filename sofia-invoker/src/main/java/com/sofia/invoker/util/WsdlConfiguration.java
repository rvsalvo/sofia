package com.sofia.invoker.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.cxf.service.model.ServiceInfo;

import com.sofia.invoker.types.WSDLVersion;


/**
 * 
 * @author rsalvo
 *
 */
public class WsdlConfiguration {
    
    List< ServiceInfo > services = new ArrayList< ServiceInfo >();
    
    private WSDLVersion WSDLVersion;
    
    public List< ServiceInfo > getServices() {
    
        return services;
    }

    
    public void setServices( List< ServiceInfo > services ) {
    
        this.services = services;
    }


    
    public WSDLVersion getWSDLVersion() {
    
        return WSDLVersion;
    }


    
    public void setWSDLVersion( WSDLVersion wSDLVersion ) {
    
        WSDLVersion = wSDLVersion;
    }
    
}
