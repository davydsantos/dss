//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.09.25 at 10:40:03 AM CEST 
//


package eu.europa.esig.dss.jaxb.detailedreport;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ISC complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ISC">
 *   &lt;complexContent>
 *     &lt;extension base="{http://dss.esig.europa.eu/validation/detailed-report}ConstraintsConclusion">
 *       &lt;sequence>
 *         &lt;element name="CertificateChain" type="{http://dss.esig.europa.eu/validation/detailed-report}CertificateChain"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ISC", propOrder = {
    "certificateChain"
})
public class XmlISC
    extends XmlConstraintsConclusion
{

    @XmlElement(name = "CertificateChain", required = true)
    protected XmlCertificateChain certificateChain;

    /**
     * Gets the value of the certificateChain property.
     * 
     * @return
     *     possible object is
     *     {@link XmlCertificateChain }
     *     
     */
    public XmlCertificateChain getCertificateChain() {
        return certificateChain;
    }

    /**
     * Sets the value of the certificateChain property.
     * 
     * @param value
     *     allowed object is
     *     {@link XmlCertificateChain }
     *     
     */
    public void setCertificateChain(XmlCertificateChain value) {
        this.certificateChain = value;
    }

}
