package br.com.sofia.regex.util;

import java.util.Collection;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class ArrayUtils {
    
    public static <T> T get( T element, Collection<T> collection ){
        
        for ( T el : collection ){
            if ( el.equals( element ) ){
                return el;
            }
        }
        
        return null;
    }
    
    public void printArray( String text ){
        
        System.out.print( "[ ");
        for ( int i : text.toCharArray() ){
            System.out.print( i + " ");
        }
        System.out.println( "]" );        
    }

}
