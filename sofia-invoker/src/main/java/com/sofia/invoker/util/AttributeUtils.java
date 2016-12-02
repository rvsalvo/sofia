package com.sofia.invoker.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sofia.invoker.types.Attribute;
import com.sofia.invoker.types.ComplexType;
import com.sofia.invoker.types.Primitive;


/**
 * Utils for dealing with Attribute type classes.
 * 
 * @author rsalvo
 *
 */
public final class AttributeUtils {
    
    private AttributeUtils() {
    }
    
    /**
     * Gets all primitives directly inside this type and wrap to a map.
     * 
     * @param type
     * @return
     */
    public static Map< String, Object > createMapFromType( ComplexType type ){
        
        Map< String, Object > map = new HashMap< String, Object >();
        
        for ( Attribute att : type.getAttributes() ){
            if ( att instanceof Primitive ){
                Primitive pr = (Primitive) att;
                map.put( pr.getName(), pr.getValue() );
            }
        }
        
        return map;
        
    }
    
    /**
     * Gets the full map object. 
     * If it has some repeated element. It will be put into a List array of type: <code>List< Map<String,Object> ></code>.
     * 
     * @param type - The ComplexType Element.
     * @return
     */
    @SuppressWarnings( "unchecked" )
    public static Map< String, Object > createFullMapFromType( ComplexType type ){
        
        Map< String, Object > map = new HashMap< String, Object >();
        
        for ( Attribute att : type.getAttributes() ){
            if ( att instanceof Primitive ){
                Primitive pr = (Primitive) att;
                map.put( pr.getName(), pr.getValue() );
            } else if ( att instanceof ComplexType ){
                ComplexType ct = (ComplexType)att;
                Object obj = map.get( ct.getName() );
                if ( obj != null ){
                    if ( obj instanceof List ){
                        List< Map< String, Object > > list = (List< Map< String, Object > >)obj; 
                        list.add( createFullMapFromType( ct ) );
                    } else {
                        List< Map< String, Object > > list = new ArrayList< Map<String,Object> >();
                        list.add( (Map< String, Object >)obj );
                        list.add( createFullMapFromType( ct ) );                        
                        map.put( ct.getName(), list );
                    }
                } else {
                    map.put( ct.getName(), createFullMapFromType( ct ) );
                }
            }
        }
        
        return map;
        
    }        
    
    /**
     * Gets the first ComplexType inside this type.
     * 
     * @param type
     * @return
     */
    public static ComplexType getComplexType( ComplexType type ){
        
        for ( Attribute att : type.getAttributes() ){
            if ( att instanceof ComplexType ){
                return (ComplexType)att;
            }
        }

        return null;
    }


    /**
     * Gets a ComplexType (by name) inside this type
     *  
     * @param type
     * @param name
     * @return
     */
    public static ComplexType getComplexType( final ComplexType type, final String name ) {

        for ( Attribute att : type.getAttributes() ) {
            if ( ( att instanceof ComplexType ) && att.getName().equals( name ) ) {
                return (ComplexType) att;
            }
        }

        return null;
    }


    /**
     * Gets an {@link Attribute} list from a ComplexType (by name)
     * 
     * @param type
     * @param name
     * @return
     */
    public static List< Attribute > getAttributesFromComplexType( final ComplexType type, final String name ) {

        List< Attribute > attributes = new ArrayList< Attribute >();

        for ( Attribute attribute : type.getAttributes() ) {

            if ( attribute.getName().equals( name ) ) {
                
                attributes.add( attribute );
            }
        }

        return attributes;
    }


    /**
     * Creates a map with all the primitives from the first Complex Type inside
     * this type.
     * 
     * @param type
     * @return
     */
    public static Map< String, Object > createMapFromInnerType( ComplexType type ){
        
        Map< String, Object > map = new HashMap< String, Object >();
        
        for ( Attribute att : type.getAttributes() ){
            if ( att instanceof ComplexType ){
                return createMapFromType( (ComplexType)att );
            }
        }
        
        return map;
        
    }


    /**
     * Gets {@link Primitive} object that contains some attribute value present into attributes list
     * 
     * @param attributes
     * @param attributeValue
     * @return
     */
    public static Primitive getPrimitive( final List< Attribute > attributes, final String attributeValue ) {

        for ( Attribute att : attributes ) {
            if ( att instanceof Primitive ) {
                Primitive pr = (Primitive) att;
                pr.getValue().equals( attributeValue );
                return pr;
            }
        }
        return null;
    }
    
    public static Object getValue( final List< Attribute > attributes, final String attributeName ) {

        for ( Attribute att : attributes ) {
            if ( att instanceof Primitive ) {
                Primitive pr = (Primitive) att;
                pr.getName().equals( attributeName );
                return pr.getValue();
            }
        }
        return null;
    }    

}
