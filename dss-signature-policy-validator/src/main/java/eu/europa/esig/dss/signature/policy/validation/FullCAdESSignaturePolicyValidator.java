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
package eu.europa.esig.dss.signature.policy.validation;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertStore;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Primitive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.esig.dss.DSSException;
import eu.europa.esig.dss.cades.CMSUtils;
import eu.europa.esig.dss.cades.validation.CAdESSignature;
import eu.europa.esig.dss.signature.policy.AlgorithmConstraintSet;
import eu.europa.esig.dss.signature.policy.CertInfoReq;
import eu.europa.esig.dss.signature.policy.CertRefReq;
import eu.europa.esig.dss.signature.policy.CertRevReq;
import eu.europa.esig.dss.signature.policy.CertificateTrustPoint;
import eu.europa.esig.dss.signature.policy.CertificateTrustTrees;
import eu.europa.esig.dss.signature.policy.CommitmentRule;
import eu.europa.esig.dss.signature.policy.CommitmentType;
import eu.europa.esig.dss.signature.policy.SignaturePolicy;
import eu.europa.esig.dss.signature.policy.SignatureValidationPolicy;
import eu.europa.esig.dss.signature.policy.SignerAndVerifierRules;
import eu.europa.esig.dss.signature.policy.SignerRules;
import eu.europa.esig.dss.signature.policy.SigningCertTrustCondition;
import eu.europa.esig.dss.signature.policy.TimestampTrustCondition;
import eu.europa.esig.dss.signature.policy.VerifierRules;
import eu.europa.esig.dss.signature.policy.asn1.ASN1SignaturePolicy;
import eu.europa.esig.dss.signature.policy.validation.items.AlgorithmConstraintSetValidator;
import eu.europa.esig.dss.signature.policy.validation.items.CAdESCertRefReqValidator;
import eu.europa.esig.dss.signature.policy.validation.items.CAdESSignerRulesExternalDataValidator;
import eu.europa.esig.dss.signature.policy.validation.items.CertInfoReqValidator;
import eu.europa.esig.dss.signature.policy.validation.items.CertificateTrustPointValidator;
import eu.europa.esig.dss.signature.policy.validation.items.CmsSignatureAttributesValidator;
import eu.europa.esig.dss.signature.policy.validation.items.ItemValidator;
import eu.europa.esig.dss.signature.policy.validation.items.RevReqValidator;
import eu.europa.esig.dss.signature.policy.validation.items.SignPolExtensionValidatorFactory;
import eu.europa.esig.dss.validation.BasicASNSignaturePolicyValidator;
import eu.europa.esig.dss.validation.TimestampToken;
import eu.europa.esig.dss.x509.CertificateToken;

/**
 * SignaturePolicy validation consists in matching the commitment rules with the given CMS blob
 * @author davyd.santos
 *
 */
public class FullCAdESSignaturePolicyValidator extends BasicASNSignaturePolicyValidator {

	private static final Logger LOG = LoggerFactory.getLogger(FullCAdESSignaturePolicyValidator.class);

	private Set<CertificateToken> signerCertPath = null;

	private SignaturePolicy asn1SignaturePolicy;
	
	private ResourceBundle bundle;
	
	private static final String BUNDLE = "fullcadesvalidationmessages";
	
	

	public FullCAdESSignaturePolicyValidator() {
		bundle = ResourceBundle.getBundle(BUNDLE);
	}

	public FullCAdESSignaturePolicyValidator(CAdESSignature sig) {
		setSignature(sig);
		bundle = ResourceBundle.getBundle(BUNDLE);
	}
	
	@Override
	public boolean canValidate() {
		return super.canValidate() && isCadesSignature();
	}
	
	@Override
	public void validate() {
		try {
			//  Upper class initializes the signature policy and validates the hash of the policy in 
			// the declared attribute and the value in the policy itself
			super.validate();
			
			if (getSignature().getPolicyId() != null && 
				getSignature().getPolicyId().getPolicyContent() != null) {
				validateSignaturePolicyCommitmentRules();
			}
		} catch(DSSException e) {
			LOG.error("Unexpected error", e);
			addErrors("general", e.getMessage());
		}
		
		
	}

