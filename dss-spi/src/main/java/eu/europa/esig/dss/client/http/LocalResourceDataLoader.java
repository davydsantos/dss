package eu.europa.esig.dss.client.http;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.europa.esig.dss.client.http.DataLoader;

/**
 * This class uses local resources instead of downloading them. Intended to help on unit-tests coding.
 */
public class LocalResourceDataLoader implements DataLoader {
	private static final long serialVersionUID = 6195089823689409447L;
	
	private static final Logger LOG = LoggerFactory.getLogger(LocalResourceDataLoader.class);

	private Map<String, String> mapUrlToResource;

	public LocalResourceDataLoader(Map<String, String> mapUrlToResource) {
		this.mapUrlToResource = mapUrlToResource;
	}

	private byte[] loadContent(String url) {
		String urlResource = mapUrlToResource.get(url);
		if (urlResource == null) {
			LOG.warn("Url '{}' is not mapped to any local resource", url);
			return null;
		}

		Path resourcePath = Paths.get(new File(urlResource).toURI());
		try {
			byte[] content = Files.readAllBytes(resourcePath);
			LOG.debug("Url '{}' content loaded from local resource '{}'", url, urlResource);
			return content;
		} catch (IOException e) {
			LOG.error("Url '{}' failed to load local resource", url);
		}

		return null;
	}
	
	@Override
	public void setContentType(String contentType) {
		// Ignore
	}

	@Override
	public byte[] post(String url, byte[] content) {
		return get(url);
	}

	@Override
	public byte[] get(String url, boolean refresh) {
		return get(url);
	}

	@Override
	public DataAndUrl get(List<String> urlStrings) {
		for (String url : urlStrings) {
			byte[] content = get(url);
			if (content != null) {
				return new DataAndUrl(content, url);
			}				
		}

		return null;
	}

	@Override
	public byte[] get(String url) {
		return loadContent(url);
	}
}
