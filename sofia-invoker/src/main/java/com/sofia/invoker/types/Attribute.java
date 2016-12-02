package com.sofia.invoker.types;

import java.io.Serializable;

/**
 * Provides the basic info for manipulating a WSDL element. 
 * 
 * @author rsalvo
 *
 */
public interface Attribute extends Serializable {
    
    Long getMinOccurs();

    void setMinOccurs( Long minOccurs );

    Long getMaxOccurs();

    void setMaxOccurs( Long maxOccurs );

    String getName();

    void setName(String name);
    
    String getPlainString();

}
