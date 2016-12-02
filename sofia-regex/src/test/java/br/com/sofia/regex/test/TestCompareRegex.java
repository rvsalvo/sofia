package br.com.sofia.regex.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import br.com.sofia.regex.matcher.RegexMatcher;
import br.com.sofia.regex.matcher.RegexPattern;



public class TestCompareRegex {
    
    @Test
    public void test(){
        
      /*  testRegexExpression( "a|b|c|d", "c" );
        
        testRegexExpression( "((a|b)(?=b)|d)", "bab" );
        
        testRegexExpression( "a(a|b+)", "abb" );
        
        testRegexExpression( "([0-9]+-[0-9]+-[0-9]+) ([0-9]+:[0-9]+)", "01-01-12 01:19" );
        
        testRegexExpression( "a(?!b)", "ac" );
        
        testRegexExpression( "([0-9]){1,2}[\\-\\/]([0-9]){1,2}[\\-\\/]([0-9]){2,4}", "10-01-1200" );        
        
        testRegexExpression( "a\\sb", "a b" );        
        
        testRegexExpression( "^a\\d+$", "a100" );
        
        testRegexExpression( "\\d+", "aaa100*" );
        
        testRegexExpression( "\\D+", "a*!!!~~ sssss100" );        
        
        testRegexExpression( "b+(?=aa)", "ddbbbaa" );
        
        testRegexExpression( "(a+)(ab|ac|aa)", "aaa" );*/
        
        //testRegexExpression( "\\{(\\d+):(([^}](?!-} ))*)", "{1: tdjdksjdksjdks dksjkdsjkdsjdkj iuwiqsisjqijsqijsqijsjq -}" );
        
        //testRegexExpression( "\\{(1+):(([^}](?!-} ))*)", "{1: q -}" );
        
        //testRegexExpression( "([0-9]){1,4}(\\\\?\\/|/)([0-9]){1,4}", "23/14" );
        
        testRegexExpression( "([a-zA-Z]+(?!['t])+)", "weren't" );        
        
    }

    private void testRegexExpression( String regex, String text ) {

        RegexPattern pattern = RegexPattern.compile( regex, false );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );
        
        checkResult( regex, text, regexMatcher, matcher );
    }

    private void checkResult( String regex, String text, RegexMatcher regexMatcher, Matcher matcher ) {

        String result = "", result2 = "";
        
        int start = 0;
        int end = 0;

        if ( matcher.find() ) {
            result = "Result Matches: start: " + matcher.start() + " end: " + matcher.end();
            start = matcher.start();
            end = matcher.end();
        }


        if ( regexMatcher.find() ) {
            result2 = "Result Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end();
        }

        if ( result.equals( result2 ) ){
            System.out.println( "Expression [ " + regex + " ] ok with text: '" + text + "' - result: " + result );
            System.out.println( "Sequence: " + text.substring( start, end ) );
        } else {
            System.err.println( "Expression [ " + regex + " ] NOT OK with text: '" + text + "' - java result: " + result + " and sofia result: " + result2 );
        }
        
        System.out.println( "" );
    }
    
}
