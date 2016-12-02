package br.com.sofia.regex.analyzer.model;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class DfaStateHolder {

    private DfaState state;
    private Type type;
    private Map< Integer, DfaStateHolder > operations = new HashMap< Integer, DfaStateHolder >();
    private int startState;
    private int[][] states;
    private boolean[] accept;
    private int[] special;
    private Map< Integer, Integer > groups = new HashMap< Integer, Integer >();
    private boolean charClass;
    
    public DfaStateHolder( DfaState state, Map< Integer, DfaStateHolder > operations ) {

        super();
        this.state = state;
        this.operations = operations;
    }
    
    public DfaStateHolder( DfaState state, Map< Integer, DfaStateHolder > operations, int startState, int[][] states, boolean[] accept, int[] special ) {

        super();
        this.state = state;
        this.operations = operations;
        this.startState = startState;
        this.states = states;
        this.accept = accept;
        this.special = special;
    }    
    
    public DfaStateHolder( DfaState state ) {

        super();
        this.state = state;
    }

    
    public DfaState getState() {
    
        return state;
    }
    
    public Map< Integer, DfaStateHolder > getOperations() {
    
        return operations;
    }

    
    public Type getType() {
    
        return type;
    }

    
    public void setType( Type type ) {
    
        this.type = type;
    }

    
    public int getStartState() {
    
        return startState;
    }

    
    public void setStartState( int startState ) {
    
        this.startState = startState;
    }

    
    public int[][] getStates() {
    
        return states;
    }

    
    public void setStates( int[][] states ) {
    
        this.states = states;
    }

    
    public boolean[] getAccept() {
    
        return accept;
    }

    
    public void setAccept( boolean[] accept ) {
    
        this.accept = accept;
    }

    
    public int[] getSpecial() {
    
        return special;
    }

    
    public void setSpecial( int[] special ) {
    
        this.special = special;
    }

    
    public Map< Integer, Integer > getGroups() {
    
        return groups;
    }

    
    public void setGroups( Map< Integer, Integer > groups ) {
    
        this.groups = groups;
    }

    
    /**
     * @return the charClass
     */
    public boolean isCharClass() {
    
        return charClass;
    }

    
    /**
     * @param charClass the charClass to set
     */
    public void setCharClass( boolean charClass ) {
    
        this.charClass = charClass;
    }

    
}
