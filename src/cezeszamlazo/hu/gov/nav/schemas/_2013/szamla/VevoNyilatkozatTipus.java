//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.16 at 03:11:04 PM CET 
//


package cezeszamlazo.hu.gov.nav.schemas._2013.szamla;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for vevo_nyilatkozat_tipus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vevo_nyilatkozat_tipus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="vevo_nem_fizet" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="iktatott_idoszak" type="{http://schemas.nav.gov.hu/2013/szamla}string_tipus"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vevo_nyilatkozat_tipus", propOrder = {
    "vevoNemFizet",
    "iktatottIdoszak"
})
public class VevoNyilatkozatTipus {

    @XmlElement(name = "vevo_nem_fizet")
    protected boolean vevoNemFizet;
    @XmlElement(name = "iktatott_idoszak", required = true)
    protected String iktatottIdoszak;

    /**
     * Gets the value of the vevoNemFizet property.
     * 
     */
    public boolean isVevoNemFizet() {
        return vevoNemFizet;
    }

    /**
     * Sets the value of the vevoNemFizet property.
     * 
     */
    public void setVevoNemFizet(boolean value) {
        this.vevoNemFizet = value;
    }

    /**
     * Gets the value of the iktatottIdoszak property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIktatottIdoszak() {
        return iktatottIdoszak;
    }

    /**
     * Sets the value of the iktatottIdoszak property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIktatottIdoszak(String value) {
        this.iktatottIdoszak = value;
    }

}
