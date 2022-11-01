package ca.gov.dtsstn.passport.api.config.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.caffeine.CaffeineCache;

/**
 * Tests for {@link CaffeineCacheFactory}.
 * <p>
 * Note that since we can't easily access the generated cache, this
 * test only asserts that we can build the cache without encountering
 * exceptions. Also, some properties are mutually exclusive, so there
 * are two test variants to cover these cases.
 *
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class CaffeineCacheFactoryTest {

	CaffeineCacheFactory caffeineCacheFactory;

	@BeforeEach
	void beforeEach() {
		this.caffeineCacheFactory = new CaffeineCacheFactory("cache");
	}

	@Test
	void testGetObject_DefaultValues() {
		assertDoesNotThrow(() -> caffeineCacheFactory.getObject());
	}

	@Test
	void testGetObject_NonDefaultValues_VariantA() {
		caffeineCacheFactory.setExpireAfterAccess(1024L);
		caffeineCacheFactory.setExpireAfterWrite(1024L);
		caffeineCacheFactory.setInitialCapacity(1024);
		caffeineCacheFactory.setMaximumSize(1024);
		caffeineCacheFactory.setRecordStats(false);
		caffeineCacheFactory.setTimeUnit(TimeUnit.MILLISECONDS);
		assertDoesNotThrow(() -> caffeineCacheFactory.getObject());
	}

	@Test
	void testGetObjectType() {
		assertThat(caffeineCacheFactory.getObjectType()).isEqualTo(CaffeineCache.class);
	}

	@Test
	void testIsEagerInit() {
		assertThat(caffeineCacheFactory.isEagerInit()).isTrue();
	}

}
