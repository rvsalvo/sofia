package br.com.sofia.regex.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import br.com.sofia.regex.matcher.RegexMatcher;
import br.com.sofia.regex.matcher.RegexPattern;



public class TestPerformace {
    
    @Test
    public void testRegex1() throws Exception {

        // String regex =
        // "([\u0030-\u0039]){1,2}[\\-\\/]([\u0030-\u0039]){1,2}[\\-\\/]([\u0030-\u0039]){2,4}";
        // String regex =
        // "(([a-zA-Zàá])+(?!['t])|([­ȷ])+)\\-(([a-zA-Zà])+(?!['t])|([­ȷ])+)";
        // String regex = "(([a-zA-ZàÇ])+(?!['t])|([­ȷ])+)";
        String regex = "\\{(\\d+):(([^}](?!-} ))*)";
        // String regex =
        // "(([a-z])+(?!['t])|([­j])+)\\-(([a-z])+(?!['t])|([­j])+)";
        // String regex = "(([U](\\.[S])+|(n)-U)\\.)";
        // String regex = "^(([^:]+)://)?([^:/]+)(:([0-9]+))?(/.*)";
        // String regex = "\\b(\\w+)(\\s+\\1)+\\b";

        StringBuilder builder = new StringBuilder();

        // BufferedReader br = new BufferedReader( new FileReader( new File(
        // "D:\\Projetos\\SofiaProject\\text001.txt" ) ) );
        BufferedReader br = new BufferedReader( new InputStreamReader( this.getClass().getResourceAsStream( "/texto.txt" ) ) );
        String strLine = null;
        // Read File Line By Line
        while ( ( strLine = br.readLine() ) != null ) {
            builder.append( strLine );
        }

        String text = builder.toString();
        // String text = "for a berth on the U.S. Ryder Cup team";
        // String text = "usd 1234.00";
        // String text = "Another 20-02-2000";

        RegexPattern pattern = RegexPattern.compile( regex );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        long now = System.nanoTime();

        if ( matcher.find() ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() );
        }

        System.out.println( "Java Took: " + String.valueOf( System.nanoTime() - now ) );

        System.out.println( "" );

        now = System.nanoTime();

        if ( regexMatcher.find() ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }

        System.out.println( "Took finder: " + String.valueOf( System.nanoTime() - now ) );

        System.out.println( "" );
    }


    @Test
    public void testRegex2() {

        String regex = "(?<!q)(([ab])|([bc]))";
        // String regex =
        // "([a-zA-ZàáãéíçêéúóõÁÁÃÉÍÓÚÕÊÇ])+(?!['t])|([­ȷ-ɏ˂-˅˒-˟˥-˿̀-ͯͰ-ͽ΄΅Ϗ϶ϼ-Ͽ҃-҇ӏӶ-ӿԐ-ԥ՚-՟֑-ׇֽֿׁׂׅׄؕ-ؚػ-ؿً-ٰٞۖ-ۯۺ-ۿ܏ܑܰ-ݏݐ-ݿަ-ޱߊ-ߵߺऀ-ः़ा-ॎ॑-ॕॢ-ॣঁ-ঃ়-ৄেৈো-্ৗৢৣਁ-ਃ਼ਾ-੏ઁ-ઃ઼-૏ஂா-ூெ-ைொ-்ఁ-ఃా-ౖാ-ൄെ-ൈะ-ฺ็-๎ັ-ຼ່-ໍ])+";
        // String regex = "((ab)|(ac))";

        String text = "ac";

        RegexPattern pattern = RegexPattern.compile( regex );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        long now = System.nanoTime();

        if ( matcher.find() ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() + " text: "
                + text.substring( matcher.start(), matcher.end() ) );
        }

        System.out.println( "Java Took: " + String.valueOf( System.nanoTime() - now ) );

        System.out.println( "" );

        now = System.nanoTime();

        if ( regexMatcher.find() ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end()
                + " text: " + text.substring( matcher.start(), matcher.end() ) );
        }

        System.out.println( "Took finder: " + String.valueOf( System.nanoTime() - now ) );

        System.out.println( "" );

    }

    @Test
    public void testRegex3() throws Exception {

        String regex = "a(a|b+)";

        String text = "abb";

        RegexPattern pattern = RegexPattern.compile( regex );

        RegexMatcher regexMatcher = pattern.matcher( text );

        Pattern pttern = Pattern.compile( regex );

        Matcher matcher = pttern.matcher( text );

        long now = System.nanoTime();

        if ( matcher.find() ) {
            System.out.println( "\nResult Matches: start: " + matcher.start() + " end: " + matcher.end() );
        }

        System.out.println( "Java Took: " + String.valueOf( System.nanoTime() - now ) );

        System.out.println( "" );

        now = System.nanoTime();

        if ( regexMatcher.find() ) {
            System.out.println( "\nResult Matches: start: " + regexMatcher.start() + " end: " + regexMatcher.end() );
        }

        System.out.println( "Took finder: " + String.valueOf( System.nanoTime() - now ) );

        System.out.println( "" );
    }

}
