package com.sofia.invoker.types;

import java.util.HashMap;
import java.util.Map;

import com.sofia.invoker.properties.SSLProperties;



/**
 * Represents an WSDL Operation
 * 
 * @author rsalvo
 *
 */
public class Operation {
    
    private Attribute argument;
    
    private String name;
    
    private Attribute returnType;
    
    private String nameSpace;
    
    private WSDLVersion version;
    
    private String wsdlUri;
    
    private String endpointUri;
    
    private String transport;
    
    private String style;
    
    private String styleUse;
    
    private boolean generateAllNamespaces;
    
    private boolean enableMTOM = false;
    
    private ReplyTo replyTo;
    
    private String relatesTo;
    
    private boolean rest;
    
    private boolean soap;
    
    private String messageId;
    
    private String serviceName;
    
    private String portName;
    
    private String action;
    
    private String verb;
    
    private SSLProperties sslProperties;
    
    public boolean isRest() {
    
        return rest;
    }


    
    public void setRest( boolean rest ) {
    
        this.rest = rest;
    }


    public String getStyle() {
    
        return style;
    }

    
    public void setStyle( String style ) {
    
        this.style = style;
    }


    private Map< String, String > properties;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Attribute getReturnType() {
        return returnType;
    }

    public void setReturnType(Attribute returnType) {
        this.returnType = returnType;
    }
    
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ( ( argument == null ) ? 0 : argument.hashCode() );
	result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
	result = prime * result
		+ ( ( returnType == null ) ? 0 : returnType.hashCode() );
	return result;
    }

    @Override
    public boolean equals( Object obj ) {
	if ( this == obj )
	    return true;
	if ( obj == null )
	    return false;
	if ( getClass() != obj.getClass() )
	    return false;
	Operation other = ( Operation ) obj;
	if ( argument == null ) {
	    if ( other.argument != null )
		return false;
	} else if ( !argument.equals( other.argument ) )
	    return false;
	if ( name == null ) {
	    if ( other.name != null )
		return false;
	} else if ( !name.equals( other.name ) )
	    return false;
	if ( returnType == null ) {
	    if ( other.returnType != null )
		return false;
	} else if ( !returnType.equals( other.returnType ) )
	    return false;
	return true;
    }

    public Attribute getArgument() {
        return argument;
    }

    public void setArgument( Attribute argument ) {
        this.argument = argument;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace( String nameSpace ) {
        this.nameSpace = nameSpace;
    }

    public WSDLVersion getVersion() {
        return version;
    }

    public void setVersion( WSDLVersion version ) {
        this.version = version;
    }

    public String getWsdlUri() {
        return wsdlUri;
    }

    public void setWsdlUri( String wsdlUri ) {
        this.wsdlUri = wsdlUri;
    }

    
    public String getEndpointUri() {
    
        return endpointUri;
    }

    
    public void setEndpointUri( String endpointUri ) {
    
        this.endpointUri = endpointUri;
    }

    
    public Map< String, String > getProperties() {
    
        if ( properties == null ){
            properties = new HashMap< String, String >();
        }
        
        return properties;
    }

    
    public void setProperties( Map< String, String > properties ) {
    
        this.properties = properties;
    }

    
    public String getTransport() {
    
        return transport;
    }

    
    public void setTransport( String transport ) {
    
        this.transport = transport;
    }


    
    public boolean isGenerateAllNamespaces() {
    
        return generateAllNamespaces;
    }


    
    public void setGenerateAllNamespaces( boolean generateAllNamespaces ) {
    
        this.generateAllNamespaces = generateAllNamespaces;
    }


    
    public ReplyTo getReplyTo() {
    
        return replyTo;
    }


    
    public void setReplyTo( ReplyTo replyTo ) {
    
        this.replyTo = replyTo;
    }


    
    public String getRelatesTo() {
    
        return relatesTo;
    }


    
    public void setRelatesTo( String relatesTo ) {
    
        this.relatesTo = relatesTo;
    }


    
    public boolean isEnableMTOM() {
    
        return enableMTOM;
    }


    
    public void setEnableMTOM( boolean enableMTOM ) {
    
        this.enableMTOM = enableMTOM;
    }



    
    public String getMessageId() {
    
        return messageId;
    }



    
    public void setMessageId( String messageId ) {
    
        this.messageId = messageId;
    }



    
    public String getServiceName() {
    
        return serviceName;
    }



    
    public void setServiceName( String serviceName ) {
    
        this.serviceName = serviceName;
    }



    
    public String getPortName() {
    
        return portName;
    }



    
    public void setPortName( String portName ) {
    
        this.portName = portName;
    }



    
    public String getStyleUse() {
    
        return styleUse;
    }



    
    public void setStyleUse( String styleUse ) {
    
        this.styleUse = styleUse;
    }



    
    public String getAction() {
    
        return action;
    }



    
    public void setAction( String action ) {
    
        this.action = action;
    }



    
    public boolean isSoap() {
    
        return soap;
    }



    
    public void setSoap( boolean soap ) {
    
        this.soap = soap;
    }



    
    public String getVerb() {
    
        return verb;
    }



    
    public void setVerb( String verb ) {
    
        this.verb = verb;
    }



    
    public SSLProperties getSslProperties() {
    
        return sslProperties;
    }



    
    public void setSslProperties( SSLProperties sslProperties ) {
    
        this.sslProperties = sslProperties;
    }

}
