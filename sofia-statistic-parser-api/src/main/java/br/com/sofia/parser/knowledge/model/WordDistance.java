/**
 * 
 */
package br.com.sofia.parser.knowledge.model;

import java.io.Serializable;

import br.com.sofia.parser.model.TokenLabel;


/**
 * @author Rodrigo Salvo
 * 
 * Stores how distant a word if from another one in a sentence.
 *
 */
public class WordDistance implements Comparable< WordDistance >, Serializable {

    private static final long serialVersionUID = 1L;
    private final TokenLabel token;
    private final int distance;
    private double score;
    
    public WordDistance( TokenLabel token, int distance, double score ) {

        super();
        this.token = token;
        this.distance = distance;
        this.score = score;
    }
    
    /**
     * @return the distance
     */
    public int getDistance() {
    
        return distance;
    }
    
    @Override
    public int compareTo( WordDistance o ) {

        if ( o != null ){
            return this.getDistance() > o.getDistance() ? 1 : this.getDistance() == o.getDistance() ? 0 : -1; 
        }
        return 0;
    }


    
    /**
     * @return the score
     */
    public double getScore() {
    
        return score;
    }

    
    /**
     * @return the token
     */
    public TokenLabel getToken() {
    
        return token;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + distance;
        result = prime * result + ( ( token == null ) ? 0 : token.hashCode() );
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals( Object obj ) {

        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        WordDistance other = (WordDistance) obj;
        if ( distance != other.distance )
            return false;
        if ( token == null ) {
            if ( other.token != null )
                return false;
        }
        else if ( !token.equals( other.token ) )
            return false;
        return true;
    }

    
    /**
     * @param score the score to set
     */
    public void setScore( double score ) {
    
        this.score = score;
    }
    
}
