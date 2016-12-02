package br.com.sofia.regex.test;

import org.junit.Test;

import br.com.sofia.regex.analyzer.impl.RegexAnalyzerImpl;
import br.com.sofia.regex.analyzer.model.RegexExpression;



public class TestExpression {
    
    @Test
    public void test(){
        
        String regex = "(?<=q)((a|b)c(d|e))";
        
        RegexAnalyzerImpl analyzer = new RegexAnalyzerImpl();
        RegexExpression expression = analyzer.process( regex );
        
        System.out.println( expression.toString() );
        
        regex = "\\r?\\n";
        
        analyzer = new RegexAnalyzerImpl();
        expression = analyzer.process( regex );
        
        System.out.println( "====> \n" + expression.toString() );        
        
    }

}
