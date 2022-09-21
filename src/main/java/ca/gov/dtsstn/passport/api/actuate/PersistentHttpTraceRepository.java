package ca.gov.dtsstn.passport.api.actuate;

import java.util.List;

import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

import ca.gov.dtsstn.passport.api.actuate.mapper.HttpTraceMapper;
import ca.gov.dtsstn.passport.api.data.HttpTraceRepository;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Repository
public class PersistentHttpTraceRepository implements org.springframework.boot.actuate.trace.http.HttpTraceRepository {

	private final HttpTraceRepository httpTraceRepository;

	private final HttpTraceMapper httpTraceMapper;

	public PersistentHttpTraceRepository(HttpTraceMapper httpTraceMapper, HttpTraceRepository httpTraceRepository) {
		this.httpTraceMapper = httpTraceMapper;
		this.httpTraceRepository = httpTraceRepository;
	}

	@Override
	public List<HttpTrace> findAll() {
		// TODO :: GjB :: make page size configurable
		final var pageRequest = PageRequest.of(0, 100, Sort.by(Direction.DESC,"timestamp"));
		return httpTraceRepository.findAll(pageRequest).map(httpTraceMapper::fromDocument).getContent();
	}

	@Override
	public void add(HttpTrace httpTrace) {
		httpTraceRepository.save(httpTraceMapper.toDocument(httpTrace));
	}

}
