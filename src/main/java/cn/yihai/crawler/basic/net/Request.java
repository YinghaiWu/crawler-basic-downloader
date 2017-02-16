package cn.yihai.crawler.basic.net;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Request implements Serializable {

	private static final long serialVersionUID = -1860885627027893044L;

	public static final String STATUS_CODE = "statusCode";
	public static final String PROXY = "proxy";
	public static final String POST_REQUES_PAIR = "nameValuePair";
	private String url;
	private String method = "GET";
	private Map<String, Object> extras;
	private long priority;
	private boolean allowRedirect = true;

	public Request() {
	}

	public Request(String url) {
		this.url = url;
	}

	public long getPriority() {
		return this.priority;
	}

	public Request setPriority(long priority) {
		this.priority = priority;
		return this;
	}

	public Object getExtra(String key) {
		if (this.extras == null) {
			return null;
		}
		return this.extras.get(key);
	}

	public Request putExtra(String key, Object value) {
		if (this.extras == null) {
			this.extras = new HashMap<String, Object>();
		}
		this.extras.put(key, value);
		return this;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Map<String, Object> getExtras() {
		return this.extras;
	}

	public void setExtras(Map<String, Object> extras) {
		this.extras = extras;
	}

	public String getMethod() {
		return this.method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isAllowRedirect() {
		return this.allowRedirect;
	}

	public void setAllowRedirect(boolean allowRedirect) {
		this.allowRedirect = allowRedirect;
	}

	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if ((o == null) || (getClass() != o.getClass())) {
			return false;
		}
		Request request = (Request) o;
		if (!this.url.equals(request.url)) {
			return false;
		}
		return true;
	}

	public int hashCode() {
		return this.url.hashCode();
	}
}
