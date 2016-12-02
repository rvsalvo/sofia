package br.com.sofia.regex.analyzer.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class NfaProcessorResult {
    
    private Map< Integer, DfaStateHolder > operations; 
    private NfaState start;
    private boolean fromStart;
    private boolean toEnd;
    private int totalGroups; 
    private Type type;

    public NfaProcessorResult( 
        NfaState start,
        Map< Integer, DfaStateHolder > operations, 
        boolean fromStart,
        boolean toEnd ) {

        this.start = start;
        this.operations = new HashMap< Integer, DfaStateHolder >( operations );
        this.fromStart = fromStart;
        this.toEnd = toEnd;
        
    }

    public NfaProcessorResult( 
        Map< Integer, DfaStateHolder > operations, 
        boolean fromStart,
        boolean toEnd ) {

        this.operations = operations;
        this.fromStart = fromStart;
        this.toEnd = toEnd;
        
    }
    
    public Map< Integer, DfaStateHolder > getOperations() {
    
        return operations;
    }

    
    public boolean isFromStart() {
    
        return fromStart;
    }

    
    public boolean isToEnd() {
    
        return toEnd;
    }

    
    public int getTotalGroups() {
    
        return totalGroups;
    }

    
    public NfaState getStart() {
    
        return start;
    }

    
    public Type getType() {
    
        return type;
    }

    
    public void setType( Type type ) {
    
        this.type = type;
    }

    
}
