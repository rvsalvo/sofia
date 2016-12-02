package com.sofia.invoker.types;

/**
 * 
 * @author rsalvo
 *
 */
public enum WSDLVersion {
    
    V11(1.1),
    V12(1.2),
    V20(2.0);

    private double type;
    
    WSDLVersion( double type ){
	this.type = type;
    }
    
    @Override
    public String toString(){
	return String.valueOf( type );
    }
    
    public static WSDLVersion getVersion( double version ){
	try {
	    for ( WSDLVersion wsdlVersion : WSDLVersion.values() ){
		if ( wsdlVersion.type == version ){
		    return wsdlVersion;
		}
	    }
	    return null;
	} catch ( Exception ex ){
	    return null;
	}
    }
    
}
