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
 * <p>Java class for termekdij_szerzes_atvallalas_tipus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="termekdij_szerzes_atvallalas_tipus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="bekezdes_pontja" type="{http://schemas.nav.gov.hu/2013/szamla}string_tipus"/>
 *         &lt;element name="bekezdes_alpontja" type="{http://schemas.nav.gov.hu/2013/szamla}string_tipus"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "termekdij_szerzes_atvallalas_tipus", propOrder = {
    "bekezdesPontja",
    "bekezdesAlpontja"
})
public class TermekdijSzerzesAtvallalasTipus {

    @XmlElement(name = "bekezdes_pontja", required = true)
    protected String bekezdesPontja;
    @XmlElement(name = "bekezdes_alpontja", required = true)
    protected String bekezdesAlpontja;

    /**
     * Gets the value of the bekezdesPontja property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBekezdesPontja() {
        return bekezdesPontja;
    }

    /**
     * Sets the value of the bekezdesPontja property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBekezdesPontja(String value) {
        this.bekezdesPontja = value;
    }

    /**
     * Gets the value of the bekezdesAlpontja property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBekezdesAlpontja() {
        return bekezdesAlpontja;
    }

    /**
     * Sets the value of the bekezdesAlpontja property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBekezdesAlpontja(String value) {
        this.bekezdesAlpontja = value;
    }

}