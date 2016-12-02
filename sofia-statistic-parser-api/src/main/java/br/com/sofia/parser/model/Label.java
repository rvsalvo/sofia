/**
 * 
 */
package br.com.sofia.parser.model;


/**
 * @author Rodrigo Salvo
 *
 */
public class Label implements Token {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final String label;
    
    public Label( String label ){
        this.label = label;
    }
    
    public String label(){
        return label;
    }

    @Override
    public String value() {

        return null;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {

        return label;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( label == null ) ? 0 : label.hashCode() );
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
        Label other = (Label) obj;
        if ( label == null ) {
            if ( other.label != null )
                return false;
        }
        else if ( !label.equals( other.label ) )
            return false;
        return true;
    }
    
}
