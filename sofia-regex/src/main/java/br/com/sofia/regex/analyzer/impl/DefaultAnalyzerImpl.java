package br.com.sofia.regex.analyzer.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import br.com.sofia.regex.analyzer.model.DfaState;
import br.com.sofia.regex.analyzer.model.DfaStateHolder;
import br.com.sofia.regex.analyzer.model.Interval;
import br.com.sofia.regex.analyzer.model.NfaProcessorResult;
import br.com.sofia.regex.analyzer.model.NfaState;
import br.com.sofia.regex.analyzer.model.RegexExpression;
import br.com.sofia.regex.analyzer.model.StatePair;
import br.com.sofia.regex.analyzer.model.TransitionState;
import br.com.sofia.regex.analyzer.model.Type;
import br.com.sofia.regex.exception.NfaContructionException;
import br.com.sofia.regex.util.ArrayUtils;
import br.com.sofia.regex.util.IntervalUtils;


/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class DefaultAnalyzerImpl {
    
    private static Logger log = Logger.getLogger( DefaultAnalyzerImpl.class );
    
    private int numStates = 1;

    private Map< Integer, NfaState > states = new HashMap< Integer, NfaState >();
    
    private int[][] dfaStates;
    
    private boolean accept[];
    
    private int special[];
    
    private Set< Integer > inputs;
    
    private Vector< Interval > charClasses = new Vector< Interval >();
    
    private Map< Integer, DfaStateHolder > dfaOperations = new HashMap< Integer, DfaStateHolder >();
    
    private AtomicInteger operation = new AtomicInteger( -10 );
    
    private int finalState = -1;
    
    private boolean fromStart;
    
    private boolean toEnd;
    
    public DefaultAnalyzerImpl( Vector< Interval > charClasses ){

        //default char intervals
        if ( charClasses.isEmpty() ){
            charClasses.add( new Interval( (char)28, (char)127 ) );
            charClasses.add( new Interval( (char)192, (char)252 ) );
        }
        
        this.charClasses = charClasses;
        
    }
    
  public NfaProcessorResult process( RegexExpression exp, boolean fromStart, boolean toEnd ) {
        
        log.debug( "Creating NFA automato" );

        states = new HashMap< Integer, NfaState >();
        inputs = new HashSet< Integer >();
        this.fromStart = fromStart;
        this.toEnd = toEnd;
        this.finalState = -1;

        StatePair pair = insertNFA( exp );

        if ( states.get( pair.getEnd() ) != null ) {
            NfaState state = states.get( pair.getEnd() );
            state.setEnd( true );
        }
        else {
            NfaState state = new NfaState( pair.getEnd() );
            state.setEnd( true );
            states.put( pair.getEnd() , state );
        }
        
        this.finalState = pair.getEnd();

        NfaState nfaHolder = states.get( pair.getStart() );
        
        return new NfaProcessorResult( nfaHolder, dfaOperations, fromStart, toEnd );
    }


    private StatePair insertNFA( RegexExpression regExp ) {

        int start, end;

        if ( regExp.isCharClass() ) {
            start = numStates;
            end = numStates + 1;

            if ( end + 1 > numStates ){
                numStates = end + 1;
            }

            insertNFAClass( regExp, start, end );

            return new StatePair( start, end );
        }

        StatePair nfa1, nfa2;
        
        switch ( regExp.getType() ) {

            case BAR:

                nfa1 = insertNFA( regExp.getExp1() );
                nfa2 = insertNFA( regExp.getExp2() );

                start = nfa2.getEnd() + 1;
                end = nfa2.getEnd() + 2;

                addEpsilonTransition( start, nfa1.getStart(), regExp.getGroup() );
                addEpsilonTransition( start, nfa2.getStart(), regExp.getGroup() );
                addEpsilonTransition( nfa1.getEnd(), end, regExp.getGroup() );
                addEpsilonTransition( nfa2.getEnd(), end, regExp.getGroup() );

                return new StatePair( start, end );

            case CONCAT:

                nfa1 = insertNFA( regExp.getExp1() );
                nfa2 = insertNFA( regExp.getExp2() );

                addEpsilonTransition( nfa1.getEnd(), nfa2.getStart(), regExp.getGroup() );

                return new StatePair( nfa1.getStart(), nfa2.getEnd() );

            case STAR:
                nfa1 = insertNFA( (RegexExpression) ( regExp ).getContent() );

                start = nfa1.getEnd() + 1;
                end = nfa1.getEnd() + 2;

                addEpsilonTransition( nfa1.getEnd(), end, regExp.getGroup() );
                addEpsilonTransition( start, nfa1.getStart(), regExp.getGroup() );

                addEpsilonTransition( start, end, regExp.getGroup() );
                addEpsilonTransition( nfa1.getEnd(), nfa1.getStart(), regExp.getGroup() );

                return new StatePair( start, end );

            case PLUS:
                nfa1 = insertNFA( (RegexExpression) ( regExp ).getContent() );

                start = nfa1.getEnd() + 1;
                end = nfa1.getEnd() + 2;

                addEpsilonTransition( nfa1.getEnd(), end, regExp.getGroup() );
                addEpsilonTransition( start, nfa1.getStart(), regExp.getGroup() );

                addEpsilonTransition( nfa1.getEnd(), nfa1.getStart(), regExp.getGroup() );

                return new StatePair( start, end );

            case QUESTION:
                nfa1 = insertNFA( (RegexExpression) ( regExp ).getContent() );

                addEpsilonTransition( nfa1.getStart(), nfa1.getEnd(), regExp.getGroup() );

                return new StatePair( nfa1.getStart(), nfa1.getEnd() );
                
            case NEGATIVE_LOOKAHEAD:
                return insertSpecialState( regExp, Type.NEGATIVE_LOOKAHEAD );
                
            case LOOKAHEAD:
                return insertSpecialState( regExp, Type.LOOKAHEAD );
                
            case NEGATIVE_LOOKBEHIND:
                return insertSpecialState( regExp, Type.NEGATIVE_LOOKBEHIND );
                
            case LOOKBEHIND:
                return insertSpecialState( regExp, Type.LOOKBEHIND );                      
                
            default:
                throw new Error( "Unknown expression type " + regExp.getType() + " in NFA construction" );                
        }

        
    }
    
    
   private StatePair insertSpecialState( RegexExpression exp, Type type ){
       
       log.debug( "Inserting special state for " + type + "\n" );
        
        int start, end;
        
        start = numStates;
        end = numStates + 1;

        if ( end + 1 > numStates ){
            numStates = end + 1;
        }
        
        DefaultAnalyzerImpl nfa = new DefaultAnalyzerImpl( this.charClasses );
        NfaProcessorResult state = nfa.process( (RegexExpression)exp.getContent(), true, true );
        state.setType( type );
        
        int value = operation.decrementAndGet();

        insertLetter( false, value , start, end, exp.getGroup() );
        
        DfaStateHolder holder = nfa.createDfa( state.getStart() );
        holder.setType( type );
        
        if ( exp.getContent() instanceof RegexExpression ){
            RegexExpression ex = (RegexExpression)exp.getContent();
            if ( Type.CLASS.equals( ex.getType() ) ){
                holder.setCharClass( true );
            }
        }
        
        dfaOperations.put( value, holder );
        
        log.debug( "Ending special state for " + type + "\n");
        
        return new StatePair( start, end );
        
    }
    
     private void addTransition( int start, int input, int dest, LinkedList< Integer > groups ) {

        log.debug( "Adding transition (" + start + ", " + (char)input + ", " + dest + ") group: " + groups + "                  >>>>>> transition:  (" + start + ", " + input + ", " + dest + ") " );
        
        inputs.add( (int)input );

        int maxS = Math.max( start, dest ) + 1;

        if ( maxS > numStates ){
            numStates = maxS;
        }
        
        NfaState destination = states.get( dest );
        if ( destination == null ){
            destination = new NfaState( dest, groups );
            states.put( dest, destination );            
        } else {
            destination.addGroups( groups );
        }

        if ( states.get( start ) != null ) {
            if ( states.get( start ).getDestinations( input ) != null ){
                states.get( start ).getDestinations( input ).add( destination );
            } else {
                states.get( start ).put( input, destination );
            }
        }
        else {
            states.put( start, new NfaState( start, input, destination, groups ) );
        }
        
    }


    private void addEpsilonTransition( int start, int dest, LinkedList< Integer > groups ) {

        int max = Math.max( start, dest ) + 1;
        if ( max > numStates ){
            numStates = max;
        }

        NfaState destination = states.get( dest );
        if ( destination == null ){
            destination = new NfaState( dest, groups );
            states.put( dest, destination );
        } else {
            destination.addGroups( groups );
        }
        
        NfaState sth = states.get( start );
        if ( sth != null ) {
            sth.getEpsilonDestinations().add( destination );
        }
        else {
            sth = new NfaState( start, destination, groups );
            states.put( start, sth );
        }

        log.debug( "Adding epsilon state (" + start + ", " + dest + ") groups: " + groups );
    }

    
    private void insertLetter( boolean caseless, int letter, int start, int end, LinkedList< Integer > group ) {

        if ( caseless ) {
            char lower = ( Character.toLowerCase( (char)letter ) );
            char upper = ( Character.toUpperCase( (char)letter ) );
            addTransition( start, lower, end, group );
            if ( upper != lower ) {
                addTransition( start, upper, end, group );
            }
        }
        else {
            addTransition( start, letter, end, group );
        }
    }


    @SuppressWarnings( "unchecked" )
    private void insertNFAClass( RegexExpression regExp, int start, int end ) {

        switch ( regExp.getType() ) {

            case BAR:
                insertNFAClass( regExp.getExp1(), start, end );
                insertNFAClass( regExp.getExp2(), start, end );
                return;

            case CLASS:
                insertClass( (Vector< Interval >) ( (RegexExpression) regExp ).getContent(), start, end, regExp.getGroup() );
                return;

            case NOT_CLASS:
                insertNotClass( (Vector< Interval >) ( (RegexExpression) regExp ).getContent(), start, end, regExp.getGroup() );
                return;

            case CONTENT:
                insertLetter( false, (Character) regExp.getContent(), start, end, regExp.getGroup() );
                return;
                
            default:
                throw new NfaContructionException( "Unknown expression type " + regExp.getType() );
                
        }

    }


    private void insertClass( Vector< Interval > intervals, int start, int end, LinkedList< Integer > group ) {

        if ( intervals == null || intervals.isEmpty() ) {
            return;
        }

        for ( Interval interval : intervals ) {
            for ( int i = interval.getStart(); i <= interval.getEnd(); i++ ) {
                addTransition( start, (char) i, end, group );
            }
        }
    }


    private void insertNotClass( Vector< Interval > intervals, int start, int end, LinkedList< Integer > group ) {

        if ( intervals == null || intervals.isEmpty() ) {
            return;
        }
        
        for ( Interval interval : IntervalUtils.diff( charClasses, intervals ) ) {
            for ( int i = interval.getStart(); i <= interval.getEnd(); i++ ) {
                addTransition( start, (char) i, end, group );
            }
        }
    }
    
    public DfaStateHolder createDfa( NfaState startState ){
        
        log.debug( "Creating DFA automato" );
        
        int numStates = 1;
        
        //construct DFA map
        Map< Integer, DfaState > statesMap = new HashMap< Integer, DfaState >();
        
        Set< TransitionState > dfaStates = new HashSet< TransitionState >();
        
        TransitionState ts = new TransitionState( numStates++ );
        ts.setStartState( true );
        
        //create initial state closure
        ts.setStates( closure( startState.getNumber() ) );
            
        //add closure for the first state
        dfaStates.add( ts );
        
        int startStateNumber = ts.getStateNumber();
        
        boolean done = false;
        
        while ( !done ){
            
            done = true;
            
            Set< TransitionState > tempStates = new HashSet< TransitionState >();
            
            for ( TransitionState st : dfaStates ){
                if ( !st.isMarked() ){
                    
                    done = false;
                    st.setMarked( true );
                    
                    //for each symbol
                    for ( Integer input : inputs ){
                        
                        Set< Integer > s = moveDfa( st, input );
                        
                        if ( !s.isEmpty() ){
                        
                            ts = new TransitionState( s ); 
                            
                            if ( dfaStates.contains( ts ) ) {
                                ts = ArrayUtils.get( ts, dfaStates );
                            } else if ( tempStates.contains( ts ) ){
                                ts = ArrayUtils.get( ts, tempStates );
                            } else {
                                ts.setStateNumber( numStates++ );
                                tempStates.add( ts );
                            }
                            
                            DfaState dest = statesMap.get( ts.getStateNumber() );
                            if ( dest == null ){
                                dest = new DfaState( ts.getStateNumber() );
                                
                                statesMap.put( ts.getStateNumber(), dest );
                            }

                            DfaState dfa = statesMap.get( st.getStateNumber() );
                            if ( dfa == null ){
                                dfa = new DfaState( st.getStateNumber() );
                                dfa.setStart( st.isStartState() );
                                
                                statesMap.put( st.getStateNumber(), dfa );
                            }
                            dfa.put( input, dest );
                            log.debug( "Creating transition for state " + dfa.getNumber() + " - input: " + input + " - dest: " + dest.getNumber() );

                        }
                        
                    }
                    
                }
            }
            
            dfaStates.addAll( tempStates );
            
        }

        for ( TransitionState st : dfaStates ){
            if ( st.getStates().contains( finalState ) ){
                st.setFinalState( true );
                DfaState dfa = statesMap.get( st.getStateNumber() );
                dfa.setEnd( true );
                log.debug( "is end state: " + dfa.getNumber() );
            }
        }
        
        int maxInput = -1;
        
        for ( int input : inputs ){
            if ( input > maxInput ){
                maxInput = input;
            }
        }
        
        log.debug( "states size: " + statesMap.size() );
        log.debug( "max input: " + maxInput );
        this.dfaStates = new int[statesMap.size()+1][maxInput+100];
        this.accept = new boolean[statesMap.size()+1];
        this.special = new int[statesMap.size()+1];
        
        for ( int stateNumber : statesMap.keySet() ){
            DfaState st = statesMap.get( stateNumber );
            if ( st.isEnd() ){
                this.accept[st.getNumber()] = true;
            } else {
                this.accept[st.getNumber()] = false;
            }
            for ( int input : st.getStates().keySet() ){
                DfaState s = st.getState( input );
                if ( input < 0 ){
                    DfaStateHolder h = dfaOperations.remove( input );
                    if ( h == null ){
                        continue;
                    }
                    //otherwise it has been processed already
                    input = maxInput - input;
                    dfaOperations.put( input, h );
                    log.debug( "checking special state " + st.getNumber() + " with input: " + input );
                    special[st.getNumber()] = input;
                }
                
                this.dfaStates[st.getNumber()][input] = s.getNumber();
            }
        }
        
        return new DfaStateHolder( statesMap.get( startStateNumber ), dfaOperations, startStateNumber, this.dfaStates, this.accept, this.special );
    }

   private Set< Integer > moveDfa( TransitionState ts, Integer input ){
        
        Set< Integer > states = new HashSet< Integer >();
        
        Set< NfaState > nfaStates = moveNfa( ts, input );
        
        for ( NfaState nfaState : nfaStates ){
            states.addAll( closure( nfaState.getNumber() ) );
        }
            
        return states;
    }
   
   
   private Set< NfaState > moveNfa( TransitionState ts, Integer input ){
       
       Set< NfaState > states = new HashSet< NfaState >();
       
       for ( Integer state : ts.getStates() ){
           
           NfaState holder = this.states.get( state );
           if ( holder.getDestinations( input ) != null ){
               states.addAll( holder.getDestinations( input ) );
           }
           
       }
    
       return states;
   }
   
   private Set< Integer > closure( int state ){
       
       Set< Integer > states = new HashSet< Integer >();
       
       states.add( state );
       NfaState nfa = this.states.get( state );
       
       if ( nfa != null && !nfa.isEnd() ){
           for ( NfaState st : nfa.getEpsilonDestinations() ){
               states.addAll( closure( st.getNumber() ) );
           }
       }
       
       return states;
   }
   
   public Map< Integer, NfaState > getStates() {
   
       return states;
   }


   
   public void setStates( Map< Integer, NfaState > states ) {
   
       this.states = states;
   }

   
   public int getFinalState() {
   
       return finalState;
   }

   
   public boolean isFromStart() {
   
       return fromStart;
   }

   
   public boolean isToEnd() {
   
       return toEnd;
   }

    
}