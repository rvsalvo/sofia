package br.com.sofia.regex.analyzer.impl;


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import br.com.sofia.regex.analyzer.model.Interval;
import br.com.sofia.regex.analyzer.model.Option;
import br.com.sofia.regex.analyzer.model.RegexExpression;
import br.com.sofia.regex.analyzer.model.State;
import br.com.sofia.regex.analyzer.model.Type;
import br.com.sofia.regex.exception.RegexParserException;
import br.com.sofia.regex.model.Symbol;


/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class RegexAnalyzerImpl {

    private static final Logger log = Logger.getLogger( RegexAnalyzerImpl.class );

    private Stack< Symbol > stack = new Stack< Symbol >();
    
    private LinkedList< Integer > groups;
    
    private int totalGroups;

    private Stack< State > action = new Stack< State >();
    
    private Pattern DIGIT = Pattern.compile( "(\\d+),(\\d+)" );
    
    private boolean fromStart, toEnd;
    
    private static final List< Character > CHARS = Arrays.asList( 'b', 'B', 'd', 'D', 's', 'S', 'w', 'W', 'z' );



    private int getLookAhead( CharSequence expression, int i, int n ) {

        if ( i + n < expression.length() ) {
            return expression.charAt( i + n );
        }

        return -1;
    }


    private boolean hasOperator( CharSequence expression, int i ) {

        int next = getLookAhead( expression, i, 1 );

        if ( next != -1 && ( (char) next == '+' || (char) next == '*' || (char) next == '?' ) ) {

            return true;

        }

        return false;
    }


    public RegexExpression process( CharSequence expression ) {
        
        this.groups = new LinkedList< Integer >();
        this.stack = new Stack< Symbol >();
        this.action = new Stack< State >();
        this.fromStart = false;
        this.toEnd = false;
        this.totalGroups = 0;
        boolean specialState = false;
        
        AtomicInteger groupCount = new AtomicInteger( 0 );
        
        int i = 0;
        
        while ( i < expression.length() ) {

            Symbol item = null;

            char c = expression.charAt( i );
            
            log.debug( "char: " + c );

            switch ( c ) {
                case '[':

                    i = processCharClass( expression, i );
                    
                    //check if it has {}
                    char nextToken = (char) getLookAhead( expression, i, 1 );
                    
                    if ( nextToken != '{' ){                    
                    
                        checkForConcat( expression, State.CHARACTER, i );
                    
                    }
                    
                break;
                case '{':

                    i++;
                    c = expression.charAt( i );

                    StringBuilder str = new StringBuilder();

                    do {

                        str.append( c );

                        i++;
                        checkRegex( expression, i );
                        c = expression.charAt( i );

                    }
                    while ( c != '}' );

                    Matcher matcher = DIGIT.matcher( str.toString() );

                    if ( matcher.find() ) {

                        Symbol it1 = stack.pop();

                        int d1 = Integer.parseInt( matcher.group( 1 ) );
                        int d2 = Integer.parseInt( matcher.group( 2 ) );

                        RegexExpression exp = makeRepeat( (RegexExpression) it1.getValue(), d1, d2 );
                        item = new Symbol( Option.DIGIT_REPEAT, State.BRACKETS, exp );

                    }
                    else {
                        // variable
                        RegexExpression exp = new RegexExpression( Type.VARIABLE, str.toString() );
                        exp.setGroup( groups );
                        item = new Symbol( Option.VARIABLE, State.BRACKETS, exp );
                        action.push( State.CHARACTER );

                    }

                    stack.push( item );
                    
                    checkForConcat( expression, State.CHARACTER, i );                 
                                      
                break;
                case '(':

                    nextToken = (char) getLookAhead( expression, i, 1 );

                    if ( nextToken == '#' ) {
                        while ( c != ')' ) {
                            i++;
                            c = expression.charAt( i );
                        }
                    }
                    else if ( nextToken == '?' ) {
                        nextToken = (char) getLookAhead( expression, i, 2 );
                        specialState = true;
                        if ( nextToken == '!' ) {
                            action.push( State.NEGATIVE_LOOKAHEAD );
                            i += 2;
                        } else if ( nextToken == '=' ){
                            action.push( State.LOOKAHEAD );
                            i += 2;      
                        } else if ( nextToken == '<' ){
                            nextToken = (char) getLookAhead( expression, i, 3 );
                            if ( nextToken == '!' ) {
                                action.push( State.NEGATIVE_LOOKBEHIND );
                                i += 3;
                            } else if ( nextToken == '=' ){
                                action.push( State.LOOKBEHIND );
                                i += 3;
                            } else {
                                groups.push( groupCount.incrementAndGet() );
                            }
                        } else if ( nextToken == ':' ){
                            //don't capture group
                            i += 2;                            
                        } else {
                            specialState = false;
                            groups.push( groupCount.incrementAndGet() );
                        }
                    } else {
                        groups.push( groupCount.incrementAndGet() );
                    }

                    action.push( State.OPEN_BRACKETS );

                break;
                case ')':

                    State state = action.pop();
                    
                    while ( State.OPEN_BRACKETS != state ) {

                        if ( State.CHARACTER == state ) {
                            
                            State previous = action.pop();
                            
                            if ( State.CHARACTER == previous ){
                                generateConcatExpression();
                                action.push( previous );
                            } else if ( State.OR == previous ){
                                
                                State st3 = action.pop();
                                if ( State.CHARACTER == st3 ){
                                    generateOrExpression();
                                    action.push( State.CHARACTER );
                                } else {
                                    action.push( st3 );
                                    action.push( previous );
                                    action.push( state );
                                }
                                
                            } else {
                                action.push( previous );
                            }
                        } else if ( State.NEGATIVE_LOOKAHEAD == state ) {
                            generateSpecialExpression( Type.NEGATIVE_LOOKAHEAD );
                        } else if ( State.LOOKAHEAD == state ) {
                            generateSpecialExpression( Type.LOOKAHEAD );
                        } else if ( State.NEGATIVE_LOOKBEHIND == state ) {
                            generateSpecialExpression( Type.NEGATIVE_LOOKBEHIND );
                        } else if ( State.LOOKBEHIND == state ) {
                            generateSpecialExpression( Type.LOOKBEHIND );
                        }
                        
                        state = action.pop();
                    }
                    
                    if ( !specialState ){
                        groups.pop();
                    } else {
                        specialState = false;
                    }
                    
                    action.push( State.CHARACTER );
                    
                    //check if it has {}
                    nextToken = (char) getLookAhead( expression, i, 1 );
                    
                    if ( !action.isEmpty() && nextToken != '{' ){

                        checkForConcat( expression, State.CLOSE_BRACKETS, i );
                        
                    }
                    
                break;
                case '+':

                    item = stack.pop();

                    RegexExpression exp = new RegexExpression( Type.PLUS, (RegexExpression) item.getValue() );
                    exp.setGroup( groups );
                    item = new Symbol( Option.CLOSECLASS, State.PLUS, exp );
                    stack.push( item );
                    
                    checkForConcat( expression, State.PLUS, i );                 
               
                break;
                case '*':

                    item = stack.pop();

                    exp = new RegexExpression( Type.STAR, (RegexExpression) item.getValue() );
                    exp.setGroup( groups );
                    item = new Symbol( Option.CLOSECLASS, State.STAR, exp );
                    stack.push( item );
                    
                    checkForConcat( expression, State.STAR, i );                 
                                        

                break;
                case '?':

                    item = stack.pop();

                    exp = new RegexExpression( Type.QUESTION, (RegexExpression) item.getValue() );
                    exp.setGroup( groups );
                    item = new Symbol( Option.QUEST, State.QUESTION, exp );
                    stack.push( item );
                    
                    checkForConcat( expression, State.QUESTION, i );                 
                                        
                break;

                case '|':
                    
                    checkForConcat( expression, State.OR, i );

                    action.push( State.OR );

                break;
                case '\\':

                    i++;
                    checkRegex( expression, i );
                    c = expression.charAt( i );

                    Character character = new Character( c );

                    if ( CHARS.contains( character ) ){
                        
                        switch ( character ){
                            case 'w':
                                processCharClass( "[0-9a-zA-Z]", 0 );
                                
                                //check if it has {}
                                nextToken = (char) getLookAhead( expression, i, 1 );
                                
                                if ( nextToken != '{' ){                    
                                
                                    checkForConcat( expression, State.CHARACTER, i );
                                
                                }                                
                                break;
                            case 'W':
                                processCharClass( "[^0-9a-zA-Z]", 0 );
                                
                                //check if it has {}
                                nextToken = (char) getLookAhead( expression, i, 1 );
                                
                                if ( nextToken != '{' ){                    
                                
                                    checkForConcat( expression, State.CHARACTER, i );
                                
                                }                                
                                break;                                
                            case 'd':
                                processCharClass( "[0-9]", 0 );
                                
                                //check if it has {}
                                nextToken = (char) getLookAhead( expression, i, 1 );
                                
                                if ( nextToken != '{' ){                    
                                
                                    checkForConcat( expression, State.CHARACTER, i );
                                
                                }                                
                                break;
                                
                            case 'D':
                                processCharClass( "[^0-9]", 0 );
                                
                                //check if it has {}
                                nextToken = (char) getLookAhead( expression, i, 1 );
                                
                                if ( nextToken != '{' ){                    
                                
                                    checkForConcat( expression, State.CHARACTER, i );
                                
                                }                                
                                break;         
                            case 's':
                                processCharClass( "[ \r\n\t]", 0 );
                                
                                //check if it has {}
                                nextToken = (char) getLookAhead( expression, i, 1 );
                                
                                if ( nextToken != '{' ){                    
                                
                                    checkForConcat( expression, State.CHARACTER, i );
                                
                                }                                
                                break;       
                            case 'S':
                                processCharClass( "[^ \r\n\t]", 0 );
                                
                                //check if it has {}
                                nextToken = (char) getLookAhead( expression, i, 1 );
                                
                                if ( nextToken != '{' ){                    
                                
                                    checkForConcat( expression, State.CHARACTER, i );
                                
                                }                                
                                break;        

                        }
                        
                    } else {
                        
                        if ( character == 'n' ){
                            character = '\n';
                        } else if ( character == 'r'){
                            character = '\r';
                        } else if ( character == 't'){
                            character = '\t';
                        }
                    
                        exp = new RegexExpression( Type.CONTENT, character );
                        exp.setGroup( groups );
    
                        item = new Symbol( Option.CHARACTER, State.SLASH, exp );
                        stack.push( item );
                        action.push( State.CHARACTER );                    
    
                        checkForConcat( expression, State.CHARACTER, i );
                    
                    }

                break;
                case '.':

                    Vector< Interval > any = new Vector< Interval >();
                    
                    Interval interval = new Interval( (char)28, (char)127 );
                    any.add( interval );
                    interval = new Interval( (char)192, (char)252 );
                    exp = new RegexExpression( Type.CLASS, any );
                    exp.setGroup( groups );

                    item = new Symbol( Option.ANY, State.NEW, exp );
                    stack.push( item );
                    action.push( State.CHARACTER );                    
                    
                    checkForConcat( expression, State.CHARACTER, i );                 
                    
                break;
                case '^':
                    if ( !stack.isEmpty() ){
                        throw new RegexParserException( "Not expected [^]" );
                    }
                    this.fromStart = true;
/*                    exp = new RegexExpression( Type.START, '^' );

                    item = new Symbol( Option.CHARACTER, State.NEW, exp );
                    stack.push( item );
                    action.push( State.CHARACTER );
*/                break;
                case '$':
                    
                    if ( i + 1 != expression.length() ){
                        throw new RegexParserException( "Not expected [$]" );
                    }     
                    this.toEnd = true;
/*                    exp = new RegexExpression( Type.END, '$' );

                    item = new Symbol( Option.CHARACTER, State.NEW, exp );
                    stack.push( item );
                    action.push( State.CHARACTER );*/
                break;
                default:

                    character = new Character( c );
                    exp = new RegexExpression( Type.CONTENT, character );
                    exp.setGroup( groups );

                    item = new Symbol( Option.CHARACTER, State.CHARACTER, exp );
                    stack.push( item );
                    action.push( State.CHARACTER );
                    
                    checkForConcat( expression, State.CHARACTER, i );                 

                break;

            }

            i++;

        }

        while ( action.size() > 1 ) {

            checkForConcat( expression, State.END, i );

        }

        Symbol item = stack.pop();
        RegexExpression exp = (RegexExpression) item.getValue();

        if ( !stack.isEmpty() ) {
            log.error( "stack is not empty. Somethig is wrong" );
            throw new RegexParserException( "Stack is not empty" );
        }
        
        totalGroups = groupCount.get();

        return exp;
    }


    private int processCharClass( CharSequence expression, int i ) {

        Symbol item;

        State classState = State.CLASS;

        // begin class
        i++;
        checkRegex( expression, i );
        char c = expression.charAt( i );

        if ( c == '^' ) {
            classState = State.NOT_CLASS;

            i++;
            checkRegex( expression, i );
            c = expression.charAt( i );

        } else if ( c == '\\' ){
            
            i++;
            checkRegex( expression, i );
            c = expression.charAt( i );

        }

        Vector< Interval > array = new Vector< Interval >();
        
        Interval interval = generateInterval( expression, c, i );
        i = interval.getEndIndex();
        
        array.add( interval );

        i++;
        c = expression.charAt( i );

        while ( c != ']' ) {
            
            if ( c == '\\' ){
                
                i++;
                checkRegex( expression, i );
                c = expression.charAt( i );

            }                        

            interval = generateInterval( expression, c, i );
            i = interval.getEndIndex();

            array.add( interval );

            item = new Symbol( Option.CLASS, classState, array );

            i++;
            c = expression.charAt( i );

        }

        RegexExpression exp = new RegexExpression( classState == State.CLASS ? Type.CLASS
            : Type.NOT_CLASS, array );
        
        exp.setGroup( groups );
        
        item = new Symbol( Option.CLASS, classState, exp );

        stack.push( item );
        action.push( State.CHARACTER );
        
        return i;
    }


    private void checkForConcat( CharSequence expression, State actual, int i ) {
        
        State lastAction = action.pop();

        if ( !hasOperator( expression, i ) && State.CHARACTER == lastAction ) {
            
            if ( action.isEmpty() ){
                action.push( lastAction );
                return;
            }
            
            State previous = action.pop();
            
            if ( State.CHARACTER == previous ){
                generateConcatExpression();
                action.push( previous );
            } else if ( State.OR == previous && ( actual == State.OR || actual == State.END ) ){
                
                State st3 = action.pop();
                if ( State.CHARACTER == st3 ){
                    generateOrExpression();
                    action.push( st3 );
                } else {
                    action.push( st3 );
                    action.push( previous );
                    action.push( lastAction );
                }
            } else if ( State.NEGATIVE_LOOKAHEAD == previous ){
                generateSpecialExpression( Type.NEGATIVE_LOOKAHEAD );        
                action.push( lastAction );
            } else if ( State.LOOKAHEAD == previous ){
                generateSpecialExpression( Type.LOOKAHEAD );        
                action.push( lastAction );
            } else if ( State.NEGATIVE_LOOKBEHIND == previous ){
                    generateSpecialExpression( Type.NEGATIVE_LOOKBEHIND );        
                    action.push( lastAction );
            } else if ( State.LOOKBEHIND == previous ){
                generateSpecialExpression( Type.LOOKBEHIND );        
                action.push( lastAction );                
            } else {
                action.push( previous );
                action.push( lastAction );
            }
        } else {
            action.push( lastAction );            
        }
    }

    private RegexExpression makeRepeat( RegexExpression r, int n1, int n2 ) {

        if ( n1 <= 0 && n2 <= 0 ) {
            throw new RegexParserException( "Repeat number is zero" );
        }

        if ( n1 > n2 ) {
            throw new RegexParserException( "First number is greater than second" );
        }

        int i;
        RegexExpression result;

        if ( n1 > 0 ) {
            result = r;
            n1--;
            n2--;
        }
        else {
            result = new RegexExpression( Type.QUESTION, r, groups );
            n2--;
        }

        for ( i = 0; i < n1; i++ ){
            result = new RegexExpression( Type.CONCAT, result, r );
            result.setGroup( groups );
        }

        n2 -= n1;
        for ( i = 0; i < n2; i++ ) {
            result = new RegexExpression( Type.CONCAT, result, new RegexExpression( Type.QUESTION, r, groups ) );
            result.setGroup( groups );
        }

        return result;
    }

    private void generateOrExpression() {

        Symbol item = stack.pop();
        Symbol item2 = stack.pop();

        RegexExpression conjunction = new RegexExpression( Type.BAR, (RegexExpression) item2.getValue(),
            (RegexExpression) item.getValue() );
        
        conjunction.setGroup( groups );

        item = new Symbol( Option.OR, State.NEW, conjunction );
        stack.push( item );

    }


    private void generateConcatExpression() {

        Symbol item = stack.pop();
        Symbol item2 = stack.pop();

        RegexExpression parent = new RegexExpression( Type.CONCAT, (RegexExpression) item2.getValue(),
            (RegexExpression) item.getValue() );
        
        parent.setGroup( groups );

        item = new Symbol( Option.CHARACTER, State.NEW, parent );
        stack.push( item );
    }

    private void generateSpecialExpression( Type type ) {

        Symbol item = stack.pop();

        RegexExpression exp = new RegexExpression( type, (RegexExpression) item.getValue() );

        item = new Symbol( Option.CHARACTER, State.NEW, exp );
        stack.push( item );
    }      

    private Interval generateInterval( CharSequence expression, char c, int i ) {

        char start = c;
        char end = c;

        i++;
        checkRegex( expression, i );
        c = expression.charAt( i );

        if ( c == '-' ) {

            i++;
            checkRegex( expression, i );
            c = expression.charAt( i );
            end = c;
        }
        else {
            i--;
        }

        Interval interval = new Interval( start, end );
        interval.setEndIndex( i );
        return interval;
    }


    private void checkRegex( CharSequence expression, int i ) {

        if ( i >= expression.length() ) {
            throw new RegexParserException( "Invalid Regex" );
        }
    }


    
    public boolean isFromStart() {
    
        return fromStart;
    }


    
    public boolean isToEnd() {
    
        return toEnd;
    }


    
    public int getTotalGroups() {
    
        return totalGroups;
    }


}
