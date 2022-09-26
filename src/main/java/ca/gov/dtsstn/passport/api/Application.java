package ca.gov.dtsstn.passport.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.SpringDocUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import ca.gov.dtsstn.passport.api.web.model.ImmutablePassportStatusSearchModel;
import ca.gov.dtsstn.passport.api.web.model.PassportStatusSearchModel;

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
		log.info("Configuring SpringDoc parameter objects");
		// Note: this is required for SpringDoc to corrently document PassportStatusSearchModel as a parameter object
		// TODO :: GjB :: find a better place for this
		SpringDocUtils.getConfig().replaceParameterObjectWithClass(PassportStatusSearchModel.class, ImmutablePassportStatusSearchModel.class);

		SpringApplication.run(Application.class, args);
	}

	/**
	 * An {@link ApplicationListener} that prints some useful startup information.
	 */
	@Bean ApplicationListener<ContextRefreshedEvent> applicationStartupListener(Environment environment) {
		log.info("Creating 'applicationStartupListener' bean");

		return event -> {
			final var applicationName = environment.getProperty("spring.application.name", "application");
			final var serverPort = environment.getProperty("server.port", "8080");
			final var contextPath = environment.getProperty("server.servlet.context-path", "/");

			log.info("===============================================================================");
			log.info("Successfully started {}", applicationName);
			log.info("	Local application URL: http://localhost:{}{}", serverPort, contextPath); // NOSONAR
			log.info("===============================================================================");
		};
	}

}
