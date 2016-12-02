package br.com.sofia.lexer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import br.com.sofia.lexer.model.Token;


public interface Lexer {
    
    List< Token > tokenize( String sentence );
    
    List< Token > tokenize( File file ) throws FileNotFoundException, IOException;
    
    boolean match( String name, String token );

}
