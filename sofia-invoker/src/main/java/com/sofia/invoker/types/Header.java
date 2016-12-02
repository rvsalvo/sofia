package com.sofia.invoker.types;

/**
 * 
 * @author rsalvo
 *
 */
public class Header {
    
    private String name;
    private String value;
    private String namespace;
    
    public String getName() {
    
        return name;
    }
    
    public void setName( String name ) {
    
        this.name = name;
    }
    
    public String getValue() {
    
        return value;
    }
    
    public void setValue( String value ) {
    
        this.value = value;
    }
    
    public String getNamespace() {
    
        return namespace;
    }
    
    public void setNamespace( String namespace ) {
    
        this.namespace = namespace;
    }

}
