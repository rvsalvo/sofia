package br.com.sofia.regex.impl;


import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import br.com.sofia.regex.analyzer.model.DfaStateHolder;
import br.com.sofia.regex.analyzer.model.NfaProcessorResult;
import br.com.sofia.regex.analyzer.model.NfaState;
import br.com.sofia.regex.analyzer.model.StateHistory;
import br.com.sofia.regex.analyzer.model.Type;
import br.com.sofia.regex.matcher.RegexMatcher;


/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class NfaMatcherImpl implements RegexMatcher {

    private static final Logger log = Logger.getLogger( NfaMatcherImpl.class );

    private NfaState startState;
    
    private NfaState p;

    private int startIndex = -1;

    private int endIndex = -1;
    
    private int matchedEndIndex = -1;

    private boolean fromStart;

    private boolean toEnd;
    
    private DfaMatcherImpl dfaMatcher;

    private Map< Integer, DfaStateHolder > operations = new HashMap< Integer, DfaStateHolder >();

    private Map< String, Set< Integer > > history = new HashMap< String, Set< Integer > >();

    private LinkedList< Integer > followStates = new LinkedList< Integer >();

    private Map< Integer, StateHistory > stateIndex = new HashMap< Integer, StateHistory >();

    private LinkedList< Integer > lastChoice;

    // contains start indexes of the group
    private int[] groups;

    // contains group string
    private String[] groupsResult;

    private CharSequence expression;

    private int totalGroups;


    public NfaMatcherImpl(
        NfaProcessorResult result,
        DfaMatcherImpl dfaMatcher,
        CharSequence expression,
        boolean fromStart,
        boolean toEnd,
        int totalGroups ) {

        this.startState = result.getStart();
        this.dfaMatcher = dfaMatcher;
        this.expression = expression;
        this.fromStart = fromStart;
        this.toEnd = toEnd;
        this.operations = result.getOperations();
        this.totalGroups = totalGroups;
        this.groups = new int[ totalGroups + 1 ];
        this.groupsResult = new String[ totalGroups + 1 ];
        for ( int i = 0; i < groups.length; i++ ) {
            groups[ i ] = -1;
        }
    }


    public boolean find() {
        
        if ( dfaMatcher.find() ){
            matchedEndIndex = dfaMatcher.end();
            return findInternal( dfaMatcher.start() );
            
        }
        return false;
    }


    public boolean find( int start ) {

        if ( dfaMatcher.find( start ) ){
            matchedEndIndex = dfaMatcher.end();
            return findInternal( dfaMatcher.start() );
            
        }
        return false;
    }
    
    private boolean findInternal( int start ) {

        reset();
        if ( match( fromStart, toEnd, start ) ) {
            return true;
        }
        return false;
    }    


    public boolean matches() {

        if ( dfaMatcher.matches() ){
        
            matchedEndIndex = dfaMatcher.end();
            
            reset();
            return match( true, true, 0 );
        
        } 
        
        return false;
    }


    protected NfaState step( NfaState state, int c, int i, NfaState old ) {

        if ( state == null ) {
            return null;
        }

        Set< Integer > usedDestinations = history.get( state.getNumber() + "-" + i );

        if ( usedDestinations == null ) {
            usedDestinations = new HashSet< Integer >();
        }

        NfaState newState = null;
        
        if ( c != -1 ){
            newState = getStateDestination( state, c, i, old, usedDestinations );
        }
        
        if ( newState == null ) {
            
            newState = getEpsilonDestination( state, c, i, old, usedDestinations );
            
            if ( newState != null ){
                newState.setEpsilon( true );
            }

        }
        
        return newState;
    }


    private NfaState getEpsilonDestination( NfaState state, int c, int i, NfaState old, Set< Integer > usedDestinations ) {

        if ( state.getEpsilonDestinations() == null ) {
            return null;
        }

        if ( state.getEpsilonDestinations().size() == 1 ) {
            return state.getEpsilonDestinations().get( 0 );
        }

        for ( NfaState dest : state.getEpsilonDestinations() ) {
            if ( !usedDestinations.contains( dest.getNumber() ) ) {
                lastChoice.push( dest.getNumber() );
                usedDestinations.add( dest.getNumber() );                
                history.put( state.getNumber() + "-" + i, usedDestinations );

                followStates.push( state.getNumber() );
                StateHistory sh = new StateHistory( i, (char) c, old, groups, groupsResult, history );
                stateIndex.put( state.getNumber(), sh );

                return dest;
            }
        }
        
        return null;
    }


    private NfaState getStateDestination( NfaState state, int c, int i, NfaState old, Set< Integer > usedDestinations ) {

        for ( int k = 1; k < groups.length; k++ ) {
            if ( state.getGroups().contains( k ) ) {
                if ( groups[ k ] == -1 ) {
                    groups[ k ] = i;
                }
            }
            else if ( groups[ k ] != -1 && groupsResult[ k ] == null ) {
                groupsResult[ k ] = expression.subSequence( groups[ k ], i ).toString();
            }
        }

        if ( state.getDestinations( c ) == null ) {
            return null;
        }

        if ( state.getDestinations( c ).size() == 1 ) {
            return state.getDestinations( c ).get( 0 );
        }

        for ( NfaState dest : state.getDestinations( c ) ) {
            if ( !usedDestinations.contains( dest.getNumber() ) ) {
                lastChoice.push( dest.getNumber() );
                usedDestinations.add( dest.getNumber() );
                history.put( state.getNumber() + "-" + i, usedDestinations );

                followStates.push( state.getNumber() );
                StateHistory sh = new StateHistory( i, (char) c, old, groups, groupsResult, history );
                stateIndex.put( state.getNumber(), sh );

                return dest;
            }
        }
        
        return null;
    }


    private boolean match( boolean fromStart, boolean toEnd, int start ) {

        if ( fromStart && start != 0 ) {
            return false;
        }

        p = this.startState;
        NfaState old = p;
        lastChoice = new LinkedList< Integer >();

        int l = expression.length();
        int i = start;
        
        while ( p != null && (!p.isEnd() || i < l ) ){

            int c = i < l ? expression.charAt( i ) : -1;

            log.debug( "In State: " + p.getNumber() + " reading char: " + (char)c + " int value: " + c + " at index: "+ i );

            p = step( p, c, i, old );

            if ( p == null ) {
                
                Integer operation = null;

                for ( Integer key : old.states().keySet() ) {
                    if ( operations.containsKey( key ) ) {
                        operation = key;
                        break;
                    }
                }
                if ( operation != null ) {

                    DfaStateHolder holder = operations.get( operation );
                    if ( Type.NEGATIVE_LOOKAHEAD.equals( holder.getType() ) ) {
                        DfaMatcherImpl temp = new DfaMatcherImpl( holder, expression.subSequence(
                            i,
                            expression.length() ), fromStart, toEnd );
                        boolean result = temp.findExact( 0 );
                        if ( result ) {
                            p = step( old, old.getNumber(), i, old );
                            old = p;
                            if ( endIndex > 0 ) {
                                endIndex--;
                            }
                            return accept( fromStart, toEnd, p );
                        }
                        p = step( old, operation, i, old );
                        old = p;

                    }
                    else if ( Type.NEGATIVE_LOOKBEHIND.equals( holder.getType() ) ) {
                        DfaMatcherImpl temp = new DfaMatcherImpl( holder, expression.subSequence(
                            i,
                            expression.length() ), fromStart, toEnd );
                        boolean result = temp.matches();
                        if ( result ) {
                            p = step( old, old.getNumber(), i, old );
                            old = p;
                            return accept( fromStart, toEnd, p );
                        }
                        p = step( old, operation, i, old );
                        old = p;
                    }
                    else if ( Type.LOOKAHEAD.equals( holder.getType() ) ) {
                        DfaMatcherImpl temp = new DfaMatcherImpl( holder, expression.subSequence(
                            i,
                            expression.length() ), fromStart, toEnd );
                        boolean result = temp.matches();
                        if ( !result ) {
                            
                            if ( !followStates.isEmpty() ) {

                                i = backtrack();
                                continue;
                            }
                            
                            return false;
                        }
                        p = step( old, operation, i, old );
                        old = p;

                    }
                    else if ( Type.LOOKBEHIND.equals( holder.getType() ) ) {
                        DfaMatcherImpl temp = new DfaMatcherImpl( holder, expression.subSequence(
                            i,
                            expression.length() ), fromStart, toEnd );
                        boolean result = temp.matches();
                        if ( !result ) {
                            if ( !followStates.isEmpty() ) {

                                i = backtrack();
                                continue;
                            }
                        }
                        p = step( old, operation, i, old );
                        old = p;

                    }
                    
                    if ( p == null ){
                        return accept( fromStart, toEnd, old );
                    }
                }
                else {

                    if ( old.isEnd() && endIndex == matchedEndIndex ) {
                        return accept( fromStart, toEnd, old );
                    } else if ( !followStates.isEmpty() ) {

                        i = backtrack();
                    }
                    else {
                        return accept( fromStart, toEnd, old );
                    }
                }
            }
            else {

                if ( !p.isEpsilon() ) {
                    if ( startIndex == -1 ) {
                        startIndex = i;
                    }
                    endIndex = i + 1;                    
                    i++;
                }
                
            }
            old = p;
        }
        
        return accept( fromStart, toEnd, old );

    }
    
    @Deprecated
    protected boolean oldMatch( boolean fromStart, boolean toEnd, int start ) {

        if ( fromStart && start != 0 ) {
            return false;
        }

        p = this.startState;
        NfaState old = p;
        boolean epsilon = false;
        lastChoice = new LinkedList< Integer >();

        int l = expression.length();
        for ( int i = start; i < l; i++ ) {

            char c = expression.charAt( i );

            log.debug( "Reading char: " + c + " int value: " + (int) c + " at index: "+ i );

            epsilon = p.isEpsilon();

            log.debug( "state is: " + p.getNumber() + " epsilon: " + epsilon );

            p = step( p, c, i, old );

            if ( p == null ) {
                
                Integer operation = null;

                for ( Integer key : old.states().keySet() ) {
                    if ( operations.containsKey( key ) ) {
                        operation = key;
                        break;
                    }
                }
                if ( operation != null ) {

                    DfaStateHolder holder = operations.get( operation );
                    if ( Type.NEGATIVE_LOOKAHEAD.equals( holder.getType() ) ) {
                        DfaMatcherImpl temp = new DfaMatcherImpl( holder, expression.subSequence(
                            i,
                            expression.length() ), fromStart, toEnd );
                        boolean result = temp.matches();
                        if ( result ) {
                            p = step( old, old.getNumber(), i, old );
                            old = p;
                            if ( endIndex > 0 ) {
                                endIndex--;
                            }
                            return accept( fromStart, toEnd, p );
                        }
                        p = step( old, operation, i, old );
                        old = p;
                        i--;

                    }
                    else if ( Type.NEGATIVE_LOOKBEHIND.equals( holder.getType() ) ) {
                        DfaMatcherImpl temp = new DfaMatcherImpl( holder, expression.subSequence(
                            i,
                            expression.length() ), fromStart, toEnd );
                        boolean result = temp.matches();
                        if ( result ) {
                            p = step( old, old.getNumber(), i, old );
                            old = p;
                            return accept( fromStart, toEnd, p );
                        }
                        p = step( old, operation, i, old );
                        old = p;
                        i--;
                    }
                    else if ( Type.LOOKAHEAD.equals( holder.getType() ) ) {
                        DfaMatcherImpl temp = new DfaMatcherImpl( holder, expression.subSequence(
                            i,
                            expression.length() ), fromStart, toEnd );
                        boolean result = temp.matches();
                        if ( !result ) {
                            
                            if ( !followStates.isEmpty() ) {

                                i = backtrack();
                                continue;
                            }
                            
                            return false;
                        }
                        p = step( old, operation, i, old );
                        old = p;
                        i--;

                    }
                    else if ( Type.LOOKBEHIND.equals( holder.getType() ) ) {
                        DfaMatcherImpl temp = new DfaMatcherImpl( holder, expression.subSequence(
                            i,
                            expression.length() ), fromStart, toEnd );
                        boolean result = temp.matches();
                        if ( !result ) {
                            if ( !followStates.isEmpty() ) {

                                i = backtrack();
                                continue;
                            }
                        }
                        p = step( old, operation, i, old );
                        old = p;

                    }
                    
                    if ( p == null ){
                        return accept( fromStart, toEnd, old );
                    }
                }
                else {

                    if ( old.isEnd() && endIndex == matchedEndIndex ) {
                        return accept( fromStart, toEnd, old );
                    } else if ( !followStates.isEmpty() ) {

                        i = backtrack();
                    }
                    else {
                        return accept( fromStart, toEnd, old );
                    }
                }
            }
            else {

                if ( p.isEpsilon() ) {
                    i--;
                }
                else {
                    if ( startIndex == -1 ) {
                        startIndex = i;
                    }
                    endIndex = i + 1;
                }
            }
            old = p;
        }
        
        int i = expression.length();
        
        if ( p != null && !p.isEnd() ){
            
            do {
                p = step( p, -1, i, old );
                old = p;
                
                if ( ( p == null || !p.isEpsilon() ) && ( !followStates.isEmpty() ) ){
                    i = backtrack();
                }
                
            } while ( p != null && !p.isEnd() && p.isEpsilon() );
        
        }

        return accept( fromStart, toEnd, old );

    }    


    private int backtrack() {

        StateHistory sh = stateIndex.get( followStates.pop() );
        int i = sh.getIndex();
        if ( i == 0 ) {
            startIndex = -1;
        }
        p = sh.getHolder();
        groups = sh.getGroups();
        groupsResult = sh.getGroupsResult();
        history = sh.getHistory();
        if ( lastChoice != null ){
            Set< Integer > usedDestinations = history.get( p.getNumber() + "-" + (i+1) );

            if ( usedDestinations == null ) {
                usedDestinations = new HashSet< Integer >();
            }
            usedDestinations.add( lastChoice.pop() );
        }
        return i;
    }


    /*
     * private boolean match( boolean fromStart, boolean toEnd, int start ) {
     * 
     * Set< Integer > usedDestinations = new HashSet< Integer >();
     * 
     * if ( fromStart && start != 0 ){ return false; }
     * 
     * NfaStateHolder stateHolder = startState; NfaStateHolder oldStateHolder =
     * null; NfaState state = null;
     * 
     * for ( int i = start; i < expression.length(); i++ ) {
     * 
     * char c = expression.charAt( i );
     * 
     * boolean processed = false;
     * 
     * log.debug( "processing char: " + c + " with index: " + i );
     * 
     * if ( stateHolder.isEpsilon() ){ state = stateHolder.get(); } else if (
     * stateHolder.get( c ) != null ){ state = stateHolder.get( c ); processed =
     * true; log.debug( "Matched " + c + " with index: " + i + " in state: "
     * +state.getNumber() ); } else if ( state == null ) { return false; }
     * 
     * if ( !stateHolder.isEpsilon() ){ for ( int k = 1; k < groups.length; k++
     * ){ if ( stateHolder.getGroup().contains( k ) ){ if ( groups[k] == -1 ){
     * groups[k] = i; } } else if ( groups[k] != -1 && groupsResult[k] == null
     * ){ groupsResult[k] = expression.subSequence( groups[k], i ).toString(); }
     * } }
     * 
     * usedDestinations = history.get( state.getNumber() + "-" + c );
     * 
     * if ( usedDestinations == null ){ usedDestinations = new HashSet< Integer
     * >(); }
     * 
     * oldStateHolder = stateHolder; stateHolder = null;
     * 
     * for ( Integer dest : state.getDestinations() ){ if (
     * !usedDestinations.contains( dest ) ){ log.debug( "Going from state: " +
     * state.getNumber() + " to destination: " + dest ); stateHolder =
     * states.get( dest ); usedDestinations.add( dest ); history.put(
     * state.getNumber() + "-" + c, usedDestinations ); break; } }
     * 
     * if ( state.getDestinations().size() > usedDestinations.size() ){
     * log.debug( "Creating history for state: " + state.getNumber() +
     * " with index " + i + ", groups: " + groups ); followStates.add(
     * state.getNumber() ); StateHistory sh = new StateHistory( i, c,
     * oldStateHolder, groups, groupsResult ); stateIndex.put(
     * state.getNumber(), sh ); }
     * 
     * if ( stateHolder != null ){
     * 
     * if ( startIndex == -1 ){ startIndex = i; } endIndex = i + 1;
     * 
     * } else {
     * 
     * if ( operations.isEmpty() ){
     * 
     * if ( !followStates.isEmpty() ){ StateHistory sh = stateIndex.get(
     * followStates.pop() ); i = sh.getIndex(); stateHolder = sh.getHolder();
     * groups = sh.getGroups(); groupsResult = sh.getGroupsResult();
     * 
     * log.debug( "going back to index: " + i ); continue; }
     * 
     * if ( toEnd || ( ( startIndex == -1 || startIndex > 0 ) && fromStart ) ){
     * return false; } else if ( state.isEnd() ){ return true; } } else if (
     * operations.keySet().contains( (int)state.getSymbol() ) ) {
     * 
     * 
     * NfaProcessorResult holder = operations.get( (int)state.getSymbol() ); if
     * ( Type.NEGATIVE_LOOKAHEAD.equals( holder.getStartState().getType() ) ){
     * NfaMatcherImplBkp temp = new NfaMatcherImplBkp( holder.getStates(),
     * holder.getStartState(), expression.subSequence( i, expression.length() ),
     * true, true, totalGroups ); boolean result = temp.matches(); if ( result )
     * { state = oldStateHolder.get( (int)state.getSymbol() ); if (
     * state.isEnd() ){ if ( endIndex != expression.length() && toEnd ){ return
     * false; } else if ( startIndex != 0 && fromStart ){ return false; } return
     * true; } return false; } processed = false; state = oldStateHolder.get(
     * (int)state.getSymbol() ); } else if ( Type.LOOKAHEAD.equals(
     * holder.getStartState().getType() ) ){ NfaMatcherImplBkp temp = new
     * NfaMatcherImplBkp( holder.getStates(), holder.getStartState(),
     * expression.subSequence( i, expression.length() ), true, true, totalGroups
     * ); boolean result = temp.matches(); if ( !result ) { return false; }
     * processed = false; state = oldStateHolder.get( (int)state.getSymbol() );
     * 
     * } else if ( Type.NEGATIVE_LOOKBEHIND.equals(
     * holder.getStartState().getType() ) ){ NfaMatcherImplBkp temp = new
     * NfaMatcherImplBkp( holder.getStates(), holder.getStartState(),
     * expression.subSequence( i, expression.length() ), true, true, totalGroups
     * ); boolean result = temp.matches(); if ( result ) { state =
     * oldStateHolder.get( (int)state.getSymbol() ); if ( state.isEnd() ){ if (
     * endIndex != expression.length() && toEnd ){ return false; } else if (
     * startIndex != 0 && fromStart ){ return false; } return true; } return
     * false; } processed = false; state = oldStateHolder.get(
     * (int)state.getSymbol() ); } else if ( Type.LOOKBEHIND.equals(
     * holder.getStartState().getType() ) ){ NfaMatcherImplBkp temp = new
     * NfaMatcherImplBkp( holder.getStates(), holder.getStartState(),
     * expression.subSequence( i, expression.length() ), true, true, totalGroups
     * ); boolean result = temp.matches(); if ( !result ) { return false; }
     * state = oldStateHolder.get( (int)state.getSymbol() ); }
     * 
     * } }
     * 
     * if ( !processed ){ i--; }
     * 
     * }
     * 
     * for ( int k = 1; k < groups.length; k++ ){ if ( groups[k] != -1 &&
     * groupsResult[k] == null ){ groupsResult[k] = expression.subSequence(
     * groups[k], expression.length() ).toString(); } }
     * 
     * do {
     * 
     * state = stateHolder.get();
     * 
     * if ( state.isEnd() ){ if ( endIndex != expression.length() && toEnd ){
     * return false; } else if ( startIndex != 0 && fromStart ){ return false; }
     * return true; }
     * 
     * stateHolder = null;
     * 
     * for ( Integer dest : state.getDestinations() ){ if (
     * !usedDestinations.contains( dest ) ){ stateHolder = states.get( dest );
     * usedDestinations.add( dest ); history.put( ""+state.getNumber(),
     * usedDestinations ); break; } }
     * 
     * if ( stateHolder == null ){ return false; }
     * 
     * } while ( stateHolder.isEpsilon() );
     * 
     * return false;
     * 
     * }
     */

    private boolean accept( boolean fromStart, boolean toEnd, NfaState p ) {

        if ( p.isEnd() ) {
            if ( endIndex != expression.length() && toEnd ) {
                return false;
            }
            else if ( startIndex != 0 && fromStart ) {
                return false;
            }

            for ( int k = 1; k < groups.length; k++ ) {
                if ( groups[ k ] != -1 && groupsResult[ k ] == null ) {
                    groupsResult[ k ] = expression.subSequence( groups[ k ], endIndex ).toString();
                }
            }

            return true;
        }

        return false;
    }


    @Override
    public RegexMatcher reset() {

        this.startIndex = -1;
        this.endIndex = -1;
        for ( int i = 0; i < groups.length; i++ ) {
            groups[ i ] = -1;
            groupsResult[ i ] = null;
        }
        followStates = new LinkedList< Integer >();
        stateIndex = new HashMap< Integer, StateHistory >();
        history = new HashMap< String, Set< Integer > >();
        lastChoice = null;
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

        if ( group > totalGroups ) {
            throw new IllegalStateException( "No match found" );
        }
        return groupsResult[ group ];

    }


    @Override
    public String group() {

        return group( 1 );
    }


    @Override
    public int groupCount() {

        return totalGroups;
    }


    @Override
    public RegexMatcher reset( CharSequence sequence ) {

        this.expression = sequence;
        this.dfaMatcher.reset( sequence );
        return reset();
    }


    @Override
    public boolean findExact( int start ) {

        if ( dfaMatcher.findExact( start ) ){
            
            matchedEndIndex = dfaMatcher.end();
            reset();
            return match( false, false, start );
            
        }
        return false;
    }
}
