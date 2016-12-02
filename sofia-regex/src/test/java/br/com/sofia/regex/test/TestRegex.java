package br.com.sofia.regex.test;

import org.junit.Test;

import br.com.sofia.regex.analyzer.impl.RegexAnalyzerImpl;
import br.com.sofia.regex.analyzer.model.RegexExpression;


public class TestRegex {
    
    @Test
    public void test(){
        
        String regex = "(?<=q)((a|b)c(d|e))";
        
        regex = "(([0-9]){1,4}[-  ])?([0-9]){1,4}(\\\\?\\/|/)([0-9]){1,4}";
        
        RegexAnalyzerImpl analyzer = new RegexAnalyzerImpl();
        RegexExpression expression = analyzer.process( regex );
        
        System.out.println( expression.toString() );
        
    }

}
