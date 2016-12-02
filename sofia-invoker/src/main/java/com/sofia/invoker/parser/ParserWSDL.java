package com.sofia.invoker.parser;


import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.xml.namespace.QName;

import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.binding.soap.SoapTransportFactory;
import org.apache.cxf.binding.soap.jms.interceptor.SoapJMSConstants;
import org.apache.cxf.binding.soap.model.SoapBindingInfo;
import org.apache.cxf.common.WSDLConstants;
import org.apache.cxf.common.xmlschema.SchemaCollection;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.EndpointInfo;
import org.apache.cxf.service.model.MessageInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.service.model.OperationInfo;
import org.apache.cxf.service.model.ServiceInfo;
import org.apache.cxf.wsdl.WSDLManager;
import org.apache.cxf.wsdl11.WSDLServiceBuilder;
import org.apache.log4j.Logger;
import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaAll;
import org.apache.ws.commons.schema.XmlSchemaAny;
import org.apache.ws.commons.schema.XmlSchemaComplexType;
import org.apache.ws.commons.schema.XmlSchemaElement;
import org.apache.ws.commons.schema.XmlSchemaParticle;
import org.apache.ws.commons.schema.XmlSchemaSequence;
import org.apache.ws.commons.schema.XmlSchemaSequenceMember;
import org.apache.ws.commons.schema.XmlSchemaSimpleType;
import org.apache.ws.commons.schema.XmlSchemaType;

import com.sofia.invoker.exception.ParserException;
import com.sofia.invoker.exception.WsdlConfigurationException;
import com.sofia.invoker.types.Attribute;
import com.sofia.invoker.types.ComplexType;
import com.sofia.invoker.types.Operation;
import com.sofia.invoker.types.Primitive;
import com.sofia.invoker.types.WSDLVersion;
import com.sofia.invoker.types.WsdlService;
import com.sofia.invoker.util.WsdlConfiguration;



/**
 * Parses a WSDL.
 * 
 * @author rsalvo
 * 
 */
public class ParserWSDL {

    private String wsdlUri;

    private Map< String, ComplexType > elements = new ConcurrentHashMap< String, ComplexType >();

    private static final String WSDL_EXCEPTION = "INVALID_WSDL: Expected element '{http://schemas.xmlsoap.org/wsdl/}definitions";

    private WsdlConfiguration configuration;


    /**
     * Parses the WSDL based on WSDL version. 
     * 
     * @param wsdlUri
     *            - the WSDL URI
     * @param version
     *            - the WSDL version
     * 
     */
    public ParserWSDL( String wsdlUri, WSDLVersion version ) {

        this.wsdlUri = wsdlUri;
        parse( version != null ? version.toString() : null );
    }

    /**
     * Parses the WSDL. 
     * 
     * @param wsdlUri - the WSDL URI.
     * 
     */
    public ParserWSDL( String wsdlUri ) {

        this.wsdlUri = wsdlUri;
        parse( null );
    }

    private static final Logger log = Logger.getLogger( ParserWSDL.class );


    protected void parse( String version ) {

        try {

            if ( configuration == null ) {
                configuration = new WsdlConfiguration();
            }

            if ( version == null ) {
                // TODO: try to discover the WSDL version
            }

            if ( WSDLVersion.V20.toString().equals( version ) ) {
                parseWsdl2();
            }
            else {
                try {
                    parseWsdl11();
                }
                catch ( WSDLException ex ) {
                    // it's not a valid WSDL 1.1 version. Is it a WSDL 2.0 version?
                    // TODO: Find a better way of checking that.
                    if ( ex.getMessage() != null && ex.getMessage().indexOf( WSDL_EXCEPTION ) >= 0 ) {

                        parseWsdl2();

                    }
                    else {
                        throw new WsdlConfigurationException( ex );
                    }
                }
            }

        }
        catch ( Exception e ) {
            throw new ParserException( e );
        }

    }


    protected void parseWsdl11() throws WSDLException {

        // It'll be WSDL 1.1
        Bus bus = BusFactory.getThreadDefaultBus();
        WSDLManager wsdlManager = bus.getExtension( WSDLManager.class );

        Definition def = wsdlManager.getDefinition( wsdlUri );
        WSDLServiceBuilder builder = new WSDLServiceBuilder( bus );
        List< ServiceInfo > services = builder.buildServices( def );

        configuration.setServices( services );

    }


    protected void parseWsdl2() throws WSDLException {

        throw new UnsupportedOperationException( "WSDL 2.0 is not supported yet." );
    }


