package ca.gov.dtsstn.passport.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	/**
	 * The application entry point. This is where it all begins.
	 */
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	/**
	 * An {@link ApplicationListener} that prints some useful startup information.
	 */
	@Bean ApplicationListener<ApplicationStartedEvent> startupListener(Environment environment) {
		return event -> {
			final String applicationName = environment.getProperty("spring.application.name", "application");
			final String serverPort = environment.getProperty("server.port", "8080");
			final String contextPath = environment.getProperty("server.servlet.context-path", "/");

			log.info("===============================================================================");
			log.info("Successfully started {}…", applicationName);
			log.info("	⏩ Local application URL: http://localhost:{}{} ⏪", serverPort, contextPath);
			log.info("===============================================================================");
		};
	}

}
