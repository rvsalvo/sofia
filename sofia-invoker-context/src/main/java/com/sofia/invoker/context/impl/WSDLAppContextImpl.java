package com.sofia.invoker.context.impl;


import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.sofia.invoker.WSInvoker;
import com.sofia.invoker.context.WSDLAppContext;
import com.sofia.invoker.context.service.WSDLService;
import com.sofia.invoker.exception.WSInvokerException;
import com.sofia.invoker.parser.ParserWSDL;
import com.sofia.invoker.types.Attribute;
import com.sofia.invoker.types.Operation;
import com.sofia.invoker.types.WsdlService;
import com.sofia.invoker.util.WSDLUtils;


/**
 * Context default implementation.
 * Creates a cached version of Parser and Invoker.
 * 
 * @author rsalvo
 *
 */
public class WSDLAppContextImpl implements WSDLAppContext {

    private static final Logger log = Logger.getLogger( WSDLAppContextImpl.class );

    private final ConcurrentHashMap< String, WSDLService > services = new ConcurrentHashMap< String, WSDLService >();

    final protected ScheduledThreadPoolExecutor purgeTimer;

    final private int serviceTtl;

    final private int purgeTime;
    
    final private WSInvoker invoker;

    protected WSDLAppContextImpl( final int serviceTtl, final int purgeTime, final boolean reuseClient ) {

        this.invoker = new WSInvoker( serviceTtl, reuseClient );
        this.serviceTtl = serviceTtl;
        this.purgeTime = purgeTime;
        this.purgeTimer = new ScheduledThreadPoolExecutor( 10 );
        this.purgeTimer.scheduleWithFixedDelay( new Runnable() {

            public void run() {

                log.debug( "Running Service Purge Timer." );
                final int max = 1000;
                int i = 0;
                try {
                    for ( final WSDLService service : services.values() ) {
                        if ( service.isExpired() ) {
                            services.remove( service.getKey() );
                            if ( i++ > max ) {
                                break;
                            }
                        }
                    }
                    purgeTimer.purge();
                }
                catch ( Exception e ) {
                    log.error( "Error while purging Service: ", e );
                }

            }
        }, purgeTime, purgeTime, TimeUnit.SECONDS );

    }
    
    public List< Operation > parse( String wsdlUri ){
        
        log.debug( "parsing wsdlUri: " + wsdlUri );
        
        if ( services.containsKey( wsdlUri ) ){
            
            log.debug( "Don't need to parse. Already parsed" );
            
            WSDLService service = services.get( wsdlUri );
            return service.getOperations();
        }
        
        
        WSDLService service = parseWsdl( wsdlUri );
        
        services.put( wsdlUri, service );
        
        return service.getOperations();
        
    }
    
   public List< WsdlService > getServices( String wsdlUri ){
        
        log.debug( "getServices wsdlUri: " + wsdlUri );
        
        if ( services.containsKey( wsdlUri ) ){
            
            log.debug( "Don't need to parse. Already parsed" );
            
            WSDLService service = services.get( wsdlUri );
            return service.getWsdlServices();
        }
        

        WSDLService service = parseWsdl( wsdlUri );
        
        services.put( wsdlUri, service );
        
        return service.getWsdlServices();
        
    }    
   
   private WSDLService parseWsdl( String wsdlUri ){
       
       ParserWSDL parser = new ParserWSDL( wsdlUri );
       List< Operation > operations = parser.getOperations();
       List< WsdlService > wsdlServices = parser.getWsdlServices();
       WSDLService service = new WSDLService( serviceTtl, wsdlUri, operations, wsdlServices, parser.getDefaultWsdlService() );
       
       return service;
   }
   

   @Override
   public WsdlService getDefaultWsdlService( String wsdlUri ) {

       log.debug( "getDefaultWsdlService wsdlUri: " + wsdlUri );
       
       if ( services.containsKey( wsdlUri ) ){
           
           log.debug( "Don't need to parse. Already parsed" );
           
           WSDLService service = services.get( wsdlUri );
           return service.getDefaultService();
       }
       

       WSDLService service = parseWsdl( wsdlUri );
       
       services.put( wsdlUri, service );
       
       return service.getDefaultService();
   }   
    
    public Operation getOperationByName( String name, String wsdlUri ){
        return getOperationByName( name, wsdlUri, true );
    }
    
    public Operation getOperationByName( String name, String wsdlUri, boolean soap ){
        List< Operation > operations = parse( wsdlUri );
        for ( Operation op : operations ){
            if ( op.getName().equals( name ) ){
                if ( ( soap && op.isSoap() ) || ( !soap && !op.isSoap() ) ){
                    return op;
                }
            }
        }
        
        //if came here, maybe it needs a refresh
        return updateCache( name, wsdlUri );
    }    
    
    private Operation updateCache( String name, String wsdlUri ){
        
        WSDLService service = parseWsdl( wsdlUri );
        services.put( wsdlUri, service );
        List< Operation > operations = service.getOperations();

        for ( Operation op : operations ){
            if ( op.getName().equals( name ) ){
                return op;
            }
        }
        
        return null;
    }
    
    public Attribute invoke( Operation operation ) throws WSInvokerException {
        try {
            Attribute resp = invoker.invoke( operation );
            return resp;
        }
        catch ( Exception ex ){
            //maybe problem with cached version?
            if ( services.contains( operation.getWsdlUri() ) ){
                services.remove( operation.getWsdlUri() );
            }
            throw new WSInvokerException( ex );
        }
        
    }
    
    public String marshall( Attribute attribute ){
        return WSDLUtils.createXmlFromAttribute( attribute );
    }

    public Attribute unmarshall( String xml ){
        return WSDLUtils.createAttributeFromXml( xml );
    }
    
    public void clearContext(){
        services.clear();
    }

    
    public int getPurgeTime() {
    
        return purgeTime;
    }

    
    public WSInvoker getInvoker() {
    
        return invoker;
    }

}
