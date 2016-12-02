/**
 * 
 */
package br.com.sofia.parser.processor.factory;

import br.com.sofia.parser.processor.FileProcessor;
import br.com.sofia.parser.processor.impl.CombinedTreebankFileProcessor;
import br.com.sofia.parser.processor.impl.PlainTreebankFileProcessor;
import br.com.sofia.parser.processor.impl.TaggedTreebankFileProcessor;


/**
 * @author Rodrigo Salvo
 *
 */
public class FileProcessorFactory {
    
    public static FileProcessor createPlainTreebankFileProcessor(){
        return new PlainTreebankFileProcessor();
    }
    
    public static FileProcessor createTaggedTreebankFileProcessor(){
        return new TaggedTreebankFileProcessor();
    } 
    
    public static CombinedTreebankFileProcessor createCombinedTreebankFileProcessor(){
        return new CombinedTreebankFileProcessor();
    }
    
}
