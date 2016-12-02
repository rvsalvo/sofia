package com.sofia.invoker.test;

import org.junit.Test;

import com.sofia.invoker.builder.AttributeBuilder;
import com.sofia.invoker.builder.AttributeFactory;
import com.sofia.invoker.types.Attribute;
import com.sofia.invoker.util.WSDLUtils;


/**
 * 
 * @author rsalvo
 *
 */
public class TestBuilder {
    
    
    @Test
    public void testBuilder(){
        
        Attribute request = AttributeFactory.createComplexType( "createTable", "" )
            .addComplexType( "table", "" )
            .addComplexType( "columns", "" )
            .appendPrimitive( "name", "" )
            .appendPrimitive( "description", "" )
            .getParent()
            .addComplexType( "columns", "11" )
            .appendPrimitive( "name", "22" )
            .appendPrimitive( "description", "33" )
            .getParent()
            .appendPrimitive( "teste", "xxx").createAttribute();
            
        System.out.println( request.toString() );
    }

    
    @Test    
    public void testBuilder2(){
        
        AttributeBuilder builder = AttributeFactory.createComplexType( "execute", "http://namespace/application" )
        .appendPrimitive( "appId", "ks9983-39jdi-d909e-diijdij" )
        .appendPrimitive( "userId", "userID" );
        
        builder.addComplexType( "parameters", "http://namespace/application" )
        .appendPrimitive( "numB", "3499599948" )
        .appendPrimitive( "msg", "essa eh uma mensagem de teste!!!!" )
        .appendPrimitive( "numA", "" );
        
        Attribute attribute = builder.createAttribute();

        System.out.println( "Resut: \n" + WSDLUtils.createXmlFromAttribute( attribute ) );
        
    }
    
    
    @Test    
    public void testBuilder3(){
        
        Attribute attribute = AttributeFactory.createComplexType( "request", "http://namespace/application" )
        .addComplexType( "application", "http://namespace/application" )
        .appendPrimitive( "tags", "sms")
        .appendPrimitive( "description", "dfsdf" )
        .getParent().appendPrimitive( "user", "portal" ).createAttribute();
        
        System.out.println( "XML: " + attribute.toString() );
        
        System.out.println( "Resut: \n" + WSDLUtils.createXmlFromAttribute( attribute ) );
        
    }    

}
