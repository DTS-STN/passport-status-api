package ca.gov.dtsstn.passport.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;

import ca.gov.dtsstn.passport.api.config.properties.ApplicationProperties;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@SpringBootApplication
@EnableConfigurationProperties({ ApplicationProperties.class })
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	/**
	 * The application entry point. This is where it all begins.
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * An {@link EventListener} that prints some useful startup information.
	 */
	@EventListener({ ApplicationReadyEvent.class })
	protected void handleApplicationReadyEvent(ApplicationReadyEvent event) {
		final var environment = event.getApplicationContext().getEnvironment();
		final var applicationName = environment.getProperty("spring.application.name", "application");
		final var serverPort = environment.getProperty("server.port", "8080");
		final var contextPath = environment.getProperty("server.servlet.context-path", "/");

		log.info("===============================================================================");
		log.info("Successfully started {}...", applicationName);
		log.info("Local application URL: http://localhost:{}{}", serverPort, contextPath);
		log.info("===============================================================================");
	}

}
