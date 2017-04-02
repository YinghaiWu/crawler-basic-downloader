package cn.yihai.crawler.basic.net;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;

public abstract class HttpClientGenerator {
	
	protected HttpClientConnectionManager connectionManager;

	public HttpClientGenerator() {
		Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
		this.connectionManager = getConnectionManager(reg);
	}

	public CloseableHttpClient getClient(Site site) {
		return generateClient(site);
	}

	private CloseableHttpClient generateClient(Site site) {
		HttpClientBuilder httpClientBuilder = HttpClients.custom().setConnectionManager(this.connectionManager);

		SocketConfig socketConfig = SocketConfig.custom().setSoKeepAlive(true).setTcpNoDelay(true).build();
		httpClientBuilder.setDefaultSocketConfig(socketConfig);
		if (site != null) {
			httpClientBuilder.setUserAgent(site.getUserAgent() == null ? "" : site.getUserAgent());
			if (site.isUseGzip()) {
				httpClientBuilder.addInterceptorFirst(new HttpRequestInterceptor() {
					public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
						if (!request.containsHeader("Accept-Encoding")) {
							request.addHeader("Accept-Encoding", "gzip");
						}
					}
				});
			}
			httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(site.getRetryTimes(), true));
			generateCookie(httpClientBuilder, site);
		}
		return httpClientBuilder.build();
	}

	private void generateCookie(HttpClientBuilder httpClientBuilder, Site site) {
		CookieStore cookieStore = new BasicCookieStore();
		for (Map.Entry<String, String> cookieEntry : site.getCookies().entrySet()) {
			BasicClientCookie cookie = new BasicClientCookie((String) cookieEntry.getKey(),
					(String) cookieEntry.getValue());
			cookie.setDomain(site.getDomain());
			cookieStore.addCookie(cookie);
		}
		for (Iterator<Map.Entry<String, Map<String, String>>> it = site.getAllCookies().entrySet().iterator(); it
				.hasNext();) {
			Map.Entry<String, Map<String, String>> domainEntry = (Map.Entry<String, Map<String, String>>) it.next();
			for (Map.Entry<String, String> cookieEntry : domainEntry.getValue().entrySet()) {
				BasicClientCookie cookie = new BasicClientCookie((String) cookieEntry.getKey(),
						(String) cookieEntry.getValue());
				cookie.setDomain((String) domainEntry.getKey());
				cookieStore.addCookie(cookie);
			}
		}
		httpClientBuilder.setDefaultCookieStore(cookieStore);
	}

	protected abstract HttpClientConnectionManager getConnectionManager(
			Registry<ConnectionSocketFactory> paramRegistry);

	protected abstract void close();
}
