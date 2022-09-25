package ca.gov.dtsstn.passport.api.data;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;
import org.springframework.data.mongodb.repository.support.SimpleMongoRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Transactional(readOnly = true)
public class ExtendedMongoRepositoryImpl<T, ID> extends SimpleMongoRepository<T, ID> implements ExtendedMongoRepository<T, ID> { // NOSONAR

	public ExtendedMongoRepositoryImpl(MongoEntityInformation<T, ID> metadata, MongoOperations mongoOperations) {
		super(metadata, mongoOperations);
	}

	@Override
	public <S extends T> Page<S> findAllCaseInsensitive(S probe, Pageable pageable) {
		final var exampleMatcher = ExampleMatcher.matching().withStringMatcher(StringMatcher.REGEX).withIgnoreCase();
		return findAll(Example.of(probe, exampleMatcher), pageable);
	}

}