	private SignaturePolicy parse() {
		SignaturePolicy sigPolicy = null;
		try (
			InputStream is = getSignaturePolicy().getPolicyContent().openStream();
			ASN1InputStream asn1is = new ASN1InputStream(is);
		) {
			ASN1Primitive asn1SP = asn1is.readObject();
			if (asn1SP == null) {
				throw new DSSException(getProperty("sigPolicy.noContent"));
			}
			sigPolicy = ASN1SignaturePolicy.getInstance(asn1SP);
		} catch (IOException e) {
			// If the sigPolicy was loaded successfully, don't bubble up the error on stream close
			if (sigPolicy == null) {
				throw new DSSException(getProperty("sigPolicy.unreadable"), e);
			}
		}
		return sigPolicy;
	}

	/**
	 * Validates signature based on a signature policy. It should not be called if
	 * No explicit signature police was declared upon signing.
	 */
	private void validateSignaturePolicyCommitmentRules() {		
		ItemValidator itemValidator = SignPolExtensionValidatorFactory.createValidator(getSignature(), getSignatureValidationPolicy());
		if (!itemValidator.validate()) {
			addErrors("signatureValidationPolicy.signPolExtensions", itemValidator.getErrorDetail());
		}
		
		Set<CommitmentRule> cmmtRules = findCommitmentRule(getSignature().getCommitmentTypeIndication() == null? null: getSignature().getCommitmentTypeIndication().getIdentifiers());
		
		//TODO do I have to validate all or is it enough if one matching is found?
		for (CommitmentRule cmmtRule : cmmtRules) {
			validateSignerAndVeriferRules(cmmtRule.getSignerAndVeriferRules());
			validateSigningCertTrustContition(cmmtRule.getSigningCertTrustCondition());
			validateTimeStampTrustContition(cmmtRule.getTimeStampTrustCondition());
			// TODO TimestampTrustCondition 
			// TODO AttributeTrustCondition
			validateAlgorithmConstraintSet(cmmtRule.getAlgorithmConstraintSet());
			
			itemValidator = SignPolExtensionValidatorFactory.createValidator(getSignature(), cmmtRule);
			if (!itemValidator.validate()) {
				addErrors("commitmentRule.signPolExtensions",  itemValidator.getErrorDetail());
			}
		}
	}

