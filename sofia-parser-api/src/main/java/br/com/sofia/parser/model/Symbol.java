package br.com.sofia.parser.model;


public class Symbol {
    
    private String value;
    private boolean terminal;
    private int index;
    
    public String getValue() {
    
        return value;
    }
    
    public void setValue( String value ) {
    
        this.value = value;
    }
    
    public boolean isTerminal() {
    
        return terminal;
    }
    
    public void setTerminal( boolean terminal ) {
    
        this.terminal = terminal;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( value == null ) ? 0 : value.hashCode() );
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
        Symbol other = (Symbol) obj;
        if ( value == null ) {
            if ( other.value != null )
                return false;
        }
        else if ( !value.equals( other.value ) )
            return false;
        return true;
    }

    
    public int getIndex() {
    
        return index;
    }

    
    public void setIndex( int index ) {
    
        this.index = index;
    }

    
}
