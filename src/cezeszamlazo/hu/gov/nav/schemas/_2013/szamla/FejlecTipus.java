//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.16 at 03:11:04 PM CET 
//


package cezeszamlazo.hu.gov.nav.schemas._2013.szamla;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for fejlec_tipus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="fejlec_tipus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="szlasorszam" type="{http://schemas.nav.gov.hu/2013/szamla}string_tipus"/>
 *         &lt;element name="szlatipus" type="{http://schemas.nav.gov.hu/2013/szamla}szlatipus_tipus"/>
 *         &lt;element name="szladatum" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="teljdatum" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "fejlec_tipus", propOrder = {
    "szlasorszam",
    "szlatipus",
    "szladatum",
    "teljdatum"
})
public class FejlecTipus {

    @XmlElement(required = true)
    protected String szlasorszam;
    @XmlElement(required = true)
    protected BigInteger szlatipus;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar szladatum;
    @XmlElement(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar teljdatum;

    /**
     * Gets the value of the szlasorszam property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSzlasorszam() {
        return szlasorszam;
    }

    /**
     * Sets the value of the szlasorszam property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSzlasorszam(String value) {
        this.szlasorszam = value;
    }

    /**
     * Gets the value of the szlatipus property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getSzlatipus() {
        return szlatipus;
    }

    /**
     * Sets the value of the szlatipus property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setSzlatipus(BigInteger value) {
        this.szlatipus = value;
    }

    /**
     * Gets the value of the szladatum property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getSzladatum() {
        return szladatum;
    }

    /**
     * Sets the value of the szladatum property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setSzladatum(XMLGregorianCalendar value) {
        this.szladatum = value;
    }

    /**
     * Gets the value of the teljdatum property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getTeljdatum() {
        return teljdatum;
    }

    /**
     * Sets the value of the teljdatum property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setTeljdatum(XMLGregorianCalendar value) {
        this.teljdatum = value;
    }

}