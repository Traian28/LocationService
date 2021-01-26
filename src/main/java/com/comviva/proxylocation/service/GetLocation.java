
package com.comviva.proxylocation.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.comviva.proxylocation.schema.ProxyLocationRequest;


/**
 * <p>Clase Java para anonymous complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="proxyLocationRequest" type="{http://schema.proxyLocation.comviva.com}proxyLocationRequest"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "proxyLocationRequest"
})
@XmlRootElement(name = "getLocation")
public class GetLocation {

    @XmlElement(required = true)
    protected ProxyLocationRequest proxyLocationRequest;

    /**
     * Obtiene el valor de la propiedad proxyLocationRequest.
     * 
     * @return
     *     possible object is
     *     {@link ProxyLocationRequest }
     *     
     */
    public ProxyLocationRequest getProxyLocationRequest() {
        return proxyLocationRequest;
    }

    /**
     * Define el valor de la propiedad proxyLocationRequest.
     * 
     * @param value
     *     allowed object is
     *     {@link ProxyLocationRequest }
     *     
     */
    public void setProxyLocationRequest(ProxyLocationRequest value) {
        this.proxyLocationRequest = value;
    }

}
