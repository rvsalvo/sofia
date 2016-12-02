package br.com.sofia.lexer.test;

import java.util.List;

import org.junit.Test;

import br.com.sofia.lexer.Lexer;
import br.com.sofia.lexer.factory.LexerFactory;
import br.com.sofia.lexer.model.Token;


public class LexerTestPtBr {
    
    //@Test
    public void test(){
        
        Lexer lexer = LexerFactory.createGeneralLexer( null, "Lexer01.lr" );
        
        long time = System.currentTimeMillis();
        
        List< Token > words = lexer.tokenize( "I'm a male. This is another test STRING. Mr. Orlof, here we do a 0800 toll free just for testing!! Call to U.S. and send an e-mail to rsalvo.br@yahoo.com.br." );            
        
        long offset = System.currentTimeMillis() - time;
        
        System.out.println( "time: " + offset + " ms" );
        
        for ( Token word : words ){
            System.out.println( word.toString() );
        }
        
    }
    
    //@Test
    public void testGeneralLexer(){
        
        System.out.println( "\nGeneralLexer test" );        
        
        Lexer lexer = LexerFactory.createGeneralLexer( null, "FlatTreebankLexer.lr" );
        
        long time = System.currentTimeMillis();
        
        List< Token > words = lexer.tokenize( "weren't" );            
        
        long offset = System.currentTimeMillis() - time;
        
        System.out.println( "time: " + offset + " ms" );
        
        for ( Token word : words ){
            System.out.println( word.toString() );
        }
        
    }  
    
    @Test
    public void testTreebank(){
        
        System.out.println( "TreebankLexer test" );
        
        Lexer lexer = LexerFactory.createDefaultLexer( null, "FlatTreebankLexer.lr" );
        
        long time = System.currentTimeMillis();
        
        List< Token > words = lexer.tokenize( "weren't" );            
        
        long offset = System.currentTimeMillis() - time;
        
        System.out.println( "time: " + offset + " ms" );
        
        for ( Token word : words ){
            System.out.println( word.toString() );
        }
        
    }     

}
