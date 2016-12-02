package br.com.sofia.parser.util;

import java.util.List;
import java.util.Set;


public class CollectionUtil {
    
    public static <T> T getObjectFromSet( T obj, Set< T > set ){
        
        for ( T element : set ){
            if ( element.equals( obj ) ){
                return element;
            }
        }
        
        return null;
    }
    
    public static <T> T getObjectFromList( T obj, List< T > set ){
        
        for ( T element : set ){
            if ( element.equals( obj ) ){
                return element;
            }
        }
        
        return null;
    }
    
    public static <T> int getObjectIndex( T obj, List< T > set ){
        
        for ( int i = 0; i < set.size(); i++ ){
            T element = set.get( i );
            if ( element.equals( obj ) ){
                return i;
            }
        }
        
        return -1;
    }

}
