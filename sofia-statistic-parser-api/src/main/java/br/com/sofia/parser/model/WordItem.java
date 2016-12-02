package br.com.sofia.parser.model;


/**
 * 
 * @author Rodrigo Salvo
 * 
 */
public class WordItem {

    private Word word;

    private WordItem next;

    
    public WordItem( Word word, WordItem next ) {

        super();
        this.word = word;
        this.next = next;
    }

    public WordItem( Word word ) {

        super();
        this.word = word;
    }
    
    public boolean hasNext(){
        return next != null;
    }
    
    /**
     * @return the word
     */
    public Word word() {

        return word;
    }


    /**
     * @param word
     *            the word to set
     */
    public void setWord( Word word ) {

        this.word = word;
    }


    /**
     * @return the next
     */
    public WordItem next() {

        return next;
    }


    /**
     * @param next
     *            the next to set
     */
    public void setNext( WordItem next ) {

        this.next = next;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( word == null ) ? 0 : word.hashCode() );
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
        WordItem other = (WordItem) obj;
        if ( word == null ) {
            if ( other.word != null )
                return false;
        }
        else if ( !word.equals( other.word ) )
            return false;
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return "WordItem [word=" + word + "]";
    }
    
}
