package com.sofia.invoker.util;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;


import com.sofia.invoker.exception.ParserException;
import com.sofia.invoker.types.Attribute;
import com.sofia.invoker.types.ComplexType;
import com.sofia.invoker.types.Primitive;
import com.sofia.invoker.types.XMLAttribute;
import com.sun.istack.ByteArrayDataSource;
import com.sun.xml.bind.v2.runtime.unmarshaller.Base64Data;

/**
 * Utils for converting from Attribute types to XML/Element and the opposite.
 * 
 * @author rsalvo
 *
 */
public class WSDLUtils {
    
    public static final String JNDI_DESTINATION_NAME = "jndiDestinationName";
    
    public static boolean hasOMSibling( Node node ){
	return ( node.getNextSibling() != null );
    }
    
    public static Attribute createAttributeFromXml( String xml ) {

        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            InputSource is = new InputSource( new ByteArrayInputStream( xml.getBytes() ) );
            
            Document document = builder.parse( is );
            Element documentElement =  document.getDocumentElement();

	    return createAttribute( documentElement );

	} catch ( Exception e ) {
	    throw new ParserException( e );
	}
    }
    
    /**
     * Creates the attribute based on the xml payload.
     * 
     * @param payload
     * @return the full filled attribute
     */
    public static Attribute createAttribute( Element payload ){
	
	if ( payload == null ){
	    return null;
	}
	
	if (!( payload instanceof Text ) && ( payload.getFirstChild() != null && !(payload.getFirstChild() instanceof Text ) ) ){
	    
	    ComplexType attribute = new ComplexType();
	    attribute.setName( payload.getLocalName() != null ? payload.getLocalName() : payload.getNodeName() );
	    attribute.setNameSpace( payload.getNamespaceURI() );
	    List< Attribute > atts = getAttributes( payload.getFirstChild() );
	    attribute.setAttributes( atts );
	    return attribute;
	    
	} else {
	    Primitive attribute = new Primitive();
	    attribute.setName( payload.getLocalName() != null ? payload.getLocalName() : payload.getNodeName() );
	    attribute.setValue( payload.getTextContent() );
            attribute.setNameSpace( payload.getNamespaceURI() );	    
	    attribute.setPrefix( payload.getPrefix() );
	    return attribute;
	}
	
    }

   private static Property createProperty( Element next ){

        if ( next == null ){
            return null;
        }
        
        if ( next instanceof Element ){
            Element el = (Element)next;
            Attr att = el.getAttributeNode( "name" );
            Node nde = el.getFirstChild();
            if ( nde instanceof Text ){
                Text text = (Text)nde;
                Property property = new Property();
                property.setKey( att.getNodeValue() );
                property.setValue( text.getTextContent() );
                return property;
            }
        }        
        return null; 
    }    
    
    /**
     * Gets the attributes array of this xml payload
     * 
     * @param payload
     * @return the attribute list
     */
    public static List< Attribute > getAttributes( Node payload ){
	
	List< Attribute > attributes = new ArrayList< Attribute >();
	
	if (!( payload instanceof Text ) && ( payload.getFirstChild() != null && !(payload.getFirstChild() instanceof Text ) ) ){
	    
	    ComplexType attribute = new ComplexType();
	    attribute.setName( payload.getLocalName() != null ? payload.getLocalName() : payload.getNodeName() );
            attribute.setNameSpace( payload.getNamespaceURI() );	    
	    List< Attribute > atts = getAttributes( payload.getFirstChild() );
	    attribute.setAttributes( atts );
	    attributes.add( attribute );
	    
	    fillSiblings( payload, attributes );
	    
	} else {
	    Primitive attribute = new Primitive();
	    attribute.setName( payload.getLocalName() != null ? payload.getLocalName() : payload.getNodeName() );
	    attribute.setValue( payload.getTextContent() );
            attribute.setPrefix( payload.getPrefix() );	    
            attribute.setNameSpace( payload.getNamespaceURI() );            
	    attributes.add( attribute );
	    
	    fillSiblings( payload, attributes );
	}
	return attributes;
    }

    protected static void fillSiblings( Node payload, List< Attribute > attributes ) {
	Node node = payload.getNextSibling();
	if ( node == null ){
	    return;
	}
	do {
	    if ( node instanceof Element ){
		Attribute at = createAttribute( (Element)node );
		attributes.add( at );
	    }
	} while ( (node = node.getNextSibling() ) != null );
    }
    
    public static Map< String, String > fillSiblings( Text firstNode ) {
        Map< String, String > properties = new HashMap< String, String >();
        if ( firstNode == null ){
            return properties;
        }
        Node node = firstNode.getNextSibling();
        if ( node == null ){
            return properties;
        }
        do {
            if ( node instanceof Element ){
                Property property = createProperty( (Element)node );
                if ( property != null ){
                    properties.put( property.getKey(), property.getValue() );
                }
            }
        } while ( (node = node.getNextSibling() ) != null );
        return properties;
    }    
    
    public static Element createElementFromXml( String xml ) {

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            
            DocumentBuilder builder = factory.newDocumentBuilder();
            
            InputSource is = new InputSource( new ByteArrayInputStream( xml.getBytes() ) );
            
            Document document = builder.parse( is );
            return document.getDocumentElement();

        } catch ( Exception e ) {
            throw new ParserException( e );
        }
    }    
    
    public static String createXmlFromElement( Element node ) {

        if ( node == null ){
            return "";
        }
        
        try {
            Source source = new DOMSource( node );
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult( stringWriter );
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform( source, result );
            return stringWriter.getBuffer().toString();
        }
        catch ( TransformerConfigurationException e ) {
            throw new ParserException( e );
        }
        catch ( TransformerException e ) {
            throw new ParserException( e );
        }
        
    }    
    
    public static String createXmlFromAttribute( Attribute attribute ) {

	try {
	    Element documentElement = createPayLoad( attribute );

	    return createXmlFromElement( documentElement );

	} catch ( Exception e ) {
	    throw new ParserException( e );
	}
    }
    
    /**
     * Creates the xml Payload for the specified attribute.
     * 
     * @param attribute
     * @return
     */
    public static Element createPayLoad( Attribute attribute ) {
        
        try { 
        
            XMLAttribute att = ( XMLAttribute ) attribute;
    
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();
            
            Element method = null;
            
            if ( att.getNameSpace() == null ){
                method = (Element) document.createElement( att.getName() );    
            } else {
                method = (Element) document.createElementNS( att.getNameSpace(), att.getName() );
                method.setPrefix( att.getPrefix() != null ? att.getPrefix() : "com0" );
            }
            
            int index = 1;
            
            getElements( att, method, att.getNameSpace(), index, document );

            return method;
        
        } catch ( Exception e ) {
            throw new ParserException( e );
        }        
            
    }

    public static Element createPayLoad( Attribute attribute, boolean createAllNamespaces ) {

        try {
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            return createPayLoad( attribute, createAllNamespaces, document );
            
        } catch ( Exception e ) {
            throw new ParserException( e );
        }          

    }
    
    public static Element createPayLoad( Attribute attribute, boolean createAllNamespaces, Document document ) {
        
        if ( attribute == null ){
            return null;
        }

        XMLAttribute att = ( XMLAttribute ) attribute;
        
        if ( createAllNamespaces && att.getNameSpace() == null ){
            throw new ParserException( "Attribute namespace is null" );
        }
        
        try {
            
            Element method = (Element) document.createElementNS( att.getNameSpace(), att.getName() );
            if ( att.getNameSpace() != null ){
                method.setPrefix( att.getPrefix() != null ? att.getPrefix() : "com0" );
            }
            
            int index = 1;        
            getElements( att, method, att.getNameSpace(), index, createAllNamespaces, document );
    
            return method;
        
        } catch ( Exception e ) {
            throw new ParserException( e );
        }        
    }    
    
    public static List< Element > createPayLoad( List< Attribute > attributes, boolean createAllNamespaces ) {

        try {
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();            

            List< Element > elements = new ArrayList< Element >();
            
            for ( Attribute attribute : attributes ){
                elements.add( createPayLoad( attribute, createAllNamespaces, document ) );
            }
            
            return elements;
        
        } catch ( Exception e ) {
            throw new ParserException( e );
        }        
    }    
    
    public static void getElements( Attribute att, Element element,
        String parentNamespace, int index,  Document document ){
        getElements( att, element, parentNamespace, index, false, document );
    }
    
    /**
     * 
     * 
     * @param att
     * @param element
     * @param parentNamespace
     * @param index
     */
    public static void getElements( Attribute attribute, Element element,
	    String parentNamespace, int index, boolean createAllNamespaces, Document document ) {

        XMLAttribute att = ( XMLAttribute ) attribute;        
        
        Element method = null;
        
        if ( att.getNameSpace() == null ){
            method = (Element) document.createElement( att.getName() );
        } else {
            method = (Element) document.createElementNS( att.getNameSpace(), att.getName() );
            method.setPrefix( att.getPrefix() != null ? att.getPrefix() : "com0" );
        }
	
	if ( att instanceof ComplexType ) {
	    ComplexType cp = ( ComplexType ) att;
	    for ( Attribute attr : cp.getAttributes() ) {
		if ( attr instanceof ComplexType ) {
		    ComplexType cpChild = ( ComplexType ) attr;
		    Element node = null;
		    int localIndex = index;
		    if ( parentNamespace != null && !parentNamespace.equals( cpChild.getNameSpace() ) && cpChild.getNameSpace() != null ) {
			node = document.createElementNS( cpChild.getNameSpace(), cpChild.getName() );
			node.setPrefix( cpChild.getPrefix() != null ? cpChild.getPrefix() : "com" + index );
		    } else if ( createAllNamespaces ){
                        node = document.createElementNS( cpChild.getNameSpace(), cpChild.getName() );
                        node.setPrefix( cpChild.getPrefix() != null ? cpChild.getPrefix() : "com" + ( --localIndex ) );
		    } else {
                        node = document.createElement( cpChild.getName() );
                    }
		   
		    getElements( attr, node, cpChild.getNameSpace(), ++localIndex, createAllNamespaces, document );
		    
		    element.appendChild( node );
		    
		} else if ( attr instanceof Primitive ) {
		    Primitive pr = ( Primitive ) attr;
		    
                    if ( parentNamespace != null && pr.getNameSpace() != null && !parentNamespace.equals( pr.getNameSpace() ) ) {
                        element.appendChild( buildPrimitiveElementWithNamespace( pr, index, pr.getNameSpace(), document, null ) );
                    } else if ( parentNamespace != null && pr.getNameSpace() != null && parentNamespace.equals( pr.getNameSpace() ) ) {
                        element.appendChild( buildPrimitiveElementWithNamespace( pr, index, pr.getNameSpace(), document, element.getPrefix() ) );
                    } else if ( createAllNamespaces ){
                        int idx = index-1;
                        element.appendChild( buildPrimitiveElementWithNamespace( pr, idx, parentNamespace, document, null ) );
                    } else {
                        element.appendChild( buildPrimitiveElement( pr, document ) );
                    }		    
		    
		}

	    }
	} else if ( att instanceof Primitive ) {
	    Primitive pr = ( Primitive ) att;
            if ( parentNamespace != null && pr.getNameSpace() != null && !parentNamespace.equals( pr.getNameSpace() ) ) {
                element.appendChild( buildPrimitiveElementWithNamespace( pr, index, pr.getNameSpace(), document, pr.getPrefix() ) );
            } else if ( parentNamespace != null && pr.getNameSpace() != null && !parentNamespace.equals( pr.getNameSpace() ) ) {
                element.appendChild( buildPrimitiveElementWithNamespace( pr, index, pr.getNameSpace(), document, element.getPrefix() ) );
            } else if ( createAllNamespaces ){
                int idx = index-1;
                element.appendChild( buildPrimitiveElementWithNamespace( pr, idx, parentNamespace, document, pr.getPrefix() ) );
            } else {
                element.appendChild( buildPrimitiveElement( pr, document ) );
            }               
	}
    }

    protected static Element buildPrimitiveElementWithNamespace( Primitive primitive, int index, String namespace, Document document, String prefix ) {
        
        if ( primitive.getValue() != null && primitive.getValue() instanceof byte[] ){
            return buildPrimitiveByteElement( primitive, index, namespace, document, prefix );
        }
        
	Element el = null;
	if ( namespace != null ){
        	el = document.createElementNS( namespace, primitive.getName() );
        	el.setPrefix( prefix != null ? prefix : "com" + index );
	} else {
	    el = document.createElement( primitive.getName() );
	}
	el.setTextContent( primitive.getValue() != null ? primitive.getValue().toString() : null );

	return el;
    }

    
    protected static Element buildPrimitiveElement( Primitive primitive, Document document ) {
        
        if ( primitive.getValue() != null && primitive.getValue() instanceof byte[] ){
            return buildPrimitiveByteElement( primitive, 0, null, document, null );
        }
        
        Element el = document.createElement( primitive.getName() );
        el.setTextContent( primitive.getValue() != null ? primitive.getValue().toString() : null );
        return el;
    }   
    
    
