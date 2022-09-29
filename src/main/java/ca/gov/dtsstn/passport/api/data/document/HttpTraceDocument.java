package ca.gov.dtsstn.passport.api.data.document;

import java.io.Serializable;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.immutables.builder.Builder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

/**
 * MongoDB document representing an HTTP trace (request/response and other related data).
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Document("httpTraces")
@TypeAlias("HttpTrace")
@SuppressWarnings({ "serial" })
public class HttpTraceDocument implements Serializable {

	@Id
	private String id;

	@CreatedBy
	private String createdBy;

	@CreatedDate
	private Instant createdDate;

	@LastModifiedBy
	private String lastModifiedBy;

	@LastModifiedDate
	private Instant lastModifiedDate;

	@Version
	private Long version;

	private Instant timestamp;

	private String principalName;

	private String sessionId;

	private String requestMethod;

	private URI requestUri;

	private Map<String, List<String>> requestHeaders;

	private String requestRemoteAddress;

	private Integer responseStatus;

	private Map<String, List<String>> responseHeaders;

	private Long timeTakenMillis;

	public HttpTraceDocument() {
		/* required by MongoDB */
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
		this.id = id;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.version = version;
		this.timestamp = timestamp;
		this.principalName = principalName;
		this.sessionId = sessionId;
		this.requestMethod = requestMethod;
		this.requestUri = requestUri;
		this.requestHeaders = requestHeaders;
		this.requestRemoteAddress = requestRemoteAddress;
		this.responseStatus = responseStatus;
		this.responseHeaders = responseHeaders;
		this.timeTakenMillis = timeTakenMillis;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Instant getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Instant createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Instant getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Instant lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}

	public URI getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(URI requestUri) {
		this.requestUri = requestUri;
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

	public Integer getResponseStatus() {
		return responseStatus;
	}

	public void setResponseStatus(Integer responseStatus) {
		this.responseStatus = responseStatus;
	}

	public Map<String, List<String>> getResponseHeaders() {
		return responseHeaders;
	}

	public void setResponseHeaders(Map<String, List<String>> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}

	public Long getTimeTakenMillis() {
		return timeTakenMillis;
	}

	public void setTimeTakenMillis(Long timeTakenMillis) {
		this.timeTakenMillis = timeTakenMillis;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }

		final var other = (HttpTraceDocument) obj;

		return Objects.equals(id, other.id);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(id);
	}

}
