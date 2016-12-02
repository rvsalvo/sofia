package br.com.sofia.parser.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Item {
    
    private Item parent;
    
    private Set< Item > childs = new HashSet< Item >();
    
    private Rule itemRule;
    
    private int state;
    
    private String symbol;
    
    private List< Rule > rules;

    
    public Item getParent() {
    
        return parent;
    }

    
    public void setParent( Item parent ) {
    
        this.parent = parent;
    }

    
    public Set< Item > getChilds() {
    
        return childs;
    }

    
    public void setChilds( Set< Item > childs ) {
    
        this.childs = childs;
    }

    
    public int getState() {
    
        return state;
    }

    
    public void setState( int state ) {
    
        this.state = state;
    }

    
    public String getSymbol() {
    
        return symbol;
    }

    
    public void setSymbol( String symbol ) {
    
        this.symbol = symbol;
    }


    
    public List< Rule > getRules() {
    
        return rules;
    }


    
    public void setRules( List< Rule > rules ) {
    
        this.rules = rules;
    }


    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( rules == null ) ? 0 : rules.hashCode() );
        result = prime * result + ( ( symbol == null ) ? 0 : symbol.hashCode() );
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
        Item other = (Item) obj;
        if ( rules == null ) {
            if ( other.rules != null )
                return false;
        }
        else if ( !rules.equals( other.rules ) )
            return false;
        if ( symbol == null ) {
            if ( other.symbol != null )
                return false;
        }
        else if ( !symbol.equals( other.symbol ) )
            return false;
        return true;
    }


    
    public Rule getItemRule() {
    
        return itemRule;
    }


    
    public void setItemRule( Rule itemRule ) {
    
        this.itemRule = itemRule;
    }

}
