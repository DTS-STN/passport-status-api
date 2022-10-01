package ca.gov.dtsstn.passport.api.data.document;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.immutables.builder.Builder;
import org.springframework.core.style.ToStringCreator;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

/**
 * MongoDB document representing an HTTP trace (request/response and other related data).
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@TypeAlias("HttpTrace")
@Document("http-traces")
@SuppressWarnings({ "serial" })
public class HttpTraceDocument extends AbstractDocument {

	private String principalName;

	private Map<String, List<String>> requestHeaders;

	private String requestMethod;

	private String requestRemoteAddress;

	private URI requestUri;

	private Map<String, List<String>> responseHeaders;

	private Integer responseStatus;

	private String sessionId;

	private Instant timestamp;

	private Long timeTakenMillis;

	public HttpTraceDocument() {
		super();
	}

	@Builder.Constructor
	protected HttpTraceDocument( // NOSONAR
			@Nullable String id,
			@Nullable String createdBy,
			@Nullable Instant createdDate,
			@Nullable String lastModifiedBy,
			@Nullable Instant lastModifiedDate,
			@Nullable Long version,
			@Nullable Instant timestamp,
			@Nullable String principalName,
			@Nullable String sessionId,
			@Nullable String requestMethod,
			@Nullable URI requestUri,
			@Nullable Map<String, List<String>> requestHeaders,
			@Nullable String requestRemoteAddress,
			@Nullable Integer responseStatus,
			@Nullable Map<String, List<String>> responseHeaders,
			@Nullable Long timeTakenMillis) {
		super(id, createdBy, createdDate, lastModifiedBy, lastModifiedDate, version);
		this.timestamp = timestamp;
		this.principalName = principalName;
		this.requestHeaders = requestHeaders;
		this.requestMethod = requestMethod;
		this.requestRemoteAddress = requestRemoteAddress;
		this.requestUri = requestUri;
		this.responseHeaders = responseHeaders;
		this.responseStatus = responseStatus;
		this.sessionId = sessionId;
		this.timeTakenMillis = timeTakenMillis;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public Map<String, List<String>> getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(Map<String, List<String>> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	public String getRequestRemoteAddress() {
		return requestRemoteAddress;
	}

	public void setRequestRemoteAddress(String requestRemoteAddress) {
		this.requestRemoteAddress = requestRemoteAddress;
	}

	public URI getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(URI requestUri) {
		this.requestUri = requestUri;
	}

	public Map<String, List<String>> getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public Integer getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(Integer responseStatus) {
		this.responseStatus = responseStatus;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public Long getTimeTakenMillis() {
		return timeTakenMillis;
	}

	public void setTimeTakenMillis(Long timeTakenMillis) {
		this.timeTakenMillis = timeTakenMillis;
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
			.append("super", super.toString())
			.append("principalName", principalName)
			.append("requestHeaders", requestHeaders)
			.append("requestMethod", requestMethod)
			.append("requestRemoteAddress", requestRemoteAddress)
			.append("requestUri", requestUri)
			.append("responseHeaders", responseHeaders)
			.append("responseStatus", responseStatus)
			.append("sessionId", sessionId)
			.append("timestamp", timestamp)
			.append("timeTakenMillis", timeTakenMillis)
			.toString();
	}

}
