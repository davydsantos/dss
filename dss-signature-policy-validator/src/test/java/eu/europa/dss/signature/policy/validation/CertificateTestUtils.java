package eu.europa.dss.signature.policy.validation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.List;

import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.util.encoders.Hex;

import eu.europa.esig.dss.DSSUtils;
import eu.europa.esig.dss.client.http.DataLoader;
import eu.europa.esig.dss.client.http.NativeHTTPDataLoader;
import eu.europa.esig.dss.x509.CertificatePool;
import eu.europa.esig.dss.x509.CertificateSourceType;
import eu.europa.esig.dss.x509.CertificateToken;

public class CertificateTestUtils {
	public static X509Certificate load(File certFile) throws IOException, CertificateException {
		byte[] certContents = Files.readAllBytes(Paths.get(certFile.toURI()));
		CertificateFactory certificateFactory = CertificateFactory.getInstance("X509");
		return (X509Certificate) certificateFactory.generateCertificate(new ByteArrayInputStream(certContents));
	}
	
	public static CertificateToken loadIssuers(File certFile, CertificatePool certPool) throws CertificateException, IOException {
		X509Certificate certificate = load(certFile);
		return loadIssuers(new CertificateToken(certificate), certPool);
	}
	
	public static CertificateToken loadIssuers(CertificateToken certificate, CertificatePool certPool) {
		DataLoader loader = new NativeHTTPDataLoader() {
			@Override
			public byte[] get(String url) {
				if (url.startsWith("ldap")) {
					return null;
				}
				return super.get(url);
			}
		};
		CertificateToken cert = certPool.getInstance(certificate, CertificateSourceType.SIGNATURE);
		return loadIssuers(loader, cert, certPool);
	}
	
	public static CertificateToken loadIssuers(DataLoader loader, CertificateToken certificate, CertificatePool certPool) {
		if (certificate.getIssuerToken() == null) {
			Collection<CertificateToken> issuerCertificates = DSSUtils.loadIssuerCertificates(certificate, loader);
			if (issuerCertificates == null) {
				return certificate;
			}
			
			for (CertificateToken certificateToken : issuerCertificates) {
				certPool.getInstance(certificateToken, CertificateSourceType.AIA);
			}
	
			for (CertificateToken issuerCertificateToken : issuerCertificates) {
				if (issuerCertificateToken.isSelfSigned()) {
					continue;
				}
				
				if (isIssuer(certificate, issuerCertificateToken)) {
					// Checks if the issuer's issuer cert is known
					boolean foundCAIssuer = false;
					List<CertificateToken> list = certPool.get(issuerCertificateToken.getIssuerX500Principal());
					for (CertificateToken possibleIssuer : list) {
						if (issuerCertificateToken.isSignedBy(possibleIssuer)) {
							foundCAIssuer = true;
						}
					}
					if (!foundCAIssuer) {
						loadIssuers(loader, issuerCertificateToken, certPool);
					}
				}
			}
		}
		return certificate;
	}

	private static boolean isIssuer(CertificateToken certificateToken, CertificateToken issuerCertificateToken){
		if (!certificateToken.isSignedBy(issuerCertificateToken)) {
			return false;
		}
		
		String ski = GetSubjectKeyIdentifier(issuerCertificateToken);
		String aki = GetAuthorityKeyIdentifier(certificateToken);
		if (ski != null && aki != null) {
			return ski.equals(aki);
		}
		
		return certificateToken.getIssuerX500Principal().equals(issuerCertificateToken.getSubjectX500Principal());
	}
	
	private static String GetSubjectKeyIdentifier(CertificateToken token) {
		byte[] skiValue = token.getCertificate().getExtensionValue(Extension.subjectKeyIdentifier.getId());
		if (skiValue != null) {
			byte[] ski = ASN1OctetString.getInstance(skiValue).getOctets();
			byte[] id = SubjectKeyIdentifier.getInstance(ski).getKeyIdentifier();
			return Hex.toHexString(id);
		}
		return null;
	}
	
	private static String GetAuthorityKeyIdentifier(CertificateToken token) {
		byte[] akiValue = token.getCertificate().getExtensionValue(Extension.authorityKeyIdentifier.getId());
		if (akiValue != null) {
			AuthorityKeyIdentifier authorityKeyIdentifier;
			byte[] aki = ASN1OctetString.getInstance(akiValue).getOctets();
			authorityKeyIdentifier = AuthorityKeyIdentifier.getInstance(aki);
			return Hex.toHexString(authorityKeyIdentifier.getKeyIdentifier());
		}
		return null;
	}
}
