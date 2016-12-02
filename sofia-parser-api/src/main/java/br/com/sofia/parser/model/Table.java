package br.com.sofia.parser.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Table {
    
    private Map< String, String > action = new HashMap< String, String >();

    private Map< String, Integer > goTo = new HashMap< String, Integer >();
    
    private Item item;
    
    private Map< Integer, Item > items = new HashMap< Integer, Item >();
    
    private List< Rule > rules;
    
    private List< Action > tags;
    
    private Rule initialRule;

    
    public Map< String, String > getAction() {
    
        return action;
    }

    
    public void setAction( Map< String, String > action ) {
    
        this.action = action;
    }

    
    public Map< String, Integer > getGoTo() {
    
        return goTo;
    }

    
    public void setGoTo( Map< String, Integer > goTo ) {
    
        this.goTo = goTo;
    }


    
    public Item getItem() {
    
        return item;
    }


    
    public void setItem( Item item ) {
    
        this.item = item;
    }


    
    public List< Rule > getRules() {
    
        return rules;
    }


    
    public void setRules( List< Rule > rules ) {
    
        this.rules = rules;
    }

    
    public void setItems( Map< Integer, Item > items ) {
    
        this.items = items;
    }


    
    public Map< Integer, Item > getItems() {
    
        return items;
    }


    
    public Rule getInitialRule() {
    
        return initialRule;
    }


    
    public void setInitialRule( Rule initialRule ) {
    
        this.initialRule = initialRule;
    }


    
    public List< Action > getTags() {
    
        return tags;
    }


    
    public void setTags( List< Action > tags ) {
    
        this.tags = tags;
    }

}
