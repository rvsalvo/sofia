package com.sofia.invoker.types;

/**
 * 
 * @author rsalvo
 *
 */
public class Primitive extends XMLAttribute {

    private static final long serialVersionUID = 1L;
    
    private Object value;
    private String type;
    
    public Primitive(){
        
    }
    
    public Primitive( String name, Object value ){
        setName( name );
        setValue( value );
    }

    public Primitive( String name, Object value, String nameSpace ){
        setName( name );
        setValue( value );
        setNameSpace( nameSpace );
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getValue(){
	return value;
    }
    
    public void setValue( Object value ){
	this.value = value;
    }
    
}
