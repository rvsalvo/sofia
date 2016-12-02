package br.com.sofia.regex.analyzer.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class NfaState {
    
    private List< NfaState > epsilonDestinations = new ArrayList< NfaState >();
    private Map< Integer, List< NfaState > > destinations = new HashMap< Integer, List<NfaState> >();

    protected boolean epsilon;
    private final int number;
    private boolean start;
    private boolean end;
    private final Set< Integer > groups = new HashSet< Integer >();
    
    public NfaState( int number ) {

        super();
        this.number = number;
    }
    
    public NfaState( int number, LinkedList< Integer > groups ) {

        super();
        this.number = number;
        this.groups.addAll( groups );
    }
    
    public NfaState( int number, NfaState dest, LinkedList< Integer > groups  ) {

        super();
        this.groups.addAll( groups );
        this.epsilonDestinations.add( dest );
        this.number = number;
    }
    
    public NfaState( int number, int input, NfaState dest, LinkedList< Integer > groups ) {

        super();
        this.groups.addAll( groups );
        List< NfaState > d = this.destinations.get( input );
        if ( d == null ){
            d = new ArrayList< NfaState >();
            d.add( dest );
        }
        this.destinations.put( input, d );
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

    

    
    public List< NfaState > getEpsilonDestinations() {
    
        return epsilonDestinations;
    }

    public List< NfaState > getDestinations( int input ) {
        
        return destinations.get( input );
    }   
    
    public Map< Integer, List< NfaState > > states(){
        return destinations;
    }
    
    public void put( int input, NfaState dest ){
        List< NfaState > states = new ArrayList< NfaState >();
        states.add( dest );
        destinations.put( input, states );
    }
    
    public int getNumber() {
    
        return number;
    }

    
    public Set< Integer > getGroups() {
    
        return groups;
    }

    
    public void addGroups( LinkedList< Integer > groups ) {
    
        this.groups.addAll( groups );
    }

    
    public boolean isEpsilon() {
    
        return epsilon;
    }

    
    public void setEpsilon( boolean epsilon ) {
    
        this.epsilon = epsilon;
    }

    @Override
    public String toString() {

        return "NfaState [number=" + number + ", isEpsilon=" + isEpsilon() + ", start=" + start + ", end=" + end + ", groups=" + groups + "]";
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + number;
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
        NfaState other = (NfaState) obj;
        if ( number != other.number )
            return false;
        return true;
    }
    
    

}
