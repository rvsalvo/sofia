package com.sofia.invoker.builder;

import com.sofia.invoker.types.ComplexType;

/**
 * Factory for creating an Attribute.
 * 
 * @author rsalvo
 *
 */
public class AttributeFactory {

    public static AttributeBuilder createComplexType( String name, String nameSpace ){

        return new AttributeBuilder( new ComplexType( name, nameSpace ) );
        
    }
    
}
