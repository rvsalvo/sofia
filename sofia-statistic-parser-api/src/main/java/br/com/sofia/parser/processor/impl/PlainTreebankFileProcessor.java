package br.com.sofia.parser.processor.impl;


import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import br.com.sofia.lexer.Lexer;
import br.com.sofia.lexer.factory.LexerFactory;
import br.com.sofia.lexer.model.Token;
import br.com.sofia.parser.model.Sentence;
import br.com.sofia.parser.model.Word;
import br.com.sofia.parser.processor.FileProcessor;


/**
 * 
 * @author Rodrigo Salvo
 * 
 */
public class PlainTreebankFileProcessor implements FileProcessor {

    private static final Logger log = Logger.getLogger( PlainTreebankFileProcessor.class );

    //Lexer lexer = LexerFactory.createReverseTreebankLexer();
    Lexer lexer = LexerFactory.createDefaultTreebankLexer();


    @Override
    public List< Sentence > processDir( File dir ) throws IOException {

        List< Sentence > sentences = new ArrayList<>();

        for ( String file : dir.list() ) {

            File f = new File( file );

            if ( !f.isDirectory() ) {

                sentences.addAll( this.processFile( new File( dir.getAbsolutePath() + "/" + file ) ) );

            }

        }

        return sentences;
    }


    @Override
    public List< Sentence > processFile( File file ) throws IOException {

        List< Sentence > sentences = new ArrayList<>();

        log.debug( "Processing file: " + file.getName() );

        if ( !file.isFile() ) {
            log.debug( "File is not a regular file... skipping." );
            return sentences;
        }

        StringBuilder builder = new StringBuilder();

        for ( String strLine : Files.readAllLines( Paths.get( file.toURI() ), Charset.forName( "UTF-8" ) ) ) {
            if ( StringUtils.isNotEmpty( strLine ) && !strLine.startsWith( ".START" ) ) {
                builder.append( strLine.replace( "''", "\"" ).replace( "``", "\"" ) ).append( "\n" );
            }
            else if ( StringUtils.isEmpty( strLine.trim() ) ) {
                if ( builder.length() != 0 ) {
                    sentences.add( createSentence( builder ) );
                    builder = new StringBuilder();
                }
            }
        }

        return sentences;

    }


    private Sentence createSentence( StringBuilder builder ) {

        List< Token > tokens = lexer.tokenize( builder.toString() );

        List< Word > tokensForSentence = new ArrayList<>();

        int index = 1;

        for ( Token token : tokens ) {

            if ( token.getWord().indexOf( "/" ) > 0 ){
                token.setWord( token.getWord().replace( "/", "\\/" ) );
            }
            
            Word word = new Word( token.getType(), token.getWord(), index );

            tokensForSentence.add( word );

            index++;

        }

        Sentence sentence = new Sentence( tokensForSentence );

        return sentence;
    }


    public boolean isEndOfFile( String token ) {

        return lexer.match( "END_OF_SENTENCE", token );

    }

}
