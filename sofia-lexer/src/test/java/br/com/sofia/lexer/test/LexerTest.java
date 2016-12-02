package br.com.sofia.lexer.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import br.com.sofia.lexer.Lexer;
import br.com.sofia.lexer.factory.LexerFactory;
import br.com.sofia.lexer.model.Token;


public class LexerTest {
    
    public void test(){
        
        Lexer lexer = LexerFactory.createDefaultLexer( null, "Lexer01.lr" );
        
        long time = System.currentTimeMillis();
        
        //List< Word > words = lexer.tokenize( "I'm an software engineer, I don't know what to say, but this is another test STRING. Mr. Orlof, here we do a 0800 toll free just for testing!! Call to U.S. and send an e-mail to rsalvo.br@yahoo.com.br." );            
        List< Token > words = lexer.tokenize( "Another ex-Golden Stater, Paul Stankowski from Oxnard, is contending "+
                                                "for a berth on the U.S. Ryder Cup team after winning his first PGA Tour "+
                                                "event last year and staying within three strokes of the lead through "+
                                                "three rounds of last month's U.S. Open. H.J. Heinz Company said it "+
                                                "completed the sale of its Ore-Ida frozen-food business catering to the "+
                                                "service industry to McCain Foods Ltd. for about $500 million."+
                                                "It's the first group action of its kind in Britain and one of "+
                                                "only a handful of lawsuits against tobacco companies outside the "+
                                                "U.S. A Paris lawyer last year sued France's Seita SA on behalf of "+
                                                "two cancer-stricken smokers.Japan Tobacco Inc. faces a suit from "+
                                                "five smokers who accuse the government-owned company of hooking "+
                                                "them on an addictive product." );
        
        long offset = System.currentTimeMillis() - time;
        
        System.out.println( "tokenized " + words.size() + " in: " + offset + " ms" );
        
        for ( Token word : words ){
            System.out.println( word.toString() );
        }
        
    }

    //@Test
    public void test1(){
        
        Lexer lexer = LexerFactory.createGeneralLexer( null, "Lexer01.lr" );
        
        long time = System.currentTimeMillis();
        
        List< Token > words = null;
        try {
            words = lexer.tokenize( new File( "E:\\Projetos\\SofiaProject\\text001.txt" ) );
        }
        catch ( FileNotFoundException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        long offset = System.currentTimeMillis() - time;
        
        for ( Token word : words ){
            System.out.println( word.toString() );
        }
                
        System.out.println( "tokenized " + words.size() + " in: " + offset + " ms" );
        
    }
    
    public void test2(){
        
        System.out.println( "Creating lexer..." );
        
        Lexer lexer = LexerFactory.createDefaultLexer( null, "Lexer01.lr" );
        
        System.out.println( "Starting processing" );
        
        long time = System.currentTimeMillis();
        
        List< Token > words = null;
        try {
            words = lexer.tokenize( new File( "E:\\Projetos\\SofiaProject\\text001.txt" ) );
        }
        catch ( FileNotFoundException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        long offset = System.currentTimeMillis() - time;
        
        for ( Token word : words ){
            System.out.println( word.toString() );
        }
                
        System.out.println( "tokenized " + words.size() + " in: " + offset + " ms" );
        
    }
    
    @Test
    public void testOther(){
        
        System.out.println( "Creating lexer..." );
        
        Lexer lexer = LexerFactory.createDefaultEnglishLexer();
        
        System.out.println( "Starting processing" );
        
        long time = System.currentTimeMillis();
        
        List< Token > words = null;
        try {
            words = lexer.tokenize( new File( "E:\\Projetos\\SofiaProject\\treebank\\treebank\\raw\\test.txt" ) );
        }
        catch ( FileNotFoundException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        long offset = System.currentTimeMillis() - time;
        
        for ( Token word : words ){
            System.out.println( word.toString() );
        }
                
        System.out.println( "tokenized " + words.size() + " in: " + offset + " ms" );
        
    }    
    
    //@Test
    public void test3(){
        
        System.out.println( "Creating lexer..." );
        
        Lexer lexer = LexerFactory.createDefaultLexer( null, "Lexer02.lr" );
        
        System.out.println( "Starting processing" );
        
        long time = System.currentTimeMillis();
        
        List< Token > words = null;
        try {
            words = lexer.tokenize( new File( "E:\\Projetos\\CTBC\\B2C\\Documentacao\\respostaSaml.xml" ) );
        }
        catch ( FileNotFoundException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch ( IOException e ) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        List< String > permitted = new ArrayList<>();
        
        long offset = System.currentTimeMillis() - time;
        
        boolean permit = false;        
        
        for ( Token word : words ){
            
            if ( "DECISION".equals( word.getType() ) ){
                word.setWord( word.getWord().replace( "<Decision>", "" ) );
                
                if ( "Permit".equals( word.getWord() ) ){
                    permit = true;
                } else {
                    permit = false;
                }
                
            } else if ( "ATTRIBUTE".equals( word.getType() ) ){
                word.setWord( word.getWord().replace( "<AttributeValue DataType=\"http://www.w3.org/2001/XMLSchema#string\">", "" ) );
                
                if ( permit ){
                    permitted.add( word.getWord() );
                }
            }
            System.out.println( word.toString() );
        }
        
        System.out.println( "#############################" );
        
        for ( String obj : permitted ){
            System.out.println( "permitted: " + obj );
        }
                
        System.out.println( "tokenized " + words.size() + " in: " + offset + " ms" );
        
    }    
    

}
