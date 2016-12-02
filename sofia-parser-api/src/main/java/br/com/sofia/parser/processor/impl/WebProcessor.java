package br.com.sofia.parser.processor.impl;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;

import br.com.sofia.parser.command.Command;
import br.com.sofia.parser.model.Action;
import br.com.sofia.parser.model.ProcessorItem;
import br.com.sofia.parser.model.ProcessorResult;
import br.com.sofia.parser.processor.AbstractProcessor;
import br.com.sofia.parser.util.GoogleResults;

import com.google.gson.Gson;


public class WebProcessor extends AbstractProcessor {

    private static final String[] VERBS = new String[] { "search", "find", "look", "grab", "browse" };

    private String verb;

    private String noun;

    private String option;


    @Override
    public ProcessorResult process( Action action ) {

        if ( Command.ACTION.equals( Command.valueOf( action.getAction().toUpperCase() ) ) ) {

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

                if ( noun == null || ( !"web".equals( noun ) && !"internet".equals( noun ) ) ) {
                    return null;
                }

            }

            index = getStringIndex( OPTION, action.getComponents() );

            if ( index != -1 ) {

                option = action.getWords().get( index );

                if ( option == null ) {
                    return createUnknowResult();
                }

                try {
                    List< ProcessorItem > items = search( option );

                    List< String > terms = new ArrayList< String >();
                    terms.add( "I" );
                    terms.add( "found" );
                    terms.add( String.valueOf( items.size() ) );
                    terms.add( "resuls" );
                    terms.add( "for" );
                    terms.add( "your" );
                    terms.add( "search" );
                    terms.add( "." );
                    
                    ProcessorResult result = new ProcessorResult();
                    result.setItems( items );
                    result.setDate( new Date() );
                    result.setSubject( option );
                    result.setResult( true );
                    result.setVerb( verb );
                    result.setOriginalTerms( action.getWords() );
                    result.setTerms( terms );
                    
                    return result;

                }
                catch ( Exception e ) {
                    e.printStackTrace();
                }

                return createUnknowResult();

            }

        }
        
