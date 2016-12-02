package br.com.sofia.parser.test;

import org.junit.Test;

import br.com.sofia.parser.LRAnalizerImpl;
import br.com.sofia.parser.Lr0ParserImpl;
import br.com.sofia.parser.Lr1ParserImpl;
import br.com.sofia.parser.model.Action;
import br.com.sofia.parser.model.Grammar;
import br.com.sofia.parser.model.GrammarFile;
import br.com.sofia.parser.model.ProcessorResult;
import br.com.sofia.parser.model.Stack;
import br.com.sofia.parser.model.Table;
import br.com.sofia.parser.processor.Processor;
import br.com.sofia.parser.processor.impl.MusicProcessor;
import br.com.sofia.parser.processor.impl.WebProcessor;


public class TestParser {
    
    @Test
    public void test(){
        
        Lr0ParserImpl parser = new Lr0ParserImpl( );
        
        Table table = parser.createTable( new GrammarFile( null, "Grammar09.gr" ) );
        
        parser.logGoTo();
        
        parser.logAction();

        LRAnalizerImpl analizer = new LRAnalizerImpl( table, parser );
        
        boolean result = analizer.analize( "id + id" );
        
        if ( result ){
            System.out.println( "Expression recognized by grammar!" );
        } else {
            System.out.println( "Expression NOT recognized by grammar!" );            
        }
        
    }
    
    public void testGrammar1(){

        System.out.println( "test 1!" );
        
        Lr1ParserImpl parser = new Lr1ParserImpl();
        
        Table table = parser.createTable( new GrammarFile( null, "Grammar01.gr" ) );
        
        parser.logGoTo();
        
        parser.logAction();
        
        LRAnalizerImpl analizer = new LRAnalizerImpl( table, parser );
        
        boolean result = analizer.analize( "id + id" );
        
        if ( result ){
            System.out.println( "Expression recognized by grammar!" );
        } else {
            System.out.println( "Expression NOT recognized by grammar!" );            
        }        
        
    } 
    
    public void testGrammar3(){

        System.out.println( "test 3!" );
        
        Lr1ParserImpl parser = new Lr1ParserImpl();
        
        Table table = parser.createTable( new GrammarFile( null, "Grammar03.gr" ) );
        
        parser.logGoTo();
        
        parser.logAction();
        
        LRAnalizerImpl analizer = new LRAnalizerImpl( table, parser );
        
        boolean result = analizer.analize( "* id = id" );
        
        if ( result ){
            System.out.println( "Expression recognized by grammar!" );
        } else {
            System.out.println( "Expression NOT recognized by grammar!" );            
        }        
        
    }     

    public void testGrammar2(){

        System.out.println( "test 2!" );
        
        Lr1ParserImpl parser = new Lr1ParserImpl();
        
        Table table = parser.createTable( new GrammarFile( null, "Grammar02.gr" ) );
        
        parser.logGoTo();
        
        parser.logAction();
        
        LRAnalizerImpl analizer = new LRAnalizerImpl( table, parser );
        
        //[S [NP [Det the][NBar [Adj old]]][VP[Verb man][NP[Det a][NBar [Noun ship]]]]]
        
        boolean result = analizer.analize( "the old man mans a ship" );
        
        if ( result ){
            System.out.println( "Expression recognized by grammar!" );
        } else {
            System.out.println( "Expression NOT recognized by grammar!" );            
        }        
        
    }
    
    public void testGrammar4(){

        System.out.println( "test 4!" );
        
        Lr1ParserImpl parser = new Lr1ParserImpl();
        
        Table table = parser.createTable( new GrammarFile( null, "Grammar04.gr" ) );
        
        parser.logGoTo();
        
        parser.logAction();
        
        LRAnalizerImpl analizer = new LRAnalizerImpl( table, parser );
        
        //[S [NP [Det the][NBar [Adj old]]][VP[Verb man][NP[Det a][NBar [Noun ship]]]]]
        
        boolean result = analizer.analize( "Sofia, turn on the music" );
        
        if ( result ){
            System.out.println( "Expression recognized by grammar!" );
        } else {
            System.out.println( "Expression NOT recognized by grammar!" );            
        }        
        
        Grammar root = analizer.getGrammar();
        
        System.out.println( root.buildText() );
        
    }

    public void testGrammar8(){

        System.out.println( "test 4!" );
        
        Lr1ParserImpl parser = new Lr1ParserImpl();
        
        Table table = parser.createTable( new GrammarFile( null, "Grammar08.gr" ) );
        
        parser.logGoTo();
        
        parser.logAction();
        
        LRAnalizerImpl analizer = new LRAnalizerImpl( table, parser );
        
        boolean result = analizer.analize( "search the web for wallpapers" );
        
        if ( result ){
            System.out.println( "Expression recognized by grammar!" );
        } else {
            System.out.println( "Expression NOT recognized by grammar!" );            
        }        
        
        Stack< Grammar > list = analizer.getGrammarTokens();

        System.out.println("\n\n");
        
        System.out.println( "Writing grammar:" );
        
        for ( Grammar g : list.collection() ){
            System.out.println( "Rule: " + g.getRule() + " symbol: '" + g.getSymbol() + "' variable: '" + g.getVariable() + "'");
        }

        
        Grammar root = analizer.getGrammar();
        
        System.out.println( root.buildText() );
        
        
    }


    public void testGrammar9(){

        System.out.println( "test 9!" );
        
        Lr1ParserImpl parser = new Lr1ParserImpl();
        
        Table table = parser.createTable( new GrammarFile( null, "Grammar09.gr" ) );
        
        parser.logGoTo();
        
        parser.logAction();
        
        LRAnalizerImpl analizer = new LRAnalizerImpl( table, parser );
        
        boolean result = analizer.analize( "play Roxette" );
        
        if ( result ){
            System.out.println( "Expression recognized by grammar!" );
        } else {
            System.out.println( "Expression NOT recognized by grammar!" );            
        }        
        
        Stack< Grammar > list = analizer.getGrammarTokens();

        System.out.println("\n\n");
        
        System.out.println( "Writing grammar:" );
        
        for ( Grammar g : list.collection() ){
            System.out.println( "Rule: " + g.getRule() + " symbol: '" + g.getSymbol() + "' variable: '" + g.getVariable() + "'");
        }

        
        Grammar root = analizer.getGrammar();
        
        System.out.println( root.buildText() );
        
        Action action = analizer.analizeActions( root );
        
        if ( action != null ){
            System.out.println( "Action: " + action.toString());
            System.out.println( "\nResponse: " + analizer.getBasicResponse() );
        }
        
        Processor processor = new WebProcessor();
        ProcessorResult processorResult = processor.process( action );
        
        if ( processorResult == null ){
            processor = new MusicProcessor();
            processorResult = processor.process( action );
        }
        
        String response = analizer.analizeResult( processorResult );
        
        System.out.println( "\nResponse: " + response );
        
    }
    
}
