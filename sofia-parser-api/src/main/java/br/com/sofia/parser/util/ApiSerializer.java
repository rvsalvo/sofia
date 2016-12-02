package br.com.sofia.parser.util;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import org.apache.log4j.Logger;


public class ApiSerializer {

    private static final Logger log = Logger.getLogger( ApiSerializer.class );

    public static void serialize( Serializable obj, String fileName ) {

        try {
            // use buffering
            OutputStream file = new FileOutputStream( fileName );
            OutputStream buffer = new BufferedOutputStream( file );
            ObjectOutput output = new ObjectOutputStream( buffer );
            try {
                output.writeObject( obj );
            }
            finally {
                output.close();
            }
        }
        catch ( IOException ex ) {
            log.error( "Cannot perform output.", ex );
        }

    }


    public static Serializable deserialize( String fileName ) {

        try {
            // use buffering
            InputStream file = new FileInputStream( fileName );
            InputStream buffer = new BufferedInputStream( file );
            ObjectInput input = new ObjectInputStream( buffer );
            try {
                return (Serializable) input.readObject();
            }
            finally {
                input.close();
            }
        }
        catch ( ClassNotFoundException ex ) {
            log.error( "Cannot perform input. Class not found.", ex );
        }
        catch ( IOException ex ) {
            log.error( "Cannot perform input.", ex );
        }
        return null;

    }

}