    /**
     * Gets all operations for this WSDL.
     * 
     * @return the list of available operations in the parsed WSDL.
     * 
     * @see Operation
     * 
     */
    public List< Operation > getOperations() {

        List< Operation > opps = new ArrayList< Operation >();

        if ( configuration == null ) {
            return opps;
        }

        List< ServiceInfo > services = configuration.getServices();

        if ( services.isEmpty() ) {
            throw new WsdlConfigurationException( "No service found or recovered for this WSDL" );
        }

        return getServiceOperations( opps, services );

    }


    /**
     * Get the list of services and port names of a WSDL.
     * 
     * @return
     */
    public List< WsdlService > getWsdlServices() {

        List< WsdlService > opps = new ArrayList< WsdlService >();

        if ( configuration == null ) {
            return opps;
        }

        for ( ServiceInfo s : configuration.getServices() ) {
            WsdlService service = new WsdlService();
            service.setServiceName( s.getName().getLocalPart() );
            service.setEndpointName( getDefaultEndpoint( s ).getName().getLocalPart() );
            opps.add( service );
        }

        return opps;
    }

    /**
     * Gets the first service.
     * 
     * @return
     */
    public WsdlService getDefaultWsdlService() {

        if ( configuration == null ) {
            return null;
        }

        for ( ServiceInfo s : configuration.getServices() ) {

            WsdlService service = new WsdlService();
            service.setServiceName( s.getName().getLocalPart() );
            service.setEndpointName( getDefaultEndpoint( s ).getName().getLocalPart() );

            return service;

        }

        return null;
    }


    /**
     * @return the list of available operations in the parsed WSDL, for the
     *         specified Service and port. If no service and port are found, it
     *         returns an empty list of operations.
     * 
     * @param serviceName
     * @param servicePort
     * 
     * @return the list of operations
     */
    public List< Operation > getOperations( String serviceName, String servicePort ) {

        List< Operation > opps = new ArrayList< Operation >();

        if ( configuration == null ) {
            return opps;
        }

        ServiceInfo service = null;

        for ( ServiceInfo s : configuration.getServices() ) {
            if ( s.getName().equals( serviceName ) && s.getEndpoint( new QName( servicePort ) ) != null ) {
                service = s;
                break;
            }
        }

        if ( service == null ) {
            throw new WsdlConfigurationException( "No service found or recovered for this WSDL" );
        }

        return getServiceOperations( opps, configuration.getServices() );

    }


    public Operation getOperation( String name ) {

        return getOperation( name, true );
    }

    public Operation getOperation( String name, boolean soap ) {

        for ( Operation op : getOperations() ) {
            if ( op.getName().equals( name ) ) {
                if ( ( soap && op.isSoap() ) || ( !soap && !op.isSoap() ) ){
                    return op;
                }
            }
        }
        return null;
    }    

    protected List< Operation > getServiceOperations( List< Operation > opps, List< ServiceInfo > services ) {

        for ( ServiceInfo service : services ) {
            for ( OperationInfo op : service.getInterface().getOperations() ) {

                Operation operation = new Operation();
                operation.setWsdlUri( wsdlUri );

                EndpointInfo endpoint = getDefaultEndpoint( service );

                String portName = endpoint.getName().getLocalPart();
                String serviceName = service.getName().getLocalPart();

                operation.setServiceName( serviceName );
                operation.setPortName( portName );

                operation.setEndpointUri( endpoint.getAddress() );

                if ( SoapJMSConstants.SOAP_JMS_NAMESPACE.equals( endpoint.getTransportId() ) ) {
                    operation.setTransport( WSDLConstants.JMS_PREFIX );
                } else if ( SoapTransportFactory.SOAP_11_HTTP_BINDING.equals( endpoint.getTransportId() ) ){
                    operation.setTransport( WSDLConstants.SOAP11_PREFIX );
                } else if ( SoapTransportFactory.SOAP_12_HTTP_BINDING.equals( endpoint.getTransportId() ) ){
                    operation.setTransport( WSDLConstants.SOAP12_PREFIX );
                }

                operation.setVersion( configuration.getWSDLVersion() );
                operation.setName( op.getName().getLocalPart() );
                operation.setNameSpace( op.getName().getNamespaceURI() );
                log.debug( op.getName().getLocalPart() );

                BindingInfo binding = endpoint.getBinding();

                if ( binding instanceof SoapBindingInfo ) {
                    SoapBindingInfo soapBinding = (SoapBindingInfo) binding;
                    operation.setStyle( soapBinding.getStyle() );
                    operation.setAction( soapBinding.getSoapAction( op ) );
                    operation.setSoap( true );
                    WSDLVersion version = WSDLVersion.getVersion( soapBinding.getSoapVersion().getVersion() );
                    if ( version != null ){
                        operation.setVersion( version );
                    }

                }

                if ( op.hasInput() ) {

                    MessageInfo input = op.getInput();

                    log.debug( "In - " + input.getName() );
                    Attribute att = buildAttribute( input );

                    fillMissingNodes( service, att );

                    operation.setArgument( att );

                }
                if ( op.hasOutput() ) {

                    MessageInfo output = op.getOutput();

                    Attribute att2 = buildAttribute( output );

                    fillMissingNodes( service, att2 );

                    operation.setReturnType( att2 );

                }

                opps.add( operation );

            }
        }

        return opps;
    }


