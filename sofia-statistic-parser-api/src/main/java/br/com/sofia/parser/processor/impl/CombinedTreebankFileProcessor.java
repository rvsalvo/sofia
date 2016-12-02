package br.com.sofia.parser.processor.impl;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import br.com.sofia.lexer.Lexer;
import br.com.sofia.lexer.factory.LexerFactory;
import br.com.sofia.lexer.model.Token;
import br.com.sofia.parser.model.Label;
import br.com.sofia.parser.model.TokenLabel;
import br.com.sofia.parser.model.TokenTreeNode;


/**
 * 
 * @author Rodrigo Salvo
 * 
 */
public class CombinedTreebankFileProcessor {

    private static final Logger log = Logger.getLogger( CombinedTreebankFileProcessor.class );

    Lexer lexer = LexerFactory.createExtendedTreebankLexer();


    public List< TokenTreeNode > processDir( File dir ) throws IOException {

        List< TokenTreeNode > sentences = new ArrayList<>();

        for ( String file : dir.list() ) {

            File f = new File( file );

            if ( !f.isDirectory() ) {

                sentences.addAll( this.processFile( new File( dir.getAbsolutePath() + "/" + file ) ) );

            }

        }

        return sentences;
    }


    @SuppressWarnings( "unchecked" )
    public List< TokenTreeNode > processFile( File file ) throws IOException {

        log.debug( "Processing file: " + file.getName() );

        StringBuilder builder = new StringBuilder();

        for ( String strLine : Files.readAllLines( Paths.get( file.toURI() ) ) ) {
            builder.append( strLine.replace( "''", "\"" ).replace( "``", "\"" ) ).append( "\n" );
        }

        if ( builder.length() != 0 ) {
            return createSentences( builder );
        }

        return Collections.EMPTY_LIST;

    }


    private List< TokenTreeNode > createSentences( StringBuilder builder ) {

        List< TokenTreeNode > sentences = new ArrayList<>();

        List< Token > tokens = lexer.tokenize( builder.toString() );

        Stack< TokenTreeNode > stack = new Stack<>();

        List< String > words = new ArrayList< String >();

        int index = 0;
        String lastToken = null;

        for ( Token token : tokens ) {

            Label t = null;

            if ( "(".equals( token.getWord() ) ) {
                index++;

                t = createLabelToken( words, t );

                words = new ArrayList< String >();

                if ( t != null ) {
                    stack.push( new TokenTreeNode( t ) );
                }

            }
            else if ( ")".equals( token.getWord() ) ) {

                index--;

                t = createLabelToken( words, t );

                words = new ArrayList< String >();

                if ( !")".equals( lastToken ) ) {
                    if ( !stack.isEmpty() && t != null ) {
                        stack.peek().addChildren( new TokenTreeNode( t ) );
                    }
                }
                else {

                    if ( !stack.isEmpty() ) {
                        TokenTreeNode parent = stack.pop();

                        if ( t != null ) {
                            parent.addChildren( new TokenTreeNode( t ) );
                        }

                        if ( !stack.isEmpty() ) {
                            stack.peek().addChildren( parent );
                        }
                        else {
                            stack.push( parent );
                        }
                    }

                }

                if ( index == 0 ) {
                    sentences.add( stack.pop() );
                }

            }
            else {

                words.add( token.getWord() );

            }

            lastToken = token.getWord();

        }

        return sentences;
    }


    protected TokenTreeNode buildTreeNode( Stack< Object > stack ) {

        if ( log.isDebugEnabled() ) {
            log.debug( "building TokenTreeNode for stack: " + stack.toString() );
        }

        LinkedList< TokenTreeNode > children = new LinkedList< TokenTreeNode >();

        while ( !stack.isEmpty() ) {

            Object token = stack.pop();

            if ( token instanceof Label ) {
                children.add( new TokenTreeNode( (Label) token ) );
            }
            else if ( token instanceof String ) {

                String t = (String) token;

                if ( "(".equals( t ) ) {

                }

            }

        }

        return null;

    }


    private Label createLabelToken( List< String > words, Label t ) {

        String label = null;
        String value = null;

        if ( words.size() == 2 ) {
            label = words.get( 0 );
            value = words.get( 1 );
            t = new TokenLabel( label, value );
        }
        else if ( words.size() == 1 ) {
            label = words.get( 0 );
            t = new Label( label );
        }
        return t;
    }


    public boolean isEndOfFile( String token ) {

        return lexer.match( "END_OF_SENTENCE", token );

    }

}
