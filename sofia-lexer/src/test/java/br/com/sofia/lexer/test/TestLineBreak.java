/**
 * 
 */
package br.com.sofia.lexer.test;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

import br.com.sofia.regex.matcher.RegexMatcher;
import br.com.sofia.regex.matcher.RegexPattern;


/**
 * @author Rodrigo Salvo
 * 
 */
public class TestLineBreak {

    @Test
    public void test() {

        File file = new File( "E:\\Projetos\\SofiaProject\\text004.txt" );
        
        BufferedReader br = null;
        
        FileInputStream fis = null;

        try {
            
            RegexPattern pattern = RegexPattern.compile( "\\n?\\r" );
            
            StringBuilder builder = new StringBuilder();
            
            fis = new FileInputStream(file);

            byte[] fileContent = new byte[(int)file.length()];

            fis.read(fileContent);

            for(int i = 0; i < fileContent.length; i++)
            {
                System.out.println( (char)fileContent[i] + " ===> " + fileContent[i]);
            }            

            br = new BufferedReader( new FileReader( file ) );
            String strLine;
            // Read File Line By Line
            while ( ( strLine = br.readLine() ) != null ) {
                builder.append( strLine ).append( "\n\r" );
                System.out.println( strLine );
            }
            
            RegexMatcher matcher = pattern.matcher( builder ); 

            if ( matcher.find() ){
                System.out.println( "found: " + builder.substring( 0, matcher.start() ) + " ==> At " + matcher.start() + " and " + matcher.end() );
            }

        } catch ( Exception ex ){
            ex.printStackTrace();
        }
        finally {
            try {
                br.close();
            }
            catch ( IOException e ) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

}
