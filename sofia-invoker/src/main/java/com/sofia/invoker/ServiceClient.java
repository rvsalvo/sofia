package com.sofia.invoker;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;

import javax.net.ssl.TrustManager;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.EndpointReference;
import javax.xml.ws.Response;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;

import org.apache.cxf.BusFactory;
import org.apache.cxf.binding.soap.SoapTransportFactory;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transport.jms.spec.JMSSpecConstants;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.wsdl.EndpointReferenceUtils;
import org.apache.log4j.Logger;

import com.sofia.invoker.exception.MessageInvokerException;
import com.sofia.invoker.properties.SSLProperties;
import com.sofia.invoker.util.DumbTrustManager;
import com.sofia.invoker.util.SSLUtils;
import com.sofia.invoker.util.XMLUtil;


/**
 * Service client class
 * 
 * @author bhlangonijr
 * @author rsalvo
 * 
 */
public class ServiceClient implements Dispatch< SOAPMessage > {

    private static final Logger log = Logger.getLogger( ServiceClient.class );

    private final String id;

    private final Dispatch< SOAPMessage > dispatch;

    private final EndpointReferenceType epr;

    private final long timestamp;

    private final boolean jmsTransport;

    private final WebServiceFeature[] enabledRequiredwsf;

    private final SSLProperties sslProperties;

    private boolean trustAllCerts = true;


    public ServiceClient(
        EndpointReferenceType epr,
        boolean jmsTransport,
        WebServiceFeature[] enabledRequiredwsf,
        SSLProperties sslProperties,
        boolean trustAllCerts ) {

        this.id = UUID.randomUUID().toString();
        this.epr = epr;
        this.timestamp = System.currentTimeMillis();
        this.enabledRequiredwsf = enabledRequiredwsf;
        this.jmsTransport = jmsTransport;
        this.sslProperties = sslProperties;
        this.dispatch = createDispatcher( getEpr() );
        this.trustAllCerts = trustAllCerts;
    }


    /*
     * Creates a client proxy object
     * 
     * @param endpoint
     * 
     * @param serviceName
     * 
     * @param servicePort
     * 
     * @param service
     * 
     * @return
     */
    private Dispatch< SOAPMessage > createDispatcher( EndpointReferenceType epr ) {

        log.debug( "Creating dispatch object .... " );

        final String address = epr.getAddress().getValue();
        final QName serviceName = EndpointReferenceUtils.getServiceName( epr, BusFactory.getThreadDefaultBus() );
        final QName portName = EndpointReferenceUtils.getPortQName( epr, BusFactory.getThreadDefaultBus() );

        W3CEndpointReferenceBuilder builder = new W3CEndpointReferenceBuilder();
        builder.serviceName( serviceName );
        builder.endpointName( portName );
        builder.address( address );

        Service service = Service.create( serviceName );
        if ( isJmsTransport() ) {
            service.addPort( portName, JMSSpecConstants.SOAP_JMS_SPECIFICATION_TRANSPORTID, address );
        }
        else {
            service.addPort( portName, SoapTransportFactory.TRANSPORT_ID, address );
        }

        Dispatch< SOAPMessage > dispatch = service.createDispatch(
            builder.build(),
            SOAPMessage.class,
            Mode.MESSAGE,
            enabledRequiredwsf != null ? enabledRequiredwsf : new WebServiceFeature[] {} );
        ( (BindingProvider) dispatch ).getRequestContext().put( "thread.local.request.context", "true" );

        if ( trustAllCerts ) {

            Client client = ( (org.apache.cxf.jaxws.DispatchImpl< SOAPMessage >) dispatch ).getClient();

            HTTPConduit conduit = (HTTPConduit) client.getConduit();

            client.getOutInterceptors().add( new MyInterceptor() );

            TLSClientParameters params = conduit.getTlsClientParameters();

            if ( params == null ) {
                params = new TLSClientParameters();
            }

            params.setTrustManagers( new TrustManager[] { new DumbTrustManager() } );

            params.setDisableCNCheck( true );

            conduit.setTlsClientParameters( params );

        }
        if ( sslProperties != null ) {

            Client client = ( (org.apache.cxf.jaxws.DispatchImpl< SOAPMessage >) dispatch ).getClient();

            HTTPConduit httpConduit = (HTTPConduit) client.getConduit();

            try {
                httpConduit.setTlsClientParameters( SSLUtils.createTLSClientParameters(
                    sslProperties.getKeyStore(),
                    sslProperties.getTrustStore(),
                    sslProperties.getPassword(),
                    sslProperties.isDisableCnCheck() ) );
            }
            catch ( FileNotFoundException e ) {
                throw new MessageInvokerException( e );
            }
            catch ( IOException e ) {
                throw new MessageInvokerException( e );
            }
            catch ( GeneralSecurityException e ) {
                throw new MessageInvokerException( e );
            }

        }

        return dispatch;

    }


    @Override
    public Binding getBinding() {

        return dispatch.getBinding();
    }


    @Override
    public EndpointReference getEndpointReference() {

        return dispatch.getEndpointReference();
    }


    @Override
    public < T extends EndpointReference > T getEndpointReference( Class< T > clazz ) {

        return dispatch.getEndpointReference( clazz );
    }


    @Override
    public Map< String, Object > getRequestContext() {

        return dispatch.getRequestContext();
    }


    @Override
    public Map< String, Object > getResponseContext() {

        return dispatch.getResponseContext();
    }


    @Override
    public SOAPMessage invoke( SOAPMessage msg ) {

        return dispatch.invoke( msg );
    }


    @Override
    public Response< SOAPMessage > invokeAsync( SOAPMessage msg ) {

        return dispatch.invokeAsync( msg );
    }


    @Override
    public Future< ? > invokeAsync( SOAPMessage msg, AsyncHandler< SOAPMessage > handler ) {

        return dispatch.invokeAsync( msg, handler );
    }


    @Override
    public void invokeOneWay( SOAPMessage msg ) {

        dispatch.invokeOneWay( msg );

    }


    public long getTimestamp() {

        return timestamp;
    }


    public boolean isExpired( long ttl ) {

        return getTimestamp() + ttl <= System.currentTimeMillis();
    }


    public final EndpointReferenceType getEpr() {

        return epr;
    }


    public String getId() {

        return id;
    }


    public boolean isJmsTransport() {

        return jmsTransport;
    }
    
    class MyInterceptor extends AbstractPhaseInterceptor< Message >{

        public MyInterceptor() {

            super( Phase.SEND );
            // TODO Auto-generated constructor stub
        }

        @Override
        public void handleMessage( Message message ) throws Fault {
            
            SOAPMessage soapMessage = message.getContent( SOAPMessage.class );

            log.debug( "Intercepting send phase: " + XMLUtil.xmlToString( soapMessage.getSOAPPart() ) );
            
        }
        
    }

}
