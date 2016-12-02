/**
 * 
 */
package br.com.sofia.parser.knowledge.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicLong;

import br.com.sofia.parser.model.TokenLabel;


/**
 * @author Rodrigo Salvo
 *
 */
public class WordStatisticInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    private final TokenLabel token;
    private AtomicLong quantity = new AtomicLong( 0 );
    private double score;
    
    private Collection< WordDistance > rightDistances = new TreeSet<>();
    
    public WordStatisticInfo( TokenLabel token, double score ) {

        super();
        this.token = token;
        this.score = score;
    }
    
    /**
     * @return the quantity
     */
    public AtomicLong getQuantity() {
    
        return quantity;
    }
    
    /**
     * @param quantity the quantity to set
     */
    public void setQuantity( AtomicLong quantity ) {
    
        this.quantity = quantity;
    }
    
    /**
     * @return the score
     */
    public double getScore() {
    
        return score;
    }
    
    /**
     * @return the rightDistance
     */
    public Collection< WordDistance > getRightDistances() {
    
        return rightDistances;
    }
    
    /**
     * @param rightDistance the rightDistance to set
     */
    public void setRightDistances( Collection< WordDistance > rightDistances ) {
    
        this.rightDistances = rightDistances;
    }
    
    /**
     * @return the token
     */
    public TokenLabel getToken() {
    
        return token;
    }

    
    /**
     * @param score the score to set
     */
    public void setScore( double score ) {
    
        this.score = score;
    }
    
}
