package ca.gov.dtsstn.passport.api.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.repository.query.MongoEntityInformation;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class ExtendedMongoRepositoryImplTests {

	ExtendedMongoRepositoryImpl<Object, String> extendedMongoRepositoryImpl;

	@SuppressWarnings({ "unchecked" })
	@BeforeEach void beforeEach() {
		final var metadata = mock(MongoEntityInformation.class);
		final var mongoOperations = mock(MongoOperations.class);

		this.extendedMongoRepositoryImpl = spy(new ExtendedMongoRepositoryImpl<Object, String>(metadata, mongoOperations));
	}

	@Test void testFindAllCaseInsensitive() {
		final var page = extendedMongoRepositoryImpl.findAllCaseInsensitive(new Object(), Pageable.unpaged());

		assertThat(page).isNotNull();
		verify(extendedMongoRepositoryImpl).findAll(ArgumentMatchers.<Example<Object>> any(), any(Pageable.class));
	}

}
