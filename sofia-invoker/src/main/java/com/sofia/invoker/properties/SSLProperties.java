package com.sofia.invoker.properties;

/**
 * 
 * @author rsalvo
 *
 */
public class SSLProperties {
    
    private String keyStore;
    private String trustStore;
    private String password;
    private boolean disableCnCheck;
    
    public String getKeyStore() {
    
        return keyStore;
    }
    
    public void setKeyStore( String keyStore ) {
    
        this.keyStore = keyStore;
    }
    
    public String getTrustStore() {
    
        return trustStore;
    }
    
    public void setTrustStore( String trustStore ) {
    
        this.trustStore = trustStore;
    }
    
    public String getPassword() {
    
        return password;
    }
    
    public void setPassword( String password ) {
    
        this.password = password;
    }
    
    public boolean isDisableCnCheck() {
    
        return disableCnCheck;
    }
    
    public void setDisableCnCheck( boolean disableCnCheck ) {
    
        this.disableCnCheck = disableCnCheck;
    }
    
}
