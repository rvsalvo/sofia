/**
 * 
 */
package br.com.sofia.regex.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import br.com.sofia.regex.matcher.RegexMatcher;
import br.com.sofia.regex.matcher.RegexPattern;


/**
 * @author Rodrigo Salvo
 *
 */
public class CompareNfaExpressionTest {
    
    @Test
    public void testNegativeLookAhead(){
        
        String regex = "([a-z]+(?!'t))";
        
        String text = "weren'tt";
        
        RegexPattern pattern = RegexPattern.compile( regex, true );

        RegexMatcher regexMatcher = pattern.matcher( text );
        
        if ( regexMatcher.findExact( 0 ) ){
            System.out.println( "RegexMatcher Matched: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }
        
        System.out.println( "group: " + regexMatcher.group( 1 ) );
        
        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );
        
        if ( matcher.find() ){
            System.out.println( "Matched: start: " + matcher.start() + " end: " + matcher.end() );
        } 

        System.out.println( "group: " + matcher.group( 1 ) );        
        
    }

}
