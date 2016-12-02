package br.com.sofia.regex.model;

import br.com.sofia.regex.analyzer.model.Option;
import br.com.sofia.regex.analyzer.model.State;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class Symbol {
    
    private Option option;
    private State state;
    private boolean used;
    private int left, right;
    private Object value;
    
    public Symbol( Option option, State state, int left, int right, Object value ) {

        super();
        this.option = option;
        this.state = state;
        this.left = left;
        this.right = right;
        this.value = value;
    }
    
    public Symbol( Option option, State state, Object value ) {

        super();
        this.option = option;
        this.state = state;
        this.value = value;
    }    

    public State getState() {
    
        return state;
    }
    
    public void setState( State state ) {
    
        this.state = state;
    }
    
    public boolean isUsed() {
    
        return used;
    }
    
    public void setUsed( boolean used ) {
    
        this.used = used;
    }
    
    public int getLeft() {
    
        return left;
    }
    
    public void setLeft( int left ) {
    
        this.left = left;
    }
    
    public int getRight() {
    
        return right;
    }
    
    public void setRight( int right ) {
    
        this.right = right;
    }
    
    public Object getValue() {
    
        return value;
    }
    
    public void setValue( Object value ) {
    
        this.value = value;
    }

    
    public Option getOption() {
    
        return option;
    }

    
    public void setOption( Option option ) {
    
        this.option = option;
    }

    @Override
    public String toString() {

        return "Symbol [option=" + option + ", state=" + state + "] \nvalue=" + value;
    }

}
