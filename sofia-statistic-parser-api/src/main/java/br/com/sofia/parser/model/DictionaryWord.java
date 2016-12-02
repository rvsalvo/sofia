package br.com.sofia.parser.model;

import java.util.concurrent.atomic.AtomicLong;


/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class DictionaryWord implements Comparable< DictionaryWord > {
    
    private final String value;
    private final String type;    
    private final String tag;
    private AtomicLong frequency = new AtomicLong( 1 );
    
    public DictionaryWord( String value, String tag, String type ) {

        super();
        this.value = value;
        this.tag = tag;
        this.type = type;
    }
    
    public void incrementFrequency(){
        this.frequency.incrementAndGet();
    }

    /**
     * @return the value
     */
    public String getValue() {
    
        return value;
    }
    
    
    /**
     * @return the tag
     */
    public String getTag() {
    
        return tag;
    }
    
    /**
     * @return the frequency
     */
    public long getFrequency() {
    
        return frequency.longValue();
    }
    
    /**
     * @param frequency the frequency to set
     */
    public void setFrequency( long frequency ) {
    
        this.frequency.set( frequency );
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( tag == null ) ? 0 : tag.hashCode() );
        result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
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
        DictionaryWord other = (DictionaryWord) obj;
        if ( tag == null ) {
            if ( other.tag != null )
                return false;
        }
        else if ( !tag.equals( other.tag ) )
            return false;
        if ( value == null ) {
            if ( other.value != null )
                return false;
        }
        else if ( !value.equals( other.value ) )
            return false;
        return true;
    }

    @Override
    public int compareTo( DictionaryWord value1 ) {

        int comp = value.compareTo( value1.getValue() );
        
        if ( comp == 0 && tag != null ){
            return tag.compareTo( value1.getTag() );
        }
        
        return comp;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return "DictionaryWord [value=" + value + ", tag=" + tag + ", frequency=" + frequency + "]";
    }

    
    /**
     * @return the type
     */
    public String getType() {
    
        return type;
    }
    
}
