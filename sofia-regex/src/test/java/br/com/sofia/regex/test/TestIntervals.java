package br.com.sofia.regex.test;

import java.util.Vector;

import org.junit.Test;

import br.com.sofia.regex.analyzer.model.Interval;
import br.com.sofia.regex.util.IntervalUtils;



public class TestIntervals {
    
    @Test
    public void test(){
        
        Vector< Interval > fullInterval = new Vector< Interval >();
        fullInterval.add( new Interval( (char)28, (char)127 ) );
        fullInterval.add( new Interval( (char)189, (char)232 ) );
        fullInterval.add( new Interval( (char)288, (char)292 ) );

        for ( Interval i : fullInterval ){
            System.out.print( " X: [" + (int)i.getStart() + " - " + (int)i.getEnd() + "]" );
        }
        System.out.println( "" );
        
        Vector< Interval > intervals = new Vector< Interval >();
        intervals.add( new Interval( (char)48, (char)121 ) );
        intervals.add( new Interval( (char)123, (char)191 ) );
        intervals.add( new Interval( (char)202, (char)291 ) );
        intervals.add( new Interval( (char)302, (char)491 ) );        
        
        for ( Interval i : intervals ){
            System.out.print( " Y: [" + (int)i.getStart() + " - " + (int)i.getEnd() + "]" );
        }        
        System.out.println( "" );
        
        Vector< Interval > result = IntervalUtils.diff( fullInterval, intervals );
        
        for ( Interval i : result ){
            System.out.print( " Result: [" + (int)i.getStart() + " - " + (int)i.getEnd() + "]" );
        }
        System.out.println( "" );
        
    }
    
    @Test
    public void testOther(){
        
        Vector< Interval > fullInterval = new Vector< Interval >();
        fullInterval.add( new Interval( (char)28, (char)127 ) );
        fullInterval.add( new Interval( (char)149, (char)192 ) );
        fullInterval.add( new Interval( (char)201, (char)232 ) );

        for ( Interval i : fullInterval ){
            System.out.print( " X: [" + (int)i.getStart() + " - " + (int)i.getEnd() + "]" );
        }
        System.out.println( "" );
        
        Vector< Interval > intervals = new Vector< Interval >();
        intervals.add( new Interval( (char)48, (char)131 ) );
        intervals.add( new Interval( (char)139, (char)191 ) );
        
        for ( Interval i : intervals ){
            System.out.print( " Y: [" + (int)i.getStart() + " - " + (int)i.getEnd() + "]" );
        }        
        System.out.println( "" );
        
        Vector< Interval > result = IntervalUtils.diff( fullInterval, intervals );
        
        for ( Interval i : result ){
            System.out.print( " Result: [" + (int)i.getStart() + " - " + (int)i.getEnd() + "]" );
        }
        System.out.println( "" );
        
    }    

}
