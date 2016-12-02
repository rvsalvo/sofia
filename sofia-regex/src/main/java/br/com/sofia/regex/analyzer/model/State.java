package br.com.sofia.regex.analyzer.model;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public enum State {
    
    CLASS,
    NOT_CLASS,
    OPEN_CLASS,
    OPEN_BRACKETS,
    CLOSE_BRACKETS,
    NEW,
    END,
    OR,
    PLUS,
    STAR,
    START,
    SLASH,
    QUESTION,
    CONCAT,
    BRACKETS,
    NEGATIVE_LOOKAHEAD,
    NEGATIVE_LOOKBEHIND,
    LOOKAHEAD,
    LOOKBEHIND,
    CHARACTER;

}
