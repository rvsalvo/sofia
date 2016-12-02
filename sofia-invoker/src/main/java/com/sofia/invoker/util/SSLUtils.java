package com.sofia.invoker.util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.cxf.configuration.jsse.TLSClientParameters;

/**
 * 
 * @author rsalvo
 *
 */
public class SSLUtils {

    public static TLSClientParameters createTLSClientParameters(
        String keyStoreFile,
        String trustStoreFile,
        String keyPassword,
        boolean disableCnCheck ) throws FileNotFoundException, IOException, GeneralSecurityException {

        TLSClientParameters tlsCP = new TLSClientParameters();
        KeyStore keyStore = KeyStore.getInstance( "JKS" );
        keyStore.load( new FileInputStream( keyStoreFile ), keyPassword.toCharArray() );
        KeyManager[] myKeyManagers = getKeyManagers( keyStore, keyPassword );
        tlsCP.setKeyManagers( myKeyManagers );

        KeyStore trustStore = KeyStore.getInstance( "JKS" );
        trustStore.load( new FileInputStream( trustStoreFile ), keyPassword.toCharArray() );
        TrustManager[] myTrustStoreKeyManagers = getTrustManagers( trustStore );
        tlsCP.setTrustManagers( myTrustStoreKeyManagers );

        tlsCP.setDisableCNCheck( disableCnCheck );

        return tlsCP;
    }


    public static TrustManager[] getTrustManagers( KeyStore trustStore ) throws NoSuchAlgorithmException,
        KeyStoreException {

        String alg = KeyManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory fac = TrustManagerFactory.getInstance( alg );
        fac.init( trustStore );
        return fac.getTrustManagers();
    }


    public static KeyManager[] getKeyManagers( KeyStore keyStore, String keyPassword ) throws GeneralSecurityException,
        IOException {

        String alg = KeyManagerFactory.getDefaultAlgorithm();
        char[] keyPass = keyPassword != null ? keyPassword.toCharArray() : null;
        KeyManagerFactory fac = KeyManagerFactory.getInstance( alg );
        fac.init( keyStore, keyPass );
        return fac.getKeyManagers();
    }

}
