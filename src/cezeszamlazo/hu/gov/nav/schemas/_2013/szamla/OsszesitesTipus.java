//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.16 at 03:11:04 PM CET 
//


package cezeszamlazo.hu.gov.nav.schemas._2013.szamla;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for osszesites_tipus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="osszesites_tipus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="afarovat" type="{http://schemas.nav.gov.hu/2013/szamla}afarovat_tipus" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="vegosszeg" type="{http://schemas.nav.gov.hu/2013/szamla}vegosszeg_tipus"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "osszesites_tipus", propOrder = {
    "afarovat",
    "vegosszeg"
})
public class OsszesitesTipus {

    protected List<AfarovatTipus> afarovat;
    @XmlElement(required = true)
    protected VegosszegTipus vegosszeg;

    /**
     * Gets the value of the afarovat property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the afarovat property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAfarovat().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AfarovatTipus }
     * 
     * 
     */
    public List<AfarovatTipus> getAfarovat() {
        if (afarovat == null) {
            afarovat = new ArrayList<AfarovatTipus>();
        }
        return this.afarovat;
    }

    /**
     * Gets the value of the vegosszeg property.
     * 
     * @return
     *     possible object is
     *     {@link VegosszegTipus }
     *     
     */
    public VegosszegTipus getVegosszeg() {
        return vegosszeg;
    }

    /**
     * Sets the value of the vegosszeg property.
     * 
     * @param value
     *     allowed object is
     *     {@link VegosszegTipus }
     *     
     */
    public void setVegosszeg(VegosszegTipus value) {
        this.vegosszeg = value;
    }

    public void setAfarovat(List<AfarovatTipus> afarovat) {
        this.afarovat = afarovat;
    }
    
    
}
