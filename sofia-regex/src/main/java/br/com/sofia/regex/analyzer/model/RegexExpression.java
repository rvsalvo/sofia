package br.com.sofia.regex.analyzer.model;


import java.util.LinkedList;
import java.util.Map;

/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class RegexExpression {

    private Type type;

    private Object content;

    private RegexExpression exp1;

    private RegexExpression exp2;

    private LinkedList< Integer > group;
    
    public RegexExpression( Type type, Object content ) {

        super();
        this.type = type;
        this.content = content;
    }

    public RegexExpression( Type type, Object content, LinkedList< Integer > group ) {

        super();
        this.type = type;
        this.content = content;
        this.group = new LinkedList< Integer >( group );
    }


    public RegexExpression( Type type, RegexExpression exp1, RegexExpression exp2 ) {

        super();
        this.type = type;
        this.exp1 = exp1;
        this.exp2 = exp2;
    }


    public boolean isCharClass( Map< String, RegexExpression > macros ) {

        switch ( type ) {
            case CONTENT:
            case CLASS:
            case NOT_CLASS:
                return true;

            case BAR:
                return this.getExp1().isCharClass( macros ) && this.getExp2().isCharClass( macros );

            case VARIABLE:
                return macros.get( (String) this.getContent() ).isCharClass( macros );

            default:
                return false;
        }
    }
    
    public boolean isCharClass() {

        return isCharClass( null );
        
    }    


    @Override
    public String toString() {

        return trace( " " );

    }


    public String trace( String tab ) {

        if ( content != null ) {

            if ( content instanceof RegexExpression ) {
                return tab
                    + "type = "
                    + type
                    + " group = " + group 
                    + "\n" + tab + "content :" + "\n"
                    + ( (RegexExpression) content ).trace( tab + "  " );
            }
            else {
                return tab
                    + "type = "
                    + type
                    + " group = " + group
                    + "\n" + tab + "content :" + "\n" + tab + "  " + content;
            }

        }
        else if ( exp1 != null && exp2 != null ) {

            return tab
                + "type = "
                + type
                + " group = " + group
                + "\n" + tab + "child 1 :" + "\n" + exp1.trace( tab + "  " )
                + "\n" + tab + "child 2 :" + "\n" + exp2.trace( tab + "  " );
        }

        return "type = "
            + type
            + " group = " + group;

    }


    public Type getType() {

        return type;
    }


    public void setType( Type type ) {

        this.type = type;
    }


    public Object getContent() {

        return content;
    }


    public void setContent( Object content ) {

        this.content = content;
    }


    public RegexExpression getExp1() {

        return exp1;
    }


    public void setExp1( RegexExpression exp1 ) {

        this.exp1 = exp1;
    }


    public RegexExpression getExp2() {

        return exp2;
    }


    public void setExp2( RegexExpression exp2 ) {

        this.exp2 = exp2;
    }

    
    public LinkedList< Integer > getGroup() {
    
        if ( group == null ){
            group = new LinkedList< Integer >();
        }
        
        return group;
    }

    
    public void setGroup( LinkedList< Integer > group ) {
    
        this.group = new LinkedList< Integer >( group );
    }

}
