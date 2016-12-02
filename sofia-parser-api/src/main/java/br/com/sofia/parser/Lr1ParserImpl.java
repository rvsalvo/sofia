package br.com.sofia.parser;


import static br.com.sofia.parser.util.GrammarUtil.TERMINAL_SYMBOL;
import static br.com.sofia.parser.util.GrammarUtil.getSymbol;
import static br.com.sofia.parser.util.GrammarUtil.getSymbolPosition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import br.com.sofia.parser.exception.RuleConflictException;
import br.com.sofia.parser.model.Item;
import br.com.sofia.parser.model.Rule;
import br.com.sofia.parser.model.Stack;
import br.com.sofia.parser.model.Transition;
import br.com.sofia.parser.util.CollectionUtil;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class Lr1ParserImpl extends Lr0ParserImpl {
    
    protected List< Rule > getRulesList( String file ) {

        try {

            List< Rule > rules = new ArrayList< Rule >();

            InputStream is = this.getClass().getResourceAsStream( file );

            BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
            String strLine;
            // Read File Line By Line
            while ( ( strLine = br.readLine() ) != null ) {
                // Print the content on the console
                System.out.println( "Read line: " + strLine );
                rules.addAll( createRules( strLine ) );
            }
            // Close the input stream
            is.close();

            return rules;

        }
        catch ( IOException ex ) {
            ex.printStackTrace();
        }

        return new ArrayList< Rule >();

    }
    
    protected List< Rule > createRules( String value ) {

        List< Rule > rules = new ArrayList< Rule >(); 
        
        String[] parts = value.split( "->" );

        String start = parts[ 0 ].trim();

        variables.add( start );
        
        for ( String rightPart : parts[1].split( "\\|" ) ){

            Rule rule0 = new Rule();
            rule0.setIndex( 0 );
            rule0.setLeft( start );
            String right = rightPart.trim() + TERMINAL_SYMBOL;
            rule0.setRight( right );
            rule0.getLookAheads().add( TERMINAL_SYMBOL );
            rules.add( rule0 );
        }
        
        return rules;

    }
    
    
    /**
     * @param item
     * @param symbol
     * @return
     */
    protected Set< Rule > goTo( Item item, String symbol ) {
        
        System.out.println( "\ngo to from state " + item.getState() + " with symbol " + symbol );

        Set< Rule > closureRules = new HashSet< Rule >();

        for ( Rule r : item.getRules() ) {
            
            int position = r.getIndex();

            int index = getSymbolPosition( r.getRight(), symbol );

            if ( index >= 0 && index <= position ) {

                index++;

                String s = getSymbol( r.getRight(), index );

                if ( s != null ) {

                    Rule r1 = cloneRule( r );
                    r1.setIndex( index );
                    
                    if ( TERMINAL_SYMBOL.equals( s ) ){
                        r1.setEndPosition( true );
                    }
                    
                    List< Rule > set = closure( r1 );
                    
                    //have to do that otherwise can miss some lookahead
                    for ( Rule rule : set ){
                        if ( closureRules.contains( rule ) ){
                            Rule closureRule = CollectionUtil.getObjectFromSet( rule, closureRules );
                            closureRule.getLookAheads().addAll( rule.getLookAheads() );
                        } else {
                            closureRules.add( rule );                            
                        }
                    }

                }
            }

        }

        return closureRules;

    }
    

    protected List< Rule > closure( Rule start ) {

        String symbol = getSymbol( start.getRight(), start.getIndex() );
        
        String nextSymbol = getSymbol( start.getRight(), start.getIndex() + 1 );
        
        Set< String > lookAheads = first( nextSymbol );
        
        Stack< Transition > transitions = new Stack< Transition >();
        
        Transition tr = new Transition();
        tr.setSymbol( symbol );
        tr.setLookAheads( start.getLookAheads() );
        tr.getLookAheads().addAll( lookAheads );

        List< Rule > closureRules = new ArrayList< Rule >();
        closureRules.add( start );

        boolean done = false;
        
        Set< Rule > processed = new HashSet< Rule >();
        
        while ( !done ) {

            if ( !isTerminal( symbol ) ) {
                for ( Rule rule : rules ) {
                    if ( rule.getLeft().equals( symbol ) ) {
                        if ( !closureRules.contains( rule ) ) {
                            Rule cloneRule = cloneRule( rule );
                            cloneRule.getLookAheads().addAll( tr.getLookAheads() );
                            closureRules.add( cloneRule );
                            String transition = getSymbol( rule.getRight(), 0 );

                            nextSymbol = getSymbol( rule.getRight(), 1 );
                            
                            lookAheads = first( nextSymbol );                                
                            
                            if ( !isTerminal( transition ) ) {

                                //TODO: should I need the start lookaheads?
                                //lookAheads.addAll( start.getLookAheads() );
                                lookAheads.addAll( tr.getLookAheads() );
                                Transition t = new Transition();
                                t.setSymbol( transition );
                                
                                if ( transitions.contains( t ) ){
                                    
                                    t = transitions.peek( t );
                                    t.getLookAheads().addAll( lookAheads );
                                    
                                } else if ( !processed.contains( rule ) ){
                                    t.setLookAheads( lookAheads );
                                    transitions.push( t );
                                    processed.add( rule );
                                }
                            }
                        } else {
                            for ( Rule r : closureRules ){
                                if ( r.equals( rule ) ){
                                    r.getLookAheads().addAll( tr.getLookAheads() );
                                }
                            }
                        }
                    }
                }

            }

            tr = transitions.pop();

            if ( tr == null ) {
                done = true;
            } else {
                symbol = tr.getSymbol();
            }

        }

        return closureRules;
    }
    
    protected void createTable( Item currentItem, String nextSymbol, Item it ) {

        if ( hasRule( it.getRules(), initialRule ) ) {
            action.put( it.getState() + ":" + TERMINAL_SYMBOL, "acc" );
        }
        
        if ( isTerminal( nextSymbol ) ) {
            String key = currentItem.getState() + ":" + nextSymbol;
            String newRule = "s " + it.getState();
            String command = action.get( key );
            if ( command != null && !command.equals( newRule ) ){
                if ( command.indexOf( "s " ) > 0 ){
                    throw new RuleConflictException( "This is not a LR(1) Grammar. Action " + key + " has rule " + command + " when trying to insert rule " + newRule );
                } else {
                    System.out.println( "#### WARNING #######: Rule " + newRule + " skipped because it already contains a reduce rule: " + command );
                }
            }
            action.put( key, newRule );
        }
        else {
            goTo.put( currentItem.getState() + ":" + nextSymbol, it.getState() );
        }
        for ( Rule r : it.getRules() ) {
            if ( r.isEndPosition() && !isRuleEqual( initialRule, r ) ) {
                Set< String > group = r.getLookAheads();
                for ( String s : group ) {
                    String key = it.getState() + ":" + s;
                    String newRule = "r " + getRuleIndex( r );
                    String command = action.get( key );
                    if ( command != null && !command.equals( newRule ) ){
                        throw new RuleConflictException( "This is not a LR(1) Grammar. Action " + key + " has rule " + command + " when trying to insert rule " + newRule );
                    }
                    action.put( key, newRule );
                }
            }
        }
    }
    
    
    
}
