package br.com.sofia.regex.analyzer.model;

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class TransitionState {
    
    private int stateNumber;
    private boolean marked = false;
    private boolean finalState = false;
    private boolean startState = false;
    private Set< Integer > states = new HashSet< Integer >();
    
    public TransitionState( int stateNumber, int state ){
        this.states.add( state );
        this.stateNumber = stateNumber;
    }
    
    public TransitionState( Set< Integer > states ){
        this.states = states;
    }    
    
    public TransitionState( int stateNumber ){
        this.stateNumber = stateNumber;
    }
    
    public TransitionState(){
    }

    public void add( int state ){
        this.states.add( state );
    }
  

    public Set< Integer > getStates() {
    
        return states;
    }

    
    public int getStateNumber() {
    
        return stateNumber;
    }

    
    public boolean isMarked() {
    
        return marked;
    }

    
    public void setMarked( boolean marked ) {
    
        this.marked = marked;
    }

    
    public void setStates( Set< Integer > states ) {
    
        this.states = states;
    }

    
    public void setStateNumber( int stateNumber ) {
    
        this.stateNumber = stateNumber;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( states == null ) ? 0 : states.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {

        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        TransitionState other = (TransitionState) obj;
        if ( states == null ) {
            if ( other.states != null )
                return false;
        }
        else if ( !states.equals( other.states ) )
            return false;
        return true;
    }

    
    public boolean isFinalState() {
    
        return finalState;
    }

    
    public void setFinalState( boolean finalState ) {
    
        this.finalState = finalState;
    }

    
    public boolean isStartState() {
    
        return startState;
    }

    
    public void setStartState( boolean startState ) {
    
        this.startState = startState;
    }

    @Override
    public String toString() {

        return "TransitionState [stateNumber=" + stateNumber + ", marked=" + marked + ", finalState=" + finalState
            + ", startState=" + startState + ", states=" + states + "]";
    }
    
}
