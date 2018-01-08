//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.10.27 at 09:43:12 AM CEST 
//


package eu.europa.esig.dss.jaxb.diagnostic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for Timestamp complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Timestamp"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ProductionTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/&gt;
 *         &lt;element name="SignedDataDigestAlgo" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="EncodedSignedDataDigestValue" type="{http://www.w3.org/2001/XMLSchema}string"/&gt;
 *         &lt;element name="MessageImprintDataFound" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="MessageImprintDataIntact" type="{http://www.w3.org/2001/XMLSchema}boolean"/&gt;
 *         &lt;element name="CanonicalizationMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="BasicSignature" type="{http://dss.esig.europa.eu/validation/diagnostic}BasicSignature"/&gt;
 *         &lt;element name="SigningCertificate" type="{http://dss.esig.europa.eu/validation/diagnostic}SigningCertificate" minOccurs="0"/&gt;
 *         &lt;element name="CertificateChain" type="{http://dss.esig.europa.eu/validation/diagnostic}CertificateChain" minOccurs="0"/&gt;
 *         &lt;element name="TimestampedObjects" type="{http://dss.esig.europa.eu/validation/diagnostic}TimestampedObjects" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="Id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *       &lt;attribute name="Type" use="required" type="{http://www.w3.org/2001/XMLSchema}string" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Timestamp", propOrder = {
    "productionTime",
    "signedDataDigestAlgo",
    "encodedSignedDataDigestValue",
    "messageImprintDataFound",
    "messageImprintDataIntact",
    "canonicalizationMethod",
    "basicSignature",
    "signingCertificate",
    "certificateChain",
    "timestampedObjects"
})
public class XmlTimestamp {

    @XmlElement(name = "ProductionTime", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date productionTime;
    @XmlElement(name = "SignedDataDigestAlgo", required = true)
    protected String signedDataDigestAlgo;
    @XmlElement(name = "EncodedSignedDataDigestValue", required = true)
    protected String encodedSignedDataDigestValue;
    @XmlElement(name = "MessageImprintDataFound")
    protected boolean messageImprintDataFound;
    @XmlElement(name = "MessageImprintDataIntact")
    protected boolean messageImprintDataIntact;
    @XmlElement(name = "CanonicalizationMethod")
    protected String canonicalizationMethod;
    @XmlElement(name = "BasicSignature", required = true)
    protected XmlBasicSignature basicSignature;
    @XmlElement(name = "SigningCertificate")
    protected XmlSigningCertificate signingCertificate;
    @XmlElementWrapper(name = "CertificateChain")
    @XmlElement(name = "ChainItem", namespace = "http://dss.esig.europa.eu/validation/diagnostic")
    protected List<XmlChainItem> certificateChain;
    @XmlElementWrapper(name = "TimestampedObjects")
    @XmlElement(name = "TimestampedObject", namespace = "http://dss.esig.europa.eu/validation/diagnostic")
    protected List<XmlTimestampedObject> timestampedObjects;
    @XmlAttribute(name = "Id", required = true)
    protected String id;
    @XmlAttribute(name = "Type", required = true)
    protected String type;

    /**
     * Gets the value of the productionTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getProductionTime() {
        return productionTime;
    }

    /**
     * Sets the value of the productionTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProductionTime(Date value) {
        this.productionTime = value;
    }

    /**
     * Gets the value of the signedDataDigestAlgo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSignedDataDigestAlgo() {
        return signedDataDigestAlgo;
    }

    /**
     * Sets the value of the signedDataDigestAlgo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSignedDataDigestAlgo(String value) {
        this.signedDataDigestAlgo = value;
    }

    /**
     * Gets the value of the encodedSignedDataDigestValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEncodedSignedDataDigestValue() {
        return encodedSignedDataDigestValue;
    }

    /**
     * Sets the value of the encodedSignedDataDigestValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEncodedSignedDataDigestValue(String value) {
        this.encodedSignedDataDigestValue = value;
    }

    /**
     * Gets the value of the messageImprintDataFound property.
     * 
     */
    public boolean isMessageImprintDataFound() {
        return messageImprintDataFound;
    }

    /**
     * Sets the value of the messageImprintDataFound property.
     * 
     */
    public void setMessageImprintDataFound(boolean value) {
        this.messageImprintDataFound = value;
    }

    /**
     * Gets the value of the messageImprintDataIntact property.
     * 
     */
    public boolean isMessageImprintDataIntact() {
        return messageImprintDataIntact;
    }

    /**
     * Sets the value of the messageImprintDataIntact property.
     * 
     */
    public void setMessageImprintDataIntact(boolean value) {
        this.messageImprintDataIntact = value;
    }

    /**
     * Gets the value of the canonicalizationMethod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCanonicalizationMethod() {
        return canonicalizationMethod;
    }

    /**
     * Sets the value of the canonicalizationMethod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCanonicalizationMethod(String value) {
        this.canonicalizationMethod = value;
    }

    /**
     * Gets the value of the basicSignature property.
     * 
     * @return
     *     possible object is
     *     {@link XmlBasicSignature }
     *     
     */
    public XmlBasicSignature getBasicSignature() {
        return basicSignature;
    }

    /**
     * Sets the value of the basicSignature property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlBasicSignature }
     *     
     */
    public void setBasicSignature(XmlBasicSignature value) {
        this.basicSignature = value;
    }

    /**
     * Gets the value of the signingCertificate property.
     * 
     * @return
     *     possible object is
     *     {@link XmlSigningCertificate }
     *     
     */
    public XmlSigningCertificate getSigningCertificate() {
        return signingCertificate;
    }

    /**
     * Sets the value of the signingCertificate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlSigningCertificate }
     *     
     */
    public void setSigningCertificate(XmlSigningCertificate value) {
        this.signingCertificate = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setId(String value) {
        this.id = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setType(String value) {
        this.type = value;
    }

    public List<XmlChainItem> getCertificateChain() {
        if (certificateChain == null) {
            certificateChain = new ArrayList<XmlChainItem>();
        }
        return certificateChain;
    }

    public void setCertificateChain(List<XmlChainItem> certificateChain) {
        this.certificateChain = certificateChain;
    }

    public List<XmlTimestampedObject> getTimestampedObjects() {
        if (timestampedObjects == null) {
            timestampedObjects = new ArrayList<XmlTimestampedObject>();
        }
        return timestampedObjects;
    }

    public void setTimestampedObjects(List<XmlTimestampedObject> timestampedObjects) {
        this.timestampedObjects = timestampedObjects;
    }

}
