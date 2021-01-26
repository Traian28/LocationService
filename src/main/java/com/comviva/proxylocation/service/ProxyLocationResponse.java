
package com.comviva.proxylocation.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="proxyLocationReturn" type="{http://schema.proxyLocation.comviva.com}proxyLocationResponse"/&gt;
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
    "proxyLocationReturn"
})
@XmlRootElement(name = "proxyLocationResponse")
public class ProxyLocationResponse {

    @XmlElement(required = true)
    protected com.comviva.proxylocation.schema.ProxyLocationResponse proxyLocationReturn;

    /**
     * Obtiene el valor de la propiedad proxyLocationReturn.
     * 
     * @return
     *     possible object is
     *     {@link com.comviva.proxylocation.schema.ProxyLocationResponse }
     *     
     */
    public com.comviva.proxylocation.schema.ProxyLocationResponse getProxyLocationReturn() {
        return proxyLocationReturn;
    }

    /**
     * Define el valor de la propiedad proxyLocationReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link com.comviva.proxylocation.schema.ProxyLocationResponse }
     *     
     */
    public void setProxyLocationReturn(com.comviva.proxylocation.schema.ProxyLocationResponse value) {
        this.proxyLocationReturn = value;
    }

}
