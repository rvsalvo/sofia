/**
 * 
 */
package br.com.sofia.parser.model;


import java.util.LinkedList;
import java.util.OptionalDouble;


/**
 * @author Rodrigo Salvo
 *
 */
public class TokenTreeNode {

    private Token token;
    
    private OptionalDouble score;

    private LinkedList< TokenTreeNode > children = new LinkedList<>();


    public TokenTreeNode() {

        super();
    }

    public TokenTreeNode( Token token ) {

        super();
        this.token = token;
    }

    public TokenTreeNode( Token token, LinkedList< TokenTreeNode > children ) {

        super();
        this.token = token;
        this.children = children;
    }

    public TokenTreeNode( Token token, OptionalDouble score, LinkedList< TokenTreeNode > children ) {

        super();
        this.token = token;
        this.score = score;
        this.children = children;
    }
    
    public void addChildren( TokenTreeNode node ){
        if ( this.children != null ){
            this.children.add( node );
        }
    }

    /**
     * @return the token
     */
    public Token token() {

        return token;
    }

    public Double score(){
        return this.score.orElse( Double.NaN );
    }

    /**
     * @param token
     *            the token to set
     */
    public void setToken( Token token ) {

        this.token = token;
    }


    /**
     * @return the childrens
     */
    public LinkedList< TokenTreeNode > children() {

        return children;
    }


    /**
     * @param children
     *            the children to set
     */
    public void setChildren( LinkedList< TokenTreeNode > children ) {

        this.children = children;

    }


    public boolean isLeaf() {

        return this.children.isEmpty();
    }


    public StringBuilder printTree( StringBuilder sb ) {

        if ( isLeaf() ) {
            if ( token() != null ) {
                sb.append( '(' );   
                sb.append( token().toString() );
                sb.append( ')' );
            }
            return sb;
        }
        else {
            sb.append( '(' );              
            if ( token() != null ) {
                sb.append( token().toString() );
            }
            LinkedList< TokenTreeNode > kids = children();
            for ( TokenTreeNode kid : kids ) {
                sb.append( ' ' );
                kid.printTree( sb );
            }
            return sb.append( ')' );
        }
    }


    @Override
    public String toString() {

        return printTree( new StringBuilder() ).toString();
    }

}
