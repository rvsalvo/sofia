package com.sofia.invoker.util;

import com.sofia.invoker.WSInvoker;
import com.sofia.invoker.exception.WSInvokerException;


/**
 * Factory for creating WSInvoker versions.
 * 
 * @author rsalvo
 *
 */
public class WSInvokerFactory {
    
    /**
     * Create a WSInvoker instance without configuration context enabled.
     * 
     * @return WSInvoker instance
     * @throws WSInvokerException
     */
    public static WSInvoker createDefaultInvoker() {
        return new WSInvoker();
    }
    
    public static WSInvoker createInvoker( long serviceTtl, boolean reuseClient ) {
        return new WSInvoker( serviceTtl, reuseClient );
    }

    public static WSInvoker createInvokerWithCache() {
        return new WSInvoker( true );
    }    

    public static WSInvoker createInvoker() {
        return new WSInvoker( false );
    }    
    
}
