//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.12.16 at 03:11:04 PM CET 
//


package cezeszamlazo.hu.gov.nav.schemas._2013.szamla;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for termek_szolgaltatas_tetelek_tipus complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="termek_szolgaltatas_tetelek_tipus">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="termeknev" type="{http://schemas.nav.gov.hu/2013/szamla}string_tipus"/>
 *         &lt;element name="gyujto_szla_csoport" type="{http://schemas.nav.gov.hu/2013/szamla}string_tipus" minOccurs="0"/>
 *         &lt;element name="eloleg" type="{http://schemas.nav.gov.hu/2013/szamla}eloleg_tipus" minOccurs="0"/>
 *         &lt;element name="besorszam" type="{http://schemas.nav.gov.hu/2013/szamla}string_tipus" minOccurs="0"/>
 *         &lt;element name="menny" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus"/>
 *         &lt;element name="mertekegys" type="{http://schemas.nav.gov.hu/2013/szamla}string_tipus"/>
 *         &lt;element name="kozv_szolgaltatas" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/>
 *         &lt;element name="nettoar" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus" minOccurs="0"/>
 *         &lt;element name="nettoegysar" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus" minOccurs="0"/>
 *         &lt;element name="dohany_ar" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus" minOccurs="0"/>
 *         &lt;element name="adokulcs" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus" minOccurs="0"/>
 *         &lt;element name="adoertek" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus" minOccurs="0"/>
 *         &lt;element name="szazalekertek" type="{http://schemas.nav.gov.hu/2013/szamla}szazalekertek_tipus" minOccurs="0"/>
 *         &lt;element name="bruttoar" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus" minOccurs="0"/>
 *         &lt;element name="arengedm" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus" minOccurs="0"/>
 *         &lt;element name="vellenertek" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus" minOccurs="0"/>
 *         &lt;element name="vkozteher" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus" minOccurs="0"/>
 *         &lt;element name="vktg" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus" minOccurs="0"/>
 *         &lt;element name="vkistag" type="{http://schemas.nav.gov.hu/2013/szamla}decimal_tipus" minOccurs="0"/>
 *         &lt;element name="kozl_eszk_inf" type="{http://schemas.nav.gov.hu/2013/szamla}kozl_eszk_inf_tipus" minOccurs="0"/>
 *         &lt;element name="asvanyolaj" type="{http://schemas.nav.gov.hu/2013/szamla}asvanyolaj_tipus" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "termek_szolgaltatas_tetelek_tipus", propOrder = {
    "termeknev",
    "gyujtoSzlaCsoport",
    "eloleg",
    "besorszam",
    "menny",
    "mertekegys",
    "kozvSzolgaltatas",
    "nettoar",
    "nettoegysar",
    "dohanyAr",
    "adokulcs",
    "adoertek",
    "szazalekertek",
    "bruttoar",
    "arengedm",
    "vellenertek",
    "vkozteher",
    "vktg",
    "vkistag",
    "kozlEszkInf",
    "asvanyolaj"
})
public class TermekSzolgaltatasTetelekTipus {

    @XmlElement(required = true)
    protected String termeknev;
    @XmlElement(name = "gyujto_szla_csoport")
    protected String gyujtoSzlaCsoport;
    protected BigInteger eloleg;
    protected String besorszam;
    @XmlElement(required = true)
    protected BigDecimal menny;
    @XmlElement(required = true)
    protected String mertekegys;
    @XmlElement(name = "kozv_szolgaltatas")
    protected Boolean kozvSzolgaltatas;
    protected BigDecimal nettoar;
    protected BigDecimal nettoegysar;
    @XmlElement(name = "dohany_ar")
    protected BigDecimal dohanyAr;
    protected BigDecimal adokulcs;
    protected BigDecimal adoertek;
    protected BigDecimal szazalekertek;
    protected BigDecimal bruttoar;
    protected BigDecimal arengedm;
    protected BigDecimal vellenertek;
    protected BigDecimal vkozteher;
    protected BigDecimal vktg;
    protected BigDecimal vkistag;
    @XmlElement(name = "kozl_eszk_inf")
    protected KozlEszkInfTipus kozlEszkInf;
    protected AsvanyolajTipus asvanyolaj;

