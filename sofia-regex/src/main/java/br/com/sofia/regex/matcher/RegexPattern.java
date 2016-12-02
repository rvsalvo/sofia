package br.com.sofia.regex.matcher;

import java.util.Vector;

import org.apache.log4j.Logger;

import br.com.sofia.regex.analyzer.impl.DefaultAnalyzerImpl;
import br.com.sofia.regex.analyzer.impl.RegexAnalyzerImpl;
import br.com.sofia.regex.analyzer.model.DfaStateHolder;
import br.com.sofia.regex.analyzer.model.Interval;
import br.com.sofia.regex.analyzer.model.NfaProcessorResult;
import br.com.sofia.regex.analyzer.model.RegexExpression;
import br.com.sofia.regex.impl.DfaMatcherImpl;
import br.com.sofia.regex.impl.NfaMatcherImpl;


/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class RegexPattern {
    
    private static final Logger log = Logger.getLogger( RegexPattern.class );
    
    private RegexAnalyzerImpl analyzer = new RegexAnalyzerImpl();
    
    private DefaultAnalyzerImpl nfaAnalyzer;
    
    private DfaStateHolder dfaHolder;
    
    private NfaProcessorResult nfaHolder;
    
    private boolean fromStart;
    
    private boolean toEnd;
    
    private int totalGroups;
    
    private boolean captureGroups;
    
    private RegexPattern( String regex, boolean captureGroups ){
        this.captureGroups = captureGroups;
        process( regex );
        
    }
    
    private RegexPattern( String regex ){
        process( regex );
        
    }    
    
    private void process( String regex ){
        
        log.debug( "Processing regex: " + regex );
        
        RegexExpression exp = analyzer.process( regex );
        
        log.debug( exp.toString() );
        
        //TODO: get char classes from analyzer
        this.nfaAnalyzer = new DefaultAnalyzerImpl( new Vector< Interval >() );

        this.totalGroups = analyzer.getTotalGroups();
        
        this.nfaHolder = nfaAnalyzer.process( exp, analyzer.isFromStart(), analyzer.isToEnd() );
        
        this.dfaHolder = nfaAnalyzer.createDfa( nfaHolder.getStart() );
        this.fromStart = analyzer.isFromStart();
        this.toEnd = analyzer.isToEnd();
            
        
    }
    
    public static RegexPattern compile( String regex ){
        log.debug( "Compiling expression: " + regex );
        return new RegexPattern( regex, false );
    }
    
    public static RegexPattern compile2( String regex ){
        log.debug( "Compiling expression: " + regex );
        return new RegexPattern( regex );
    }    
    
    /**
     * Compiles the presented regular expression, creating either a DFA or NFA (with groups capture) automaton for it.
     * 
     * @param regex - The regular expression to be used.
     * @param captureGroups - If groups capture should be supported. In case of 'true' will generate a NFA automaton.
     * @return The Regular Expression Pattern.
     */
    public static RegexPattern compile( String regex, boolean captureGroups ){
        return new RegexPattern( regex, captureGroups );
    }    
    
    public RegexMatcher matcher( CharSequence sequence ){
        
        if ( captureGroups ){
            //return new DfaGroupMatcherImpl( dfaHolder, sequence, fromStart, toEnd );
            return new NfaMatcherImpl( this.nfaHolder, new DfaMatcherImpl( dfaHolder, sequence, fromStart, toEnd ), sequence, fromStart, toEnd, totalGroups );
        }
        return new DfaMatcherImpl( dfaHolder, sequence, fromStart, toEnd );
    }    
}
