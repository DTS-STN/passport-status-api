package ca.gov.dtsstn.passport.api.data;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.UUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 * @since 0.0.0
 */
@ExtendWith({ MockitoExtension.class })
class UuidGeneratorTests {

	@Mock UUIDGenerator uuidGeneratorDelegate;

	UuidGenerator uuidGenerator;

	final UUID id = UUID.randomUUID();

	@BeforeEach
	void setUp() {
		this.uuidGenerator = new UuidGenerator(uuidGeneratorDelegate);
	}

	@Test
	void testConfigureCallsDelegate() {
		uuidGenerator.configure(null, null, null);
		verify(uuidGeneratorDelegate).configure(null, null, null);
	}

	@Test
	void testGenerate_withId() {
		final SharedSessionContractImplementor sharedSessionContractImplementor = mock(SharedSessionContractImplementor.class, Mockito.RETURNS_DEEP_STUBS);
		when(sharedSessionContractImplementor.getEntityPersister(any(), any()).getClassMetadata().getIdentifier(any(), any(SharedSessionContractImplementor.class))).thenReturn(id.toString());
		assertThat(uuidGenerator.generate(sharedSessionContractImplementor, new Object())).asString().isEqualTo(id.toString());
	}

	@Test
	void testGenerate_withNullId() {
		final SharedSessionContractImplementor sharedSessionContractImplementor = mock(SharedSessionContractImplementor.class, Mockito.RETURNS_DEEP_STUBS);
		when(sharedSessionContractImplementor.getEntityPersister(any(), any()).getClassMetadata().getIdentifier(any(), any(SharedSessionContractImplementor.class))).thenReturn(null);
		when(uuidGeneratorDelegate.generate(any(), any())).thenReturn(id);
		assertThat(uuidGenerator.generate(sharedSessionContractImplementor, new Object())).asString().isEqualTo(id.toString());
	}

}