        return null;
    }


    private ProcessorResult createUnknowResult() {

        ProcessorResult result = new ProcessorResult();
        result.setSubject( option );
        result.setResult( false );
        result.setVerb( verb );
        
        return result;
    }


    public void otherTest( String option ) {

        /*
         * ODataConsumer c =
         * ODataConsumer.create("http://odata.netflix.com/Catalog/");
         * 
         * Entry< String, String > entry = ODataClientRequest.post( "", entry )
         * Enumerable<OObject> e = c.callFunction("Movies").execute();
         */
    }


    public void search2( String option ) throws Exception {

        String google = "https://api.datamarket.azure.com/Data.ashx/Bing/Search/Web?$format=json&Query=";

        String charset = "UTF-8";

        URL url = new URL( google + URLEncoder.encode( option, charset ) );

        try {
            String name = "ZNtpFGogI9lQU6PvcZ+j7ke6KRYp5R3EhdCzeTVN2gk=";
            String password = "ZNtpFGogI9lQU6PvcZ+j7ke6KRYp5R3EhdCzeTVN2gk=";

            String authString = name + ":" + password;
            System.out.println( "auth string: " + authString );
            byte[] authEncBytes = Base64.encodeBase64( authString.getBytes() );
            String authStringEnc = new String( authEncBytes );
            System.out.println( "Base64 encoded auth string: " + authStringEnc );

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier( new HostnameVerifier() {

                public boolean verify( String hostname, SSLSession session ) {

                    // check hostname/session
                    return true;
                }
            } );
            conn.setRequestProperty( "Authorization", "Basic " + authStringEnc );
            conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded; charset=" + charset );
            conn.setRequestProperty( "Accept-Charset", charset );
            conn.setRequestProperty( "Content-Length", "0" );
            conn.setRequestMethod( "POST" );
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader( is );

            int numCharsRead;
            char[] charArray = new char[ 1024 ];
            StringBuffer sb = new StringBuffer();
            while ( ( numCharsRead = isr.read( charArray ) ) > 0 ) {
                sb.append( charArray, 0, numCharsRead );
            }
            String result = sb.toString();

            System.out.println( "*** BEGIN ***" );
            System.out.println( result );
            System.out.println( "*** END ***" );
        }
        catch ( MalformedURLException e ) {
            e.printStackTrace();
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }


    public List< ProcessorItem > search( String option ) throws Exception {
        
        setTrustAllCerts();

        String google = "https://www.googleapis.com/customsearch/v1?key=AIzaSyA6Qiz3Oo8rlVlM4AUiV8tqKPPXYevtt6A&g1=br&cx=017070547341497860720:-uab5q35_sc&q=";

        String charset = "UTF-8";

        URL url = new URL( google + URLEncoder.encode( option, charset ) );

        try {

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier( new HostnameVerifier() {

                public boolean verify( String hostname, SSLSession session ) {

                    // check hostname/session
                    return true;
                }
            } );
            conn.setRequestMethod( "GET" );
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader( is );
            
            GoogleResults results = new Gson().fromJson( isr, GoogleResults.class );
            System.out.println( "total found: " + results.getItems().size() );
            
            List< ProcessorItem > items = new ArrayList< ProcessorItem >();

            for ( GoogleResults.Item result : results.getItems() ) {
                ProcessorItem item = new ProcessorItem();
                item.setName( result.getTitle() );
                item.setOption( result.getLink() );
                item.setType( this.getClass().getName() );
                items.add( item );
            }            
            
            return items;

        }
        catch ( MalformedURLException e ) {
            e.printStackTrace();
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        
        return null;
    }

    
   public List< ProcessorItem > search4( String option ) throws Exception {
        
        String google = "http://www.google.com?q=";

        String charset = "UTF-8";

        URL url = new URL( google + URLEncoder.encode( option, charset ) );

        try {

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod( "GET" );
            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader( is );

            int numCharsRead;
            char[] charArray = new char[ 1024 ];
            StringBuffer sb = new StringBuffer();
            while ( ( numCharsRead = isr.read( charArray ) ) > 0 ) {
                sb.append( charArray, 0, numCharsRead );
            }
            String result = sb.toString();

            System.out.println( "*** BEGIN ***" );
            System.out.println( result );
            System.out.println( "*** END ***" );
        }
        catch ( MalformedURLException e ) {
            e.printStackTrace();
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
        
        return null;
    }

/*
    public List< ProcessorItem > search( String option ) throws Exception {

        String google = "http://ajax.googleapis.com/ajax/services/search/web?v=1.0&q=";
        String charset = "UTF-8";

        URL url = new URL( google + URLEncoder.encode( option, charset ) );
        Reader reader = new InputStreamReader( url.openStream(), charset );
        GoogleResults results = new Gson().fromJson( reader, GoogleResults.class );

        System.out.println( "Searching the web for keyword: " + option );

        System.out.println( "Found " + results.getResponseData().getResults().size() + " results " );

        List< ProcessorItem > items = new ArrayList< ProcessorItem >();

        for ( GoogleResults.Result result : results.getResponseData().getResults() ) {
            ProcessorItem item = new ProcessorItem();
            item.setName( result.getTitle() );
            item.setOption( result.getUrl() );
            item.setType( this.getClass().getName() );
            items.add( item );
        }

        return null;
    }*/


    private void setTrustAllCerts() throws Exception {

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                return null;
            }


            public void checkClientTrusted( java.security.cert.X509Certificate[] certs, String authType ) {

            }


            public void checkServerTrusted( java.security.cert.X509Certificate[] certs, String authType ) {

            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance( "SSL" );
            sc.init( null, trustAllCerts, new java.security.SecureRandom() );
            HttpsURLConnection.setDefaultSSLSocketFactory( sc.getSocketFactory() );
            HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier() {

                public boolean verify( String urlHostName, SSLSession session ) {

                    return true;
                }
            } );
        }
        catch ( Exception e ) {
            // We can not recover from this exception.
            e.printStackTrace();
        }
    }

}