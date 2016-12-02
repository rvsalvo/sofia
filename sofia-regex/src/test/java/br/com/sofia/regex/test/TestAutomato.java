package br.com.sofia.regex.test;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import br.com.sofia.regex.matcher.RegexMatcher;
import br.com.sofia.regex.matcher.RegexPattern;



public class TestAutomato {

    @Test
    public void testRegex1() {

        String regex = "([0-9]+-[0-9]+-[0-9]+) ([0-9]+:[0-9]+)";

        String text = "01-01-12 01:19";

        RegexPattern pattern = RegexPattern.compile( regex );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        if ( matcher.find() ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() );
        }

        if ( regexMatcher.find() ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }

    }

    @Test
    public void testRegex2() {

        String regex = "a(?!b)";

        String text = "ac";

        RegexPattern pattern = RegexPattern.compile( regex );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        if ( matcher.find() ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() );
        }

        if ( regexMatcher.find() ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }

    }

    @Test
    public void testRegex3() {

        String regex = "([0-9]){1,2}[\\-\\/]([0-9]){1,2}[\\-\\/]([0-9]){2,4}";

        String text = "10-01-1200";

        RegexPattern pattern = RegexPattern.compile( regex );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        if ( matcher.find() ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() );
        }

        if ( regexMatcher.find() ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }

        if ( regexMatcher.matches() ) {
            System.out.println( "Result matches!" );
        }

    }

    @Test
    public void testRegex4() {

        String regex = "a\\sb";

        String text = "a b";

        RegexPattern pattern = RegexPattern.compile( regex );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        if ( matcher.find() ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() );
        }

        if ( regexMatcher.find() ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }

        if ( regexMatcher.matches() ) {
            System.out.println( "Result matches!" );
        }

    }

    @Test
    public void testRegex5() {

        String regex = "^a\\d+$";

        String text = "a100";

        RegexPattern pattern = RegexPattern.compile( regex );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        if ( matcher.find() ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() );
        }

        if ( regexMatcher.find() ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }

        if ( regexMatcher.matches() ) {
            System.out.println( "Result matches!" );
        }

    }

    @Test
    public void testRegex6() {

        String regex = "\\d+";

        String text = "aaa100*";

        RegexPattern pattern = RegexPattern.compile( regex );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        if ( matcher.find( 4 ) ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() );
        }

        if ( regexMatcher.find( 4 ) ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }

        if ( regexMatcher.matches() ) {
            System.out.println( "Result matches!" );
        }

    }

    @Test
    public void testRegex7() {

        String regex = "\\D+";

        String text = "a*!!!~~ sssss100";

        RegexPattern pattern = RegexPattern.compile( regex );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        if ( matcher.find() ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() );
        }

        if ( regexMatcher.find() ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }

        if ( regexMatcher.matches() ) {
            System.out.println( "Result matches!" );
        }

    }

    @Test
    public void testRegex8() {

        String regex = "\\d{1,4}";

        String text = "aaa100*";

        RegexPattern pattern = RegexPattern.compile( regex );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        if ( matcher.find() ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() );
        }

        if ( regexMatcher.find() ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }

        if ( regexMatcher.matches() ) {
            System.out.println( "Result matches!" );
        }

    }

    @Test
    public void testRegex9() {

        String regex = "b+(?=aa)";

        String text = "ddbbbaa";

        RegexPattern pattern = RegexPattern.compile( regex, true );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        if ( matcher.find() ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() );
        }

        if ( regexMatcher.find() ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }

        if ( regexMatcher.matches() ) {
            System.out.println( "Result matches!" );
        }

    }

    @Test
    public void testRegex10() {

        String regex = "a(a|b+)";

        String text = "abb";

        RegexPattern pattern = RegexPattern.compile( regex );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        if ( matcher.find() ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() );
            System.out.println( "groups: " + matcher.groupCount() + " - " + matcher.group( 1 ) );
        }

        if ( regexMatcher.find() ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }

        if ( regexMatcher.matches() ) {
            System.out.println( "Result matches!" );
        }

    }

    @Test
    public void testRegex11() {

        String regex = "(a+)(ab|ac|aa)";

        String text = "aaa";

        RegexPattern pattern = RegexPattern.compile( regex, true );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        if ( matcher.find() ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() );
            System.out.println( "groups: " + matcher.groupCount() );
            for ( int i = 1; i <= matcher.groupCount(); i++ ) {
                System.out.println( "Group " + i + ": " + matcher.group( i ) );
            }
        }

        if ( regexMatcher.find() ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
            System.out.println( "groups: " + regexMatcher.groupCount() );
            for ( int i = 1; i <= regexMatcher.groupCount(); i++ ) {
                System.out.println( "Group " + i + ": " + regexMatcher.group( i ) );
            }

        }

        if ( regexMatcher.matches() ) {
            System.out.println( "Result matches!" );
        }

    }

}
