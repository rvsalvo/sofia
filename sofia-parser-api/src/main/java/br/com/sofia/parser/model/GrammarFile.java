package br.com.sofia.parser.model;


public class GrammarFile {
    
    private final String path;
    private final String name;
    public static final String TERMINATION = "gr";
    
    public GrammarFile( String path, String name ){
        this.path = path != null ? path : "";
        this.name = name;
    }
    
    public String getActionFile(){
        return path + "/" + name.replace( "." + TERMINATION, "Action." + TERMINATION );
    }
    
    public String getFile(){
        return path + "/" + name;
    }
    
    public String getPath() {
    
        return path;
    }
    
    public String getName() {
    
        return name;
    }
    
}
