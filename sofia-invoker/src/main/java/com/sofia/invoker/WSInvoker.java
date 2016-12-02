package com.sofia.invoker;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.AddressingFeature;
import javax.xml.ws.soap.MTOMFeature;

import org.apache.cxf.common.WSDLConstants;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.JAXWSAConstants;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;

import com.sofia.invoker.exception.ParserException;
import com.sofia.invoker.exception.WSInvokerException;
import com.sofia.invoker.exception.WsdlConfigurationException;
import com.sofia.invoker.types.Attribute;
import com.sofia.invoker.types.ComplexType;
import com.sofia.invoker.types.Operation;
import com.sofia.invoker.util.WSAUtils;
import com.sofia.invoker.util.WSDLUtils;


/**
 * Invokes a WSDL based web-service
 * 
 * @author rsalvo
 * 
 */
public class WSInvoker {

    private static final long DEFAULT_SERVICE_TTL = 4 * 60 * 60 * 1000;

    private static final String WSDL_IDENTIFIER = "?wsdl";

    private static final Logger log = Logger.getLogger( WSInvoker.class );

    private MessageDispatcher dispatcher;

    private boolean reuseClient;


    /**
     * Creates a new instance of WSInvoker class.
     * 
     */
    public WSInvoker() {

        initialize( DEFAULT_SERVICE_TTL, false );
    }


    /**
     * Creates a new instance of WSInvoker class.
     * 
     * @param reuseClient - If the ServiceClient should be reused or not.
     * 
     */
    public WSInvoker( boolean reuseClient ) {

        initialize( DEFAULT_SERVICE_TTL, reuseClient );
    }


    /**
     * Creates a new instance of WSInvoker class.
     * 
     * @param serviceTtl
     *            - The TTL for the cached the ServiceClient.
     * @param reuseClient
     *            - If the ServiceClient should be reused or not.
     */
    public WSInvoker( long serviceTtl, boolean reuseClient ) {

        initialize( serviceTtl, reuseClient );
    }


    protected void initialize( long serviceTtl, boolean reuseClient ) {

        try {

            this.dispatcher = new MessageDispatcher( serviceTtl, false, reuseClient, null, true );

        }
        catch ( Exception e ) {
            throw new WsdlConfigurationException( e );
        }
    }


