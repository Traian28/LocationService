
package com.comviva.proxylocation.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.comviva.proxylocation.schema.ProxyCancelLocationRequest;


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
 *         &lt;element name="proxyCancelLocationRequest" type="{http://schema.proxyLocation.comviva.com}proxyCancelLocationRequest"/&gt;
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
    "proxyCancelLocationRequest"
})
@XmlRootElement(name = "cancelLocation")
public class CancelLocation {

    @XmlElement(required = true)
    protected ProxyCancelLocationRequest proxyCancelLocationRequest;

    /**
     * Obtiene el valor de la propiedad proxyCancelLocationRequest.
     * 
     * @return
     *     possible object is
     *     {@link ProxyCancelLocationRequest }
     *     
     */
    public ProxyCancelLocationRequest getProxyCancelLocationRequest() {
        return proxyCancelLocationRequest;
    }

    /**
     * Define el valor de la propiedad proxyCancelLocationRequest.
     * 
     * @param value
     *     allowed object is
     *     {@link ProxyCancelLocationRequest }
     *     
     */
    public void setProxyCancelLocationRequest(ProxyCancelLocationRequest value) {
        this.proxyCancelLocationRequest = value;
    }

}
