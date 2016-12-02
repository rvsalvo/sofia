package br.com.sofia.parser.model;


/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class Word extends TokenLabel {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private String tag;
    private final int index;
    
    private Word reference;
    private String tagReference;
    
    public Word( String label, String word, int index ) {

        super( label, word );
        this.index = index;
    }

    public String tag() {
    
        return tag;
    }

    
    public void setTag( String tag ) {
    
        this.tag = tag;
    }

    
    
    /**
     * @return the index
     */
    public int getIndex() {
    
        return index;
    }

    
    /**
     * @return the reference
     */
    public Word getReference() {
    
        return reference;
    }

    
    /**
     * @param reference the reference to set
     */
    public void setReference( Word reference ) {
    
        this.reference = reference;
    }

    
    /**
     * @return the tagReference
     */
    public String getTagReference() {
    
        return tagReference;
    }

    
    /**
     * @param tagReference the tagReference to set
     */
    public void setTagReference( String tagReference ) {
    
        this.tagReference = tagReference;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( tag == null ) ? 0 : tag.hashCode() );
        result = prime * result + ( ( value() == null ) ? 0 : value().hashCode() );
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
        Word other = (Word) obj;
        if ( tag == null ) {
            if ( other.tag != null )
                return false;
        }
        else if ( !tag.equals( other.tag ) )
            return false;
        if ( value() == null ) {
            if ( other.value() != null )
                return false;
        }
        else if ( !value().equals( other.value() ) ){
            return false;
        }
       
        return true;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return "Word [word=" + value() + ", tag=" + tag + ", type=" + label() + ", index=" + index + "]";
    }
    
}
