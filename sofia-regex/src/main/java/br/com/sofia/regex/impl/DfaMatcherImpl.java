package br.com.sofia.regex.impl;


import java.util.HashMap;
import java.util.Map;

import br.com.sofia.regex.analyzer.model.DfaStateHolder;
import br.com.sofia.regex.analyzer.model.Type;
import br.com.sofia.regex.matcher.RegexMatcher;


/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class DfaMatcherImpl implements RegexMatcher {

    private int startIndex = -1;

    private int endIndex = -1;

    private boolean fromStart;

    private boolean toEnd;

    private int[][] states;

    private boolean[] accept;

    private int[] special;

    private int start;

    private Map< Integer, DfaStateHolder > operations = new HashMap< Integer, DfaStateHolder >();

    private CharSequence expression;


    public DfaMatcherImpl( DfaStateHolder stateHolder, CharSequence expression, boolean fromStart, boolean toEnd ) {

        this.operations = stateHolder.getOperations();
        this.expression = expression;
        this.fromStart = fromStart;
        this.toEnd = toEnd;
        this.states = stateHolder.getStates();
        this.start = stateHolder.getStartState();
        this.accept = stateHolder.getAccept();
        this.special = stateHolder.getSpecial();
    }


    public boolean find() {
        
        for ( int i = 0; i < expression.length(); i++ ) {
            reset();
            if ( match( fromStart, toEnd, i ) ) {
                return true;
            }
        }
        return false;
    }


    public boolean find( int start ) {

        for ( int i = start; i < expression.length(); i++ ) {
            reset();
            if ( match( fromStart, toEnd, i ) ) {
                return true;
            }
        }
        return false;
    }


    public boolean matches() {

        reset();
        return match( true, true, 0 );
    }


    public int step( int state, int c ) {

        if ( states[ state ].length < c ){
            return 0;
        }
        
        return states[ state ][ c ];
    }


    private boolean match( boolean fromStart, boolean toEnd, int start ) {

        if ( fromStart && start != 0 ) {
            return false;
        }

        int p = this.start;
        int old = p;

        int l = expression.length();
        for ( int i = start; i < l; i++ ) {

            p = step( p, expression.charAt( i ) );
            if ( p == 0 ) {
                if ( special[ old ] != 0 ) {

                    DfaStateHolder holder = operations.get( special[ old ] );
                    if ( Type.NEGATIVE_LOOKAHEAD.equals( holder.getType() ) ) {
                        DfaMatcherImpl temp = new DfaMatcherImpl( holder, expression.subSequence(
                            i,
                            expression.length() ), true, false );
                        boolean result = temp.findExact( 0 );
                        if ( result ) {
                            p = step( old, special[ old ] );
                            old = p;
                            if ( endIndex > 0 ) {
                                endIndex--;
                            }
                            return accept( fromStart, toEnd, p );
                        }
                        p = step( old, special[ old ] );
                        old = p;
                        i--;
                        
                    } else if ( Type.NEGATIVE_LOOKBEHIND.equals( holder.getType() ) ) {
                        DfaMatcherImpl temp = new DfaMatcherImpl( holder, expression.subSequence(
                            i,
                            expression.length() ), true, true );
                        boolean result = temp.matches();
                        if ( result ) {
                            p = step( old, special[ old ] );
                            old = p;
                            return accept( fromStart, toEnd, p );
                        }
                        p = step( old, special[ old ] );
                        old = p;
                        i--;
                    }
                    else if ( Type.LOOKAHEAD.equals( holder.getType() ) ) {
                        DfaMatcherImpl temp = new DfaMatcherImpl( holder, expression.subSequence(
                            i,
                            expression.length() ), true, true );
                        boolean result = temp.matches();
                        if ( !result ) {
                            return false;
                        }
                        p = step( old, special[ old ] );
                        old = p;
                        i--;
                        
                    } else if ( Type.LOOKBEHIND.equals( holder.getType() ) ) {
                        DfaMatcherImpl temp = new DfaMatcherImpl( holder, expression.subSequence(
                            i,
                            expression.length() ), true, true );
                        boolean result = temp.matches();
                        if ( !result ) {
                            return false;
                        }
                        p = step( old, special[ old ] );
                        old = p;
                        
                    }
                }
                else {

                    return accept( fromStart, toEnd, old );
                }
            }
            else {
                if ( startIndex == -1 ) {
                    startIndex = i;
                }
                endIndex = i + 1;
            }
            old = p;
        }
        return accept( fromStart, toEnd, p );

    }


    private boolean accept( boolean fromStart, boolean toEnd, int p ) {

        if ( accept[ p ] ) {
            if ( endIndex != expression.length() && toEnd ) {
                return false;
            }
            else if ( startIndex != 0 && fromStart ) {
                return false;
            }
            return true;
        }

        return false;
    }


    @Override
    public RegexMatcher reset() {

        this.startIndex = -1;
        this.endIndex = -1;
        return this;

    }


    @Override
    public int start() {

        return startIndex;
    }


    @Override
    public int end() {

        return endIndex;
    }


    @Override
    public String group( int group ) {

        throw new UnsupportedOperationException( "Group capture not supported" );
    }


    @Override
    public String group() {

        throw new UnsupportedOperationException( "Group capture not supported" );
    }


    @Override
    public int groupCount() {

        throw new UnsupportedOperationException( "Group capture not supported" );
    }


    @Override
    public RegexMatcher reset( CharSequence sequence ) {

        this.expression = sequence;
        return reset();
    }


    @Override
    public boolean findExact( int start ) {

        reset();
        return match( false, false, start );
    }

}
