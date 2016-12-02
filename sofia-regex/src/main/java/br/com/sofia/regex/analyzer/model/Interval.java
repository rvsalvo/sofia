package br.com.sofia.regex.analyzer.model;




/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class Interval {

    private final char start, end;
    private int startIndex, endIndex;

    public Interval( char start, char end ) {

        this.start = start;
        this.end = end;
    }


    public char getStart() {

        return start;
    }


    public char getEnd() {

        return end;
    }


    public boolean contains( char point ) {

        return start <= point && end >= point;
    }


    public boolean contains( Interval other ) {

        return this.start <= other.start && this.end >= other.end;
    }
    

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + end;
        result = prime * result + start;
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
        Interval other = (Interval) obj;
        if ( end != other.end )
            return false;
        if ( start != other.start )
            return false;
        return true;
    }
    

    protected static boolean isPrintable( char c ) {

        return c > 31 && c < 127;
    }


    public String toString() {

        StringBuffer result = new StringBuffer( "[" );

/*        if ( isPrintable( start ) )
            result.append( "'" + start + "'" );
        else*/
            result.append( (int) start );

        if ( start != end ) {
            result.append( "-" );

  /*          if ( isPrintable( end ) )
                result.append( "'" + end + "'" );
            else*/
                result.append( (int) end );
        }

        result.append( "]" );
        return result.toString();
    }


    
    public int getStartIndex() {
    
        return startIndex;
    }


    
    public void setStartIndex( int startIndex ) {
    
        this.startIndex = startIndex;
    }


    
    public int getEndIndex() {
    
        return endIndex;
    }


    
    public void setEndIndex( int endIndex ) {
    
        this.endIndex = endIndex;
    }

}
