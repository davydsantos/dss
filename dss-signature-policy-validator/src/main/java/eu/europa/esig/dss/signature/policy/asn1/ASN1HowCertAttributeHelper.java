/*******************************************************************************
 * DSS - Digital Signature Services
 * Copyright (C) 2015 European Commission, provided under the CEF programme
 * 
 * This file is part of the "DSS - Digital Signature Services" project.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 ******************************************************************************/
package eu.europa.esig.dss.signature.policy.asn1;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1Enumerated;

import eu.europa.esig.dss.signature.policy.HowCertAttribute;

/**
 * @author davyd.santos
 *
 */
public class ASN1HowCertAttributeHelper {
	
	public static HowCertAttribute getInstance(ASN1Encodable as) {
		ASN1Enumerated enu = ASN1Enumerated.getInstance(as);
		return getInstance(as == null? null: enu.getValue().intValue(), null);
	}
	
	public static HowCertAttribute getInstance(Integer value, HowCertAttribute defaultValue) {
		if (value != null) {
			for(HowCertAttribute v : HowCertAttribute.values()) {
				if (v.ordinal() == value) {
					return v;
				}
			}
		}
		
		if (defaultValue != null) {
			return defaultValue;
		}
		throw new IllegalArgumentException("Invalid value: " + value);
	}
}
