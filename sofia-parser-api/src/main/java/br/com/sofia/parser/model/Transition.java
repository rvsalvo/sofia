package br.com.sofia.parser.model;

import java.util.Set;


public class Transition {
    
    private Set< String > lookAheads;
    private String symbol;
    
    public Set< String > getLookAheads() {
    
        return lookAheads;
    }
    
    public void setLookAheads( Set< String > lookAheads ) {
    
        this.lookAheads = lookAheads;
    }
    
    public String getSymbol() {
    
        return symbol;
    }
    
    public void setSymbol( String symbol ) {
    
        this.symbol = symbol;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( symbol == null ) ? 0 : symbol.hashCode() );
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
        Transition other = (Transition) obj;
        if ( symbol == null ) {
            if ( other.symbol != null )
                return false;
        }
        else if ( !symbol.equals( other.symbol ) )
            return false;
        return true;
    }
    
}
