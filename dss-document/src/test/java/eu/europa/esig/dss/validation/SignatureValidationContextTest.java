package eu.europa.esig.dss.validation;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.client.http.LocalResourceDataLoader;
import eu.europa.esig.dss.test.mock.MockCRLSource;
import eu.europa.esig.dss.test.mock.MockEmptyTSLCertificateSource;
import eu.europa.esig.dss.x509.CertificatePool;
import eu.europa.esig.dss.x509.CertificateToken;
import eu.europa.esig.dss.x509.RevocationToken;
import eu.europa.esig.dss.x509.crl.ListCRLSource;
import eu.europa.esig.dss.x509.ocsp.ListOCSPSource;

public class SignatureValidationContextTest {
	
	@Test
	public void testValidateWhenCertificateIssuerIsPresentOnP7CFile() throws Exception {
		// Setup
		final CertificateVerifier certificateVerifier = new CommonCertificateVerifier();		
		MockCRLSource crlSource = new MockCRLSource(
				new FileInputStream("src/test/resources/icp-brasil-serasarfbv5.crl"),
				new FileInputStream("src/test/resources/icp-brasil-acrfbv4.crl"),
				new FileInputStream("src/test/resources/icp-brasil-LCRacraizv5.crl"));
		Map<String, String> mapUrlToResource = new HashMap<String, String>();
		mapUrlToResource.put("http://www.certificadodigital.com.br/cadeias/serasarfbv5.p7b", "src/test/resources/icp-brasil-serasarfbv5.p7b");		

		certificateVerifier.setTrustedCertSource(new MockEmptyTSLCertificateSource());
		certificateVerifier.setDataLoader(new LocalResourceDataLoader(mapUrlToResource));
		certificateVerifier.setCrlSource(crlSource);
		certificateVerifier.setOcspSource(new ListOCSPSource());
		certificateVerifier.setSignatureCRLSource(new ListCRLSource(crlSource));

		final CertificatePool validationCertificatePool = certificateVerifier.createValidationPool();		
		final SignatureValidationContext validationContext = new SignatureValidationContext(validationCertificatePool);
		validationContext.initialize(certificateVerifier);

		final CertificateToken endUserCertToken = DSSUtils.loadCertificate(new File("src/test/resources/icp-brasil.crt"));		
		validationContext.addCertificateTokenForVerification(endUserCertToken);

		// Act
		validationContext.validate();
		
		// Verify certificate token signature
		assertTrue("end user certificate token has valid signature", endUserCertToken.isSignatureValid());

		// Verify certificate token chain
		String[] expectedIssuerPrincipal = { 
				"CN=AC SERASA RFB v5,OU=Secretaria da Receita Federal do Brasil - RFB,O=ICP-Brasil,C=BR",
				"CN=AC Secretaria da Receita Federal do Brasil v4,OU=Autoridade Certificadora Raiz Brasileira v5,O=ICP-Brasil,C=BR",
				"CN=Autoridade Certificadora Raiz Brasileira v5,OU=Instituto Nacional de Tecnologia da Informacao - ITI,O=ICP-Brasil,C=BR"
		};		
		CertificateToken curToken = endUserCertToken;
		for (String expectedPrincipal : expectedIssuerPrincipal) {
			assertNotNull(String.format("issuer expected '%s'", expectedPrincipal), curToken.getIssuerToken());
			assertEquals("issuer principal should match", expectedPrincipal, curToken.getIssuerToken().getSubjectX500Principal().getName());
			curToken = curToken.getIssuerToken();
		}

		// Verify processed certificates
		Map<String, Boolean> expectedProcessedCertificatePrincipals = new HashMap<String, Boolean>();
		expectedProcessedCertificatePrincipals.put(endUserCertToken.getSubjectX500Principal().getName(), Boolean.FALSE);
		for (String expectedPrincipal : expectedIssuerPrincipal) {
			expectedProcessedCertificatePrincipals.put(expectedPrincipal, Boolean.FALSE);
		}

		Set<CertificateToken> processedCertificates = validationContext.getProcessedCertificates();
		for (CertificateToken processedToken : processedCertificates) {
			String subjectPrincipal = processedToken.getSubjectX500Principal().getName();
			assertTrue("processed certificate should be expected", expectedProcessedCertificatePrincipals.containsKey(subjectPrincipal));
			expectedProcessedCertificatePrincipals.put(subjectPrincipal, Boolean.TRUE);
		}

		for (Entry<String, Boolean> entry : expectedProcessedCertificatePrincipals.entrySet()) {
			assertTrue(String.format("principal '%s' should be present on processed certificates", entry.getKey()), entry.getValue());
		}

		// Verify processed revocations
		Map<String, Boolean> expectedProcessedRevocationIssuers = new HashMap<String, Boolean>();
		for (String expectedPrincipal : expectedIssuerPrincipal) {
			expectedProcessedRevocationIssuers.put(expectedPrincipal, Boolean.FALSE);
		}

		Set<RevocationToken> processedRevocations = validationContext.getProcessedRevocations();
		for (RevocationToken processedRevocation : processedRevocations) {
			assertTrue(processedRevocation.getStatus());
			String principalName = processedRevocation.getIssuerX500Principal().getName();
			assertTrue(String.format("processed revocation '%s' should be expected", principalName), 
					expectedProcessedRevocationIssuers.containsKey(principalName));
			expectedProcessedRevocationIssuers.put(principalName, Boolean.TRUE);
		}

		for (Entry<String, Boolean> entry : expectedProcessedRevocationIssuers.entrySet()) {
			assertTrue(String.format("principal '%s' should be present on processed revocations", entry.getKey()), entry.getValue());
		}
	}
}
