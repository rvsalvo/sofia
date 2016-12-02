package br.com.sofia.parser.processor.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.log4j.Logger;

import br.com.sofia.parser.command.Command;
import br.com.sofia.parser.model.Action;
import br.com.sofia.parser.model.ProcessorItem;
import br.com.sofia.parser.model.ProcessorResult;
import br.com.sofia.parser.processor.AbstractProcessor;
import br.com.sofia.parser.util.FileUtils;
import br.com.sofia.parser.util.Tag;
import br.com.sofia.parser.util.TagReader;


public class MusicProcessor extends AbstractProcessor {
    
    private static final Logger log = Logger.getLogger( MusicProcessor.class );

    private static final String[] VERBS = new String[] { "play", "stop" };
    
    private Map< String, Integer > genres = new HashMap< String, Integer >();
    
    private Process runningProcess;

    private String verb;

    private String noun;
    
    private String kind;

    private String option;
    
    private String baseDir = "G:\\Audio\\Pop e Rock Internacional\\";

    public MusicProcessor(){
        
        genres.put( "rock", 17 );
        genres.put( "pop", 13 );
        
    }

    @Override
    public ProcessorResult process( Action action ) {

        if ( Command.ACTION.equals( Command.valueOf( action.getAction().toUpperCase() ) ) ) {
            
            resetState();

            verb = action.getWords().get( 0 );

            boolean process = false;

            for ( String vb : VERBS ) {
                if ( vb.equalsIgnoreCase( verb ) ) {
                    process = true;
                    break;
                }
            }

            if ( !process ) {
                return null;
            }

            int index = getStringIndex( NOUN, action.getComponents() );

            if ( index != -1 ) {
                noun = action.getWords().get( index );

                if ( noun == null || ( !"music".equals( noun ) && !"song".equals( noun ) ) ) {
                    return null;
                }

            }
            
            index = getStringIndex( ADJECTIVE, action.getComponents() );

            if ( index != -1 ) {
                kind = action.getWords().get( index );

                if ( kind == null || ( !has( kind ) ) ) {
                    return null;
                }

            }            

            index = getStringIndex( OPTION, action.getComponents() );

            if ( index != -1 ) {

                option = action.getWords().get( index );

                if ( option == null ) {
                    return createUnknowResult();
                }

            }

            if ( option != null ){
                
               File dir = new File( baseDir );
                
                Set< File > files = FileUtils.list( dir, new WildcardFileFilter( "*" + option + "*.mp3", IOCase.INSENSITIVE ) );
                
                for ( File file : files ) {
                    
                    try {
                        
                            log.debug( "playing song... " + file.getName() );
                            //play song
                            
                            List< String > terms = new ArrayList< String >();
                            terms.add( "Playing" );
                            terms.add( file.getName() );
                            terms.add( "." );
                            
                            List< ProcessorItem > items = new ArrayList< ProcessorItem >();

                            ProcessorItem item = new ProcessorItem();
                            item.setName( file.getName() );
                            item.setType( noun );
                            items.add( item );
                            
                            ProcessorResult result = new ProcessorResult();
                            result.setItems( items );
                            result.setDate( new Date() );
                            result.setSubject( noun );
                            result.setResult( true );
                            result.setVerb( verb );
                            result.setOriginalTerms( action.getWords() );
                            result.setTerms( terms );
                            
                            //Desktop.getDesktop().open( file );
                            
                            //Process p = Runtime.getRuntime().exec( "rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath() );
                            
                            runningProcess = Runtime.getRuntime().exec( "\"C:\\Program Files (x86)\\Winamp\\winamp.exe\"" + " \"" + file.getAbsolutePath() + "\"" );
                            
                            return result;
                            
                    }
                    catch ( Exception e ) {
                        e.printStackTrace();
                    }
                    
                }

                return createUnknowResult();

                
            } else if ( noun != null && kind != null ){
                
                //find style
                
                TagReader reader = new TagReader();
                
                File dir = new File( baseDir );
                
                Set< File > files = FileUtils.list( dir, new WildcardFileFilter( "*.mp3" ) );
                
                for ( File file : files ) {
                    
                    try {
                        Tag tag = reader.readTag( file );
                        
                        if ( tag.getGenre() == genres.get( kind ) ){
                            
                            log.debug( "playing song... " + file.getName() );
                            //play song
                            
                            List< String > terms = new ArrayList< String >();
                            terms.add( "Playing" );
                            terms.add( file.getName() );
                            terms.add( "." );
                            
                            List< ProcessorItem > items = new ArrayList< ProcessorItem >();

                            ProcessorItem item = new ProcessorItem();
                            item.setName( file.getName() );
                            item.setType( noun );
                            items.add( item );
                            
                            ProcessorResult result = new ProcessorResult();
                            result.setItems( items );
                            result.setDate( new Date() );
                            result.setSubject( noun );
                            result.setResult( true );
                            result.setVerb( verb );
                            result.setOriginalTerms( action.getWords() );
                            result.setTerms( terms );
                            
                            //Desktop.getDesktop().open( file );
                            
                            //Process p = Runtime.getRuntime().exec( "rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath() );
                            
                            runningProcess = Runtime.getRuntime().exec( "\"C:\\Program Files (x86)\\Winamp\\winamp.exe\"" + " \"" + file.getAbsolutePath() + "\"" );
                            
                            return result;
                            
                        }
                    }
                    catch ( Exception e ) {
                        e.printStackTrace();
                    }
                    
                }

                return createUnknowResult();

            } else if ( noun != null && "stop".equals( verb ) ){
                
                if ( runningProcess != null ){
                    runningProcess.destroy();
                    runningProcess = null;
                    
                    List< String > terms = new ArrayList< String >();
                    terms.add( "Stopping" );
                    terms.add( "the" );
                    terms.add( noun );
                    terms.add( "." );
                    
                    List< ProcessorItem > items = new ArrayList< ProcessorItem >();

                    ProcessorResult result = new ProcessorResult();
                    result.setItems( items );
                    result.setDate( new Date() );
                    result.setSubject( noun );
                    result.setResult( true );
                    result.setVerb( verb );
                    result.setOriginalTerms( action.getWords() );
                    result.setTerms( terms );
                    
                    return result;
                    
                } else {
                    
                    List< String > terms = new ArrayList< String >();
                    terms.add( "There is" );
                    terms.add( "no" );
                    terms.add( noun );
                    terms.add( "playing" );
                    terms.add( "." );
                    
                    List< ProcessorItem > items = new ArrayList< ProcessorItem >();

                    ProcessorResult result = new ProcessorResult();
                    result.setItems( items );
                    result.setDate( new Date() );
                    result.setSubject( noun );
                    result.setResult( true );
                    result.setVerb( verb );
                    result.setOriginalTerms( action.getWords() );
                    result.setTerms( terms );
                    
                    return result; 
                    
                }
                
            }
            
        }
                
        return null;
    }

    private void resetState() {

        kind = null;
        noun = null;
        option = null;
    }
    
    private boolean has( String kind ){
        
        for ( String k : genres.keySet() ){
            if ( k.equals( kind ) ){
                return true;
            }
        }
        
        return false;
    }


    private ProcessorResult createUnknowResult() {

        ProcessorResult result = new ProcessorResult();
        result.setSubject( option );
        result.setResult( false );
        result.setVerb( verb );
        
        return result;
    }

    
    public Process getRunningProcess() {
    
        return runningProcess;
    }

    
    public void setRunningProcess( Process runningProcess ) {
    
        this.runningProcess = runningProcess;
    }
    
}
