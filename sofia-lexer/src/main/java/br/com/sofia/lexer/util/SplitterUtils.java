package br.com.sofia.lexer.util;

import java.util.ArrayList;
import java.util.List;


public class SplitterUtils {

    public static String[] splitWithoutBrackets( char splitter, CharSequence expression ){
        
        List< String > parts = new ArrayList< String >();
        
        int counter = 0;
        
        int start = 0;
        int end = 0;
        
        for ( int i = 0; i < expression.length(); i++ ){
            char c = expression.charAt( i );
            if ( c == splitter && counter == 0 ){
                if ( end > start ){
                    parts.add( expression.subSequence( start, end ).toString() );
                    start = i+1;
                    end = i;
                }
            } else if ( c == '(' ){
                counter++;
            } else if ( c == ')' ){
                counter--;
            }
            end++;
        }
        
        if ( end > start ){
            parts.add( expression.subSequence( start, end ).toString() );
        }
        
        return parts.toArray( new String[]{} );
    }
    
}
