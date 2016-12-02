package com.sofia.invoker;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.Dispatch;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.AddressingFeature;
import javax.xml.ws.soap.MTOMFeature;

import org.apache.cxf.BusFactory;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.wsdl.EndpointReferenceUtils;
import org.apache.log4j.Logger;

import com.sofia.invoker.properties.SSLProperties;
import com.sofia.invoker.util.NamingThreadFactory;
import com.sofia.invoker.util.XMLUtil;



/**
 * The Message Dispatcher is used for handling outgoing messages.
 * 
 * @author bhlangonijr
 * @author rsalvo
 * 
 */
public class MessageDispatcher {

    private static final Logger log = Logger.getLogger( MessageDispatcher.class );

    private final ConcurrentHashMap< Integer, ServiceClient > client;

    private static final long DEFAULT_SERVICE_TTL = 4 * 60 * 60 * 1000;

    private static final long REQUEST_COUNTER_THRESHOLD = 50000;

    private long SERVICE_TTL;

    private final ExecutorService threadPool;

    private final AtomicLong requestCounter;

    private final ReentrantLock purgeLock;

    private final boolean jmsTransport;
    
    private final boolean useCache;
    
    private final WebServiceFeature[] enabledRequiredwsf;    
    
    private final Map< String, Object > requestContext;
    
    private boolean trustAllCerts = true;

    public MessageDispatcher( long serviceTtl, boolean jmsTransport, boolean useCache, WebServiceFeature[] enabledRequiredwsf, Map< String, Object > requestContext, boolean trustAllCerts ) {

        this.client = new ConcurrentHashMap< Integer, ServiceClient >();
        this.SERVICE_TTL = serviceTtl;
        this.threadPool = Executors.newSingleThreadExecutor( new NamingThreadFactory( "MessageDispatcher-Purge" ) );
        this.requestCounter = new AtomicLong( 0 );
        this.purgeLock = new ReentrantLock();
        this.jmsTransport = jmsTransport;
        this.useCache = useCache;
        this.enabledRequiredwsf = enabledRequiredwsf;
        this.requestContext = requestContext;
        this.trustAllCerts = trustAllCerts;
        
    }
    
    public MessageDispatcher( long serviceTtl, boolean jmsTransport, boolean useCache, WebServiceFeature[] enabledRequiredwsf ,boolean trustAllCerts ) {

        this( serviceTtl, jmsTransport, useCache, enabledRequiredwsf, new HashMap< String, Object >(),trustAllCerts );
        
    }    
    
    public MessageDispatcher( long serviceTtl, boolean useCache, boolean trustAllCerts ) {

        this( serviceTtl, false, useCache, new WebServiceFeature[] { new AddressingFeature( true, false ), new MTOMFeature( true ) }, trustAllCerts ); 
        
    }    

    public MessageDispatcher() {

        this( DEFAULT_SERVICE_TTL, true, true );
        
    }


    public MessageDispatcher( long serviceTtl ) {

        this( serviceTtl, true, true );
    }


    /**
     * Dispatch the message to a service in the remote endpoint.
     * 
     * The service proxy and dispatcher object created to dispatch the message
     * will be cached until some error occurs or the cache entry is invalidated
     * based on the TTL parameters passed to the constructor of this class.
     * 
     * @param wsdl
     *            URL location of the WSDL
     * @param remoteService
     *            QName of the remote service
     * @param remotePort
     *            QName of the remote port
     * @param remoteAddress
     *            string representation of the remote address
     * @param message
     *            Message to be sent
     */
    public void invokeOneWay( EndpointReferenceType epr, Map< String, Object > requestContext, SOAPMessage message, boolean isJms, WebServiceFeature[] features, SSLProperties sslProperties )
        throws WebServiceException {

        final Dispatch< SOAPMessage > dispatch = prepareToSend( epr, requestContext, isJms, features, sslProperties, trustAllCerts );
        
        dispatch.invokeOneWay( message );

        if ( log.isDebugEnabled() ) {
            log.debug( "Sending message using proxy[" + ( (ServiceClient) dispatch ).getId() + "]: ["+trustAllCerts+"]"
                + XMLUtil.xmlToString( message.getSOAPPart() ) );
        }        
        
    }
    
