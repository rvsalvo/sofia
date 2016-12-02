package br.com.sofia.parser;

import static br.com.sofia.parser.util.GrammarUtil.getSymbol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.sofia.parser.model.Grammar;
import br.com.sofia.parser.model.GrammarInfo;
import br.com.sofia.parser.model.Item;
import br.com.sofia.parser.model.ProcessorResult;
import br.com.sofia.parser.model.Rule;
import br.com.sofia.parser.model.Stack;
import br.com.sofia.parser.model.State;
import br.com.sofia.parser.model.Table;
import br.com.sofia.parser.model.Action;
import br.com.sofia.parser.util.CollectionUtil;
import br.com.sofia.parser.util.GrammarUtil;


public class LRAnalizerImpl {
    
    private LRParser parser;
    
    private Stack< Grammar > grammarTokens;
    
    private Item actualItemFrom;
    
    private Item actualItemTo;
    
    private Table table;
    
    protected Map< String, String > bindingVariables = new HashMap< String, String >();
    
    public LRAnalizerImpl( Table table, LRParser parser ){
        this.table = table;
        this.parser = parser;
        
    }
    
    public boolean analize( String expression ){
        
        grammarTokens = new Stack< Grammar >();
        actualItemFrom = null;
        actualItemTo = null;
        
        System.out.println( "" );
        System.out.println( "" );
        
        expression += GrammarUtil.WHITESPACE_SYMBOL + GrammarUtil.TERMINAL_SYMBOL;
        
        Stack< State > stack = new Stack< State >();
        
        State current = new State();
        current.setStateNumber( 0 );
        stack.push( current );
        
        int position = 0;
        
        String concatSymbol = null;
        
        while ( true ){
        
            String symbol = getSymbol( expression, position );
            
            String key = current.getStateNumber() + ":" + symbol;
            
            System.out.println( "Processing symbol: " + symbol + " key = " + key );
            
            String action = table.getAction().get( key );
            
            if ( action == null ){
                
                String newSymbol = "";
                
                int positionAux = position;
                
                String actionAux = action;
                
                while ( actionAux == null && newSymbol != null ){

                    position++;
                    
                    newSymbol = getSymbol( expression, position );

                    key = current.getStateNumber() + ":" + newSymbol;
                    
                    actionAux = table.getAction().get( key );
                    
                    if ( GrammarUtil.TERMINAL_SYMBOL.equals( newSymbol ) || actionAux != null ){
                        break;
                    }

                    symbol += newSymbol != null ? GrammarUtil.WHITESPACE_SYMBOL + newSymbol : "";
                    
                    positionAux++;
                    
                }
                
                position = positionAux;                
                
                for ( Rule r1 : actualItemTo.getRules() ){

                    int pos = GrammarUtil.getSymbolPosition( r1.getRight(), current.getSymbol() );
                    if ( r1.getIndex() == pos + 1 ){
                        
                        for ( String var : r1.getLookAheads() ){
                            if ( var.indexOf( "#" ) == 0 ){
                                
                                bindingVariables.put( symbol, var );
                                concatSymbol = symbol;
                                
                                symbol = var;
                                
                                key = current.getStateNumber() + ":" + symbol;
                                
                                System.out.println( "Processing binding variable: " + symbol + " key = " + key );
                                
                                action = table.getAction().get( key );
                                
                                break;
                                
                            }
                        }
                        
                    }
                    
                }
                
                if ( action == null ){
                    
                    if ( concatSymbol != null && concatSymbol.indexOf( symbol ) >= 0 ){
                        symbol = bindingVariables.get( concatSymbol );
                    } else {
                        symbol = bindingVariables.get( symbol );
                    }
                    key = current.getStateNumber() + ":" + symbol;
                    
                    System.out.println( "Processing binding variable: " + symbol + " key = " + key );
                    
                    action = table.getAction().get( key );                    
                }
            }
            
            if ( action == null ){
                System.err.println( "Error validating grammar for key: " + key );
                break;
            } else if ( action.indexOf( "r " ) == 0 ){
                
                int ruleIndex = Integer.parseInt( action.substring( 2 ) );
                
                Rule rule = table.getRules().get( ruleIndex );
                
                symbol = rule.getLeft();
                
                System.out.println( "Reduce for Rule index: " + ruleIndex + " rule: " + rule.toString() + " - Generated left side: " + symbol );

                Grammar grammar = new Grammar();
                grammar.setRule( rule );
                grammar.setItemTo( actualItemTo );
                grammar.setItemFrom( actualItemFrom );
                grammar.setSymbol( current.getSymbol() );
                grammar.setVariable( symbol );
                
                grammarTokens.push( grammar );
                
                int size = GrammarUtil.getRulePositions( rule.getRight() ) - 1;
                
                System.out.println( "REDUCE Current stack info: [" + current.toString() + "]" );
                
                while ( size > 0 ){
                    stack.pop();
                    size--;
                }
                
                current = stack.pop();
                key = current.getStateNumber() + ":" + symbol;
                
                int nextState = table.getGoTo().get( key );
                
                Item from = table.getItems().get( current.getStateNumber() );
                Item to = table.getItems().get( nextState );
                
                System.out.println( "####GOTO: Symbol = " + symbol + " Going from state: I" + current.getStateNumber() + " " + GrammarUtil.logItem( from ) +   " to state: I" + nextState  + " " + GrammarUtil.logItem( to ) );

                stack.push( current );
                
                State st = new State();
                st.setStateNumber( nextState );
                st.setSymbol( symbol );
                stack.push( st );
                current = st;                
                
            } else if ( action.equals( "acc" ) ){
                return true;
            } 
            
            if ( action.indexOf( "s " ) == 0 ){
                
                int nextState = Integer.parseInt( action.substring( 2 ) );
                
                Item from = table.getItems().get( current.getStateNumber() );
                Item to = table.getItems().get( nextState );                
                
                System.out.println( "Shifting from state: I" + current.getStateNumber() + " " + GrammarUtil.logItem( from ) +   " to state: I" + nextState  + " " + GrammarUtil.logItem( to ) );
                
                actualItemFrom = from;
                actualItemTo = to;
                
                State st = new State();
                st.setStateNumber( nextState );
                st.setSymbol( symbol );
                stack.push( st );
                current = st;

                position++;
                
            } 
            
            System.out.println( "Current stack info: [" + current.toString() + "]" );
            
        }
    
        return false;
    }
    
