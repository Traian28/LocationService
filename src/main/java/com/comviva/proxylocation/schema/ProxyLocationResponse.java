
package com.comviva.proxylocation.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Clase Java para proxyLocationResponse complex type.
 * 
 * <p>El siguiente fragmento de esquema especifica el contenido que se espera que haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="proxyLocationResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="resultCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="resultDescription" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="net" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="rawData" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="mobileCountryCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="mobileNetworkCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="locationAreaCode" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="cellIdentityOrSAI" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "proxyLocationResponse", propOrder = {
    "resultCode",
    "resultDescription",
    "net",
    "rawData",
    "mobileCountryCode",
    "mobileNetworkCode",
    "locationAreaCode",
    "cellIdentityOrSAI"
})
public class ProxyLocationResponse {

    @XmlElement(required = true)
    protected String resultCode;
    @XmlElement(required = true, nillable = true)
    protected String resultDescription;
    @XmlElement(required = true, nillable = true)
    protected String net;
    @XmlElement(required = true, nillable = true)
    protected String rawData;
    @XmlElement(required = true, nillable = true)
    protected String mobileCountryCode;
    @XmlElement(required = true)
    protected String mobileNetworkCode;
    @XmlElement(required = true, nillable = true)
    protected String locationAreaCode;
    @XmlElement(required = true, nillable = true)
    protected String cellIdentityOrSAI;

    /**
     * Obtiene el valor de la propiedad resultCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultCode() {
        return resultCode;
    }

    /**
     * Define el valor de la propiedad resultCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultCode(String value) {
        this.resultCode = value;
    }

    /**
     * Obtiene el valor de la propiedad resultDescription.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultDescription() {
        return resultDescription;
    }

    /**
     * Define el valor de la propiedad resultDescription.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultDescription(String value) {
        this.resultDescription = value;
    }

    /**
     * Obtiene el valor de la propiedad net.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNet() {
        return net;
    }

    /**
     * Define el valor de la propiedad net.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNet(String value) {
        this.net = value;
    }

    /**
     * Obtiene el valor de la propiedad rawData.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRawData() {
        return rawData;
    }

    /**
     * Define el valor de la propiedad rawData.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRawData(String value) {
        this.rawData = value;
    }

    /**
     * Obtiene el valor de la propiedad mobileCountryCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileCountryCode() {
        return mobileCountryCode;
    }

    /**
     * Define el valor de la propiedad mobileCountryCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileCountryCode(String value) {
        this.mobileCountryCode = value;
    }

    /**
     * Obtiene el valor de la propiedad mobileNetworkCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMobileNetworkCode() {
        return mobileNetworkCode;
    }

    /**
     * Define el valor de la propiedad mobileNetworkCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMobileNetworkCode(String value) {
        this.mobileNetworkCode = value;
    }

    /**
     * Obtiene el valor de la propiedad locationAreaCode.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocationAreaCode() {
        return locationAreaCode;
    }

    /**
     * Define el valor de la propiedad locationAreaCode.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocationAreaCode(String value) {
        this.locationAreaCode = value;
    }

    /**
     * Obtiene el valor de la propiedad cellIdentityOrSAI.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCellIdentityOrSAI() {
        return cellIdentityOrSAI;
    }

    /**
     * Define el valor de la propiedad cellIdentityOrSAI.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCellIdentityOrSAI(String value) {
        this.cellIdentityOrSAI = value;
    }

}
