/**
 * 
 */
package br.com.sofia.parser.model;


/**
 * @author Rodrigo Salvo
 *
 */
public class TokenLabel extends Label {
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private final String value;
    
    public TokenLabel( String label, String value ){
        super( label );
        this.value = value;
    }

    
    @Override
    public String value(){
        return value;
    }
    
    @Override
    public String toString(){
        return label() + " " + value();
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = super.hashCode();
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
        if ( !super.equals( obj ) )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        TokenLabel other = (TokenLabel) obj;
        if ( value == null ) {
            if ( other.value != null )
                return false;
        }
        else if ( !value.equals( other.value ) )
            return false;
        return true;
    }
    

}
