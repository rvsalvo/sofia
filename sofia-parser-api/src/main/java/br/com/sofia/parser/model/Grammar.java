package br.com.sofia.parser.model;

import java.util.ArrayList;
import java.util.List;


public class Grammar {
    
    private String variable;
    private String symbol;
    private Grammar parent;
    private Rule rule;
    private Item itemTo;
    private Item itemFrom;
    private List< Grammar > childs = new ArrayList< Grammar >();
    
    public String getVariable() {
        
        if ( variable == null ){
            return "";
        }
    
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
    
    public Grammar getParent() {
    
        return parent;
    }
    
    public void setParent( Grammar parent ) {
    
        this.parent = parent;
    }
    
    public List< Grammar > getChilds() {
    
        return childs;
    }
    
    public void setChilds( List< Grammar > childs ) {
    
        this.childs = childs;
    }

    
    public Rule getRule() {
    
        return rule;
    }

    
    public void setRule( Rule rule ) {
    
        this.rule = rule;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( rule == null ) ? 0 : rule.hashCode() );
        result = prime * result + ( ( symbol == null ) ? 0 : symbol.hashCode() );
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
        Grammar other = (Grammar) obj;
        if ( rule == null ) {
            if ( other.rule != null )
                return false;
        }
        else if ( !rule.getLeft().equals( other.rule.getLeft() ) || !rule.getRight().equals( other.rule.getRight() ) )
            return false;
        if ( symbol == null ) {
            if ( other.symbol != null )
                return false;
        }
        else if ( !symbol.equals( other.symbol ) )
            return false;
        if ( variable == null ) {
            if ( other.variable != null )
                return false;
        }
        else if ( !variable.equals( other.variable ) )
            return false;
        return true;
    }

    @Override
    public String toString() {

        return "Grammar [variable=" + variable + ", symbol=" + symbol + ", rule=" + rule + "]";
    }

    
    public Item getItemTo() {
    
        return itemTo;
    }

    
    public void setItemTo( Item itemTo ) {
    
        this.itemTo = itemTo;
    }

    
    public Item getItemFrom() {
    
        return itemFrom;
    }

    
    public void setItemFrom( Item itemFrom ) {
    
        this.itemFrom = itemFrom;
    }

    public String buildText(){
        StringBuilder builder = new StringBuilder( "\n" );
        builder.append( "(" + getVariable() );
        buildGrammar( getChilds(), builder );
        builder.append( ")" );
        return builder.toString();        
    }
    
    private void buildGrammar( List< Grammar > list, StringBuilder builder ) {
        
        for ( Grammar g : list ){
            if ( g.getSymbol() != null && ( g.getChilds() == null || g.getChilds().isEmpty() ) ){
                builder.append( "(" + g.getVariable() );                
                builder.append( " " + g.getSymbol() + ")" );
            } else {
                builder.append( "(" + g.getVariable() );                
                buildGrammar( g.getChilds(), builder );
                builder.append( ")" );
            }
        }
    }   
    
    
    private void buildGrammarSymbols( List< Grammar > list, List< GrammarInfo > symbols ) {
        
        for ( Grammar g : list ){
            if ( g.getSymbol() != null && ( g.getChilds() == null || g.getChilds().isEmpty() ) ){
                GrammarInfo info = new GrammarInfo();
                info.setSymbol( g.getSymbol() );
                info.setVariable( g.getVariable() );
                symbols.add( info );
            } else {
                buildGrammarSymbols( g.getChilds(), symbols );
            }
        }
    }   
        
    
    public List< GrammarInfo > getSymbolList(){
        
        List< GrammarInfo > symbolList = new ArrayList< GrammarInfo >();
        
        buildGrammarSymbols( this.getChilds(), symbolList );
        
        return symbolList;
        
    }
    
}
