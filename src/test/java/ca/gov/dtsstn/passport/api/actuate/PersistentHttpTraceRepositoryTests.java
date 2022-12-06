package ca.gov.dtsstn.passport.api.actuate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.boot.actuate.web.exchanges.HttpExchange.Principal;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import ca.gov.dtsstn.passport.api.data.HttpRequestRepository;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@ExtendWith({ MockitoExtension.class })
class PersistentHttpTraceRepositoryTests {

	PersistentHttpTraceRepository persistentHttpTraceRepository;

	@Mock HttpRequestRepository httpRequestRepository;

	@Mock InMemoryHttpExchangeRepository inMemoryHttpExchangeRepository;

	@BeforeEach void beforeEach() {
		new PersistentHttpTraceRepository(httpRequestRepository); // just for coverage 💩
		this.persistentHttpTraceRepository = new PersistentHttpTraceRepository(httpRequestRepository, inMemoryHttpExchangeRepository);
	}

	@Test void testAdd() {
		final var request = new HttpExchange.Request(URI.create("https://example.com/"), "127.0.0.1", "GET", new HttpHeaders());
		final var response = new HttpExchange.Response(HttpStatus.OK.value(), new HttpHeaders());
		final var timestamp = LocalDate.of(2000, 01, 01).atStartOfDay(ZoneOffset.UTC).toInstant();
		final var principal = new Principal("user");
		final var session = new HttpExchange.Session("00000000-0000-0000-0000-000000000000");
		final var timeTaken = Duration.ofMinutes(1);

		persistentHttpTraceRepository.add(new HttpExchange(timestamp, request, response, principal, session, timeTaken));

		verify(httpRequestRepository).save(any());
		verify(inMemoryHttpExchangeRepository).add(any());
	}

	@Test void testFindAll() {
		when(inMemoryHttpExchangeRepository.findAll()).thenReturn(List.of());

		final var httpTraces = persistentHttpTraceRepository.findAll();

		assertThat(httpTraces).isNotNull();
		verify(inMemoryHttpExchangeRepository).findAll();
	}

	@Test void testSetCapacity() {
		assertThatNoException().isThrownBy(() -> persistentHttpTraceRepository.setCapacity(0));
		assertThatIllegalArgumentException().isThrownBy(() -> persistentHttpTraceRepository.setCapacity(-1));
	}

}
