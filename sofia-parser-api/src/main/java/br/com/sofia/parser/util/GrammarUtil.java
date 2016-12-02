package br.com.sofia.parser.util;

import java.util.ArrayList;
import java.util.List;

import br.com.sofia.parser.model.Item;
import br.com.sofia.parser.model.Rule;


public class GrammarUtil {
    

    private static final char[] CHARACTERS = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
        'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V',
        'W', 'X', 'Y', 'Z', '#' };
    
    public static final String TERMINAL_SYMBOL = "$";

    public static final String WHITESPACE_SYMBOL = " ";
    
    public static void writeItem( Item it ) {

        StringBuilder s = new StringBuilder( " [  " );

        for ( Rule r : it.getRules() ) {

            int position = 0;

            boolean done = false;

            s.append( r.getLeft() ).append( "->" );

            while ( !done ) {

                if ( position == r.getIndex() ) {
                    if ( s.substring( s.length() - 1 ).equals( WHITESPACE_SYMBOL ) ){ 
                        s.replace( s.length() - 1, s.length(), "" );
                    }                    
                    s.append( "." );
                }

                String symbol = getSymbol( r.getRight(), position );

                if ( symbol == null ) {
                    done = true;
                    if ( s.indexOf( "." ) < 0 ) {
                        if ( s.substring( s.length() - 1 ).equals( WHITESPACE_SYMBOL ) ){ 
                            s.replace( s.length() - 1, s.length(), "" );
                        }
                        s.append( "." );
                    }
                }
                else if ( !TERMINAL_SYMBOL.equals( symbol ) ) {

                    s.append( symbol + WHITESPACE_SYMBOL );

                }

                position++;

            }
            
            s.append( ", {" );
            
            for ( String l : r.getLookAheads() ){
                s.append( l ).append( WHITESPACE_SYMBOL );
            }

            s.append( "}  " );
        }

        s.append( "]" );
        System.out.print( "\nItem state " + it.getState() + ": " + s.toString() );
        if ( it.getParent() != null ) {
            System.out.print( " #### Transition from " + it.getParent().getState() + " with symbol '" + it.getSymbol() + "'" );
        }
    }
        
    public static String logItem( Item it ) {

        StringBuilder s = new StringBuilder( "[  " );

        for ( Rule r : it.getRules() ) {

            int position = 0;

            boolean done = false;

            s.append( r.getLeft() ).append( "->" );

            while ( !done ) {

                if ( position == r.getIndex() ) {
                    if ( s.substring( s.length() - 1 ).equals( WHITESPACE_SYMBOL ) ){ 
                        s.replace( s.length() - 1, s.length(), "" );
                    }                    
                    s.append( "." );
                }

                String symbol = getSymbol( r.getRight(), position );

                if ( symbol == null ) {
                    done = true;
                    if ( s.indexOf( "." ) < 0 ) {
                        if ( s.substring( s.length() - 1 ).equals( WHITESPACE_SYMBOL ) ){ 
                            s.replace( s.length() - 1, s.length(), "" );
                        }
                        s.append( "." );
                    }
                }
                else if ( !TERMINAL_SYMBOL.equals( symbol ) ) {

                    s.append( symbol + WHITESPACE_SYMBOL );

                }

                position++;

            }
            
            s.append( ", {" );
            
            for ( String l : r.getLookAheads() ){
                s.append( l ).append( WHITESPACE_SYMBOL );
            }

            s.append( "}  " );
        }

        s.append( "]" );
        
        return s.toString();
    }
            
    
    public static String getSymbol( String body, int position ) {

        boolean whiteSpace = false;
        boolean unidentified = false;
        StringBuilder word = new StringBuilder();

        List< String > list = new ArrayList< String >();

        for ( char c : body.toCharArray() ) {
            if ( list.size() > position ) {
                break;
            }
            if ( hasSymbol( c ) ) {
                if ( whiteSpace ) {
                    whiteSpace = false;
                    unidentified = false;
                    list.add( word.toString() );
                    word = new StringBuilder();
                }
                else if ( unidentified ) {
                    unidentified = false;
                    whiteSpace = false;                    
                    list.add( word.toString() );
                    word = new StringBuilder();
                }
                word.append( c );
            }
            else if ( TERMINAL_SYMBOL.equals( String.valueOf( c ) ) ) {
                if ( word.length() != 0 ) {
                    list.add( word.toString() );
                }
                list.add( String.valueOf( c ) );
            }
            else if ( WHITESPACE_SYMBOL.equals( String.valueOf( c ) ) ) {
                whiteSpace = true;
                continue;
            }
            else {
                if ( word.length() != 0 ) {
                    list.add( word.toString() );
                }
                word = new StringBuilder( String.valueOf( c ) );
                unidentified = true;
            }
        }

        if ( list.size() <= position ) {
            return null;
        }

        return list.get( position );

    }
    
    

    
    public static int getRulePositions( String body ) {

        boolean whiteSpace = false;
        boolean unidentified = false;
        StringBuilder word = new StringBuilder();

        List< String > list = new ArrayList< String >();

        for ( char c : body.toCharArray() ) {

            if ( hasSymbol( c ) ) {
                if ( whiteSpace ) {
                    whiteSpace = false;
                    list.add( word.toString() );
                    word = new StringBuilder();
                }
                else if ( unidentified ) {
                    unidentified = false;
                    list.add( word.toString() );
                    word = new StringBuilder();
                }
                word.append( c );
            }
            else if ( TERMINAL_SYMBOL.equals( String.valueOf( c ) ) ) {
                if ( word.length() != 0 ) {
                    list.add( word.toString() );
                }
                list.add( String.valueOf( c ) );
            }
            else if ( WHITESPACE_SYMBOL.equals( String.valueOf( c ) ) ) {
                whiteSpace = true;
                continue;
            }
            else {
                if ( word.length() != 0 ) {
                    list.add( word.toString() );
                }
                word = new StringBuilder( String.valueOf( c ) );
                unidentified = true;
            }
        }

        return list.size();

    }

    public static boolean hasSymbol( char s ) {

        for ( char c : CHARACTERS ) {
            if ( s == c ) {
                return true;
            }
        }
        return false;
    }


    public static int getSymbolLastIndex( String body, int position ) {

        String regex = "\\s+";

        String[] parts = body.split( regex );

        if ( position >= parts.length ) {
            return parts.length;
        }

        String s = parts[ position ];
        return body.indexOf( s ) + s.length();

    }


    public static int getSymbolPosition( String body, String symbol ) {

        boolean whiteSpace = false;
        boolean unidentified = false;
        StringBuilder word = new StringBuilder();

        List< String > list = new ArrayList< String >();

        for ( char c : body.toCharArray() ) {

            if ( hasSymbol( c ) ) {
                if ( whiteSpace ) {
                    whiteSpace = false;
                    list.add( word.toString() );
                    word = new StringBuilder();
                }
                else if ( unidentified ) {
                    unidentified = false;
                    list.add( word.toString() );
                    word = new StringBuilder();
                }
                word.append( c );
            }
            else if ( TERMINAL_SYMBOL.equals( String.valueOf( c ) ) ) {
                if ( word.length() != 0 ) {
                    list.add( word.toString() );
                }
                list.add( String.valueOf( c ) );
            }
            else if ( WHITESPACE_SYMBOL.equals( String.valueOf( c ) ) ) {
                whiteSpace = true;
                continue;
            }
            else {
                if ( word.length() != 0 ) {
                    list.add( word.toString() );
                }
                word = new StringBuilder( String.valueOf( c ) );
                unidentified = true;
            }
        }

        for ( int i = 0; i < list.size(); i++ ) {
            String s = list.get( i );
            if ( s.equals( symbol ) ) {
                return i;
            }
        }

        return -1;
    }

}
