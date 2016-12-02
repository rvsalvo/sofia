package br.com.sofia.parser.desktop;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;

import br.com.sofia.parser.LRAnalizerImpl;
import br.com.sofia.parser.Lr1ParserImpl;
import br.com.sofia.parser.model.Action;
import br.com.sofia.parser.model.Grammar;
import br.com.sofia.parser.model.GrammarFile;
import br.com.sofia.parser.model.ProcessorResult;
import br.com.sofia.parser.model.Stack;
import br.com.sofia.parser.model.Table;
import br.com.sofia.parser.processor.Processor;
import br.com.sofia.parser.processor.impl.MusicProcessor;
import br.com.sofia.parser.processor.impl.WebProcessor;


public class Desktop {
    
    private static final Logger log = Logger.getLogger( Desktop.class );

    public static void main( String[] args ){
        
        Lr1ParserImpl parser = new Lr1ParserImpl();
        
        Table table = parser.createTable( new GrammarFile( null, "Grammar09.gr" ) );
        
        final List< Processor > processors = new ArrayList< Processor >();
        
        final Map< String, Object > lastInfo = new HashMap< String, Object >();
        
        processors.add( new WebProcessor() );
        processors.add( new MusicProcessor() );
        
        parser.logGoTo();
        
        parser.logAction();
        
        final LRAnalizerImpl analizer = new LRAnalizerImpl( table, parser );
        
        final JTextField numberField = new JTextField(15);
        final JTextField resultField = new JTextField(20);
        
        JFrame myFrame = new JFrame( "Sofia App" );
        myFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        myFrame.setVisible(true);
        
        resultField.setEditable(false);
        myFrame.add(new JLabel("Enter command:"), BorderLayout.WEST);
        myFrame.add(numberField, BorderLayout.CENTER);
        myFrame.add(resultField, BorderLayout.SOUTH);
        JButton butt = new JButton("Process");
        butt.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            String text = numberField.getText();
            
            boolean result = false;
            
            try {
                result = analizer.analize( text );
            } catch ( Exception ex ){
                log.error( ex );
            }
            
            String response = null;
            
            resultField.setText( "" );
            
            if ( result ){
                
                System.out.println( "Expression recognized by grammar!" );
                
                Stack< Grammar > list = analizer.getGrammarTokens();
    
                System.out.println("\n\n");
                
                System.out.println( "Writing grammar:" );
                
                for ( Grammar g : list.collection() ){
                    System.out.println( "Rule: " + g.getRule() + " symbol: '" + g.getSymbol() + "' variable: '" + g.getVariable() + "'");
                }
    
                
                Grammar root = analizer.getGrammar();
                
                System.out.println( root.buildText() );
                
                Action action = analizer.analizeActions( root );
                
                if ( action != null ){
                    System.out.println( "Action: " + action.toString());
                    System.out.println( "\nResponse: " + analizer.getBasicResponse() );
                    resultField.setText( analizer.getBasicResponse() + "\n" );
                }
                
                Processor proc = (Processor)lastInfo.get( "lastProcessorExecuted" );
                
                ProcessorResult processorResult = proc != null ? proc.process( action ) : null;
            
                if ( processorResult != null ){
                    
                    response = analizer.analizeResult( processorResult );
                    lastInfo.put( "lastProcessorExecuted", proc );
                    
                } else {
                
                    for ( Processor processor : processors ){
    
                        processorResult = processor.process( action );
                    
                        if ( processorResult != null ){
                            response = analizer.analizeResult( processorResult );
                            lastInfo.put( "lastProcessorExecuted", processor );
                            break;
                        }
                        
                    }
                    
                }
                
                if ( response == null ){
                    response = analizer.analizeResult( null );
                }
                
            
            } else {
                response = "Command was not recognized.";
            }
            
            resultField.setText( resultField.getText() + response );
            
          }
        });
        
        myFrame.add(butt, BorderLayout.EAST);
        myFrame.pack();        
        
    }
    
    
    
}
