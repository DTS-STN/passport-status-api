package ca.gov.dtsstn.passport.api.actuate;

import java.util.List;
import java.util.Optional;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.HttpTraceRepository;
import ca.gov.dtsstn.passport.api.data.document.HttpTraceDocument;

/**
 * A persistent implementation of {@link org.springframework.boot.actuate.trace.http.HttpTraceRepository}.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Repository
@ConfigurationProperties("application.http-trace-repository")
public class PersistentHttpTraceRepository implements org.springframework.boot.actuate.trace.http.HttpTraceRepository {

	private final HttpTraceRepository httpTraceRepository;

	private final HttpTraceMapper httpTraceMapper = Mappers.getMapper(HttpTraceMapper.class);

	private final InMemoryHttpTraceRepository inMemoryHttpTraceRepository = new InMemoryHttpTraceRepository();

	public PersistentHttpTraceRepository(HttpTraceRepository httpTraceRepository) {
		Assert.notNull(httpTraceRepository, "httpTraceRepository is required; it must not be null");
		this.httpTraceRepository = httpTraceRepository;
	}

	@Override
	public void add(HttpTrace httpTrace) {
		inMemoryHttpTraceRepository.add(httpTrace);
		Optional.ofNullable(httpTrace)
			.map(httpTraceMapper::toDocument)
			.ifPresent(httpTraceRepository::save);
	}

	@Override
	public List<HttpTrace> findAll() {
		return inMemoryHttpTraceRepository.findAll();
	}

	public void setCapacity(int capacity) {
		Assert.isTrue(capacity >= 0, "application.http-trace-repository.capacity must be greater than or equal to zero");
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
		HttpTraceDocument toDocument(@Nullable HttpTrace httpTrace);

	}

}
