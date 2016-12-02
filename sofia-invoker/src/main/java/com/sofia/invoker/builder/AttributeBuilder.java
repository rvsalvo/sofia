package com.sofia.invoker.builder;

import com.sofia.invoker.types.Attribute;
import com.sofia.invoker.types.ComplexType;
import com.sofia.invoker.types.Primitive;

/**
 * Allows to construct an entire Attribute for request/response.
 * 
 * @author rsalvo
 *
 */
public class AttributeBuilder {

    private ComplexType attribute;
    private final ComplexType attributeParent;

    public AttributeBuilder( ComplexType attributeParent ){
        this.attribute = attributeParent;
        this.attributeParent = attributeParent;
    }


    /**
     * Creates a Primitive attribute. 
     * 
     * @param name
     * @param value
     * @return the parent ComplexType AttributeRequest reference
     */
    public AttributeBuilder appendPrimitive( String name, Object value ){
        if ( value != null ){
            Primitive pr = new Primitive( name, value );
            attribute.getAttributes().add( pr );
        }
        return this;
    }
    
    /**
     * Creates a Primitive attribute. 
     * 
     * @param name
     * @param value
     * @param nameSpace
     * @return the parent ComplexType AttributeRequest reference
     */
    public AttributeBuilder appendPrimitive( String name, Object value, String nameSpace ){
        if ( value != null ){
            Primitive pr = new Primitive( name, value, nameSpace );
            attribute.getAttributes().add( pr );
        }
        return this;
    }    

    /**
     * Creates a ComplexType attribute. 
     * 
     * @param name
     * @param nameSpace
     * @return the created ComplexType AttributeRequest reference
     */
    public AttributeBuilder addComplexType( String name, String nameSpace ){
        ComplexType type = new ComplexType( name, nameSpace, attribute );
        attribute.getAttributes().add( type ); 
        this.attribute = type;
        return this;
    }
    
    /**
     * Creates a ComplextType attribute.
     * 
     * @param name
     * @param nameSpace
     * @return the parent ComplexType AttributeRequest reference

     */
    public AttributeBuilder appendComplexType( String name, String nameSpace ){
        ComplexType type = new ComplexType( name, nameSpace, attribute );
        attribute.getAttributes().add( type );
        return this;
    }
    
    public Attribute createAttribute(){
        return attributeParent;
    }
    
    /**
     * Creates a ComplextType attribute.
     * 
     * @param type
     * @return the parent ComplexType AttributeRequest reference

     */    
    public AttributeBuilder appendComplexType( ComplexType type ){
        attribute.getAttributes().add( type );
        return this;
    }

    /**
     * Gets the parent attribute.
     * 
     * @return
     */
    public AttributeBuilder getParent() {
    
        this.attribute = attribute.getParent();
        return this;
        
    }
    
}
