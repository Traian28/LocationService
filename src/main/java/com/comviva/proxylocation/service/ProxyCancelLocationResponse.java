
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
 *         &lt;element name="proxyCancelLocationReturn" type="{http://schema.proxyLocation.comviva.com}proxyCancelLocationResponse"/&gt;
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
    "proxyCancelLocationReturn"
})
@XmlRootElement(name = "proxyCancelLocationResponse")
public class ProxyCancelLocationResponse {

    @XmlElement(required = true)
    protected com.comviva.proxylocation.schema.ProxyCancelLocationResponse proxyCancelLocationReturn;

    /**
     * Obtiene el valor de la propiedad proxyCancelLocationReturn.
     * 
     * @return
     *     possible object is
     *     {@link com.comviva.proxylocation.schema.ProxyCancelLocationResponse }
     *     
     */
    public com.comviva.proxylocation.schema.ProxyCancelLocationResponse getProxyCancelLocationReturn() {
        return proxyCancelLocationReturn;
    }

    /**
     * Define el valor de la propiedad proxyCancelLocationReturn.
     * 
     * @param value
     *     allowed object is
     *     {@link com.comviva.proxylocation.schema.ProxyCancelLocationResponse }
     *     
     */
    public void setProxyCancelLocationReturn(com.comviva.proxylocation.schema.ProxyCancelLocationResponse value) {
        this.proxyCancelLocationReturn = value;
    }

}
