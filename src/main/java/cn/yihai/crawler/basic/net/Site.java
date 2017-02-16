package cn.yihai.crawler.basic.net;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpHost;

public class Site {
	private String domain;
	private String userAgent;
	private Map<String, String> defaultCookies = new LinkedHashMap<String, String>();
	private Map<String, Map<String, String>> cookies = new HashMap<String, Map<String, String>>();
	private String charset;
	private List<Request> startRequests = new ArrayList<Request>();
	private int sleepTime = 500;
	private int timeOut = 5000;
	private int retryTimes = 0;
	private static final Set<Integer> DEFAULT_STATUS_CODE_SET = new HashSet<Integer>();
	private Set<Integer> acceptStatCode = DEFAULT_STATUS_CODE_SET;
	private Map<String, String> headers = new HashMap<String, String>();
	private HttpHost httpProxy;
	private boolean useGzip = true;

	public static Site me() {
		return new Site();
	}

	public Site addCookie(String name, String value) {
		this.defaultCookies.put(name, value);
		return this;
	}

	public void setDefaultCookies(Map<String, String> defaultCookies) {
		if (defaultCookies == null) {
			return;
		}
		this.defaultCookies = defaultCookies;
	}

	public Site addCookie(String domain, String name, String value) {
		Map<String, String> cookie = new HashMap<String, String>();
		cookie.put(name, value);
		this.cookies.put(domain, cookie);
		return this;
	}

	public void setCookies(Map<String, Map<String, String>> cookies) {
		if (cookies == null) {
			return;
		}
		this.cookies = cookies;
	}

	public Site setUserAgent(String userAgent) {
		this.userAgent = userAgent;
		return this;
	}

	public Map<String, String> getCookies() {
		return this.defaultCookies;
	}

	public Map<String, Map<String, String>> getAllCookies() {
		return this.cookies;
	}

	public String getUserAgent() {
		return this.userAgent;
	}

	public String getDomain() {
		return this.domain;
	}

	public Site setDomain(String domain) {
		this.domain = domain;
		return this;
	}

	public Site setCharset(String charset) {
		this.charset = charset;
		return this;
	}

	public String getCharset() {
		return this.charset;
	}

	public int getTimeOut() {
		return this.timeOut;
	}

	public Site setTimeOut(int timeOut) {
		this.timeOut = timeOut;
		return this;
	}

	public int getRetryTimes() {
		return this.retryTimes;
	}

	public Site setRetryTimes(int retryTimes) {
		this.retryTimes = retryTimes;
		return this;
	}

	public Site setAcceptStatCode(Set<Integer> acceptStatCode) {
		this.acceptStatCode = acceptStatCode;
		return this;
	}

	public Set<Integer> getAcceptStatCode() {
		return this.acceptStatCode;
	}

	public List<Request> getStartRequests() {
		return this.startRequests;
	}

	public Site addStartUrl(String startUrl) {
		this.startRequests.add(new Request(startUrl));
		return this;
	}

	public Site setSleepTime(int sleepTime) {
		this.sleepTime = sleepTime;
		return this;
	}

	public int getSleepTime() {
		return this.sleepTime;
	}

	public Map<String, String> getHeaders() {
		return this.headers;
	}

	public Site addHeader(String key, String value) {
		this.headers.put(key, value);
		return this;
	}

	public HttpHost getHttpProxy() {
		return this.httpProxy;
	}

	public Site setHttpProxy(HttpHost httpProxy) {
		this.httpProxy = httpProxy;
		return this;
	}

	public boolean isUseGzip() {
		return this.useGzip;
	}

	public Site setUseGzip(boolean useGzip) {
		this.useGzip = useGzip;
		return this;
	}

	public int hashCode() {
		int prime = 31;
		int result = 1;
		result = prime * result + (this.acceptStatCode == null ? 0 : this.acceptStatCode.hashCode());
		result = prime * result + (this.charset == null ? 0 : this.charset.hashCode());
		result = prime * result + (this.cookies == null ? 0 : this.cookies.hashCode());
		result = prime * result + (this.defaultCookies == null ? 0 : this.defaultCookies.hashCode());
		result = prime * result + (this.domain == null ? 0 : this.domain.hashCode());
		result = prime * result + (this.headers == null ? 0 : this.headers.hashCode());
		result = prime * result + (this.httpProxy == null ? 0 : this.httpProxy.hashCode());
		result = prime * result + this.retryTimes;
		result = prime * result + this.sleepTime;
		result = prime * result + (this.startRequests == null ? 0 : this.startRequests.hashCode());
		result = prime * result + this.timeOut;
		result = prime * result + (this.useGzip ? 1231 : 1237);
		result = prime * result + (this.userAgent == null ? 0 : this.userAgent.hashCode());
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Site other = (Site) obj;
		if (this.acceptStatCode == null) {
			if (other.acceptStatCode != null) {
				return false;
			}
		} else if (!this.acceptStatCode.equals(other.acceptStatCode)) {
			return false;
		}
		if (this.charset == null) {
			if (other.charset != null) {
				return false;
			}
		} else if (!this.charset.equals(other.charset)) {
			return false;
		}
		if (this.cookies == null) {
			if (other.cookies != null) {
				return false;
			}
		} else if (!this.cookies.equals(other.cookies)) {
			return false;
		}
		if (this.defaultCookies == null) {
			if (other.defaultCookies != null) {
				return false;
			}
		} else if (!this.defaultCookies.equals(other.defaultCookies)) {
			return false;
		}
		if (this.domain == null) {
			if (other.domain != null) {
				return false;
			}
		} else if (!this.domain.equals(other.domain)) {
			return false;
		}
		if (this.headers == null) {
			if (other.headers != null) {
				return false;
			}
		} else if (!this.headers.equals(other.headers)) {
			return false;
		}
		if (this.httpProxy == null) {
			if (other.httpProxy != null) {
				return false;
			}
		} else if (!this.httpProxy.equals(other.httpProxy)) {
			return false;
		}
		if (this.retryTimes != other.retryTimes) {
			return false;
		}
		if (this.sleepTime != other.sleepTime) {
			return false;
		}
		if (this.startRequests == null) {
			if (other.startRequests != null) {
				return false;
			}
		} else if (!this.startRequests.equals(other.startRequests)) {
			return false;
		}
		if (this.timeOut != other.timeOut) {
			return false;
		}
		if (this.useGzip != other.useGzip) {
			return false;
		}
		if (this.userAgent == null) {
			if (other.userAgent != null) {
				return false;
			}
		} else if (!this.userAgent.equals(other.userAgent)) {
			return false;
		}
		return true;
	}
}