    /**
     * Gets the preferable endpoint, i.e. HTTP.
     * 
     * @param service
     * @return
     */
    protected EndpointInfo getDefaultEndpoint( ServiceInfo service ) {

        EndpointInfo endpoint = null;
        if ( WSDLVersion.V20.equals( configuration.getWSDLVersion() ) ) {
            endpoint = getHTTPEndpoint( service );
        }
        else if ( service.getEndpoints() != null ) {
            endpoint = getHTTPEndpoint( service );
        }
        if ( endpoint == null ) {
            // didn't find anyone
            for ( EndpointInfo info : service.getEndpoints() ) {
                return info;
            }
        }

        return endpoint;
    }


    private EndpointInfo getHTTPEndpoint( ServiceInfo service ) {

        for ( EndpointInfo end : service.getEndpoints() ) {
            if ( end.getAddress() != null && end.getAddress().toLowerCase().indexOf( "http://" ) >= 0 ) {
                return end;
            }
        }
        return null;
    }


    protected Map< String, String > detectJmsInformation( List< ServiceInfo > services ) {

        Map< String, String > properties = new HashMap< String, String >();
        for ( ServiceInfo s : services ) {
            for ( BindingInfo b : s.getBindings() ) {
                log.debug( b );
                
                //TODO: convert from axis version!
                /*
                 * if ( b instanceof SOAPBindingImpl ){ SOAPBindingImpl sb =
                 * (SOAPBindingImpl)b; if ( sb.getTransportURI() != null &&
                 * sb.getTransportURI().indexOf( WSDLConstants.JMS_PREFIX ) >= 0
                 * ){ for ( Object o : s.getPorts().values() ){ Port port =
                 * (Port)o; if ( port.getBinding().equals( b ) ){ for ( Object
                 * ob : port.getExtensibilityElements() ){ if ( ob instanceof
                 * UnknownExtensibilityElement ){ UnknownExtensibilityElement
                 * uel = (UnknownExtensibilityElement)ob; Element el =
                 * uel.getElement(); NamedNodeMap nodes = el.getAttributes();
                 * for ( int i = 0; i< nodes.getLength(); i++ ){ Node node =
                 * nodes.item( i ); properties.put( node.getNodeName(),
                 * node.getNodeValue() ); } }
                 * 
                 * } break; } } } }
                 */
            }
        }
        return properties;
    }


    /**
     * Completes the ComplexTypes that weren't present at the definition
     * directly
     * 
     * @param service
     * @param att
     */
    protected void fillMissingNodes( ServiceInfo service, Attribute att ) {

        if ( att instanceof ComplexType ) {
            ComplexType cp = (ComplexType) att;
            for ( Attribute at : cp.getAttributes() ) {
                if ( ( at instanceof ComplexType ) && missesNode( (ComplexType) at ) ) {
                    fillAttributeElements( service, at );
                }
            }
        }
    }


    protected void fillAttributeElements( ServiceInfo service, Attribute at ) {

        ComplexType element = (ComplexType) at;
        SchemaCollection col = service.getXmlSchemaCollection();
        for ( XmlSchema schema : col.getXmlSchemas() ) {

            for ( Object obj : schema.getSchemaTypes().values() ) {
                if ( obj instanceof XmlSchemaComplexType ) {
                    XmlSchemaComplexType xmlCt = (XmlSchemaComplexType) obj;
                    if ( xmlCt.getName() != null && xmlCt.getName().equals( element.getElementType() ) ) {
                        element.setAttributes( getElementSequence( xmlCt ) );
                    }
                }
            }
        }
    }


