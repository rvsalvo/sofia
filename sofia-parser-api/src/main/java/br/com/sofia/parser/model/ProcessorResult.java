package br.com.sofia.parser.model;

import java.util.Date;
import java.util.List;


public class ProcessorResult {
    
    private List< ProcessorItem > items;
    private boolean result;
    private List< String > terms;
    private List< String > originalTerms;
    private String subject;
    private String verb;
    private Date date;
    
    public List< ProcessorItem > getItems() {
    
        return items;
    }
    
    public void setItems( List< ProcessorItem > items ) {
    
        this.items = items;
    }
    
    public boolean isResult() {
    
        return result;
    }
    
    public void setResult( boolean result ) {
    
        this.result = result;
    }
    
    public List< String > getTerms() {
    
        return terms;
    }
    
    public void setTerms( List< String > terms ) {
    
        this.terms = terms;
    }

    
    public String getSubject() {
    
        return subject;
    }

    
    public void setSubject( String subject ) {
    
        this.subject = subject;
    }

    
    public Date getDate() {
    
        return date;
    }

    
    public void setDate( Date date ) {
    
        this.date = date;
    }

    
    public List< String > getOriginalTerms() {
    
        return originalTerms;
    }

    
    public void setOriginalTerms( List< String > originalTerms ) {
    
        this.originalTerms = originalTerms;
    }

    
    public String getVerb() {
    
        return verb;
    }

    
    public void setVerb( String verb ) {
    
        this.verb = verb;
    }
    
}
