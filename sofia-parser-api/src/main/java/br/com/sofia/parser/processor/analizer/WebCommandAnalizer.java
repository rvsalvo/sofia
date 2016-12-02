package br.com.sofia.parser.processor.analizer;


public class WebCommandAnalizer {
    
    private boolean search;
    private boolean web;
    private String words;
    private boolean images;
    private String address;
    private String docTypes;
    
    public boolean isSearch() {
    
        return search;
    }
    
    public void setSearch( boolean search ) {
    
        this.search = search;
    }
    
    public boolean isWeb() {
    
        return web;
    }
    
    public void setWeb( boolean web ) {
    
        this.web = web;
    }
    
    public String getWords() {
    
        return words;
    }
    
    public void setWords( String words ) {
    
        this.words = words;
    }
    
    public boolean isImages() {
    
        return images;
    }
    
    public void setImages( boolean images ) {
    
        this.images = images;
    }
    
    public String getAddress() {
    
        return address;
    }
    
    public void setAddress( String address ) {
    
        this.address = address;
    }
    
    public String getDocTypes() {
    
        return docTypes;
    }
    
    public void setDocTypes( String docTypes ) {
    
        this.docTypes = docTypes;
    }
    
}
