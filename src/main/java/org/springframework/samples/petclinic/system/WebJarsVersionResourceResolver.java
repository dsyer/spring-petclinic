package org.springframework.samples.petclinic.system;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.resource.AbstractResourceResolver;
import org.springframework.web.servlet.resource.ResourceResolverChain;

public class WebJarsVersionResourceResolver  extends AbstractResourceResolver {

	private static final String PROPERTIES_ROOT = "META-INF/maven/";
	private static final String NPM = "org.webjars.npm/";
	private static final String PLAIN = "org.webjars/";
	private static final String POM_PROPERTIES = "/pom.properties";

	@Override
	protected Resource resolveResourceInternal(@Nullable HttpServletRequest request, String requestPath,
			List<? extends Resource> locations, ResourceResolverChain chain) {

		Resource resolved = chain.resolveResource(request, requestPath, locations);
		if (resolved == null) {
			String webJarResourcePath = findWebJarResourcePath(requestPath);
			if (webJarResourcePath != null) {
				return chain.resolveResource(request, webJarResourcePath, locations);
			}
		}
		return resolved;
	}

	@Override
	protected String resolveUrlPathInternal(String resourceUrlPath,
			List<? extends Resource> locations, ResourceResolverChain chain) {

		String path = chain.resolveUrlPath(resourceUrlPath, locations);
		if (path == null) {
			String webJarResourcePath = findWebJarResourcePath(resourceUrlPath);
			if (webJarResourcePath != null) {
				return chain.resolveUrlPath(webJarResourcePath, locations);
			}
		}
		return path;
	}

	@Nullable
	protected String findWebJarResourcePath(String path) {
		String webjar = webjar(path);
		if (webjar.length() > 0) {
			String version = version(webjar);
			if (version != null) {
				String partialPath = path(webjar, version, path);
				if (partialPath != null) {
					String webJarPath = webjar + File.separator + version + File.separator + partialPath;
					return webJarPath;
				}
			}
		}
		return null;
	}

	private String webjar(String path) {
		int startOffset = (path.startsWith("/") ? 1 : 0);
		int endOffset = path.indexOf('/', 1);
		String webjar = endOffset != -1 ? path.substring(startOffset, endOffset) : path;
		return webjar;
	}


	private String version(String webjar) {
		Resource resource = new ClassPathResource(PROPERTIES_ROOT + NPM + webjar + POM_PROPERTIES);
		if (!resource.isReadable()) {
			resource = new ClassPathResource(PROPERTIES_ROOT + PLAIN + webjar + POM_PROPERTIES);
		}
		if (resource.isReadable()) {
			Properties properties;
			try {
				properties = PropertiesLoaderUtils.loadProperties(resource);
				return properties.getProperty("version");
			} catch (IOException e) {
			}
		}
		return null;
	}

	private String path(String webjar, String version, String path) {
		if (path.startsWith(webjar)) {
			path = path.substring(webjar.length()+1);
		}
		return path;
	}
}
