package br.com.sofia.parser.processor;

import br.com.sofia.parser.model.Action;
import br.com.sofia.parser.model.ProcessorResult;


public interface Processor {
    
    public ProcessorResult process( Action action );

}
