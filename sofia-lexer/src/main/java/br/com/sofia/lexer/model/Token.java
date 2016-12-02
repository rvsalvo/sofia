package br.com.sofia.lexer.model;


public class Token {

    private String type;
    private String word;
    private int startIndex;
    private int finalIndex;
    
    public Token( String type, String word, int startIndex, int finalIndex ) {

        super();
        this.type = type;
        this.word = word;
        this.startIndex = startIndex;
        this.finalIndex = finalIndex;
    }

    public String getType() {
    
        return type;
    }
    
    public void setType( String type ) {
    
        this.type = type;
    }
    
    public String getWord() {
    
        return word;
    }
    
    public void setWord( String word ) {
    
        this.word = word;
    }
    
    public int getStartIndex() {
    
        return startIndex;
    }
    
    public void setStartIndex( int startIndex ) {
    
        this.startIndex = startIndex;
    }
    
    public int getFinalIndex() {
    
        return finalIndex;
    }
    
    public void setFinalIndex( int finalIndex ) {
    
        this.finalIndex = finalIndex;
    }

    @Override
    public String toString() {

        return "Word [type=" + type + ", word=" + word + "]";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( type == null ) ? 0 : type.hashCode() );
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
        Token other = (Token) obj;
        if ( type == null ) {
            if ( other.type != null )
                return false;
        }
        else if ( !type.equals( other.type ) )
            return false;
        if ( word == null ) {
            if ( other.word != null )
                return false;
        }
        else if ( !word.equals( other.word ) )
            return false;
        return true;
    }
    
}