    public String analizeResult( ProcessorResult result ){
        
        if ( result == null ){
            return "I am sorry, I could not process your request. Try something different";
        }
        
        if ( !result.isResult() ){
            return "I know you are trying to " + result.getVerb() + " something, but there is something that I did not understood on your request.";
        }

        StringBuilder builder = new StringBuilder();
        for ( String word : result.getTerms() ){
            builder.append( word ).append( GrammarUtil.WHITESPACE_SYMBOL );
        }
        
        return builder.toString();
    }
    
    public Grammar getGrammar(){
        
        Grammar root = null;
        
        Stack< Grammar > grammar = new Stack< Grammar >();
        for ( Grammar g : grammarTokens.collection() ){
            grammar.push( g );
        }
        grammar.reverse();
        
        Stack< Grammar > tempGrammar = new Stack< Grammar >();
        
        while ( grammar.getSize() > 0 ){
            Grammar g = grammar.pop();
            if ( !parser.isTerminal( g.getSymbol() ) ){
                int size = GrammarUtil.getRulePositions( g.getRule().getRight() ) - 1;
                while ( size > 0 ){
                    Grammar g1 = tempGrammar.pop();
                    g.getChilds().add( g1 );
                    size--;
                }
                Collections.reverse( g.getChilds() );
            } 
            tempGrammar.push( g );
        }
        
        root = tempGrammar.pop();
        
        if ( root.getParent() == null ){
            
            boolean done = false;
            
            while ( !done ){
            
                for ( Rule r1 : table.getRules() ){
                    int pos = GrammarUtil.getSymbolPosition( r1.getRight(), !root.getVariable().isEmpty() ? root.getVariable() : root.getSymbol() );
                    if ( pos >= 0 ){
                        Grammar gr = new Grammar();
                        gr.setRule( r1 );
                        gr.setVariable( r1.getLeft() );
                        gr.setSymbol( root.getVariable() );
                        root.setParent( gr );
                        gr.getChilds().add( root );
                        break;
                    }
                }

                if ( root.getParent() != null ){
                    root = root.getParent();
                } else {
                    done = true;
                }
            }
        }
        
        
        return root;
    }
    
    public String getBasicResponse(){
        
        String[] response = new String[]{ "Ok...", "Yes...", "Processing..." };
        
        return response[(int)(Math.random()*3)];
    }
    
    public Action analizeActions( Grammar grammar ){
        
        List< Action > tags = table.getTags();
        
        List< Action > analizedActions = new ArrayList< Action >();
        
        List< String > binding = new ArrayList< String >();
        
        List< GrammarInfo > list = grammar.getSymbolList();
        
        for ( Action tag : tags ){
            
            int lastIndex = -1;   
            binding = new ArrayList< String >();
            
            for ( String comp : tag.getComponents() ){

                GrammarInfo info = new GrammarInfo();
                info.setVariable( comp );
                int index = CollectionUtil.getObjectIndex( info, list );
                
                if ( index > lastIndex ){
                    info = list.get( index );
                    String value = null;
                    for ( String key : bindingVariables.keySet() ){
                        String content = bindingVariables.get( key );
                        if ( content.equals( info.getSymbol() ) ){
                            value = key;
                            break;
                        }
                    }
                    binding.add( value != null ? value : info.getSymbol() );
                    lastIndex = index;
                }
                
            }

            if ( binding.size() == tag.getComponents().size() ){
                tag.setWords( binding );
                analizedActions.add( tag );
            }
            
        }
        
        int lastSize = -1;
        Action theAction = null;
        
        for ( Action act : analizedActions ){
            if ( act.getWords().size() > lastSize ){
                theAction = act;
                lastSize = act.getWords().size();
            }
        }
        
        return theAction;
    }

    
    public Stack< Grammar > getGrammarTokens() {
    
        return grammarTokens;
    }

    
}
