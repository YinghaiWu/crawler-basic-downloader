package cn.yihai.crawler.basic.net;

import org.apache.http.config.Registry;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

public class BasicHttpClientGenerator extends HttpClientGenerator {
	private BasicHttpClientConnectionManager connectionManager;

	protected HttpClientConnectionManager getConnectionManager(Registry<ConnectionSocketFactory> reg) {
		this.connectionManager = new BasicHttpClientConnectionManager(reg);
		return this.connectionManager;
	}

	protected void close() {
		this.connectionManager.shutdown();
	}
}
