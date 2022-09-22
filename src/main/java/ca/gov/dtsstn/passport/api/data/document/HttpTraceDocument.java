package ca.gov.dtsstn.passport.api.data.document;

import java.io.Serializable;
import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.immutables.value.Value.Immutable;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.Nullable;

/**
 * MongoDB document representing an HTTP trace (request/response and other related data).
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Immutable
@TypeAlias("HttpTrace")
@Document("httpTraces")
public interface HttpTraceDocument extends Serializable {

	@Id
	@Nullable
	String getId();

	@Nullable
	Instant getTimestamp();

	@Nullable
	String getPrincipalName();

	@Nullable
	String getSessionId();

	@Nullable
	String getRequestMethod();

	@Nullable
	URI getRequestUri();

	@Nullable
	Map<String, List<String>> getRequestHeaders();

	@Nullable
	String getRequestRemoteAddress();

	@Nullable
	Integer getResponseStatus();

	@Nullable
	Map<String, List<String>> getResponseHeaders();

	@Nullable
	Long getTimeTakenMillis();

}
