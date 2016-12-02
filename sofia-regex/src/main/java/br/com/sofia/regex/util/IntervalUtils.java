package br.com.sofia.regex.util;


import java.util.LinkedList;
import java.util.Vector;

import br.com.sofia.regex.analyzer.model.Interval;


/**
 * 
 * @author Rodrigo Salvo
 *
 */
public class IntervalUtils {

    public static Vector< Interval > intersection( Vector< Interval > intervals, Vector< Interval > other ) {

        Vector< Interval > result = new Vector< Interval >();

        int i = 0;
        int j = 0;

        int size = intervals.size();
        int setSize = other.size();

        while ( i < size && j < setSize ) {
            Interval x = (Interval) intervals.elementAt( i );
            Interval y = (Interval) other.elementAt( j );

            if ( x.getEnd() < y.getStart() ) {
                i++;
                continue;
            }

            if ( y.getEnd() < x.getStart() ) {
                j++;
                continue;
            }

            result.addElement( new Interval( (char) Math.max( x.getStart(), y.getStart() ), (char) Math.min(
                x.getEnd(),
                y.getEnd() ) ) );

            if ( x.getEnd() >= y.getEnd() ){
                j++;
            }
            if ( y.getEnd() >= x.getEnd() ){
                i++;
            }
        }

        return result;
    }


    public static Vector< Interval > diff( Vector< Interval > intervals1, Vector< Interval > other ) {

        int j = 0;

        Vector< Interval > finalResult = new Vector< Interval >();
        
        LinkedList< Interval > intervals = new LinkedList< Interval >();
        for ( Interval o : intervals1 ){
            intervals.addLast( o );
        }

        while ( !intervals.isEmpty() && j < other.size() ) {

            Interval x = (Interval) intervals.pop();
            Interval y = (Interval) other.elementAt( j );

            if ( x.getStart() < y.getStart() && x.getEnd() > y.getEnd() ) {
                finalResult.add( new Interval( x.getStart(), x.getEnd() < y.getStart() ? x.getEnd() : (char) ( y.getStart() - 1 ) ) );
                intervals.push( new Interval( (char) ( y.getEnd() + 1 ), x.getEnd() ) );
                j++;
            }
            else if ( x.getStart() > y.getStart() && x.getEnd() > y.getEnd() ) {
                intervals.push( new Interval( x.getStart() > y.getEnd() ? x.getStart() : (char) ( y.getEnd() + 1 ), x.getEnd() ) );
                j++;
            }
            else if ( x.getStart() < y.getStart() && x.getEnd() < y.getEnd() ) {
                finalResult.add( new Interval( x.getStart(), x.getEnd() < y.getStart() ? x.getEnd() : (char) ( y.getStart() - 1 ) ) );
            }

        }
        
        while ( !intervals.isEmpty() ){
            finalResult.add( intervals.pop() );
        }

        return finalResult;
    }

}
