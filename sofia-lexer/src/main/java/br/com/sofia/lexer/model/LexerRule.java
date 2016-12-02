package br.com.sofia.lexer.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import br.com.sofia.regex.matcher.RegexMatcher;
import br.com.sofia.regex.matcher.RegexPattern;



public class LexerRule {
    
    private static final Logger log = Logger.getLogger( LexerRule.class );
    
    private String left;
    private String right;
    
    private RegexPattern regexPattern;
    private RegexMatcher regexMatcher;
    
    private Pattern pattern;
    private Matcher matcher;
    
    private RuleCommand command;
    
    public String getLeft() {
    
        return left;
    }
    
    public void setLeft( String left ) {
    
        this.left = left;
    }
    
    public String getRight() {
    
        return right;
    }
    
    public void setRight( String right ) {
    
        this.right = right;
        
        if ( this.right != null ){
            //this.right = "\\G" + right;
            this.pattern = Pattern.compile( right );
            this.matcher = this.pattern.matcher( "" );
            
            log.debug( "Compiling expression: " + right );            
            this.regexPattern = RegexPattern.compile( right );
            this.regexMatcher = this.regexPattern.matcher( "" );            
            
        }
    }
    
    @Override
    public String toString() {

        return left + "->" + right + " [" + command.toString() + "]";
        
    }

    
    public Pattern getPattern() {
    
        return pattern;
    }

    
    public void setPattern( Pattern pattern ) {
    
        this.pattern = pattern;
    }

    
    public Matcher getMatcher() {
    
        return matcher;
    }

    
    public void setMatcher( Matcher matcher ) {
    
        this.matcher = matcher;
    }

    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ( ( left == null ) ? 0 : left.hashCode() );
        result = prime * result + ( ( right == null ) ? 0 : right.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {

        if ( this == obj )
            return true;
        if ( obj == null )
            return false;
        if ( getClass() != obj.getClass() )
            return false;
        LexerRule other = (LexerRule) obj;
        if ( left == null ) {
            if ( other.left != null )
                return false;
        }
        else if ( !left.equals( other.left ) )
            return false;
        if ( right == null ) {
            if ( other.right != null )
                return false;
        }
        else if ( !right.equals( other.right ) )
            return false;
        return true;
    }

    
    public RuleCommand getCommand() {
    
        return command;
    }

    
    public void setCommand( RuleCommand command ) {
    
        this.command = command;
    }

    
    public RegexPattern getRegexPattern() {
    
        return regexPattern;
    }

    
    public void setRegexPattern( RegexPattern regexPattern ) {
    
        this.regexPattern = regexPattern;
    }

    
    public RegexMatcher getRegexMatcher() {
    
        return regexMatcher;
    }

    
    public void setRegexMatcher( RegexMatcher regexMatcher ) {
    
        this.regexMatcher = regexMatcher;
    }

}
