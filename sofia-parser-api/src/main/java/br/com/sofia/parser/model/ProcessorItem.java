package br.com.sofia.parser.model;


public class ProcessorItem {
    
    private String name;
    private String option;
    private String content;
    private String type;
    
    public String getName() {
    
        return name;
    }
    
    public void setName( String name ) {
    
        this.name = name;
    }
    
    public String getOption() {
    
        return option;
    }
    
    public void setOption( String option ) {
    
        this.option = option;
    }
    
    public String getContent() {
    
        return content;
    }
    
    public void setContent( String content ) {
    
        this.content = content;
    }

    
    public String getType() {
    
        return type;
    }

    
    public void setType( String type ) {
    
        this.type = type;
    }
    
}
