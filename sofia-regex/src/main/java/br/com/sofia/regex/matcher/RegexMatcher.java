package br.com.sofia.regex.matcher;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public interface RegexMatcher {
    
    boolean find();
    
    boolean find( int start );
    
    boolean findExact( int start );
    
    boolean matches();
    
    RegexMatcher reset();
    
    RegexMatcher reset( CharSequence sequence );
    
    int start();
    
    int end();
    
    String group( int group );
    
    String group();
    
    int groupCount();

}


