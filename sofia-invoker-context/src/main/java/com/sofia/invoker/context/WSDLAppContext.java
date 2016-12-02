package com.sofia.invoker.context;

import java.util.List;

import com.sofia.invoker.exception.WSInvokerException;
import com.sofia.invoker.types.Attribute;
import com.sofia.invoker.types.Operation;
import com.sofia.invoker.types.WsdlService;


/**
 * 
 * @author Rodrigo Salvo
 *
 */
public interface WSDLAppContext {

    List< Operation > parse( String wsdlUri );
    
    Operation getOperationByName( String name, String wsdlUri );
    
    Operation getOperationByName( String name, String wsdlUri, boolean soap );
    
    Attribute invoke( Operation operation ) throws WSInvokerException ;
    
    List< WsdlService > getServices( String wsdlUri );  
    
    WsdlService getDefaultWsdlService( String wsdlUri );
    
    String marshall( Attribute attribute );

    Attribute unmarshall( String xml );
    
    void clearContext();
    
}
