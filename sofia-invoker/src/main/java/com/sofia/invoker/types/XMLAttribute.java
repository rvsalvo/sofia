package com.sofia.invoker.types;

/**
 * 
 * @author rsalvo
 *
 */
public class XMLAttribute implements Attribute {
    
    private static final long serialVersionUID = 1L;
    
    private String name;
    private Long minOccurs;
    private Long maxOccurs;
    private Integer size;
    private String mask;   
    private boolean nillable;
    private String elementType;
    private String nameSpace;
    private String prefix;
    
    public XMLAttribute(){
        
    }
    
    public XMLAttribute( String name, String nameSpace ){
        this.name = name;
        this.nameSpace = nameSpace;
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setNameSpace( String nameSpace ) {
        this.nameSpace = nameSpace;
    }

    public String getElementType() {
        return elementType;
    }

    public void setElementType( String elementType ) {
        this.elementType = elementType;
    }

    public Long getMinOccurs() {
        return minOccurs;
    }

    public void setMinOccurs( Long minOccurs ) {
        this.minOccurs = minOccurs;
    }

    public Long getMaxOccurs() {
        return maxOccurs;
    }

    public void setMaxOccurs( Long maxOccurs ) {
        this.maxOccurs = maxOccurs;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize( Integer size ) {
        this.size = size;
    }

    public String getMask() {
        return mask;
    }

    public void setMask( String mask ) {
        this.mask = mask;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override    
    public void setName(String name) {
        this.name = name;
    }
    

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result
		+ ( ( elementType == null ) ? 0 : elementType.hashCode() );
	result = prime * result + ( ( name == null ) ? 0 : name.hashCode() );
	result = prime * result
		+ ( ( nameSpace == null ) ? 0 : nameSpace.hashCode() );
	return result;
    }

    @Override
    public boolean equals( Object obj ) {
	if ( this == obj )
	    return true;
	if ( obj == null )
	    return false;
	if ( getClass() != obj.getClass() )
	    return false;
	XMLAttribute other = ( XMLAttribute ) obj;
	if ( elementType == null ) {
	    if ( other.elementType != null )
		return false;
	} else if ( !elementType.equals( other.elementType ) )
	    return false;
	if ( name == null ) {
	    if ( other.name != null )
		return false;
	} else if ( !name.equals( other.name ) )
	    return false;
	if ( nameSpace == null ) {
	    if ( other.nameSpace != null )
		return false;
	} else if ( !nameSpace.equals( other.nameSpace ) )
	    return false;
	return true;
    }

    public boolean isNillable() {
        return nillable;
    }

    public void setNillable( boolean nillable ) {
        this.nillable = nillable;
    }
    
    private void logAttribute( Attribute attribute, String nivel, StringBuilder print ){
        
        if ( attribute instanceof ComplexType ){
            ComplexType cAtt = (ComplexType)attribute;
            print.append( nivel + "<ct:" + cAtt.getName() + ">\n");
            for ( Attribute att : cAtt.getAttributes() ){
                logAttribute( att, nivel + " ", print );
            }
            print.append( nivel + "</ct:" + cAtt.getName() + ">\n");            
        } else if ( attribute instanceof Primitive ){
            Primitive pr = (Primitive)attribute;
            print.append( nivel + "<pr:" + pr.getName() + ">" + pr.getValue().toString() + "</pr:" + pr.getName() + ">\n" );
        }
    }
    
    @Override
    public String toString(){
        
        StringBuilder print = new StringBuilder();
        logAttribute( this, "", print );
        
        return print.toString();
    }

    @Override
    public String getPlainString() {

        StringBuilder print = new StringBuilder();
        
        writeAttribute( this, print );
        
        return print.toString();
    }
    
    private void writeAttribute( Attribute attribute, StringBuilder print ){
        
        if ( attribute instanceof ComplexType ){
            ComplexType cAtt = (ComplexType)attribute;
            print.append( "<" + cAtt.getName() + ">");
            for ( Attribute att : cAtt.getAttributes() ){
                writeAttribute( att, print );
            }
            print.append( "</" + cAtt.getName() + ">");            
        } else if ( attribute instanceof Primitive ){
            Primitive pr = (Primitive)attribute;
            print.append( "<" + pr.getName() + ">" + pr.getValue().toString() + "</" + pr.getName() + ">" );
        }
    }

    
    public String getPrefix() {
    
        return prefix;
    }

    
    public void setPrefix( String prefix ) {
    
        this.prefix = prefix;
    }
    
    
    
}
