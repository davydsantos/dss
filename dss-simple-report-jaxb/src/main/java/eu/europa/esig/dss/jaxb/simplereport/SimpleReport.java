//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.25 at 07:50:29 AM CEST 
//


package eu.europa.esig.dss.jaxb.simplereport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Policy">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="PolicyName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="PolicyDescription" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *         &lt;element name="ValidationTime" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="DocumentName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ValidSignaturesCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="SignaturesCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="ContainerType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Signature" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="Filename" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *                   &lt;element name="SigningTime" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/>
 *                   &lt;element name="SignedBy" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="CertificateChain" type="{http://dss.esig.europa.eu/validation/simple-report}CertificateChain"/>
 *                   &lt;element name="SignatureLevel" type="{http://dss.esig.europa.eu/validation/simple-report}SignatureLevel" minOccurs="0"/>
 *                   &lt;element name="Indication" type="{http://dss.esig.europa.eu/validation/simple-report}Indication"/>
 *                   &lt;element name="SubIndication" type="{http://dss.esig.europa.eu/validation/simple-report}SubIndication" minOccurs="0"/>
 *                   &lt;element name="Errors" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="Warnings" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="Infos" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *                   &lt;element name="SignatureScope" maxOccurs="unbounded" minOccurs="0">
 *                     &lt;complexType>
 *                       &lt;simpleContent>
 *                         &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *                           &lt;attribute name="name" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                           &lt;attribute name="scope" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                         &lt;/extension>
 *                       &lt;/simpleContent>
 *                     &lt;/complexType>
 *                   &lt;/element>
 *                 &lt;/sequence>
 *                 &lt;attribute name="Id" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="CounterSignature" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *                 &lt;attribute name="ParentId" type="{http://www.w3.org/2001/XMLSchema}string" />
 *                 &lt;attribute name="SignatureFormat" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
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
@XmlType(name = "", propOrder = {
    "policy",
    "validationTime",
    "documentName",
    "validSignaturesCount",
    "signaturesCount",
    "containerType",
    "signature"
})
@XmlRootElement(name = "SimpleReport")
public class SimpleReport {

    @XmlElement(name = "Policy", required = true)
    protected XmlPolicy policy;
    @XmlElement(name = "ValidationTime", required = true, type = String.class)
    @XmlJavaTypeAdapter(Adapter1 .class)
    @XmlSchemaType(name = "dateTime")
    protected Date validationTime;
    @XmlElement(name = "DocumentName", required = true)
    protected String documentName;
    @XmlElement(name = "ValidSignaturesCount")
    protected int validSignaturesCount;
    @XmlElement(name = "SignaturesCount")
    protected int signaturesCount;
    @XmlElement(name = "ContainerType")
    protected String containerType;
    @XmlElement(name = "Signature")
    protected List<XmlSignature> signature;

    /**
     * Gets the value of the policy property.
     * 
     * @return
     *     possible object is
     *     {@link XmlPolicy }
     *     
     */
    public XmlPolicy getPolicy() {
        return policy;
    }

    /**
     * Sets the value of the policy property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlPolicy }
     *     
     */
    public void setPolicy(XmlPolicy value) {
        this.policy = value;
    }

    /**
     * Gets the value of the validationTime property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getValidationTime() {
        return validationTime;
    }

    /**
     * Sets the value of the validationTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidationTime(Date value) {
        this.validationTime = value;
    }

    /**
     * Gets the value of the documentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDocumentName() {
        return documentName;
    }

    /**
     * Sets the value of the documentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDocumentName(String value) {
        this.documentName = value;
    }

    /**
     * Gets the value of the validSignaturesCount property.
     * 
     */
    public int getValidSignaturesCount() {
        return validSignaturesCount;
    }

    /**
     * Sets the value of the validSignaturesCount property.
     * 
     */
    public void setValidSignaturesCount(int value) {
        this.validSignaturesCount = value;
    }

    /**
     * Gets the value of the signaturesCount property.
     * 
     */
    public int getSignaturesCount() {
        return signaturesCount;
    }

    /**
     * Sets the value of the signaturesCount property.
     * 
     */
    public void setSignaturesCount(int value) {
        this.signaturesCount = value;
    }

    /**
     * Gets the value of the containerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getContainerType() {
        return containerType;
    }

    /**
     * Sets the value of the containerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setContainerType(String value) {
        this.containerType = value;
    }

    /**
     * Gets the value of the signature property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signature property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignature().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XmlSignature }
     * 
     * 
     */
    public List<XmlSignature> getSignature() {
        if (signature == null) {
            signature = new ArrayList<XmlSignature>();
        }
        return this.signature;
    }

}