    private boolean missesNode( ComplexType element ) {

        return element.getAttributes() == null || element.getAttributes().isEmpty();
    }


    protected String getBaseURI( String currentURI ) throws URISyntaxException, IOException {

        File file = new File( currentURI );
        if ( file.exists() ) {
            return file.getCanonicalFile().getParentFile().toURI().toString();
        }
        String uriFragment = currentURI.substring( 0, currentURI.lastIndexOf( "/" ) );
        return uriFragment + ( uriFragment.endsWith( "/" ) ? "" : "/" );
    }


    protected String[] getStringArray( String obj ) {

        String[] x = new String[ obj.length() ];
        for ( int i = 0; i < obj.length(); i++ ) {
            x[ i ] = String.valueOf( obj.charAt( i ) );
        }
        return x;
    }


    private Attribute buildAttribute( MessageInfo message ) {

        if ( message != null ) {

            ComplexType ct = new ComplexType();
            ct.setName( message.getName().getLocalPart() );
            // ct.setNameSpace( message.getOperation().get )
            List< Attribute > atts = new ArrayList< Attribute >();
            boolean hasElement = false;

            for ( MessagePartInfo info : message.getMessageParts() ) {

                if ( info.isElement() ) {

                    hasElement = true;

                    XmlSchemaElement xmlSchemaElement = (XmlSchemaElement) info.getXmlSchema();

                    Attribute att = getAttribute( xmlSchemaElement );
                    atts.add( att );

                }
                else {

                    Attribute att = getAttribute( info );
                    atts.add( att );
                }

            }

            if ( hasElement ) {

                // Document/literal style
                if ( atts.size() == 1 ){
                    return atts.get( 0 );
                } 
                // Document/literal not wrapped style
                //using CT for convenient purposes only
                ct.setAttributes( atts );
                ct.setWrapped( false );
                return ct;

            }
            else {

                // RPC style
                ct.setAttributes( atts );

                return ct;
            }

        }

        return null;
    }


    /**
     * Gets an attribute based on the XmlSchemaElement.
     * 
     * @param xmlSchemaElement
     * @return
     */
    public Attribute getAttribute( XmlSchemaElement xmlSchemaElement ) {

        if ( xmlSchemaElement != null ) {

            String targetNamespace = xmlSchemaElement.getQName().getNamespaceURI();

            XmlSchemaType schemaType = xmlSchemaElement.getSchemaType();

            if ( schemaType instanceof XmlSchemaComplexType ) {

                XmlSchemaComplexType complexType = ( (XmlSchemaComplexType) schemaType );

                // check if this element was already processed
                String key = targetNamespace + "_"
                    + ( complexType.getName() != null ? complexType.getName() : xmlSchemaElement.getName() );

                String name = xmlSchemaElement.getQName() != null ? xmlSchemaElement.getQName().getLocalPart()
                    : xmlSchemaElement.getName();             
                
                key += name != null ? "_" + name : "";

                ComplexType type = elements.get( key );
                
                if ( type != null ) {
                    return type;
                }

                type = new ComplexType();
                type.setElementType( complexType.getName() );
                type.setNameSpace( targetNamespace );
                long minOccurs = xmlSchemaElement.getMinOccurs();
                long maxOccurs = xmlSchemaElement.getMaxOccurs();
                type.setMinOccurs( minOccurs );
                type.setMaxOccurs( maxOccurs );

                type.setName( name );

                elements.put( key, type );

                List< Attribute > attributes = getElementSequence( complexType );

                type.setAttributes( attributes );
                return type;

            }
            else if ( schemaType instanceof XmlSchemaSimpleType ) {

                XmlSchemaSimpleType simpleType = ( (XmlSchemaSimpleType) schemaType );

                Primitive type = new Primitive();
                type.setType( simpleType.getName() );
                type.setNameSpace( targetNamespace );
                long minOccurs = xmlSchemaElement.getMinOccurs();
                long maxOccurs = xmlSchemaElement.getMaxOccurs();
                type.setMinOccurs( minOccurs );
                type.setMaxOccurs( maxOccurs );
                type.setNillable( xmlSchemaElement.isNillable() );
                String name = xmlSchemaElement.getQName() != null ? xmlSchemaElement.getQName().getLocalPart()
                    : xmlSchemaElement.getName();
                type.setName( name );

                return type;

            }
            else if ( schemaType == null && xmlSchemaElement.getSchemaTypeName() != null
                && xmlSchemaElement.getSchemaTypeName().getLocalPart() != null ) {

                ComplexType type = new ComplexType();
                type.setElementType( xmlSchemaElement.getSchemaTypeName().getLocalPart() );
                type.setNameSpace( targetNamespace );
                long minOccurs = xmlSchemaElement.getMinOccurs();
                long maxOccurs = xmlSchemaElement.getMaxOccurs();
                type.setMinOccurs( minOccurs );
                type.setMaxOccurs( maxOccurs );
                if ( xmlSchemaElement.getSchemaTypeName().getLocalPart().toLowerCase().indexOf( "array" ) > 0 ) {
                    type.setArray( true );
                }
                String name = xmlSchemaElement.getQName() != null ? xmlSchemaElement.getQName().getLocalPart()
                    : xmlSchemaElement.getName();
                type.setName( name );

                return type;
            }
        }

        return null;

    }


