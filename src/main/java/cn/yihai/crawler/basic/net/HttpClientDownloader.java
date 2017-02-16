package cn.yihai.crawler.basic.net;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientDownloader {
	private static Logger logger = LoggerFactory.getLogger(HttpClientDownloader.class);
	private HttpClientGenerator httpClientGenerator;
	private boolean autoReleaseConnect = false;

	public HttpClientDownloader(boolean autoReleaseConnect) {
		this.autoReleaseConnect = autoReleaseConnect;
		this.httpClientGenerator = new BasicHttpClientGenerator();
	}

	public HttpClientDownloader(boolean autoReleaseConnect, int poolSize) {
		this.autoReleaseConnect = autoReleaseConnect;
		PoolingHttpClientGenerator pcg = new PoolingHttpClientGenerator();
		pcg.setPoolSize(poolSize);
		this.httpClientGenerator = pcg;
	}

	public Response download(String url) throws Exception {
		return download(url, null);
	}

	public Response download(String url, Site site) throws Exception {
		Request request = new Request();
		request.setUrl(url);

		return download(request, site);
	}

	public Response download(Request request) throws Exception {
		return download(request, null);
	}

	public Response download(Request request, Site site) throws Exception {
		String charset = null;
		Map<String, String> headers = null;
		Set<Integer> acceptStatCode;
		if (site != null) {
			acceptStatCode = site.getAcceptStatCode();
			charset = site.getCharset();
			headers = site.getHeaders();
		} else {
			site = Site.me();
			acceptStatCode = site.getAcceptStatCode();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("downloading page {}", request.getUrl());
		}
		CloseableHttpResponse httpResponse = null;
		int statusCode = 0;
		try {
			HttpUriRequest httpUriRequest = getHttpUriRequest(request, site, headers);
			httpResponse = this.httpClientGenerator.getClient(site).execute(httpUriRequest);
			statusCode = httpResponse.getStatusLine().getStatusCode();
			request.putExtra("statusCode", Integer.valueOf(statusCode));
//			Response localResponse;
			if (acceptStatCode.isEmpty()) {
				return handleResponse(request, charset, httpResponse);
			}
			if (statusAccept(acceptStatCode, statusCode)) {
				return handleResponse(request, charset, httpResponse);
			}
			return handleResponse(request, charset, httpResponse);
		} finally {
			try {
				if (httpResponse != null) {
					EntityUtils.consume(httpResponse.getEntity());
				}
				if (this.autoReleaseConnect) {
					close();
				}
				if (site.getSleepTime() != 0) {
					Thread.sleep(site.getSleepTime());
				}
			} catch (Exception e) {
				logger.warn(e.getMessage(), e);
			}
		}
	}

	public void releaseAfterConnect(boolean autoReleaseConnect) {
		this.autoReleaseConnect = autoReleaseConnect;
	}

	public void close() {
		this.httpClientGenerator.close();
	}

	protected boolean statusAccept(Set<Integer> acceptStatCode, int statusCode) {
		return acceptStatCode.contains(Integer.valueOf(statusCode));
	}

	protected HttpUriRequest getHttpUriRequest(Request request, Site site, Map<String, String> headers) {
		RequestBuilder requestBuilder = selectRequestMethod(request).setUri(request.getUrl());
		if (headers != null) {
			for (Map.Entry<String, String> headerEntry : headers.entrySet()) {
				requestBuilder.addHeader((String) headerEntry.getKey(), (String) headerEntry.getValue());
			}
		}
		RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
		requestConfigBuilder.setConnectionRequestTimeout(site.getTimeOut());
		requestConfigBuilder.setSocketTimeout(site.getTimeOut());
		requestConfigBuilder.setConnectTimeout(site.getTimeOut());
		requestConfigBuilder.setCookieSpec("default");
		if (!request.isAllowRedirect()) {
			requestConfigBuilder.setRedirectsEnabled(false);
		}
		if (site.getHttpProxy() != null) {
			HttpHost host = site.getHttpProxy();
			requestConfigBuilder.setProxy(host);
			request.putExtra("proxy", host);
		}
		requestBuilder.setConfig(requestConfigBuilder.build());
		return requestBuilder.build();
	}

	protected RequestBuilder selectRequestMethod(Request request) {
		String method = request.getMethod();
		if ((method == null) || (method.equalsIgnoreCase("GET"))) {
			return RequestBuilder.get();
		}
		if (method.equalsIgnoreCase("POST")) {
			RequestBuilder requestBuilder = RequestBuilder.post();
			NameValuePair[] nameValuePair = (NameValuePair[]) request.getExtra("nameValuePair");
			if ((nameValuePair != null) && (nameValuePair.length > 0)) {
				requestBuilder.addParameters(nameValuePair);
			}
			return requestBuilder;
		}
		if (method.equalsIgnoreCase("HEAD")) {
			return RequestBuilder.head();
		}
		if (method.equalsIgnoreCase("PUT")) {
			return RequestBuilder.put();
		}
		if (method.equalsIgnoreCase("DELETE")) {
			return RequestBuilder.delete();
		}
		if (method.equalsIgnoreCase("TRACE")) {
			return RequestBuilder.trace();
		}
		throw new IllegalArgumentException("Illegal HTTP Method " + method);
	}

	protected Response handleResponse(Request request, String charset, HttpResponse httpResponse) throws IOException {
		Response response = new Response();
		response.setRequest(request);
		if (httpResponse == null) {
			return response;
		}
		response.setRawText(getContent(charset, httpResponse));
		response.setStatusCode(httpResponse.getStatusLine().getStatusCode());

		Header header = httpResponse.getFirstHeader("Location");
		if (header != null) {
			response.setLocation(header.getValue());
		}
		getCookie(response, httpResponse);

		return response;
	}

	private void getCookie(Response response, HttpResponse httpResponse) {
		HeaderIterator hit = httpResponse.headerIterator("Set-Cookie");
		while (hit.hasNext()) {
			Header header = hit.nextHeader();
			HeaderElement[] headerElement = header.getElements();
			if (headerElement == null) {
				return;
			}
			for (HeaderElement he : headerElement) {
				if ((he.getName() != null) && (he.getValue() != null)) {
					response.addCookie(he.getName(), he.getValue());
				}
			}
		}
	}

	protected String getContent(String charset, HttpResponse httpResponse) throws IOException {
		if (charset == null) {
			return EntityUtils.toString(httpResponse.getEntity());
		}
		return EntityUtils.toString(httpResponse.getEntity(), charset);
	}
}
