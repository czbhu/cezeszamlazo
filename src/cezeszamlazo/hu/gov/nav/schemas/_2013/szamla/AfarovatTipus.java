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
 * <p>Java class for afarovat_tipus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="afarovat_tipus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="nettoar" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus"/>
 *         &lt;element name="adokulcs" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus"/>
 *         &lt;element name="adoertek" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus"/>
 *         &lt;element name="bruttoar" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "afarovat_tipus", propOrder = {
    "nettoar",
    "adokulcs",
    "adoertek",
    "bruttoar"
})
public class AfarovatTipus {

    @XmlElement(required = true)
    protected BigDecimal nettoar;
    @XmlElement(required = true)
    protected BigDecimal adokulcs;
    @XmlElement(required = true)
    protected BigDecimal adoertek;
    @XmlElement(required = true)
    protected BigDecimal bruttoar;

    /**
     * Gets the value of the nettoar property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNettoar() {
        return nettoar;
    }

    /**
     * Sets the value of the nettoar property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNettoar(BigDecimal value) {
        this.nettoar = value;
    }

    /**
     * Gets the value of the adokulcs property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAdokulcs() {
        return adokulcs;
    }

    /**
     * Sets the value of the adokulcs property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAdokulcs(BigDecimal value) {
        this.adokulcs = value;
    }

    /**
     * Gets the value of the adoertek property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAdoertek() {
        return adoertek;
    }

    /**
     * Sets the value of the adoertek property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAdoertek(BigDecimal value) {
        this.adoertek = value;
    }

    /**
     * Gets the value of the bruttoar property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBruttoar() {
        return bruttoar;
    }

    /**
     * Sets the value of the bruttoar property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBruttoar(BigDecimal value) {
        this.bruttoar = value;
    }

}
