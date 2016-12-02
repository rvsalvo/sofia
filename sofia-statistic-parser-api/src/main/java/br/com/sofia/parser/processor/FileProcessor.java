/**
 * 
 */
package br.com.sofia.parser.processor;

import java.io.File;
import java.io.IOException;
import java.util.List;

import br.com.sofia.parser.model.Sentence;


/**
 * @author Rodrigo Salvo
 *
 */
public interface FileProcessor {
    
    List< Sentence > processDir( File dir ) throws IOException;
    
    List< Sentence > processFile( File file ) throws IOException;
    
    boolean isEndOfFile( String token );

}
