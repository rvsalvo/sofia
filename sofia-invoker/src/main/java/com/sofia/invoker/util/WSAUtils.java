package com.sofia.invoker.util;


import java.util.Map;
import java.util.Map.Entry;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;

import org.apache.cxf.service.model.MessageInfo;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.apache.cxf.ws.addressing.AttributedURIType;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.apache.cxf.ws.addressing.ObjectFactory;
import org.apache.cxf.ws.addressing.ReferenceParametersType;
import org.apache.cxf.ws.addressing.RelatesToType;
import org.apache.cxf.ws.addressing.impl.AddressingPropertiesImpl;
import org.apache.cxf.ws.addressing.wsdl.ServiceNameType;
import org.apache.cxf.wsdl.WSAEndpointReferenceUtils;
import org.w3c.dom.Node;


/**
 * WS-Addressing utilitary methods
 * 
 * @author bhlangonijr
 * 
 */
public class WSAUtils {

    public static final String WSA_MESSAGE_ID = "MessageID";

    public static final String WSA_REPLY_TO = "ReplyTo";

    private static final ObjectFactory wsaObjectFactory = new ObjectFactory();

    private static final org.apache.cxf.ws.addressing.wsdl.ObjectFactory wsdlObjectFactory = new org.apache.cxf.ws.addressing.wsdl.ObjectFactory();


    /**
     * Create a new endpoint reference with service and port metadata
     * 
     * @param address
     * @param serviceQName
     * @param portQName
     * @return
     */
    public static EndpointReferenceType createEndpointReference( String address, QName serviceQName, QName portQName ) {

        EndpointReferenceType epr = wsaObjectFactory.createEndpointReferenceType();
        epr.setMetadata( wsaObjectFactory.createMetadataType() );
        AttributedURIType addr = wsaObjectFactory.createAttributedURIType();
        addr.setValue( address );
        epr.setAddress( addr );
            if ( portQName != null && serviceQName != null ){
            ServiceNameType serviceName = new ServiceNameType();
            serviceName.setEndpointName( portQName.getLocalPart() );
            serviceName.setValue( serviceQName );
            epr.getMetadata().getAny().add( wsdlObjectFactory.createServiceName( serviceName ) );
        }
        return epr;
    }


    /**
     * Set & Get the messageinfo into the EPR
     * 
     * @param epr
     * @param messageInfo
     * @return
     */
    public static EndpointReferenceType setGetElement( EndpointReferenceType epr, Object element ) {

        ReferenceParametersType ref = wsaObjectFactory.createReferenceParametersType();
        ref.getAny().add( 0, element );
        epr.setReferenceParameters( ref );
        return epr;
    }


    /**
     * Return the replyTo EPR from the SOAPHeader
     * 
     * @param message
     * @param jaxbUtil
     * @return
     * @throws SOAPException
     * @throws JAXBException
     */
    @SuppressWarnings( "unchecked" )
    public static EndpointReferenceType getReplyToEpr( SOAPMessage message, JAXBUtil jaxbUtil ) throws SOAPException,
        JAXBException {

        final Node replyTo = XMLUtil.fetchByName( message.getSOAPHeader(), WSA_REPLY_TO );
        return ( (JAXBElement< EndpointReferenceType >) jaxbUtil.unmarshal( replyTo ) ).getValue();
    }


    /**
     * Retrieves de message ingo from a SOAP message header
     * 
     * @param message
     * @param jaxbUtil
     * @return
     * @throws SOAPException
     * @throws JAXBException
     */
    public static MessageInfo getMessageInfo( SOAPMessage message, JAXBUtil jaxbUtil ) throws SOAPException,
        JAXBException {

        final EndpointReferenceType replyTo = getReplyToEpr( message, jaxbUtil );
        return (MessageInfo) replyTo.getReferenceParameters().getAny().get( 0 );
    }


    /**
     * Creates a literal endpoint based on a class name and a map of properties
     * 
     * @param clazz
     * @param properties
     * @return
     */
    public static String createLiteralEndpoint( String queueName, Map< String, String > properties ) {

        return createLiteralEndpoint( null, queueName, properties );
    }


    /**
     * Creates a literal endpoint based on a class name and a map of properties
     * 
     * @param sufix
     * @param clazz
     * @param properties
     * @return
     */
    public static String createLiteralEndpoint( String sufix, String queueName, Map< String, String > properties ) {

        StringBuilder endpoint = new StringBuilder();
        endpoint.append( "jms:" );
        String destType = "topic";
        if ( properties.containsKey( "destinationStyle" ) ) {
            destType = properties.get( "destinationStyle" );
        }
        endpoint.append( destType );
        endpoint.append( ":" );
        endpoint.append( queueName );
        if ( sufix != null ) {
            endpoint.append( sufix );
        }
        endpoint.append( "." );
        endpoint.append( destType );
        endpoint.append( "?" );

        int count = 0;
        for ( Entry< String, String > property : properties.entrySet() ) {
            if ( count > 0 ) {
                endpoint.append( "&" );
            }
            endpoint.append( property.getKey() );
            endpoint.append( "=" );
            endpoint.append( property.getValue().trim() );
            count++;
        }
        return endpoint.toString();
    }


    /**
     * Create the ws-addressing header
     * 
     * @param messageInfo
     * @param relatesTo
     * @param replyTo
     * @return
     */
    public static AddressingProperties createAddressingMap(
        String messageId,
        String relatesTo,
        EndpointReferenceType replyTo,
        Object element ) {

        AddressingProperties maps = new AddressingPropertiesImpl();
        if ( messageId != null ){
            AttributedURIType msgId = wsaObjectFactory.createAttributedURIType();
            msgId.setValue( messageId );
            maps.setMessageID( msgId );
        }
        if ( relatesTo != null ) {
            RelatesToType rTo = wsaObjectFactory.createRelatesToType();
            rTo.setValue( relatesTo );
            maps.setRelatesTo( rTo );
        }
        if ( replyTo != null ) {
            EndpointReferenceType newReplyTo = WSAEndpointReferenceUtils.duplicate( replyTo );
            if ( element != null ) {
                setGetElement( newReplyTo, element );
            }
            maps.setReplyTo( newReplyTo );
        }
        return maps;
    }

}
