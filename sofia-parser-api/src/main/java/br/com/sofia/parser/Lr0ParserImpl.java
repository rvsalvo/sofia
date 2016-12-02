package br.com.sofia.parser;


import static br.com.sofia.parser.util.GrammarUtil.TERMINAL_SYMBOL;
import static br.com.sofia.parser.util.GrammarUtil.WHITESPACE_SYMBOL;
import static br.com.sofia.parser.util.GrammarUtil.getSymbol;
import static br.com.sofia.parser.util.GrammarUtil.getSymbolPosition;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import br.com.sofia.parser.exception.RuleConflictException;
import br.com.sofia.parser.model.GrammarFile;
import br.com.sofia.parser.model.Item;
import br.com.sofia.parser.model.Rule;
import br.com.sofia.parser.model.Stack;
import br.com.sofia.parser.model.Table;
import br.com.sofia.parser.model.Action;
import br.com.sofia.parser.util.CollectionUtil;
import br.com.sofia.parser.util.GrammarUtil;

/**
 * 
 * @author Rodrigo Salvo
 * 
 */
public class Lr0ParserImpl implements LRParser {

    protected Item root = null;
    
    protected List< Rule > rules = new ArrayList< Rule >();

    protected Rule initialRule = null;

    protected Map< String, String > action = new HashMap< String, String >();

    protected Map< String, Integer > goTo = new HashMap< String, Integer >();
    
    protected Set< String > variables = new HashSet< String >();
    
    private List< Action > tags = new ArrayList< Action >();

    public Table createTable( GrammarFile file ) {

        // get rules from specified file
        rules = getRulesList( file.getFile() );
        
        tags = getTagsList( file.getActionFile() );

        for ( Rule rule : rules ){
            System.out.println( "Rule: " + rule.toString() );
        }
        
        Set< Item > items = getRules();
        
        Table table = new Table();
        table.setAction( action );
        table.setGoTo( goTo );
        table.setItem( root );
        table.setRules( rules );

        for ( Item item : items ){
            table.getItems().put( item.getState(), item );
        }
        table.getItems().put( root.getState(), root );
        table.setInitialRule( initialRule );
        table.setTags( tags );
        
        return table;

    }


    public void logFolllow( String symbol ) {

        Set< String > set = follow( symbol );
        
        System.out.print( "Follow ("+symbol+") = { " );
        for ( String s : set ){
            System.out.print( s + WHITESPACE_SYMBOL );
        }
        System.out.print( "}" );
        
        System.out.println( "" );
    }


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
                rules.add( createRule( strLine ) );
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

    protected List< Action > getTagsList( String file ) {

        try {

            List< Action > tags = new ArrayList< Action >();

            InputStream is = this.getClass().getResourceAsStream( file );

            BufferedReader br = new BufferedReader( new InputStreamReader( is ) );
            String strLine;
            // Read File Line By Line
            while ( ( strLine = br.readLine() ) != null ) {
                // Print the content on the console
                System.out.println( "Read line: " + strLine );
                tags.add( createTag( strLine ) );
            }
            // Close the input stream
            is.close();

            return tags;

        }
        catch ( IOException ex ) {
            ex.printStackTrace();
        }

        return new ArrayList< Action >();

    }
    
