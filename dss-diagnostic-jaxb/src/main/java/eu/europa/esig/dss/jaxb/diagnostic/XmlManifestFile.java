//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.05.29 at 09:26:39 AM CEST 
//


package eu.europa.esig.dss.jaxb.diagnostic;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ManifestFile complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ManifestFile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Filename" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="SignatureFilename" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Entries" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Entry" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManifestFile", propOrder = {
    "filename",
    "signatureFilename",
    "entries"
})
public class XmlManifestFile {

    @XmlElement(name = "Filename")
    protected String filename;
    @XmlElement(name = "SignatureFilename")
    protected String signatureFilename;
    @XmlElementWrapper(name = "Entries")
    @XmlElement(name = "Entry", namespace = "http://dss.esig.europa.eu/validation/diagnostic")
    protected List<String> entries;

    /**
     * Gets the value of the filename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets the value of the filename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFilename(String value) {
        this.filename = value;
    }

    /**
     * Gets the value of the signatureFilename property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignatureFilename() {
        return signatureFilename;
    }

    /**
     * Sets the value of the signatureFilename property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignatureFilename(String value) {
        this.signatureFilename = value;
    }

    public List<String> getEntries() {
        if (entries == null) {
            entries = new ArrayList<String>();
        }
        return entries;
    }

    public void setEntries(List<String> entries) {
        this.entries = entries;
    }

}
