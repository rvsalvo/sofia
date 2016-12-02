package br.com.sofia.parser.processor;

import java.util.List;


public abstract class AbstractProcessor implements Processor {
    
    protected static final String NOUN = "NN";
    protected static final String ADJECTIVE = "JJ";
    protected static final String VERB = "VB";
    protected static final String OPTION = "Option";
    
    protected int getStringIndex( String value, List< String > list ){
        
        int i = 0;
        for ( String el : list ){
            if ( el.equals( value ) ){
                return i;
            }
            i++;
        }
        
        return -1;
        
    }

}
