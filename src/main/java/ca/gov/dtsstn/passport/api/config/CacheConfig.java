package ca.gov.dtsstn.passport.api.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ca.gov.dtsstn.passport.api.config.cache.CaffeineCacheFactory;

/**
 * @author Greg Baker (gregory.j.baker@hrsdc-rhdcc.gc.ca)
 */
@EnableCaching
@Configuration
@ConditionalOnProperty(name = { "application.caching.enabled" }, matchIfMissing = true)
public class CacheConfig {

	private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

	@ConfigurationProperties("application.caching.caches.status-codes")
	@Bean CaffeineCacheFactory statusCodesCache() {
		log.info("Creating 'statusCodesCache' bean");
		return new CaffeineCacheFactory("status-codes");
	}

}
