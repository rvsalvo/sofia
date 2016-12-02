package br.com.sofia.lexer.test;


public class Test {

    /**
     * @param args
     */
    public static void main( String[] args ) {

        char[] inputs = new char[65000];
        
        inputs[0] = 'a';
        inputs[1] = 'á';

        System.out.println( (int)0xFFFF );
        
        for ( int i = 0; i < 1000; i++ ){
            System.out.println( "char: " + (char)i + " i: " + i);
        }
        
        for ( int i = 0; i < 10; i++ ){
            if ( inputs[i] != '\u0000' ){
                System.out.println( inputs[i] + "  int = " + (int)inputs[i] );
            }
        }

    }

}