    /**
     * Gets the value of the termeknev property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTermeknev() {
        return termeknev;
    }

    /**
     * Sets the value of the termeknev property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTermeknev(String value) {
        this.termeknev = value;
    }

    /**
     * Gets the value of the gyujtoSzlaCsoport property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGyujtoSzlaCsoport() {
        return gyujtoSzlaCsoport;
    }

    /**
     * Sets the value of the gyujtoSzlaCsoport property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGyujtoSzlaCsoport(String value) {
        this.gyujtoSzlaCsoport = value;
    }

    /**
     * Gets the value of the eloleg property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEloleg() {
        return eloleg;
    }

    /**
     * Sets the value of the eloleg property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEloleg(BigInteger value) {
        this.eloleg = value;
    }

    /**
     * Gets the value of the besorszam property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBesorszam() {
        return besorszam;
    }

    /**
     * Sets the value of the besorszam property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBesorszam(String value) {
        this.besorszam = value;
    }

    /**
     * Gets the value of the menny property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getMenny() {
        return menny;
    }

    /**
     * Sets the value of the menny property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setMenny(BigDecimal value) {
        this.menny = value;
    }

    /**
     * Gets the value of the mertekegys property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMertekegys() {
        return mertekegys;
    }

    /**
     * Sets the value of the mertekegys property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMertekegys(String value) {
        this.mertekegys = value;
    }

    /**
     * Gets the value of the kozvSzolgaltatas property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isKozvSzolgaltatas() {
        return kozvSzolgaltatas;
    }

    /**
     * Sets the value of the kozvSzolgaltatas property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setKozvSzolgaltatas(Boolean value) {
        this.kozvSzolgaltatas = value;
    }

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
     * Gets the value of the nettoegysar property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getNettoegysar() {
        return nettoegysar;
    }

    /**
     * Sets the value of the nettoegysar property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setNettoegysar(BigDecimal value) {
        this.nettoegysar = value;
    }

    /**
     * Gets the value of the dohanyAr property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getDohanyAr() {
        return dohanyAr;
    }

    /**
     * Sets the value of the dohanyAr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setDohanyAr(BigDecimal value) {
        this.dohanyAr = value;
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
     * Gets the value of the szazalekertek property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getSzazalekertek() {
        return szazalekertek;
    }

    /**
     * Sets the value of the szazalekertek property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setSzazalekertek(BigDecimal value) {
        this.szazalekertek = value;
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

    /**
     * Gets the value of the arengedm property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getArengedm() {
        return arengedm;
    }

    /**
     * Sets the value of the arengedm property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setArengedm(BigDecimal value) {
        this.arengedm = value;
    }

    /**
     * Gets the value of the vellenertek property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVellenertek() {
        return vellenertek;
    }

    /**
     * Sets the value of the vellenertek property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVellenertek(BigDecimal value) {
        this.vellenertek = value;
    }

    /**
     * Gets the value of the vkozteher property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVkozteher() {
        return vkozteher;
    }

    /**
     * Sets the value of the vkozteher property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVkozteher(BigDecimal value) {
        this.vkozteher = value;
    }

    /**
     * Gets the value of the vktg property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVktg() {
        return vktg;
    }

    /**
     * Sets the value of the vktg property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVktg(BigDecimal value) {
        this.vktg = value;
    }

    /**
     * Gets the value of the vkistag property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getVkistag() {
        return vkistag;
    }

    /**
     * Sets the value of the vkistag property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setVkistag(BigDecimal value) {
        this.vkistag = value;
    }

    /**
     * Gets the value of the kozlEszkInf property.
     * 
     * @return
     *     possible object is
     *     {@link KozlEszkInfTipus }
     *     
     */
    public KozlEszkInfTipus getKozlEszkInf() {
        return kozlEszkInf;
    }

    /**
     * Sets the value of the kozlEszkInf property.
     * 
     * @param value
     *     allowed object is
     *     {@link KozlEszkInfTipus }
     *     
     */
    public void setKozlEszkInf(KozlEszkInfTipus value) {
        this.kozlEszkInf = value;
    }

    /**
     * Gets the value of the asvanyolaj property.
     * 
     * @return
     *     possible object is
     *     {@link AsvanyolajTipus }
     *     
     */
    public AsvanyolajTipus getAsvanyolaj() {
        return asvanyolaj;
    }

    /**
     * Sets the value of the asvanyolaj property.
     * 
     * @param value
     *     allowed object is
     *     {@link AsvanyolajTipus }
     *     
     */
    public void setAsvanyolaj(AsvanyolajTipus value) {
        this.asvanyolaj = value;
    }

}
