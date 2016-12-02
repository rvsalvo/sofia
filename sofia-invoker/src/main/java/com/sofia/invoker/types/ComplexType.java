package com.sofia.invoker.types;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author rsalvo
 *
 */
public class ComplexType extends XMLAttribute {
    
    public ComplexType(){
        super();
        this.parent = null;        
    }
    
    public ComplexType( String name, String nameSpace ){
        super( name, nameSpace );
        this.parent = null;
    }

    public ComplexType( ComplexType parent ){
        super();
        this.parent = parent;
    }

    public ComplexType( String name, String nameSpace, ComplexType parent ){
        super( name, nameSpace );
        this.parent = parent;
    }
    
    private static final long serialVersionUID = 1L;

    private List< Attribute > attributes = new ArrayList< Attribute >();
    
    private boolean array;
    
    private boolean wrapped = true;
    
    private final ComplexType parent;

    public List< Attribute > getAttributes() {
        return attributes;
    }

    public void setAttributes(List< Attribute > attributes) {
        this.attributes = attributes;
    }

    public boolean isArray() {
        return array;
    }

    public void setArray( boolean array ) {
        this.array = array;
    }

    
    public ComplexType getParent() {
    
        return parent;
    }

    
    public boolean isWrapped() {
    
        return wrapped;
    }

    
    public void setWrapped( boolean wrapped ) {
    
        this.wrapped = wrapped;
    }
    
}
