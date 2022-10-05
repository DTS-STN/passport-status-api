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

import ca.gov.dtsstn.passport.api.data.init.DatabaseInitializer;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Component
@Endpoint(id = "init-data")
@ConfigurationProperties("application.endpoint.init-data")
public class InitializeDatabaseEndpoint {

	private static final Logger log = LoggerFactory.getLogger(InitializeDatabaseEndpoint.class);

	private final DatabaseInitializer databaseInitializer;

	public InitializeDatabaseEndpoint(DatabaseInitializer databaseInitializer) {
		Assert.notNull(databaseInitializer, "databaseInitializer is required; it must not be null");
		this.databaseInitializer = databaseInitializer;
	}

	@WriteOperation
	public ResponseEntity<Map<String, String>> initializeData() {
		log.info("InitDataEndpoint called; invoking data initializer");
		databaseInitializer.initializeData();
		return ResponseEntity.accepted().body(Map.of("message", "Request accepted; initializing database", "timestamp", Instant.now().toString()));
	}

}
