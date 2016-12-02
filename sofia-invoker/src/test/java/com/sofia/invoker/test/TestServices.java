package com.sofia.invoker.test;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.sofia.invoker.WSInvoker;
import com.sofia.invoker.builder.AttributeFactory;
import com.sofia.invoker.parser.ParserWSDL;
import com.sofia.invoker.types.Attribute;
import com.sofia.invoker.types.ComplexType;
import com.sofia.invoker.types.Operation;
import com.sofia.invoker.types.Primitive;
import com.sofia.invoker.types.WsdlService;
import com.sofia.invoker.util.AttributeUtils;


/**
 * 
 * @author rsalvo
 *
 */
public class TestServices {

    @Test
    public void testService(){
        
        try {
            
            String wsdlUri = "http://10.51.25.136:9000/orderService?wsdl";
            
            ParserWSDL parser = new ParserWSDL( wsdlUri );
            
            List< WsdlService > services = parser.getWsdlServices();
            
            for ( WsdlService service : services ){
                System.out.println( "- " + service.getServiceName() + " : " + service.getEndpointName() );
            }
            
            for ( Operation operation : parser.getOperations() ){
                System.out.println( operation.getName() );
            }
            
            WSInvoker invoker = new WSInvoker();
            
            Operation op = parser.getOperation( "execute" );
            
            Attribute req = AttributeFactory.createComplexType( "execute", "http://ws.orderapi.algarcrm.algartelecom.com.br/" )
                .appendPrimitive( "orderId", 1010L )
                .createAttribute();
            
            op.setArgument( req );
            
            Attribute resp = invoker.invoke( op );
            
            System.out.println( resp.toString() );
            
        }
        catch ( Exception ex ){
            ex.printStackTrace();
        }
    }
    
    public void testRequest(){
        
        try {
            
            String wsdlUri = "http://www.byjg.com.br/site/webservice.php/ws/cep?WSDL";
            
            ParserWSDL parser = new ParserWSDL( wsdlUri );
            
            Operation op = parser.getOperation( "obterCEP" );
            
            Attribute req = op.getArgument();

            if ( req instanceof ComplexType ){
                ComplexType ct = (ComplexType) req;
                for ( Attribute att : ct.getAttributes() ){
                    if ( att instanceof ComplexType ){
                        //call recursively to get the next nest element
                    } else {
                        Primitive pr = (Primitive) att;
                        
                        System.out.println( "attribute name: " + pr.getName() + " attribute namespace: " + pr.getNameSpace() );
                    }
                }
            }
            
        }
        catch ( Exception ex ){
            ex.printStackTrace();
        }
    } 
    
    @SuppressWarnings( "unchecked" )
    public void testResponse(){
        
        try {
            
            String wsdlUri = "http://www.byjg.com.br/site/webservice.php/ws/cep?WSDL";
            
            ParserWSDL parser = new ParserWSDL( wsdlUri );
            
            List< WsdlService > services = parser.getWsdlServices();
            
            for ( WsdlService service : services ){
                System.out.println( "- " + service.getServiceName() + " : " + service.getEndpointName() );
            }
            
            for ( Operation operation : parser.getOperations() ){
                System.out.println( operation.getName() );
            }
            
            WSInvoker invoker = new WSInvoker();
            
            Operation op = parser.getOperation( "obterCEP" );
            
            Attribute req = AttributeFactory.createComplexType( "obterCEP", null )
                .appendPrimitive( "logradouro", "Afonso Pena" )
                .appendPrimitive( "localidade", "Uberlandia" )
                .appendPrimitive( "UF", "MG" )
                .createAttribute();
            
            op.setArgument( req );
            
            Attribute resp = invoker.invoke( op );
            
            Map< String, Object > map = AttributeUtils.createFullMapFromType( (ComplexType ) resp );
            
            String value = (String)map.get( "primitiveField" );
            
            System.out.println( value );
            
            Map< String, Object > cp = (Map< String, Object >)map.get( "complextField1" ); 
            
            value = (String)cp.get( "otherPrimitiveField" );
            
            System.out.println( value );
            
        }
        catch ( Exception ex ){
            ex.printStackTrace();
        }
    }     
    
}
