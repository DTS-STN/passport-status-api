package ca.gov.dtsstn.passport.api.web;

import java.time.Instant;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import ca.gov.dtsstn.passport.api.data.DataInitializer;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
@Endpoint(id = "init-data")
@ConfigurationProperties("application.endpoint.init-data")
public class InitDataEndpoint {

	private static final Logger log = LoggerFactory.getLogger(InitDataEndpoint.class);

	private final DataInitializer dataInitializer;

	public InitDataEndpoint(DataInitializer dataInitializer) {
		Assert.notNull(dataInitializer, "dataInitializer is required; it must not be null");
		this.dataInitializer = dataInitializer;
	}

	@WriteOperation
	public ResponseEntity<Map<String, ?>> initializeData() {
		log.info("InitDataEndpoint called; invoking data initializer");
		dataInitializer.initializeData();
		return ResponseEntity.accepted().body(Map.of("message", "Request accepted; initializing database", "timestamp", Instant.now()));
	}

}