	private void validateSigningCertTrustContition(SigningCertTrustCondition signingCertTrustCondition) {
		RevReqValidator revReqValidator = new RevReqValidator(signingCertTrustCondition.getSignerRevReq().getEndCertRevReq(), getSignature().getSigningCertificateToken());
		if (!revReqValidator.validate()) {
			addErrors("signingCertTrustCondition.signerRevReq.endCertRevReq");
		}
		try {
			signerCertPath = buildTrustedCertificationPath(getSignature().getSigningCertificateToken(), signingCertTrustCondition.getSignerTrustTrees());
			if (signerCertPath.isEmpty()) {
				addErrors("signingCertTrustCondition.signerTrustTrees");
			}

			if (!validateRevReq(signerCertPath, signingCertTrustCondition.getSignerRevReq())) {
				addErrors("signingCertTrustCondition.signerRevReq.endCertRevReq");
			}
		} catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | IOException e) {
			addErrors("signingCertTrustCondition");
			LOG.warn("Error on validating signingCertTrustCondition", e);
		}
	}

	private boolean validateRevReq(Set<CertificateToken> certPath, CertRevReq revReqConstraints) {
		for (CertificateToken certificate : certPath) {
			if (certificate.isSelfSigned() && certificate.isTrusted()) {
				// We don't need to validate trusted root CA
				continue;
			}
			RevReqValidator revReqValidator = new RevReqValidator(revReqConstraints.getCaCerts(), certificate);
			if (!revReqValidator.validate()) {
				LOG.debug(getProperty("evReqConstraints.error", revReqConstraints.getCaCerts(), certificate));
				return false;
			}
		}
		return true;
	}

	private Set<CertificateToken> buildTrustedCertificationPath(CertificateToken certificate, CertificateTrustTrees certificateTrustTrees) throws IOException, InvalidAlgorithmParameterException, NoSuchAlgorithmException {
		if (certificateTrustTrees == null || certificateTrustTrees.getCertificateTrustPoints().isEmpty()) {
			return CertificateTrustPointValidator.buildKnownChain(certificate);
		}
		
		for (CertificateTrustPoint trustPoint : certificateTrustTrees.getCertificateTrustPoints()) {
			CertificateTrustPointValidator trustPointValidator = new CertificateTrustPointValidator(getCadesSignature().getCertPool(), certificate, trustPoint);
			if (trustPointValidator.validate()) {
				return trustPointValidator.getChainCertificates();
			}
		}
		return Collections.emptySet();
	}

	private void validateTimeStampTrustContition(TimestampTrustCondition timeStampTrustCondition) {
		if (timeStampTrustCondition == null) {
			return;
		}
		for(TimestampToken signatureTimestamp : getSignature().getSignatureTimestamps()) {
			if (timeStampTrustCondition.getTtsRevReq() != null && timeStampTrustCondition.getTtsRevReq() != null ) {
				RevReqValidator revReqValidator = new RevReqValidator(timeStampTrustCondition.getTtsRevReq().getEndCertRevReq(), signatureTimestamp.getIssuerToken());
				if (!revReqValidator.validate()) {
					addErrors("timeStampTrustCondition.ttsRevReq.endCertRevReq");
				}
			}
			if (timeStampTrustCondition.getTtsCertificateTrustTrees() != null || timeStampTrustCondition.getTtsRevReq() != null) {
				try {
					Set<CertificateToken> ttsCertPath = buildTrustedCertificationPath(signatureTimestamp.getIssuerToken(), timeStampTrustCondition.getTtsCertificateTrustTrees());
					if (ttsCertPath.isEmpty()) {
						addErrors("timeStampTrustCondition.ttsCertificateTrustTrees");
					}
		
					if (!validateRevReq(ttsCertPath, timeStampTrustCondition.getTtsRevReq())) {
						addErrors("timeStampTrustCondition.ttsRevReq.endCertRevReq");
					}
				} catch (InvalidAlgorithmParameterException | NoSuchAlgorithmException | IOException e) {
					addErrors("timeStampTrustCondition");
					LOG.warn("Error on validating timeStampTrustCondition", e);
				}
			}
			
			// TODO Check NameConstraints
			// TODO Check SignatureTimestampDelay
			// TODO Check CautionPeriod
		}
	}
	
	private void validateSignerAndVeriferRules(SignerAndVerifierRules signerAndVeriferRules) {
		validateSignerRules(signerAndVeriferRules.getSignerRules());
		validateVerifierRules(signerAndVeriferRules.getVerifierRules());
	}

	private void validateSignerRules(SignerRules signerRules) {
		CAdESSignerRulesExternalDataValidator externalDataValidator = new CAdESSignerRulesExternalDataValidator(getCadesSignature(), signerRules.getExternalSignedData());
		if (!externalDataValidator.validate()) {
			addErrors("signerRules.externalSignedData", signerRules.getExternalSignedData());
		}
		
		CmsSignatureAttributesValidator attributesValidator = new CmsSignatureAttributesValidator(signerRules.getMandatedSignedAttr(), CMSUtils.getSignedAttributes(getCadesSignature().getSignerInformation()));
		if (!attributesValidator.validate()) {
			addErrors("signerRules.mandatedSignedAttr", attributesValidator.getMissingAttributes());
		}
		attributesValidator = new CmsSignatureAttributesValidator(signerRules.getMandatedUnsignedAttr(), CMSUtils.getUnsignedAttributes(getCadesSignature().getSignerInformation()));
		if (!attributesValidator.validate()) {
			addErrors("signerRules.mandatedUnsignedAttr", attributesValidator.getMissingAttributes());
		}
		CAdESCertRefReqValidator certRefReqValidator = new CAdESCertRefReqValidator(signerRules.getMandatedCertificateRef(), getCadesSignature(), signerCertPath);
		if (!certRefReqValidator.validate()) {
			if (signerRules.getMandatedCertificateRef() == CertRefReq.signerOnly) {
				if (certRefReqValidator.containsAdditionalCertRef()) {
					addErrors("signerRules.mandatedCertificateRef");
				} else {
					addErrors("signerRules.mandatedCertificateRef");
				}
			} else {
				addErrors("signerRules.mandatedCertificateRef");
			}
		}
		
		if (!new CertInfoReqValidator(signerRules.getMandatedCertificateInfo(), getCadesSignature(), signerCertPath).validate()) {
			if (signerRules.getMandatedCertificateInfo() == CertInfoReq.none) {
				addErrors("signerRules.mandatedCertificateInfo");
			} else if (signerRules.getMandatedCertificateInfo() == CertInfoReq.signerOnly) {
				addErrors("signerRules.mandatedCertificateInfo");
			} else if (signerRules.getMandatedCertificateInfo() == CertInfoReq.fullPath) {
				addErrors("signerRules.mandatedCertificateInfo");
			}
		}
		
		ItemValidator itemValidator = SignPolExtensionValidatorFactory.createValidator(getSignature(), signerRules);
		if (!itemValidator.validate()) {
			addErrors("signerRules.signPolExtensions", itemValidator.getErrorDetail());
		}
	}

	private void validateVerifierRules(VerifierRules verifierRules) {
		CmsSignatureAttributesValidator attributesValidator = new CmsSignatureAttributesValidator(verifierRules.getMandatedUnsignedAttr(), CMSUtils.getUnsignedAttributes(getCadesSignature().getSignerInformation()));
		if (!attributesValidator.validate()) {
			addErrors("verifierRules.mandatedUnsignedAttr", attributesValidator.getMissingAttributes());
		}
		
		ItemValidator itemValidator = SignPolExtensionValidatorFactory.createValidator(getSignature(), verifierRules);
		if (!itemValidator.validate()) {
			addErrors("verifierRules.signPolExtensions", itemValidator.getErrorDetail());
		}
	}

	private Set<CommitmentRule> findCommitmentRule(List<String> identifiers) {
		Set<CommitmentRule> commtRules = new LinkedHashSet<CommitmentRule>();
		SignatureValidationPolicy signatureValidationPolicy = getSignatureValidationPolicy();
		if (identifiers == null || identifiers.isEmpty()) {
			identifiers = Collections.singletonList(null);
		}
		
		for (String oid : identifiers) {
			commtRules.add(findCommitmentRule(signatureValidationPolicy, oid));
		}

		return commtRules;
	}

	private void validateAlgorithmConstraintSet(AlgorithmConstraintSet algorithmConstraintSet) {
		ItemValidator validator = new AlgorithmConstraintSetValidator(algorithmConstraintSet.getSignerAlgorithmConstraints(), getCadesSignature());
		if (!validator.validate()) {
			addErrors("algorithmConstraintSet.signerAlgorithmConstraints");
		}
		
		// TODO eeCertAlgorithmConstraints
		// TODO caCertAlgorithmConstraints
		// TODO tsaCertAlgorithmConstraints
		// TODO aaCertAlgorithmConstraints
	}

	private CommitmentRule findCommitmentRule(SignatureValidationPolicy signatureValidationPolicy, String oid) {
		for (CommitmentRule cmmtRule : signatureValidationPolicy.getCommitmentRules()) {
			if (oid == null) {
				if (cmmtRule.getSelCommitmentTypes().contains(null)) {
					return new CommitmentRuleWrapper(cmmtRule, signatureValidationPolicy.getCommonRules());
				}
			} else {
				for (CommitmentType cmmtType : cmmtRule.getSelCommitmentTypes()) {
					if (oid.equals(cmmtType.getIdentifier())) {
						return new CommitmentRuleWrapper(cmmtRule, signatureValidationPolicy.getCommonRules()); 
					}
				}
			}
		}
		
		// RFC 3125
		// "... the electronic signature must contain a commitment type indication
		// that must fit one of the commitments types that are mentioned in
		// CommitmentType."
		throw new DSSException(getProperty("CommitmentRule.notFound"));
	}

	protected SignatureValidationPolicy getSignatureValidationPolicy() {
		if (asn1SignaturePolicy == null) {
			asn1SignaturePolicy = parse();
		}
		SignatureValidationPolicy signatureValidationPolicy = asn1SignaturePolicy.getSignPolicyInfo().getSignatureValidationPolicy();
		return signatureValidationPolicy;
	}

	private CAdESSignature getCadesSignature() {
		return (CAdESSignature) getSignature();
	}

	private boolean isCadesSignature() {
		return getSignature() instanceof CAdESSignature;
	}
	
	protected void addErrors(String key, Object... extraParams) {
		super.addError(key, getProperty(key, extraParams));
	}

	private String getProperty(String key, Object... extraParams) {
		return MessageFormat.format(bundle.getString(key), extraParams);
	}
}