    public SOAPMessage invoke( EndpointReferenceType epr, 
        Map< String, Object > requestContext, 
        SOAPMessage message, 
        boolean isJms, 
        WebServiceFeature[] features,
        SSLProperties sslProperties) {
    	
        final Dispatch< SOAPMessage > dispatch = prepareToSend( epr, requestContext, isJms, features, sslProperties, trustAllCerts );
        
        if ( log.isDebugEnabled() ) {
            log.debug( "Sending message using proxy[" + ( (ServiceClient) dispatch ).getId() + "]: ["+trustAllCerts+"]"
                + XMLUtil.xmlToString( message.getSOAPPart() ) );
        }        
        
        SOAPMessage response = dispatch.invoke( message );

        if ( log.isDebugEnabled() ) {
            log.debug( "Received response: "
                + XMLUtil.xmlToString( response.getSOAPPart() ) );
        }
        
        return response;
    }


    private Dispatch< SOAPMessage > prepareToSend( EndpointReferenceType epr, 
        Map< String, Object > context, 
        boolean isJms, 
        WebServiceFeature[] features,
        SSLProperties sslProperties, boolean trustAllCerts ) {

        purgeExpiredObjects();
        final Dispatch< SOAPMessage > dispatch = getOrCreateDispatch( epr, isJms, features, sslProperties, trustAllCerts );
        if ( context != null ){
            dispatch.getRequestContext().putAll( context );
        } else if ( requestContext != null ) {
            dispatch.getRequestContext().putAll( requestContext );
        }
        return dispatch;
    }    

    /**
     * Dispatch the message to a service in the remote endpoint.
     * 
     * The service proxy and dispatcher object created to dispatch the message
     * will be cached until some error occurs or the cache entry is invalidated
     * based on the TTL parameters passed to the constructor of this class.
     * 
     * @param epr
     *            EndpointReferenceType
     * @param message
     */
    public void invokeOneWay( EndpointReferenceType epr, SOAPMessage message ) {

        invokeOneWay( epr, null, message, isJmsTransport(), null, null );
    }
    
    public SOAPMessage invoke( EndpointReferenceType epr, SOAPMessage message, boolean isJms ) {

        return invoke( epr, null, message, isJms, null, null );
    }    

    public SOAPMessage invoke( EndpointReferenceType epr, SOAPMessage message ) {

        return invoke( epr, null, message, isJmsTransport(), null, null );
    }    
    

    /*
     * Purge expired proxies and services
     */
    private void purgeExpiredObjects() {

        if ( requestCounter.incrementAndGet() >= REQUEST_COUNTER_THRESHOLD ) {
            if ( purgeLock.tryLock() ) {
                try {
                    threadPool.execute( new Runnable() {

                        @Override
                        public void run() {

                            log.info( "Purging expired proxies..." );
                            for ( ServiceClient c : client.values() ) {
                                if ( c.isExpired( SERVICE_TTL ) ) {
                                    client.remove( c );
                                }
                            }
                            log.info( "Finished purging expired services and proxies objects." );
                        }
                    } );
                    requestCounter.set( 0 );
                }
                finally {
                    purgeLock.unlock();
                }
            }
        }
    }


    /*
     * Get the dispatch object with the specified parameters or create a new one
     * in case it doesn't exist This method also handles concurrent calls to it
     */
    private ServiceClient getOrCreateDispatch( EndpointReferenceType epr, boolean isJms, WebServiceFeature[] features, SSLProperties sslProperties, boolean trustAllCerts ) {

        WebServiceFeature[] featuresToUse = features != null ? features : enabledRequiredwsf;
        
        if ( !useCache ){
            return new ServiceClient( epr, isJms, featuresToUse, sslProperties, trustAllCerts );
        }
        
        final Integer key = getDispatchKey( epr );
        ServiceClient result = client.get( key );
        if ( result == null ) {
            synchronized ( this ) {
                result = client.get( key );
                if ( result == null ) {
                    final ServiceClient n = new ServiceClient( epr, isJmsTransport(), features != null ? features : enabledRequiredwsf, sslProperties, trustAllCerts );
                    result = client.putIfAbsent( key, n );
                    log.info( "Creating a new ServiceClient " + key + " - Id: " + n.getId() );
                }
            }
            if ( result == null ) {
                result = client.get( key );
            }
        }
        return result;
    }


    /*
     * Get the dispatch object key
     */
    private int getDispatchKey( EndpointReferenceType epr ) {

        final QName serviceName = EndpointReferenceUtils.getServiceName( epr, BusFactory.getThreadDefaultBus() );
        return ( serviceName.getLocalPart() + epr.getAddress().getValue() ).hashCode();
    }


    public AtomicLong getRequestCounter() {

        return requestCounter;
    }


    /**
     * @return the jmsTransport
     */
    public boolean isJmsTransport() {

        return jmsTransport;
    }

}
