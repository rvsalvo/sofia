package com.sofia.invoker.util;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXSource;

import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * JAXB object that holds marshallers and unmarshallers
 * 
 * @author bhlangonijr
 */
public class JAXBUtil {

	private final JAXBContext jaxbContext;
	private final Marshaller marshaller;
	private final Unmarshaller unmarshaller;
	private final XMLReader filter = new NamespaceFilter(null,false);

	public JAXBUtil(Marshaller marshaller,
			Unmarshaller unmarshaller,
			JAXBContext jaxbContext) {
		this.marshaller = marshaller;
		this.unmarshaller = unmarshaller;
		this.jaxbContext = jaxbContext;
	}

	public void setFormattedOutput(boolean o) throws PropertyException {
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
				new Boolean(o));
	}

	public JAXBContext getJaxbContext() {
		return jaxbContext;
	}

	public Object unmarshal(InputStream is) throws JAXBException { 
		return unmarshaller.unmarshal(is);
	}

	public Object unmarshal(File file) throws JAXBException { 
		return unmarshaller.unmarshal(file);
	}

	public Object unmarshal(Node node) throws JAXBException {
		return unmarshaller.unmarshal(node);
	}

	public void marshal(Object jaxbElement, Result result) throws JAXBException {
		marshaller.marshal(jaxbElement, result);		
	}

	public void marshal(Object jaxbElement, Node node) throws JAXBException {
		marshaller.marshal(jaxbElement, node);		
	}

	public void marshal(Object jaxbElement, OutputStream os)
			throws JAXBException {
		marshaller.marshal(jaxbElement, os);
	}

	public Object unmarshalWithNullFilter(Node node) throws JAXBException {		
		InputSource is = new InputSource();	
		SAXSource ss = new SAXSource(filter, is);
		return unmarshaller.unmarshal(ss);

	}

}
