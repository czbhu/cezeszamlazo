//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.16 at 03:11:04 PM CET 
//


package cezeszamlazo.hu.gov.nav.schemas._2013.szamla;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for vizikozl_tipus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="vizikozl_tipus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="hajo_hossz" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus" minOccurs="0"/>
 *         &lt;element name="vizitev" type="{http://schemas.nav.gov.hu/2013/szamla}string_tipus" minOccurs="0"/>
 *         &lt;element name="forgba_datum" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *         &lt;element name="hajozott_ora" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "vizikozl_tipus", propOrder = {
    "hajoHossz",
    "vizitev",
    "forgbaDatum",
    "hajozottOra"
})
public class VizikozlTipus {

    @XmlElement(name = "hajo_hossz")
    protected BigDecimal hajoHossz;
    protected String vizitev;
    @XmlElement(name = "forgba_datum")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar forgbaDatum;
    @XmlElement(name = "hajozott_ora")
    protected BigDecimal hajozottOra;

    /**
     * Gets the value of the hajoHossz property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHajoHossz() {
        return hajoHossz;
    }

    /**
     * Sets the value of the hajoHossz property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHajoHossz(BigDecimal value) {
        this.hajoHossz = value;
    }

    /**
     * Gets the value of the vizitev property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVizitev() {
        return vizitev;
    }

    /**
     * Sets the value of the vizitev property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVizitev(String value) {
        this.vizitev = value;
    }

    /**
     * Gets the value of the forgbaDatum property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getForgbaDatum() {
        return forgbaDatum;
    }

    /**
     * Sets the value of the forgbaDatum property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setForgbaDatum(XMLGregorianCalendar value) {
        this.forgbaDatum = value;
    }

    /**
     * Gets the value of the hajozottOra property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getHajozottOra() {
        return hajozottOra;
    }

    /**
     * Sets the value of the hajozottOra property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setHajozottOra(BigDecimal value) {
        this.hajozottOra = value;
    }

}
