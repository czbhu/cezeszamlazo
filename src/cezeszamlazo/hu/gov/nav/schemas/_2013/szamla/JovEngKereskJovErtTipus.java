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
 * <p>Java class for jov_eng_keresk_jov_ert_tipus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="jov_eng_keresk_jov_ert_tipus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="eng_szam" type="{http://schemas.nav.gov.hu/2013/szamla}string_tipus"/>
 *         &lt;element name="vevo_szam" type="{http://schemas.nav.gov.hu/2013/szamla}string_tipus"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "jov_eng_keresk_jov_ert_tipus", propOrder = {
    "engSzam",
    "vevoSzam"
})
public class JovEngKereskJovErtTipus {

    @XmlElement(name = "eng_szam", required = true)
    protected String engSzam;
    @XmlElement(name = "vevo_szam", required = true)
    protected String vevoSzam;

    /**
     * Gets the value of the engSzam property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEngSzam() {
        return engSzam;
    }

    /**
     * Sets the value of the engSzam property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEngSzam(String value) {
        this.engSzam = value;
    }

    /**
     * Gets the value of the vevoSzam property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVevoSzam() {
        return vevoSzam;
    }

    /**
     * Sets the value of the vevoSzam property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVevoSzam(String value) {
        this.vevoSzam = value;
    }

}
