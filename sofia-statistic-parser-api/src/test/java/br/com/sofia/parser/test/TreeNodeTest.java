/**
 * 
 */
package br.com.sofia.parser.test;

import java.util.LinkedList;

import org.junit.Test;

import br.com.sofia.parser.model.Label;
import br.com.sofia.parser.model.TokenLabel;
import br.com.sofia.parser.model.TokenTreeNode;


/**
 * @author Rodrigo Salvo
 *
 */
public class TreeNodeTest {

    @Test
    public void test(){
        
        TokenTreeNode root = new TokenTreeNode();
        root.setToken( new Label( "ROOT" ) );
        
        LinkedList< TokenTreeNode > children = new LinkedList< TokenTreeNode >();
        TokenTreeNode c = new TokenTreeNode();
        c.setToken( new Label( "S" ) );
        children.add( c );
        
        LinkedList< TokenTreeNode > children2 = new LinkedList< TokenTreeNode >();        
        TokenTreeNode c2 = new TokenTreeNode();
        c2.setToken( new Label( "NP" ) );
        children2.add( c2 );        

        LinkedList< TokenTreeNode > children3 = new LinkedList< TokenTreeNode >();        
        TokenTreeNode c3 = new TokenTreeNode();
        c3.setToken( new TokenLabel( "DT", "This" ) );
        children3.add( c3 );        
        
        root.setChildren( children );
        
        c.setChildren( children2 );
        
        c2.setChildren( children3 );
        
        System.out.println( root.toString() );
    }
    
}
