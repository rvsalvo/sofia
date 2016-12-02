/**
 * 
 */
package br.com.sofia.parser.knowledge.trainer;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import br.com.sofia.parser.exception.ParserException;
import br.com.sofia.parser.knowledge.TreebankKnowledgeBase;
import br.com.sofia.parser.knowledge.factory.KnowledgeBaseFactory;
import br.com.sofia.parser.knowledge.model.KnowledgeMapper;
import br.com.sofia.parser.knowledge.model.WordDistance;
import br.com.sofia.parser.knowledge.model.WordStatisticInfo;
import br.com.sofia.parser.model.Sentence;
import br.com.sofia.parser.model.TokenLabel;
import br.com.sofia.parser.model.Word;


/**
 * @author Rodrigo Salvo
 *
 */
public class TreeBankKnowledgeBaseTrainer {
    
    private static final Logger log = Logger.getLogger( TreeBankKnowledgeBaseTrainer.class );
    
    private final String treeBankBaseDir;

    public TreeBankKnowledgeBaseTrainer( String treeBankBaseDir ){
        this.treeBankBaseDir = treeBankBaseDir;
    }
    
    public void generateStatisticQuantityInfo() throws ParserException {
        
        TreebankKnowledgeBase base = KnowledgeBaseFactory.createTreebankKnowledgeBase();
        
        List< Sentence > sequences = base.processTreebankDir( new File( treeBankBaseDir + "/raw" ), 
            new File( treeBankBaseDir  + "/tagged" ) );

        for ( Sentence sentence : sequences ) {
            log.debug( "processing sequence for sentense: " + sentence.toString() );
            for ( Word word : sentence.getWords() ){
                
                TokenLabel token = new TokenLabel( word.tag(), word.value() );
                
                KnowledgeMapper.INSTANCE().incrementToken( token );                  
                
                //The average seven-day compound yield of the 400 taxable funds

                /*
                The -> (average,1) (seven-day,2) (compound,3)
                average -> (seven-day,1) (compound,2) (yield,3)
                seven-day -> (compound,1) (yield,2) (of,3)
                compound 
                yield
                of -> (the,1) (400,2) (taxable,3)
                the
                400
                taxable
                funds 
                */

                Iterator<Word> it = sentence.getWords().iterator();
                int index = 1;
                while ( it.hasNext() ){
                        Word wd = it.next();
                        if ( wd.equals( word ) ){
                            while ( index <= 4 && it.hasNext() ){
                                Word nextWord = it.next();
                                TokenLabel tl = new TokenLabel( nextWord.tag(), nextWord.value() );
                                WordDistance distance = new WordDistance( tl, index, 0 );
                                KnowledgeMapper.INSTANCE().putTokenDistance( token, distance );
                                index++;
                            }
                            break;
                        }
                        
                }                
                
            }
            //sentence.getWords().stream().parallel().forEach( word -> process( word ) );
        }         
        
    }
    
    public void printGeneratedStatisticInfo(){
        
        StringBuilder builder;      
        
        for ( WordStatisticInfo info : KnowledgeMapper.INSTANCE().values() ){
            
            builder = new StringBuilder();

            builder.append( info.getToken().value() )
            .append( "/" )
            .append( info.getToken().label() );
            
            if ( !info.getRightDistances().isEmpty() ){
                builder.append( " -> " );
            }
            
            for ( WordDistance distance : info.getRightDistances() ){
                builder.append( " (" )
                .append( distance.getToken().value() )
                .append( ", " )
                .append( distance.getDistance() )
                .append( ")" );
            }
            
            log.info( builder.toString() );
            
        }
        
    }
    
}
