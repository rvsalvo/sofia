package br.com.sofia.parser.util;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;

import br.com.sofia.parser.exception.ParserException;


public class HtmlParser {
    
    public static String html2text( URL url ) throws ParserException{
        try {
            return Jsoup.parse( url, 10000 ).text();
        }
        catch ( IOException e ) {
            throw new ParserException( e );
        }
    }

}