/*    protected static Element buildPrimitiveByteElement( Primitive primitive, int index, String namespace, Document document ) {

        Element el = document.createElementNS( namespace ,primitive.getName() );
        el.setPrefix( "com" + index );

        DataSource rawData= new  ByteArrayDataSource( (byte[]) primitive.getValue(), null );
        
        // Creating the Data Handler for the file.  Any implementation of
        // javax.activation.DataSource interface can fit here.
        DataHandler dataHandler = new DataHandler( rawData );
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            dataHandler.writeTo( os );
        }
        catch ( IOException e ) {
            throw new ParserException( e );
        }
        
        //create an OMText node with the above DataHandler and set optimized to true
        Text textData = document.createTextNode( os.toString() );

        el.appendChild( textData );

        return el;
    }   */ 
    
    
    protected static Element buildPrimitiveByteElement( Primitive primitive, int index, String namespace, Document document, String prefix ) {

        Element el = null ;
        
        if ( namespace != null ){
            el = document.createElementNS( namespace, primitive.getName() );
            el.setPrefix( prefix != null ? prefix : "com" + index );
        } else {
            el = document.createElement( primitive.getName() );            
        }

        DataSource rawData = new  ByteArrayDataSource( (byte[]) primitive.getValue(), null );
        
        // Creating the Data Handler for the file.  Any implementation of
        // javax.activation.DataSource interface can fit here.
        DataHandler dataHandler = new DataHandler( rawData );
        
        Base64Data bd = new Base64Data();
        bd.set( dataHandler );
        
        int vlen = bd.length();
        char[] buf = new char[vlen+1];
        bd.writeTo(buf,0);
        
        //create an Text node with the above DataHandler
        Text textData = document.createTextNode( new String(buf,0,vlen) );

        el.appendChild( textData );

        return el;
    }     
    
}
