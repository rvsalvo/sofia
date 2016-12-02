package com.sofia.invoker.context.impl;

import com.sofia.invoker.context.WSDLAppContext;

/**
 * Context Factory class
 * 
 * @author rsalvo
 *
 */
public class WSDLAppContextFactory {

    public static WSDLAppContext createWSDLContext( int serviceTtl, int purgeTime, boolean reuseClient ){
        return new WSDLAppContextImpl( serviceTtl, purgeTime, reuseClient );
    }
    
    public static WSDLAppContext createDefaultWSDLContext(){
        return new WSDLAppContextImpl( 3600, 1000, true );
    }
    
    public static WSDLAppContext createWSDLContext( boolean reuseClient ){
        return new WSDLAppContextImpl( 3600, 1000, reuseClient );
    }    
    
    public static WSDLAppContext createWSDLContext(){
        return new WSDLAppContextImpl( 3600, 1000, false );
    }     

}
