package br.com.sofia.parser.model;


public class GrammarInfo {
    
    private String variable;
    private String symbol;
    
    public String getVariable() {
    
        return variable;
    }
    
    public void setVariable( String variable ) {
    
        this.variable = variable;
    }
    
    public String getSymbol() {
    
        return symbol;
    }
    
    public void setSymbol( String symbol ) {
    
        this.symbol = symbol;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( variable == null ) ? 0 : variable.hashCode() );
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
        GrammarInfo other = (GrammarInfo) obj;
        if ( variable == null ) {
            if ( other.variable != null )
                return false;
        }
        else if ( !variable.equals( other.variable ) )
            return false;
        return true;
    }
    
}
