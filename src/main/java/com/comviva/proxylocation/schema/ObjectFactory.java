
package com.comviva.proxylocation.schema;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.comviva.proxylocation.schema package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.comviva.proxylocation.schema
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ProxyLocationRequest }
     * 
     */
    public ProxyLocationRequest createProxyLocationRequest() {
        return new ProxyLocationRequest();
    }

    /**
     * Create an instance of {@link ProxyLocationResponse }
     * 
     */
    public ProxyLocationResponse createProxyLocationResponse() {
        return new ProxyLocationResponse();
    }

    /**
     * Create an instance of {@link ProxyCancelLocationRequest }
     * 
     */
    public ProxyCancelLocationRequest createProxyCancelLocationRequest() {
        return new ProxyCancelLocationRequest();
    }

    /**
     * Create an instance of {@link ProxyCancelLocationResponse }
     * 
     */
    public ProxyCancelLocationResponse createProxyCancelLocationResponse() {
        return new ProxyCancelLocationResponse();
    }

}
