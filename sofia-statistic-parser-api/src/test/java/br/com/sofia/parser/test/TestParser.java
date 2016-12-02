package br.com.sofia.parser.test;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;

import br.com.sofia.parser.exception.ParserException;
import br.com.sofia.parser.knowledge.TreebankKnowledgeBase;
import br.com.sofia.parser.knowledge.factory.KnowledgeBaseFactory;
import br.com.sofia.parser.knowledge.trainer.TreeBankKnowledgeBaseTrainer;
import br.com.sofia.parser.model.Sentence;
import br.com.sofia.parser.processor.FileProcessor;
import br.com.sofia.parser.processor.factory.FileProcessorFactory;


/**
 * 
 * @author Rodrigo Salvo
 * 
 */
public class TestParser {

    private static final Logger log = Logger.getLogger( TestParser.class );

    //@Test
    public void testTokenizer() {

        String dirName = "E:\\Projetos\\SofiaProject\\treebank\\treebank\\raw\\wsj_0199";

        File dir = new File( dirName );

        FileProcessor processor = FileProcessorFactory.createPlainTreebankFileProcessor();

        try {
            List< Sentence > sequences = processor.processFile( dir );

            for ( Sentence sentence : sequences ) {
                log.info( sentence.toString() );                
            }

        }
        catch ( IOException e ) {
            log.error( e );
        }
        
    }
    
    //@Test
    public void testTagged() {

        String dirName = "E:\\Projetos\\SofiaProject\\treebank\\treebank\\tagged\\wsj_0001.pos";

        File dir = new File( dirName );

        FileProcessor processor = FileProcessorFactory.createTaggedTreebankFileProcessor();

        try {
            List< Sentence > sequences = processor.processFile( dir );

            for ( Sentence sentence : sequences ) {
                log.info( sentence.toString() );                
            }

        }
        catch ( IOException e ) {
            log.error( e );
        }
        
    } 
    
    //@Test
    public void testCombined() {

        String dirName = "E:\\Projetos\\SofiaProject\\treebank\\treebank\\combined\\wsj_0001.mrg";

        File dir = new File( dirName );

        FileProcessor processor = FileProcessorFactory.createPlainTreebankFileProcessor();

        try {
            List< Sentence > sequences = processor.processFile( dir );

            for ( Sentence sentence : sequences ) {
                log.info( sentence.toString() );                
            }

        }
        catch ( IOException e ) {
            log.error( e );
        }
        
    }     
    
    //@Test
    public void testKnowledgeBase() throws ParserException {
        
        TreebankKnowledgeBase base = KnowledgeBaseFactory.createTreebankKnowledgeBase();
       
        List< Sentence > sequences = base.processTreebankFile( new File( "E:\\Projetos\\SofiaProject\\treebank\\treebank\\raw\\wsj_0198" ), 
            new File( "E:\\Projetos\\SofiaProject\\treebank\\treebank\\tagged\\wsj_0198.pos" ) );
        
        for ( Sentence sentence : sequences ) {
            log.info( sentence.toString() );                
        }        
    }
    
    //@Test
    public void testKnowledgeBaseByDir() throws ParserException {
        
        TreebankKnowledgeBase base = KnowledgeBaseFactory.createTreebankKnowledgeBase();
       
        List< Sentence > sequences = base.processTreebankDir( new File( "E:\\Projetos\\SofiaProject\\treebank\\treebank\\raw" ), 
            new File( "E:\\Projetos\\SofiaProject\\treebank\\treebank\\tagged" ) );
        
        for ( Sentence sentence : sequences ) {
            log.info( sentence.toString() );                
        }        
    }
    
    @Test
    public void testStatisticGeneration() throws ParserException {
        
        TreeBankKnowledgeBaseTrainer trainer = new TreeBankKnowledgeBaseTrainer( "E:\\Projetos\\SofiaProject\\treebank\\tests" );
        
        log.info( "Generating statistic info..." );
        
        trainer.generateStatisticQuantityInfo();

        log.info( "###############################################" );
        log.info( "###############################################" );
        log.info( "Statistic info generated!" );
        log.info( "###############################################" );
        log.info( "###############################################" );
        
        trainer.printGeneratedStatisticInfo();
        
    }

}
