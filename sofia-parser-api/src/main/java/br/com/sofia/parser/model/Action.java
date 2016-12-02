package br.com.sofia.parser.model;

import java.util.ArrayList;
import java.util.List;


public class Action {
    
    private String action;
    private List< String > components;
    private List< String > words = new ArrayList< String >();
    
    public List< String > getComponents() {
    
        return components;
    }
    
    public void setComponents( List< String > components ) {
    
        this.components = components;
    }

    
    public List< String > getWords() {
    
        return words;
    }

    
    public void setWords( List< String > words ) {
    
        this.words = words;
    }
    
    @Override
    public String toString(){
        
        StringBuilder builder = new StringBuilder( action );
        builder.append( "(" );
        
        for ( String word : words ){
            builder.append( word).append( "," );
        }

        return builder.substring( 0, builder.length() - 1 ) + ")";
        
    }

    
    public String getAction() {
    
        return action;
    }

    
    public void setAction( String action ) {
    
        this.action = action;
    }
    
}