    protected Set< Item > getRules() {
        
        List< Item > grammar = new ArrayList< Item >();
        
        Set< Item > processedGrammar = new HashSet< Item >();
        
        List< Item > tempGrammar = new ArrayList< Item >();        
        
        AtomicInteger counter = new AtomicInteger( 0 );

        Item initialState = createFirstState( counter );
        
        grammar.add( initialState );        

        boolean done = false;

        while ( !done ) {

            done = true;
            
            for ( Item currentItem : grammar ){

                for ( Rule rule : currentItem.getRules() ) {
    
                    String nextSymbol = getSymbol( rule.getRight(), rule.getIndex() );
    
                    if ( nextSymbol == null || TERMINAL_SYMBOL.equals( nextSymbol ) ) {
                        rule.setEndPosition( true );
                        continue;
                    }
    
                    Set< Rule > set = goTo( currentItem, nextSymbol );
    
                    Item it = createItem( set, 0, nextSymbol, currentItem );
                    if ( !processedGrammar.contains( it ) ) {
    
                        done = false;
                        it.setState( counter.incrementAndGet() );
                        processedGrammar.add( it );                        
                        tempGrammar.add( it );
                        currentItem.getChilds().add( it );
                        GrammarUtil.writeItem( it );
                        
                    }
                    else {
                        //if exists I have to check the lookahead
                        
                        Item itAux = getFromGrammar( it, processedGrammar );
                        for ( Rule ruleAux : itAux.getRules() ){
                            Rule other = CollectionUtil.getObjectFromList( ruleAux, it.getRules() );
                            ruleAux.getLookAheads().addAll( other.getLookAheads() );
                        }
                        
                        it = itAux;
                    }
                    
                    createTable( currentItem, nextSymbol, it );
    
                }
                
                tempGrammar.remove( currentItem );
            
            }

            grammar = new ArrayList< Item >();
            grammar.addAll( tempGrammar );
            
        }
        
        return processedGrammar;
        
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
                throw new RuleConflictException( "This is not a LR(0) Grammar. Action " + key + " has rule " + command + " when trying to insert rule " + newRule );
            }
            action.put( key, newRule );
        }
        else {
            goTo.put( currentItem.getState() + ":" + nextSymbol, it.getState() );
        }
        for ( Rule r : it.getRules() ) {
            if ( r.isEndPosition() && !isRuleEqual( initialRule, r ) ) {
                Set< String > group = follow( r.getLeft() );
                for ( String s : group ) {
                    String key = it.getState() + ":" + s;
                    String newRule = "r " + getRuleIndex( r );
                    String command = action.get( key );
                    if ( command != null && !command.equals( newRule ) ){
                        throw new RuleConflictException( "This is not a LR(0) Grammar. Action " + key + " has rule " + command + " when trying to insert rule " + newRule );
                    }
                    action.put( key, newRule );
                }
            }
        }
    }
    
    protected boolean hasRule( List< Rule > rules, Rule rule ){
        for ( Rule r : rules ){
            if ( isRuleEqual( rule, r ) ){
                return true;
            }
        }
        return false;
    }


    protected boolean isRuleEqual( Rule rule, Rule r ) {

        return r.getLeft().equals( rule.getLeft() ) && r.getRight().equals( rule.getRight() );
    }

    protected Item createFirstState( AtomicInteger counter ) {

        Rule init = rules.get( 0 );
        initialRule = new Rule();
        initialRule.setLeft( init.getLeft() );
        initialRule.setRight( init.getRight() );
        initialRule.getLookAheads().add( TERMINAL_SYMBOL );
        initialRule.setIndex( 0 );

        List< Rule > closureRules = closure( init );
        Item item = new Item();
        item.setRules( closureRules );
        item.setState( counter.get() );

        GrammarUtil.writeItem( item );

        root = item;
        
        return item;
    }


    protected int getRuleIndex( Rule rule ) {

        for ( int i = 0; i < rules.size(); i++ ) {
            Rule r = rules.get( i );
            if ( isRuleEqual( r, rule ) ) {
                return i;
            }
        }
        return -1;
    }


    protected Set< String > follow( String symbol ) {

        return followSymbol( symbol, new HashSet< String >() );
    }
    
    protected Set< String > first( String symbol ) {

        return firstSymbol( symbol, new HashSet< String >() );
    }    
    
    protected Set< String > firstSymbol( String symbol, Set< String > analized ) {

        Set< String > set = new HashSet< String >();
        
        if ( symbol == null ){
            return set;
        }
        
        if ( isTerminal( symbol ) ) { 
            set.add( symbol );
            return set;
        } else {
            analized.add( symbol );
        }
        
        for ( Rule rule : rules ){
            if ( rule.getLeft().equals( symbol ) ){
                String right = getSymbol( rule.getRight(), 0 );
                if ( !analized.contains( right ) ){
                    set.addAll( firstSymbol( right, analized ) );
                }
            }
        }
        
        return set;
        
    }


    protected Set< String > followSymbol( String symbol, Set< String > analized ) {

        Set< String > set = new HashSet< String >();
        
        set.add( TERMINAL_SYMBOL );

        analized.add( symbol );

        for ( Rule r1 : rules ) {
            
            Rule rule = cloneRule( r1 );
            
            int index = getSymbolPosition( rule.getRight(), symbol );
            //int symbolIndex = index;
            if ( index >= 0 ) {

                index++;
                String s = getSymbol( rule.getRight(), index );
                
                if ( s != null ){
                    Set< String > firstSymbols = first( s );
                    if ( firstSymbols.isEmpty() && ( !analized.contains( rule.getLeft() ) ) ){
                        set.addAll( followSymbol( rule.getLeft(), analized ) );
                    } else { 
                        set.addAll( firstSymbols );
                    }
                    
                } else if ( !analized.contains( rule.getLeft() ) ) {                    
                    set.addAll( followSymbol( rule.getLeft(), analized ) );
                }

            }
        }

        return set;

    }


    public void logAction() {

        for ( String key : action.keySet() ) {
            String value = action.get( key );
            System.out.println( "Action I" + key + " -> " + value );
        }

    }


    public void logGoTo() {

        for ( String key : goTo.keySet() ) {
            Integer value = goTo.get( key );
            System.out.println( "GOTO I" + key + " -> " + value );
        }

    }


    protected Item getFromGrammar( Item it, Set< Item > grammar ) {

        for ( Item item : grammar ) {
            if ( item.equals( it ) ) {
                return item;
            }
        }
        return null;
    }



    /**
     * @param item
     * @param symbol
     * @return
     */
    protected Set< Rule > goTo( Item item, String symbol ) {

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
                    
                    closureRules.addAll( set );
                    
                }
            }

        }

        return closureRules;

    }


    protected Rule createRule( String value ) {

        String[] parts = value.split( "->" );

        String start = parts[ 0 ].trim();

        Rule rule0 = new Rule();
        rule0.setIndex( 0 );
        rule0.setLeft( start );
        String right = parts[ 1 ].trim() + TERMINAL_SYMBOL;
        rule0.setRight( right );
        rule0.getLookAheads().add( TERMINAL_SYMBOL );
        
        variables.add( rule0.getLeft() );

        return rule0;

    }

    protected Action createTag( String value ) {

        String[] parts = value.split( " " );

        String action = parts[ 0 ].trim();
        
        List< String > components = new ArrayList< String >();

        Action tg = new Action();
        tg.setAction( action );
        
        for ( int i = 1; i < parts.length; i++ ){
            String cp = parts[ i ].trim();
            components.add( cp );
        }
        tg.setComponents( components );
        
        return tg;

    }
 
    protected List< Rule > closure( Rule start ) {

        String symbol = getSymbol( start.getRight(), start.getIndex() );

        Stack< String > transitions = new Stack< String >();

        List< Rule > closureRules = new ArrayList< Rule >();
        closureRules.add( start );

        boolean done = false;

        while ( !done ) {

            if ( !isTerminal( symbol ) ) {
                for ( Rule rule : rules ) {
                    if ( rule.getLeft().equals( symbol ) ) {
                        if ( !closureRules.contains( rule ) ) {
                            Rule cloneRule = cloneRule( rule );
                            closureRules.add( cloneRule );
                            String transition = getSymbol( rule.getRight(), 0 );
                            if ( !transition.equals( symbol ) && !isTerminal( transition ) ) {
                                transitions.push( transition );
                            }
                        }
                    }
                }

            }

            symbol = transitions.pop();

            if ( symbol == null ) {
                done = true;
            }

        }

        return closureRules;
    }


    protected Rule cloneRule( Rule rule ) {

        Rule cloneRule = new Rule();
        cloneRule.setLeft( rule.getLeft() );
        cloneRule.setRight( rule.getRight() );
        cloneRule.setIndex( 0 );
        cloneRule.setLookAheads( new HashSet< String >( rule.getLookAheads() ) );
        return cloneRule;
    }


    protected Item createItem( Set< Rule > rules, int state, String symbol, Item parent ) {

        Item item = new Item();
        List< Rule > r = new ArrayList< Rule >();
        r.addAll( rules );
        item.setRules( r );
        item.setSymbol( symbol );
        item.setParent( parent );
        item.setState( state );

        return item;

    }


    public boolean isTerminal( String value ) {

        if ( TERMINAL_SYMBOL.equals( value ) ) {
            return true;
        }

        return !variables.contains( value );

    }
    
    public List< Rule > getGrammarRules(){
        return this.rules;
    }


    
    public List< Action > getTags() {
    
        return tags;
    }

}
