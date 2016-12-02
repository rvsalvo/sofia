package br.com.sofia.lexer.test;

import org.junit.Test;

import br.com.sofia.lexer.util.SplitterUtils;


public class TestSplitter {
    
    @Test
    public void test(){

        String expression = "((teste)|((Jan|Feb|Mar|Apr|Jun|Jul|Aug|Sep|Sept|Oct|Nov|Dec)|(Mon|Tue|Tues|Wed|Thu|Thurs|Fri)|{ABSTATE}|{ABCOMP}|{ABNUM}|{ABPTIT}|etc|al|seq)\\.)";
        
        for ( String part : SplitterUtils.splitWithoutBrackets( '|', expression ) ){
            System.out.println( part );
        }
        
    }

}
