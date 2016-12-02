package br.com.sofia.regex.analyzer.model;

import java.util.HashMap;
import java.util.Map;


/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class DfaState {
    
    private boolean start;
    private boolean end;
    private int number;
    private boolean negate;
    
    //map input destinations
    private Map< Integer, DfaState > states = new HashMap< Integer, DfaState >();
    
    public DfaState( int number ){
        this.number = number;
    }
    
    public boolean isStart() {
    
        return start;
    }
    
    public void setStart( boolean start ) {
    
        this.start = start;
    }
    
    public boolean isEnd() {
    
        return end;
    }
    
    public void setEnd( boolean end ) {
    
        this.end = end;
    }

    
    public int getNumber() {
    
        return number;
    }

    
    public void setNumber( int number ) {
    
        this.number = number;
    }
    
    public DfaState getState( int input ){
        return states.get( input );
    }
    
    public DfaState get( int input ){
        return states.get( input );
    }

    public void put( int input, DfaState state ){
        states.put( input, state );
    }

    @Override
    public String toString() {

        return "DfaState [start=" + start + ", end=" + end + ", number=" + number + "]";
    }

    
    public boolean isNegate() {
    
        return negate;
    }

    
    public void setNegate( boolean negate ) {
    
        this.negate = negate;
    }

    
    public Map< Integer, DfaState > getStates() {
    
        return states;
    }

}
