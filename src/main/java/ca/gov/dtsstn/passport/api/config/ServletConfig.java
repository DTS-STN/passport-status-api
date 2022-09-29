package ca.gov.dtsstn.passport.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@Configuration
public class ServletConfig {

	private static final Logger log = LoggerFactory.getLogger(ServletConfig.class);

	/**
	 * Adds etag response headers based on content hashing.
	 */
	@ConfigurationProperties("application.shallow-etag-header-filter")
	@Bean ShallowEtagHeaderFilter shallowEtagHeaderFilter() {
		log.info("Creating 'shallowEtagHeaderFilter' bean");
		return new ShallowEtagHeaderFilter();
	}

	/**
	 * A {@link FilterRegistrationBean} that ensures the
	 * {@link ShallowEtagHeaderFilter} is fired as early as possible in the filter
	 * chain.
	 */
	@Bean FilterRegistrationBean<ShallowEtagHeaderFilter> shallowEtagHeaderFilterRegistration() {
		log.info("Creating 'shallowEtagHeaderFilterRegistration' bean");
		final var shallowEtagHeaderFilter = shallowEtagHeaderFilter();
		final var filterRegistrationBean = new FilterRegistrationBean<>(shallowEtagHeaderFilter);
		filterRegistrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
		return filterRegistrationBean;
	}

}
