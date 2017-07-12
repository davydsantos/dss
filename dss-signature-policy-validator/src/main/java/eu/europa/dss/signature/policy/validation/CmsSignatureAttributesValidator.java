package eu.europa.dss.signature.policy.validation;

import java.util.List;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cms.AttributeTable;

public class CmsSignatureAttributesValidator extends AbstractSignatureAttributesValidator {
	private AttributeTable attributeTable;
	
	public CmsSignatureAttributesValidator(List<String> mandatedAttributes, AttributeTable attributes) {
		super(mandatedAttributes);
		attributeTable = attributes;
	}
	
	protected boolean containsAttribute(String oid) {
		return attributeTable.get(new ASN1ObjectIdentifier(oid)) == null;
	}
}