    public Attribute getAttribute( MessagePartInfo info ) {

        XmlSchemaType schemaType = (XmlSchemaType) info.getXmlSchema();

        if ( schemaType != null ) {

            if ( schemaType instanceof XmlSchemaComplexType ) {

                XmlSchemaComplexType complexType = ( (XmlSchemaComplexType) schemaType );

                // check if this element was already processed
                String key = complexType.getQName().getNamespaceURI() + "_"
                    + ( complexType.getName() != null ? complexType.getName() : info.getConcreteName().getLocalPart() );

                ComplexType type = elements.get( key );

                if ( type != null ) {
                    return type;
                }

                type = new ComplexType();
                type.setElementType( complexType.getName() );
                type.setNameSpace( complexType.getQName().getNamespaceURI() );
                // long minOccurs = complexType.getQName().get;
                // long maxOccurs = xmlSchemaElement.getMaxOccurs();
                // type.setMinOccurs( minOccurs );
                // type.setMaxOccurs( maxOccurs );
                String name = info.getConcreteName().getLocalPart();
                type.setName( name );

                elements.put( key, type );

                List< Attribute > attributes = getElementSequence( complexType );

                type.setAttributes( attributes );
                return type;

            }
            else if ( schemaType instanceof XmlSchemaSimpleType ) {

                XmlSchemaSimpleType simpleType = ( (XmlSchemaSimpleType) schemaType );

                Primitive type = new Primitive();
                type.setType( simpleType.getName() );
                type.setNameSpace( simpleType.getQName().getNamespaceURI() );
                // long minOccurs = xmlSchemaElement.getMinOccurs();
                // long maxOccurs = xmlSchemaElement.getMaxOccurs();
                // type.setMinOccurs( minOccurs );
                // type.setMaxOccurs( maxOccurs );
                // type.setNillable( xmlSchemaElement.isNillable() );
                type.setName( info.getConcreteName().getLocalPart() );

                return type;

            }
        }

        return null;

    }


    protected List< Attribute > getElementSequence( XmlSchemaComplexType complexType ) {

        List< Attribute > attributes = new ArrayList< Attribute >();

        XmlSchemaParticle particle = complexType.getParticle();
        if ( particle instanceof XmlSchemaSequence || particle instanceof XmlSchemaAll ) {

            XmlSchemaSequence sequence = (XmlSchemaSequence) particle;

            // now we need to know some information from the binding
            // operation.
            for ( XmlSchemaSequenceMember object : sequence.getItems() ) {

                if ( object instanceof XmlSchemaElement ) {
                    XmlSchemaElement innerElement = (XmlSchemaElement) object;

                    Attribute attribute = getAttribute( innerElement );

                    attributes.add( attribute );

                }
                else if ( object instanceof XmlSchemaAny ) {

                    XmlSchemaAny element = (XmlSchemaAny) object;

                    Primitive pr = new Primitive();
                    pr.setType( "any" );
                    pr.setNameSpace( element.getNamespace() );
                    long minOccurs = element.getMinOccurs();
                    long maxOccurs = element.getMaxOccurs();
                    pr.setMinOccurs( minOccurs );
                    pr.setMaxOccurs( maxOccurs );
                    pr.setNillable( minOccurs == 0 );

                    attributes.add( pr );

                }

            }
        }
        return attributes;
    }


    public String getWsdlUri() {

        return wsdlUri;
    }


    public void setWsdlUri( String wsdlUri ) {

        this.wsdlUri = wsdlUri;
    }

}
