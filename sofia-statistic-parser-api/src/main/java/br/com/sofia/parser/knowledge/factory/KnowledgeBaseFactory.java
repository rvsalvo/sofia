/**
 * 
 */
package br.com.sofia.parser.knowledge.factory;

import br.com.sofia.parser.knowledge.TreebankKnowledgeBase;


/**
 * @author Rodrigo Salvo
 *
 */
public class KnowledgeBaseFactory {
    
    public static TreebankKnowledgeBase createTreebankKnowledgeBase(){
        return new TreebankKnowledgeBase();
    }

}
