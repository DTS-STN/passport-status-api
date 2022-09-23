package ca.gov.dtsstn.passport.api.actuate;

import java.util.List;

import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.actuate.mapper.HttpTraceMapper;
import ca.gov.dtsstn.passport.api.data.HttpTraceRepository;

/**
 * A persistent implementation of {@link org.springframework.boot.actuate.trace.http.HttpTraceRepository}.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Repository
@ConfigurationProperties("application.http-trace-repository")
public class PersistentHttpTraceRepository implements org.springframework.boot.actuate.trace.http.HttpTraceRepository {

	private final HttpTraceRepository httpTraceRepository;

	private final HttpTraceMapper httpTraceMapper;

	private int pageSize = 100;

	public PersistentHttpTraceRepository(HttpTraceMapper httpTraceMapper, HttpTraceRepository httpTraceRepository) {
		this.httpTraceMapper = httpTraceMapper;
		this.httpTraceRepository = httpTraceRepository;
	}

	@Override
	public List<HttpTrace> findAll() {
		final var pageRequest = PageRequest.of(0, pageSize, Sort.by(Direction.DESC,"timestamp"));
		return httpTraceRepository.findAll(pageRequest).map(httpTraceMapper::fromDocument).getContent();
	}

	@Override
	public void add(HttpTrace httpTrace) {
		httpTraceRepository.save(httpTraceMapper.toDocument(httpTrace));
	}

	public void setPageSize(int pageSize) {
		Assert.isTrue(pageSize >= 0, "application.http-trace-repository.page-size must be greater than or equal to zero");
		this.pageSize = pageSize;
	}

}
