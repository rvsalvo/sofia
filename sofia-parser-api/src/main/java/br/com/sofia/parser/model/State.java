package br.com.sofia.parser.model;


public class State {
    
    private int stateNumber;
    private String symbol;
    private Item item;
    
    public int getStateNumber() {
    
        return stateNumber;
    }
    
    public void setStateNumber( int stateNumber ) {
    
        this.stateNumber = stateNumber;
    }
    
    public String getSymbol() {
    
        return symbol;
    }
    
    public void setSymbol( String symbol ) {
    
        this.symbol = symbol;
    }

    @Override
    public String toString() {

        return symbol + " " + stateNumber + " ";
    }

    
    public Item getItem() {
    
        return item;
    }

    
    public void setItem( Item item ) {
    
        this.item = item;
    }
    
    
    
}
