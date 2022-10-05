package ca.gov.dtsstn.passport.api.actuate;

import java.util.List;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTraceRepository;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.HttpRequestRepository;
import ca.gov.dtsstn.passport.api.data.document.HttpRequestDocument;

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

	private final InMemoryHttpTraceRepository inMemoryHttpTraceRepository = new InMemoryHttpTraceRepository();

	public PersistentHttpTraceRepository(HttpRequestRepository httpRequestRepository) {
		Assert.notNull(httpRequestRepository, "httpRequestRepository is required; it must not be null");
		this.httpRequestRepository = httpRequestRepository;
	}

	@Override
	public void add(HttpTrace httpTrace) {
		inMemoryHttpTraceRepository.add(httpTrace);
		Optional.ofNullable(httpTrace)
			.map(httpTraceMapper::toDocument)
			.ifPresent(httpRequestRepository::save);
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
	interface HttpTraceMapper {

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
		@Mapping(target = "createdBy", ignore = true)
		@Mapping(target = "createdDate", ignore = true)
		@Mapping(target = "lastModifiedBy", ignore = true)
		@Mapping(target = "lastModifiedDate", ignore = true)
		@Mapping(target = "version", ignore = true)
		HttpRequestDocument toDocument(@Nullable HttpTrace httpTrace);

	}

}
