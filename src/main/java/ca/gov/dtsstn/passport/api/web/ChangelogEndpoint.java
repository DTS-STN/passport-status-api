package ca.gov.dtsstn.passport.api.web;

import java.io.IOException;
import java.io.Serializable;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
@Endpoint(id = "changelog")
@ConfigurationProperties("application.endpoint.changelog")
public class ChangelogEndpoint {

	protected final ObjectMapper objectMapper;

	protected String changelogPath = "changelog.json";

	public ChangelogEndpoint(ObjectMapper objectMapper) {
		Assert.notNull(objectMapper, "objectMapper is required; it must not be null");
		this.objectMapper = objectMapper;
	}

	@ReadOperation
	public ResponseEntity<?> getChangelog(@Nullable Integer size, @Nullable Boolean simple) throws IOException {
		final var gitLogs = List.of(objectMapper.readValue(new ClassPathResource(changelogPath).getInputStream(), GitLog[].class));
		final var maxSize = (size != null) ? size : Integer.MAX_VALUE;
		return Boolean.TRUE.equals(simple) ? handleSimpleResponse(gitLogs, maxSize) : handleFullResponse(gitLogs, maxSize);
	}

	protected ResponseEntity<List<GitLog>> handleFullResponse(List<GitLog> gitLogs, int maxSize) {
		final var body = gitLogs.stream().limit(maxSize).toList();
		return ResponseEntity.ok().body(body);
	}

	protected ResponseEntity<String> handleSimpleResponse(Collection<GitLog> gitLogs, int maxSize) {
		final var body = gitLogs.stream().limit(maxSize).map(GitLog::toString).collect(Collectors.joining("\n"));
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "text/plain;charset=UTF-8").body(body);
	}

	public void setChangelogPath(String changelogPath) {
		Assert.hasText(changelogPath, "changelogPath is required; it must not be blank or null");
		this.changelogPath = changelogPath;
	}

	@SuppressWarnings({ "serial" })
	@JsonIgnoreProperties({ "tags" })
	public static final class GitLog implements Serializable {

		public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss XX";

		private final String id;

		private final String message;

		private final String authorName;

		private final String authorEmail;

		private final String committerName;

		private final String committerEmail;

		private final Instant date;

		@JsonCreator
		public GitLog(
				@JsonProperty("id") String id,
				@JsonProperty("message") String message,
				@JsonProperty("authorName") String authorName,
				@JsonProperty("authorEmail") String authorEmail,
				@JsonProperty("committerName") String committerName,
				@JsonProperty("committerEmail") String committerEmail,
				@JsonProperty("date") String date) {
			this.id = id;
			this.message = message;
			this.authorName = authorName;
			this.authorEmail = authorEmail;
			this.committerName = committerName;
			this.committerEmail = committerEmail;
			this.date = Instant.from(DateTimeFormatter.ofPattern(DATE_FORMAT).parse(date));
		}

		public String getId() {
			return id;
		}

		public String getMessage() {
			return message;
		}

		public String getAuthorName() {
			return authorName;
		}

		public String getAuthorEmail() {
			return authorEmail;
		}

		public String getCommitterName() {
			return committerName;
		}

		public String getCommitterEmail() {
			return committerEmail;
		}

		public Instant getDate() {
			return date;
		}

		@Override
		public String toString() {
			return "%s    %s %s (%s)".formatted(date, id.substring(0, 8), message, authorName);
		}

	}

}
