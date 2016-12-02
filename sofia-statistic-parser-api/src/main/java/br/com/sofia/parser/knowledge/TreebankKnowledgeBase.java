/**
 * 
 */
package br.com.sofia.parser.knowledge;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;

import br.com.sofia.parser.exception.ParserException;
import br.com.sofia.parser.model.DictionaryWord;
import br.com.sofia.parser.model.Sentence;
import br.com.sofia.parser.model.Word;
import br.com.sofia.parser.model.WordItem;
import br.com.sofia.parser.processor.FileProcessor;
import br.com.sofia.parser.processor.factory.FileProcessorFactory;


/**
 * @author Rodrigo Salvo
 *
 */
public class TreebankKnowledgeBase {

    private static final Logger log = Logger.getLogger( TreebankKnowledgeBase.class );

    private Map< Word, DictionaryWord > dictionary = new HashMap<>();


    public void generateBase( List< Sentence > sentences ) {

        for ( Sentence sentence : sentences ) {

            for ( Word token : sentence.getWords() ) {

                DictionaryWord word = dictionary.get( token );

                if ( word == null ) {
                    word = new DictionaryWord( token.value(), token.tag(), token.label() );
                    dictionary.put( token, word );
                }
                else {
                    word.incrementFrequency();
                }

            }

        }

    }


    public List< Sentence > processTreebankDir( File plainFileDir, File taggedFileDir ) throws ParserException {

        List< Sentence > allSentences = new ArrayList<>();

        for ( String f : plainFileDir.list() ) {

            File file = new File( plainFileDir.getAbsolutePath() + "/" + f );

            for ( String ft : taggedFileDir.list() ) {

                File fileTagged = new File( taggedFileDir.getAbsolutePath() + "/" + ft );

                if ( FilenameUtils.getBaseName( file.getName() ).equals(
                    FilenameUtils.getBaseName( fileTagged.getName() ) ) ) {
                    allSentences.addAll( processTreebankFile( file, fileTagged ) );
                    break;
                }

            }

        }

        return allSentences;
    }


    public List< Sentence > processTreebankFile( File plainFile, File taggedFile ) throws ParserException {

        log.debug( "processing files: " + plainFile + ", " + taggedFile );

        FileProcessor plainProcessor = FileProcessorFactory.createPlainTreebankFileProcessor();

        FileProcessor taggedProcessor = FileProcessorFactory.createTaggedTreebankFileProcessor();

        try {
            List< Sentence > plainSequences = plainProcessor.processFile( plainFile );

            List< Sentence > taggedSequences = taggedProcessor.processFile( taggedFile );

            WordItem item = null;
            WordItem last = null;

            // get tagged file
            for ( Sentence sentence : taggedSequences ) {

                // for each word of tagged file
                for ( Word word : sentence.getWords() ) {

                    WordItem it = new WordItem( word );

                    // set next word
                    if ( last != null ) {
                        last.setNext( it );
                    }

                    // get first item
                    if ( item == null ) {
                        item = it;
                    }

                    last = it;

                }

            }

            for ( Sentence sentence : plainSequences ) {
                for ( Word word : sentence.getWords() ) {

                    if ( item != null && item.word().equals( word ) ) {

                        WordItem nextItem = item.next();

                        if ( nextItem != null ) {

                            String tag = nextItem.word().value();

                            log.debug( "matched word: " + word.value() + " tag: " + tag );

                            word.setTag( tag );
                            item = nextItem.next();

                        }
                        else if ( taggedProcessor.isEndOfFile( item.word().value() ) ) {

                            String tag = item.word().label();

                            log.debug( "end of file - match word: " + word.value() + " tag: " + tag );

                            word.setTag( tag );
                            item = item.next();

                        }
                        else {
                            throw new ParserException( "next tag is null for word: " + word.value() );
                        }

                    }

                }
            }

            return plainSequences;

        }
        catch ( IOException e ) {
            log.error( e );
            throw new ParserException( e );
        }

    }

}
