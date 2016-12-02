/**
 * 
 */
package br.com.sofia.parser.test;


import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import br.com.sofia.parser.model.TokenTreeNode;
import br.com.sofia.parser.processor.factory.FileProcessorFactory;
import br.com.sofia.parser.processor.impl.CombinedTreebankFileProcessor;


/**
 * @author Rodrigo Salvo
 *
 */
public class ParseTreeNodeTest {

    @Test
    public void test() throws IOException {

        CombinedTreebankFileProcessor fileProcessor = FileProcessorFactory.createCombinedTreebankFileProcessor();

        List< TokenTreeNode > trees = fileProcessor.processFile( new File( "E:\\Projetos\\SofiaProject\\treebank\\treebank\\combined\\wsj_0198.mrg" ) );

        for ( TokenTreeNode root : trees ) {
            System.out.println( "sentence: " + root.toString() );
        }

    }

}
