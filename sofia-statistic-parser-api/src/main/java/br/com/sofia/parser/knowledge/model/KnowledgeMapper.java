/**
 * 
 */
package br.com.sofia.parser.knowledge.model;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import br.com.sofia.parser.model.TokenLabel;


/**
 * @author Rodrigo Salvo
 *
 */
public final class KnowledgeMapper {
    
    private static final KnowledgeMapper mapper = new KnowledgeMapper();
    
    private Map< TokenLabel, WordStatisticInfo > tokenMap = new ConcurrentHashMap<>();
    
    private KnowledgeMapper(){
        
    }
    
    public static KnowledgeMapper INSTANCE(){
        return mapper;
    }
    
    public WordStatisticInfo getStatisticByToken( TokenLabel token ){
        return tokenMap.get( token );
    }
    
    public Collection< WordStatisticInfo > values(){
        return tokenMap.values();
    }
    
    public void puToken( TokenLabel token, WordStatisticInfo info ){
        tokenMap.put( token, info );
    }
    
    public synchronized WordStatisticInfo incrementToken( TokenLabel token ){
        WordStatisticInfo info = tokenMap.get( token );
        
        if ( info == null ){
            info = new WordStatisticInfo( token, 0 );
            tokenMap.put( token, info );
        }
        
        info.getQuantity().incrementAndGet();
        
        return info;
    }

    public void putTokenDistance( TokenLabel token, WordDistance distance ) {

        WordStatisticInfo info = tokenMap.get( token );
        info.getRightDistances().add( distance );
        
    }

}
