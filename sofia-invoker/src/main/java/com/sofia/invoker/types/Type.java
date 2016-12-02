package com.sofia.invoker.types;

import java.math.BigDecimal;
import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 
 * @author rsalvo
 *
 */
public enum Type {
    
    INT( "int",Integer.class ),
    ANY( "any", String.class ),
    INTEGER( "integer",Integer.class ),
    STRING( "string",String.class ),
    LONG( "long",Long.class ),
    ANY_URI( "anyURI",URI.class ),		 	 
    BOOLEAN( "boolean",Boolean.class ),		 	 	 
    BYTE( "byte",Byte.class ),	
    DATE( "date",Date.class ),		 
    DATETIME( "dateTime",Date.class ),	 
    DECIMAL( "decimal",BigDecimal.class ),
    DOUBLE( "double",Double.class ),	 
    DURATION( "duration",Timestamp.class ),	 
    FLOAT( "float",Float.class ),
    SHORT( "short",Short.class ),
    TIME( "time",Timestamp.class ),
    VOID( "", Void.class ),
    LIST( "array", List.class ),
    TOKEN( "token",String.class );
    
    private String type;
    private Class<?> clazz;
    
    Type( String type, Class<?> clazz ){
	this.type = type;
	this.clazz = clazz;
    }
    
    @Override
    public String toString(){
	return type;
    }
    
    public Class<?> getClassType(){
	return clazz;
    }
    
    public static Class<?> getTypeClass( String type ){
	
	if ( type != null && type.indexOf( "Array" ) > 0 ){
	    return List.class;
	}
	
	return getType( type ).getClass();
	
    }
    
    public static Type getType( String type ){
	
	if ( type != null && type.indexOf( "Array" ) > 0 ){
	    return Type.LIST;
	}
	
	for ( Type tp : Type.values() ){
	    if ( tp.toString().equals( type ) ){
		return tp;
	    }
	}
	return null;
    }
    
    public static Type getType( Class<?> type ){
	for ( Type tp : Type.values() ){
	    if ( tp.getClassType() == type ){
		return tp;
	    }
	}
	return null;
    }     
    
}
