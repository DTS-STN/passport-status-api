package ca.gov.dtsstn.passport.api.actuate;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.gov.dtsstn.passport.api.data.HttpRequestRepository;
import ca.gov.dtsstn.passport.api.data.entity.HttpRequestEntity;

/**
 * A persistent implementation of {@link HttpTraceRepository}.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Repository
@ConfigurationProperties("application.http-request-repository")
public class PersistentHttpTraceRepository implements HttpTraceRepository {

	private final HttpRequestRepository httpRequestRepository;

	private final HttpTraceMapper httpTraceMapper = Mappers.getMapper(HttpTraceMapper.class);

	private final InMemoryHttpTraceRepository inMemoryHttpTraceRepository;

	@Autowired // marks this constructor as the primary one spring will use
	public PersistentHttpTraceRepository(HttpRequestRepository httpRequestRepository) {
		this(httpRequestRepository, new InMemoryHttpTraceRepository());
	}

	public PersistentHttpTraceRepository(HttpRequestRepository httpRequestRepository, InMemoryHttpTraceRepository inMemoryHttpTraceRepository) {
		Assert.notNull(httpRequestRepository, "httpRequestRepository is required; it must not be null");
		Assert.notNull(inMemoryHttpTraceRepository, "inMemoryHttpTraceRepository is required; it must not be null");

		this.httpRequestRepository = httpRequestRepository;
		this.inMemoryHttpTraceRepository = inMemoryHttpTraceRepository;
	}

	@Override
	public void add(HttpTrace httpTrace) {
		Assert.notNull(httpTrace, "httpTrace is required; it must not be null");
		inMemoryHttpTraceRepository.add(httpTrace);
		Optional.ofNullable(httpTraceMapper.toEntity(httpTrace)).ifPresent(httpRequestRepository::save);
	}

	@Override
	public List<HttpTrace> findAll() {
		return inMemoryHttpTraceRepository.findAll();
	}

	public void setCapacity(int capacity) {
		Assert.isTrue(capacity >= 0, "application.http-request-repository.capacity must be greater than or equal to zero");
		inMemoryHttpTraceRepository.setCapacity(capacity);
	}

	@Mapper
	abstract static class HttpTraceMapper {

		protected final ObjectMapper objectMapper = new ObjectMapper();

		@Nullable
		@Mapping(target = "id", ignore = true)
		@Mapping(target = "principalName", source = "principal.name")
		@Mapping(target = "sessionId", source = "session.id")
		@Mapping(target = "requestMethod", source = "request.method")
		@Mapping(target = "requestUri", source = "request.uri")
		@Mapping(target = "requestHeaders", source = "request.headers")
		@Mapping(target = "requestRemoteAddress", source = "request.remoteAddress")
		@Mapping(target = "responseStatus", source = "response.status")
		@Mapping(target = "responseHeaders", source = "response.headers")
		@Mapping(target = "timeTakenMillis", source = "timeTaken")
		@Mapping(target = "isNew", ignore = true)
		@Mapping(target = "createdBy", ignore = true)
		@Mapping(target = "createdDate", ignore = true)
		@Mapping(target = "lastModifiedBy", ignore = true)
		@Mapping(target = "lastModifiedDate", ignore = true)
		public abstract HttpRequestEntity toEntity(@Nullable HttpTrace httpTrace);

		@Nullable
		public String toString(@Nullable Map<String, List<String>> headers) throws JsonProcessingException {
			if (headers == null) { return null; }
			return objectMapper.writeValueAsString(headers);
		}

		@Nullable
		public String toString(@Nullable URI uri) {
			if (uri == null) { return null; }
			return uri.toString();
		}

	}

}
