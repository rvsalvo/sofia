package br.com.sofia.regex.analyzer.model;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class EndState {
    
    private NfaState state;
    private int index;
    
    public EndState( NfaState state, int index ) {

        super();
        this.state = state;
        this.index = index;
    }

    public NfaState getState() {
    
        return state;
    }
    
    public void setState( NfaState state ) {
    
        this.state = state;
    }
    
    public int getIndex() {
    
        return index;
    }
    
    public void setIndex( int index ) {
    
        this.index = index;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + index;
        result = prime * result + ( ( state == null ) ? 0 : state.hashCode() );
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
        EndState other = (EndState) obj;
        if ( index != other.index )
            return false;
        if ( state == null ) {
            if ( other.state != null )
                return false;
        }
        else if ( !state.equals( other.state ) )
            return false;
        return true;
    }

}
