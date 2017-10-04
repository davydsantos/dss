package eu.europa.esig.dss.client.http;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import eu.europa.esig.dss.client.http.DataLoader.DataAndUrl;
import eu.europa.esig.dss.utils.Utils;

public class LocalResourceDataLoaderTest {

	private static final String TEST_URL = "http://www.dss-unit-test.net/crl/certificate.crl";
	private static final String TEST_ALTERNATE_URL = "http://server2.dss-unit-test.net/crl/certificate.crl";
	private static final String NON_EXISTENT_RESOURCE = "src/test/resources/non-existent.bin";
	private static final String CRL_RESOURCE = "src/test/resources/crl/belgium2.crl";
	private static final String CRL_RESOURCE_CONTENT_HEX = "3082016d3057300d06092a864886f70d01010505003028310b3009060355040613024245311930170603550403131042656c6769756d20526f6f7420434132170d3133303731313131303030305a170d3134303133313131303030305a300d06092a864886f70d010105050003820101002559d78e12a24217507a4adf992070839dc0526d3bab446b0a337bd1297c8d90007b9990a01e5b5ed1683a9805f6cc419d1067e3f7d0de6bf795cde31e1140407d55ef0c42f71d006a2ea228b00750af2d036792e1d261afc096024953c6bd2773866f38fe2b054f9d963e7d603d2418359cdc616b135192fdcc695378dfb5e19104e26a507ad073df1611098613806703cdb06f9cf6658bf42ac8628ac9cbb9216375e2bee2327d034da56601611ac118aeefdfb6b916927805b81007203f515d5297a635ddf9904419e15fce75539c2539eec94df63decbba2b083b2366106942183aa9f7a16fea055dc5b0fd538e72cc835c6e194a37f73c8e04e6bdc36ce";

	@Test
	public void testGetWithNoResourcesShouldReturnNull() {
		Map<String, String> emptyUrlToResources = Collections.emptyMap();
		LocalResourceDataLoader dataLoader = new LocalResourceDataLoader(emptyUrlToResources);

		byte[] content = dataLoader.get(TEST_URL);

		assertNull(content);
	}

	@Test
	public void testGetWithResourcesShouldReturnContent() {
		Map<String, String> urlToResources = new HashMap<String, String>();
		urlToResources.put(TEST_URL, CRL_RESOURCE);
		LocalResourceDataLoader dataLoader = new LocalResourceDataLoader(urlToResources);

		byte[] content = dataLoader.get(TEST_URL);

		assertNotNull(content);
		byte[] expectedContent = Utils.fromHex(CRL_RESOURCE_CONTENT_HEX);
		assertArrayEquals("content match", expectedContent, content);
	}

	@Test
	public void testGetForUnknownUrlShouldReturnNull() {
		Map<String, String> urlToResources = new HashMap<String, String>();
		urlToResources.put(TEST_URL, CRL_RESOURCE);
		LocalResourceDataLoader dataLoader = new LocalResourceDataLoader(urlToResources);

		byte[] content = dataLoader.get("http://www.dss-unit-test.net/crl/non-mapped.crl");

		assertNull(content);
	}

	@Test
	public void testGetWhenResourceNotExistsShouldReturnNull() {
		Map<String, String> urlToResources = new HashMap<String, String>();
		urlToResources.put(TEST_URL, NON_EXISTENT_RESOURCE);
		LocalResourceDataLoader dataLoader = new LocalResourceDataLoader(urlToResources);

		byte[] content = dataLoader.get(TEST_URL);

		assertNull(content);
	}

	@Test
	public void testGetFromMultipleUrlsShouldReturnContent() {
		Map<String, String> urlToResources = new HashMap<String, String>();
		urlToResources.put(TEST_URL, NON_EXISTENT_RESOURCE);
		urlToResources.put(TEST_ALTERNATE_URL, CRL_RESOURCE);
		LocalResourceDataLoader dataLoader = new LocalResourceDataLoader(urlToResources);
		
		DataAndUrl dataUrl = dataLoader.get(Arrays.asList(TEST_URL, TEST_ALTERNATE_URL));

		assertNotNull(dataUrl);
		assertNotNull(dataUrl.data);
		byte[] expectedContent = Utils.fromHex(CRL_RESOURCE_CONTENT_HEX);
		assertArrayEquals("content match", expectedContent, dataUrl.data);
		assertEquals(TEST_ALTERNATE_URL, dataUrl.urlString);
	}

	@Test
	public void testGetFromMultipleUrlsWithoutContentShouldReturnNull() {
		Map<String, String> urlToResources = new HashMap<String, String>();
		urlToResources.put(TEST_URL, NON_EXISTENT_RESOURCE);
		urlToResources.put(TEST_ALTERNATE_URL, NON_EXISTENT_RESOURCE);
		LocalResourceDataLoader dataLoader = new LocalResourceDataLoader(urlToResources);
		
		DataAndUrl dataUrl = dataLoader.get(Arrays.asList(TEST_URL, TEST_ALTERNATE_URL));

		assertNull(dataUrl);
	}

	@Test
	public void testGetWithRefreshEnabledAndExistentResourcesShouldReturnContent() {
		Map<String, String> urlToResources = new HashMap<String, String>();
		urlToResources.put(TEST_URL, CRL_RESOURCE);
		LocalResourceDataLoader dataLoader = new LocalResourceDataLoader(urlToResources);

		byte[] content = dataLoader.get(TEST_URL, true);

		assertNotNull(content);
		byte[] expectedContent = Utils.fromHex(CRL_RESOURCE_CONTENT_HEX);
		assertArrayEquals("content match", expectedContent, content);
	}

	@Test
	public void testGetWithRefreshDisabledAndExistentResourcesShouldReturnContent() {
		Map<String, String> urlToResources = new HashMap<String, String>();
		urlToResources.put(TEST_URL, CRL_RESOURCE);
		LocalResourceDataLoader dataLoader = new LocalResourceDataLoader(urlToResources);

		byte[] content = dataLoader.get(TEST_URL, false);

		assertNotNull(content);
		byte[] expectedContent = Utils.fromHex(CRL_RESOURCE_CONTENT_HEX);
		assertArrayEquals("content match", expectedContent, content);
	}

	@Test
	public void testPostWithExistentResourcesShouldReturnContent() {
		Map<String, String> urlToResources = new HashMap<String, String>();
		urlToResources.put(TEST_URL, CRL_RESOURCE);
		LocalResourceDataLoader dataLoader = new LocalResourceDataLoader(urlToResources);

		byte[] postData = { 0x30, 0x40, 0x50, 0x10 };
		dataLoader.setContentType("application/anydata");
		byte[] content = dataLoader.post(TEST_URL, postData);

		assertNotNull(content);
		byte[] expectedContent = Utils.fromHex(CRL_RESOURCE_CONTENT_HEX);
		assertArrayEquals("content match", expectedContent, content);
	}
}
