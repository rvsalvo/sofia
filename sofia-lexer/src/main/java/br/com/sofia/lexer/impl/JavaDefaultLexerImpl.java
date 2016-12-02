package br.com.sofia.lexer.impl;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;

import br.com.sofia.lexer.Lexer;
import br.com.sofia.lexer.model.JavaLexerRule;
import br.com.sofia.lexer.model.LexerFile;
import br.com.sofia.lexer.model.RuleCommand;
import br.com.sofia.lexer.model.Token;
import br.com.sofia.lexer.util.SplitterUtils;


/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class JavaDefaultLexerImpl implements Lexer {

    // private static final String REGEX_SPLIT = "\\|(?![^()]*+\\))";

    // private static final List< String > WHITESPACE = Arrays.asList(
    // "WHITESPACE", "SPACE", "SPACES", "NEWLINE" );

    private static final Logger log = Logger.getLogger( JavaDefaultLexerImpl.class );

    private LexerFile file;

    protected Pattern patternVar = Pattern.compile( "\\{([A-Z0-9_]+)\\}" );

    protected Pattern patternUtf = Pattern.compile( "(\\\\u[0-9a-fA-F]{4})" );

    protected Pattern patternBrackets = Pattern.compile( "\\[{1}.+(\\|){1}.+\\]{1}|\\({1}.+(\\|){1}.+\\){1}" );

    private static final String SEPARATOR = "\\s(\\|)\\s";

    private List< JavaLexerRule > rules = new ArrayList<>();

    private Map< String, Set< String > > expressions = new HashMap< String, Set< String > >();


    public JavaDefaultLexerImpl( String path, String name ) {

        this.file = new LexerFile( null, name );
        initialize();
    }


    public void initialize() {

        this.rules = getRulesList( file.getFile() );

        if ( log.isDebugEnabled() ) {
            for ( JavaLexerRule rule : rules ) {
                log.debug( rule.toString() );
            }
        }

    }


    protected List< JavaLexerRule > getRulesList( String file ) {

        try {

            List< JavaLexerRule > rules = new ArrayList<>();

            InputStream is = this.getClass().getResourceAsStream( file );

            BufferedReader br = new BufferedReader( new InputStreamReader( is, "UTF-8" ) );
            String strLine;
            // Read File Line By Line
            while ( ( strLine = br.readLine() ) != null ) {
                // Print the content on the console
                log.debug( "Reading line: " + strLine );

                if ( strLine.startsWith( "#" ) ) {
                    continue;
                }

                Matcher m = patternUtf.matcher( strLine );

                while ( m.find() ) {
                    String variable = m.group( 0 );

                    String c = StringEscapeUtils.unescapeJava( variable );
                    strLine = strLine.replaceAll( "\\" + variable, c );

                }

                rules.addAll( createRules( strLine ) );
            }
            // Close the input stream
            is.close();

            return rules;

        }
        catch ( IOException ex ) {
            ex.printStackTrace();
        }

        return new ArrayList<>();

    }


    protected Set< JavaLexerRule > createRules( String value ) {

        Set< JavaLexerRule > rules = new HashSet<>();

        String[] parts = value.split( "->" );

        String start = parts[ 0 ].trim();

        RuleCommand command = RuleCommand.DEFAULT;

        if ( parts.length > 2 ) {
            try {
                command = RuleCommand.valueOf( parts[ 2 ].trim() );
            }
            catch ( Exception e ) {
                log.error( e );
            }
        }

        for ( String rightPart : parts[ 1 ].split( SEPARATOR ) ) {

            JavaLexerRule rule0 = new JavaLexerRule();
            rule0.setLeft( start );
            String right = rightPart.trim();

            Matcher m = patternVar.matcher( right );

            if ( m.find() ) {

                CharSequence variable = m.group( 0 );

                Set< String > set = new HashSet< String >();
                Set< String > set1 = new HashSet< String >();
                Set< String > processed = new HashSet< String >();

                String key = variable.subSequence( 1, variable.length() - 1 ).toString();

                Set< String > elements = expressions.get( key );
                if ( elements != null ) {

                    for ( String el : elements ) {

                        String newRight = createRightPart( key, el, right );
                        set.add( newRight );

                    }

                    processed.add( key );

                }

                while ( m.find() ) {

                    variable = m.group( 0 );

                    key = variable.subSequence( 1, variable.length() - 1 ).toString();

                    if ( processed.contains( key ) ) {
                        continue;
                    }

                    elements = expressions.get( key );
                    if ( elements != null ) {

                        for ( String el : elements ) {

                            if ( set.isEmpty() ) {
                                String newRight = createRightPart( key, el, right );

                                set1.add( newRight );
                            }
                            else {
                                for ( String r : set ) {

                                    String newRight = createRightPart( key, el, r );
                                    set1.add( newRight );
                                }
                            }

                        }

                        set = new HashSet< String >();
                        set.addAll( set1 );
                        set1 = new HashSet< String >();
                    }

                }

                // create rule
                for ( String str : set ) {

                    rule0 = new JavaLexerRule();
                    rule0.setLeft( start );
                    rule0.setRight( str );
                    rule0.setCommand( command );
                    rules.add( rule0 );

                    fillExpression( start, str );

                }

            }
            else {

                rule0.setRight( right );
                rule0.setCommand( command );
                rules.add( rule0 );

                fillExpression( start, right );

            }
        }

        return rules;

    }


    private String createRightPart( String key, String element, String right ) {

        String var = "{" + key + "}";

        Set< String > rightParts = new HashSet< String >();

        String builder = "";

        for ( String rightPart : SplitterUtils.splitWithoutBrackets( '|', right ) ) {
            if ( rightPart.indexOf( var ) >= 0 ) {
                rightParts.add( rightPart );
            }
            else {
                builder = rightPart + "|" + builder;
            }
        }

        for ( String part : rightParts ) {
            String rPart = part.replaceAll( createKey( key ), Matcher.quoteReplacement( "(" + element + ")" ) );
            builder = rPart + "|" + builder;
        }

        if ( builder.toString().endsWith( "|" ) ) {
            return builder.substring( 0, builder.length() - 1 );
        }
        return builder.toString();
    }


    private String createKey( String key ) {

        return "\\{" + key + "\\}";
    }


    private void fillExpression( String start, String right ) {

        Set< String > elements2 = expressions.get( start );
        if ( elements2 != null ) {
            elements2.add( right );
        }
        else {
            elements2 = new HashSet< String >();
            elements2.add( right );
            expressions.put( start, elements2 );
        }
    }


    @Override
    public List< Token > tokenize( File file ) throws FileNotFoundException, IOException {

        BufferedReader br = null;

        try {

            StringBuilder builder = new StringBuilder();

            br = new BufferedReader( new FileReader( file ) );
            String strLine;
            // Read File Line By Line
            while ( ( strLine = br.readLine() ) != null ) {
                builder.append( strLine ).append( "\n" );
            }

            return tokenize( builder.toString() );

        }
        finally {
            if ( br != null ) {
                br.close();
            }
        }
    }


    @Override
    public List< Token > tokenize( String sentence ) {

        boolean found = false;

        List< Token > words = new ArrayList< Token >();

        CharSequence sequence = sentence;

        int endPos = sequence.length();
        int end = 1;

        for ( int start = 0; start < endPos; start++ ) {

            int sequenceLength = 0;
            String theToken = null;
            int sequenceStart = 0;
            int sequenceEnd = 0;
            String type = null;
            RuleCommand command = RuleCommand.DEFAULT;

            for ( JavaLexerRule rule : rules ) {
                Matcher matcher = rule.getRegexMatcher().reset( sequence );

                if ( log.isDebugEnabled() ) {
                    log.debug( "Processing right: " + rule.getRight() );
                }

                if ( matcher.find( start ) ) {

                    end = matcher.end();

                    log.debug( "Match ended on: " + end );

                    String token = sequence.subSequence( start, end ).toString();
                    if ( token.length() > sequenceLength ) {
                        theToken = token;
                        sequenceStart = start;
                        sequenceEnd = end;
                        type = rule.getLeft();
                        sequenceLength = token.length();
                        found = true;
                        command = rule.getCommand();

                        if ( RuleCommand.SKIP_FORWARD.equals( rule.getCommand() )
                            || RuleCommand.SKIP.equals( rule.getCommand() ) ) {
                            break;
                        }
                    }

                }
            }

            if ( found && theToken != null ) {

                if ( !RuleCommand.SKIP.equals( command ) ) {
                    Token word = new Token( type, theToken, sequenceStart, sequenceEnd );
                    words.add( word );

                    log.debug( "New word found: " + word.toString() );

                    end = sequenceEnd;
                }

                start = end - 1;
            }

        }

        return words;

    }


    @Override
    public boolean match( String name, String token ) {

        for ( JavaLexerRule rule : this.rules ) {
            if ( rule.getLeft().equals( name ) ) {
                rule.getMatcher().reset( token );
                return rule.getMatcher().matches();
            }
        }

        return false;

    }

}