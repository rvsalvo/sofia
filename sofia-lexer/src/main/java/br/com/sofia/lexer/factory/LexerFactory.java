package br.com.sofia.lexer.factory;

import br.com.sofia.lexer.Lexer;
import br.com.sofia.lexer.impl.DefaultLexerImpl;
import br.com.sofia.lexer.impl.DefaultReverseLexerImpl;
import br.com.sofia.lexer.impl.GeneralLexerImpl;
import br.com.sofia.lexer.impl.JavaDefaultLexerImpl;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class LexerFactory {
    
    public static Lexer createDefaultLexer( String path, String name ){
        return new DefaultLexerImpl( path, name );
    }
    
    public static Lexer createDefaultEnglishLexer(){
        return new DefaultLexerImpl( null, "EnglishLexer.lr" );
    }   
    
    public static Lexer createDefaultTreebankLexer(){
        return new DefaultLexerImpl( null, "TreebankLexer.lr" );
    }
    
    public static Lexer createReverseTreebankLexer(){
        return new DefaultReverseLexerImpl( null, "TreebankLexer.lr" );
    }   
    
    public static Lexer createJavaDefaultTreebankLexer(){
        return new JavaDefaultLexerImpl( null, "TreebankLexer.lr" );
    }    
    
    public static Lexer createExtendedTreebankLexer(){
        return new DefaultLexerImpl( null, "ExtendedTreebankLexer.lr" );
    }    

    public static Lexer createGeneralLexer( String path, String name ){
        return new GeneralLexerImpl( path, name );
    }
}