    /**
     * Invokes an web-service operation.
     * 
     * @param operation
     *            - the Service operation including the argument (Attribute)
     *            full filled.
     * @see Attribute
     * @see Operation
     * 
     * @return the returned Attribute full filled
     */
    public Attribute invoke( Operation operation ) throws WSInvokerException {

        try {

            if ( operation == null ) {
                throw new WSInvokerException( "Operation is null" );
            }

            log.debug( "Invoking operation: " + operation.getName() );

            String wsdlService = null;

            boolean isJms = false;
            boolean hasAddressing = false;

            if ( WSDLConstants.JMS_PREFIX.equals( operation.getTransport() ) ) {

                wsdlService = operation.getEndpointUri();
                isJms = true;

            }
            else if ( operation.getEndpointUri() != null ) {
                wsdlService = operation.getEndpointUri();
            }
            else if ( operation.getWsdlUri() == null
                || operation.getWsdlUri().toLowerCase().indexOf( WSDL_IDENTIFIER ) < 0
                || operation.getWsdlUri().toLowerCase().indexOf( "http" ) < 0 ) {
                throw new ParserException( "Invalid WSDL URI" );
            }
            else {
                wsdlService = operation.getWsdlUri().substring(
                    0,
                    operation.getWsdlUri().toLowerCase().indexOf( WSDL_IDENTIFIER ) );
            }

            final Map< String, Object > requestContext = new HashMap< String, Object >();

            if ( operation.getReplyTo() != null || operation.getRelatesTo() != null ) {

                EndpointReferenceType replyTo = null;
                if ( operation.getReplyTo() != null ) {
                    replyTo = WSAUtils.createEndpointReference( operation.getReplyTo().getAddress(), new QName(
                        operation.getReplyTo().getNameSpace(), operation.getReplyTo().getServiceName() ), new QName(
                        operation.getReplyTo().getNameSpace(), operation.getReplyTo().getServicePort() ) );
                }

                requestContext.put(
                    JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES,
                    WSAUtils.createAddressingMap( operation.getMessageId(), operation.getRelatesTo(), replyTo, null ) );

                hasAddressing = true;
            }

            EndpointReferenceType epr = WSAUtils.createEndpointReference(
                wsdlService,
                new QName( operation.getServiceName() ),
                new QName( operation.getPortName() ) );

            List< WebServiceFeature > features = new ArrayList< WebServiceFeature >();

            if ( hasAddressing ) {
                features.add( new AddressingFeature( true, false ) );
            }
            if ( operation.isEnableMTOM() ) {
                features.add( new MTOMFeature( true ) );
            }
            else {
                features.add( new MTOMFeature( false ) );
            }

            SOAPMessage message = null;

            if ( operation.getArgument() != null ) {
                if ( operation.getArgument() instanceof ComplexType ) {
                    ComplexType ct = (ComplexType) operation.getArgument();
                    if ( ct.isWrapped() ) {
                        message = createMessage( WSDLUtils.createPayLoad(
                            operation.getArgument(),
                            operation.isGenerateAllNamespaces() ) );
                    }
                    else {
                        message = createMessage(
                            null,
                            null,
                            WSDLUtils.createPayLoad( ct.getAttributes(), operation.isGenerateAllNamespaces() ) );
                    }
                }
                else {
                    message = createMessage( WSDLUtils.createPayLoad(
                        operation.getArgument(),
                        operation.isGenerateAllNamespaces() ) );
                }
            }
            else {
                message = createMessage( null );
            }

            if ( operation.getAction() != null ) {
                requestContext.put( BindingProvider.SOAPACTION_USE_PROPERTY, Boolean.TRUE );
                requestContext.put( BindingProvider.SOAPACTION_URI_PROPERTY, operation.getAction() );
            }

            if ( operation.getReturnType() == null ) {

                dispatcher.invokeOneWay(
                    epr,
                    requestContext,
                    message,
                    isJms,
                    features.toArray( new WebServiceFeature[] {} ),
                    operation.getSslProperties() );

                return null;

            }
            else {
                SOAPMessage response = dispatcher.invoke(
                    epr,
                    requestContext,
                    message,
                    false,
                    features.toArray( new WebServiceFeature[] {} ),
                    operation.getSslProperties() );

                return WSDLUtils.createAttribute( (Element) response.getSOAPBody().getFirstChild() );

            }

        }
        catch ( Exception e ) {
            throw new WSInvokerException( e );
        }

    }


    public SOAPMessage createMessage( Element element ) throws SOAPException, JAXBException {

        return createMessage( null, null, element );

    }


    public SOAPMessage createMessage( QName action, Element element ) throws SOAPException, JAXBException {

        return createMessage( null, action, element );

    }


    public SOAPMessage createMessage( MimeHeaders headers, QName action, Element element ) throws SOAPException,
        JAXBException {

        SOAPMessage message = MessageFactory.newInstance().createMessage();

        SOAPPart part = message.getSOAPPart();

        // Obtain the SOAPEnvelope, header and body elements.
        SOAPEnvelope env = part.getEnvelope();

        SOAPBody body = env.getBody();
        SOAPFactory fac = SOAPFactory.newInstance();

        if ( element != null && action != null ) {
            SOAPElement operation = body.addChildElement( action );
            operation.addChildElement( fac.createElement( element ) );
        }
        else if ( element != null ) {
            body.addChildElement( fac.createElement( element ) );
        }

        message.saveChanges();

        return message;

    }


    public SOAPMessage createMessage( MimeHeaders headers, QName action, List< Element > elements )
        throws SOAPException, JAXBException {

        SOAPMessage message = MessageFactory.newInstance().createMessage();
        SOAPBody body = message.getSOAPBody();
        SOAPFactory fac = SOAPFactory.newInstance();

        if ( action != null ) {
            SOAPElement operation = body.addChildElement( action );
            for ( Element element : elements ) {
                operation.addChildElement( fac.createElement( element ) );
            }
        }
        else {
            for ( Element element : elements ) {
                body.addChildElement( fac.createElement( element ) );
            }
        }

        message.saveChanges();

        return message;

    }


    public boolean isReuseClient() {

        return reuseClient;
    }


    public MessageDispatcher getDispatcher() {

        return dispatcher;
    }


    public void setDispatcher( MessageDispatcher dispatcher ) {

        this.dispatcher = dispatcher;
    }

}
