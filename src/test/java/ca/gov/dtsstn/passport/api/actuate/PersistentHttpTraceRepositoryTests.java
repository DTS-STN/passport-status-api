package ca.gov.dtsstn.passport.api.actuate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneOffset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.HttpTrace.Principal;
import org.springframework.boot.actuate.trace.http.HttpTrace.Request;
import org.springframework.boot.actuate.trace.http.HttpTrace.Response;
import org.springframework.boot.actuate.trace.http.HttpTrace.Session;
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

	@BeforeEach void beforeEach() {
		this.persistentHttpTraceRepository = new PersistentHttpTraceRepository(httpRequestRepository);
	}

	@Test void testAdd() {
		final var request = new Request("GET", URI.create("https://example.com/"), new HttpHeaders(), "127.0.0.1");
		final var response = new Response(HttpStatus.OK.value(), new HttpHeaders());
		final var timestamp = LocalDate.of(2000, 01, 01).atStartOfDay(ZoneOffset.UTC).toInstant();
		final var principal = new Principal("user");
		final var session = new Session("00000000-0000-0000-0000-000000000000");
		final var timeTaken = Duration.ofMinutes(1).toMillis();

		persistentHttpTraceRepository.add(new HttpTrace(request, response, timestamp, principal, session, timeTaken));

		verify(httpRequestRepository).save(any());
	}

}
