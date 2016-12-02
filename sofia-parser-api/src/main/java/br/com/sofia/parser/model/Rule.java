package br.com.sofia.parser.model;

import java.util.HashSet;
import java.util.Set;


public class Rule {
    
    private String left;
    private String right;
    private int index;
    private boolean endPosition;
    
    private Set< String > lookAheads = new HashSet< String >();
    
    public String getLeft() {
    
        return left;
    }
    
    public void setLeft( String left ) {
    
        this.left = left;
    }
    
    public String getRight() {
    
        return right;
    }
    
    public void setRight( String right ) {
    
        this.right = right;
    }
    
    public int getIndex() {
    
        return index;
    }
    
    public void setIndex( int index ) {
    
        this.index = index;
    }



    public boolean isEndPosition() {
    
        return endPosition;
    }

    
    public void setEndPosition( boolean endPosition ) {
    
        this.endPosition = endPosition;
    }

    
    public Set< String > getLookAheads() {
    
        return lookAheads;
    }

    
    public void setLookAheads( Set< String > lookAheads ) {
    
        this.lookAheads = lookAheads;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        result = prime * result + ( ( left == null ) ? 0 : left.hashCode() );
        result = prime * result + ( ( right == null ) ? 0 : right.hashCode() );
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
        Rule other = (Rule) obj;
        if ( index != other.index )
            return false;
        if ( left == null ) {
            if ( other.left != null )
                return false;
        }
        else if ( !left.equals( other.left ) )
            return false;
        if ( right == null ) {
            if ( other.right != null )
                return false;
        }
        else if ( !right.equals( other.right ) )
            return false;
        return true;
    }

    @Override
    public String toString() {

        return left + "->" + right;
        
    }

}
