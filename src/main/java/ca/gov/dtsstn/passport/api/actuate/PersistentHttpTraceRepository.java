package ca.gov.dtsstn.passport.api.actuate;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
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
public class PersistentHttpTraceRepository extends InMemoryHttpTraceRepository {

	private final HttpTraceRepository httpTraceRepository;

	private final HttpTraceMapper httpTraceMapper;

	public PersistentHttpTraceRepository(HttpTraceMapper httpTraceMapper, HttpTraceRepository httpTraceRepository) {
		Assert.notNull(httpTraceMapper, "httpTraceMapper is required; it must not be null");
		Assert.notNull(httpTraceRepository, "httpTraceRepository is required; it must not be null");
		this.httpTraceMapper = httpTraceMapper;
		this.httpTraceRepository = httpTraceRepository;
	}

	@Override
	@SuppressWarnings({ "java:S4449" })
	public void add(HttpTrace httpTrace) {
		super.add(httpTrace);
		httpTraceRepository.save(httpTraceMapper.toDocument(httpTrace));
	}

	@Override
	public void setCapacity(int capacity) {
		Assert.isTrue(capacity >= 0, "application.http-trace-repository.capacity must be greater than or equal to zero");
		super.setCapacity(capacity);
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
		@Mapping(target = "putRequestHeaders", ignore = true)
		@Mapping(target = "putAllRequestHeaders", ignore = true)
		@Mapping(target = "responseStatus", source = "response.status")
		@Mapping(target = "responseHeaders", source = "response.headers")
		@Mapping(target = "timeTakenMillis", source = "timeTaken")
		// for some reason, MapStruct thinks these are target fields 🤷
		@Mapping(target = "putResponseHeaders", ignore = true)
		@Mapping(target = "putAllResponseHeaders", ignore = true)
		HttpTraceDocument toDocument(@Nullable HttpTrace httpTrace);

	}

}
