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
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gyujto_szla_tipus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="gyujto_szla_tipus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="gyujtocsopo_ossz" type="{http://schemas.nav.gov.hu/2013/szamla}string_tipus"/>
 *         &lt;element name="gyujtocsopo_nossz" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus"/>
 *         &lt;element name="gyujtocsopo_bossz" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "gyujto_szla_tipus", propOrder = {
    "gyujtocsopoOssz",
    "gyujtocsopoNossz",
    "gyujtocsopoBossz"
})
public class GyujtoSzlaTipus {

    @XmlElement(name = "gyujtocsopo_ossz", required = true)
    protected String gyujtocsopoOssz;
    @XmlElement(name = "gyujtocsopo_nossz", required = true)
    protected BigDecimal gyujtocsopoNossz;
    @XmlElement(name = "gyujtocsopo_bossz", required = true)
    protected BigDecimal gyujtocsopoBossz;

    /**
     * Gets the value of the gyujtocsopoOssz property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGyujtocsopoOssz() {
        return gyujtocsopoOssz;
    }

    /**
     * Sets the value of the gyujtocsopoOssz property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGyujtocsopoOssz(String value) {
        this.gyujtocsopoOssz = value;
    }

    /**
     * Gets the value of the gyujtocsopoNossz property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGyujtocsopoNossz() {
        return gyujtocsopoNossz;
    }

    /**
     * Sets the value of the gyujtocsopoNossz property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGyujtocsopoNossz(BigDecimal value) {
        this.gyujtocsopoNossz = value;
    }

    /**
     * Gets the value of the gyujtocsopoBossz property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGyujtocsopoBossz() {
        return gyujtocsopoBossz;
    }

    /**
     * Sets the value of the gyujtocsopoBossz property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGyujtocsopoBossz(BigDecimal value) {
        this.gyujtocsopoBossz = value;
    }

}
