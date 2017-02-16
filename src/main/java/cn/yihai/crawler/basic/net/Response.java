package cn.yihai.crawler.basic.net;

import java.util.HashMap;
import java.util.Map;

public class Response {
	private Request request;
	private String rawText;
	private int statusCode;
	private boolean needCycleRetry;
	private String location;
	private Map<String, String> cookies = new HashMap<String, String>();

	public Request getRequest() {
		return this.request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
	
	public boolean isNeedCycleRetry() {
		return this.needCycleRetry;
	}

	public void setNeedCycleRetry(boolean needCycleRetry) {
		this.needCycleRetry = needCycleRetry;
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getRawText() {
		return this.rawText;
	}

	public Response setRawText(String rawText) {
		this.rawText = rawText;
		return this;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Map<String, String> getCookies() {
		return this.cookies;
	}

	public void setCookies(Map<String, String> cookies) {
		this.cookies = cookies;
	}

	public void addCookie(String key, String value) {
		if (this.cookies == null) {
			this.cookies = new HashMap<String, String>();
		}
		this.cookies.put(key, value);
	}

	public String getCookieValue() {
		StringBuilder cookieValue = new StringBuilder();
		int len = this.cookies.size();
		int index = 1;
		for (Map.Entry<String, String> elem : this.cookies.entrySet()) {
			cookieValue.append((String) elem.getKey());
			cookieValue.append("=");
			cookieValue.append((String) elem.getValue());
			if (index < len) {
				cookieValue.append("; ");
			}
			index++;
		}
		return cookieValue.toString().trim();
	}
}
