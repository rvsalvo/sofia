package br.com.sofia.lexer.model;


public class LexerFile {
    
    private final String path;
    private final String name;
    public static final String TERMINATION = "lr";
    
    public LexerFile( String path, String name ){
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
