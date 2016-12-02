package br.com.sofia.parser.model;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class Sentence implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private List< Word > words;    
    
    public Sentence( List< Word > words ){
        this.words = words;
    }
    
    @Override
    public String toString() {

        return "Sentence [words=" + words + "]";
    }

    
    /**
     * @return the words
     */
    public List< Word > getWords() {
    
        return words;
    }

    
    /**
     * @param words the words to set
     */
    public void setWords( List< Word > words ) {
    
        this.words = words;
    }

}
