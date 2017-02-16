package cn.yihai.crawler.basic.net;

import org.apache.http.config.Registry;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

public class PoolingHttpClientGenerator extends HttpClientGenerator {
	private PoolingHttpClientConnectionManager connectionManager;

	protected HttpClientConnectionManager getConnectionManager(Registry<ConnectionSocketFactory> reg) {
		this.connectionManager = new PoolingHttpClientConnectionManager(reg);
		this.connectionManager.setDefaultMaxPerRoute(200);

		return this.connectionManager;
	}

	public HttpClientGenerator setPoolSize(int poolSize) {
		this.connectionManager.setMaxTotal(poolSize);
		return this;
	}

	protected void close() {
		this.connectionManager.shutdown();
	}
}