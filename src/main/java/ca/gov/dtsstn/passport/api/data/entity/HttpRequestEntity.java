package ca.gov.dtsstn.passport.api.data.entity;

import java.time.Instant;

import org.immutables.builder.Builder;
import org.springframework.core.style.ToStringCreator;
import org.springframework.lang.Nullable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Entity(name = "HttpRequest")
@SuppressWarnings({ "serial" })
public class HttpRequestEntity extends AbstractEntity {

	@Column(length = 64, nullable = true)
	private String principalName;

	@Column(length = 65536, nullable = true)
	private String requestHeaders;

	@Column(length = 8, nullable = false)
	private String requestMethod;

	@Column(length = 64, nullable = true)
	private String requestRemoteAddress;

	@Column(length = 4096, nullable = false)
	private String requestUri;

	@Column(length = 65536, nullable = true)
	private String responseHeaders;

	@Column(nullable = false)
	private Integer responseStatus;

	@Column(length = 256, nullable = true)
	private String sessionId;

	@Column(nullable = false)
	private Instant timestamp;

	@Column(nullable = true)
	private Long timeTakenMillis;

	public HttpRequestEntity() {
		super();
	}

	@Builder.Constructor
	protected HttpRequestEntity( // NOSONAR (too many parameters)
			@Nullable String id,
			@Nullable String createdBy,
			@Nullable Instant createdDate,
			@Nullable String lastModifiedBy,
			@Nullable Instant lastModifiedDate,
			@Nullable Boolean isNew,
			@Nullable String principalName,
			@Nullable String requestHeaders,
			@Nullable String requestMethod,
			@Nullable String requestRemoteAddress,
			@Nullable String requestUri,
			@Nullable String responseHeaders,
			@Nullable Integer responseStatus,
			@Nullable String sessionId,
			@Nullable Instant timestamp,
			@Nullable Long timeTakenMillis) {
		super(id, createdBy, createdDate, lastModifiedBy, lastModifiedDate, isNew);
		this.principalName = principalName;
		this.requestHeaders = requestHeaders;
		this.requestMethod = requestMethod;
		this.requestRemoteAddress = requestRemoteAddress;
		this.requestUri = requestUri;
		this.responseHeaders = responseHeaders;
		this.responseStatus = responseStatus;
		this.sessionId = sessionId;
		this.timestamp = timestamp;
		this.timeTakenMillis = timeTakenMillis;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getRequestHeaders() {
		return requestHeaders;
	}

	public void setRequestHeaders(String requestHeaders) {
		this.requestHeaders = requestHeaders;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getRequestRemoteAddress() {
		return requestRemoteAddress;
	}

	public void setRequestRemoteAddress(String requestRemoteAddress) {
		this.requestRemoteAddress = requestRemoteAddress;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public String getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(String responseHeaders) {
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
	public boolean equals(Object obj) {
		// keeps SonarLint happy
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		// keeps SonarLint happy
		return super.hashCode();
	}

	@Override
	public String toString() {
		return new ToStringCreator(this)
			.append("super", super.toString())
			.append("id", id)
			.append("createdBy", createdBy)
			.append("createdDate", createdDate)
			.append("lastModifiedBy", lastModifiedBy)
			.append("lastModifiedDate", lastModifiedDate)
			.append("isNew", isNew)
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
