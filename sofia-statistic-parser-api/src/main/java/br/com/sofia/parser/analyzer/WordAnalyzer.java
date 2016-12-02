package br.com.sofia.parser.analyzer;

import java.util.List;

import br.com.sofia.parser.model.DictionaryWord;


public class WordAnalyzer {
    
    public List< DictionaryWord > analyze( String sentence ){
        return null;
    }

    protected DictionaryWord createWord( String word ) {

        DictionaryWord w = new DictionaryWord( word, null, null );

        return w;
    }
}
