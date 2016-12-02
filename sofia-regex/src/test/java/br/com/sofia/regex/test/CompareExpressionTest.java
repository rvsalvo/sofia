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
public class CompareExpressionTest {
    
    @Test
    public void testNegativeLookAhead(){
        
        String regex = "([a-z])+(?!['t])";
        
        String text = "weren't";
        
        RegexPattern pattern = RegexPattern.compile( regex, false );

        RegexMatcher regexMatcher = pattern.matcher( text );
        
        if ( regexMatcher.findExact( 0 ) ){
            System.out.println( "RegexMatcher Matched: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }
        
        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );
        
        if ( matcher.find() ){
            System.out.println( "Matched: start: " + matcher.start() + " end: " + matcher.end() );
        }     
        
/*        RegexAnalyzerImpl analyzer = new RegexAnalyzerImpl();
        RegexExpression expression = analyzer.process( "['t]" );
        
        System.out.println( expression.toString() );  */     
        
    }

}
